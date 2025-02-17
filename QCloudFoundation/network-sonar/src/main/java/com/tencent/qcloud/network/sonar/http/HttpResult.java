package com.tencent.qcloud.network.sonar.http;

import com.tencent.qcloud.network.sonar.utils.SonarLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Created by jordanqin on 2024/12/9.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class HttpResult {
    public String domain;
    public String url;
    public boolean bypassProxy;
    public int responseCode;
    public Map<String, List<String>> responseHeaders;
    public long timeConsuming;

    public String encode() {
        JSONObject o = new JSONObject();
        try {
            o.put("method", "http");
            o.put("domain", this.domain);
            o.put("url", this.url);
            o.put("bypassProxy", this.bypassProxy);
            o.put("responseCode", this.responseCode);
            if(responseHeaders != null){
                o.put("responseHeaders", this.responseHeaders);
            }
            o.put("timeConsuming", this.timeConsuming);
            return o.toString();
        } catch (JSONException err) {
            if(SonarLog.openLog){
                err.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return encode();
    }
}
