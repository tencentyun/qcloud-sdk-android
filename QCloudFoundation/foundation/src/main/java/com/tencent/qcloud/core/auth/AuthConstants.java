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

public class AuthConstants {

    public static final String Q_SIGN_ALGORITHM = "q-sign-algorithm";

    public static final String Q_AK = "q-ak";

    public static final String Q_SIGN_TIME = "q-sign-time";

    public static final String Q_KEY_TIME = "q-key-time";

    public static final String Q_HEADER_LIST = "q-header-list";

    public static final String Q_URL_PARAM_LIST = "q-url-param-list";

    public static final String Q_SIGNATURE = "q-signature";

    public static final String SHA1 = "sha1";

    static final int EXPIRE_TIME_RESERVE_IN_SECONDS = 60;

    private AuthConstants(){
        throw new AssertionError("AuthConstants is static class");
    }
}
