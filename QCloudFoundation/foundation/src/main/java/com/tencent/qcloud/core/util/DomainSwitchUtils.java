package com.tencent.qcloud.core.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * <p>
 * Created by jordanqin on 2023/12/11 17:20.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class DomainSwitchUtils {
    public static String DOMAIN_MYQCLOUD = "myqcloud.com";
    public static String DOMAIN_TENCENTCOS = "tencentcos.cn";

    public static boolean isMyqcloudUrl(String url) {
        if(TextUtils.isEmpty(url)) return false;

        // Matches *.cos.*.myqcloud.com
        Pattern pattern1 = Pattern.compile(".*\\.cos\\..*\\.myqcloud\\.com");

        // Matches service.cos.myqcloud.com
        Pattern pattern2 = Pattern.compile("service\\.cos\\.myqcloud\\.com");

        // Matches cos.*.myqcloud.com
        Pattern pattern3 = Pattern.compile("cos\\..*\\.myqcloud\\.com");

        // Matches *.cos.accelerate.myqcloud.com
        Pattern pattern4 = Pattern.compile(".*\\.cos\\.accelerate\\.myqcloud\\.com");

        return pattern1.matcher(url).matches() && !pattern2.matcher(url).matches() &&
                !pattern3.matcher(url).matches() && !pattern4.matcher(url).matches();
    }
}
