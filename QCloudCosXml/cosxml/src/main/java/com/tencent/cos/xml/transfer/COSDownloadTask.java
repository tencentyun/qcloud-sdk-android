package com.tencent.cos.xml.transfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.BeaconService;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.Range;
import com.tencent.cos.xml.crypto.COSDirect;
import com.tencent.cos.xml.crypto.Headers;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.cos.xml.utils.FileUtils;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.TaskExecutors;
import com.tencent.qcloud.core.util.ContextHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import bolts.CancellationTokenSource;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * 将 COS 对象下载到本地，支持续传、下载指定 Range。
 *
 * <p>
 * 需要的 COS 权限：HeadObject、GetObject
 *
 * <p>
 * Created by rickenwang on 2021/6/30.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class COSDownloadTask extends COSTransferTask {

    private static final String TAG = "QCloudDownload";

    // 最多同时下载两个
    private static final int DOWNLOAD_CONCURRENT = 2;

    private static ThreadPoolExecutor downloadTaskExecutor = new ThreadPoolExecutor(DOWNLOAD_CONCURRENT, DOWNLOAD_CONCURRENT, 5L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE),
            new COSTransferTask.TaskThreadFactory(TAG + "-", 8));

    private volatile GetObjectRequest getObjectRequest;

    private SimpleDownloadTask simpleDownloadTask;

    public COSDownloadTask(CosXmlSimpleService cosService, GetObjectRequest getObjectRequest) {
        super(cosService, getObjectRequest);
        this.getObjectRequest = getObjectRequest;
    }

    @Override
    protected String tag() {
        return TAG;
    }

    public void pause() {

        if (taskState != TransferState.IN_PROGRESS && taskState != TransferState.WAITING) {
            QCloudLogger.i(TAG, "[%s]: cannot pause upload task in state %s", taskId, taskState);
            return;
        }
        QCloudLogger.i(TAG, "[%s]: pause upload task", taskId);
        manualPause = true;
        // 快速回调状态
        onTransferPaused();
        cts.cancel();

        if (simpleDownloadTask != null) {
            simpleDownloadTask.cancel();
        }
    }

    public void resume() {

        if (taskState != TransferState.PAUSED) {
            QCloudLogger.i(TAG, "[%s]: cannot resume upload task in state %s", taskId, taskState);
            return;
        }

        QCloudLogger.i(TAG, "[%s]: resume upload task", taskId, taskState);
        start();
    }

    public void cancel() {

        QCloudLogger.i(TAG, "[%s]: cancel upload task", taskId);

        manualCancel = true;
        // 快速回调状态
        onTransferFailed(cosXmlRequest, CosXmlClientException.manualCancelException(), null);
        cts.cancel();
        if (simpleDownloadTask != null) {
            simpleDownloadTask.cancel();
        }

        // 删除本地文件
        FileUtils.deleteFileIfExist(getObjectRequest.getDownloadPath());
    }


    @Override
    protected void checking() throws CosXmlClientException {

        httpTaskMetrics.setDomainName(getObjectRequest.getRequestHost(cosService.getConfig()));
        getObjectRequest.attachMetrics(httpTaskMetrics);
        getObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                long fileOffset = getObjectRequest.getFileOffset();
                onTransferProgressChange(complete + fileOffset, target + fileOffset);
            }
        });
        if (getObjectRequest.getFileOffset() > 0) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "download not support file offset " + getObjectRequest.getFileOffset());
        }
    }

    @Override
    protected Executor executor() {
        return downloadTaskExecutor;
    }

    @Override
    protected void onTransferSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
        super.onTransferSuccess(cosXmlRequest, cosXmlResult);
        BeaconService.getInstance().reportCOSDownloadTaskSuccess(cosXmlRequest, cosDirect.isTransferSecurely());
    }

    @Override
    protected void onTransferFailed(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
        super.onTransferFailed(cosXmlRequest, clientException, serviceException);
        if (clientException != null) {
            BeaconService.getInstance().reportCOSDownloadTaskClientException(cosXmlRequest, clientException, cosDirect.isTransferSecurely());
        } else if (serviceException != null) {
            BeaconService.getInstance().reportCOSDownloadTaskServiceException(cosXmlRequest, serviceException, cosDirect.isTransferSecurely());
        }
    }

    @Override
    protected CosXmlResult execute() throws CosXmlClientException, CosXmlServiceException {
        return simpleDownload();
    }

    // 简单下载
    private GetObjectResult simpleDownload() throws CosXmlClientException, CosXmlServiceException {

        simpleDownloadTask = new SimpleDownloadTask(cosDirect, getObjectRequest, cts);
        simpleDownloadTask.bucket = bucket;
        simpleDownloadTask.key = key;
        simpleDownloadTask.region = region;
        simpleDownloadTask.taskId = taskId;
        TaskExecutors.DOWNLOAD_EXECUTOR.execute(simpleDownloadTask);
        Task<GetObjectResult> task = simpleDownloadTask.getTask();
        try {
            task.waitForCompletion();
            if (task.isCancelled()) {
                throw CosXmlClientException.manualCancelException();
            } else if (task.isFaulted()) {
                throwException(task.getError());
            }
        } catch (InterruptedException e) {
            throwException(new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(),
                    e.getMessage()));
        }
        return task.getResult();
    }


    // 暂不支持分块下载
    private GetObjectResult multipartDownload() throws CosXmlClientException, CosXmlServiceException {

        return null;
    }

    private static class SimpleDownloadTask implements Runnable {

        private bolts.TaskCompletionSource<GetObjectResult> tcs;
        private COSDirect cosDirect;
        private volatile GetObjectRequest getObjectRequest;
        private volatile HeadObjectRequest headObjectRequest;
        private CancellationTokenSource cancellationTokenSource;

        private String bucket, region, key;

        private String taskId;

        private SharedPreferences sharedPreferences;

        private String lastModified, eTag, crc64ecma;
        private long remoteStart = 0; // 包含
        private long remoteEnd = -1; // 包含

        public SimpleDownloadTask(COSDirect cosDirect, GetObjectRequest getObjectRequest,
                                CancellationTokenSource cancellationTokenSource) {
            this.cosDirect = cosDirect;
            this.getObjectRequest = getObjectRequest;
            this.tcs = new TaskCompletionSource<>();
            this.cancellationTokenSource = cancellationTokenSource;
        }

        @Override
        public void run() {

            try {
                // 1. 生成下载的参数信息
                checkoutManualCanceled();
                checking();

                // 2. 检查是否需要续传，如果是续传需要修改 GetObjectRequest 参数
                checkoutManualCanceled();
                boolean hasDownloadPart = hasDownloadPart();

                // 3. 根据是否需要续传来修改上下文
                checkoutManualCanceled();
                prepareDownloadContext(hasDownloadPart && !cosDirect.isTransferSecurely());

                // 4. 执行下载
                checkoutManualCanceled();
                GetObjectResult getObjectResult = download();

                // 5. 文件 CRC 校验或者 MD5 校验
                //    校验失败后需要删除下载文件
                checkoutManualCanceled();
                try {
                    verifyContent(getObjectResult);
                } catch (CosXmlClientException clientException) {
                    FileUtils.deleteFileIfExist(getObjectRequest.getDownloadPath());
                    throw clientException;
                }

                tcs.setResult(getObjectResult);
            } catch (CancellationException e) {
                tcs.setCancelled();
            } catch (Exception e) {
                tcs.setError(e);
            }
        }

        private void checkoutManualCanceled() throws CosXmlClientException {
            if (cancellationTokenSource.isCancellationRequested()) {
                throw CosXmlClientException.manualCancelException();
            }
        }

        public Task<GetObjectResult> getTask() {
            return tcs.getTask();
        }

        public void cancel() {
            cancellationTokenSource.cancel();
            if (headObjectRequest != null) {
                cosDirect.cancel(headObjectRequest);
            }
            if (getObjectRequest != null) {
                cosDirect.cancel(getObjectRequest);
            }
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        // 1. HeadObject 拿到文件信息 lastModified、contentLength、eTag、crc64ecma
        // 2. 拿到 range 参数
        // 3. 拿到 offset 参数
        private void checking() throws CosXmlClientException, CosXmlServiceException {

            Range range = getObjectRequest.getRange();
            if (range != null) {
                remoteStart = range.getStart();
                remoteEnd = range.getEnd();
            } else {
                remoteStart = 0;
                remoteEnd = -1;
            }

            Context context = ContextHolder.getAppContext();
            if (context == null) {
                throw CosXmlClientException.internalException("context is null");
            }
            sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);

            headObjectRequest = new HeadObjectRequest(bucket, key);
            headObjectRequest.setRegion(region);
            HeadObjectResult headObjectResult = cosDirect.headObject(headObjectRequest);
            lastModified = headObjectResult.getHeader("Last-Modified");
            eTag = headObjectResult.getHeader("ETag");
            crc64ecma = headObjectResult.getHeader("x-cos-hash-crc64ecma");
        }

        // 1. 先根据 key 找到 DownloadRecord
        // 2. 校验 DownloadRecord 的有效性
        @Nullable private boolean hasDownloadPart() throws CosXmlClientException {

            String downloadRecordStr = sharedPreferences.getString(key, "");
            if (TextUtils.isEmpty(downloadRecordStr)) {
                return false;
            }

            QCloudLogger.i(TAG, "[%s]: find DownloadRecord: %s", taskId, downloadRecordStr);

            try {
                DownloadRecord downloadRecord = DownloadRecord.toJson(downloadRecordStr);
                if (downloadRecord.lastModified == null || !downloadRecord.lastModified.equals(lastModified) ||
                    downloadRecord.eTag == null || !downloadRecord.eTag.equals(eTag) ||
                    (downloadRecord.crc64ecma != null && crc64ecma != null && !downloadRecord.crc64ecma.equals(crc64ecma)) ||
                    downloadRecord.remoteStart != remoteStart || downloadRecord.remoteEnd != remoteEnd) {

                    QCloudLogger.w(TAG, "[%s]: verify DownloadRecord failed: lastModified:%s, eTag:%s, crc64ecma:%s, remoteStart:%d, remoteEnd:%d",
                            taskId, lastModified, eTag, crc64ecma, remoteStart, remoteEnd);
                    return false;
                } else {
                    return true;
                }
            } catch (JSONException e) {
                QCloudLogger.i(TAG, "[%s]: parse DownloadRecord failed: %s", taskId, e.getMessage());
                return false;
            }

        }


        // 1. 根据 DownloadRecord 清理本地环境
        // 2. 生成新的 GetObjectRequest
        private void prepareDownloadContext(boolean hasDownloadPart) {

            File localFile = new File(getObjectRequest.getDownloadPath());

            // 续传
            if (hasDownloadPart) {
                long size = localFile.length();
                getObjectRequest.setFileOffset(size);
                getObjectRequest.setRange(size + remoteStart, remoteEnd);
                QCloudLogger.i(TAG, "[%s]: has download part %d", taskId, size);
            } else {
                FileUtils.deleteFileIfExist(localFile.getAbsolutePath());
            }

        }

        // 执行 GetObjectRequest 请求
        private GetObjectResult download() throws CosXmlClientException, CosXmlServiceException {

            // 保存下载记录
            try {
                sharedPreferences.edit().putString(key, DownloadRecord.flatJson(
                        new DownloadRecord(lastModified, eTag, crc64ecma, remoteStart, remoteEnd,
                        new LinkedList<DownloadedBlock>()))).apply();
            } catch (JSONException e) {
                QCloudLogger.w(TAG, "[%s]: save DownloadRecord failed: %s", taskId, e.getMessage());
            }
            QCloudLogger.i(TAG, "[%s]: start download", taskId);
            GetObjectResult result = cosDirect.getObject(getObjectRequest);
            sharedPreferences.edit().remove(key).apply();
            QCloudLogger.i(TAG, "[%s]: download complete", taskId);
            return result;
        }

        private void verifyContent(GetObjectResult getObjectResult) throws CosXmlClientException {

            //
            String remoteCRC64 = getObjectResult.getHeader("x-cos-hash-crc64ecma");
            String unencryptedMd5 = getObjectResult.getHeader(Headers.UNENCRYPTED_CONTENT_MD5);
            File localFile = new File(getObjectRequest.getDownloadPath());

            // 对于 Range Download 只能对比当前 GetObjectRequest 返回的 crc64 是否一致
            if (isRangeDownload()) {
                checkCRC64(remoteCRC64, localFile, getObjectRequest.getFileOffset(), localFile.length() - getObjectRequest.getFileOffset());
            } else {
                // 校验整个下载的文件
                if (cosDirect.isTransferSecurely()) {
                    checkMd5(unencryptedMd5, localFile);
                } else {
                    checkCRC64(remoteCRC64, localFile, 0, -1);
                }
            }
        }


        private boolean isRangeDownload() {
            return remoteStart != 0 || remoteEnd != -1;
        }

        private void checkMd5(@Nullable String remoteMD5, File localFile) throws CosXmlClientException {

            if (TextUtils.isEmpty(remoteMD5)) {
                return;
            }
            String localMD5 = DigestUtils.getMD5(localFile.getAbsolutePath());
            remoteMD5 = remoteMD5.replaceAll("\"", "");
            if (!localMD5.equals(remoteMD5)) {
                throw CosXmlClientException.internalException("verify MD5 failed, local MD5: " + localMD5 + ", remote MD5: " + remoteMD5);
            }
        }

        private void checkCRC64(@Nullable String remoteCRC, File localFile, long offset, long size) throws CosXmlClientException {

            if (TextUtils.isEmpty(remoteCRC)) {
                return;
            }
            long localCRC64 = 0;
            try {
                localCRC64 = DigestUtils.getCRC64(new FileInputStream(localFile), offset, size);
            } catch (FileNotFoundException e) {
                throw CosXmlClientException.internalException("verify CRC64 failed: " + e.getMessage());
            }
            long remoteCRC64 = DigestUtils.getBigIntFromString(remoteCRC);
            if (localCRC64 != remoteCRC64) {
                throw CosXmlClientException.internalException("verify CRC64 failed, local crc64: " + localCRC64 + ", remote crc64: " + remoteCRC64);
            }
        }
    }

    private static class DownloadRecord {

        String lastModified;

        //
        String eTag;

        @Nullable String crc64ecma;

        // COS 对象偏移量
        long remoteStart;
        long remoteEnd;


        // 只有分块下载才有值，暂时不支持分块下载
        List<DownloadedBlock> downloadedBlocks;


        public DownloadRecord(String lastModified, String eTag, String crc64ecma,
                              long remoteStart, long remoteEnd, List<DownloadedBlock> downloadedBlocks) {
            this.lastModified = lastModified;
            this.eTag = eTag;
            this.crc64ecma = crc64ecma;
            this.downloadedBlocks = downloadedBlocks;
            this.remoteStart = remoteStart;
            this.remoteEnd = remoteEnd;
        }

        public static String flatJson(DownloadRecord downloadRecord) throws JSONException {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lastModified", downloadRecord.lastModified);
            jsonObject.put("eTag", downloadRecord.eTag);
            jsonObject.put("crc64ecma", downloadRecord.crc64ecma);
            jsonObject.put("remoteStart", downloadRecord.remoteStart);
            jsonObject.put("remoteEnd", downloadRecord.remoteEnd);
            JSONArray downloadedBlocks = new JSONArray();
            for (DownloadedBlock block : downloadRecord.downloadedBlocks) {
                JSONObject blockObject = new JSONObject();
                blockObject.put("from", block.from);
                blockObject.put("to", block.to);
                downloadedBlocks.put(blockObject);
            }
            jsonObject.put("downloadedBlocks", downloadedBlocks);
            return jsonObject.toString();
        }

        public static DownloadRecord toJson(String str) throws JSONException {

            JSONObject jsonObject = new JSONObject(str);
            String lastModified = jsonObject.getString("lastModified");
            String eTag = jsonObject.getString("eTag");
            String crc64ecma = jsonObject.optString("crc64ecma");
            String remoteStart = jsonObject.getString("remoteStart");
            String remoteEnd = jsonObject.getString("remoteEnd");
            JSONArray jsonArray = jsonObject.getJSONArray("downloadedBlocks");
            List<DownloadedBlock> downloadedBlocks = new LinkedList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject block = (JSONObject) jsonArray.get(i);
                downloadedBlocks.add(new DownloadedBlock(Long.parseLong(block.getString("from")),
                        Long.parseLong(block.getString("to"))));
            }
            return new DownloadRecord(lastModified, eTag, crc64ecma,
                    Long.parseLong(remoteStart), Long.parseLong(remoteEnd), downloadedBlocks);
        }
    }

    private static class DownloadedBlock {

        long from;
        long to;

        public DownloadedBlock(long from, long to) {
            this.from = from;
            this.to = to;
        }
    }
}






