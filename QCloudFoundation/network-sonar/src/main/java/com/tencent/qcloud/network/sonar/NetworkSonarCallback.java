package com.tencent.qcloud.network.sonar;

import java.util.List;

/**
 * <p>
 * Created by jordanqin on 2024/8/16 17:14.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public interface NetworkSonarCallback {
    void onStart(SonarType type);

    void onSuccess(SonarResult result);

    void onFail(SonarResult result);

    void onFinish(List<SonarResult> results);
}