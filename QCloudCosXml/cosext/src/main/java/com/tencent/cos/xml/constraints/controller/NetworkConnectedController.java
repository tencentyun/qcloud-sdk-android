package com.tencent.cos.xml.constraints.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;

import com.tencent.cos.xml.constraints.TransferSpec;
import com.tencent.cos.xml.constraints.TransferTrackers;

import static androidx.work.NetworkType.CONNECTED;

/**
 * Created by rickenwang on 2019-11-27.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@SuppressLint("RestrictedApi")
public class NetworkConnectedController extends ConstraintController<NetworkState> {
    public NetworkConnectedController(Context context, TaskExecutor taskExecutor) {
        super(TransferTrackers.getInstance(context, taskExecutor).getNetworkStateTracker());
    }

    @Override
    public boolean hasConstraint(@NonNull TransferSpec transferSpec) {
        return transferSpec.constraints.getRequiredNetworkType() == CONNECTED;
    }

    @Override
    public boolean isConstrained(@NonNull NetworkState state) {
        if (Build.VERSION.SDK_INT >= 26) {
            return !state.isConnected() || !state.isValidated();
        } else {
            return !state.isConnected();
        }
    }
}
