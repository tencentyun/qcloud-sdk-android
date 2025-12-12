package com.tencent.qcloud.core.http.interceptor;

import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_MULT_CHOICE;
import static java.net.HttpURLConnection.HTTP_SEE_OTHER;

import android.text.TextUtils;

import com.tencent.qcloud.core.common.DomainSwitchException;
import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.task.TaskManager;
import com.tencent.qcloud.core.util.DomainSwitchUtils;
import com.tencent.qcloud.core.util.OkhttpInternalUtils;

import java.io.IOException;
import java.net.ProtocolException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <p>
 * Created by jordanqin on 2023/12/11 19:57.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class RedirectInterceptor implements Interceptor {
    private static final int MAX_FOLLOW_UPS = 20;
    private OkHttpClient client;

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpTask task = (HttpTask) TaskManager.getInstance().get((String) request.tag());

        int followUpCount = 0;
        Response priorResponse = null;
        while (true) {
            if (task == null || task.isCanceled()) {
                throw new IOException("CANCELED");
            }

            Response response = chain.proceed(request);

            // Attach the prior response if it exists. Such responses never have a body.
            if (priorResponse != null) {
                response = response.newBuilder()
                        .priorResponse(priorResponse.newBuilder()
                                .body(null)
                                .build())
                        .build();
            }

            Request followUp= followUpRequest(response, task.isDomainSwitch(), task.isSelfSigner());
            if (followUp == null) {
                return response;
            }

            OkhttpInternalUtils.closeQuietly(response.body());

            if (++followUpCount > MAX_FOLLOW_UPS) {
                throw new ProtocolException("Too many follow-up requests: " + followUpCount);
            }
            request = followUp;
            priorResponse = response;
        }
    }

    private Request followUpRequest(Response userResponse, boolean isDomainSwitch, boolean isSelfSigner) throws DomainSwitchException {
        if (userResponse == null) throw new IllegalStateException();
        int responseCode = userResponse.code();

        final String method = userResponse.request().method();
        switch (responseCode) {
            case OkhttpInternalUtils.HTTP_PERM_REDIRECT:
            case OkhttpInternalUtils.HTTP_TEMP_REDIRECT:
                // "If the 307 or 308 status code is received in response to a request other than GET
                // or HEAD, the user agent MUST NOT automatically redirect the request"
                if (!method.equals("GET") && !method.equals("HEAD")) {
                    return null;
                }
                // fall-through
            case HTTP_MULT_CHOICE:
            case HTTP_MOVED_PERM:
            case HTTP_MOVED_TEMP:
            case HTTP_SEE_OTHER:
                // 对于301/302/307，检查是否需要域名切换
                if ((responseCode == HTTP_MOVED_PERM || responseCode == HTTP_MOVED_TEMP || 
                     responseCode == OkhttpInternalUtils.HTTP_TEMP_REDIRECT) &&
                    isDomainSwitch && !isSelfSigner && 
                    DomainSwitchUtils.isMyqcloudUrl(userResponse.request().url().host()) &&
                    TextUtils.isEmpty(userResponse.header("x-cos-request-id")) &&
                        TextUtils.isEmpty(userResponse.header("x-ci-request-id"))) {
                    // 满足域名切换条件，抛出异常让RetryInterceptor处理
                    throw new DomainSwitchException();
                }
                
                String location = userResponse.header("Location");
                if (location == null) return null;
                HttpUrl url = userResponse.request().url().resolve(location);

                // Don't follow redirects to unsupported protocols.
                if (url == null) return null;

                // If configured, don't follow redirects between SSL and non-SSL.
                boolean sameScheme = url.scheme().equals(userResponse.request().url().scheme());
                if (!sameScheme && !client.followSslRedirects()) return null;

                // Most redirects don't include a request body.
                Request.Builder requestBuilder = userResponse.request().newBuilder();
                if (OkhttpInternalUtils.permitsRequestBody(method)) {
                    final boolean maintainBody = OkhttpInternalUtils.redirectsWithBody(method);
                    if (OkhttpInternalUtils.redirectsToGet(method)) {
                        requestBuilder.method("GET", null);
                    } else {
                        RequestBody requestBody = maintainBody ? userResponse.request().body() : null;
                        requestBuilder.method(method, requestBody);
                    }
                    if (!maintainBody) {
                        requestBuilder.removeHeader("Transfer-Encoding");
                        requestBuilder.removeHeader("Content-Length");
                        requestBuilder.removeHeader("Content-Type");
                    }
                }

                // When redirecting across hosts, drop all authentication headers. This
                // is potentially annoying to the application layer since they have no
                // way to retain them.
                if (!sameConnection(userResponse, url)) {
                    requestBuilder.removeHeader("Authorization");
                }
                // 删掉已有的host header否则修改url后不会用新的url的host
                requestBuilder.removeHeader("Host");

                return requestBuilder.url(url).build();
            default:
                return null;
        }
    }

    private boolean sameConnection(Response response, HttpUrl followUp) {
        HttpUrl url = response.request().url();
        return url.host().equals(followUp.host())
                && url.port() == followUp.port()
                && url.scheme().equals(followUp.scheme());
    }
}
