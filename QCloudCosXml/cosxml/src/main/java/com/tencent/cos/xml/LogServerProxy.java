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

package com.tencent.cos.xml;

import android.content.Context;

import com.tencent.qcloud.core.logger.FileLogAdapter;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LogServerProxy {
    private static final String TAG = "LogServerProxy";
    private final String className = "com.tencent.qcloud.logutils.LogServer";
    private final String interfaceName = "com.tencent.qcloud.logutils.OnLogListener";
    private final String interfaceMethodName = "onLoad";
    private Object logServer;
    private Method destroyResourceMethod;
    private Method setOnLogListenerMethod;
    private Context applicationContext;
    private static LogServerProxy instance;
    private FileLogAdapter fileLogAdapter;

    private LogServerProxy(Context applicationContext, final FileLogAdapter fileLogAdapter){
        this.applicationContext = applicationContext;
        this.fileLogAdapter = fileLogAdapter;
        try {
            Class cls = Class.forName(className);
            Constructor constructor = cls.getConstructor(android.content.Context.class);
            if(constructor != null){
                logServer = constructor.newInstance(this.applicationContext);
            }
            destroyResourceMethod = cls.getDeclaredMethod("destroy");
            if(destroyResourceMethod != null){
                destroyResourceMethod.setAccessible(true);
            }

            Class interfaceCls = Class.forName(interfaceName);
            Object interfaceInstance = Proxy.newProxyInstance(LogServerProxy.class.getClassLoader(), new Class[]{interfaceCls}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if(interfaceMethodName.equals(method.getName())){
                        return fileLogAdapter.getLogFilesDesc(30);
                    }
                    return null;
                }
            });
            setOnLogListenerMethod = cls.getDeclaredMethod("setOnLogListener", interfaceCls);
            if(setOnLogListenerMethod != null){
                setOnLogListenerMethod.setAccessible(true);
                setOnLogListenerMethod.invoke(logServer, interfaceInstance);
            }
        } catch (ClassNotFoundException e) {
            QCloudLogger.d(TAG, className + " : not found");
        } catch (NoSuchMethodException e) {
            QCloudLogger.d(TAG, e.getMessage() + " : not found");
        } catch (InstantiationException e) {
            QCloudLogger.d(TAG, e.getMessage() + " : not found");
        } catch (IllegalAccessException e) {
            QCloudLogger.d(TAG, e.getMessage() + " : not found");
        } catch (InvocationTargetException e) {
            QCloudLogger.d(TAG, e.getMessage() + " : not found");
        }
    }


    public static void init(Context context, FileLogAdapter fileLogAdapter){
        synchronized (LogServerProxy.class){
            if(instance == null){
                instance = new LogServerProxy(context, fileLogAdapter);
            }
        }
    }

    public static LogServerProxy getInstance(){
        return instance;
    }

    public FileLogAdapter getFileLogAdapter(){
        return fileLogAdapter;
    }

    public void destroy(){
        if(logServer != null && destroyResourceMethod != null){
            try {
                destroyResourceMethod.invoke(logServer);
            } catch (IllegalAccessException e) {
                QCloudLogger.d(TAG, e.getMessage());
            } catch (InvocationTargetException e) {
                QCloudLogger.d(TAG, e.getMessage());
            }
        }
    }

}
