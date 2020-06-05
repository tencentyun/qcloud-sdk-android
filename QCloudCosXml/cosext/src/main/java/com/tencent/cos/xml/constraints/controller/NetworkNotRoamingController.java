package com.tencent.cos.xml.constraints.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Logger;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;

import com.tencent.cos.xml.constraints.TransferSpec;

import static androidx.work.NetworkType.NOT_ROAMING;

/**
 * Created by rickenwang on 2019-12-02.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@SuppressLint("RestrictedApi")
public class NetworkNotRoamingController extends ConstraintController<NetworkState> {
    private static final String TAG = Logger.tagWithPrefix("NetworkNotRoamingCtrlr");

    public NetworkNotRoamingController(Context context, TaskExecutor taskExecutor) {
        super(Trackers.getInstance(context, taskExecutor).getNetworkStateTracker());
    }

    @Override
    public boolean hasConstraint(@NonNull TransferSpec transferSpec) {
        return transferSpec.constraints.getRequiredNetworkType() == NOT_ROAMING;
    }

    /**
     * Check for not-roaming constraint on API 24+, when JobInfo#NETWORK_TYPE_NOT_ROAMING was added,
     * to be consistent with JobScheduler functionality.
     */
    @Override
    public boolean isConstrained(@NonNull NetworkState state) {
        if (Build.VERSION.SDK_INT < 24) {
            Logger.get().debug(
                    TAG,
                    "Not-roaming network constraint is not supported before API 24, "
                            + "only checking for connected state.");
            return !state.isConnected();
        }
        return !state.isConnected() || !state.isNotRoaming();
    }
}
