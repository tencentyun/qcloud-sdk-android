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

import java.util.Date;

/**
 * OAuth2证书
 */

public class OAuth2Credentials implements QCloudCredentials {

    private String platform;

    private String accessToken;

    private Date validFromDate;

    private Date tokenStartTime;

    private String refreshToken;

    private String openId;

    private String scope;

    private String authorizationCode;

    private OAuth2Credentials(Builder builder) {
        platform = builder.platform;
        accessToken = builder.accessToken;
        tokenStartTime = new Date(builder.tokenStartTime);
        validFromDate = new Date(builder.tokenStartTime + builder.expiresIn * 1000);
        refreshToken = builder.refreshToken;
        openId = builder.openId;
        scope = builder.scope;
        authorizationCode = builder.authorizationCode;
    }

    public String getPlatform() {
        return platform;
    }

    @Override
    public String getSecretId() {
        return openId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getOpenId() {
        return openId;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > validFromDate.getTime();
    }

    public long getExpiresInSeconds() {
        return (validFromDate.getTime() - tokenStartTime.getTime()) / 1000;
    }

    public Date getValidFromDate() {
        return validFromDate;
    }

    public Date getTokenStartTime() {
        return tokenStartTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public static final class Builder {
        private String platform;
        private String accessToken;
        private long expiresIn;
        private long tokenStartTime;
        private String refreshToken;
        private String openId;
        private String scope;
        private String authorizationCode;

        public Builder() {
        }

        public Builder platform(String val) {
            platform = val;
            return this;
        }

        public Builder accessToken(String val) {
            accessToken = val;
            return this;
        }

        public Builder expiresInSeconds(long val) {
            expiresIn = val;
            return this;
        }

        public Builder tokenStartTime(long val) {
            tokenStartTime = val;
            return this;
        }

        public Builder refreshToken(String val) {
            refreshToken = val;
            return this;
        }

        public Builder openId(String val) {
            openId = val;
            return this;
        }

        public Builder authorizationCode(String authorizationCode) {
            this.authorizationCode = authorizationCode;
            return this;
        }

        public Builder scope(String val) {
            scope = val;
            return this;
        }

        public OAuth2Credentials build() {
            return new OAuth2Credentials(this);
        }
    }
}
