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

import static com.tencent.cos.xml.transfer.COSDownloadTask.TASK_UNKNOWN_STATUS;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.BeaconService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.crypto.COSDirect;
import com.tencent.cos.xml.crypto.CryptoModuleBase;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.ObjectRequest;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import bolts.CancellationTokenSource;

/**
 * 传输任务
 *
 * <p>
 * Created by rickenwang on 2021/6/30.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public abstract class COSTransferTask {

    protected String region, bucket, key;

    protected CosXmlProgressListener cosXmlProgressListener;
    protected CosXmlResultListener cosXmlResultListener;
    protected TransferStateListener transferStateListener;

    volatile TransferState taskState  = TransferState.WAITING;

    protected CosXmlRequest cosXmlRequest;
    protected CosXmlResult cosXmlResult;
    protected CosXmlClientException clientException;
    protected CosXmlServiceException serviceException;

    /**
     * 任务运行的整体情况
     */
    protected volatile TransferTaskMetrics transferTaskMetrics;

    protected volatile String taskId;

    // 用户手动调用了暂停
    protected volatile boolean manualPause = false;

    // 用户手动调用了取消
    protected volatile boolean manualCancel = false;

    protected volatile CancellationTokenSource mTransferTaskCts;  

    private final String TAG;

    // 客户端加密
    protected CosXmlServiceConfig cosXmlServiceConfig;
    protected COSDirect cosDirect;
    protected CryptoModuleBase cryptoModuleBase;

    COSTransferTask(COSDirect cosDirect, ObjectRequest cosXmlRequest) {
        cosXmlServiceConfig = cosDirect.getCosService().getConfig();
        taskId = UUID.randomUUID().toString();
        this.cosXmlRequest = cosXmlRequest;
        this.cosDirect = cosDirect;
        bucket = cosXmlRequest.getBucket();
        region = cosXmlRequest.getRegion();
        if (TextUtils.isEmpty(region)) {
            region = cosXmlServiceConfig.getRegion();
            cosXmlRequest.setRegion(region);
        }
        key = cosXmlRequest.getCosPath();
        TAG = tag();

        if (cosDirect.isTransferSecurely()) {
            loggerInfo(TAG, taskId, "encrypted transmission enabled");
        }
        loggerInfo(TAG, taskId, "create a %s task, region: %s, bucket: %s, key: %s",
                cosXmlRequest.getClass().getSimpleName(), region, bucket, key);
    }

    public void setCosXmlResultListener(CosXmlResultListener cosXmlResultListener) {
        this.cosXmlResultListener = cosXmlResultListener;
        
        if (taskState == TransferState.COMPLETED) {
            notifyTransferResultSuccess(cosXmlRequest, cosXmlResult);
        } else if (taskState == TransferState.FAILED) {
            notifyTransferResultFailed(cosXmlRequest, clientException, serviceException);
        }
    }

    public void setTransferStateListener(TransferStateListener transferStateListener) {
        this.transferStateListener = transferStateListener;
        
        notifyTransferStateChange();
    }

    public void setCosXmlProgressListener(CosXmlProgressListener cosXmlProgressListener) {
        this.cosXmlProgressListener = cosXmlProgressListener;
    }


    /**
     * 开始传输，内部调用
     */
    protected void start() {
        manualPause = false;
        manualCancel = false;
        transferTaskMetrics = new TransferTaskMetrics();
        transferTaskMetrics.domain = cosXmlRequest.getRequestHost(cosXmlServiceConfig);
        onTransferWaiting();
        mTransferTaskCts = new CancellationTokenSource();
        executor().execute(new TransferRunnable());
    }

    /**
     * 暂停传输
     */
    public void pause() {

        if (taskState != TransferState.IN_PROGRESS && taskState != TransferState.WAITING) {
            loggerInfo(TAG, taskId, "cannot pause upload task in state %s", taskState);
            return;
        }
        loggerInfo(TAG, taskId, "pause upload task");

        // 快速回调状态
        manualPause = true;
        onTransferPaused();
        if (mTransferTaskCts != null) mTransferTaskCts.cancel();
    }

    public void pause(boolean now) {
        pause();
    }

    /**
     * 恢复传输
     */
    public void resume() {
        if (taskState != TransferState.PAUSED) {
            loggerInfo(TAG, taskId, "cannot resume upload task in state %s", taskState);
            return;
        }
        loggerInfo(TAG, taskId, "resume upload task", taskState);
        start();
    }

    /**
     * 取消传输，如果是分片上传会调用 abort 接口
     */
    public void cancel() {
        
        loggerInfo(TAG, taskId, "cancel upload task");

        // 快速回调状态
        manualCancel = true;
        onTransferFailed(cosXmlRequest, CosXmlClientException.manualCancelException(), null);
        if (mTransferTaskCts != null) mTransferTaskCts.cancel();
    }

    public void cancel(boolean now) {
        cancel();
    }

    /**
     * 获得本次传输的运行信息
     *
     * @return TransferTaskMetrics
     */
    public TransferTaskMetrics getTransferTaskMetrics() {
        return transferTaskMetrics;
    }

    abstract protected String tag();

    abstract protected Executor executor();

    private class TransferRunnable implements Runnable {

        @Override
        public void run() {
            try {
                onTransferInProgress();
                checking();
                cosXmlResult = execute();
                onTransferSuccess(cosXmlRequest, cosXmlResult);
            } catch (CosXmlClientException clientException) {
                // 手动取消和暂停报错不在这里回调，否则可能会长时间阻塞
                if (!isManualPaused() && !isManualCanceled()) {
                    //TASK_UNKNOWN_STATUS状态下不进行处理 task会继续进行
                    if(!TASK_UNKNOWN_STATUS.equals(clientException.getMessage())){
                        onTransferFailed(cosXmlRequest, clientException, null);
                    }
                }
            } catch (CosXmlServiceException serviceException) {
                if (!isManualPaused() && !isManualCanceled()) {
                    onTransferFailed(cosXmlRequest, null, serviceException);
                }
            } catch (Exception exception) {
                if (!isManualPaused() && !isManualCanceled()) {
                    onTransferFailed(cosXmlRequest, new CosXmlClientException(ClientErrorCode.UNKNOWN.getCode(), exception.getMessage()),
                            null);
                }
            }
        }
    }

