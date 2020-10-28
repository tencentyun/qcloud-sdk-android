/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

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
