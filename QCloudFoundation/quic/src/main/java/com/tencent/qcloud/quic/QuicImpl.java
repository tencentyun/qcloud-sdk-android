package com.tencent.qcloud.quic;

import android.os.*;
import android.support.annotation.Nullable;
import com.tencent.qcloud.core.http.CallMetricsListener;
import okio.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.ProtocolException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static com.tencent.qcloud.quic.QuicNative.*;

public class QuicImpl implements NetworkCallback, java.util.concurrent.Callable<QuicResponse>{

    private QuicRequest quicRequest;
    private QuicResponse quicResponse;
    private QuicException exception;

    private ConnectPool connectPool;
    private QuicNative realQuicCall;

    // Quic 通过 onDataReceive 回调来返回响应数据，首先返回 Header，然后返回 Body
    // 是否已经接收了 Header 数据
    private boolean receivedHeader = false;

    // 真正取消：response 接收完 + onRequestCompleted 或者 onClose
    // 需要排除: response 未接收，onRequestCompleted 就发生了的情况
    // 一旦 response 获取了，则任务可表示结束了
    // 任务结束，包括正常结束和异常退出
    private volatile boolean isOver = false;

    // 是否完成响应
    private boolean isCompleted = false;

    // 是否接收到了响应数据
    private boolean receivedResponse = false;

    // 是否产生了异常，包括 Quic 通过 Close 返回的异常，或者是 java 代码导致的异常
    private boolean reportException = false;

    private CallMetricsListener callMetricsListener;

    private CountDownLatch latch = new CountDownLatch(1);

    public QuicImpl(QuicRequest quicRequest, ConnectPool connectPool){
        this.quicRequest = quicRequest;
        this.connectPool = connectPool;
        this.quicResponse = new QuicResponse();
    }

    public void setCallMetricsListener(CallMetricsListener callMetricsListener){
        this.callMetricsListener = callMetricsListener;
    }

    public void setOutputDestination(OutputStream outputStream){
        this.quicResponse.setOutputStream(outputStream);
    }

    public void setProgressCallback(ProgressCallback progress){
        this.quicResponse.setProgressCallback(progress);
    }

    /**
     * 成功建立链接
     * @param handleId
     * @param code
     */
    @Override
    public void onConnect(int handleId, int code) {
        onInternalConnect();
    }

    /**
     * 接收响应包
     * @param handleId
     * @param data
     * @param len
     */
    @Override
    public void onDataReceive(int handleId, byte[] data, int len) {
        onInternalReceiveResponse(data, len);
    }

    /**
     * 响应包已接收完
     * @param handleId
     */
    @Override
    public void onCompleted(int handleId, int code) {
        onInternalComplete(handleId, code);
    }

    /**
     * 底层链接 close
     * @param handleId
     * @param code
     * @param desc
     */
    @Override
    public void onClose(int handleId, int code, String desc) {
        onInternalClose(handleId, code, desc);
    }

    private void onInternalClientFailed(Exception clientException) {

        connectPool.updateQuicNativeState(realQuicCall, CLIENT_FAILED);
        reportException = true;
        exception = new QuicException(clientException);
        latch.countDown();
        quitSafely();
    }

    private void onInternalConnect() {
        connectPool.updateQuicNativeState(realQuicCall, CONNECTED);
        isOver = false;
        sendData();
    }

    private void onInternalReceiveResponse(byte[] data, int len){
        if (!receivedHeader) {
            parseResponseHeader(data, len);
            callMetricsListener.responseHeadersEnd(null, null);
            receivedHeader = true;
            callMetricsListener.responseBodyStart(null);
        } else {
            parseBody(data, len);
            callMetricsListener.responseBodyEnd(null, -1L);
        }
        receivedResponse = true;
    }

    private void onInternalComplete(int handleId, int code) {

        isCompleted = true;
        connectPool.updateQuicNativeState(realQuicCall, COMPLETED);
        latch.countDown();
        quitSafely();
    }

