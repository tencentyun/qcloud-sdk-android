package com.tencent.cos.xml.constraints.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Logger;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;

import com.tencent.cos.xml.constraints.TransferSpec;
import com.tencent.cos.xml.constraints.TransferTrackers;

import static androidx.work.NetworkType.METERED;

/**
 * Created by rickenwang on 2019-12-02.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@SuppressLint("RestrictedApi")
public class NetworkMeteredController extends ConstraintController<NetworkState> {
    private static final String TAG = Logger.tagWithPrefix("NetworkMeteredCtrlr");

    public NetworkMeteredController(Context context, TaskExecutor taskExecutor) {
        super(TransferTrackers.getInstance(context, taskExecutor).getNetworkStateTracker());
    }

    @Override
    public boolean hasConstraint(@NonNull TransferSpec transferSpec) {
        return transferSpec.constraints.getRequiredNetworkType() == METERED;
    }

    /**
     * Check for metered constraint on API 26+, when JobInfo#NETWORK_METERED was added, to
     * be consistent with JobScheduler functionality.
     */
    @Override
    public boolean isConstrained(@NonNull NetworkState state) {
        if (Build.VERSION.SDK_INT < 26) {
            Log.d(TAG, "Metered network constraint is not supported before API 26, "
                    + "only checking for connected state.");
            return !state.isConnected();
        }
        return !state.isConnected() || !state.isMetered();
    }
}