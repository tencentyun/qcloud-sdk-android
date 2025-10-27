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


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosTrackService;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.utils.COSUtils;
import com.tencent.cos.xml.utils.CRC64Calculator;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.cos.xml.utils.FileUtils;
import com.tencent.qcloud.core.common.QCloudTaskStateListener;
import com.tencent.qcloud.core.logger.COSLogger;
import com.tencent.qcloud.core.task.QCloudTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载传输任务
 */
public final class COSXMLDownloadTask extends COSXMLTask{

    private final static String TAG = COSXMLDownloadTask.class.getSimpleName();
    private String localSaveDirPath;
    private String localSaveFileName;

    private static final long CRC_CHECK_INTERVAL = 10 * 1024 * 1024; // 10MB校验间隔
    private static final int MAX_RETRY = 3; // 最大重试次数
    private boolean crc64CheckEnabled = true; // CRC校验开关
    private String serverCrc64; // 服务端CRC值
    private Map<Long, Long> crc64Map = new ConcurrentHashMap<>(); // 分块CRC记录
    private long lastCrcCheckPosition = 0L; // 最后校验位置
    private int retryCount = 0; // 当前重试次数
    private ExecutorService crcExecutor = Executors.newSingleThreadExecutor(); // CRC计算线程
    private CountDownLatch crcLatch = new CountDownLatch(1); // CRC计算完成信号量

    /** 下载 */
    private long rangeStart = 0L;
    private long rangeEnd = -1L;
    private long fileOffset = 0L;
    private boolean objectKeySimplifyCheck = true;
    private String eTag;
    private long hasWriteDataLen = 0L;
    private long startTime = 0L;
    private HeadObjectRequest headObjectRequest;
    private GetObjectRequest getObjectRequest;
    private SharedPreferences sharedPreferences;
    /**
     * 标记一次请求的客户端唯一标识，用于日志上报和本地日志记录
     */
    private String clientTraceId;


    COSXMLDownloadTask(Context context, CosXmlSimpleService cosXmlService, String region, String bucket, String cosPath, String localSaveDirPath, String localSaveFileName){
        this.region = region;
        this.bucket = bucket;
        this.cosPath = cosPath;
        this.localSaveDirPath = localSaveDirPath;
        this.localSaveFileName = localSaveFileName;
        this.cosXmlService = cosXmlService;
        if(context != null){
            sharedPreferences = context.getSharedPreferences("COSXMLDOWNLOADTASK", 0);
        }
        this.clientTraceId = UUID.randomUUID().toString();
    }

    COSXMLDownloadTask(Context context, CosXmlSimpleService cosXmlService, GetObjectRequest getObjectRequest){

        this(context, cosXmlService, getObjectRequest.getRegion(), getObjectRequest.getBucket(),
                getObjectRequest.getPath(cosXmlService.getConfig()), getObjectRequest.getSavePath(), getObjectRequest.getSaveFileName());
        this.queries = getObjectRequest.getQueryString();
        this.headers = getObjectRequest.getRequestHeaders();
        this.noSignHeaders = getObjectRequest.getNoSignHeaders();
        this.networkType = getObjectRequest.getNetworkType();
        this.host = getObjectRequest.getHost();
        this.credentialProvider = getObjectRequest.getCredentialProvider();
        this.objectKeySimplifyCheck = getObjectRequest.isObjectKeySimplifyCheck();
        this.isNeedMd5 = getObjectRequest.isNeedMD5();
        //需要取出range字段
        if(this.headers != null && this.headers.containsKey(COSRequestHeaderKey.RANGE)){
            List<String> ranges = this.headers.get(COSRequestHeaderKey.RANGE);
            String range = ranges.get(0);
            int index1 = range.indexOf("=");
            int index2 = range.indexOf("-");
            rangeStart = Long.valueOf(range.substring(index1 + 1, index2));
            String end = range.substring(index2 + 1);
            if(!TextUtils.isEmpty(end)){
                rangeEnd = Long.valueOf(end);
            }

            // 如果设置了range，则不校验crc64 因为服务端不会返回部分文件的crc64
            this.crc64CheckEnabled = false;
        }
        this.fileOffset = getObjectRequest.getFileOffset();
    }

