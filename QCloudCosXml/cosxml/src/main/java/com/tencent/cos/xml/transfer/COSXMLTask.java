package com.tencent.cos.xml.transfer;

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
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tencent.cos.xml.transfer.TaskStateMonitor.MESSAGE_TASK_CONSTRAINT;
import static com.tencent.cos.xml.transfer.TaskStateMonitor.MESSAGE_TASK_INIT;
import static com.tencent.cos.xml.transfer.TaskStateMonitor.MESSAGE_TASK_MANUAL;

/**
 * Created by bradyxiao on 2018/8/23.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */

public abstract class COSXMLTask {

    protected static TaskStateMonitor monitor = TaskStateMonitor.getInstance();

    /** CosXmlService */
    protected CosXmlSimpleService cosXmlService;
    /**
     * cos 业务
     */
    protected String region;
    protected String bucket;
    protected String cosPath;
    /** 返回 cosXmlResult  or  throw Exception */
    protected CosXmlResult mResult;
    protected Exception mException;

    /** url query 属性 */
    protected Map<String, String> queries;

    /** header 属性 */
    protected Map<String, List<String>> headers;
    /** 是否需要计算 MD5 */
    protected boolean isNeedMd5 = true;
    /** register some callback */
    protected CosXmlProgressListener cosXmlProgressListener;
    protected CosXmlResultListener cosXmlResultListener;
    protected TransferStateListener transferStateListener;

    protected TransferStateListener internalStateListener;
    protected CosXmlProgressListener internalProgressListener;

    /** cosxml task state during the whole lifecycle */
    volatile TransferState taskState  = TransferState.WAITING;

    /** 退出：pause, cancel, failed*/
    protected AtomicBoolean IS_EXIT = new AtomicBoolean(false);

    /** 直接提供签名串 */
    protected OnSignatureListener onSignatureListener;

    /** 获取 http metrics */
    protected OnGetHttpTaskMetrics onGetHttpTaskMetrics;

    protected void setCosXmlService(CosXmlSimpleService cosXmlService){
        this.cosXmlService = cosXmlService;
    }

    public void setCosXmlProgressListener(CosXmlProgressListener cosXmlProgressListener){
        this.cosXmlProgressListener = cosXmlProgressListener;
    }

    public void setCosXmlResultListener(CosXmlResultListener cosXmlResultListener){
        this.cosXmlResultListener = cosXmlResultListener;
        monitor.sendStateMessage(this, null, mException, mResult, MESSAGE_TASK_INIT);
    }

    public void setTransferStateListener(TransferStateListener transferStateListener){
        this.transferStateListener = transferStateListener;
        monitor.sendStateMessage(this, taskState, null, null, MESSAGE_TASK_INIT);
    }

    public void setOnSignatureListener(OnSignatureListener onSignatureListener){
        this.onSignatureListener = onSignatureListener;
    }

    public void setOnGetHttpTaskMetrics(OnGetHttpTaskMetrics onGetHttpTaskMetrics){
        this.onGetHttpTaskMetrics = onGetHttpTaskMetrics;
    }

    protected void getHttpMetrics(CosXmlRequest cosXmlRequest, final String requestName){
        if(onGetHttpTaskMetrics != null){
            cosXmlRequest.attachMetrics(new HttpTaskMetrics(){
                @Override
                public void onDataReady() {
                    super.onDataReady();
                    onGetHttpTaskMetrics.onGetHttpMetrics(requestName, this);
                }
            });
        }
    }

    protected void internalCompleted(){}

    protected void internalFailed(){}

    protected void internalPause(){}

    protected void internalCancel(){}

    protected void internalResume(){}

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
     * 异步的，发送通知，置位
     */
    public void pause() {
        if(IS_EXIT.get())return;
        else IS_EXIT.set(true);
        monitor.sendStateMessage(this, TransferState.PAUSED,null,null, MESSAGE_TASK_MANUAL);
    }

    public void cancel() {
        if(IS_EXIT.get())return;
        else IS_EXIT.set(true);
        monitor.sendStateMessage(this, TransferState.CANCELED,new CosXmlClientException(ClientErrorCode.USER_CANCELLED.getCode(), "canceled by user"),null, MESSAGE_TASK_MANUAL);
    }

    public void resume() {
        monitor.sendStateMessage(this, TransferState.RESUMED_WAITING,null,null, MESSAGE_TASK_MANUAL);
    }

    public TransferState getTaskState() {
        return taskState;
    }

    public CosXmlResult getResult(){
        return mResult;
    }

    public Exception getException(){
        return mException;
    }

    protected abstract CosXmlRequest buildCOSXMLTaskRequest(); // 构造COSXMLTask返回的Request

    protected abstract CosXmlResult buildCOSXMLTaskResult(CosXmlResult sourceResult); //构造COSXMLTask返回的Result

    private void dispatchStateChange(TransferState transferState) {

        if (transferStateListener != null){
            transferStateListener.onStateChanged(transferState);
        }

        if (internalStateListener != null) {
            internalStateListener.onStateChanged(transferState);
        }
    }

    /**
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
                throw new IllegalStateException("invalid state: " + newTaskState);
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
}
