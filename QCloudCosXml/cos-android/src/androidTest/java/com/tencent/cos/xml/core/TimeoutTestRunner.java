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

package com.tencent.cos.xml.core;

import android.os.Bundle;
import android.util.Log;

import androidx.test.runner.AndroidJUnitRunner;

/**
 * 自定义测试运行器
 * 为所有测试添加13分钟超时限制
 * 
 * 使用方法：在 build.gradle 中配置:
 * testInstrumentationRunner 'com.tencent.cos.xml.core.TimeoutTestRunner'
 */
public class TimeoutTestRunner extends AndroidJUnitRunner {

    private static final String TAG = "TimeoutTestRunner";
    // 13分钟 = 780000毫秒
    private static final String DEFAULT_TIMEOUT_MSEC = "780000";

    @Override
    public void onCreate(Bundle arguments) {
        Log.i(TAG, "TimeoutTestRunner onCreate");

        // 设置默认超时：13分钟 = 780000毫秒
        if (arguments == null) {
            arguments = new Bundle();
        }

        if (!arguments.containsKey("timeout_msec")) {
            arguments.putString("timeout_msec", DEFAULT_TIMEOUT_MSEC);
            Log.i(TAG, "Set default timeout_msec: " + DEFAULT_TIMEOUT_MSEC);
        }

        // 注册超时监听器
        String existingListener = arguments.getString("listener");
        String timeoutListener = TimeoutRunListener.class.getName();
        if (existingListener != null && !existingListener.isEmpty()) {
            // 如果已有其他监听器，追加
            arguments.putString("listener", existingListener + "," + timeoutListener);
        } else {
            arguments.putString("listener", timeoutListener);
        }
        Log.i(TAG, "Registered TimeoutRunListener");

        super.onCreate(arguments);
    }
}
