package com.tencent.qcloud.core;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tencent.qcloud.core.auth.COSXmlSignSourceProvider;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudSignSourceProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudResultListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpResult;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.http.QCloudHttpRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.http.ResponseFileConverter;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.RetryStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/11/19.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class CosUnitTest {

    private Context context;
    private QCloudHttpClient httpClient;
    private QCloudCredentialProvider credentialProvider;

    private String bucket = "android-ut-persist-bucket-" ;//+ BuildConfig.COSAppId;
    private String region = "ap-guangzhou";

    @Before
    public void setupContext() {
        context = InstrumentationRegistry.getContext();

        httpClient = new QCloudHttpClient.Builder().setRetryStrategy(RetryStrategy.FAIL_FAST).build();
        httpClient.setDebuggable(true);

        credentialProvider = new ShortTimeCredentialProvider(
//                BuildConfig.COSSecretId,
//                BuildConfig.COSSecretKey,
                "", "",
                600
        );
    }

    @Test
    public void testPutBytes() throws InterruptedException {
        final String path = "/ut_tmp_bytes.txt";
        final byte[] bytes = new byte[1000 * 1000];

        final QCloudHttpRequest.Builder<String> request = new QCloudHttpRequest.Builder<String>()
                .method("PUT")
                .path(path)
                .contentMD5()
                .body(RequestBodySerializer.bytes(null,
                        bytes, 300, -1));

        exec(request);
    }

    @Test
    public void testGetObject() throws InterruptedException {
        final String path = "ip.txt";

        final QCloudHttpRequest.Builder<Void> request = new QCloudHttpRequest.Builder<Void>()
                .method("GET")
                .path(path)
                .converter(ResponseFileConverter.file(getLocalFilePathPrefix() + path));

        exec(request);
    }

    @Test
    public void testSignParamAndHeader() throws InterruptedException {
        final String path = "ip.txt";

        final QCloudHttpRequest.Builder<Void> request = new QCloudHttpRequest.Builder<Void>()
                .method("GET")
                .path(path)
                .converter(ResponseFileConverter.file(getLocalFilePathPrefix() + path))
                .query("prefix", "!#$%&'*+,-.")
                .addHeader("b2", "a#c")
                ;

        COSXmlSignSourceProvider sourceProvider = defaultSourceProvider();

        exec(request, sourceProvider);
    }

    private String getEndpoint() {
        return bucket + ".cos." + region + ".myqcloud.com";
    }

    private String getLocalFilePathPrefix() {
        return context.getExternalCacheDir() + File.separator + "cos_ut_tmp_";
    }

    private COSXmlSignSourceProvider defaultSourceProvider() {
        return new COSXmlSignSourceProvider();
    }

    private <T> void exec(QCloudHttpRequest.Builder<T> requestBuilder) throws InterruptedException {
        exec(requestBuilder, defaultSourceProvider());
    }

    private <T> void exec(QCloudHttpRequest.Builder<T> requestBuilder, QCloudSignSourceProvider sourceProvider)
            throws InterruptedException {
        final Object lock = new Object();

        requestBuilder
                .scheme("http")
                .host(getEndpoint())
                .signer("CosXmlSigner", sourceProvider);
        QCloudHttpRequest<T> request = requestBuilder.build();


        HttpTaskMetrics metrics = new HttpTaskMetrics() {
            @Override
            public void onDataReady() {
                super.onDataReady();
                QCloudLogger.i("QCloudHttp", toString());
            }
        };

        httpClient.resolveRequest(request, credentialProvider)
                .attachMetric(metrics)
                .schedule()
                .addResultListener(new QCloudResultListener<HttpResult<T>>() {
                    @Override
                    public void onSuccess(HttpResult<T> result) {
                        Assert.assertTrue(result.isSuccessful());
                        synchronized (lock) {
                            lock.notify();
                        }
                    }

                    @Override
                    public void onFailure(QCloudClientException clientException,
                                          QCloudServiceException serviceException) {
                        Assert.assertTrue(false);
                        synchronized (lock) {
                            lock.notify();
                        }
                    }
                });

        synchronized (lock) {
            lock.wait();
        }
    }


}
