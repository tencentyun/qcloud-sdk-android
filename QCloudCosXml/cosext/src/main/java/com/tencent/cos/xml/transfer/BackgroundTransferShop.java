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

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.constraints.TransferSpec;
import com.tencent.cos.xml.constraints.TransferSpecDao;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.util.List;
import java.util.UUID;

public class BackgroundTransferShop {

    private static final String TAG = "BackgroundTransferShop";

    private static final String TAG_DEBUG = "COS-EXT";

    private TransferSpecDao transferSpecDao;

    private Context context;

    private ServiceAliveTracker aliveTracker;

    BackgroundTransferShop(Context context, TransferSpecDao transferSpecDao) {

        this.context = context;
        this.transferSpecDao = transferSpecDao;
    }

    @WorkerThread
    TransferSpec executeExistTransferTask(@NonNull TransferWorkManager transferWorkManager, @NonNull COSXMLTask cosxmlTask,
                                @NonNull TransferRequest transferRequest, @NonNull TransferSpec transferSpec) {

        if (isTransferComplete(transferSpec)) {
            invokeTransferTaskComplete(cosxmlTask);
        } else if (!TextUtils.isEmpty(transferSpec.workerId)) {

            WorkManager.getInstance(context).cancelWorkById(UUID.fromString(transferSpec.workerId));
            resetTransferSpecWorkerIdInDb(transferSpec.id);
        }

        transferSpec.workerId = "";
        return transferSpec;
    }

    void resetTransferSpecWorkerIdInDb(String transferSpecId) {
        transferSpecDao.updateTransferSpecWorkerId(transferSpecId, "");
    }

    boolean isTransferComplete(@NonNull TransferSpec transferSpec) {

        return transferSpec.state == TransferState.COMPLETED;
    }

    private void invokeTransferTaskComplete(COSXMLTask task) {

        task.taskState = TransferState.COMPLETED;
        if (task.cosXmlResultListener != null) {
            task.cosXmlResultListener.onSuccess(task.buildCOSXMLTaskRequest(), task.buildCOSXMLTaskResult(null));
        }

        if (task.transferStateListener != null) {
            task.transferStateListener.onStateChanged(TransferState.FAILED);
        }
    }


    static void onServiceDead(Context context) {

        QCloudLogger.i(TAG_DEBUG, "onServiceDead");

        TransferSpecDao transferSpecDao = TransferScheduler.getInstance(context).getTransferSpecDao();
        List<TransferSpec> transferSpecs = transferSpecDao.getAllTransferSpecs();
        for (TransferSpec transferSpec : transferSpecs) {

            if (TextUtils.isEmpty(transferSpec.workerId) && isTransferRunning(transferSpec)) {
                String workerId = startUploadWorker(context, transferSpec.id);
                transferSpecDao.updateTransferSpecWorkerId(transferSpec.id, workerId);
                QCloudLogger.i(TAG, "start transfer worker with id " + transferSpec.id);
                QCloudLogger.i(TAG_DEBUG, "start transfer worker with id " + transferSpec.id);
            }
        }
    }

    private static boolean isTransferRunning(@NonNull TransferSpec transferSpec) {

        return transferSpec.state == TransferState.IN_PROGRESS ||
                transferSpec.state == TransferState.RESUMED_WAITING ||
                transferSpec.state == TransferState.UNKNOWN ||
                transferSpec.state == TransferState.WAITING;
    }

    private static String startUploadWorker(Context context, String transferSpecId) {

        OneTimeWorkRequest.Builder builder = new OneTimeWorkRequest.Builder(UploadWorker.class);
        WorkRequest workRequest = builder
                .setInputData(new Data.Builder().putString("id", transferSpecId).build())
                .build();
        WorkManager.getInstance(context).enqueue(workRequest);

        return workRequest.getId().toString();
    }
}
