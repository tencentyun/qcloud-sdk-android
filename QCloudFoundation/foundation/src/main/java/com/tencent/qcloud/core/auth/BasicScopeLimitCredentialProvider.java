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

package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.common.QCloudClientException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 指定范围限制的证书提供器
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
