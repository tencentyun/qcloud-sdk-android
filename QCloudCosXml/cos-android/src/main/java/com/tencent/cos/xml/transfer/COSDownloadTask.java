package com.tencent.cos.xml.transfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.Range;
import com.tencent.cos.xml.crypto.COSDirect;
import com.tencent.cos.xml.crypto.Headers;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.cos.xml.utils.FileUtils;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.util.ContextHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static final String TASK_UNKNOWN_STATUS = "task unknown status";

    // 最多同时下载两个
    private static final int DOWNLOAD_CONCURRENT = 3;

    private static ThreadPoolExecutor downloadTaskExecutor = new ThreadPoolExecutor(DOWNLOAD_CONCURRENT, DOWNLOAD_CONCURRENT, 5L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE),
            new COSTransferTask.TaskThreadFactory(TAG + "-", 8));

    private volatile GetObjectRequest mGetObjectRequest;
    private volatile long remoteStart = 0; // 包含
    private volatile long remoteEnd = -1; // 包含

    private SimpleDownloadTask simpleDownloadTask;

    public COSDownloadTask(COSDirect cosDirect, GetObjectRequest getObjectRequest) {
        super(cosDirect, getObjectRequest);
        this.mGetObjectRequest = getObjectRequest;
        Range range = getObjectRequest.getRange();
        if (range != null) {
            remoteStart = range.getStart();
            remoteEnd = range.getEnd();
        }
    }

    @Override
    protected String tag() {
        return TAG;
    }

    @Override
    protected Executor executor() {
        return downloadTaskExecutor;
    }

    @Override
    public void pause() {
        super.pause();

        if (simpleDownloadTask != null) {
            simpleDownloadTask.cancel(false);
        }
    }

    @Override
    public void pause(boolean now) {
        super.pause();

        if (simpleDownloadTask != null) {
            simpleDownloadTask.cancel(now);
        }
    }

    @Override
    public void cancel() {
        super.cancel();

        if (simpleDownloadTask != null) {
            simpleDownloadTask.cancel(false);
        }

        // 删除本地文件
        FileUtils.deleteFileIfExist(mGetObjectRequest.getDownloadPath());
    }

    @Override
    public void cancel(boolean now) {
        super.cancel();

        if (simpleDownloadTask != null) {
            simpleDownloadTask.cancel(now);
        }

        // 删除本地文件
        FileUtils.deleteFileIfExist(mGetObjectRequest.getDownloadPath());
    }

    @Override
    protected void checking() throws CosXmlClientException {
        super.checking();
    }


    @Override
    protected CosXmlResult execute() throws Exception {
        return simpleDownload();
    }

    // 简单下载
    private GetObjectResult simpleDownload() throws Exception {

        simpleDownloadTask = new SimpleDownloadTask(cosDirect, mGetObjectRequest, mTransferTaskCts);
        simpleDownloadTask.bucket = bucket;
        simpleDownloadTask.key = key;
        simpleDownloadTask.region = region;
        simpleDownloadTask.setTaskId(taskId);
        simpleDownloadTask.mTransferMetrics = transferTaskMetrics;
        mGetObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                long fileOffset = mGetObjectRequest.getFileOffset();
                onTransferProgressChange(complete + fileOffset, target + fileOffset);
            }
        });

        simpleDownloadTask.run();
        Task<GetObjectResult> task = simpleDownloadTask.getTask();

        if (task.isFaulted()) {
            throw task.getError();
        } else if (task.isCompleted()) {
            return task.getResult();
        } else {
            loggerInfo(TAG, taskId, TASK_UNKNOWN_STATUS);
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), TASK_UNKNOWN_STATUS);
        }
    }


    // 暂不支持分块下载
