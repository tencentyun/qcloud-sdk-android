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

import com.tencent.qcloud.core.http.CallMetricsListener;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.tquic.impl.TnetQuicRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import okio.BufferedSink;
import okio.Okio;

public class QuicImpl extends TnetQuicRequest.Callback implements java.util.concurrent.Callable<QuicResponse>{

    private QuicRequest quicRequest;
    private QuicResponse quicResponse;
    private QuicException exception;

    private TnetQuicRequest realQuicCall;

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

    private static final CallMetricsListener emptyCallMetricsListener = new CallMetricsListener(null);

    private CountDownLatch latch = new CountDownLatch(1);

    public QuicImpl(QuicRequest quicRequest){
        this.quicRequest = quicRequest;
        this.quicResponse = new QuicResponse();
        this.callMetricsListener = emptyCallMetricsListener;
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
     */
    @Override
    public void onConnect(int error_code) {
        onInternalConnect();
    }

    @Override
    public void onNetworkLinked() {

    }

    /**
     * 接收响应包 Header
     */
    @Override
    public void onHeaderRecv(String header) {
        onInternalReceiveHeaderResponse(header);
    }

    /**
     * 接收响应包 Body
     */
    @Override
    public void onDataRecv(byte[] body) {
        onInternalReceiveBodyResponse(body, body.length);
    }

    /**
     * 响应包已接收完
     */
    @Override
    public void onComplete(int stream_error) {
        onInternalComplete();
    }

    /**
     * 底层链接 close
     */
    @Override
    public void onClose(int error_code, String error_str) {
        onInternalClose();
    }

    private void onInternalClientFailed(Exception clientException) {
        reportException = true;
        exception = new QuicException(clientException);
        latch.countDown();
        quitSafely();
    }

    private void onInternalConnect() {
        isOver = false;
        sendData();
    }

    private void onInternalReceiveHeaderResponse(String header){
        // quic 并不会走到这里
//        parseResponseHeader(header);
//        callMetricsListener.responseHeadersEnd(null, null);
//        receivedHeader = true;
//        callMetricsListener.responseBodyStart(null);
//        receivedResponse = true;
    }

    private void onInternalReceiveBodyResponse(byte[] data, int len){
//        parseBody(data, len);
//        callMetricsListener.responseBodyEnd(null, -1L);
//        receivedResponse = true;

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

    private void onInternalComplete() {
        isCompleted = true;
        latch.countDown();
        quitSafely();
    }

    private void onInternalClose() {

        if(isOver) {
            return; //cancel by user
        }
        reportException = true;
        latch.countDown();
        quitSafely();
    }


    /**
     * 建立链接
     */
    private void startConnect(){
        callMetricsListener.connectStart(null, null, null);
        realQuicCall.connect(quicRequest.url, quicRequest.ip);
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
                realQuicCall.addHeaders(header.getKey(), header.getValue());
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
            // QLog.d("headers==>%s", firtPakcet);
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
                            // QLog.d("parseResponseHeader we reach the body data");
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
               //QLog.d("HTTP/1.0");
            } else if (httpMinorVersion == 1) {
                //QLog.d("HTTP/1.1");
            } else {
                throw new ProtocolException("Unexpected status line: " + statusLine);
            }
        } else if (statusLine.startsWith("ICY ")) {
            // Shoutcast uses ICY instead of "HTTP/1.0".
            //QLog.d("HTTP/1.0");
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
        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "finish(handleId = " + getHandleId() + ")");
    }

    /**
     * 取消链接
     */
    public void cancelConnect(){
        realQuicCall.CancelRequest();
    }

    /**
     * 清除资源
     */
    public void clear(){
        realQuicCall.Destroy();
    }

    @Override
    public QuicResponse call() throws QuicException {

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "create new QuicNative object");
        realQuicCall = new TnetQuicRequest(this, QuicClientImpl.getQuicConfig(), 0);

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "start connect " + realQuicCall.toString());

        startConnect();

        // wait for complete
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "finish call(handleId = " + getHandleId() + ")");
//        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "finish call(handleId = " + getHandleId() + "),  state is " + readableState(realQuicCall.currentState));

        if(exception != null){
            throw exception;
        }
        return quicResponse;
    }

    private int getHandleId() {

        if (realQuicCall != null) {
            return realQuicCall.hashCode();
        }
        return -1;
    }

    public QuicException getException(){
        return exception;
    }


    private void quitSafely() {

        QCloudLogger.d(QCloudHttpClient.QUIC_LOG_TAG, "quitSafely(handleId = %d), reportException: %b, isCompleted: %b, hasReceiveResponse: %b, isOver: %b", getHandleId(),
                reportException, isCompleted, receivedResponse, isOver);

        if(reportException || (isCompleted && receivedResponse)){
            isOver = true;
        }else {
            return;
        }
        if (isCompleted) {
            finish();
        }
    }
}
