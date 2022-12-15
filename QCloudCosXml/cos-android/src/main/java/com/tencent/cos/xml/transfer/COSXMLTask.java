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

import static com.tencent.cos.xml.transfer.TaskStateMonitor.MESSAGE_TASK_CONSTRAINT;
import static com.tencent.cos.xml.transfer.TaskStateMonitor.MESSAGE_TASK_INIT;
import static com.tencent.cos.xml.transfer.TaskStateMonitor.MESSAGE_TASK_MANUAL;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.BeaconService;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.qcloud.core.http.HttpTaskMetrics;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 传输任务
 */
public abstract class COSXMLTask {
    private static final String TAG = "COSXMLTask";

    /**
     * 状态监听器
     */
    protected static TaskStateMonitor monitor = TaskStateMonitor.getInstance();

    /** Tencent Cloud COS服务 */
    protected CosXmlSimpleService cosXmlService;

    /** 区域 */
    protected String region;
    /** 存储桶 */
    protected String bucket;
    /** 对象cos路径 */
    protected String cosPath;

    /** 返回结果 */
    protected CosXmlResult mResult;
    /** 需要抛出的异常 */
    protected Exception mException;

    /** url query 属性 */
    protected Map<String, String> queries;

    /** header 属性 */
    protected Map<String, List<String>> headers;
    /** 是否需要计算 MD5 */
    protected boolean isNeedMd5 = true;
    /** 进度回调监听器 */
    protected CosXmlProgressListener cosXmlProgressListener;
    /** 结果回调监听器 */
    protected CosXmlResultListener cosXmlResultListener;
    /** 状态监听器 */
    protected TransferStateListener transferStateListener;
    /** 初始化分块上传监听器 */
    protected InitMultipleUploadListener initMultipleUploadListener;

    protected TransferStateListener internalStateListener;
    protected CosXmlProgressListener internalProgressListener;
    protected InitMultipleUploadListener internalInitMultipleUploadListener;

    /** cosxml task state during the whole lifecycle */
    volatile TransferState taskState  = TransferState.WAITING;

    /** 退出：pause, cancel, failed*/
    protected AtomicBoolean IS_EXIT = new AtomicBoolean(false);

    /** 直接提供签名串 */
    protected OnSignatureListener onSignatureListener;

    /** 获取 http metrics */
    protected OnGetHttpTaskMetrics onGetHttpTaskMetrics;

    // 等待超时计时器
    protected Timer waitTimeoutTimer;

    /**
     * 设置COS服务
     * @param cosXmlService COS服务类
     */
    protected void setCosXmlService(CosXmlSimpleService cosXmlService){
        this.cosXmlService = cosXmlService;
    }

    /**
     * 设置进度回调监听器
     * @param cosXmlProgressListener 进度回调监听器
     */
    public void setCosXmlProgressListener(CosXmlProgressListener cosXmlProgressListener){
        this.cosXmlProgressListener = cosXmlProgressListener;
    }

    /**
     * 设置结果回调监听器
     * @param cosXmlResultListener 结果回调监听器
     */
    public void setCosXmlResultListener(CosXmlResultListener cosXmlResultListener){
        this.cosXmlResultListener = cosXmlResultListener;
        monitor.sendStateMessage(this, null, mException, mResult, MESSAGE_TASK_INIT);
    }

    /**
     * 设置状态监听器
     * @param transferStateListener 状态监听器
     */
    public void setTransferStateListener(TransferStateListener transferStateListener){
        this.transferStateListener = transferStateListener;
        monitor.sendStateMessage(this, taskState, null, null, MESSAGE_TASK_INIT);
    }

    /**
     * 设置初始化分块上传监听器
     * @param initMultipleUploadListener 初始化分块上传监听器
     */
    public void setInitMultipleUploadListener(InitMultipleUploadListener initMultipleUploadListener){
        this.initMultipleUploadListener = initMultipleUploadListener;
    }

    public void setOnSignatureListener(OnSignatureListener onSignatureListener){
        this.onSignatureListener = onSignatureListener;
    }

    public void setOnGetHttpTaskMetrics(OnGetHttpTaskMetrics onGetHttpTaskMetrics){
        this.onGetHttpTaskMetrics = onGetHttpTaskMetrics;
    }

    protected void getHttpMetrics(CosXmlRequest cosXmlRequest, final String requestName){
        cosXmlRequest.attachMetrics(new COSXMLMetrics(requestName));
    }

