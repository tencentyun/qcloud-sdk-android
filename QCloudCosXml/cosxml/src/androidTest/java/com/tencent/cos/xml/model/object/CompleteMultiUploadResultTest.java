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

package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.core.TestUtils;;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.MTAProxy;
import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@RunWith(AndroidJUnit4.class)
public class CompleteMultiUploadResultTest {

    @Test
    public void testComplete() throws Exception {
        MTAProxy.init(TestUtils.getContext().getApplicationContext());
        HttpRequest<CompleteMultiUploadResult> httpRequest = new HttpRequest.Builder<CompleteMultiUploadResult>()
                .scheme("https")
                .method("POST")
                .body(RequestBodySerializer.string("text/plain", "this is a ut"))
                .host("appid-bucket.cos.region.myqcloud.com").build();
        Response response = new Response.Builder()
                .request(new Request.Builder().url("http://cloud.tencent.com").build())
                .addHeader("ut", "cos test")
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("text/plain"), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<Error>\n" +
                        "<Code>[错误码]</Code>\n" +
                        "<Message>[错误信息]</Message>\n" +
                        "<Resource>[资源地址]</Resource>\n" +
                        "<RequestId>[请求ID]</RequestId>\n" +
                        "<TraceId>[错误ID]</TraceId>\n" +
                        "</Error>"))
                .build();
        Object httpResponse;
        CompleteMultiUploadResult completeMultiUploadResult = new CompleteMultiUploadResult();
        Class cls = Class.forName("com.tencent.qcloud.core.http.HttpResponse");
        Constructor constructor = cls.getDeclaredConstructor(com.tencent.qcloud.core.http.HttpRequest.class, okhttp3.Response.class);
        try {
            if (constructor != null) {
                constructor.setAccessible(true);
                httpResponse = constructor.newInstance(httpRequest, response);
                completeMultiUploadResult.parseResponseBody((HttpResponse) httpResponse);

            }
        }catch (CosXmlServiceException serverEx){
            Log.d(QServer.TAG, serverEx.getMessage());
        }

    }

    @Test
    public void test2() throws Exception{

        MTAProxy.init(TestUtils.getContext().getApplicationContext());
        HttpRequest<CompleteMultiUploadResult> httpRequest = new HttpRequest.Builder<CompleteMultiUploadResult>()
                .scheme("https")
                .method("POST")
                .body(RequestBodySerializer.string("text/plain", "this is a ut"))
                .host("appid-bucket.cos.region.myqcloud.com").build();
        Response response = new Response.Builder()
                .request(new Request.Builder().url("http://cloud.tencent.com").build())
                .addHeader("ut", "cos test")
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("text/plain"), "<CompleteMultipartUploadResult>\n" +
                        "    <Location>arlenhuangtestsgnoversion-1251668577.cos.ap-beijing.myqcloud.com/ObjectName</Location>\n" +
                        "    <Bucket>arlenhuangtestsgnoversion-1251668577</Bucket>\n" +
                        "    <Key>ObjectName</Key>\n" +
                        "    <ETag>\"3a0f1fd698c235af9cf098cb74aa25bc\"</ETag>\n" +
                        "</CompleteMultipartUploadResult>"))
                .build();
        Object httpResponse;
        CompleteMultiUploadResult completeMultiUploadResult = new CompleteMultiUploadResult();
        Class cls = Class.forName("com.tencent.qcloud.core.http.HttpResponse");
        Constructor constructor = cls.getDeclaredConstructor(com.tencent.qcloud.core.http.HttpRequest.class, okhttp3.Response.class);
        if (constructor != null) {
            constructor.setAccessible(true);
            httpResponse = constructor.newInstance(httpRequest, response);
            completeMultiUploadResult.parseResponseBody((HttpResponse) httpResponse);
            Log.d(QServer.TAG, completeMultiUploadResult.printResult());
        }
    }
}