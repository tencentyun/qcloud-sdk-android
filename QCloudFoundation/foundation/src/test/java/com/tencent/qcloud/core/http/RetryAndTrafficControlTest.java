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

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/12/18.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
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
