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