package com.tencent.qcloud.core;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudResultListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.http.HttpResult;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.http.QCloudHttpRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.http.ResponseBodyConverter;
import com.tencent.qcloud.core.logger.FileLogAdapter;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.RetryStrategy;
import com.tencent.qcloud.core.task.TaskExecutors;
import com.tencent.qcloud.core.util.QCloudHttpUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/5/30.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class HttpClientUnitTest {
    static final String TAG = "UnitTest";

    private Context context;
    private QCloudHttpClient httpClient;

    @Before
    public void setupContext() {
        context = InstrumentationRegistry.getContext();

        httpClient = new QCloudHttpClient.Builder().setRetryStrategy(RetryStrategy.FAIL_FAST).build();
        httpClient.setDebuggable(true);
        QCloudLogger.addAdapter(FileLogAdapter.getInstance(context, "httpLog"));
    }

    @Test
    public void testPutUri() throws IOException,InterruptedException {
        File dataFile = createFile(2000);
        Uri uri = Uri.fromFile(dataFile);

        RequestBodySerializer requestBodySerializer = RequestBodySerializer.uri("text/plain",
                uri, context, 100, 3000);
        put(requestBodySerializer, "/ut_tmp_uri.txt");
    }

    @Test
    public void testPutInputStream() throws IOException,InterruptedException {
        File dataFile = createFile(2000);
        File tmpFile = new File(context.getExternalCacheDir() + File.separator + "put_tmp");

        RequestBodySerializer requestBodySerializer = RequestBodySerializer.stream("text/plain", tmpFile,
                new FileInputStream(dataFile), 100, 1500);
        put(requestBodySerializer, "/ut_tmp_input_stream.txt");
    }

    @Test
    public void testPutFile() throws IOException,InterruptedException {
        File dataFile = createFile(2000);

        RequestBodySerializer requestBodySerializer = RequestBodySerializer.file("text/plain",
                dataFile);
        put(requestBodySerializer, "/ut_tmp_file.txt");
    }

    @Test
    public void testPutBytes() throws IOException,InterruptedException {
        byte[] bytes = new byte[2000];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 1;
        }

        RequestBodySerializer requestBodySerializer = RequestBodySerializer.bytes(null,
                bytes, 300, -1);
        put(requestBodySerializer, "/ut_tmp_bytes.txt");
    }

    @Test
    public void testPutUrl() throws IOException,InterruptedException {
        URL remoteUrl = new URL("http://public-1253653367.cosgz.myqcloud.com/uri_tmp_file.txt");
//        RequestBodySerializer requestBodySerializer = RequestBodySerializer.url("text/plain",
//                remoteUrl);
        RequestBodySerializer requestBodySerializer = RequestBodySerializer.url("text/plain",
                remoteUrl, 100, 400);
        put(requestBodySerializer, "/ut_tmp_url.txt");
    }

    @Test
    public void testGetUrl() throws InterruptedException {
        get("/uri_tmp_file.txt");
    }

    private void put(RequestBodySerializer requestBodySerializer, String path) throws InterruptedException {
        final Object lock = new Object();

        final QCloudHttpRequest<String> request = new QCloudHttpRequest.Builder<String>()
                .scheme("http")
                .method("PUT")
                .host("public-1253653367.cos.ap-guangzhou.myqcloud.com")
                .path(path)
                .body(requestBodySerializer)
                .build();
        final HttpTaskMetrics httpMetric = new HttpTaskMetrics();
        httpClient.resolveRequest(request)
                .attachMetric(httpMetric)
                .schedule()
                .addResultListener(new QCloudResultListener<HttpResult<String>>() {
                    @Override
                    public void onSuccess(HttpResult<String> result) {
                        Log.i(TAG, "thread = " + Thread.currentThread());
                        Assert.assertTrue(result.isSuccessful());
                        synchronized (lock) {
                            lock.notify();
                        }
                    }

                    @Override
                    public void onFailure(QCloudClientException clientException, QCloudServiceException serviceException) {
                        Log.i(TAG, "thread = " + Thread.currentThread());
                        Assert.assertTrue(true);
                        synchronized (lock) {
                            lock.notify();
                        }
                    }
                })
                .observeOn(TaskExecutors.UI_THREAD_EXECUTOR);

        synchronized (lock) {
            lock.wait();
        }
        TimeUnit.SECONDS.sleep(10);
    }

    private void get(String path) throws InterruptedException {
        final String localPath = context.getExternalCacheDir() + File.separator + "http_ut_get_" + path;

        final Object lock = new Object();

        final QCloudHttpRequest<Void> request = new QCloudHttpRequest.Builder<Void>()
                .scheme("http")
                .method("GET")
                .host("public-1253653367.cos.ap-guangzhou.myqcloud.com")
                .path(path)
                .converter(ResponseBodyConverter.file(localPath))
                .build();
        final HttpTaskMetrics httpMetric = new HttpTaskMetrics();
        httpClient.resolveRequest(request)
                .attachMetric(httpMetric)
                .schedule()
                .addResultListener(new QCloudResultListener<HttpResult<Void>>() {
                    @Override
                    public void onSuccess(HttpResult<Void> result) {
                        Log.i(TAG, "thread = " + Thread.currentThread());
                        Assert.assertTrue(result.isSuccessful());
                        Assert.assertTrue(new File(localPath).exists());
                        synchronized (lock) {
                            lock.notify();
                        }
                    }

                    @Override
                    public void onFailure(QCloudClientException clientException, QCloudServiceException serviceException) {
                        Log.i(TAG, "thread = " + Thread.currentThread());
                        Assert.assertTrue(true);
                        synchronized (lock) {
                            lock.notify();
                        }
                    }
                })
                .observeOn(TaskExecutors.UI_THREAD_EXECUTOR);

        synchronized (lock) {
            lock.wait();
        }
        TimeUnit.SECONDS.sleep(10);
    }

    @Nullable
    private File createFile(long length) throws IOException {
        String srcPath = context.getExternalCacheDir() + File.separator + "http_ut_tmp_file";
        File file = new File(srcPath);
        if (!file.exists() && file.createNewFile()) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.setLength(length);
            randomAccessFile.close();
        }
        return file.exists() ? file : null;
    }

    @Test
    public void testFileSort() {
        File dir = context.getExternalCacheDir();
        System.out.println(dir);
        File[] logFiles = dir.listFiles();
        Arrays.sort(logFiles, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return (int) (rhs.lastModified() - lhs.lastModified());
            }
        });
        File latest = logFiles[0];
        Assert.assertTrue(latest.getName().equals("QCloudLogs"));
    }

    @Test
    public void testUrlEncodingAndDecoding() {
        Assert.assertEquals(QCloudHttpUtils.urlDecodeString("%21%23%24%25%26%27%2A%2B%2C-."),
                "!#$%&'*+,-.");
        Assert.assertEquals(QCloudHttpUtils.urlEncodeString("!#$%&'*+,-."),
                "%21%23%24%25%26%27%2A%2B%2C-.");
    }
}
