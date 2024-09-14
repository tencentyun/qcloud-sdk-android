package com.tencent.qcloud.network.sonar.info;

import android.content.Context;

import com.tencent.qcloud.network.sonar.Sonar;
import com.tencent.qcloud.network.sonar.SonarRequest;
import com.tencent.qcloud.network.sonar.SonarResult;

/**
 * <p>
 * Created by jordanqin on 2024/8/19 11:04.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
// TODO: 2024/8/21 主要用于APP诊断，做APP诊断的时候再搞，主要需要传入context
public class InfoSonar implements Sonar<InfoResult> {
    private final Context appContext;

    public InfoSonar(Context appContext) {
        this.appContext = appContext;
    }

    public SonarResult<InfoResult> start(SonarRequest request) {
//        InfoResult infoResult = new InfoResult();
//        infoResult.setNetworkAvailable(Utils.isNetworkAvailable(appContext));
//        return new SonarResult<>(SonarType.NET_INFO, infoResult);
        return null;
    }

    @Override
    public void stop() {

    }
}
