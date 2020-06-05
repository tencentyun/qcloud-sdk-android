package com.tencent.qcloud.quic.test;

import android.os.*;
import android.support.annotation.Nullable;
import okio.Okio;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static com.tencent.qcloud.quic.test.QuicNative.*;

public class QuicImpl implements NetworkCallback, java.util.concurrent.Callable<QuicResponse>{

    private QuicRequest quicRequest;
    private QuicResponse quicResponse;
    private ResultCallback resultCallback;
    private QuicException exception;

    private ConnectPool connectPool;
    private QuicNative quicHandle;

    private Looper looper;
    private @Nullable Handler handler;

    private boolean isHeader;

    private volatile boolean isOver = false;


    public QuicImpl(ConnectPool connectPool) {
        this.connectPool = connectPool;
    }

    public void setQuicRequest(QuicRequest quicRequest){
        this.quicRequest = quicRequest;
    }

    public void setQuicResponse(QuicResponse quicResponse){
        this.quicResponse = quicResponse;
    }

    public void setResultCallback(ResultCallback resultCallback){
        this.resultCallback = resultCallback;
    }

    /**
     * 成功建立链接
     * @param handleId
     * @param code
     */
    @Override
    public void onConnect(int handleId, int code) {
        handler.sendEmptyMessage(CONNECTED);
    }

    /**
     * 接收响应包
     * @param handleId
     * @param data
     * @param len
     */
    @Override
    public void onDataReceive(int handleId, byte[] data, int len) {
        Message message = handler.obtainMessage();
        message.what = RECEIVING;
        Bundle bundle = new Bundle();
        bundle.putByteArray("DATA", data);
        bundle.putInt("LEN", len);
        message.obj = bundle;
        handler.sendMessage(message);
    }

    /**
     * 响应包已接收完
     * @param handleId
     */
    @Override
    public void onCompleted(int handleId) {
        handler.sendEmptyMessage(COMPLETED);
    }

    /**
     * 底层链接 close
     * @param handleId
     * @param code
     * @param desc
     */
    @Override
    public void onClose(int handleId, int code, String desc) {
        if(isOver)return; //cancel by user
        Message message = handler.obtainMessage();
        message.what = SERVER_FAILED;
        QuicException quicException = new QuicException(String.format("Closed(%d, %s)", code, desc));
        message.obj = quicException;
        handler.sendMessage(message);
    }

    /**
     * 建立链接
     */
    private void buildConnect(){
        quicHandle.connect(quicRequest.host, quicRequest.ip, quicRequest.port, quicRequest.tcpPort);
    }

    /**
     * 发送数据
     */
    private void sendData(){
        try {
            //发送 header
            for(Map.Entry<String, String> header : quicRequest.headers.entrySet()){
                quicHandle.addHeader(header.getKey(), header.getValue());
            }
            //发送body
            if(quicRequest.inputStream != null){
                byte[] buffer = new byte[1024 * 4];
                long completed = 0L;
                int len;
                while (completed < quicRequest.contentLength){
                    len = quicRequest.inputStream.read(buffer, 0, buffer.length);
                    if(len == -1)break;
                    completed += len;
                    if(completed == quicRequest.contentLength){
                        quicHandle.sendRequest(buffer, len, true);
                    }else {
                        quicHandle.sendRequest(buffer, len, false);
                    }
                }
            }else {
                quicHandle.sendRequest(new byte[0], 0, true);
            }

        }catch (IOException e){
            //失败
            Message message = handler.obtainMessage();
            message.what = CLIENT_FAILED;
            message.obj = new QuicException(e);
            handler.sendMessage(message);
        }
    }

