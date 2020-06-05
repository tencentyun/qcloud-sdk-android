package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.common.QCloudClientException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 *     限定范围的密钥提供者，临时密钥的权限被限定在操作和资源上，只有同样Scope的密钥才会被重用，您也需要根据场景自行缓存密钥
 * </p>
 * Created by wjielai on 2018/12/18.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public abstract class BasicScopeLimitCredentialProvider implements ScopeLimitCredentialProvider {

    private static final int MAX_CACHE_CREDENTIAL_SIZE = 100;
    private Map<Integer, SessionQCloudCredentials> credentialPairs = new HashMap<>(MAX_CACHE_CREDENTIAL_SIZE);

    @Override
    public SessionQCloudCredentials getCredentials(STSCredentialScope[] credentialScope)
            throws QCloudClientException {
        int scopeId = STSCredentialScope.jsonify(credentialScope).hashCode();
        SessionQCloudCredentials credentials = lookupValidCredentials(scopeId);
        if (credentials == null) {
            credentials = fetchNewCredentials(credentialScope);
            cacheCredentialsAndCleanUp(scopeId, credentials);
        }

        return credentials;
    }

    private synchronized SessionQCloudCredentials lookupValidCredentials(int scopeId) {
        SessionQCloudCredentials credentials = credentialPairs.get(scopeId);
        if (credentials != null && credentials.isValid()) {
            return credentials;
        }

        return null;
    }

    private synchronized void cacheCredentialsAndCleanUp(int scopeId, SessionQCloudCredentials newCredentials) {
        for(Iterator<Map.Entry<Integer, SessionQCloudCredentials>> it = credentialPairs.entrySet().iterator();
            it.hasNext(); ) {
            Map.Entry<Integer, SessionQCloudCredentials> entry = it.next();
            if(!entry.getValue().isValid()) {
                it.remove();
            }
        }

        if (credentialPairs.size() > MAX_CACHE_CREDENTIAL_SIZE) {
            int overSize = credentialPairs.size() - MAX_CACHE_CREDENTIAL_SIZE;
            for(Iterator<Map.Entry<Integer, SessionQCloudCredentials>> it = credentialPairs.entrySet().iterator();
                it.hasNext(); ) {
                if (overSize-- > 0) {
                    it.remove();
                } else {
                    break;
                }
            }
        }

        credentialPairs.put(scopeId, newCredentials);
    }

    @Override
    public void refresh() {
        // empty
    }

    @Override
    public QCloudCredentials getCredentials() throws QCloudClientException {
        throw new UnsupportedOperationException("not support ths op");
    }

    /**
     * 根据权限，请求一个新的临时密钥
     *
     * @param credentialScope 当前申请的密钥需要的最小权限
     * @return 一个有效的临时密钥
     * @throws QCloudClientException 如果请求超时或者失败，会抛出异常
     */
    protected abstract SessionQCloudCredentials fetchNewCredentials(STSCredentialScope[] credentialScope)
            throws QCloudClientException;
}
