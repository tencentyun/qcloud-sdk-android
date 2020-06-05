package com.tencent.qcloud.core;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tencent.qcloud.core.logger.FileLogAdapter;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/5/29.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class LoggerUnitTest {

    static final String TAG = "UnitTest";

    @Before
    public void setupLogger() {
        Context context = InstrumentationRegistry.getContext();
        QCloudLogger.addAdapter(FileLogAdapter.getInstance(context, "unit_test"));
    }

    @Test
    public void testTextLog() {
        QCloudLogger.v(TAG, "this is a verbose message");
        QCloudLogger.d(TAG, "this is a debug message");
        QCloudLogger.i(TAG, "this is a info message");
        QCloudLogger.w(TAG, "this is a warn message, current time is %s", System.currentTimeMillis());
        QCloudLogger.e(TAG, "this is a error message, current time is %s", System.currentTimeMillis());

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void testExceptionLog() {
        Throwable exception = new IOException("A IO exception happens");
        QCloudLogger.w(TAG, exception, "this is a warn message");
        QCloudLogger.e(TAG, exception, "this is a error message");

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {

        }
    }
}
