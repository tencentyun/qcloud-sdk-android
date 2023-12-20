package com.tencent.cos.xml;

import android.app.Application;

import com.tencent.qcloud.track.BuildConfig;

/**
 * <p>
 * Created by jordanqin on 2023/9/14 21:38.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        trackInit();
    }

    private void trackInit(){
        CosTrackService.initCLs(this,
                "9ab664b4-f657-4ef9-8b8c-305b819e477d",
                "ap-guangzhou.cls.tencentcs.com",
                BuildConfig.CLS_SECRET_ID,
                BuildConfig.CLS_SECRET_KEY);
    }
}
