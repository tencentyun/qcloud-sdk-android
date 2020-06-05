package com.tencent.cos.xml.transfer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.utils.SerialExecutor;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.constraints.TransferConstraintsTracker;
import com.tencent.cos.xml.constraints.TransferExecutor;
import com.tencent.cos.xml.constraints.TransferSpec;
import com.tencent.cos.xml.constraints.TransferSpecDao;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.utils.MapUtils;
import com.tencent.qcloud.core.auth.COSXmlSigner;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by rickenwang on 2019-12-06.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class NormalTransferShop extends TransferShop {


    private Map<String, COSXMLTask> cosxmlTasks;

    private Map<String, TransferSpec> transferSpecs;

    private TransferConstraintsTracker constraintsTracker;

    @SuppressLint("RestrictedApi")
    NormalTransferShop(Context context, SerialExecutor executor, TransferSpecDao transferSpecDao) {

        super(transferSpecDao);
        this.transferSpecDao = transferSpecDao;
        cosxmlTasks = new HashMap<>();
        transferSpecs = new HashMap<>();

        constraintsTracker = new TransferConstraintsTracker(context, new TransferExecutor(executor),
                new WorkConstraintsCallback() {

                    @Override
                    public void onAllConstraintsMet(@NonNull List<String> transferSpecIds) {

                        executor.execute(() -> {
                            constraintsMetTransfers(transferSpecIds);
                        });

                    }

                    @Override
                    public void onAllConstraintsNotMet(@NonNull List<String> transferSpecIds) {

                        executor.execute(() -> {
                            constraintsNotMetTransfers(transferSpecIds);
                        });
                    }
                });
    }

    /**
     * 执行一个非首次提交的传输任务
     *
     * @param cosxmlTask 当前传输请求的 COSXMLTask
     * @param transferRequest 当前传输请求
     * @param transferSpec 之前传输请求的 TransferSpec
     */
    @WorkerThread
    void executeExistTransferTask(@NonNull TransferWorkManager transferWorkManager, @NonNull COSXMLTask cosxmlTask, @NonNull TransferRequest transferRequest, @NonNull TransferSpec transferSpec) {

        /**
         * 如果之前的传输任务是上次 App 进程启动的任务
         *
         */
        if (!isTransferTaskExist(transferSpec.id)) {
            recordTransferTaskAndSpec(cosxmlTask, isTransferContentChanged(transferSpec, transferRequest) ?
                    updateTransferSpec(transferWorkManager, transferSpec, transferRequest) : transferSpec);
            return;
        }

        // 否则是本次 App 进程启动的任务
        if (isTransferContentChanged(transferSpec, transferRequest)) {

            COSXMLTask oldTask = cosxmlTasks.get(transferSpec.id);
            if (oldTask != null) {
                invokeTransferTaskFailed(oldTask);
            }
            recordTransferTaskAndSpec(cosxmlTask, updateTransferSpec(transferWorkManager, transferSpec, transferRequest));

        } else {
            invokeTransferTaskFailed(cosxmlTask);
        }
    }

    void executeNewTransferTask(@NonNull TransferWorkManager transferWorkManager, @NonNull COSXMLTask cosxmlTask, @NonNull TransferRequest transferRequest) {

        TransferSpec transferSpec = null;
        if (transferRequest instanceof UploadRequest) {
            transferSpec = new TransferSpec((UploadRequest) transferRequest);
        } else if (transferRequest instanceof DownloadRequest) {
            transferSpec = new TransferSpec((DownloadRequest) transferRequest);
        }

        if (transferSpec != null) {
            transferSpec.setServiceEgg(transferWorkManager.toBytes());
        }
        recordTransferTaskAndSpec(cosxmlTask, transferSpec);
    }

    private void recordTransferTaskAndSpec( @NonNull COSXMLTask cosxmlTask, @NonNull TransferSpec transferSpec) {

        cosxmlTasks.put(transferSpec.id, cosxmlTask);
        transferSpecs.put(transferSpec.id, transferSpec);
        constraintsTracker.replace(new LinkedList<>(transferSpecs.values()));
        transferSpecDao.insertTransferSpec(transferSpec);
    }

    private TransferSpec updateTransferSpec(@NonNull TransferWorkManager transferWorkManager, TransferSpec transferSpec, TransferRequest transferRequest) {

        transferSpec.uploadId = "";
        transferSpec.headers = MapUtils.map2Json(transferRequest.getHeaders());
        transferSpec.state = TransferState.UNKNOWN;
        transferSpec.transferServiceEgg = new String(transferWorkManager.toBytes());
        transferSpec.constraints = transferRequest.getConstraints();

        return transferSpec;
    }

    private boolean isTransferContentChanged(@NonNull TransferSpec transferSpec, TransferRequest transferRequest) {
        return false;
    }

    private boolean isTransferTaskExist(String transferSpecId) {

        return cosxmlTasks.containsKey(transferSpecId);
    }



    /**
     * 约束条件被满足
     *
     * @param transferSpecIds
     */
    private void constraintsMetTransfers(@NonNull List<String> transferSpecIds) {

        List<TransferSpec> transferSpecs = transferSpecDao.getTransferSpecs(transferSpecIds);

        for (TransferSpec transferSpec : transferSpecs) {

            /**
             * 1、调用 TransferManager#upload() 方法后，一直处于 CONSTRAINED 状态
             * 2、调用 TransferManager#upload() 方法后，先 IN_PROGRESS 后 CONSTRAINED
             * 3、本次启动没有调用  TransferManager#upload() 方法，而是上次 app 启动时调用的，
             *    这种交给 WorkManager 处理，TransferSchedule 可以直接忽略
             */
            if (isTransferByTask(transferSpec)) {

                COSXMLTask cosxmlTask = cosxmlTasks.get(transferSpec.id);

                // 2
                if (cosxmlTask != null && cosxmlTask.getTaskState() == TransferState.CONSTRAINED) {
                    cosxmlTask.constraintSatisfied();
                }

                // 1
                // if (cosxmlTask != null && cosxmlTask.getTaskState() == TransferState.UNKNOWN) {
                if (cosxmlTask != null && transferSpec.state == TransferState.UNKNOWN) {
                    startTask(cosxmlTask, transferSpec.uploadId);
                }

                observeCosTask(cosxmlTask, transferSpec.id, null, new CosXmlProgressListener() {
                    @Override
                    public void onProgress(long complete, long target) {

                        QCloudLogger.i("COS-EXT", "transfer cos key is " + transferSpec.key + " : complete " + complete + ", target " + target);
                    }
                });
            }
        }

    }

    /**
     *
     * @param transferSpecIds
     */
    private void constraintsNotMetTransfers(@NonNull List<String> transferSpecIds) {

        List<TransferSpec> transferSpecs = transferSpecDao.getTransferSpecs(transferSpecIds);

        for (TransferSpec transferSpec : transferSpecs) {

            if (isTransferByTask(transferSpec) && transferSpec.state != TransferState.CONSTRAINED) {

                COSXMLTask cosxmlTask = cosxmlTasks.get(transferSpec.id);
                if (cosxmlTask != null && isTaskRunning(cosxmlTask.getTaskState())) {
                    cosxmlTask.constraintUnSatisfied();
                }
            }

        }
    }




}
