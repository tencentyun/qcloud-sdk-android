package com.tencent.cos.xml.transfer;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.BeaconService;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.crypto.CipherLiteInputStream;
import com.tencent.cos.xml.crypto.MultipartUploadCryptoContext;
import com.tencent.cos.xml.crypto.COSDirect;
import com.tencent.cos.xml.crypto.ResettableInputStream;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsRequest;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsResult;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.AbortMultiUploadResult;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
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
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.TaskExecutors;
import com.tencent.qcloud.core.util.ContextHolder;
import com.tencent.qcloud.core.util.QCloudUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import bolts.CancellationTokenSource;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * 支持上传数据源，优先级为 filePath > Uri
 *
 * 1. filePath：支持分片上传、客户端加密上传
 * 2. Uri：支持分片上传、客户端加密上传
 *
 *
 * 3. byte 数组：暂不支持，不支持分片上传
 * 4. String 字符串：暂不支持，不支持分片上传
 * 5. InputStream：暂不支持，会预先将数据写到一个本地文件缓存，然后以文件上传的方式进行上传，否则：
 *                 无法支持分片上传（无法预知数据流的长度来判断是否需要分片，并且分片上传需要预先读取数据来校验分块）
 *                 无法支持加密上传（加密上传需要预先读取数据计算原始的 md5 值）
 *                一些重试的逻辑也无法支持
 *
 */
public class COSUploadTask extends COSTransferTask {

    public static final String TAG = "QCloudUpload";

    // 最多同时上传两个
    private static final int UPLOAD_CONCURRENT = 2;

    // 是否禁止用户操作
    // 在最后完成分片的过程中，不允许用户手动点击暂停或者取消，
    // 否则可能产生服务端和客户端回调状态不一致的情况。
    private volatile boolean frozenManual = false;

