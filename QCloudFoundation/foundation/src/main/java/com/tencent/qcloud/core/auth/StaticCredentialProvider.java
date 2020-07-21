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

/**
 * 静态证书提供器<br>
 * 直接接收{@link QCloudCredentials}并对外输出
 */

public class StaticCredentialProvider implements QCloudCredentialProvider {

    private QCloudCredentials mCredentials;

    public StaticCredentialProvider() {

    }

    public StaticCredentialProvider(QCloudCredentials credentials) {
        mCredentials = credentials;
    }

    /**
     * 更新访问证书，请在每次调用 API 前都更新，以免发生请求无授权的错误。
     *
     * @param credentials 腾讯云访问证书
     */
    public void update(QCloudCredentials credentials) {
        mCredentials = credentials;
    }

    @Override
    public QCloudCredentials getCredentials() {
        return mCredentials;
    }

    @Override
    public void refresh() {

    }
}
