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
import android.net.Uri;
import android.text.TextUtils;

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
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.ListPartsResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.cos.xml.model.tag.InitiateMultipartUpload;
import com.tencent.cos.xml.model.tag.ListParts;
import com.tencent.cos.xml.model.tag.UrlUploadPolicy;
import com.tencent.cos.xml.model.tag.pic.PicUploadResult;
import com.tencent.cos.xml.utils.CloseUtil;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.qcloud.core.common.QCloudTaskStateListener;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.QCloudTask;
import com.tencent.qcloud.core.util.ContextHolder;
import com.tencent.qcloud.core.util.QCloudUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 上传传输任务
 */
public final class COSXMLUploadTask extends COSXMLTask {

    private final String TAG = "UploadTask";

    private static Executor executor = Executors.newSingleThreadExecutor();

    /** 满足分片上传的文件最小长度 */
    protected long multiUploadSizeDivision;
    /** 源文件的本地路径 */
    String srcPath;
    /** 源文件的长度 */
    private long fileLength;

    /** 字节数组, COSXMLUploadTask 中只支持简单上传 */
    private byte[] bytes;

    /** 字节流，COSXMLUploadTask 中只支持简单上传 */
    private InputStream inputStream;

    private Uri uri;

    private URL url;
    private UrlUploadPolicy urlUploadPolicy;

    /** 简单上传 */
    private PutObjectRequest putObjectRequest;

    // 查询文件
    private HeadObjectRequest headObjectRequest;

    /** 分片上传*/
    private boolean isSliceUpload = false;
    /** 分片大小 */
    protected long sliceSize;
    /** 分片上传 UploadId 属性 */
    private String uploadId;
    /** 初始化分片上传 */
    private InitMultipartUploadRequest initMultipartUploadRequest;
    /** 列举以上传的分片 */
    private ListPartsRequest listPartsRequest;
    /** 完成所有上传分片 */
    private CompleteMultiUploadRequest completeMultiUploadRequest;
    /** 上传分片块 */
    private Map<UploadPartRequest,Long> uploadPartRequestLongMap;
    private Map<Integer, SlicePartStruct> partStructMap;
    private AtomicInteger UPLOAD_PART_COUNT;
    private AtomicLong ALREADY_SEND_DATA_LEN;
    private Object SYNC_UPLOAD_PART = new Object();
    private long startTime = 0L;
    private long simpleAlreadySendDataLen = 0L;
    boolean forceSimpleUpload;
    boolean priorityLow = false;

    HttpTaskMetrics httpTaskMetrics;


    /**
     * 正在发送 CompleteMultiUpload 请求的过程中不允许暂停
     */
    private AtomicBoolean sendingCompleteRequest = new AtomicBoolean(false);

    private WeightStrategy weightStrategy = new WeightStrategy();

    private MultiUploadsStateListener multiUploadsStateListenerHandler = new MultiUploadsStateListener() {
        @Override
        public void onInit() {
            multiUploadPart(cosXmlService);
        }

        @Override
        public void onListParts() {
            multiUploadPart(cosXmlService);
        }

        @Override
        public void onUploadParts() {
            completeMultiUpload(cosXmlService);
        }

        @Override
        public void onCompleted(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
            updateState(TransferState.COMPLETED, null, cosXmlResult, false);
        }

        @Override
        public void onFailed(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
            Exception causeException = clientException == null ? serviceException : clientException;
            reportException(cosXmlRequest, clientException, serviceException);
            updateState(TransferState.FAILED, causeException, null, false);
        }
    };

    private COSXMLUploadTask(CosXmlSimpleService cosXmlService, String region, String bucket, String cosPath){
        this.cosXmlService = cosXmlService;
        this.region = region;
        this.bucket = bucket;
        this.cosPath = cosPath;
        httpTaskMetrics = new HttpTaskMetrics(); // HttpTaskMetrics.createMetricsWithHost(cosXmlService.getConfig().getHeaderHost(region, bucket));
    }

    COSXMLUploadTask(CosXmlSimpleService cosXmlService, String region, String bucket, String cosPath, String srcPath, String uploadId){
        this(cosXmlService, region, bucket, cosPath);
        this.srcPath = srcPath;
        this.uploadId = uploadId;
    }

    COSXMLUploadTask(CosXmlSimpleService cosXmlService, String region, String bucket, String cosPath, Uri uri, String uploadId){
        this(cosXmlService, region, bucket, cosPath);
        this.uri = uri;
        this.uploadId = uploadId;
    }

    COSXMLUploadTask(CosXmlSimpleService cosXmlService, String region, String bucket, String cosPath, URL url, UrlUploadPolicy urlUploadPolicy, String uploadId){
        this(cosXmlService, region, bucket, cosPath);
        this.url = url;
        this.urlUploadPolicy = urlUploadPolicy;
        this.uploadId = uploadId;
    }

    COSXMLUploadTask(CosXmlSimpleService cosXmlService, String region, String bucket, String cosPath, URL url, String uploadId){
        this(cosXmlService, region, bucket, cosPath);
        this.url = url;
        this.uploadId = uploadId;
    }

