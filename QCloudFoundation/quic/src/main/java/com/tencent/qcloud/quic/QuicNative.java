package com.tencent.qcloud.quic;

import android.util.Log;

import java.util.Locale;
import java.util.UUID;

public class QuicNative {

    protected int handleId = UUID.randomUUID().toString().hashCode();

    protected static final int INIT = 0;
    protected static final int CONNECTED = 1;
    protected static final int RECEIVING = 2;
    protected static final int COMPLETED = 3;
    //已cancel_request
    protected static final int SERVER_FAILED = 4;
    //第一版，cancel_request, 以后版本 不cancel_request 满足复用
    protected static final int CLIENT_FAILED = 5;

    protected String host;
    protected String ip;
    protected int port;
    protected int tcpPort;
    protected boolean isCompleted = false;
    protected long idleStartTime = Long.MAX_VALUE;
    protected int currentState = INIT; //取值：init, connected, SERVER_FAILED

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "{handleId:%s, host:%s, ip:%s, port:%d, tcpPort:%d, isComplete:%b, idleStartTime:%d, currentState:%s}",
                handleId, host, ip, port, tcpPort, isCompleted, idleStartTime, readableState(currentState));
    }

    static {
        System.loadLibrary("tquic");
        System.loadLibrary("costquic");
    }

    private NetworkCallback callback;

    public QuicNative(){}


    public void setCallback(NetworkCallback callback) {
        this.callback = callback;
    }

    @CallByNative
    private void onConnect(int code){

        // QLog.d("onConnect , code = %d", code);
        currentState = CONNECTED;
        if(callback != null)callback.onConnect(handleId, code);
    }

    @CallByNative
    private void onDataReceive(byte[] data, int len){

        // QLog.d("onDataReceive , data length = %d", len);
        if(callback != null)callback.onDataReceive(handleId, data, len);
    }

    @CallByNative
    private void onCompleted(int code){

        // QLog.d("onCompleted , code = %d", code);

        if(callback != null)callback.onCompleted(handleId, code);
    }

    @CallByNative
    private void onClose(int code, String desc){

        // QLog.d("onClose , code = %d, desc=%s", code, desc);
        currentState = SERVER_FAILED;
        if(callback != null)callback.onClose(handleId, code, desc);
    }


    public void connect(String host, String ip, int port, int tcpPort){

        // QLog.d("start connect host=%s, ip=%s, port=%d, tcpPort=%d", host, ip, port, tcpPort);
        connect(handleId, host, ip, port, tcpPort);
    }

    public void addHeader(String key, String value){
        addHeader(handleId, key, value);
    }

    public void sendRequest(byte[] content, int len, boolean finish){

        // QLog.d("send request content length=%d, finish=%b", len, finish);
        sendRequest(handleId, content, len, finish);
    }

    public void cancelRequest(){
        cancelRequest(handleId);
    } //取消本链接

    public String getState(){
        return getState(handleId);
    }

    public void clear(){
        clear(handleId);
    }


    public static native void init();

    public static native void setDebugLog(boolean isEnable);

    public static native void destory(); //取消所有链接

    private native void connect(int handleId, String host, String ip, int port, int tcpPort);

    private native void addHeader(int handleId, String key, String value);

    private native void sendRequest(int handleId, byte[] content, int len, boolean finish);

    private native void cancelRequest(int handleId);

    private native String getState(int handleId);

    private native void clear(int handleId);

    public interface NetworkCallback {
        void onConnect(int handleId, int code);
        void onDataReceive(int handleId, byte[] data, int len);
        void onCompleted(int handleId, int code);
        void onClose(int handleId, int code, String desc);
    }

//    protected static final int INIT = 0;
//    protected static final int CONNECTED = 1;
//    protected static final int RECEIVING = 2;
//    protected static final int COMPLETED = 3;
//    //已cancel_request
//    protected static final int SERVER_FAILED = 4;
//    //第一版，cancel_request, 以后版本 不cancel_request 满足复用
//    protected static final int CLIENT_FAILED = 5;

    public static String readableState(int state) {

        switch (state) {

            case 0: return "INIT";
            case 1: return "CONNECTED";
            case 2: return "RECEIVING";
            case 3: return "COMPLETE";
            case 4: return "SERVER_FAILED";
            case 5: return "CLIENT_FAILED";

            default: return "UNKNOWN";
        }
    }
}
