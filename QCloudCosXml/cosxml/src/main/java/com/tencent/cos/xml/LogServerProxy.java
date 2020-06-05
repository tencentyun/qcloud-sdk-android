package com.tencent.cos.xml;

import android.content.Context;

import com.tencent.qcloud.core.logger.FileLogAdapter;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by bradyxiao on 2018/10/25.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */

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