    COSXMLUploadTask(CosXmlSimpleService cosXmlService, String region, String bucket, String cosPath, byte[] bytes){
        this(cosXmlService, region, bucket, cosPath);
        this.bytes = bytes;
    }

    COSXMLUploadTask(CosXmlSimpleService cosXmlService, String region, String bucket, String cosPath, InputStream inputStream){
        this(cosXmlService, region, bucket, cosPath);
        this.inputStream = inputStream;
    }

    COSXMLUploadTask(CosXmlSimpleService cosXmlService, PutObjectRequest putObjectRequest, String uploadId){
//        this(cosXmlService, putObjectRequest.getRegion(), putObjectRequest.getBucket(), putObjectRequest.getPath(cosXmlService.getConfig()),
//                putObjectRequest.getSrcPath(), uploadId);
        this(cosXmlService, putObjectRequest.getRegion(), putObjectRequest.getBucket(), putObjectRequest.getPath(cosXmlService.getConfig()));
        this.uri = putObjectRequest.getUri();
        this.url = putObjectRequest.getUrl();
        this.urlUploadPolicy = putObjectRequest.getUrlUploadPolicy();
        this.srcPath = putObjectRequest.getSrcPath();
        this.bytes = putObjectRequest.getData();
        this.inputStream = putObjectRequest.getInputStream();

        this.queries = putObjectRequest.getQueryString();
        this.headers = putObjectRequest.getRequestHeaders();
        this.isNeedMd5 = putObjectRequest.isNeedMD5();
        this.uploadId = uploadId;
        this.priorityLow = putObjectRequest.isPriorityLow();
    }


