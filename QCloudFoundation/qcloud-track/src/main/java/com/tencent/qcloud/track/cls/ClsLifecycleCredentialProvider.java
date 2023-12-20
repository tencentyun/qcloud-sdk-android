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

package com.tencent.qcloud.track.cls;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CLS临时秘钥提供器
 */

public abstract class ClsLifecycleCredentialProvider {
    private volatile ClsSessionCredentials credentials;

    private ReentrantLock lock = new ReentrantLock();

    public ClsSessionCredentials getCredentials() throws ClsAuthenticationException {
        ClsSessionCredentials cred = safeGetCredentials();
        if (cred == null || !cred.isValid()) {
            refresh();
            return safeGetCredentials();
        }
        return cred;
    }

    public void refresh() throws ClsAuthenticationException {
        boolean locked = false;
        try {
            locked = lock.tryLock(20, TimeUnit.SECONDS);

            if (!locked) {
                throw new ClsAuthenticationException("lock timeout, no credential for sign");
            }

            ClsSessionCredentials cred = safeGetCredentials();
            if (cred == null || !cred.isValid()) {
                safeSetCredentials(null);
                try {
                    ClsSessionCredentials newCredentials = fetchNewCredentials();
                    safeSetCredentials(newCredentials);
                } catch (Exception e) {
                    if (e instanceof ClsAuthenticationException) {
                        throw e;
                    }
                    throw new ClsAuthenticationException("fetch credentials error happens: " + e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            throw new ClsAuthenticationException("interrupt when try to get credential: " + e.getMessage());
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    /**
     * 强制让凭证失效
     */
    public synchronized void forceInvalidationCredential(){
        safeSetCredentials(null);
    }

    private synchronized void safeSetCredentials(ClsSessionCredentials credentials) {
        this.credentials = credentials;
    }

    private synchronized ClsSessionCredentials safeGetCredentials() {
        return credentials;
    }

    /**
     * 请求一个新的临时密钥
     *
     * @return 一个有效的临时密钥
     */
    protected abstract ClsSessionCredentials fetchNewCredentials() throws ClsAuthenticationException;
}