    private boolean checkParameter(){
        if(objectKeySimplifyCheck) {
            String normalizedPath = cosPath;
            try {
                File file = new File("/" + cosPath);
                normalizedPath = file.getCanonicalPath();
            } catch (IOException e) {e.printStackTrace();}
            if ("/".equals(normalizedPath)) {
                updateState(TransferState.FAILED, new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "The key in the getobject is illegal"), null, false);
                return false;
            }
        }
        return true;
    }

    /**
     * 下载操作
     */
    protected void download(){
        if(!checkParameter()) return;
        startTime = System.nanoTime();
        this.clientTraceId = UUID.randomUUID().toString();
        run();
    }

    private void realDownload(final long rangeStart, final long rangeEnd, final long fileOffset){
        getObjectRequest = new GetObjectRequest(bucket, cosPath, localSaveDirPath, localSaveFileName);
        getObjectRequest.setRegion(region);
        getObjectRequest.setFileOffset(fileOffset);
        getObjectRequest.setQueryParameters(queries);
        getObjectRequest.setRequestHeaders(headers);
        getObjectRequest.setObjectKeySimplifyCheck(objectKeySimplifyCheck);
        getObjectRequest.addNoSignHeader(noSignHeaders);
        getObjectRequest.setNetworkType(networkType);
        getObjectRequest.setHost(host);
        getObjectRequest.setCredentialProvider(credentialProvider);
        if(rangeEnd > 0 || rangeStart > 0){
            getObjectRequest.setRange(rangeStart, rangeEnd);
        }

        if(onSignatureListener != null){
            getObjectRequest.setSign(onSignatureListener.onGetSign(getObjectRequest));
        }

        getHttpMetrics(getObjectRequest, "GetObjectRequest");

        getObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                // 每10MB计算CRC64，并处理最后不足10M的部分
                long currentPosition = hasWriteDataLen + complete;
                if(crc64CheckEnabled) {
                    // 检查是否达到10MB间隔或是最后一段数据
                    if(currentPosition - lastCrcCheckPosition >= CRC_CHECK_INTERVAL ||
                       (complete == target && currentPosition > lastCrcCheckPosition)) {
                        COSLogger.dProcess(TAG, "calculateFileCrc start: " + lastCrcCheckPosition + " end: " + currentPosition);
                        long start = lastCrcCheckPosition;
                        lastCrcCheckPosition = currentPosition;
                        calculateFileCrc(start, currentPosition, complete == target);
                    }
                }

                if(cosXmlProgressListener != null){
                    cosXmlProgressListener.onProgress(currentPosition, hasWriteDataLen + target);
                }
            }
        });
        getObjectRequest.setClientTraceId(this.clientTraceId);
        cosXmlService.internalGetObjectAsync(getObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if(request != getObjectRequest){
                    return;
                }

                // 获取服务端CRC64值
                if(crc64CheckEnabled) {
                    serverCrc64 = result.getHeader("x-cos-hash-crc64ecma");
                    COSLogger.dProcess(TAG, "serverCrc64: " + serverCrc64);

                    // 等待所有CRC计算完成
                    try {
                        crcLatch.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        handleCrcCheckFailure();
                        return;
                    }
                }

                // 执行最终校验
                if(crc64CheckEnabled && !validateCrc64()) {
                    handleCrcCheckFailure();
                    return;
                }

                CosTrackService.getInstance().reportDownloadTaskSuccess(getObjectRequest, getCosXmlServiceConfigTrackParams());

                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                updateState(TransferState.COMPLETED, null, result, false);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if(request != getObjectRequest){
                    return;
                }

                Exception causeException = null;
                if (clientException != null && taskState != TransferState.PAUSED && taskState != TransferState.CANCELED) {
                    CosTrackService.getInstance().reportDownloadTaskClientException(request, clientException, getCosXmlServiceConfigTrackParams());
                    causeException = clientException;
                }

                if (serviceException != null && taskState != TransferState.PAUSED && taskState != TransferState.CANCELED) {
                    // BeaconService.getInstance().reportDownload(region, BeaconService.EVENT_PARAMS_NODE_GET, serviceException);
                    // CosTrackService.getInstance().reportDownload(region, CosTrackService.EVENT_PARAMS_NODE_GET, serviceException);
                    CosTrackService.getInstance().reportDownloadTaskServiceException(request, serviceException, getCosXmlServiceConfigTrackParams());
                    causeException = serviceException;
                }
                // causeException.printStackTrace();

                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                updateState(TransferState.FAILED, causeException, null, false);
            }
        });
    }


    @Override
    protected CosXmlRequest buildCOSXMLTaskRequest() {
        COSXMLDownloadTaskRequest cosxmlDownloadTaskRequest = new COSXMLDownloadTaskRequest(region, bucket, cosPath,
                localSaveDirPath, localSaveFileName, headers, queries);
       return cosxmlDownloadTaskRequest;
    }

    @Override
    protected CosXmlResult buildCOSXMLTaskResult(CosXmlResult sourceResult) {
        COSXMLDownloadTaskResult cosxmlDownloadTaskResult = new COSXMLDownloadTaskResult();
        if(sourceResult != null){
            cosxmlDownloadTaskResult.httpCode = sourceResult.httpCode;
            cosxmlDownloadTaskResult.httpMessage = sourceResult.httpMessage;
            cosxmlDownloadTaskResult.headers = sourceResult.headers;
            cosxmlDownloadTaskResult.eTag = eTag;
            cosxmlDownloadTaskResult.accessUrl = sourceResult.accessUrl;
        }
        return cosxmlDownloadTaskResult;
    }

    private String getKey(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("download").append("_")
                .append(region).append("_")
                .append(bucket).append("_")
                .append(cosPath).append("_")
                .append(rangeStart).append("_")
                .append(rangeEnd).append("_")
                .append(fileOffset).append("_")
                .append(localSaveDirPath).append("_")
                .append(localSaveFileName).append("_")
                .append(eTag);
        try {
            return DigestUtils.getSha1(stringBuffer.toString());
        } catch (CosXmlClientException e) {
            return stringBuffer.toString();
        }
    }

    /**
     * 第一次下载时，如果当前路径下文件已存在，则先删除
     *
     * @return
     */