    private static ThreadPoolExecutor uploadTaskExecutor = new ThreadPoolExecutor(UPLOAD_CONCURRENT, UPLOAD_CONCURRENT, 5L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE),
            new TaskThreadFactory(TAG + "-", 8));

    private PutObjectRequest mPutObjectRequest;


    private SimpleUploadTask simpleUploadTask; 
    private MultipartUploadTask multipartUploadTask;

    private long multipartUploadThreshold = 2 * 1024 * 1024;

    /**
     * 上传文件、uri 时，会对其初始化
     */
    private long uploadLength = -1;


    public COSUploadTask(CosXmlSimpleService cosService, PutObjectRequest putObjectRequest) {
        super(cosService, putObjectRequest);
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
    protected void start() {
        super.start();
        frozenManual = false;
    }

    public boolean pause(boolean force) {

        if (taskState != TransferState.IN_PROGRESS && taskState != TransferState.WAITING) {
            QCloudLogger.i(TAG, "[%s]: cannot pause upload task in state %s", taskId, taskState);
            return false;
        }
        
        if (frozenManual && !force) {
            QCloudLogger.i(TAG, "[%s]: cannot pause upload task, frozenManual:%b, force: %b", taskId, frozenManual, frozenManual);
            return false;
        }

        QCloudLogger.i(TAG, "[%s]: pause upload task", taskId);
        
        manualPause = true;
        // 快速回调状态
        onTransferPaused();

        cts.cancel();
        
        if (simpleUploadTask != null) {
            simpleUploadTask.cancel();
        }
        
        if (multipartUploadTask != null) {
            multipartUploadTask.cancel();
        }
        
        return true;
    }

    public void resume() {
        
        if (taskState != TransferState.PAUSED) {
            QCloudLogger.i(TAG, "[%s]: cannot resume upload task in state %s", taskId, taskState);
            return;
        }
        
        QCloudLogger.i(TAG, "[%s]: resume upload task", taskId, taskState);
        start();
    }

    public Task<AbortMultiUploadResult> cancel(boolean force) {
        
        if (frozenManual && !force) {
            return Task.forError(new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(),
                    "cannot cancel this task now, please wait for task complete."));
        }

        QCloudLogger.i(TAG, "[%s]: cancel upload task", taskId);

        manualCancel = true;
        cts.cancel();
        if (simpleUploadTask != null) {
            simpleUploadTask.cancel();
        }
        if (multipartUploadTask != null) {
            multipartUploadTask.cancel();
        }
        
        if (multipartUploadTask != null) {
            return multipartUploadTask.abortUpload();
        } else {
            return Task.forResult(null);
        }
    }

    public HttpTaskMetrics getTaskMetrics() {
        return httpTaskMetrics;
    }

    @Override
    protected void onTransferSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
        super.onTransferSuccess(cosXmlRequest, cosXmlResult);
        BeaconService.getInstance().reportCOSUploadTaskSuccess(cosXmlRequest, cosDirect.isTransferSecurely());
    }

    @Override
    protected void onTransferFailed(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
        super.onTransferFailed(cosXmlRequest, clientException, serviceException);
        if (clientException != null) {
            BeaconService.getInstance().reportCOSUploadTaskClientException(cosXmlRequest, clientException, cosDirect.isTransferSecurely());
        } else if (serviceException != null) {
            BeaconService.getInstance().reportCOSUploadTaskServiceException(cosXmlRequest, serviceException, cosDirect.isTransferSecurely());
        }
    }

    @Override
    protected CosXmlResult execute() throws CosXmlClientException, CosXmlServiceException {
        CosXmlResult cosXmlResult;

        if (shouldMultipartUpload()) {
            cosXmlResult = multipartUpload();
        } else {
            cosXmlResult = simpleUpload();
        }
        return cosXmlResult;
    }

    @Override
    protected void checking() throws CosXmlClientException {

        httpTaskMetrics.setDomainName(mPutObjectRequest.getRequestHost(cosService.getConfig()));
        mPutObjectRequest.attachMetrics(httpTaskMetrics);
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
        UrlUploadPolicy urlUploadPolicy = mPutObjectRequest.getUrlUploadPolicy();

        if (TextUtils.isEmpty(filePath) && uri == null) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),
                    "Only support upload file or uri");
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
                uploadLength = QCloudUtils.getUriContentLength(uri, context.getContentResolver());
            }
        }
    }

    // 简单上传
    private CosXmlResult simpleUpload() throws CosXmlClientException, CosXmlServiceException {

        QCloudLogger.i(TAG, "[%s]: start upload with simple upload.", taskId, key);

        simpleUploadTask = new SimpleUploadTask(cosDirect, mPutObjectRequest, cts);
        simpleUploadTask.taskId = taskId;
        TaskExecutors.UPLOAD_EXECUTOR.execute(simpleUploadTask);
        Task<PutObjectResult> task = simpleUploadTask.getTask();
        try {
            task.waitForCompletion();
            if (task.isCancelled()) {
                throw CosXmlClientException.manualCancelException();
            } else if (task.isFaulted()) {
                throwException(task.getError());
            }
            QCloudLogger.i(TAG, "[%s]: upload success.", taskId);
        } catch (InterruptedException e) {
            throwException(new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), 
                    e.getMessage()));
        }
        return task.getResult();
    }

    // 分块上传
    private CosXmlResult multipartUpload() throws CosXmlClientException, CosXmlServiceException{
        
        QCloudLogger.i(TAG, "[%s]: start upload with multipart upload", taskId);

        multipartUploadTask = new MultipartUploadTask(cosDirect, mPutObjectRequest, getUploadLength(),
                cts, httpTaskMetrics);
        multipartUploadTask.setTaskId(taskId);
        multipartUploadTask.bucket = bucket;
        multipartUploadTask.key = key;
        multipartUploadTask.region = region;
        multipartUploadTask.cosServiceTaskProxy = new CosServiceTaskProxy(cosService);
        multipartUploadTask.cosService = cosService;
        TaskExecutors.UPLOAD_EXECUTOR.execute(multipartUploadTask);
        Task<CosXmlResult> task = multipartUploadTask.getTask();
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
            QCloudLogger.i(TAG, "[%s]: start upload with multipart upload.", taskId, key);
        }
        return task.getResult();
    }

    private boolean shouldMultipartUpload() {
        return isMultipartUploadRequest() && isMultipartUploadLength();
    }

    private boolean isMultipartUploadLength() {
        return uploadLength >= multipartUploadThreshold;
    }
    
    // 通过上传数据源的类型来判断是否可以分片上传
    private boolean isMultipartUploadRequest() {
        return true;
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

    private static class SimpleUploadTask implements Runnable {

        private bolts.TaskCompletionSource<PutObjectResult> tcs;
        private PutObjectRequest putObjectRequest;
        private CancellationTokenSource cancellationTokenSource;
        private COSDirect cosDirect;
        private String taskId;

        public SimpleUploadTask(COSDirect apiDirect, PutObjectRequest putObjectRequest,
                                CancellationTokenSource cancellationTokenSource) {
            this.cosDirect = apiDirect;
            this.putObjectRequest = putObjectRequest;
            this.tcs = new TaskCompletionSource<>();
            this.cancellationTokenSource = cancellationTokenSource;
        }

        @Override
        public void run() {

            if (cancellationTokenSource.isCancellationRequested()) {
                return;
            }

            try {
                tcs.setResult(cosDirect.putObject(putObjectRequest));
            } catch (CancellationException e) {
                tcs.setCancelled();
            } catch (Exception e) {
                tcs.setError(e);
            }
        }

        public Task<PutObjectResult> getTask() {
            return tcs.getTask();
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public void cancel() {
            cosDirect.cancel(putObjectRequest);
        }
    }


    private static class MultipartUploadTask implements Runnable, Comparable<MultipartUploadTask> {

        private bolts.TaskCompletionSource<CosXmlResult> tcs;
        private PutObjectRequest putObjectRequest;
        private CancellationTokenSource cancellationTokenSource;

        private String bucket, region, key;

        private TreeSet<UploadPart> uploadParts;

        private ParallelUploadTask parallelUploadTask;
        private SerialUploadTask serialUploadTask;

        private long poolNetworkPartSize = 1024 * 1024;
        private long normalNetworkPartSize = 1024 * 1024;

        private HttpTaskMetrics httpTaskMetrics;

        private long totalUploadSize = -1;

        // 用于上传的 uploadId
        private String mUploadId;

        private volatile ListMultiUploadsRequest listMultiUploadsRequest;
        private volatile InitMultipartUploadRequest initMultipartUploadRequest;
        private volatile ListPartsRequest listPartsRequest;
        private volatile CompleteMultiUploadRequest completeMultiUploadRequest;
        private COSDirect apiDirect;

        private CosServiceTaskProxy cosServiceTaskProxy;
        
        private String taskId = "";

        private CosXmlSimpleService cosService;

        private MultipartUploadCryptoContext mCryptoContext;


        public MultipartUploadTask(COSDirect apiDirect, PutObjectRequest putObjectRequest,
                                   long totalUploadSize, CancellationTokenSource cancellationTokenSource,
                                   HttpTaskMetrics httpTaskMetrics) {
            this.putObjectRequest = putObjectRequest;
            this.tcs = new TaskCompletionSource<>();
            this.cancellationTokenSource = cancellationTokenSource;
            this.apiDirect = apiDirect;

            uploadParts = new TreeSet<>(new Comparator<UploadPart>() {
                @Override
                public int compare(UploadPart o1, UploadPart o2) {
                    int x = o1.number;
                    int y = o2.number;
                    return (x < y) ? -1 : ((x == y) ? 0 : 1);
                }
            });

            this.httpTaskMetrics = httpTaskMetrics;
            this.totalUploadSize = totalUploadSize;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {

            try {


                if (!apiDirect.isTransferSecurely()) {

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

                // 4. 并发上传剩余分片
                checkoutManualCanceled();
                if (isParallelUpload()) {
                    multipartUploadParallel(mUploadId);
                } else {
                    multipartUploadSerial(mUploadId);
                }

                // 4. 完成分片上传
                checkoutManualCanceled();
                CosXmlResult cosXmlResult = completeMultipartUpload(mUploadId);

                // 5. 最后进行校验文件
                checkoutManualCanceled();
                crc64Verify(getCRC64(cosXmlResult));

                tcs.setResult(cosXmlResult);
            } catch (CancellationException e) {
                tcs.setCancelled();
            } catch (Exception e) {
                tcs.setError(e);
            }
        }
        
        public void cancel() {

            cancellationTokenSource.cancel();
            if (listMultiUploadsRequest != null) {
                apiDirect.cancel(listMultiUploadsRequest);
            }
            if (initMultipartUploadRequest != null) {
                apiDirect.cancel(initMultipartUploadRequest);
            }
            if (listPartsRequest != null) {
                apiDirect.cancel(listPartsRequest);
            }
            if (completeMultiUploadRequest != null) {
                apiDirect.cancel(completeMultiUploadRequest);
            }
            if (parallelUploadTask != null) {
                parallelUploadTask.cancel();
            }

            if (serialUploadTask != null) {
                serialUploadTask.cancel();
            }
        }

        public Task<AbortMultiUploadResult> abortUpload() {

            AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(bucket, key, mUploadId);
            abortMultiUploadRequest.setRegion(region);
            return cosServiceTaskProxy.abortMultiUpload(abortMultiUploadRequest);
        }

        public Task<CosXmlResult> getTask() {
            return tcs.getTask();
        }

        private boolean isParallelUpload() {
            return !apiDirect.isTransferSecurely();
        }

        private void checkoutManualCanceled() throws CosXmlClientException {
            if (cancellationTokenSource.isCancellationRequested()) {
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

        // 根据 key 拿到所有备选的 uploadId，需要对比存储类型是否一致
        private List<String> listObjectUploadIds() throws CosXmlClientException, CosXmlServiceException {
            
            String prefix = key.startsWith("/") ? key.substring(1) : key;
            
            listMultiUploadsRequest = new ListMultiUploadsRequest(bucket);
            listMultiUploadsRequest.setPrefix(prefix);
            listMultiUploadsRequest.setRegion(region);
            List<String> uploadIds = new LinkedList<>();
            ListMultiUploadsResult listMultiUploadsResult = cosService.listMultiUploads(listMultiUploadsRequest);
            ListMultipartUploads listMultipartUploads = listMultiUploadsResult.listMultipartUploads;
            if (listMultipartUploads != null) {
                List<ListMultipartUploads.Upload> uploads = listMultipartUploads.uploads;
                QCloudLogger.i(TAG, "[%s]: find %d uploadIds with prefix %s", taskId, uploads.size(), prefix);
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
            QCloudLogger.i(TAG, "[%s]: find %d plan uploadId", taskId, uploadIds.size());
            return uploadIds;
        }

        // 生成一个新的 uploadId 用于上传
        private String initUploadId() throws CosXmlClientException, CosXmlServiceException {
            
            initMultipartUploadRequest = new InitMultipartUploadRequest(bucket, key);
            initMultipartUploadRequest.setRegion(region);
            InitMultipartUploadResult initMultipartUploadResult = apiDirect.initMultipartUpload(initMultipartUploadRequest);
            String uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;
            if (apiDirect.isTransferSecurely()) {
                mCryptoContext = apiDirect.getCryptoModule().getCryptoContext(uploadId);
            }
            QCloudLogger.i(TAG, "[%s]: create a new uploadId %s", taskId, uploadId);
            return uploadId;
        }

        // 1. 根据 uploadId 获取已经上传的分片
        // 2. 校验分片的有效性，尽可能的去复用分片
        // 3. 生成 uploadParts，用于后续上传
        private boolean checkoutUploadId(String uploadId)
                throws CosXmlClientException, CosXmlServiceException {

            uploadParts.clear();

            if (apiDirect.isTransferSecurely()) {
                if (apiDirect.getCryptoModule() == null) {
                    throw CosXmlClientException.internalException("cannot client size encryption, crypto module is null");
                }
                if (!apiDirect.getCryptoModule().hasMultipartUploadContext(uploadId)) {
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
                        break;
                    }
                    // 校验远程和本地的 MD5 是否一致
                    String localMd5 = DigestUtils.getCOSMd5(localInputStream, offset, partSize);
                    if (localMd5 == null || !localMd5.equals(part.eTag)) {
                        break;
                    }

                    uploadParts.add(new UploadPart(part.eTag, partNumber, offset, partSize));
                    offset += partSize;
                }
                localInputStream.close();
                
            } catch (IOException exception) {
                QCloudLogger.w(TAG, "[%s]: check parts encounter exception: %s", taskId, exception.getMessage());
                throw CosXmlClientException.internalException(exception.getMessage());
            }
            
            QCloudLogger.i(TAG, "[%s]: you have uploaded %d parts of it, upload offset: %s, partNumber: %d", taskId, parts.size(), offset, nextPartNumber);
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

        // 并发上传所有分片
        private void multipartUploadParallel(String uploadId) throws CosXmlClientException, CosXmlServiceException {

            long offset = 0;
            long size = totalUploadSize;
            int startNumber = 1;
            UploadPart lastUploadedPart = uploadParts.isEmpty() ? null : uploadParts.last();
            if (lastUploadedPart != null) {
                offset = lastUploadedPart.offset + lastUploadedPart.size;
                size = size - offset;
                startNumber = lastUploadedPart.number + 1;
            }
            if (size > 0) {
                parallelUploadTask = new ParallelUploadTask(cosService, putObjectRequest, offset, size, startNumber, uploadId);
                parallelUploadTask.setHttpTaskMetrics(httpTaskMetrics);
                parallelUploadTask.setNormalNetworkSliceSize(normalNetworkPartSize);
                parallelUploadTask.setPoolNetworkSliceSize(poolNetworkPartSize);
                parallelUploadTask.setProgressListener(putObjectRequest.getProgressListener());
                uploadParts.addAll(parallelUploadTask.waitForComplete());
            }
        }

        private void multipartUploadSerial(String uploadId) throws CosXmlClientException, CosXmlServiceException {

            long offset = 0;
            long size = totalUploadSize;
            int startNumber = 1;
            UploadPart lastUploadedPart = uploadParts.isEmpty() ? null : uploadParts.last();
            if (lastUploadedPart != null) {
                offset = lastUploadedPart.offset + lastUploadedPart.size;
                size = size - offset;
                startNumber = lastUploadedPart.number + 1;
            }
            if (size > 0) {
                serialUploadTask = new SerialUploadTask(apiDirect, putObjectRequest, offset, size, startNumber, uploadId);
                serialUploadTask.setTaskId(taskId);
                serialUploadTask.setHttpTaskMetrics(httpTaskMetrics);
                serialUploadTask.setNormalNetworkSliceSize(normalNetworkPartSize);
                serialUploadTask.setPoolNetworkSliceSize(poolNetworkPartSize);
                serialUploadTask.setProgressListener(putObjectRequest.getProgressListener());
                uploadParts.addAll(serialUploadTask.waitForComplete());
            }
        }


        // complete 404 时，需要先去 head
        private CosXmlResult completeMultipartUpload(String uploadId)
                throws CosXmlClientException, CosXmlServiceException {

            Map<Integer,String> partNumberAndETag = new HashMap<>();
            for (UploadPart uploadPart : uploadParts) {
                partNumberAndETag.put(uploadPart.number, uploadPart.etag);
            }

            try {
                completeMultiUploadRequest = new CompleteMultiUploadRequest(bucket, key, uploadId, partNumberAndETag);
                CompleteMultiUploadResult completeMultiUploadResult = apiDirect.completeMultipartUpload(completeMultiUploadRequest);
                QCloudLogger.i(TAG, "[%s]: complete upload task", taskId, key, uploadId);
                return completeMultiUploadResult;
            } catch (CosXmlServiceException serviceException) {
                if ("NoSuchUpload".equals(serviceException.getErrorCode())) {
                    // Head Object
                    HeadObjectRequest headObjectRequest = new HeadObjectRequest(bucket, key);
                    headObjectRequest.setRegion(region);
                    QCloudLogger.i(TAG, "complete uploadId [%s] failed with NoSuchUpload. check if it has been uploaded by HeadObjectRequest.",
                            uploadId);
                    return cosService.headObject(headObjectRequest);
                } else {
                    throw serviceException;
                }
            }
        }

        private void crc64Verify(String crc64) throws CosXmlClientException {

            if (TextUtils.isEmpty(crc64)) {
                QCloudLogger.i(TAG, "[%s]: unable to verify object [%s] with crc64 is null", taskId, key);
                return;
            }
            try {
                long remoteCrc64 = DigestUtils.getBigIntFromString(crc64);
                InputStream inputStream = openInputStream();
                long localCrc64 = DigestUtils.getCRC64(inputStream);
                inputStream.close();
                QCloudLogger.i(TAG, "crc64 verify failed, remote crc 64bit value is %s, %d, but local crc 64bit value is %d", crc64, remoteCrc64,
                        localCrc64);
                if (remoteCrc64 != localCrc64) {
                    delete();
                    throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(),
                            String.format(Locale.ENGLISH, "crc64 verify failed, remote crc 64bit value is %d, but local crc 64bit value is %d", remoteCrc64,
                                localCrc64));
                }
                QCloudLogger.i(TAG, "[%s]: verify crc64 %s success", taskId, crc64);
            } catch (IOException ioException) {
                throw new CosXmlClientException(ClientErrorCode.IO_ERROR.getCode(),
                        String.format(Locale.ENGLISH, "failed open inputStream to verify crc64: %s", ioException.getMessage()));
            }
        }

        private UploadPartRequest transform(PutObjectRequest putObjectRequest) {
            UploadPartRequest uploadPartRequest =  new UploadPartRequest(bucket, key, 1,
                    putObjectRequest.getSrcPath(), mUploadId);
            uploadPartRequest.setFileOffset(0);
            uploadPartRequest.setFileContentLength(new File(putObjectRequest.getSrcPath()).length());

            return uploadPartRequest;
        }


        // 只有分片上传才需要校验
        // 打开原始流或者加密流，用于 MD5 或者 CRC64 校验
        // 对于加密上传，必须要保证加密密钥以及内容加密密钥一致
        private InputStream openInputStream() throws IOException, CosXmlClientException {

            String filePath = putObjectRequest.getSrcPath();
            Uri uri = putObjectRequest.getUri();

            InputStream inputStream = null;
            if (filePath != null) {
                inputStream =  new ResettableInputStream(filePath);
            } else if (uri != null && ContextHolder.getAppContext() != null) {
                inputStream = ContextHolder.getAppContext().getContentResolver().openInputStream(uri);
            }

            if (apiDirect.isTransferSecurely()) {

                if (mCryptoContext != null) {
                    inputStream = apiDirect.getCryptoModule().newMultipartCOSCipherInputStream(transform(putObjectRequest),
                            mCryptoContext.getCipherLite());
                } else {
                    throw CosXmlClientException.internalException(mUploadId + " crypto context not found");
                }
            }

            if (inputStream == null) {
                throw new IOException("There is no valid data source!");
            }
            return inputStream;
        }

        private void delete() {

            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);
            deleteObjectRequest.setRegion(region);
            try {
                cosService.deleteObject(deleteObjectRequest);
            } catch (CosXmlClientException e) {
                e.printStackTrace();
            } catch (CosXmlServiceException e) {
                e.printStackTrace();
            }
        }

        @Nullable private String getCRC64(CosXmlResult cosXmlResult) {
            List<String> crc64Values = cosXmlResult.headers.get("x-cos-hash-crc64ecma");
            if (crc64Values != null && crc64Values.size() == 1) {
                return crc64Values.get(0);
            }
            return null;
        }

        @Override
        public int compareTo(MultipartUploadTask o) {
            return 0;
        }
    }
}