//    private void throwException() throws CosXmlClientException, CosXmlServiceException {
//        throw new CosXmlClientException(INTERNAL_ERROR);
//    }

    /**
     * 执行上传任务
     */
    abstract protected CosXmlResult execute() throws Exception;

    /**
     * 检查传输参数，并计算额外参数
     * 
     * @throws CosXmlClientException
     */
    protected void checking() throws CosXmlClientException {

        if (TextUtils.isEmpty(bucket)) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "bucket is null");
        }

        if (TextUtils.isEmpty(region)) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "region is null");
        }
    }

    public TransferState getTaskState() {
        return taskState;
    }

    /**
     * 
     */
    protected void onTransferWaiting() {
        taskState = TransferState.WAITING;
        // 开始计时
        transferTaskMetrics.onStart();
        notifyTransferStateChange();
    }
    
    protected void onTransferInProgress() {
        taskState = TransferState.IN_PROGRESS;

        transferTaskMetrics.onInProgress();
        notifyTransferStateChange();
    }
    
    protected void onTransferPaused() {
        taskState = TransferState.PAUSED;

        // 暂停也会计算 TransferTaskMetrics
        transferTaskMetrics.onComplete();

        notifyTransferStateChange();
    }
    
    protected void onTransferSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
        this.cosXmlResult = cosXmlResult;
        taskState = TransferState.COMPLETED;

        // 计算 TransferTaskMetrics
        transferTaskMetrics.onComplete();

        notifyTransferStateChange();
        notifyTransferResultSuccess(cosXmlRequest, cosXmlResult);

        // 上报事件
        BeaconService.getInstance().reportTransferSuccess(cosXmlRequest, transferTaskMetrics, cosDirect.isTransferSecurely());
    }

    protected void onTransferFailed(CosXmlRequest cosXmlRequest, CosXmlClientException clientException,
                                    CosXmlServiceException serviceException) {
        if (clientException != null) {
            loggerInfo(TAG, taskId, "transfer failed, clientException=%d, %s", clientException.errorCode, clientException.getMessage());
        } else if (serviceException != null) {
            loggerInfo(TAG, taskId, "transfer failed, serviceException=%s, %s", serviceException.getErrorCode(), serviceException.getErrorMessage());
        }

        this.clientException = clientException;
        this.serviceException = serviceException;
        // 计算 TransferTaskMetrics
        transferTaskMetrics.onComplete();
        taskState = TransferState.FAILED;
        notifyTransferStateChange();
        notifyTransferResultFailed(cosXmlRequest, clientException, serviceException);

        // 上报事件
        if (clientException != null) {
            BeaconService.getInstance().reportTransferClientException(cosXmlRequest, transferTaskMetrics, clientException, cosDirect.isTransferSecurely());
        } else if (serviceException != null) {
            BeaconService.getInstance().reportTransferServiceException(cosXmlRequest, transferTaskMetrics, serviceException, cosDirect.isTransferSecurely());
        }
    }


    synchronized protected void onTransferProgressChange(long complete, long target) {

        // 开始有 progress 进度
        transferTaskMetrics.onFirstProgressCallback();
        notifyTransferProgressChange(complete, target);
    }


    protected boolean isManualPaused() {
        return manualPause;
    }

    protected boolean isManualCanceled() {
        return manualCancel;
    }

    private void notifyTransferStateChange() {

        if (transferStateListener != null) {
            transferStateListener.onStateChanged(taskState);
        }
    }

    private void notifyTransferProgressChange(final long complete, final long target) {

        if (cosXmlProgressListener != null && taskState == TransferState.IN_PROGRESS) {
            cosXmlProgressListener.onProgress(complete, target);
        }
    }

    private void notifyTransferResultSuccess(final CosXmlRequest cosXmlRequest, final CosXmlResult cosXmlResult) {

        if (cosXmlResultListener != null) {
            cosXmlResultListener.onSuccess(cosXmlRequest, cosXmlResult);
        }
    }

    private void notifyTransferResultFailed(final CosXmlRequest cosXmlRequest, final CosXmlClientException clientException,
                                            final CosXmlServiceException serviceException) {
        if (serviceException != null && TextUtils.isEmpty(serviceException.getErrorCode())) {
            serviceException.setErrorCode(serviceException.getHttpMessage());
        }

        if (cosXmlResultListener != null) {
            cosXmlResultListener.onFail(cosXmlRequest, clientException, serviceException);
        }
    }

    public CosXmlClientException getClientException() {
        return clientException;
    }

    public CosXmlServiceException getServiceException() {
        return serviceException;
    }

