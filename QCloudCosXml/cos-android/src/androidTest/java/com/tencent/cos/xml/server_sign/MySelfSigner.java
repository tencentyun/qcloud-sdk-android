package com.tencent.cos.xml.server_sign;

import android.util.Log;

import com.tencent.qcloud.core.auth.QCloudSelfSigner;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.QCloudHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 实现自签名器
 * <p>
 * Created by jordanqin on 2023/8/11 17:20.
 * Copyright 2010-2023 Tencent Cloud. All Rights Reserved.
 */
public class MySelfSigner implements QCloudSelfSigner {
    private static final String TAG = "MySelfSigner";
    /**
     * 只针对cos请求 暂时不处理ci请求
     */
    public final static String COS_SESSION_TOKEN = "x-cos-security-token";

    private static final int MAX_CACHE_SIGNRESULT_SIZE = 100;

    /**
     * 缓存签名 防止分块上传重复获取签名
     */
    private final Map<Integer, SignResult> signResultPairs = new HashMap<>(MAX_CACHE_SIGNRESULT_SIZE);

    @Override
    public void sign(QCloudHttpRequest request) throws QCloudClientException {
        int sourceId = request.url().toString().hashCode();
        SignResult signResult = lookupValidSignResult(sourceId);
        if (signResult == null) {
            signResult = getSignResult(request.method(), request.host(), request.url().getPath(), request.headers(), request.queries());
            Log.d(TAG, signResult.authorization);
            cacheSignResultAndCleanUp(sourceId, signResult);
        }

        // 3. 给请求添加签名header
        request.removeHeader(HttpConstants.Header.AUTHORIZATION);
        request.addHeader(HttpConstants.Header.AUTHORIZATION, signResult.authorization);
        if(signResult.securityToken != null) {
            request.removeHeader(COS_SESSION_TOKEN);
            request.addHeader(COS_SESSION_TOKEN, signResult.securityToken);
        }
    }


    private synchronized SignResult lookupValidSignResult(int sourceId) {
        SignResult signResult = signResultPairs.get(sourceId);
        if (signResult != null && signResult.isValid()) {
            return signResult;
        }
        return null;
    }

    private synchronized void cacheSignResultAndCleanUp(int sourceId, SignResult signResult) {
        if (signResultPairs.size() > MAX_CACHE_SIGNRESULT_SIZE) {
            int overSize = signResultPairs.size() - MAX_CACHE_SIGNRESULT_SIZE;
            for (Iterator<Map.Entry<Integer, SignResult>> it = signResultPairs.entrySet().iterator();
                 it.hasNext(); ) {
                if (overSize-- > 0) {
                    it.remove();
                } else {
                    break;
                }
            }
        }

        signResultPairs.put(sourceId, signResult);
    }

    /**
     * 服务端签名结果实体
     */
    public static class SignResult {
        public String authorization;
        public String securityToken;
        public long expiredTime;

        public SignResult(String authorization, String securityToken, long expiredTime) {
            this.authorization = authorization;
            this.securityToken = securityToken;
            this.expiredTime = expiredTime;
        }

        public boolean isValid(){
            int EXPIRE_TIME_RESERVE_IN_SECONDS = 60;
            return System.currentTimeMillis() / 1000 <= expiredTime - EXPIRE_TIME_RESERVE_IN_SECONDS;
        }
    }

    public static SignResult getSignResult(String httpMethod, String host, String coskey, Map<String, List<String>> headers, Map<String, String> queries) throws QCloudClientException {
        coskey = coskey.startsWith("/") ? coskey.substring(1) : coskey;
        try {
            host = URLEncoder.encode(host, "UTF-8");
            coskey = URLEncoder.encode(coskey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, httpMethod);
        Log.d(TAG, host);
        Log.d(TAG, coskey);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://9.135.33.98:3000/server-sign")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccessful())
            throw new QCloudClientException(new IOException("Unexpected code " + response));

        Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
        }

        try {
            String jsonStr = "";
            try {
                jsonStr = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("getSignResult", jsonStr);
            JSONTokener jsonParser = new JSONTokener(jsonStr);
            JSONObject json = (JSONObject) jsonParser.nextValue();
            if(json.getInt("code") == 0){
                JSONObject data = json.getJSONObject("data");
                String authorization = data.getString("authorization");
                long expiredTime = data.getLong("expiredTime");
                String securityToken = null;
                if(data.has("securityToken")){
                    securityToken = data.getString("securityToken");
                }
                return new SignResult(authorization, securityToken, expiredTime);
            } else {
                throw new QCloudClientException("get authorization failed");
            }
        } catch (JSONException ex) {
            throw new QCloudClientException(ex);
        }
    }
}
