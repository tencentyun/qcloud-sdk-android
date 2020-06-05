package com.tencent.cos.xml.constraints;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.impl.constraints.trackers.NetworkStateTracker;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;

/**
 * Created by rickenwang on 2019-11-27.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@SuppressLint("RestrictedApi")
public class TransferTrackers {

    private static TransferTrackers instance;

    public static synchronized TransferTrackers getInstance(Context context, TaskExecutor taskExecutor) {

        if (instance == null) {
            instance = new TransferTrackers(context, taskExecutor);
        }
        return instance;

    }


    private NetworkStateTracker mNetworkStateTracker;


    private TransferTrackers(@NonNull Context context, @NonNull TaskExecutor taskExecutor) {
        Context appContext = context.getApplicationContext();
        mNetworkStateTracker = new NetworkStateTracker(appContext, taskExecutor);
    }


    @NonNull
    public NetworkStateTracker getNetworkStateTracker() {
        return mNetworkStateTracker;
    }
}