    private void onInternalClose(int handleId, int code, String desc) {

        QLog.d("has been completed : %s", isOver);

        if(isOver) {
            return; //cancel by user
        }

        exception = new QuicException(String.format(Locale.ENGLISH, "Closed(%d, %s)", code, desc));;
        connectPool.updateQuicNativeState(realQuicCall, SERVER_FAILED);
        reportException = true;
        latch.countDown();
        quitSafely();
    }


    /**
     * 建立链接
     */
    private void startConnect(){
        callMetricsListener.connectStart(null, null, null);
        realQuicCall.connect(quicRequest.host, quicRequest.ip, quicRequest.port, quicRequest.tcpPort);
    }

    /**
     * 发送数据
     */
    private void sendData(){
        callMetricsListener.connectEnd(null, null, null, null);
        try {
            callMetricsListener.requestHeadersStart(null);
            //发送 header
            for(Map.Entry<String, String> header : quicRequest.headers.entrySet()){
                realQuicCall.addHeader(header.getKey(), header.getValue());
            }
            callMetricsListener.requestHeadersEnd(null, null);
            callMetricsListener.requestBodyStart(null);
            //发送data
            if(quicRequest.requestBody != null){
                QuicOutputStream quicOutputStream = new QuicOutputStream(realQuicCall);
                BufferedSink bufferedSink = Okio.buffer(Okio.sink(quicOutputStream));
                quicRequest.requestBody.writeTo(bufferedSink);
                try {
                    bufferedSink.flush();
                    bufferedSink.close();
                }catch (Exception e){}
                realQuicCall.sendRequest(new byte[0], 0, true);
            }else {
                realQuicCall.sendRequest(new byte[0], 0, true);
            }
            callMetricsListener.requestBodyEnd(null, -1L);
            callMetricsListener.responseHeadersStart(null);
        } catch (IOException e){
            //失败
            onInternalClientFailed(e);
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
            // from simbachen tests
            String firtPakcet = new String(data, "ISO-8859-1");
            QLog.d("headers==>%s", firtPakcet);
            //QLog.d("headers==>%s", new String(data, "utf-8"));
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
                        parseStateLine(line);
                    }else if(line.contains(":")){ // date: Wed, 13 Mar 2019 13:14:51 GMT
                        int index = line.indexOf(":");
                        String key = line.substring(0, index).trim();
                        String value = line.substring(index + 1).trim();
                        quicResponse.headers.put(key, value);
                        if(key.equalsIgnoreCase("content-length")){
                            quicResponse.setContentLength(Long.valueOf(value));
                        }else if(key.equalsIgnoreCase("content-type")){
                            quicResponse.contentType = value;
                        }

                    }else if(line.length() == 0){ // "\r\n"
                        //from simbachen tests
                        String bodyString = bufferedReader.readLine();
                        if( bodyString != null){
                            QLog.d("parseResponseHeader we reach the body data");
                            byte[] bodyByte = bodyString.getBytes("ISO-8859-1");
                            parseBody(bodyByte, bodyByte.length);
                        }else {
                            break;
                        }
                    }
                    line = bufferedReader.readLine();
                }
            }catch (IOException e){
                // error
                onInternalClientFailed(e);
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

    // H T T P / 1 . 1   2 0 0   T e m p o r a r y   R e d i r e c t
    // 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0
    private void parseStateLine(String statusLine) throws ProtocolException {

        int codeStart;
        if (statusLine.startsWith("HTTP/1.")) {
            if (statusLine.length() < 9 || statusLine.charAt(8) != ' ') {
                throw new ProtocolException("Unexpected status line: " + statusLine);
            }
            int httpMinorVersion = statusLine.charAt(7) - '0';
            codeStart = 9;

            if (httpMinorVersion == 0) {
               QLog.d("HTTP/1.0");
            } else if (httpMinorVersion == 1) {
                QLog.d("HTTP/1.1");
            } else {
                throw new ProtocolException("Unexpected status line: " + statusLine);
            }
        } else if (statusLine.startsWith("ICY ")) {
            // Shoutcast uses ICY instead of "HTTP/1.0".
            QLog.d("HTTP/1.0");
            codeStart = 4;
        } else {
            throw new ProtocolException("Unexpected status line: " + statusLine);
        }

        // Parse response code like "200". Always 3 digits.
        if (statusLine.length() < codeStart + 3) {
            throw new ProtocolException("Unexpected status line: " + statusLine);
        }
        int code;
        try {
            code = Integer.parseInt(statusLine.substring(codeStart, codeStart + 3));
        } catch (NumberFormatException e) {
            throw new ProtocolException("Unexpected status line: " + statusLine);
        }

        // Parse an optional response message like "OK" or "Not Modified". If it
        // exists, it is separated from the response code by a space.
        String message = "";
        if (statusLine.length() > codeStart + 3) {
            if (statusLine.charAt(codeStart + 3) != ' ') {
                throw new ProtocolException("Unexpected status line: " + statusLine);
            }
            message = statusLine.substring(codeStart + 4);
        }
        quicResponse.code = code;
        quicResponse.message = message;
    }

    /**
     * 接收body
     * @param data
     * @param len
     */
    private void parseBody(byte[] data, int len){
        try {
            if(quicResponse.code >= 200 && quicResponse.code < 300 && quicResponse.fileSink != null){
                quicResponse.fileSink.write(data, 0, len);
                quicResponse.updateProgress(len);
            }else {
                quicResponse.buffer.write(data, 0, len);
                quicResponse.currentLength += len;
            }
        } catch (Exception e) {
            //失败
            onInternalClientFailed(e);
        }

    }

    /**
     * 请求-响应 结束
     */
    private void finish(){
        try {
            if(quicResponse.code >= 200 && quicResponse.code < 300){
                if(quicResponse.fileSink != null){
                    quicResponse.fileSink.flush();
                    quicResponse.fileSink.close();
                }
            } else {
                quicResponse.buffer.flush();
            }
        } catch (Exception e) {
            onInternalClientFailed(e);
        }

        QLog.d("quic net info: %s", realQuicCall.getState());
    }

    /**
     * 取消链接
     */
    public void cancelConnect(){
        realQuicCall.cancelRequest();
    }

    /**
     * 清除资源
     */
    public void clear(){
        realQuicCall.clear();
    }

    @Override
    public QuicResponse call() throws QuicException {

        QLog.d("start get a connect: ");

        //获取connect： quicNative
        // realQuicCall = connectPool.getQuicNative(quicRequest.host, quicRequest.ip, quicRequest.port, quicRequest.tcpPort);
        realQuicCall = ConnectPool.createNewQuicNative(quicRequest.host, quicRequest.ip, quicRequest.port, quicRequest.tcpPort);
        realQuicCall.setCallback(this);

        QLog.d("handle message " + "start call " + realQuicCall.toString());
        connectPool.dumpQuicNatives();

        if(realQuicCall.currentState == CONNECTED){
            QLog.d("quic native is connected.");
            onInternalConnect();
        }else {
            QLog.d("quic native start connect.");
            startConnect();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Looper.loop();//开始循环
        //循环结束
        QLog.d("latch count released");
        if(exception != null){
            throw exception;
        }
        return quicResponse;
    }

    public QuicException getException(){
        return exception;
    }


    private void quitSafely() {

        QLog.d("isClose: %s; isCompleted: %s; hasReceiveResponse: %s", reportException, isCompleted, receivedResponse);
        if(reportException || (isCompleted && receivedResponse)){
            isOver = true;
        }else {
            return;
        }
        if (isCompleted) {
            finish();
        }
        connectPool.updateQuicNativeState(realQuicCall, COMPLETED);
    }

}
