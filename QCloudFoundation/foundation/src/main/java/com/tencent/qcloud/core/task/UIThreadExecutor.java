package com.tencent.qcloud.core.task;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * <p>
 * </p>
 * Created by wjielai on 2017/11/30.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

final class UIThreadExecutor implements Executor {
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public final void execute(@NonNull Runnable runnable) {
        mHandler.post(runnable);
    }
}
