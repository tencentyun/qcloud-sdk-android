package com.tencent.cos.xml.transfer;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.constraints.TransferSpecDao;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.qcloud.core.logger.QCloudLogger;

/**
 * Created by rickenwang on 2019-12-09.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
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
