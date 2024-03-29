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

package com.tencent.qcloud.core;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