//    private GetObjectResult multipartDownload() throws CosXmlClientException, CosXmlServiceException {
//
//        return null;
//    }

    private class SimpleDownloadTask implements Runnable {

        private bolts.TaskCompletionSource<GetObjectResult> tcs;
        private COSDirect cosDirect;
        private volatile GetObjectRequest getObjectRequest;
        private volatile HeadObjectRequest headObjectRequest;
        private CancellationTokenSource mTransferTaskCts;

        private String bucket, region, key;

        private String taskId;

        private SharedPreferences sharedPreferences;

        private String lastModified, eTag, crc64ecma;
        private TransferTaskMetrics mTransferMetrics;

        public SimpleDownloadTask(COSDirect cosDirect, GetObjectRequest getObjectRequest,
                                CancellationTokenSource transferTaskCts) {
            this.cosDirect = cosDirect;
            this.getObjectRequest = getObjectRequest;
            //下载重试时会改变request的range，因此不能让range参与签名
            this.getObjectRequest.addNoSignHeader(COSRequestHeaderKey.RANGE);
            this.tcs = new TaskCompletionSource<>();
            this.mTransferTaskCts = transferTaskCts;
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

            } catch (Exception e) {
                tcs.setError(e);
            }
        }

        private void checkoutManualCanceled() throws CosXmlClientException {
            if (mTransferTaskCts.isCancellationRequested()) {
                throw CosXmlClientException.manualCancelException();
            }
        }

        public Task<GetObjectResult> getTask() {
            return tcs.getTask();
        }

        public void cancel(boolean now) {
            if (headObjectRequest != null) {
                cosDirect.cancel(headObjectRequest, now);
            }
            if (getObjectRequest != null) {
                cosDirect.cancel(getObjectRequest, now);
            }
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        // 1. HeadObject 拿到文件信息 lastModified、contentLength、eTag、crc64ecma
        // 2. 拿到 range 参数
        // 3. 拿到 offset 参数
        private void checking() throws CosXmlClientException, CosXmlServiceException {
            Context context = ContextHolder.getAppContext();
            if (context == null) {
                throw CosXmlClientException.internalException("context is null");
            }
            sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);

            headObjectRequest = new HeadObjectRequest(bucket, key);
            HttpTaskMetrics httpTaskMetrics = new HttpTaskMetrics();
            headObjectRequest.attachMetrics(httpTaskMetrics);
            headObjectRequest.setRegion(region);
            headObjectRequest.setRequestHeaders(getHeadHeaders(getObjectRequest));
            HeadObjectResult headObjectResult = cosDirect.headObject(headObjectRequest);
            mTransferMetrics.connectAddress = httpTaskMetrics.getConnectAddress();
            lastModified = headObjectResult.getHeader("Last-Modified");
            eTag = headObjectResult.getHeader("ETag");
            crc64ecma = headObjectResult.getHeader("x-cos-hash-crc64ecma");
            loggerInfo(TAG, taskId, "start download to %s", getObjectRequest.getDownloadPath());
            loggerInfo(TAG, taskId, "checkout remoteStart=%d, remoteEnd=%d, crc64ecma=%s", remoteStart, remoteEnd, crc64ecma);
        }

        private Map<String, List<String>> getHeadHeaders(GetObjectRequest getObjectRequest) {
            Map<String, List<String>> customHeaders = getObjectRequest.getRequestHeaders();
            if (customHeaders == null) {
                return new HashMap<>();
            }
            return new HashMap<>(customHeaders);
        }

        // 1. 先根据 key 找到 DownloadRecord
        // 2. 校验 DownloadRecord 的有效性
        @Nullable private boolean hasDownloadPart() throws CosXmlClientException {

            String downloadRecordStr = sharedPreferences.getString(key, "");
            if (TextUtils.isEmpty(downloadRecordStr)) {
                loggerInfo(TAG, taskId, "not find DownloadRecord");
                return false;
            }

            loggerInfo(TAG, taskId, "find DownloadRecord: %s", downloadRecordStr);

            try {
                DownloadRecord downloadRecord = DownloadRecord.toJson(downloadRecordStr);
                if (downloadRecord.lastModified == null || !downloadRecord.lastModified.equals(lastModified) ||
                    downloadRecord.eTag == null || !downloadRecord.eTag.equals(eTag) ||
                    (downloadRecord.crc64ecma != null && crc64ecma != null && !downloadRecord.crc64ecma.equals(crc64ecma)) ||
                    downloadRecord.remoteStart != remoteStart || downloadRecord.remoteEnd != remoteEnd) {

                    loggerWarn(TAG, taskId, "verify DownloadRecord failed: lastModified:%s, eTag:%s, crc64ecma:%s, remoteStart:%d, remoteEnd:%d",
                            lastModified, eTag, crc64ecma, remoteStart, remoteEnd);
                    return false;
                } else {
                    return true;
                }
            } catch (JSONException e) {
                loggerInfo(TAG, taskId, "parse DownloadRecord failed: %s", e.getMessage());
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
                loggerInfo(TAG, taskId, "has download part %d", size);
            } else {
                FileUtils.deleteFileIfExist(localFile.getAbsolutePath());
            }

        }

        // 执行 GetObjectRequest 请求
        private GetObjectResult download() throws CosXmlClientException, CosXmlServiceException {

            // 保存下载记录
            try {
                sharedPreferences.edit().putString(key, DownloadRecord.flatJson(
                        new DownloadRecord(lastModified, eTag, crc64ecma, remoteStart, remoteEnd
                        ))).apply();
            } catch (JSONException e) {
                loggerWarn(TAG, taskId,"save DownloadRecord failed: %s", e.getMessage());
            }
            Range range = getObjectRequest.getRange();
            loggerInfo(TAG, taskId, "start download [%d,%d] with fileOffset=%d", range != null ? range.getStart() : 0,
                    range != null ? range.getEnd() : -1, getObjectRequest.getFileOffset());
            GetObjectResult result = cosDirect.getObject(getObjectRequest);
            sharedPreferences.edit().remove(key).apply();
            loggerInfo(TAG, taskId, "download complete");
            return result;
        }

        private void verifyContent(GetObjectResult getObjectResult) throws CosXmlClientException {

            //
            String remoteCRC64 = getObjectResult.getHeader("x-cos-hash-crc64ecma");
            String unencryptedMd5 = getObjectResult.getHeader(Headers.UNENCRYPTED_CONTENT_MD5);
            File localFile = new File(getObjectRequest.getDownloadPath());
            mTransferMetrics.size = localFile.length() - getObjectRequest.getFileOffset();

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
            loggerInfo(TAG, taskId, "check md5=%s success", remoteMD5);
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

            loggerInfo(TAG, taskId, "check offset=%d, size=%d, crc64=%s success", offset, size, remoteCRC);
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

        public DownloadRecord(String lastModified, String eTag, String crc64ecma,
                              long remoteStart, long remoteEnd) {
            this.lastModified = lastModified;
            this.eTag = eTag;
            this.crc64ecma = crc64ecma;
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
            return jsonObject.toString();
        }

        public static DownloadRecord toJson(String str) throws JSONException {

            JSONObject jsonObject = new JSONObject(str);
            String lastModified = jsonObject.getString("lastModified");
            String eTag = jsonObject.getString("eTag");
            String crc64ecma = jsonObject.optString("crc64ecma");
            String remoteStart = jsonObject.getString("remoteStart");
            String remoteEnd = jsonObject.getString("remoteEnd");
            return new DownloadRecord(lastModified, eTag, crc64ecma,
                    Long.parseLong(remoteStart), Long.parseLong(remoteEnd));
        }
    }
}