    /**
     * 解析头部字段
     * @param data
     * @param len
     * @return
     */
    private void parseResponseHeader(byte[] data, int len){
        try {
            QLog.d("headers==>%s", new String(data, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
        }
        if(quicResponse != null){
            ByteArrayInputStream byteArrayInputStream = null;
            BufferedReader bufferedReader = null;
            try {
                byteArrayInputStream = new ByteArrayInputStream(data, 0, len);
                bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
                String line = bufferedReader.readLine();
                while (line != null){
                    if(line.startsWith("HTTP/1.")){ // HTTP/1.1 200 OK
                        quicResponse.headers.put("STATE_LINE", line);
                    }else if(line.contains(":")){ // date: Wed, 13 Mar 2019 13:14:51 GMT
                        int index = line.indexOf(":");
                        String key = line.substring(0, index).trim();
                        String value = line.substring(index + 1).trim();
                        quicResponse.headers.put(key, value);
                        if(key.equalsIgnoreCase("content-length")){
                            quicResponse.setContentLength(Long.valueOf(value));
                        }
                    }
                    line = bufferedReader.readLine();
                }
            }catch (IOException e){
                // error
                Message message = handler.obtainMessage();
                message.what = CLIENT_FAILED;
                message.obj = new QuicException(e);
                handler.sendMessage(message);
            }
            finally {
                if(bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(byteArrayInputStream != null){
                    try {
                        byteArrayInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 接收body
     * @param data
     * @param len
     */
    private void receiveData(byte[] data, int len){
        try {
            if(quicResponse.outputStream != null){
                quicResponse.outputStream.write(data, 0, len);
                quicResponse.updateProgress(len);
            }
        }catch (IOException e){
          //失败
            Message message = handler.obtainMessage();
            message.what = CLIENT_FAILED;
            message.obj = new QuicException(e);
            handler.sendMessage(message);
        }

    }

    /**
     * 请求-响应 结束
     */
    private void finish(){
        try {
            if(quicResponse != null && quicResponse.outputStream != null){
                quicResponse.outputStream.flush();
                quicResponse.outputStream.close();
            }
        }catch (IOException e){
           //失败
            Message message = handler.obtainMessage();
            message.what = CLIENT_FAILED;
            message.obj = new QuicException(e);
            handler.sendMessage(message);
        }
    }

    /**
     * 取消链接
     * @return
     */
    private void cancelConnect(){
        quicHandle.cancelRequest();
    }

    @Override
    public QuicResponse call(){
        //获取connect： quicNative
        quicHandle = connectPool.getQuicNative(quicRequest.host, quicRequest.ip, quicRequest.port, quicRequest.tcpPort);
        quicHandle.setCallback(this);
        connectPool.setComplete(quicHandle, false);

        synchronized (this){
            looper = Looper.myLooper(); //表示当前线程已存在
            if(looper != null){
                notifyAll();
            }
        }
        if(looper == null){
            Looper.prepare(); //开始准备
            QLog.d("looper prepare");
            synchronized (this){
                looper = Looper.myLooper();
                notifyAll();
            }
        }

        //设置消息队列
        try {
            setMessageQueue();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        handler = new Handler(getLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case INIT:
                        buildConnect();
                        break;
                    case CONNECTED:
                        isHeader = true;
                        isOver = false;
                        sendData();
                        break;
                    case RECEIVING:
                        Bundle bundle = (Bundle) msg.obj;
                        if(isHeader){
                            parseResponseHeader(bundle.getByteArray("DATA"), bundle.getInt("LEN"));
                            isHeader = false;
                        }else {
                            receiveData(bundle.getByteArray("DATA"), bundle.getInt("LEN"));
                        }
                        break;
                    case COMPLETED:
                        isOver = true;
                        finish();
                        //cancelConnect();
                        connectPool.setComplete(quicHandle, true);
                        quitSafely();
                        break;
                    case SERVER_FAILED:
                        isOver = true;
                        exception = (QuicException) msg.obj;
                        handler.removeCallbacksAndMessages(null);
                        quitSafely();
                        break;
                    case CLIENT_FAILED:
                        isOver = true;
                        exception = (QuicException) msg.obj;
                        handler.removeCallbacksAndMessages(null);
                        //cancelConnect();
                        quitSafely();
                        break;
                }
            }
        };
        QLog.d("looper loop start");
        if(quicHandle.currentState == CONNECTED){
            handler.sendEmptyMessage(CONNECTED);
        }else {
            handler.sendEmptyMessage(INIT);
        }
        Looper.loop();//开始循环
        //循环结束
        QLog.d("looper loop end");
        if(resultCallback != null){
            if(exception != null){
                resultCallback.onFailure(quicRequest, exception);
            }else {
                resultCallback.onResponse(quicRequest, quicResponse);
            }
        }
        return quicResponse;
    }

    public QuicException getException(){
        return exception;
    }

    private Looper getLooper() {
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

    private void quitSafely() {
        Looper looper = getLooper();
        if (looper != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                looper.quitSafely();
            }else {
                looper.quit();
            }
        }
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

}
