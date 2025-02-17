package com.tencent.qcloud.network.sonar;

/**
 * <p>
 * Created by jordanqin on 2024/8/16 17:15.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public interface Sonar<T> {
    public static final String ERROR_MSG_NO_NETWORK = "Network is not available";
    public static final String ERROR_MSG_IP_IS_EMPTY = "request ip is null";
    public static final String ERROR_MSG_HOST_IS_EMPTY = "request host is null";
    public static final String ERROR_MSG_PING_PROCESS_IS_NULL = "ping process is null";

    SonarResult<T> start(SonarRequest request);
    void stop();
}
