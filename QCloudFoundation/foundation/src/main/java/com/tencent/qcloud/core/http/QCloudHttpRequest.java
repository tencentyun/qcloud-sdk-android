package com.tencent.qcloud.core.http;


import com.tencent.qcloud.core.auth.QCloudSignSourceProvider;
import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.auth.STSCredentialScope;
import com.tencent.qcloud.core.auth.SignerFactory;
import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.util.QCloudStringUtils;

import java.net.URL;

/**
 * <p>
 * </p>
 * Created by wjielai on 2017/11/29.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public class QCloudHttpRequest<T> extends HttpRequest<T> {

    private final QCloudSignSourceProvider signProvider;
    private final String signerType;
    private final STSCredentialScope[] credentialScope;

    public QCloudHttpRequest(Builder<T> builder) {
        super(builder);

        signerType = builder.signerType;
        signProvider = builder.signProvider;
        credentialScope = builder.credentialScope;
    }

    public QCloudSignSourceProvider getSignProvider() {
        return signProvider;
    }

    public STSCredentialScope[] getCredentialScope() {
        return credentialScope;
    }

    QCloudSigner getQCloudSigner() throws QCloudClientException {
        QCloudSigner signer = null;
        if (signerType != null && shouldCalculateAuth()) {
            signer = SignerFactory.getSigner(signerType);
            if (signer == null) {
                throw new QCloudClientException(new QCloudAuthenticationException("can't get signer for type : " + signerType));
            }
        }

        return signer;
    }

    private boolean shouldCalculateAuth() {
        return QCloudStringUtils.isEmpty(header(HttpConstants.Header.AUTHORIZATION));
    }

    public static class Builder<T> extends HttpRequest.Builder<T> {

        private QCloudSignSourceProvider signProvider;
        private String signerType;
        private STSCredentialScope[] credentialScope;

        public Builder<T> signer(String signerType, QCloudSignSourceProvider signProvider) {
            this.signerType = signerType;
            this.signProvider = signProvider;
            return this;
        }

        public Builder<T> credentialScope(STSCredentialScope[] credentialScope) {
            this.credentialScope = credentialScope;
            return this;
        }

        @Override
        public Builder<T> url(URL url) {
            return (Builder<T>) super.url(url);
        }

        @Override
        public Builder<T> scheme(String scheme) {
            return (Builder<T>) super.scheme(scheme);
        }

        @Override
        public Builder<T> path(String path) {
            return (Builder<T>) super.path(path);
        }

        @Override
        public Builder<T> host(String host) {
            return (Builder<T>) super.host(host);
        }

        @Override
        public Builder<T> port(int port) {
            return (Builder<T>) super.port(port);
        }

        @Override
        public Builder<T> method(String method) {
            return (Builder<T>) super.method(method);
        }

        @Override
        public Builder<T> query(String key, String value) {
            return (Builder<T>) super.query(key, value);
        }

        @Override
        public Builder<T> contentMD5() {
            return (Builder<T>) super.contentMD5();
        }

        @Override
        public Builder<T> addHeader(String name, String value) {
            return (Builder<T>) super.addHeader(name, value);
        }

        @Override
        public Builder<T> removeHeader(String name) {
            return (Builder<T>) super.removeHeader(name);
        }

        @Override
        public Builder<T> userAgent(String userAgent) {
            return (Builder<T>) super.userAgent(userAgent);
        }

        @Override
        public Builder<T> setUseCache(boolean cacheEnabled) {
            return (Builder<T>) super.setUseCache(cacheEnabled);
        }

        @Override
        public Builder<T> body(RequestBodySerializer bodySerializer) {
            return (Builder<T>) super.body(bodySerializer);
        }

        @Override
        public Builder<T> tag(Object tag) {
            return (Builder<T>) super.tag(tag);
        }

        @Override
        public Builder<T> converter(ResponseBodyConverter<T> responseBodyConverter) {
            return (Builder<T>) super.converter(responseBodyConverter);
        }

        public QCloudHttpRequest<T> build() {
            prepareBuild();
            return new QCloudHttpRequest<>(this);
        }
    }
}
