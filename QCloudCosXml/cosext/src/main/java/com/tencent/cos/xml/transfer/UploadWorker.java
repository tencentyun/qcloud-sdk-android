package com.tencent.cos.xml.transfer;

import android.content.Context;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.constraints.TransferDatabase;
import com.tencent.cos.xml.constraints.TransferSpecDao;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.qcloud.core.logger.QCloudLogger;

/**
 * Created by rickenwang on 2019-11-18.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class UploadWorker extends ListenableWorker {

    private final String TAG = "UploadWorker";

    private final static String TAG_DEBUG = "COS-EXT";

    private TransferSpecDao transferSpecDao;

    private COSXMLUploadTask uploadTask;

    private TransferManager transferManager;

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public UploadWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        transferSpecDao = TransferDatabase.create(getApplicationContext()).transferSpecDao();
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {


        return CallbackToFutureAdapter.getFuture(completer -> {

            Context context = getApplicationContext();

            QCloudLogger.i(TAG, "UploadWorker start work " + getId());

            Data inputData = getInputData();
            String transferSpecId = inputData.getString("id");

            if (TextUtils.isEmpty(transferSpecId)) {
                completer.set(Result.failure());
                return null;
            }

            transferSpecDao.getTransferSpecsLiveData(transferSpecId).observeForever(transferSpec -> {

                if (transferSpec == null) {
                    completer.set(Result.failure());
                    return;
                }

                transferManager = fromServiceEgg(context, transferSpec.getServiceEgg());
                if (transferManager == null) {
                    QCloudLogger.e(TAG, "Please init TransferExtManager first!");
                    completer.set(Result.failure());
                    return;
                }

                uploadTask = transferManager.upload(transferSpec.bucket, transferSpec.key, transferSpec.filePath, transferSpec.uploadId);

                TransferRecordUtils.observeCosTask(uploadTask, transferSpec.id, transferSpecDao, new TransferStateListener() {
                    @Override
                    public void onStateChanged(TransferState state) {

                        QCloudLogger.i(TAG_DEBUG, "background upload " + transferSpec.filePath + " to " + transferSpec.key + " :  + state " + state);

                        if (state == TransferState.FAILED || state == TransferState.CANCELED) {
                            completer.set(Result.failure());
                        } else if (state == TransferState.COMPLETED) {
                            completer.set(Result.success());
                        }
                    }
                }, new CosXmlProgressListener() {
                    private int lastProgress = 0;
                    @Override
                    public void onProgress(long complete, long target) {
                        // Log.d("SAMPLE", "complete " + complete + ", target " + target + ":" + cosxmlUploadTask.getUploadId());
                        int thisProgress = (int) (100 * complete / target);
                        if (lastProgress + 1 <= thisProgress) {
                            QCloudLogger.i(TAG_DEBUG, "background upload " + transferSpec.filePath + " to " + transferSpec.key + " : complete " + complete + ", target " + target);
                            lastProgress = thisProgress;
                        }
                    }
                });

            });
            return new Object();
        });
    }

    @Override
    public void onStopped() {

        super.onStopped();

        QCloudLogger.i(TAG_DEBUG, "onStopped " + getId());
        QCloudLogger.i(TAG_DEBUG, "prepare to cancel all task " + getId());
        if (uploadTask != null && transferManager!= null) {
            QCloudLogger.i(TAG_DEBUG, "cancel all task " + getId());
            uploadTask.cancelAllRequest(transferManager.getCosXmlService());
        }
    }

    @Nullable
    public static TransferManager fromServiceEgg(Context context, byte[] egg) {

        if (egg != null) {

            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(egg, 0, egg.length);
            parcel.setDataPosition(0);

            CosXmlServiceConfig cosXmlServiceConfig = parcel.readParcelable(UploadWorker.class.getClassLoader());
            WorkTransferConfig transferConfig = parcel.readParcelable(UploadWorker.class.getClassLoader());
            ParcelableCredentialProvider credentialProvider = parcel.readParcelable(UploadWorker.class.getClassLoader());

            parcel.recycle();
            return new TransferManager(new CosXmlSimpleService(context, cosXmlServiceConfig, credentialProvider), transferConfig);
        }
        return null;
    }

}
