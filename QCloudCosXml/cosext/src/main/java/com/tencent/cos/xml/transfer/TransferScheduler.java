package com.tencent.cos.xml.transfer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.NetworkType;
import androidx.work.WorkManager;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.utils.SerialExecutor;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.constraints.TransferConstraintsTracker;
import com.tencent.cos.xml.constraints.TransferDatabase;
import com.tencent.cos.xml.constraints.TransferExecutor;
import com.tencent.cos.xml.constraints.TransferSpec;
import com.tencent.cos.xml.constraints.TransferSpecDao;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

/**
 *
 *
 * Created by rickenwang on 2019-11-28.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@SuppressLint("RestrictedApi")
public class TransferScheduler {

    private final String TAG = "TransferSchedule";

    private static volatile TransferScheduler instance;

    private Context context;

    private SerialExecutor executor;

    private TransferDatabase transferDatabase;

    private TransferSpecDao transferSpecDao;

    private BackgroundTransferShop backgroundTransferShop;

    private NormalTransferShop normalTransferShop;

    public static synchronized TransferScheduler getInstance(Context context) {

        if (instance == null) {
            instance = new TransferScheduler(context);
        }
        return instance;
    }

    private TransferScheduler(Context context) {

        this.context = context;
        this.executor = new SerialExecutor(Executors.newFixedThreadPool(1));
        transferDatabase = TransferDatabase.create(context);
        transferSpecDao = transferDatabase.transferSpecDao();
        backgroundTransferShop = new BackgroundTransferShop(context, transferSpecDao);
        normalTransferShop = new NormalTransferShop(context, executor, transferSpecDao);
    }


    public void schedule(TransferRequest transferRequest, COSXMLTask cosxmlTask, TransferWorkManager transferWorkManager) {

        executor.execute(() -> {

            TransferSpec existSpec =  getTransferSpecByTransferRequest(transferRequest);

            if (existSpec != null) { // 之前提交过相同的任务

                existSpec = backgroundTransferShop.executeExistTransferTask(transferWorkManager,
                        cosxmlTask, transferRequest, existSpec);

                normalTransferShop.executeExistTransferTask(transferWorkManager,
                        cosxmlTask, transferRequest, existSpec);
            } else {
                normalTransferShop.executeNewTransferTask(transferWorkManager, cosxmlTask, transferRequest);
            }
        });
    }

    @Nullable private TransferSpec getTransferSpecByTransferRequest(TransferRequest transferRequest) {

        String bucket  = transferRequest.getBucket();
        String cosKey = "";
        String filePath = "";

        if (transferRequest instanceof UploadRequest) {
            UploadRequest uploadRequest = (UploadRequest) transferRequest;
            cosKey = uploadRequest.getCosKey();
            filePath = uploadRequest.getFilePath();

        }
        return transferSpecDao.getTransferSpecByEndpoint(bucket, cosKey, filePath);
    }


    public TransferSpecDao getTransferSpecDao() {
        return transferSpecDao;
    }

}
