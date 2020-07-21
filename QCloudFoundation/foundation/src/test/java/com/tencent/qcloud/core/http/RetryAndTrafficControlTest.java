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

package com.tencent.qcloud.core.http;

import com.tencent.qcloud.core.http.interceptor.RetryInterceptor;
import com.tencent.qcloud.core.task.RetryStrategy;

import org.junit.Assert;
import org.junit.Test;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RetryAndTrafficControlTest {

    @Test
    public void testClockSkew() throws Exception {
        RetryInterceptor interceptor =
                new RetryInterceptor(RetryStrategy.FAIL_FAST);
        Request request = new Request.Builder()
                .url("http://www.qq.com")
                .build();
        Response.Builder responseBuilder = new Response.Builder()
                .protocol(Protocol.HTTP_1_1)
                .message("error")
                .request(request);

        String skewBody = "<Error>\n" +
                "  <Code>RequestTimeTooSkewed</Code>\n" +
                "  <Message>[错误信息]</Message>\n" +
                "  <Resource>[资源地址]</Resource>\n" +
                "  <RequestId>[请求ID]</RequestId>\n" +
                "  <TraceId>[错误ID]</TraceId>\n" +
                "</Error>";
        Response response = responseBuilder
                .code(403)
                .body(ResponseBody.create(MediaType.parse(HttpConstants.ContentType.XML), skewBody))
                .build();
        //Assert.assertNotNull(interceptor.getClockSkewError(response, response.code()));
        Assert.assertEquals(skewBody, response.body().string());

        response = responseBuilder
                .code(401)
                .body(ResponseBody.create(MediaType.parse(HttpConstants.ContentType.XML), skewBody))
                .build();
        //Assert.assertNull(interceptor.getClockSkewError(response, response.code()));

        skewBody = "<Error>\n" +
                "  <Code>NoSuchBucket</Code>\n" +
                "  <Message>[错误信息]</Message>\n" +
                "  <Resource>[资源地址]</Resource>\n" +
                "  <RequestId>[请求ID]</RequestId>\n" +
                "  <TraceId>[错误ID]</TraceId>\n" +
                "</Error>";
        response = responseBuilder
                .code(403)
                .body(ResponseBody.create(MediaType.parse(HttpConstants.ContentType.XML), skewBody))
                .build();
        //Assert.assertNull(interceptor.getClockSkewError(response, response.code()));
    }
}
