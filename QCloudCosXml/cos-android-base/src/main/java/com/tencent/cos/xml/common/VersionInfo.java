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

package com.tencent.cos.xml.common;

import android.text.TextUtils;

import com.tencent.cos.xml.base.BuildConfig;

/**
 * cos android sdk版本信息
 */
public class VersionInfo {
    public static String sdkName;
    public static int versionCode = -1;
    public static String versionName;

    public static String getUserAgent(){
        return String.format("%s-android-sdk-%s", getSdkName(), getVersionName());
    }

    public static String getQuicUserAgent() {
        return String.format("%s-android-quic-sdk-%s", getSdkName(), getVersionName());
    }

    public static String getSdkName() {
        if(TextUtils.isEmpty(sdkName)){
            sdkName = "cos-base";
        }
        return sdkName;
    }

    public static String getVersionName() {
        if(TextUtils.isEmpty(versionName)){
            versionName = BuildConfig.VERSION_NAME;
        }
        return versionName;
    }

    public static int getVersionCode() {
        if(versionCode == -1){
            versionCode = BuildConfig.VERSION_CODE;
        }
        return versionCode;
    }
}
