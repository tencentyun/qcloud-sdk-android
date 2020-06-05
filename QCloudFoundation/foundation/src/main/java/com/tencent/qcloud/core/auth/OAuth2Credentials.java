package com.tencent.qcloud.core.auth;

import java.util.Date;

/**
 * <p>
 * </p>
 * Created by wjielai on 2017/12/11.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
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
