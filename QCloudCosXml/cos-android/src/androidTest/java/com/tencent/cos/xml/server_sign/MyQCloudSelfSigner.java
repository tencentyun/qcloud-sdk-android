package com.tencent.cos.xml.server_sign;

import android.util.Log;

import com.tencent.qcloud.core.auth.QCloudSelfSigner;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.QCloudHttpRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Created by jordanqin on 2023/8/11 17:20.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class MyQCloudSelfSigner implements QCloudSelfSigner {
    private static final String TAG = "MyQCloudSelfSigner";
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
        // 1. 获取request的签名source
        MyCOSXmlSignSourceProvider sourceProvider = new MyCOSXmlSignSourceProvider();
        sourceProvider.setSignTime(MyCOSXmlSigner.credentials.getKeyTime());
        String source = source(sourceProvider, request);

        // 2. 获取签名结果
        int sourceId = source.hashCode();
        SignResult signResult = lookupValidSignResult(sourceId);
        if (signResult == null) {
            signResult = fetchNewSignResult(sourceProvider, source);
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

    /**
     * 远程获取签名结果
     */
    private SignResult fetchNewSignResult(MyCOSXmlSignSourceProvider sourceProvider, String source) throws QCloudClientException {
        Log.d(TAG, "fetchNewSignResult");
        return MyCOSXmlSigner.sign(source, sourceProvider.getRealHeaderList(), sourceProvider.getRealParameterList());
    }

    /**
     * 根据reqeust获取签名source
     */
    private String source(MyCOSXmlSignSourceProvider sourceProvider, QCloudHttpRequest request){
        // 在这里去掉request不需要签名的内容，例如分块上传的partNumber content-md5
        String partNumberKey =  "partNumber";
        String contentMD5Key =  "Content-MD5";
        if(request.queries().containsKey(partNumberKey)){
            sourceProvider.addNoSignHeader(contentMD5Key);
            sourceProvider.addNoSignParam(partNumberKey);
        }
        try {
            String source = sourceProvider.source(request);
            Log.d(TAG, source);
            return source;
        } catch (QCloudClientException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized SignResult lookupValidSignResult(int sourceId) {
        SignResult signResult = signResultPairs.get(sourceId);
        if (signResult != null) {
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

        public SignResult(String authorization, String securityToken) {
            this.authorization = authorization;
            this.securityToken = securityToken;
        }
    }
}
