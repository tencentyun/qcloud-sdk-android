/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.transfer;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;

import com.tencent.cos.xml.BeaconService;
import com.tencent.cos.xml.model.CosXmlResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 传输任务状态监听器
 */
final class TaskStateMonitor implements Runnable{
    private static final String TAG = "TaskStateMonitor";

    private static TaskStateMonitor monitor;
    private static Handler taskHandler;
    private Looper looper;
    private ExecutorService executorService;
    private volatile boolean isRunning = false;

    public static final int MESSAGE_TASK_RESULT = 1;
    public static final int MESSAGE_TASK_MANUAL = 2;
    public static final int MESSAGE_RELEASE_LOOP = 3;
    public static final int MESSAGE_TASK_INIT = 4;
    public static final int MESSAGE_TASK_CONSTRAINT = 5;

    private TaskStateMonitor(){
        executorService = Executors.newSingleThreadExecutor();
    }

    public static TaskStateMonitor getInstance(){
        synchronized (TaskStateMonitor.class){
            if(monitor == null){
                monitor = new TaskStateMonitor();
            }
            monitor.monitor();
        }
        return monitor;
    }

    private void monitor(){
        if(isRunning)return;
        executorService.submit(this);
        isRunning = true;
    }


    @Override
    public void run() {
        //消息循环
        synchronized (this){
            looper = Looper.myLooper(); //表示当前线程已存在
            if(looper != null){
                notifyAll();
            }
        }
        if(looper == null){
            Looper.prepare();
            synchronized (this){
                looper = Looper.myLooper();
                notifyAll();
            }
        }
        //设置消息队列
        try {
            setMessageQueue();
        } catch (NoSuchFieldException e) {
            setMessageQueueException(e);
        } catch (IllegalAccessException e) {
            setMessageQueueException(e);
        } catch (InvocationTargetException e) {
            setMessageQueueException(e);
        } catch (InstantiationException e) {
            setMessageQueueException(e);
        } catch (ClassNotFoundException e) {
            setMessageQueueException(e);
        }
        taskHandler = new Handler(getLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MESSAGE_TASK_RESULT:
                    case MESSAGE_TASK_CONSTRAINT:
                        StructMsg structMsg = (StructMsg) msg.obj;
                        stateTransform(structMsg.cosxmlTask, structMsg.transferState, structMsg.exception, structMsg.result, false);
                        break;
                    case MESSAGE_TASK_MANUAL:
                        StructMsg manualMsg = (StructMsg) msg.obj;
                        stateTransform(manualMsg.cosxmlTask,manualMsg.transferState, manualMsg.exception, null, false);
                        break;
                    case MESSAGE_TASK_INIT:
                        StructMsg initMsg = (StructMsg) msg.obj;
                        stateTransform(initMsg.cosxmlTask,initMsg.transferState, initMsg.exception, initMsg.result, true);
                        break;
                    case MESSAGE_RELEASE_LOOP:
                        releaseLooper();
                        break;
                }
            }
        };
        Looper.loop();
    }

    private void setMessageQueueException(Exception e){
        e.printStackTrace();
        BeaconService.getInstance().reportError(TAG, e);
    }

    private void setMessageQueue() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException {
        Field messageQueueField = Looper.class.getDeclaredField("mQueue");
        messageQueueField.setAccessible(true);
        Class<MessageQueue> messageQueueClass = (Class<MessageQueue>) Class.forName("android.os.MessageQueue");
        Constructor<MessageQueue>[] messageQueueConstructor = (Constructor<MessageQueue>[]) messageQueueClass.getDeclaredConstructors();
        for(Constructor<MessageQueue> constructor : messageQueueConstructor){
            constructor.setAccessible(true);
            Class[] types = constructor.getParameterTypes();
            for(Class clazz : types){
                if(clazz.getName().equalsIgnoreCase("boolean")){
                    messageQueueField.set(looper, constructor.newInstance(true));
                    break;
                }
            }
        }
    }

    public Looper getLooper(){
        if (!Thread.currentThread().isAlive()) {
            return null;
        }

        // If the thread has been started, wait until the looper has been created.
        synchronized (this) {
            while (Thread.currentThread().isAlive() && looper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return looper;
    }

    /**
     * 退出
     */
    public void quitSafely(){
        taskHandler.removeCallbacksAndMessages(null);
        Looper looper = getLooper();
        if (looper != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                looper.quitSafely();
            }else {
                looper.quit();
            }
        }
        isRunning = false;
    }

    /**
     * 更新状态
     */
    protected void stateTransform(COSXMLTask cosxmlTask, TransferState newState, Exception exception, CosXmlResult result, boolean isInit){
        cosxmlTask.updateState(newState, exception, result, isInit);
    }

    /**
     * 发送状态消息
     */
    protected void sendStateMessage(COSXMLTask cosxmlTask, TransferState newState, Exception exception, CosXmlResult cosXmlResult, int what){
        if(taskHandler == null)return; //暂不处理
        Message message = taskHandler.obtainMessage();
        message.what = what;
        StructMsg structMsg = new StructMsg();
        structMsg.cosxmlTask = cosxmlTask;
        structMsg.transferState = newState;
        structMsg.exception = exception;
        structMsg.result = cosXmlResult;
        message.obj = structMsg;
        taskHandler.sendMessage(message);
    }

    private void releaseLooper(){
        quitSafely();
    }

    private class StructMsg{
        COSXMLTask cosxmlTask;
        volatile TransferState transferState;
        Exception exception;
        CosXmlResult result;
    }
}
