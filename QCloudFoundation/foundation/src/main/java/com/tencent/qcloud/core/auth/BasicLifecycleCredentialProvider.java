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

import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基础的生命周期证书提供器<br>
 * 用于定义如何获取具有生命周期的证书{@link QCloudLifecycleCredentials}，<br>
 * 您可以自定义临时密钥服务的 HTTP 响应体，给终端返回服务器时间作为签名的开始时间，用来避免由于用户手机本地时间偏差过大导致的签名不正确，或者使用其他的协议来进行终端和服务端之间的通信。
 * <p>
 * SDK 示例：<a herf="https://cloud.tencent.com/document/product/436/12159#.E5.88.9D.E5.A7.8B.E5.8C.96.E6.9C.8D.E5.8A.A1">方式二：自定义响应体授权</a>
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
