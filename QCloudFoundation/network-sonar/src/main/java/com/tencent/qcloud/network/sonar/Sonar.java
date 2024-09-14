package com.tencent.qcloud.network.sonar;

/**
 * <p>
 * Created by jordanqin on 2024/8/16 17:15.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public interface Sonar<T> {
    SonarResult<T> start(SonarRequest request);
    void stop();
}
