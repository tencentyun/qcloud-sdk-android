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

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.BeaconService;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.crypto.CryptoModuleBase;
import com.tencent.cos.xml.crypto.COSDirect;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.ObjectRequest;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import bolts.CancellationTokenSource;

import static com.tencent.cos.xml.common.ClientErrorCode.INTERNAL_ERROR;

/**
 * 传输任务
 */
public abstract class COSTransferTask {

    protected CosXmlSimpleService cosService;

    protected String region, bucket, key;

    protected CosXmlProgressListener cosXmlProgressListener;
    protected CosXmlResultListener cosXmlResultListener;
    protected TransferStateListener transferStateListener;

    volatile TransferState taskState  = TransferState.WAITING;

    protected CosXmlRequest cosXmlRequest;
    protected CosXmlResult cosXmlResult;
    protected CosXmlClientException clientException;
    protected CosXmlServiceException serviceException;

    protected HttpTaskMetrics httpTaskMetrics;

    protected String taskId;

    protected volatile boolean manualPause = false;
    protected volatile boolean manualCancel = false;

    protected volatile CancellationTokenSource cts;

    private final String TAG;

    // 客户端加密
    protected COSDirect cosDirect;
    protected CryptoModuleBase cryptoModuleBase;

    public COSTransferTask(CosXmlSimpleService cosService, ObjectRequest cosXmlRequest) {
        httpTaskMetrics = new HttpTaskMetrics();
        this.cosService = cosService;
        taskId = UUID.randomUUID().toString();
        this.cosXmlRequest = cosXmlRequest;

        bucket = cosXmlRequest.getBucket();
        region = cosXmlRequest.getRegion();
        if (TextUtils.isEmpty(region)) {
            region = cosService.getConfig().getRegion();
        }
        key = cosXmlRequest.getCosPath();
        TAG = tag();
        QCloudLogger.i(TAG, "[%s]: create a %s task, region: %s, bucket: %s, key: %s",
                taskId, cosXmlRequest.getClass().getSimpleName(), region, bucket, key);
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


    // 启动传输
    protected void start() {
        manualPause = false;
        manualCancel = false;
        onTransferWaiting();
        cts = new CancellationTokenSource();
        executor().execute(new TransferRunnable());
    }

    public void setApiDirect(COSDirect cosDirect) {
        this.cosDirect = cosDirect;
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
                    onTransferFailed(cosXmlRequest, clientException, null);
                }
            } catch (CosXmlServiceException serviceException) {
                if (!isManualPaused() && !isManualCanceled()) {
                    onTransferFailed(cosXmlRequest, null, serviceException);
                }
            }
        }
    }

    abstract protected CosXmlResult execute() throws CosXmlClientException, CosXmlServiceException;

    /**
     * 检查传输参数，并计算额外参数
     * 
     * @throws CosXmlClientException
     */
    abstract protected void checking() throws CosXmlClientException;

    public TransferState getTaskState() {
        return taskState;
    }

    protected void onTransferWaiting() {
        taskState = TransferState.WAITING;
        notifyTransferStateChange();
    }
    
    protected void onTransferInProgress() {
        taskState = TransferState.IN_PROGRESS;
        notifyTransferStateChange();
    }
    
    protected void onTransferPaused() {
        taskState = TransferState.PAUSED;
        notifyTransferStateChange();
    }
    
    protected void onTransferSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
        this.cosXmlRequest = cosXmlRequest;
        this.cosXmlResult = cosXmlResult;
        taskState = TransferState.COMPLETED;
        notifyTransferStateChange();
        notifyTransferResultSuccess(cosXmlRequest, cosXmlResult);
    }

    protected void onTransferFailed(CosXmlRequest cosXmlRequest, CosXmlClientException clientException,
                                    CosXmlServiceException serviceException) {
        this.clientException = clientException;
        this.serviceException = serviceException;
        taskState = TransferState.FAILED;
        notifyTransferStateChange();
        notifyTransferResultFailed(cosXmlRequest, clientException, serviceException);
    }

    protected void onTransferProgressChange(long complete, long target) {
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

    private void notifyTransferProgressChange(long complete, long target) {

        if (cosXmlProgressListener != null && taskState == TransferState.IN_PROGRESS) {
            cosXmlProgressListener.onProgress(complete, target);
        }
    }

    private void notifyTransferResultSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {

        if (cosXmlResultListener != null) {
            cosXmlResultListener.onSuccess(cosXmlRequest, cosXmlResult);
        }
    }

    private void notifyTransferResultFailed(CosXmlRequest cosXmlRequest, CosXmlClientException clientException,
                                         CosXmlServiceException serviceException) {

        // 部分情况下 errorCode 为空，这里补齐信息
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

    protected void throwException(Exception e) throws CosXmlClientException, CosXmlServiceException{

        if (e instanceof CosXmlClientException) {
            throw (CosXmlClientException) e;
        } else if (e instanceof CosXmlServiceException) {
            throw (CosXmlServiceException) e;
        } else if (e != null) {
            throw new CosXmlClientException(INTERNAL_ERROR.getCode(), e.getMessage());
        } else {
            throw CosXmlClientException.internalException("unknown exception");
        }
    }

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
}