    public void startTimeoutTimer(long millisecond) {
//        if (millisecond < 1000) {
//            return;
//        }

        waitTimeoutTimer = new Timer();
        waitTimeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (taskState == TransferState.WAITING || taskState == TransferState.RESUMED_WAITING) {
                    encounterError(null, new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), "Task waiting timeout."), null);
                }
            }
        }, millisecond);
    }

    class COSXMLMetrics extends HttpTaskMetrics {

        String requestName;
        COSXMLMetrics(String requestName) {
            this.requestName = requestName;
        }
        @Override
        public void onDataReady() {
            super.onDataReady();
            if (onGetHttpTaskMetrics != null) {
                onGetHttpTaskMetrics.onGetHttpMetrics(requestName, this);
            }
        }
    }

    protected void internalCompleted(){}

    protected void internalFailed(){}

    protected void internalPause(){}

    protected void internalCancel(){}

    protected void internalResume(){}

    abstract protected void encounterError(@Nullable CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException);

    /**
     * 限制条件被满足，状态应该由 {@link TransferState#CONSTRAINED} 切换到 {@link TransferState#RESUMED_WAITING}
     */
    void constraintSatisfied() {

        monitor.sendStateMessage(this, TransferState.RESUMED_WAITING,null,null, MESSAGE_TASK_CONSTRAINT);
    }

    void constraintUnSatisfied() {

        monitor.sendStateMessage(this, TransferState.CONSTRAINED, null, null, MESSAGE_TASK_CONSTRAINT);
    }

    /**
     * 暂停任务，若是 {@link COSXMLUploadTask} 请调用 {@link COSXMLUploadTask#pauseSafely()} 接口来暂停。
     */
    public void pause() {
        if(IS_EXIT.get())return;
        else IS_EXIT.set(true);
        monitor.sendStateMessage(this, TransferState.PAUSED,null,null, MESSAGE_TASK_MANUAL);
    }

    /**
     * 取消任务
     */
    public void cancel() {
        if(IS_EXIT.get())return;
        else IS_EXIT.set(true);
        monitor.sendStateMessage(this, TransferState.CANCELED,new CosXmlClientException(ClientErrorCode.USER_CANCELLED.getCode(), "canceled by user"),null, MESSAGE_TASK_MANUAL);
    }

    /**
     * 恢复任务
     */
    public void resume() {
        monitor.sendStateMessage(this, TransferState.RESUMED_WAITING,null,null, MESSAGE_TASK_MANUAL);
    }

    /**
     * 获取任务状态
     * @return 任务状态
     */
    public TransferState getTaskState() {
        return taskState;
    }

    /**
     * 获取返回结果
     * @return 返回结果
     */
    public CosXmlResult getResult(){
        return mResult;
    }

    /**
     * 获取需要抛出的异常
     * @return 需要抛出的异常
     */
    public Exception getException(){
        return mException;
    }

    /**
     * 构造COSXMLTask返回的Request
     * @return COSXMLTask返回的Request
     */
    protected abstract CosXmlRequest buildCOSXMLTaskRequest();

    public void clearResultAndException() {
        mException = null;
        mResult = null;
    }

    /**
     * 构造COSXMLTask返回的Result
     * @param sourceResult 原始CosXmlResult
     * @return Task返回的Result
     */
    protected abstract CosXmlResult buildCOSXMLTaskResult(CosXmlResult sourceResult);

    private void dispatchStateChange(TransferState transferState) {

        if (transferStateListener != null){
            transferStateListener.onStateChanged(transferState);
        }

        if (internalStateListener != null) {
            internalStateListener.onStateChanged(transferState);
        }
    }

    /**
     * 更新状态
     * waiting: 准备状态, 任何状态都可以转为它, task 准备执行.
     * in_progress: 运行状态，只能由waiting转为它, task 执行中.
     * complete: 完成状态，只能由 in_progress状态转为它, task 执行完.
     * failed: 失败状态，只能由waiting、in_progress状态转为它, task 执行失败.
     * pause: 暂停状态，只能由waiting、in_progress状态转为它, task 暂停执行.
     * cancel: 取消状态，除了complete之外，任何状态都可以转为它, task 取消，并释放资源.
     * resume_waiting: 恢复状态，只能由 pause、failed. 这是一个特殊的状态，会直接过渡到waiting, task 恢复执行.
     * 此方法中 newTaskState 不能是 waiting
     * @param newTaskState new state for operating
     */
    protected synchronized void updateState(TransferState newTaskState, Exception exception, CosXmlResult result, boolean isInit){

        // QCloudLogger.i("QCloudTest", "newState " + newTaskState + ", current " + taskState + ", isinit " + isInit);

        if(isInit){
            if(exception != null){
                if(cosXmlResultListener != null){
                    if(exception instanceof CosXmlClientException){
                        cosXmlResultListener.onFail(buildCOSXMLTaskRequest(), (CosXmlClientException) exception, null);
                    }else {
                        cosXmlResultListener.onFail(buildCOSXMLTaskRequest(), null, (CosXmlServiceException) exception);
                    }
                }
            }else if(result != null){
                if(cosXmlResultListener != null){
                    cosXmlResultListener.onSuccess(buildCOSXMLTaskRequest(), result);
                }
            }else if (newTaskState != null){
                dispatchStateChange(taskState);
            }
            return;
        }
        switch (newTaskState){
            case WAITING:
                if(taskState == TransferState.RESUMED_WAITING){
                    taskState = TransferState.WAITING;
                    dispatchStateChange(taskState);
                }
                break;
            case IN_PROGRESS:
                if(taskState == TransferState.WAITING){
                    taskState = TransferState.IN_PROGRESS;
                    dispatchStateChange(taskState);
                }
                break;
            case COMPLETED:
                if(taskState == TransferState.IN_PROGRESS){
                    taskState = TransferState.COMPLETED;
                    mResult = buildCOSXMLTaskResult(result);
                    if(cosXmlResultListener != null){
                        cosXmlResultListener.onSuccess(buildCOSXMLTaskRequest(), mResult);
                    }
                    dispatchStateChange(taskState);
                    internalCompleted();
                }
                break;
            case FAILED:
                if(taskState == TransferState.WAITING
                        || taskState == TransferState.IN_PROGRESS){
                    taskState = TransferState.FAILED;
                    mException = exception;
                    if(cosXmlResultListener != null){
                        if(exception instanceof CosXmlClientException){
                            cosXmlResultListener.onFail(buildCOSXMLTaskRequest(), (CosXmlClientException) exception, null);
                        }else {
                            cosXmlResultListener.onFail(buildCOSXMLTaskRequest(), null, (CosXmlServiceException) exception);
                        }
                    }
                    dispatchStateChange(taskState);
                    internalFailed();
                }
                break;
            case PAUSED:
                if(taskState == TransferState.WAITING
                        || taskState == TransferState.IN_PROGRESS){
                    taskState = TransferState.PAUSED;
                    dispatchStateChange(taskState);
                    internalPause();
                }
                break;
            case CANCELED:
                if(taskState != TransferState.CANCELED
                        && taskState != TransferState.COMPLETED){
                    taskState = TransferState.CANCELED;
                    dispatchStateChange(taskState);
                    mException = exception;
                    if(cosXmlResultListener != null){
                        cosXmlResultListener.onFail(buildCOSXMLTaskRequest(), (CosXmlClientException) exception, null);
                    }
                    internalCancel();
                }
                break;
            case RESUMED_WAITING:
                if(taskState == TransferState.PAUSED
                        || taskState == TransferState.FAILED || taskState == TransferState.CONSTRAINED){
                    taskState = TransferState.RESUMED_WAITING;
                    dispatchStateChange(taskState);
                    internalResume();
                }
                break;

            case CONSTRAINED:
                if (taskState == TransferState.WAITING || taskState == TransferState.RESUMED_WAITING
                  || taskState == TransferState.IN_PROGRESS) {
                    taskState = TransferState.CONSTRAINED;
                    dispatchStateChange(taskState);
                    internalPause();
                }

            default:
                IllegalStateException illegalStateException = new IllegalStateException("invalid state: " + newTaskState);
                BeaconService.getInstance().reportError(TAG ,illegalStateException);
                throw illegalStateException;
        }
    }

    @Deprecated
    public interface OnSignatureListener {
        /**
         * @param cosXmlRequest request
         * @return String
         * @see com.tencent.cos.xml.model.object.HeadObjectRequest
         * @see PutObjectRequest
         * @see InitMultipartUploadRequest
         * @see ListPartsRequest
         * @see UploadPartRequest
         * @see CompleteMultiUploadRequest
         * @see AbortMultiUploadRequest
         */
        String onGetSign(CosXmlRequest cosXmlRequest);
    }

    public interface OnGetHttpTaskMetrics{
        void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics);
    }

    void setInternalStateListener(TransferStateListener internalStateListener) {
        this.internalStateListener = internalStateListener;
    }

    void setInternalProgressListener(CosXmlProgressListener internalProgressListener) {
        this.internalProgressListener = internalProgressListener;
    }

    void setInternalInitMultipleUploadListener(InitMultipleUploadListener internalInitMultipleUploadListener) {
        this.internalInitMultipleUploadListener = internalInitMultipleUploadListener;
    }
}
