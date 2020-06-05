package com.tencent.cos.xml.constraints;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.work.impl.utils.SerialExecutor;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by rickenwang on 2019-11-28.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@SuppressLint("RestrictedApi")
public class TransferExecutor implements TaskExecutor {

    private final SerialExecutor executor;

    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    private final Executor mMainThreadExecutor = new Executor() {
        @Override
        public void execute(@NonNull Runnable command) {
            postToMainThread(command);
        }
    };

    public TransferExecutor(SerialExecutor backgroundExecutor) {
        this.executor = backgroundExecutor;
    }

    @Override
    public void postToMainThread(Runnable r) {
        mMainThreadHandler.post(r);
    }

    @Override
    public Executor getMainThreadExecutor() {
        return mMainThreadExecutor;
    }

    @Override
    public void executeOnBackgroundThread(Runnable r) {
        executor.execute(r);
    }

    @Override
    public SerialExecutor getBackgroundExecutor() {
        return null;
    }

}
