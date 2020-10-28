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

package com.tencent.qcloud.quic.test;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import android.util.Log;


import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class TQuicTest {

    QuicManager quicManager;

    protected synchronized void init(){

        Context appContext = InstrumentationRegistry.getTargetContext();
        if(quicManager == null){

            quicManager = new QuicManager();

            quicManager.init(true);
        }

    }

    @Test
    public void testGet() throws Exception{

        init();

        String host = "stgwhttp2.kof.qq.com";
        String ip = "";
        int port = 443;
        int tcpPort = 80;
        try {
            InetAddress[] inetAddresses = InetAddress.getAllByName(host);
            if(inetAddresses != null && inetAddresses.length > 0){
                ip = inetAddresses[0].getHostAddress();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        QuicRequest quicRequest = new QuicRequest(host, ip, port, tcpPort);
        quicRequest.addHeader(":scheme", "https");
        quicRequest.addHeader(":path",  "/1.jpg");
        quicRequest.addHeader(":method", "GET");
        QuicResponse quicResponse = new QuicResponse();
        try {
            quicResponse.progressCallback = new ProgressCallback() {
                @Override
                public void onProgress(long current, long total) {
                    Log.d("XIAO", Thread.currentThread().getName() + "->" + current + "/" + total);
                }
            };
            String fileName = "11-" + System.currentTimeMillis() + ".jpg";
            quicResponse.outputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/" + fileName);
            quicManager.execute(quicRequest, quicResponse);
            for(Map.Entry<String, String> header : quicResponse.headers.entrySet()){
                Log.d("XIAO", "(" + header.getKey() + "|" + header.getValue() + ")");
            }
        } catch (QuicException e) {
            throw e;
        } catch (FileNotFoundException e) {
           throw e;
        }
    }


    volatile boolean isOver = false;
    Exception exception;

    @Test
    public void testAsyncGet() throws Exception{
        init();
        String host = "stgwhttp2.kof.qq.com";
        String ip = "";
        int port = 443;
        int tcpPort = 80;
        try {
            InetAddress[] inetAddresses = InetAddress.getAllByName(host);
            if(inetAddresses != null && inetAddresses.length > 0){
                ip = inetAddresses[0].getHostAddress();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        QuicRequest quicRequest = new QuicRequest(host, ip, port, tcpPort);
        quicRequest.addHeader(":scheme", "https");
        quicRequest.addHeader(":path",  "/1.jpg");
        quicRequest.addHeader(":method", "GET");
        QuicResponse quicResponse = new QuicResponse();
        try {
            quicResponse.progressCallback = new ProgressCallback() {
                @Override
                public void onProgress(long current, long total) {
                    Log.d("XIAO", Thread.currentThread().getName() + "->" + current + "/" + total);
                }
            };
            String fileName = "1-" + System.currentTimeMillis() + ".jpg";
            quicResponse.outputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/" + fileName);
            quicManager.enqueue(quicRequest, quicResponse, new ResultCallback() {

                @Override
                public void onFailure(QuicRequest request, QuicException e) {
                    isOver = true;
                    exception = e;
                }

                @Override
                public void onResponse(QuicRequest request, QuicResponse response) {
                    for (Map.Entry<String, String> header : response.headers.entrySet()) {
                        Log.d("XIAO", "async (" + header.getKey() + "|" + header.getValue() + ")");
                    }
                    isOver = true;
                }
            });
            while (!isOver){
                Thread.sleep(50);
            }
            if(exception != null) throw exception;
        }catch (Exception e){
            throw e;
        }
    }


}
