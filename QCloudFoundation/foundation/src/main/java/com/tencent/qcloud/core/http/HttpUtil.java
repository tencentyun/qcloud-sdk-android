package com.tencent.qcloud.core.http;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020-04-23.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class HttpUtil {

    public static boolean isNetworkTimeoutError(Exception e) {
        return e instanceof SocketTimeoutException ||
                e instanceof ConnectException ||
                e instanceof NoRouteToHostException ||
                e instanceof UnknownHostException;
    }
}
