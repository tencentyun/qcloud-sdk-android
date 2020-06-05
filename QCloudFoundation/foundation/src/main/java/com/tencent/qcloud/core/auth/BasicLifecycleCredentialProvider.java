package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 带生命周期的密钥提供者，提供者会缓存上次请求得到的密钥，如果还在有效期内会直接被重用，过期了再次请求
 *
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 *
 * &lt;a herf https://www.qcloud.com/document/product/436/7778&gt;&lt;/a&gt;
 */

public abstract class BasicLifecycleCredentialProvider implements QCloudCredentialProvider {

    private volatile QCloudLifecycleCredentials credentials;

    private ReentrantLock lock = new ReentrantLock();

    @Override
    public QCloudCredentials getCredentials()  throws QCloudClientException {
        QCloudLifecycleCredentials cred = safeGetCredentials();
        if (cred == null || !cred.isValid()) {
            refresh();
            return safeGetCredentials();
        }
        return cred;
    }

    @Override
    public void refresh() throws QCloudClientException {
        boolean locked = false;
        try {
            locked = lock.tryLock(20, TimeUnit.SECONDS);

            if (!locked) {
                throw new QCloudClientException(new QCloudAuthenticationException("lock timeout, no credential for sign"));
            }

            QCloudLifecycleCredentials cred = safeGetCredentials();
            if (cred == null || !cred.isValid()) {
                safeSetCredentials(null);
                try {
                    QCloudLifecycleCredentials newCredentials = fetchNewCredentials();
                    safeSetCredentials(newCredentials);
                } catch (Exception e) {
                    if (e instanceof QCloudClientException) {
                        throw e;
                    }
                    throw new QCloudClientException("fetch credentials error happens", new QCloudAuthenticationException(e.getMessage()));
                }
            }
        } catch (InterruptedException e) {
            throw new QCloudClientException("interrupt when try to get credential", new QCloudAuthenticationException(e.getMessage()));
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    private synchronized void safeSetCredentials(QCloudLifecycleCredentials credentials) {
        this.credentials = credentials;
    }

    private synchronized QCloudLifecycleCredentials safeGetCredentials() {
        return credentials;
    }

    /**
     * 请求一个新的临时密钥
     *
     * @return 一个有效的临时密钥
     * @throws QCloudClientException 如果请求超时或者失败，会抛出异常
     */
    protected abstract QCloudLifecycleCredentials fetchNewCredentials() throws QCloudClientException;
}
