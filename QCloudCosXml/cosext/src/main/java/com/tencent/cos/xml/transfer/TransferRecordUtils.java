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

import androidx.annotation.Nullable;

import com.tencent.cos.xml.constraints.TransferSpecDao;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.qcloud.core.logger.QCloudLogger;

public class TransferRecordUtils {


    static void observeCosTask(COSXMLTask cosxmlTask, String transferSpecId, TransferSpecDao transferSpecDao,
                               @Nullable TransferStateListener stateListener, @Nullable CosXmlProgressListener progressListener) {


        if (cosxmlTask == null || TextUtils.isEmpty(transferSpecId)) {
            return;
        }

        cosxmlTask.setInternalStateListener(state ->  {

            if (state == TransferState.COMPLETED) {
                transferSpecDao.deleteTransferSpec(transferSpecId);

            } else {
                transferSpecDao.updateTransferSpecTransferStatus(transferSpecId, state);
            }

            if (stateListener != null) {
                stateListener.onStateChanged(state);
            }
        });

        cosxmlTask.setInternalProgressListener(new CosXmlProgressListener() {

            private boolean firstCallback = true;

            @Override
            public void onProgress(long complete, long target) {

                if (cosxmlTask instanceof COSXMLUploadTask ) {

                    COSXMLUploadTask uploadTask = (COSXMLUploadTask) cosxmlTask;
                    String uploadId = uploadTask.getUploadId();
                    if (uploadId != null && firstCallback) {
                        firstCallback = false;
                        QCloudLogger.i("COS-EXT", "task uploadId is " + uploadId);
                        transferSpecDao.updateTransferSpecUploadId(transferSpecId, uploadId);
                    }
                }

                if (progressListener != null) {
                    progressListener.onProgress(complete, target);
                }
            }
        });

    }


    static void observeCosTask(COSXMLTask cosxmlTask, String transferSpecId, TransferSpecDao transferSpecDao) {

        observeCosTask(cosxmlTask, transferSpecId, transferSpecDao, null, null);

    }
}
