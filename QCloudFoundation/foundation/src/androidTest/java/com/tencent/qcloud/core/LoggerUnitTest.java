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

import com.tencent.qcloud.core.logger.FileLogAdapter;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