//    protected void throwException(Exception e) throws CosXmlClientException, CosXmlServiceException{
//
//        if (e instanceof CosXmlClientException) {
//            throw (CosXmlClientException) e;
//        } else if (e instanceof CosXmlServiceException) {
//            throw (CosXmlServiceException) e;
//        } else if (e != null) {
//            throw new CosXmlClientException(INTERNAL_ERROR.getCode(), e.getMessage());
//        } else {
//            throw CosXmlClientException.internalException("unknown exception");
//        }
//    }

    protected static final class TaskThreadFactory implements ThreadFactory {
        private final AtomicInteger increment = new AtomicInteger(1);
        private final String tag;
        private final int priority;

        TaskThreadFactory(String tag, int priority) {
            this.tag = tag;
            this.priority = priority;
        }

        @Override
        public final Thread newThread(@NonNull Runnable runnable) {
            Thread newThread = new Thread(runnable, tag + increment.getAndIncrement());
            newThread.setDaemon(false);
            newThread.setPriority(priority);
            return newThread;
        }
    }

    protected static void loggerInfo(String tag, String taskId, String format, Object... params) {
        QCloudLogger.i(tag, "[%s]: " + format, compose(taskId, params));
    }


    protected static void loggerWarn(String tag, String taskId, String format, Object... params) {
        QCloudLogger.w(tag, "[%s]: " + format, compose(taskId, params));
    }

    private static Object[] compose(String taskId, Object... paras) {
        Object[] result = new Object[paras.length + 1];
        result[0] = taskId;
        System.arraycopy(paras, 0, result, 1, paras.length);
        return result;
    }
}

