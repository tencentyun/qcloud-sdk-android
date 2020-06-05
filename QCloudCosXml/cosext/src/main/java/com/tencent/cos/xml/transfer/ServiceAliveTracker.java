package com.tencent.cos.xml.transfer;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tencent.cos.xml.constraints.TransferDatabase;
import com.tencent.cos.xml.constraints.TransferSpec;
import com.tencent.cos.xml.constraints.TransferSpecDao;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by rickenwang on 2019-11-25.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class ServiceAliveTracker extends Worker {

    private String TAG = "ServiceAliveTracker";

    private static boolean isServiceAlive = false;

    public ServiceAliveTracker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        QCloudLogger.i(TAG, "monitor worker start!");

        Context context = getApplicationContext();

        if (isServiceAlive) {
            ServiceAliveTracker.start(context, 5);
            QCloudLogger.i(TAG, "service is started, monitor worker start delay!");
        } else {

            QCloudLogger.i(TAG, "service is not started, monitor worker start transfer worker!");
            BackgroundTransferShop.onServiceDead(getApplicationContext());
        }
        return Result.success();
    }


    public synchronized static void start(Context context) {

        if (isServiceAlive) {
            return;
        }

        isServiceAlive = true;
        start(context, 0);
    }

    private static void start(Context context, int delayInSecond) {

        OneTimeWorkRequest monitorRequest = new OneTimeWorkRequest.Builder(ServiceAliveTracker.class)
                .setInitialDelay(delayInSecond, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(monitorRequest);
    }


}
