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

package com.tencent.qcloud.quic;

import android.util.Log;

import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.util.Locale;
import java.util.UUID;

public class QuicNative {

    int handleId = UUID.randomUUID().toString().hashCode();

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

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "CallByNative: onConnect, handleId = %s, code = %d", handleId, code);
        currentState = CONNECTED;
        if(callback != null)callback.onConnect(handleId, code);
    }

    @CallByNative
    private void onDataReceive(byte[] data, int len){

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "CallByNative: onDataReceive, handleId = %s, len = %d ", handleId,  len);
        if(callback != null)callback.onDataReceive(handleId, data, len);
    }

    @CallByNative
    private void onCompleted(int code){

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "CallByNative: onCompleted, handleId = %s, code = %d", handleId, code);
        if(callback != null)callback.onCompleted(handleId, code);
    }

    @CallByNative
    private void onClose(int code, String desc){

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "CallByNative: onClose, handleId = %s, code = %d, desc = %s", handleId, code, desc);
        currentState = SERVER_FAILED;
        if(callback != null)callback.onClose(handleId, code, desc);
    }


    public void connect(String host, String ip, int port, int tcpPort){

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "CallNative: connect, handleId = %s, host = %s, ip = %s, port = %d, tcpPort = %d ",
                handleId, host, ip, port, tcpPort);

        connect(handleId, host, ip, port, tcpPort);
    }

    public void addHeader(String key, String value){

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "CallNative: addHeader, handleId = %s, key = %s, value = %s",
                handleId, key, value);

        addHeader(handleId, key, value);
    }

    public void sendRequest(byte[] content, int len, boolean finish){

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "CallNative: sendRequest, handleId = %s, len = %d, finish = %b",
                handleId, len, finish);

        sendRequest(handleId, content, len, finish);
    }

    public void cancelRequest(){

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "CallNative: cancelRequest, handleId = %s", handleId);
        cancelRequest(handleId);
    } //取消本链接

    public String getState(){

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "CallNative: getState, handleId = %s", handleId);
        return getState(handleId);
    }

    public void clear(){
        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "CallNative: clear, handleId = %s", handleId);
        clear(handleId);
    }


    public static native void init();

    public static native void setTnetConfigRaceType(int raceType);

    public static native void setTnetConfigIsCustomProtocol(boolean isCustomProtocol);

    public static native void setTnetConfigTotalTimeoutSec(int totalTimeoutSec);

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
