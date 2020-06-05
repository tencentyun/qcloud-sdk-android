package com.tencent.cos.xml.common;

import com.tencent.cos.xml.BuildConfig;

/**
 * Created by bradyxiao on 2017/11/30.
 * cos android sdk version information
 */

public class VersionInfo {

    public static final int version = BuildConfig.VERSION_CODE;
    public static final String platform = "cos-android-sdk-" + BuildConfig.VERSION_NAME;

    public static String getUserAgent(){
        return platform;
    }
}
