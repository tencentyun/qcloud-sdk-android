package com.tencent.qcloud.network.sonar.http;

import android.text.TextUtils;

import com.tencent.qcloud.network.sonar.Sonar;
import com.tencent.qcloud.network.sonar.SonarRequest;
import com.tencent.qcloud.network.sonar.SonarResult;
import com.tencent.qcloud.network.sonar.SonarType;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/**
 * <p>
 * Created by jordanqin on 2024/12/9.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class HttpSonar implements Sonar<HttpResult> {
    public boolean bypassProxy = false;

    public HttpSonar(boolean bypassProxy) {
        this.bypassProxy = bypassProxy;
    }

    @Override
    public SonarResult<HttpResult> start(SonarRequest request) {
        if(!request.isNetworkAvailable()){
            return new SonarResult<>(SonarType.HTTP, new Exception(Sonar.ERROR_MSG_NO_NETWORK));
        }

        if(TextUtils.isEmpty(request.getHost())){
            return new SonarResult<>(SonarType.HTTP, new Exception(Sonar.ERROR_MSG_HOST_IS_EMPTY));
        }

        HttpResult httpResult = new HttpResult();
        httpResult.domain = request.getHost();
        httpResult.bypassProxy = bypassProxy;
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://"+request.getHost());
            httpResult.url = url.toString();
            if (bypassProxy) {
                Proxy proxy = Proxy.NO_PROXY;
                connection = (HttpURLConnection) url.openConnection(proxy);
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }

            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod("HEAD");

            long startTime = System.currentTimeMillis();
            connection.connect();
            int responseCode = connection.getResponseCode();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            httpResult.responseCode = responseCode;
            httpResult.timeConsuming = duration;
            httpResult.responseHeaders = connection.getHeaderFields();
        } catch (Exception e) {
            return new SonarResult<>(SonarType.HTTP, e);
        } finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return new SonarResult<>(SonarType.HTTP, httpResult);
    }

    @Override
    public void stop() {

    }
}