//    private synchronized String removeIfExist(){
//        if(sharedPreferences != null){
//
//            String filePath = sharedPreferences.getString(getKey(), null);
//            if (filePath == null) { // 第一次下载
//                FileUtils.deleteFileIfExist(getDownloadPath());
//            }
//            return filePath;
//        }
//        return null;
//    }

    private synchronized String hasExisted(){
        if(sharedPreferences != null){
            return sharedPreferences.getString(getKey(), null);
        }
        return null;
    }

    private synchronized void clear(){
        if(sharedPreferences != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(getKey()); // 清理下载记录
            editor.remove(getKey() + "_crc"); // 清理CRC记录
            editor.commit();
        }
    }

    private void cancelAllRequest(boolean now){
        HeadObjectRequest tempHeadObjectRequest = headObjectRequest;
        cosXmlService.cancel(tempHeadObjectRequest, now);
        GetObjectRequest tempGetObjectRequest = getObjectRequest;
        cosXmlService.cancel(tempGetObjectRequest, now);
    }

    private synchronized void save(String absolutePath){
        if(sharedPreferences != null){
            sharedPreferences.edit().putString(getKey(), absolutePath).commit();
        }
    }

    // CRC64计算和校验方法
    private void calculateFileCrc(long start, long end, boolean isLast)  {
        crcExecutor.execute(() -> {
            try {
                File file = new File(getDownloadPath());
                long crcValue = CRC64Calculator.getCRC64(new FileInputStream(file), start, end - start);
                crc64Map.put(start, crcValue);

                // 每次计算完CRC后立即持久化
                synchronized (COSXMLDownloadTask.this) {
                    if (sharedPreferences != null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getKey() + "_crc", serializeCrcMap());
                        editor.apply();
                    }
                }

                COSLogger.dProcess(TAG, "calculateFileCrc finish start: " + start + " end: " + end + " crcValue= " + crcValue);

                // 如果是最后一次计算，通知主线程
                if(isLast) {
                    crcLatch.countDown();
                }
            } catch (IOException e) {
                COSLogger.wProcess(TAG, "CRC64 calculation error: " + e.getMessage());
                handleCrcCheckFailure();
            }
        });
    }

    private boolean validateCrc64() {
        // 如果没有serverCrc64，则认为校验通过
        if(TextUtils.isEmpty(serverCrc64)){
            return true;
        }

        try {
            String mergedCrc = mergeCrcValues();
            COSLogger.dProcess(TAG, "mergedCrc= " + mergedCrc);
            return mergedCrc.equals(serverCrc64);
        } catch (Exception e) {
            COSLogger.wProcess(TAG, "MergeCRC64 validation error: " + e.getMessage());
            return false;
        }
    }

    private String mergeCrcValues() {
        Long[] sortedKeys = crc64Map.keySet().toArray(new Long[0]);
        Arrays.sort(sortedKeys);

        COSLogger.dProcess(TAG, "crc64Map sortedKeys= " + Arrays.toString(sortedKeys) + " lastCrcCheckPosition= " + lastCrcCheckPosition);

        long mergedCrc = new BigInteger(Long.toUnsignedString(crc64Map.get(sortedKeys[0]),10).trim()).longValue();

        for (int i = 1; i < sortedKeys.length; i++) {
            long start = sortedKeys[i];
            long blockLength = (i == sortedKeys.length - 1) ?
                lastCrcCheckPosition - start :
                sortedKeys[i + 1] - start;

            long crc2 = new BigInteger(Long.toUnsignedString(crc64Map.get(start),10).trim()).longValue();
            mergedCrc = CRC64Calculator.combine(
                mergedCrc, crc2, blockLength
            );
        }

        return Long.toUnsignedString(mergedCrc);
    }

    private void handleCrcCheckFailure() {
        COSLogger.wProcess(TAG, "CRC64 check failed:" + retryCount);
        if(retryCount++ < MAX_RETRY) {
            // 先中断正在执行的CRC计算任务
            crcExecutor.shutdownNow();

            // 删除错误文件
            if(!FileUtils.deleteFileIfExist(getDownloadPath())){
                updateState(TransferState.FAILED,
                        new CosXmlClientException(ClientErrorCode.IO_ERROR.getCode(), "CRC64 check failed"),
                        null,
                        false);
                return;
            }
            // 重置状态重试
            clear();
            crc64Map.clear();
            lastCrcCheckPosition = 0L;
            IS_EXIT.set(false);

            // 重置线程池(需要重新创建实例)
            crcExecutor = Executors.newSingleThreadExecutor();
            // 重置信号量
            crcLatch = new CountDownLatch(1);
            // 重新开始下载
            download();
        } else {
            updateState(TransferState.FAILED,
                new CosXmlClientException(ClientErrorCode.IO_ERROR.getCode(), "CRC64 check failed"),
                null,
                false);
        }
    }

    // 续传时校验已有CRC记录
    /**
     * 校验本地文件已下载部分的CRC值
     * @param file 本地文件
     * @param checkLength 需要校验的长度
     * @return 是否校验通过
     */
    private boolean validatePartialCrc(File file, long checkLength) throws IOException {
        if(checkLength <= 0) return true;

        try (FileInputStream fis = new FileInputStream(file)) {
            // 分段校验(每10MB一段)
            long segmentSize = 10 * 1024 * 1024;
            long position = 0;

            while(position < checkLength) {
                long end = Math.min(position + segmentSize, checkLength);
                long crcValue = CRC64Calculator.getCRC64(fis, position, end - position);

                // 检查记录的CRC值是否匹配
                Long savedCrc = crc64Map.get(position);
                if(savedCrc == null || savedCrc != crcValue) {
                    return false;
                }

                position = end;
            }
            return true;
        }
    }

    /**
     * 序列化CRC64 Map为字符串
     * 格式: "key1:value1,key2:value2,..."
     */
    private String serializeCrcMap() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, Long> entry : crc64Map.entrySet()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(entry.getKey()).append(":").append(entry.getValue());
        }
        return sb.toString();
    }

    /**
     * 从字符串反序列化为CRC64 Map
     * @param data 序列化后的字符串，格式为"key1:value1,key2:value2,..."
     */
    private void deserializeCrcMap(String data) {
        crc64Map.clear();
        if (TextUtils.isEmpty(data)) {
            return;
        }

        String[] entries = data.split(",");
        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                try {
                    long key = Long.parseLong(parts[0]);
                    long value = Long.parseLong(parts[1]);
                    crc64Map.put(key, value);
                } catch (NumberFormatException e) {
                    COSLogger.wProcess(TAG, "Failed to parse CRC64 entry: " + entry);
                }
            }
        }
    }

    /**
     * 获取最后一个CRC记录的起始位置
     * @return 最后一个CRC记录的key(起始位置)，如果没有记录返回0
     */
    private long crc64MapLastKey() {
        long last = 0L;
        for (Long key : crc64Map.keySet()) {
            if (key > last) {
                last = key;
            }
        }
        return last;
    }

    private String getDownloadPath(){
        String path  = null;
        if(localSaveDirPath != null){
            if(!localSaveDirPath.endsWith("/")){
                path = localSaveDirPath + "/";
            }else{
                path = localSaveDirPath;
            }
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            if (localSaveFileName != null){
                path = path + localSaveFileName;
                return path;
            }
            if(cosPath != null){
                int separator = cosPath.lastIndexOf("/");
                if(separator >= 0){
                    path = path + cosPath.substring(separator + 1);
                }else{
                    path = path + cosPath;
                }
            }
        }
        return path;
    }

    protected void run() {

        headObjectRequest = new HeadObjectRequest(bucket, cosPath);
        headObjectRequest.setRequestHeaders(headers);
        headObjectRequest.addNoSignHeader(noSignHeaders);
        headObjectRequest.setNetworkType(networkType);
        headObjectRequest.setHost(host);
        headObjectRequest.setCredentialProvider(credentialProvider);
        headObjectRequest.setQueryParameters(queries);
        headObjectRequest.setRegion(region);
        final String downloadPath = getDownloadPath();

        if(onSignatureListener != null){
            headObjectRequest.setSign(onSignatureListener.onGetSign(headObjectRequest));
        }

        getHttpMetrics(headObjectRequest, "HeadObjectRequest");

        headObjectRequest.setTaskStateListener(new QCloudTaskStateListener() {
            @Override
            public void onStateChanged(String taskId, int state) {
                if(IS_EXIT.get())return;
                if (state != QCloudTask.STATE_QUEUEING) {
                    updateState(TransferState.IN_PROGRESS, null, null, false);
                }
            }
        });
        headObjectRequest.setClientTraceId(this.clientTraceId);

        cosXmlService.headObjectAsync(headObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if(request != headObjectRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                List<String> eTags = result.headers.get("ETag");
                if(eTags != null && eTags.size() > 0){
                    eTag = eTags.get(0);
                }
                String absolutePath = hasExisted();
                if(absolutePath != null){
                    File file = new File(absolutePath);
                    if(file.exists()){
                        String savedCrc = sharedPreferences.getString(getKey() + "_crc", null);
                        // 如果文件存在且crc记录不存在，说明是遗留续传，不开启CRC校验
                        crc64CheckEnabled = !TextUtils.isEmpty(savedCrc);
                        long fileLength = file.length();

                        // 检查CRC校验记录
                        if(savedCrc != null && crc64CheckEnabled){
                            deserializeCrcMap(savedCrc);
                            long lastCrcPosition = crc64Map.isEmpty() ? 0 : crc64MapLastKey();

                            // 情况1：本地文件比CRC记录长（APP被强制终止导致）
                            if(fileLength > lastCrcPosition){
                                COSLogger.dProcess(TAG, "Local file is longer than CRC records, will truncate to last valid position");
                                // 截断文件到最后一个有效CRC位置
                                if(!FileUtils.truncateFile(file, lastCrcPosition)) {
                                    COSLogger.dProcess(TAG, "File truncate failed, will handle as failure");
                                    handleCrcCheckFailure();
                                    return;
                                }
                                // 验证CRC
                                try {
                                    if(!validatePartialCrc(file, lastCrcPosition)){
                                        COSLogger.dProcess(TAG, "CRC check failed for existing file");
                                        handleCrcCheckFailure();
                                        return;
                                    }
                                } catch (IOException e) {
                                    COSLogger.wProcess(TAG, "CRC validation error: " + e.getMessage());
                                    handleCrcCheckFailure();
                                    return;
                                }
                            }

                            // 情况2：本地文件比CRC记录短（文件被部分删除）
                            if(fileLength < lastCrcPosition){
                                COSLogger.dProcess(TAG, "Local file is shorter than CRC records");
                                handleCrcCheckFailure();
                                return;
                            }

                            // 情况3：长度匹配，验证CRC
                            try {
                                if(!validatePartialCrc(file, lastCrcPosition)){
                                    COSLogger.dProcess(TAG, "CRC check failed for existing file");
                                    handleCrcCheckFailure();
                                    return;
                                }
                            } catch (IOException e) {
                                COSLogger.wProcess(TAG, "CRC validation error: " + e.getMessage());
                                handleCrcCheckFailure();
                                return;
                            }

                            List<String> contentLengths = result.headers.get("Content-Length");
                            // 检查文件长度是否匹配
                            if(contentLengths != null && contentLengths.size() > 0 && Long.valueOf(contentLengths.get(0)) == fileLength){
                                if(cosXmlProgressListener != null){
                                    cosXmlProgressListener.onProgress(fileLength, fileLength);
                                }
                                IS_EXIT.set(true);
                                updateState(TransferState.COMPLETED, null, result, false);
                                return;
                            }

                            // CRC验证通过，从记录位置继续下载
                            hasWriteDataLen = lastCrcPosition - fileOffset;
                            realDownload(rangeStart + hasWriteDataLen, rangeEnd, fileOffset + hasWriteDataLen);
                            return;
                        }

                        List<String> contentLengths = result.headers.get("Content-Length");
                        // 检查文件长度是否匹配
                        if(contentLengths != null && contentLengths.size() > 0 && Long.valueOf(contentLengths.get(0)) == fileLength){
                            if(cosXmlProgressListener != null){
                                cosXmlProgressListener.onProgress(fileLength, fileLength);
                            }
                            IS_EXIT.set(true);
                            updateState(TransferState.COMPLETED, null, result, false);
                            return;
                        }

                        // 没有CRC记录，简单续传
                        hasWriteDataLen = fileLength - fileOffset;
                        realDownload(rangeStart + hasWriteDataLen, rangeEnd, fileOffset + hasWriteDataLen);
                        return;
                    }
                }
                // 第一次下载文件，会先删除本地文件
                FileUtils.deleteFileIfExist(downloadPath);
                save(getDownloadPath()); // 第一次下载
                hasWriteDataLen = 0L;
                realDownload(rangeStart, rangeEnd, fileOffset);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if(request != headObjectRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                Exception causeException = clientException == null ? serviceException : clientException;
                causeException.printStackTrace();
                updateState(TransferState.FAILED, causeException, null, false);
                COSLogger.iProcess(TAG, "head " + cosPath + "failed !, exception is " + causeException.getMessage(), causeException);
            }
        });
    }

    @Override
    protected void internalCompleted() {
        clear();
    }

    @Override
    protected void internalFailed() {
        cancelAllRequest(false);
    }

    @Override
    protected void internalPause(boolean now) {
        if (getObjectRequest != null) {
            CosTrackService.getInstance().reportDownloadTaskSuccess(getObjectRequest, getCosXmlServiceConfigTrackParams());
        }
        cancelAllRequest(now);
    }

    @Override
    protected void internalCancel(boolean now) {
        cancelAllRequest(now);
        clear();
    }

    @Override
    protected void internalResume() {
        taskState = TransferState.WAITING;
        IS_EXIT.set(false);
        download();
    }

    @Override
    protected void encounterError(@Nullable CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
        if(IS_EXIT.get())return;
        IS_EXIT.set(true);
        updateState(TransferState.FAILED, COSUtils.mergeException(clientException, serviceException), null, false);
    }

    /**
     * 下载传输任务的请求
     */
    public static class COSXMLDownloadTaskRequest extends GetObjectRequest{
        protected COSXMLDownloadTaskRequest(String region, String bucket, String cosPath, String savePath, String saveFileName, Map<String, List<String>> headers,
                                            Map<String, String> queryStr){
            super(bucket, cosPath, savePath, saveFileName);
            this.setRegion(region);
            this.setRequestHeaders(headers);
            this.setQueryParameters(queryStr);
        }
    }

    /**
     * 下载传输任务的返回结果
     */
    public static class COSXMLDownloadTaskResult extends CosXmlResult{
        protected COSXMLDownloadTaskResult(){}
        public String eTag;
    }
}