    protected boolean checkParameter(){
        if(bytes == null && inputStream == null && srcPath == null && uri == null && url == null){

            if(IS_EXIT.get()) return false;
            IS_EXIT.set(true);
            multiUploadsStateListenerHandler.onFailed(new PutObjectRequest(bucket, cosPath, ""),
                    new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "source is is invalid: null"),
                    null);
            return false;
        }
        if(srcPath != null){
            File file = new File(srcPath);
            if(!file.exists() || file.isDirectory() || !file.canRead()){
                if(IS_EXIT.get()) return false;
                IS_EXIT.set(true);
                multiUploadsStateListenerHandler.onFailed(new PutObjectRequest(bucket, cosPath, srcPath),
                        new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "srcPath is is invalid: " + srcPath),
                        null);
                return false;
            }
            fileLength = file.length();
        }

        if (uri != null) {
            Context context = ContextHolder.getAppContext();
            if (context != null) {
                fileLength = QCloudUtils.getUriContentLength(uri, context.getContentResolver());
            }
        }

        if(url != null && urlUploadPolicy != null){
            if(urlUploadPolicy.getDownloadType()!= UrlUploadPolicy.Type.NOTSUPPORT) {
                fileLength = urlUploadPolicy.getFileLength();
            } else {
                if(IS_EXIT.get())return false;
                monitor.sendStateMessage(this, TransferState.FAILED,
                        new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "url not support download"), null, TaskStateMonitor.MESSAGE_TASK_RESULT);
                IS_EXIT.set(true);
                return false;
            }
        }

        return true;
    }

    /**
     * 上传操作
     */
    protected void upload(){
        if(!checkParameter()) return;
        startTime = System.nanoTime();
        startUpload();
    }

    private void dispatchProgressChange(long complete, long target) {

        if (cosXmlProgressListener != null) {
            cosXmlProgressListener.onProgress(complete, target);
        }

        if (internalProgressListener != null) {
            internalProgressListener.onProgress(complete, target);
        }
    }

    private void dispatchInitMultipleUpload(InitiateMultipartUpload initiateMultipartUpload) {
        if (initMultipleUploadListener != null) {
            initMultipleUploadListener.onSuccess(initiateMultipartUpload);
        }

        if (internalInitMultipleUploadListener != null) {
            internalInitMultipleUploadListener.onSuccess(initiateMultipartUpload);
        }
    }

    private void simpleUpload(CosXmlSimpleService cosXmlService){
        if(bytes != null){
            putObjectRequest = new PutObjectRequest(bucket, cosPath, bytes);
        }else if(inputStream != null){
            putObjectRequest = new PutObjectRequest(bucket, cosPath, inputStream);
        }else if (uri != null) {
            putObjectRequest = new PutObjectRequest(bucket, cosPath, uri);
        }else if (url != null) {
            putObjectRequest = new PutObjectRequest(bucket, cosPath, url);
        } else {
            putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
        }

        putObjectRequest.setRegion(region);
        if (url != null) {
            putObjectRequest.setNeedMD5(false);
        } else {
            putObjectRequest.setNeedMD5(isNeedMd5);

        }
        putObjectRequest.setRequestHeaders(headers);

        if(onSignatureListener != null){
            putObjectRequest.setSign(onSignatureListener.onGetSign(putObjectRequest));
        }
        getHttpMetrics(putObjectRequest, "PutObjectRequest");

        putObjectRequest.setTaskStateListener(new QCloudTaskStateListener() {
            @Override
            public void onStateChanged(String taskId, int state) {
                if(IS_EXIT.get())return;
                if (state == QCloudTask.STATE_EXECUTING || state == QCloudTask.STATE_COMPLETE) {
                    onUpdateInProgress();
                }
            }
        });
        putObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                simpleAlreadySendDataLen = complete;
                dispatchProgressChange(complete, target);
            }
        });
        if (priorityLow) {
            putObjectRequest.setPriorityLow();
        }

        cosXmlService.putObjectAsync(putObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if(request != putObjectRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                //BeaconService.getInstance().reportUpload(region, simpleAlreadySendDataLen, TimeUtils.getTookTime(startTime));
                BeaconService.getInstance().reportUploadTaskSuccess(request);
                updateState(TransferState.COMPLETED, null, result, false);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if(request != putObjectRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                multiUploadsStateListenerHandler.onFailed(request, clientException, serviceException);
            }
        });
    }

    private void reportException(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {

        if (request == null) {
            request = buildCOSXMLTaskRequest();
        }
        
        if (clientException != null) {
            BeaconService.getInstance().reportUploadTaskClientException(request, clientException);
        }
        if (serviceException != null) {
            BeaconService.getInstance().reportUploadTaskServiceException(request, serviceException);
        }

    }

    private void multiUpload(CosXmlSimpleService cosXmlService){
        initSlicePart(0, fileLength, 1);
        if(!TextUtils.isEmpty(uploadId)){
            listMultiUpload(cosXmlService);
        }else {
            initMultiUpload(cosXmlService);
        }
    }

    private void initMultiUpload(CosXmlSimpleService cosXmlService){
        initMultipartUploadRequest = new InitMultipartUploadRequest(bucket, cosPath);
        initMultipartUploadRequest.setRegion(region);

        initMultipartUploadRequest.setRequestHeaders(headers);

        if(onSignatureListener != null){
            initMultipartUploadRequest.setSign(onSignatureListener.onGetSign(initMultipartUploadRequest));
        }

        httpTaskMetrics.setDomainName(initMultipartUploadRequest.getRequestHost(cosXmlService.getConfig()));

        getHttpMetrics(initMultipartUploadRequest, "InitMultipartUploadRequest");

//        initMultipartUploadRequest.setTaskStateListener(new QCloudTaskStateListener() {
//            @Override
//            public void onStateChanged(String taskId, int state) {
//                if(IS_EXIT.get())return;
//                updateState(TransferState.WAITING, null, null, false);
//            }
//        });
        cosXmlService.initMultipartUploadAsync(initMultipartUploadRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if(request != initMultipartUploadRequest){
                    return;
                }
                // notify -> upload part
                if(IS_EXIT.get())return;
                onUpdateInProgress();

                InitiateMultipartUpload initMultipartUpload = ((InitMultipartUploadResult)result).initMultipartUpload;
                uploadId = initMultipartUpload.uploadId;
                multiUploadsStateListenerHandler.onInit();
                dispatchInitMultipleUpload(initMultipartUpload);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                if(request != initMultipartUploadRequest){
                    return;
                }
                // notify -> exit caused by failed
                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                multiUploadsStateListenerHandler.onFailed(request, exception, serviceException);
            }
        });
    }

    private void listMultiUpload(CosXmlSimpleService cosXmlService){
        listPartsRequest = new ListPartsRequest(bucket, cosPath, uploadId);
        listPartsRequest.setRegion(region);
        listPartsRequest.setRequestHeaders(headers);

        if(onSignatureListener != null){
            listPartsRequest.setSign(onSignatureListener.onGetSign(listPartsRequest));
        }
        httpTaskMetrics.setDomainName(listPartsRequest.getRequestHost(cosXmlService.getConfig()));
        getHttpMetrics(listPartsRequest, "ListPartsRequest");

        listPartsRequest.setTaskStateListener(new QCloudTaskStateListener() {
            @Override
            public void onStateChanged(String taskId, int state) {
                if(IS_EXIT.get())return;
                onUpdateInProgress();
            }
        });

        cosXmlService.listPartsAsync(listPartsRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, final CosXmlResult result) {
                if(request != listPartsRequest){
                    return;
                }
                //update list part, then upload part.
                if(IS_EXIT.get())return;

                // 外部URL链接不做校验
                if (url != null) {
                    updateSlicePart((ListPartsResult)result);
                    multiUploadsStateListenerHandler.onListParts();
                } else {
                    // 虽然这里默认是在 TaskExecutors.UPLOAD_EXECUTOR 线程中回调，但是用户可能主动设置了
                    // UI 线程为 observe 线程，这里为了防止阻塞 UI，另起一个线程池
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            boolean verifySuccess;
                            InputStream uploadFileStream = null;
                            try {
                                uploadFileStream = openUploadFileStream();
                                verifySuccess = verifyUploadParts(((ListPartsResult)result).listParts, uploadFileStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                                verifySuccess = false;
                            } finally {
                                if (uploadFileStream != null) {
                                    try {
                                        CloseUtil.closeQuietly(uploadFileStream);
                                    } catch (CosXmlClientException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            if (verifySuccess) {
                                updateSlicePart((ListPartsResult)result);
                                multiUploadsStateListenerHandler.onListParts();
                            } else {
                                reTrans();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFail(final CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if(request != listPartsRequest){
                    return;
                }
                if(IS_EXIT.get())return;


                if (serviceException != null && "NoSuchUpload".equals(serviceException.getErrorCode())) {

                    // uploadId 不存在，可以分为两种情况：
                    // 1. 文件已经已经上传；
                    // 2. 文件没有上传；
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            HeadObjectResult headObjectResult = headObjectToCheckCRC64();
                            if (headObjectResult != null) {
                                onTransferComplete(request, headObjectResult);
                            } else if(!IS_EXIT.get()) {
                                reTrans();
                            }
                        }
                    });

                } else {
                    IS_EXIT.set(true);
                    multiUploadsStateListenerHandler.onFailed(request, clientException, serviceException);
                }
            }
        });
    }

    private void onUpdateInProgress() {
        updateState(TransferState.IN_PROGRESS, null, null, false);
        if (waitTimeoutTimer != null) {
            waitTimeoutTimer.cancel();
        }
    }

    // 需要主动关闭流
    private InputStream openUploadFileStream() throws IOException {

        if (srcPath != null) {
            return new FileInputStream(srcPath);
        } else if (uri != null) {
            if (ContextHolder.getAppContext() == null) {
                throw new IOException("Open src file failed, Application context is null!");
            }
            return ContextHolder.getAppContext().getContentResolver().openInputStream(uri);
        }
        throw new IOException("There is no src file path or uri!");
    }


    private boolean verifyUploadParts(ListParts listParts, InputStream inputStream) throws IOException {

        List<ListParts.Part> parts = listParts.parts;
        Collections.sort(parts, new Comparator<ListParts.Part>() {
            @Override
            public int compare(ListParts.Part a, ListParts.Part b) {
                int aNumb = Integer.valueOf(a.partNumber);
                int bNumb = Integer.valueOf(b.partNumber);
                if(aNumb > bNumb) return 1;
                if(aNumb < bNumb) return -1;
                return 0;
            }
        });

        boolean isFixSliceSize = isFixSliceSize(parts);
        boolean isContinuousSlice = true;
        int lastPartNumber = 0;
        for (ListParts.Part part : parts) {
            int partNumber = Integer.parseInt(part.partNumber);
            isContinuousSlice = isContinuousSlice && lastPartNumber + 1 == partNumber;

            // 变长分块大小，且不连续的分块会抛弃
            if (!isFixSliceSize && !isContinuousSlice) {
                return true;
            }
            String localMd5 = DigestUtils.getCOSMd5(inputStream, (partNumber - lastPartNumber - 1) * sliceSize, Long.parseLong(part.size));
            if (!part.eTag.equals(localMd5)) {
                QCloudLogger.i(TAG, "verify upload parts failed, part number " +
                        part.partNumber + ", etag " + part.eTag + ", but local md5 is " + localMd5);
                return false;
            }
            lastPartNumber = partNumber;
        }
        return true;
    }

    //

    /**
     * 通过校验 crc64 来检查文件是否已经上传过了
     *
     */
    private @Nullable HeadObjectResult headObjectToCheckCRC64() {
        try {
            headObjectRequest  = new HeadObjectRequest(bucket, cosPath);
            HeadObjectResult headObjectResult = cosXmlService.headObject(headObjectRequest);
            String crcValue = getCRCValue(headObjectResult);
            long remoteCrc64 = DigestUtils.getBigIntFromString(crcValue);
            InputStream inputStream = openUploadFileStream();
            long localCrc64 = DigestUtils.getCRC64(inputStream);
            inputStream.close();
            if (remoteCrc64 == localCrc64) {
                return headObjectResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private @Nullable String getCRCValue(HeadObjectResult headObjectResult) {
        List<String> crc64Values = headObjectResult.headers.get("x-cos-hash-crc64ecma");
        if (crc64Values != null && crc64Values.size() == 1) {
            return crc64Values.get(0);
        }
        return null;
    }


    private void multiUploadPart(CosXmlSimpleService cosXmlService){
        //是否已上传完
        boolean isUploadFinished = true;
        // 循环进行
        for(final Map.Entry<Integer, SlicePartStruct> entry : partStructMap.entrySet()){
            final SlicePartStruct slicePartStruct = entry.getValue();
            //是否已经failed了，则就不要在继续了
            if(!slicePartStruct.isAlreadyUpload && !IS_EXIT.get()){
                isUploadFinished = false;

                UploadPartRequest uploadPartRequest = null;
                if(srcPath != null){
                    uploadPartRequest = new UploadPartRequest(bucket, cosPath, slicePartStruct.partNumber, srcPath, slicePartStruct.offset, slicePartStruct.sliceSize,  uploadId);
                }
                if(uri != null){
                    uploadPartRequest = new UploadPartRequest(bucket, cosPath, slicePartStruct.partNumber, uri, slicePartStruct.offset, slicePartStruct.sliceSize, uploadId);
                }
                if(url != null){
                    uploadPartRequest = new UploadPartRequest(bucket, cosPath, slicePartStruct.partNumber, url, slicePartStruct.offset, slicePartStruct.sliceSize,  uploadId);
                }


                if (priorityLow) {
                    uploadPartRequest.setPriorityLow();
                }
                uploadPartRequest.setRegion(region);
                if(url == null) {
                    uploadPartRequest.setNeedMD5(isNeedMd5);
                } else {
                    uploadPartRequest.setNeedMD5(false);
                }
                uploadPartRequest.setRequestHeaders(headers);
                uploadPartRequest.setOnRequestWeightListener(new CosXmlRequest.OnRequestWeightListener() {
                    @Override
                    public int onWeight() {
                        return weightStrategy.getWeight(ALREADY_SEND_DATA_LEN.get());
                    }
                });

                if(onSignatureListener != null){
                    uploadPartRequest.setSign(onSignatureListener.onGetSign(uploadPartRequest));
                }

                getHttpMetrics(uploadPartRequest, "UploadPartRequest");

                uploadPartRequestLongMap.put(uploadPartRequest, 0L);
                final UploadPartRequest finalUploadPartRequest = uploadPartRequest;
                uploadPartRequest.setProgressListener(new CosXmlProgressListener() {
                    @Override
                    public void onProgress(long complete, long target) {
                        if(IS_EXIT.get())return;//已经上报失败了
                        try {
                            long dataLen = ALREADY_SEND_DATA_LEN.addAndGet(complete - uploadPartRequestLongMap.get(finalUploadPartRequest));
                            uploadPartRequestLongMap.put(finalUploadPartRequest, complete);
                            dispatchProgressChange(dataLen, fileLength);
                        }catch (Exception e){
                            //cause by cancel or pause
                        }
                    }
                });
                final UploadPartRequest finalUploadPartRequest1 = uploadPartRequest;
                cosXmlService.uploadPartAsync(uploadPartRequest, new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        if(request != finalUploadPartRequest1){
                            return;
                        }
                        httpTaskMetrics.merge(request.getMetrics());
                        if(IS_EXIT.get())return;
                        slicePartStruct.eTag = ((UploadPartResult)result).eTag;
                        slicePartStruct.isAlreadyUpload = true;
                        synchronized (SYNC_UPLOAD_PART){
                            UPLOAD_PART_COUNT.decrementAndGet();
                            if(UPLOAD_PART_COUNT.get() == 0){
                                multiUploadsStateListenerHandler.onUploadParts();
                            }
                        }
                    }

                    @Override
                    public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                        if(request != finalUploadPartRequest1){
                            return;
                        }
                        if(IS_EXIT.get())return;//已经上报失败了
                        IS_EXIT.set(true);
                        multiUploadsStateListenerHandler.onFailed(request, exception, serviceException);
                    }
                });
            }
        }
        if(isUploadFinished && !IS_EXIT.get()){
            dispatchProgressChange(fileLength, fileLength);
            multiUploadsStateListenerHandler.onUploadParts();
        }
    }

    private void completeMultiUpload(CosXmlSimpleService cosXmlService){

        sendingCompleteRequest.set(true);

        completeMultiUploadRequest = new CompleteMultiUploadRequest(bucket, cosPath,
                uploadId, null);
        completeMultiUploadRequest.setRegion(region);
        for(Map.Entry<Integer, SlicePartStruct> entry : partStructMap.entrySet()){
            SlicePartStruct slicePartStruct = entry.getValue();
            completeMultiUploadRequest.setPartNumberAndETag(slicePartStruct.partNumber, slicePartStruct.eTag);
        }

        completeMultiUploadRequest.setNeedMD5(isNeedMd5);
        completeMultiUploadRequest.setRequestHeaders(getCustomCompleteHeaders(headers));

        if(onSignatureListener != null){
            completeMultiUploadRequest.setSign(onSignatureListener.onGetSign(completeMultiUploadRequest));
        }

        getHttpMetrics(completeMultiUploadRequest, "CompleteMultiUploadRequest");

        cosXmlService.completeMultiUploadAsync(completeMultiUploadRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if(request != completeMultiUploadRequest){
                    return;
                }
                sendingCompleteRequest.set(false);
                onTransferComplete(request, result);
            }

            @Override
            public void onFail(final CosXmlRequest request, final CosXmlClientException clientException, final CosXmlServiceException serviceException) {
                if(request != completeMultiUploadRequest){
                    return;
                }

                // 如果是 404，检查上传是否已经成功
                // 由于有重试机制，对于首次超时，但实际后台实际成功后，sdk 重试时，会报 NoSuchUpload 错误，可以在
                if (serviceException != null && "NoSuchUpload".equals(serviceException.getErrorCode())) {

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            HeadObjectResult headObjectResult = headObjectToCheckCRC64();
                            if (headObjectResult != null) { // 上传成功
                                onTransferComplete(request, headObjectResult);
                            } else {
                                encounterError(request, clientException, serviceException);
                            }
                            sendingCompleteRequest.set(false);
                        }
                    });

                } else {
                    encounterError(request, clientException, serviceException);
                    sendingCompleteRequest.set(false);
                }
            }
        });
    }


    private void onTransferComplete(CosXmlRequest request, CosXmlResult result) {

        request.attachMetrics(httpTaskMetrics);
        if(IS_EXIT.get())return;
        IS_EXIT.set(true);
        BeaconService.getInstance().reportUploadTaskSuccess(request);
        multiUploadsStateListenerHandler.onCompleted(request, result);
    }


    @Override
    protected void internalCompleted() {
        clear();
    }

    @Override
    protected void internalFailed() {
        cancelAllRequest(cosXmlService);
    }

    @Override
    protected void internalPause() {

        CosXmlRequest request = buildCOSXMLTaskRequest();
        request.attachMetrics(httpTaskMetrics);
        BeaconService.getInstance().reportUploadTaskSuccess(request);
        cancelAllRequest(cosXmlService);
    }

    /**
     * 如果已经发送了 CompleteMultiUpload 请求，则不允许暂停
     *
     * @return 是否暂停成功
     */
    public boolean pauseSafely() {

        if (sendingCompleteRequest.get()) {
            return false;
        }

        pause();
        return true;
    }


    @Override
    protected void internalCancel() {
        cancelAllRequest(cosXmlService);
        if(isSliceUpload)abortMultiUpload(cosXmlService);
        clear();
    }

    @Override
    protected void internalResume() {
        taskState = TransferState.WAITING;
        IS_EXIT.set(false); //初始化
        upload();
    }

    @Override
    protected void encounterError(@Nullable CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
        if(IS_EXIT.get())return;
        IS_EXIT.set(true);
        multiUploadsStateListenerHandler.onFailed(request != null ? request : buildCOSXMLTaskRequest(),
                clientException, serviceException);
    }


    void cancelAllRequest(CosXmlSimpleService cosXmlService){

        HeadObjectRequest tempHeadObjectRequest = headObjectRequest;
        if (tempHeadObjectRequest != null) {
            cosXmlService.cancel(tempHeadObjectRequest);
        }

        PutObjectRequest tempPutObjectRequest = putObjectRequest;
        if(tempPutObjectRequest != null){
            cosXmlService.cancel(tempPutObjectRequest);
        }
        InitMultipartUploadRequest tempInitMultipartUploadRequest = initMultipartUploadRequest;
        if(tempInitMultipartUploadRequest != null){
            cosXmlService.cancel(tempInitMultipartUploadRequest);
        }
        ListPartsRequest tempListPartsRequest = listPartsRequest;
        if(tempListPartsRequest != null){
            cosXmlService.cancel(tempListPartsRequest);
        }

        if(uploadPartRequestLongMap != null){
            Set<UploadPartRequest> set = uploadPartRequestLongMap.keySet();
            Iterator<UploadPartRequest> iterator = set.iterator();
            while(iterator.hasNext()){
                cosXmlService.cancel(iterator.next());
            }
        }

        CompleteMultiUploadRequest tempCompleteMultiUploadRequest = completeMultiUploadRequest;
        if(tempCompleteMultiUploadRequest != null){
            cosXmlService.cancel(tempCompleteMultiUploadRequest);
        }

    }

    private void abortMultiUpload(CosXmlSimpleService cosXmlService){
        if(uploadId == null) return;
        AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(bucket, cosPath,
                uploadId);
        abortMultiUploadRequest.setRegion(region);

        if(onSignatureListener != null){
            abortMultiUploadRequest.setSign(onSignatureListener.onGetSign(abortMultiUploadRequest));
        }

        getHttpMetrics(abortMultiUploadRequest, "AbortMultiUploadRequest");

        cosXmlService.abortMultiUploadAsync(abortMultiUploadRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // abort success
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                // abort failed
            }
        });
    }

    private void clear(){
        if(uploadPartRequestLongMap != null){
            uploadPartRequestLongMap.clear();
        }
        if(partStructMap != null){
            partStructMap.clear();
        }
    }

    @Override
    protected CosXmlRequest buildCOSXMLTaskRequest() {
        COSXMLUploadTaskRequest cosxmlUploadTaskRequest = new COSXMLUploadTaskRequest(region, bucket,
                cosPath, srcPath, headers, queries);
        return cosxmlUploadTaskRequest;
    }

    @Override
    protected CosXmlResult buildCOSXMLTaskResult(CosXmlResult sourceResult) {
        COSXMLUploadTaskResult cosxmlUploadTaskResult = new COSXMLUploadTaskResult();
        if(sourceResult != null && sourceResult instanceof PutObjectResult){
            PutObjectResult putObjectResult = (PutObjectResult) sourceResult;
            cosxmlUploadTaskResult.httpCode = putObjectResult.httpCode;
            cosxmlUploadTaskResult.httpMessage = putObjectResult.httpMessage;
            cosxmlUploadTaskResult.headers = putObjectResult.headers;
            cosxmlUploadTaskResult.eTag = putObjectResult.eTag;
            cosxmlUploadTaskResult.accessUrl = putObjectResult.accessUrl;
            cosxmlUploadTaskResult.picUploadResult = putObjectResult.picUploadResult();
        }else if(sourceResult != null && sourceResult instanceof CompleteMultiUploadResult){
            CompleteMultiUploadResult completeMultiUploadResult = (CompleteMultiUploadResult) sourceResult;
            cosxmlUploadTaskResult.httpCode = completeMultiUploadResult.httpCode;
            cosxmlUploadTaskResult.httpMessage = completeMultiUploadResult.httpMessage;
            cosxmlUploadTaskResult.headers = completeMultiUploadResult.headers;
            cosxmlUploadTaskResult.accessUrl = completeMultiUploadResult.accessUrl;
            if(completeMultiUploadResult.completeMultipartUpload != null){
                cosxmlUploadTaskResult.eTag = completeMultiUploadResult.completeMultipartUpload.eTag;
                PicUploadResult picUploadResult = new PicUploadResult();
                picUploadResult.originalInfo = completeMultiUploadResult.completeMultipartUpload.getOriginInfo();
                picUploadResult.processResults = completeMultiUploadResult.completeMultipartUpload.processResults;
                cosxmlUploadTaskResult.picUploadResult = picUploadResult;
            }
        }
        return cosxmlUploadTaskResult;
    }

    /**
     * 获取分片uploadId属性
     * @return 分片uploadId属性
     */
    public String getUploadId(){
        return uploadId;
    }

    /**
     * 设置分片uploadId
     * @param uploadId 分片uploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * 初始化上传任务
     * @param fileOffset 源文件的偏移量
     * @param uploadSize 需要上传的大小
     * @param startNumber upload part 的起始 number
     */
    private void initSlicePart(long fileOffset, long uploadSize, int startNumber){
        int count = (int) (uploadSize / sliceSize);

        for(int numberOffset = 0; numberOffset < count; numberOffset++){
            SlicePartStruct slicePartStruct = new SlicePartStruct();
            slicePartStruct.isAlreadyUpload = false;
            slicePartStruct.partNumber = startNumber + numberOffset;
            slicePartStruct.offset = fileOffset + numberOffset * sliceSize;
            slicePartStruct.sliceSize = sliceSize;
            partStructMap.put(slicePartStruct.partNumber, slicePartStruct);
        }

        if (uploadSize % sliceSize != 0) { // 还有部分不足一个分片
            SlicePartStruct slicePartStruct = new SlicePartStruct();
            slicePartStruct.isAlreadyUpload = false;
            slicePartStruct.partNumber = startNumber + count;
            slicePartStruct.offset = fileOffset + count * sliceSize;
            slicePartStruct.sliceSize = fileOffset + uploadSize - slicePartStruct.offset;
            partStructMap.put(slicePartStruct.partNumber, slicePartStruct);
            count++;
        }

        UPLOAD_PART_COUNT.set(startNumber + count - 1);
        if(IS_EXIT.get())return;
    }

    /**
     * 需要做如下判断，已上传的分片大小和请求设置分片大小是否一致
     * 1）若是一致，则可以乱序续传
     * 2）若不一致，则续传只能从开始partNumber = 1连续已上传的分片开始
     * 如何判断是否一致
     * 1）
     * @param listPartsResult
     */
    private void updateSlicePart(ListPartsResult listPartsResult){
        if(listPartsResult != null && listPartsResult.listParts != null){
            List<ListParts.Part> parts = listPartsResult.listParts.parts;
            if(parts != null && parts.size() > 0){
                if(isFixSliceSize(parts)){
                    for(ListParts.Part part : parts){
                        if(partStructMap.containsKey(Integer.valueOf(part.partNumber))){
                            SlicePartStruct slicePartStruct = partStructMap.get(Integer.valueOf(part.partNumber));
                            slicePartStruct.isAlreadyUpload = true;
                            slicePartStruct.eTag = part.eTag;
                            UPLOAD_PART_COUNT.decrementAndGet();
                            ALREADY_SEND_DATA_LEN.addAndGet(Long.parseLong(part.size));
                        }
                    }
                }else {
                    //不支持，则只能从partNumber = 1开始，获取连续块
                    //排序已上传块
                    Collections.sort(parts, new Comparator<ListParts.Part>() {
                        @Override
                        public int compare(ListParts.Part a, ListParts.Part b) {
                            int aNumb = Integer.valueOf(a.partNumber);
                            int bNumb = Integer.valueOf(b.partNumber);
                            if(aNumb > bNumb) return 1;
                            if(aNumb < bNumb) return -1;
                            return 0;
                        }
                    });
                    //只取连续块，且从partNumber =1开始
                    int index = getIndexOfParts(parts);
                    if(index < 0){
                        return;
                    }
                    partStructMap.clear();
                    long completed = 0L;
                    for(int i = 0; i <= index; i ++){
                        ListParts.Part part = parts.get(i);
                        SlicePartStruct slicePartStruct = new SlicePartStruct();
                        slicePartStruct.partNumber = i + 1;
                        slicePartStruct.offset = completed;
                        slicePartStruct.sliceSize = Long.parseLong(part.size);
                        slicePartStruct.eTag = part.eTag;
                        slicePartStruct.isAlreadyUpload = true;
                        completed += slicePartStruct.sliceSize;
                        partStructMap.put(i + 1, slicePartStruct);
                    }
                    //重新计算剩下的分片
                    ALREADY_SEND_DATA_LEN.addAndGet(completed);
                    initSlicePart(completed, fileLength - completed, index + 2);
                    for(int i = 0; i <= index; i ++){
                        UPLOAD_PART_COUNT.decrementAndGet();
                    }
                }
            }
        }
    }

    private boolean isFixSliceSize(List<ListParts.Part> parts){
        boolean isTrue = true;
        for(ListParts.Part part : parts){
            if(partStructMap.containsKey(Integer.valueOf(part.partNumber))){
                SlicePartStruct slicePartStruct = partStructMap.get(Integer.valueOf(part.partNumber));
                if(slicePartStruct.sliceSize == Long.valueOf(part.size)) continue;
                else {
                    isTrue = false;
                    break;
                }
            }
        }
        return isTrue;
    }

    private int getIndexOfParts(List<ListParts.Part> parts){
        int index = -1;
        int currentPartNumber = 1;
        ListParts.Part firstPart = parts.get(0);
        if(Integer.valueOf(firstPart.partNumber) != 1){
            return index;
        }
        index = 0;
        ListParts.Part tmp;
        for(int i = 1, size = parts.size(); i < size; i ++){
            tmp = parts.get(i);
            if(Integer.valueOf(tmp.partNumber) != currentPartNumber + 1){
                break;
            }else {
                index = i;
                currentPartNumber = Integer.valueOf(tmp.partNumber);
            }
        }
        return index;
    }

    protected void startUpload() {

        //bytes or inputStream using simple upload method
        if(bytes != null || inputStream != null){
            simpleUpload(cosXmlService);
            return;
        }

        if(fileLength < multiUploadSizeDivision || forceSimpleUpload){
            simpleUpload(cosXmlService);
        }else {
            //根据size计算分块大小 分块数量不能超过10000
            long defaultPartSize = sliceSize;
            double partCount = Math.ceil(fileLength / (double)defaultPartSize);
            if(partCount > 10000){
                sliceSize = (long) Math.ceil(fileLength / 10000.0);
            } else {
                sliceSize = defaultPartSize;
            }

            isSliceUpload = true;
            UPLOAD_PART_COUNT = new AtomicInteger(0); //用于计算分片数
            ALREADY_SEND_DATA_LEN = new AtomicLong(0); //分片上传进度计数
            partStructMap = new LinkedHashMap<>(); //必须有序
            uploadPartRequestLongMap = new LinkedHashMap<>();
            multiUpload(cosXmlService);
        }
    }

    private void reTrans() {

        uploadId = null;
        startUpload();
    }

    @Override
    public void resume() {
        if(inputStream != null){
            if(IS_EXIT.get())return;
            IS_EXIT.set(true);
            multiUploadsStateListenerHandler.onFailed(buildCOSXMLTaskRequest(),
                    new CosXmlClientException(ClientErrorCode.SINK_SOURCE_NOT_FOUND.getCode(), "inputStream closed"), null);
            return;
        }
        super.resume();
    }

    private static class SlicePartStruct{
        public int partNumber;
        public boolean isAlreadyUpload;
        public long offset;
        public long sliceSize;
        public String eTag;
    }

    private static class WeightStrategy {

        private final long DEFAULT_WEIGHT_NORMAL_SIZE = 80 * 1024 * 1024;
        private final long DEFAULT_WEIGHT_HIGH_SIZE   = 150 * 1024 * 1024;

        private long normalSize = DEFAULT_WEIGHT_NORMAL_SIZE;
        private long highSize = DEFAULT_WEIGHT_HIGH_SIZE;

        private WeightStrategy() {

        }

        private int getWeight(long size) {

            if (size > highSize) {
                return QCloudTask.WEIGHT_HIGH;
            } else if (size > normalSize) {
                return QCloudTask.WEIGHT_NORMAL;
            } else {
                return QCloudTask.WEIGHT_LOW;
            }
        }
    }

    public boolean getSendingCompleteRequest() {
        return sendingCompleteRequest.get();
    }

    private interface MultiUploadsStateListener{
        void onInit();
        void onListParts();
        void onUploadParts();
        void onCompleted(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult);
        void onFailed(CosXmlRequest cosXmlRequest, CosXmlClientException exception, CosXmlServiceException serviceException);
    }

    /**
     * 上传传输任务的请求
     */
    public static class COSXMLUploadTaskRequest extends PutObjectRequest{
        protected COSXMLUploadTaskRequest(String region, String bucket, String cosPath, String srcPath, Map<String, List<String>> headers,
                                          Map<String, String> queryStr) {
            super(bucket, cosPath, srcPath);
            this.setRegion(region);
            this.setRequestHeaders(headers);
            this.setQueryParameters(queryStr);
        }
    }

    /**
     * 上传传输任务的返回结果
     */
    public static class COSXMLUploadTaskResult extends CosXmlResult {
        protected COSXMLUploadTaskResult(){}
        public String eTag;
        public PicUploadResult picUploadResult;
    }

    private Map<String, List<String>> getCustomCompleteHeaders(@Nullable Map<String, List<String>> customHeaders) {
        if (customHeaders == null) {
            return new HashMap<>();
        }
        Map<String, List<String>> headers = new HashMap<>(customHeaders);
        headers.remove(HttpConstants.Header.CONTENT_TYPE);
        return headers;
    }
}
