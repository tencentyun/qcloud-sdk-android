package com.tencent.cos.xml;

import android.app.Application;

import com.tencent.cos.xml.core.TestUtils;

/**
 * <p>
 * Created by jordanqin on 2023/9/14 21:38.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                // 在这里处理异常，例如记录日志
                TestUtils.printError(throwable.getMessage());
            }
        });
        trackInit();
    }

    private void trackInit(){
//        CosTrackService.initCLs(this,
//                "9ab664b4-f657-4ef9-8b8c-305b819e477d",
//                "ap-guangzhou.cls.tencentcs.com",
//                TestConfig.CLS_SECRET_ID,
//                TestConfig.CLS_SECRET_KEY);
    }
}
