package com.tencent.qcloud.network.sonar;

/**
 * <p>
 * Created by jordanqin on 2024/9/4 11:02.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class SonarCallback {
    public interface Step<T> {
        void step(T result);
    }
}
