package com.tencent.cos.xml.transfer;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.constraints.TransferSpec;
import com.tencent.cos.xml.constraints.TransferSpecDao;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;

public class TransferShop {

    protected TransferSpecDao transferSpecDao;

    TransferShop(TransferSpecDao transferSpecDao) {
        this.transferSpecDao = transferSpecDao;
    }

    protected void observeCosTask(COSXMLTask cosxmlTask, String transferSpecId, TransferStateListener stateListener,
                                  CosXmlProgressListener progressListener) {

        TransferRecordUtils.observeCosTask(cosxmlTask, transferSpecId, transferSpecDao, stateListener, progressListener);
    }


    protected boolean isTaskRunning(TransferState state) {

        return state == TransferState.UNKNOWN
                || state == TransferState.IN_PROGRESS
                || state == TransferState.WAITING
                || state == TransferState.RESUMED_WAITING;
    }


    protected boolean isTransferByTask(@NonNull TransferSpec transferSpec) {

        return TextUtils.isEmpty(transferSpec.workerId);
    }

    protected void startTask(@NonNull COSXMLTask cosxmlTask, String uploadId) {

        if (cosxmlTask instanceof COSXMLUploadTask) {
            COSXMLUploadTask uploadTask = (COSXMLUploadTask) cosxmlTask;
            if (!TextUtils.isEmpty(uploadId)) {
                uploadTask.setUploadId(uploadId);
            }
            uploadTask.upload();
        } else if (cosxmlTask instanceof COSXMLDownloadTask) {
            ((COSXMLDownloadTask) cosxmlTask).download();
        }

    }


    protected void invokeTransferTaskFailed(@NonNull COSXMLTask task) {

        task.taskState = TransferState.FAILED;
        if (task.cosXmlResultListener != null) {
            task.cosXmlResultListener.onFail(task.buildCOSXMLTaskRequest(), new CosXmlClientException(ClientErrorCode.DUPLICATE_TASK.getCode(),
                    ClientErrorCode.DUPLICATE_TASK.getErrorMsg()), null);
        }

        if (task.transferStateListener != null) {
            task.transferStateListener.onStateChanged(TransferState.FAILED);
        }
    }

    protected void invokeTransferTaskComplete(COSXMLTask task) {

        task.taskState = TransferState.COMPLETED;
        if (task.cosXmlResultListener != null) {
            task.cosXmlResultListener.onSuccess(task.buildCOSXMLTaskRequest(), task.buildCOSXMLTaskResult(null));
        }

        if (task.transferStateListener != null) {
            task.transferStateListener.onStateChanged(TransferState.COMPLETED);
        }
    }
}