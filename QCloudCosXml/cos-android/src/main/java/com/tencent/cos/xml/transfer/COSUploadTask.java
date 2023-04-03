package com.tencent.cos.xml.transfer;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.crypto.COSDirect;
import com.tencent.cos.xml.crypto.MultipartUploadCryptoContext;
import com.tencent.cos.xml.crypto.ObjectMetadata;
import com.tencent.cos.xml.crypto.ResettableInputStream;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsRequest;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsResult;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.ListPartsResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.tag.ListMultipartUploads;
import com.tencent.cos.xml.model.tag.ListParts;
import com.tencent.cos.xml.model.tag.UrlUploadPolicy;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.util.ContextHolder;
import com.tencent.qcloud.core.util.QCloudUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import bolts.CancellationTokenSource;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * 支持上传数据源，优先级为 filePath 大于 Uri
 *
 * 1. filePath：支持分片上传、客户端加密上传
 * 2. Uri：支持分片上传、客户端加密上传
 *
 *
 * 3. byte 数组：不支持分片上传
 * 4. String 字符串：不支持分片上传
 * 5. InputStream：暂不支持，会预先将数据写到一个本地文件缓存，然后以文件上传的方式进行上传，否则：
 *                 无法支持分片上传（无法预知数据流的长度来判断是否需要分片，并且分片上传需要预先读取数据来校验分块）
 *                 无法支持加密上传（加密上传需要预先读取数据计算原始的 md5 值）
 *                一些重试的逻辑也无法支持
 *
 * <p>
 * Created by rickenwang on 2021/6/30.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class COSUploadTask extends COSTransferTask {

    public static final String TAG = "QCloudUpload";

    // 上传并发数
    private static final int UPLOAD_CONCURRENT = 2;

    private static ThreadPoolExecutor uploadTaskExecutor = new ThreadPoolExecutor(UPLOAD_CONCURRENT, UPLOAD_CONCURRENT, 5L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE),
            new TaskThreadFactory(TAG + "-", 8));

    private PutObjectRequest mPutObjectRequest;

    private BaseUploadTask uploadTask;

    private long multipartUploadThreshold = 2 * 1024 * 1024;

    // 分片大小
    private long mMaxPartSize = 1024 * 1024;
    private boolean verifyCRC64 = true;

    private boolean mForceSimpleUpload = false;

    /**
     * 上传的长度
     */
    private long uploadLength = -1;

    public COSUploadTask(COSDirect cosDirect, PutObjectRequest putObjectRequest) {
        super(cosDirect, putObjectRequest);
        mPutObjectRequest = putObjectRequest;
    }

    @Override
    protected String tag() {
        return TAG;
    }

    @Override
    protected Executor executor() {
        return uploadTaskExecutor;
    }

    @Override
    public void pause() {
        super.pause();

        if (uploadTask != null) {
            uploadTask.cancel();
        }
    }

    @Override
    public void cancel() {
        super.cancel();

        if (uploadTask != null) {
            uploadTask.cancel();
        }
        
        if (uploadTask instanceof MultipartUploadTask) {
            ((MultipartUploadTask) uploadTask).abort();
        }
    }

    /**
     * 设置分片上传时的分片大小
     *
     * @param partSize 分片大小
     */
    void setPartSize(long partSize) {
        mMaxPartSize = partSize;
    }

    /**
     * 分片上传时是否整体校验 crc64
     *
     * @param verifyCRC64 是否校验 crc64
     */
    void setVerifyCRC64(boolean verifyCRC64) {
        this.verifyCRC64 = verifyCRC64;
    }

    /**
     * 设置分片阈值
     *
     * @param threshold
     */
    void setSliceSizeThreshold(long threshold) {
        multipartUploadThreshold = threshold;
    }

    /**
     * 是否强制简单上传
     *
     * @param forceSimpleUpload
     */
    void forceSimpleUpload(boolean forceSimpleUpload) {
        mForceSimpleUpload = forceSimpleUpload;
    }


    @Override
    protected void checking() throws CosXmlClientException {
        super.checking();
        mPutObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                onTransferProgressChange(complete, getUploadLength());
            }
        });

        byte[] bytes = mPutObjectRequest.getData();
        InputStream inputStream = mPutObjectRequest.getInputStream();
        String filePath = mPutObjectRequest.getSrcPath();
        Uri uri = mPutObjectRequest.getUri();
        URL url = mPutObjectRequest.getUrl();
        String str = mPutObjectRequest.getStrData();
        UrlUploadPolicy urlUploadPolicy = mPutObjectRequest.getUrlUploadPolicy();

        if (TextUtils.isEmpty(filePath) && uri == null && bytes == null && str == null) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),
                    "Only support upload file path, uri, bytes array and string");
        }

        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), filePath + " is not exist.");
            } else if (file.isDirectory()) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), filePath + " is directory.");
            } else if (!file.canRead()) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), " read " + filePath + " failed, please check permission to read this file.");
            }
            uploadLength = file.length();
        } else if (uri != null) {
            Context context = ContextHolder.getAppContext();
            if (context != null) {
                if (!QCloudUtils.doesUriFileExist(uri, context.getContentResolver())) {
                    throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "uri " + uri + " is not exist");
                }
                uploadLength = QCloudUtils.getUriContentLength(uri, context.getContentResolver());
            }
        } else if (bytes != null) {
            uploadLength = bytes.length;
        } else if (str != null) {
            uploadLength = str.getBytes().length;
        }

        loggerInfo(TAG, taskId, "checkout upload length=%d", uploadLength);
    }
    
    @Override
    protected CosXmlResult execute() throws Exception {

        if (shouldMultipartUpload()) {
            loggerInfo(TAG, taskId, "start upload with multipart upload");
            MultipartUploadTask multipartUploadTask = new MultipartUploadTask(cosDirect, mPutObjectRequest, mTransferTaskCts,
                    getUploadLength());
            multipartUploadTask.mMaxPartSize = mMaxPartSize;
            multipartUploadTask.verifyCRC64 = verifyCRC64;
            uploadTask = multipartUploadTask;
        } else {
            loggerInfo(TAG, taskId, "start upload with simple upload.", key);
            uploadTask = new SimpleUploadTask(cosDirect, mPutObjectRequest, mTransferTaskCts, getUploadLength());
        }
        uploadTask.mTransferMetrics = transferTaskMetrics;
        uploadTask.taskId = taskId;

        CosXmlResult cosxmlResult = uploadTask.upload(mPutObjectRequest);
        if (cosxmlResult instanceof CompleteMultiUploadResult) {
            return buildPutObjectResult((CompleteMultiUploadResult) cosxmlResult);
        } else {
            return cosxmlResult;
        }
    }

    private PutObjectResult buildPutObjectResult(CompleteMultiUploadResult completeMultiUploadResult) {
        PutObjectResult putObjectResult = new PutObjectResult();
        putObjectResult.httpCode = completeMultiUploadResult.httpCode;
        putObjectResult.httpMessage = completeMultiUploadResult.httpMessage;
        putObjectResult.headers = completeMultiUploadResult.headers;
        putObjectResult.accessUrl = completeMultiUploadResult.accessUrl;
        putObjectResult.eTag = completeMultiUploadResult.completeMultipartUpload.eTag;
        putObjectResult.picUploadResult = completeMultiUploadResult.completeMultipartUpload.getPicUploadResult();
        return putObjectResult;
    }

    private boolean shouldMultipartUpload() {
        return !mForceSimpleUpload && isMultipartUploadRequest() && isMultipartUploadLength();
    }

    private boolean isMultipartUploadLength() {
        return uploadLength >= multipartUploadThreshold;
    }
    
    /**
     * 通过上传数据源的类型来判断是否可以分片上传
     * 
     * 只有文件和 uri 支持分片上传
     */
    private boolean isMultipartUploadRequest() {
        return mPutObjectRequest.getSrcPath() != null || mPutObjectRequest.getUri() != null;
    }

    private long getUploadLength() {
        return uploadLength;
    }

    static final class UploadPart {

        String etag;
        // 从 1 开始
        int number;

        long offset;
        long size;

        public UploadPart(String etag, int number, long offset, long size) {
            this.etag = etag;
            this.number = number;
            this.offset = offset;
            this.size = size;
        }
    }

    abstract static class BaseUploadTask implements Runnable {

        protected bolts.TaskCompletionSource<CosXmlResult> tcs;
        protected COSDirect cosDirect;
        protected PutObjectRequest putObjectRequest;
        protected CancellationTokenSource mTransferTaskCts;
        protected String taskId;
        protected TransferTaskMetrics mTransferMetrics;
        protected long totalUploadSize;

        BaseUploadTask(COSDirect apiDirect, PutObjectRequest putObjectRequest,
                       CancellationTokenSource transferTaskCts, long totalUploadSize) {

            this.cosDirect = apiDirect;
            this.putObjectRequest = putObjectRequest;
            this.tcs = new TaskCompletionSource<>();
            this.mTransferTaskCts = transferTaskCts;
            this.totalUploadSize = totalUploadSize;
        }

        @Override
        public void run() {

            if (mTransferTaskCts.isCancellationRequested()) {
                tcs.setCancelled();
                return;
            }

            try {
                tcs.setResult(upload(putObjectRequest));
            } catch (Exception e) {
                tcs.setError(e);
            }
        }

        public Task<CosXmlResult> getTask() {
            return tcs.getTask();
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        abstract public void cancel();

        protected abstract CosXmlResult upload(PutObjectRequest putObjectRequest) throws Exception;
    }

    private static class SimpleUploadTask extends BaseUploadTask {


        SimpleUploadTask(COSDirect apiDirect, PutObjectRequest putObjectRequest,
                         CancellationTokenSource cts, long totalUploadSize) {
            super(apiDirect, putObjectRequest, cts, totalUploadSize);

        }

        @Override
        public void cancel() {
            cosDirect.cancel(putObjectRequest);
        }

        @Override
        protected PutObjectResult upload(PutObjectRequest putObjectRequest) throws Exception {
            HttpTaskMetrics httpTaskMetrics = new HttpTaskMetrics();
            mTransferMetrics.size = totalUploadSize;
            putObjectRequest.attachMetrics(httpTaskMetrics);
            PutObjectResult putObjectResult = cosDirect.putObject(putObjectRequest);
            mTransferMetrics.connectAddress = httpTaskMetrics.getConnectAddress();
            loggerInfo(TAG, taskId, "complete upload task");
            return putObjectResult;
        }

    }


    private static class MultipartUploadTask extends BaseUploadTask {

        private String bucket, region, key;

        private TreeSet<UploadPart> uploadParts;

        private BaseUploadPartsTask uploadPartsTask;

        // 用于上传的 uploadId
        private String mUploadId;

        private volatile ListMultiUploadsRequest listMultiUploadsRequest;
        private volatile InitMultipartUploadRequest initMultipartUploadRequest;
        private volatile ListPartsRequest listPartsRequest;
        private volatile CompleteMultiUploadRequest completeMultiUploadRequest;

        private CosXmlSimpleService cosService;

        private MultipartUploadCryptoContext mCryptoContext;

        private long mMaxPartSize = 1024 * 1024;
        private boolean verifyCRC64 = true;


        public MultipartUploadTask(COSDirect apiDirect, PutObjectRequest putObjectRequest,
                                   CancellationTokenSource cts, long totalUploadSize) {
            super(apiDirect, putObjectRequest, cts, totalUploadSize);
            this.putObjectRequest = putObjectRequest;
            this.tcs = new TaskCompletionSource<>();
            bucket = putObjectRequest.getBucket();
            region = putObjectRequest.getRegion();
            key = putObjectRequest.getCosPath();
            uploadParts = new TreeSet<>(new Comparator<UploadPart>() {
                @Override
                public int compare(UploadPart o1, UploadPart o2) {
                    int x = o1.number;
                    int y = o2.number;
                    return (x < y) ? -1 : ((x == y) ? 0 : 1);
                }
            });
            cosService = cosDirect.getCosService();
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        @Override
        protected CosXmlResult upload(PutObjectRequest putObjectRequest) throws Exception {

            if (!cosDirect.isTransferSecurely()) {

                // 1. 查询该对象曾经的 uploadId，用于续传
                checkoutManualCanceled();
                List<String> uploadIds = listObjectUploadIds();

                // 2. 查找可以续传的 uploadId
                checkoutManualCanceled();
                for (String uploadId : uploadIds) {
                    if (checkoutUploadId(uploadId)) {
                        mUploadId = uploadId;
                        break;
                    }
                }
            }

            // 3. 如果没有可以续传的 uploadId，则生成一个新的
            checkoutManualCanceled();
            if (TextUtils.isEmpty(mUploadId)) {
                mUploadId = initUploadId();
            }

            // 4. 上传剩余分片
            checkoutManualCanceled();
            uploadParts(mUploadId);

            // 4. 完成分片上传
            checkoutManualCanceled();
            CosXmlResult cosXmlResult = completeMultipartUpload(mUploadId);

            // 5. 最后进行校验文件
            if (verifyCRC64) {
                checkoutManualCanceled();
                crc64Verify(cosXmlResult.getHeader("x-cos-hash-crc64ecma"));
            }

            return cosXmlResult;
        }

        @Override
        public void cancel() {

            if (listMultiUploadsRequest != null) {
                cosDirect.cancel(listMultiUploadsRequest);
            }
            if (initMultipartUploadRequest != null) {
                cosDirect.cancel(initMultipartUploadRequest);
            }
            if (listPartsRequest != null) {
                cosDirect.cancel(listPartsRequest);
            }
            if (completeMultiUploadRequest != null) {
                cosDirect.cancel(completeMultiUploadRequest);
            }
            if (uploadPartsTask != null) {
                uploadPartsTask.cancel();
            }
        }

        private void abort() {
            if (!TextUtils.isEmpty(mUploadId)) {
                AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(bucket, key, mUploadId);
                abortMultiUploadRequest.setRegion(region);
                cosService.abortMultiUploadAsync(abortMultiUploadRequest, new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {}
                    @Override
                    public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {}
                });
            }
        }

        private boolean isParallelUpload() {
            return !cosDirect.isTransferSecurely();
        }

        private void checkoutManualCanceled() throws CosXmlClientException {
            if (mTransferTaskCts.isCancellationRequested()) {
                throw CosXmlClientException.manualCancelException();
            }
        }

        // x-cos-storage-class
        private String getStorageClass() {

            List<String> storageClassList = putObjectRequest.getRequestHeaders()
                    .get("x-cos-storage-class");
            if (storageClassList == null || storageClassList.isEmpty()) {
                return "STANDARD";
            } else {
                return storageClassList.get(0);
            }
        }

        // 根据 key 拿到所有备选的 uploadId，需要对比存储类型是否一致，可能需要发多次请求
        private List<String> listObjectUploadIds() throws CosXmlClientException, CosXmlServiceException {
            
            String prefix = key.startsWith("/") ? key.substring(1) : key;
            HttpTaskMetrics httpTaskMetrics = new HttpTaskMetrics();
            listMultiUploadsRequest = new ListMultiUploadsRequest(bucket);
            listMultiUploadsRequest.setPrefix(prefix);
            listMultiUploadsRequest.setRegion(region);
            // 拿到 connect address 信息
            listMultiUploadsRequest.attachMetrics(httpTaskMetrics);
            List<String> uploadIds = new LinkedList<>();
            ListMultiUploadsResult listMultiUploadsResult = cosService.listMultiUploads(listMultiUploadsRequest);
            mTransferMetrics.connectAddress = httpTaskMetrics.getConnectAddress();
            ListMultipartUploads listMultipartUploads = listMultiUploadsResult.listMultipartUploads;
            if (listMultipartUploads != null) {
                List<ListMultipartUploads.Upload> uploads = listMultipartUploads.uploads;
                loggerInfo(TAG, taskId, "find %d uploadIds with prefix %s", uploads.size(), prefix);
                if (!uploads.isEmpty()) {
                    // 找最近的 uploadId
                    Collections.sort(uploads, new Comparator<ListMultipartUploads.Upload>() {
                        @Override
                        public int compare(ListMultipartUploads.Upload o1, ListMultipartUploads.Upload o2) {
                            Long y = parseInitiatedDate(o1.initiated);
                            Long x = parseInitiatedDate(o2.initiated);
                            return (x < y) ? -1 : ((x == y) ? 0 : 1);
                        }
                    });
                    
                    for (ListMultipartUploads.Upload upload : uploads) {
                        if (upload.key.equals(prefix) && upload.storageClass.equals(getStorageClass())) {
                            uploadIds.add(upload.uploadID);
                        }
                    }
                }
            }
            loggerInfo(TAG, taskId, "find %d plan uploadId",  uploadIds.size());
            return uploadIds;
        }

        // 生成一个新的 uploadId 用于上传
        private String initUploadId() throws Exception {
            
            initMultipartUploadRequest = new InitMultipartUploadRequest(bucket, key);
            initMultipartUploadRequest.setRegion(region);
            initMultipartUploadRequest.setRequestHeaders(getInitHeaders(putObjectRequest));
            HttpTaskMetrics httpTaskMetrics = new HttpTaskMetrics();
            initMultipartUploadRequest.attachMetrics(httpTaskMetrics);

            // 加密上传时需要带上 unencrypted-content-length 和 unencrypted-content-md5
            if (cosDirect.isTransferSecurely()) {
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(totalUploadSize);
                objectMetadata.setContentMD5(DigestUtils.getCOSMd5(openUnencryptedInputStream(), 0, totalUploadSize));
                initMultipartUploadRequest.setMetadata(objectMetadata);
            }
            InitMultipartUploadResult initMultipartUploadResult = cosDirect.initMultipartUpload(initMultipartUploadRequest);
            mTransferMetrics.connectAddress = httpTaskMetrics.getConnectAddress();
            String uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;
            if (cosDirect.isTransferSecurely()) {
                mCryptoContext = cosDirect.getCryptoModule().getCryptoContext(uploadId);
            }
            loggerInfo(TAG, taskId, "create a new uploadId %s", uploadId);
            return uploadId;
        }

        private Map<String, List<String>> getInitHeaders(PutObjectRequest putObjectRequest) {
            Map<String, List<String>> customHeaders = putObjectRequest.getRequestHeaders();
            if (customHeaders == null) {
                return new HashMap<>();
            }
            return new HashMap<>(customHeaders);
        }

        private Map<String, List<String>> getCompleteHeaders(PutObjectRequest putObjectRequest) {
            Map<String, List<String>> customHeaders = putObjectRequest.getRequestHeaders();
            if (customHeaders == null) {
                return new HashMap<>();
            }
            Map<String, List<String>> headers = new HashMap<>(customHeaders);

            // complete 的 content-type 不能用户自定义
            headers.remove(HttpConstants.Header.CONTENT_TYPE);
            return headers;
        }

        // 1. 根据 uploadId 获取已经上传的分片
        // 2. 校验分片的有效性，尽可能的去复用分片
        // 3. 生成 uploadParts，用于后续上传
        private boolean checkoutUploadId(String uploadId)
                throws CosXmlClientException, CosXmlServiceException {

            uploadParts.clear();

            if (cosDirect.isTransferSecurely()) {
                if (cosDirect.getCryptoModule() == null) {
                    throw CosXmlClientException.internalException("cannot client size encryption, crypto module is null");
                }
                if (!cosDirect.getCryptoModule().hasMultipartUploadContext(uploadId)) {
                    return false;
                }
            }

            listPartsRequest = new ListPartsRequest(bucket, key, uploadId);
            listPartsRequest.setRegion(region);
            ListPartsResult listPartsResult = cosService.listParts(listPartsRequest);
            List<ListParts.Part> parts = listPartsResult.listParts.parts;
            Collections.sort(parts, new Comparator<ListParts.Part>() {
                @Override
                public int compare(ListParts.Part o1, ListParts.Part o2) {
                    int x = Integer.parseInt(o1.partNumber);
                    int y = Integer.parseInt(o2.partNumber);
                    return (x < y) ? -1 : ((x == y) ? 0 : 1);
                }
            });

            
            int nextPartNumber = 0;
            int offset = 0;
            
            try {
                InputStream localInputStream = openInputStream();
                for (ListParts.Part part : parts) {
                    int partNumber = Integer.parseInt(part.partNumber);
                    long partSize = Long.parseLong(part.size);
                    // 只复用连续分块
                    if (++nextPartNumber != partNumber) {
                        loggerInfo(TAG, taskId, "part number %d not continuous", partNumber);
                        break;
                    }
                    // 校验远程和本地的 MD5 是否一致
                    String localMd5 = DigestUtils.getCOSMd5(localInputStream, 0, partSize);
                    if (localMd5 == null || !localMd5.equals(part.eTag)) {
                        loggerInfo(TAG, taskId, "part number %d md5 not the same, local md5=%s, part md5=%s", partNumber, localMd5, part.eTag);
                        break;
                    }
                    loggerInfo(TAG, taskId, "check part %d success", partNumber);
                    uploadParts.add(new UploadPart(part.eTag, partNumber, offset, partSize));
                    offset += partSize;
                }
                localInputStream.close();
                
            } catch (IOException exception) {
                loggerWarn(TAG, taskId, "check parts encounter exception: %s", exception.getMessage());
                throw CosXmlClientException.internalException(exception.getMessage());
            }
            
            loggerInfo(TAG, taskId, "you have uploaded %d parts of it, upload offset: %s, partNumber: %d", parts.size(), offset, nextPartNumber);
            return true;
        }
        

        private long parseInitiatedDate(String date) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            try {
                return dateFormat.parse(date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }

        // 上传所有分片
        private void uploadParts(String uploadId) throws Exception {

            long offset = 0;
            long size = totalUploadSize;
            int startNumber = 1;
            UploadPart lastUploadedPart = uploadParts.isEmpty() ? null : uploadParts.last();
            if (lastUploadedPart != null) {
                offset = lastUploadedPart.offset + lastUploadedPart.size;
                size = size - offset;
                startNumber = lastUploadedPart.number + 1;
            }

            // 本次上传的尺寸
            mTransferMetrics.size = size;

            if (size > 0) {
                loggerInfo(TAG, taskId, "start upload parts, offset=%s, size=%d, startNumber=%d", offset, size, startNumber);
                if (isParallelUpload()) {
                    uploadPartsTask = new ParallelUploadPartsTask(cosDirect, putObjectRequest, offset, size, startNumber, uploadId);
                } else {
                    uploadPartsTask = new SerialUploadPartsTask(cosDirect, putObjectRequest, offset, size, startNumber, uploadId);
                }
                uploadPartsTask.mMaxPartSize = mMaxPartSize;
                uploadPartsTask.setTaskId(taskId);
                uploadPartsTask.setProgressListener(putObjectRequest.getProgressListener());
                uploadParts.addAll(uploadPartsTask.upload());
            }
        }


        // complete 404 时，需要先去 head
        private CosXmlResult completeMultipartUpload(String uploadId)
                throws CosXmlClientException, CosXmlServiceException {

            LinkedHashMap<Integer,String> partNumberAndETag = new LinkedHashMap<>();
            for (UploadPart uploadPart : uploadParts) {
                partNumberAndETag.put(uploadPart.number, uploadPart.etag);
            }

            try {
                completeMultiUploadRequest = new CompleteMultiUploadRequest(bucket, key, uploadId, partNumberAndETag);
                completeMultiUploadRequest.setRequestHeaders(getCompleteHeaders(putObjectRequest));
                CompleteMultiUploadResult completeMultiUploadResult = cosDirect.completeMultipartUpload(completeMultiUploadRequest);
                loggerInfo(TAG, taskId,"complete upload task", key, uploadId);
                return completeMultiUploadResult;
            } catch (CosXmlServiceException serviceException) {
                if ("NoSuchUpload".equals(serviceException.getErrorCode())) {
                    // Head Object
                    try {
                        HeadObjectRequest headObjectRequest = new HeadObjectRequest(bucket, key);
                        headObjectRequest.setRegion(region);
                        loggerInfo(TAG, taskId,  "complete uploadId [%s] failed with NoSuchUpload. check if it has been uploaded by HeadObjectRequest.",
                                uploadId);
                        // 这里不用校验 crc64，后面会统一校验
                        return cosService.headObject(headObjectRequest);
                    } catch (Exception exception) {
                        throw serviceException;
                    }
                } else {
                    throw serviceException;
                }
            }
        }

        private void crc64Verify(String crc64) throws CosXmlClientException {

            if (TextUtils.isEmpty(crc64)) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),  "unable to verify with crc64 is null");
            }
            try {
                long remoteCrc64 = DigestUtils.getBigIntFromString(crc64);
                InputStream inputStream = openInputStream();
                long localCrc64 = DigestUtils.getCRC64(inputStream);
                inputStream.close();
                if (remoteCrc64 != localCrc64) {
                    throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(),
                            String.format(Locale.ENGLISH, "crc64 verify failed, remote crc 64bit value is %d, but local crc 64bit value is %d", remoteCrc64,
                                localCrc64));
                }
                loggerInfo(TAG, taskId, "verify crc64 %s success", crc64);
            } catch (IOException ioException) {
                throw new CosXmlClientException(ClientErrorCode.IO_ERROR.getCode(),
                        String.format(Locale.ENGLISH, "failed open inputStream to verify crc64: %s", ioException.getMessage()));
            }
        }

        private UploadPartRequest transform(PutObjectRequest putObjectRequest) throws CosXmlClientException {
            UploadPartRequest uploadPartRequest = null;
            if (putObjectRequest.getUri() != null) {
                uploadPartRequest = new UploadPartRequest(bucket, key, 1,
                        putObjectRequest.getUri(), mUploadId);
            } else if (putObjectRequest.getSrcPath() != null) {
                uploadPartRequest = new UploadPartRequest(bucket, key, 1,
                        putObjectRequest.getSrcPath(), mUploadId);
            }
            assetNotNull(uploadPartRequest);
            uploadPartRequest.setFileOffset(0);
            uploadPartRequest.setFileContentLength(new File(putObjectRequest.getSrcPath()).length());

            return uploadPartRequest;
        }


        // 只有分片上传才需要校验
        // 打开原始流或者加密流，用于 MD5 或者 CRC64 校验
        // 对于加密上传，必须要保证加密密钥以及内容加密密钥一致
        private InputStream openInputStream() throws CosXmlClientException {

            InputStream inputStream = openUnencryptedInputStream();
            if (cosDirect.isTransferSecurely()) {

                if (mCryptoContext != null) {
                    inputStream = cosDirect.getCryptoModule().newCOSCipherLiteInputStream(putObjectRequest,
                            mCryptoContext.getCipherLite());
                } else {
                    throw CosXmlClientException.internalException(mUploadId + " crypto context not found");
                }
            }
            assetNotNull(inputStream);
            return inputStream;
        }

        private InputStream openUnencryptedInputStream() throws CosXmlClientException {

            InputStream inputStream = null;
            try {
                String filePath = putObjectRequest.getSrcPath();
                Uri uri = putObjectRequest.getUri();

                if (filePath != null) {
                    inputStream =  new ResettableInputStream(filePath);
                } else if (uri != null && ContextHolder.getAppContext() != null) {
                    inputStream = ContextHolder.getAppContext().getContentResolver().openInputStream(uri);
                }
            } catch (Exception e) {
                throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), "open InputStream failed");
            }
            assetNotNull(inputStream);
            return inputStream;
        }

        private void assetNotNull(Object object) throws CosXmlClientException {
            if (object == null) {
                throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), Object.class.getSimpleName() + "is null");
            }
        }

//        private void delete() {
//
//            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);
//            deleteObjectRequest.setRegion(region);
//            try {
//                cosService.deleteObject(deleteObjectRequest);
//            } catch (CosXmlClientException e) {
//                e.printStackTrace();
//            } catch (CosXmlServiceException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
