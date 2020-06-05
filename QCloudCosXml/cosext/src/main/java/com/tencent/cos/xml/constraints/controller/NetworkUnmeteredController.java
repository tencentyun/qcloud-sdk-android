package com.tencent.cos.xml.constraints.controller;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;

import com.tencent.cos.xml.constraints.TransferSpec;

import static androidx.work.NetworkType.UNMETERED;

/**
 * Created by rickenwang on 2019-12-02.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@SuppressLint("RestrictedApi")
public class NetworkUnmeteredController extends ConstraintController<NetworkState> {

    public NetworkUnmeteredController(
            @NonNull Context context,
            @NonNull TaskExecutor taskExecutor) {

        super(Trackers.getInstance(context, taskExecutor).getNetworkStateTracker());
    }

    @Override
    public boolean hasConstraint(@NonNull TransferSpec transferSpec) {
        return transferSpec.constraints.getRequiredNetworkType() == UNMETERED;
    }

    @Override
    public boolean isConstrained(@NonNull NetworkState state) {
        return !state.isConnected() || state.isMetered();
    }
}