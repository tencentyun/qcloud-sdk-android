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

import static com.tencent.cos.xml.common.ClientErrorCode.ETAG_NOT_FOUND;

import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseArray;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.crypto.COSDirect;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.qcloud.core.http.HttpTaskMetrics;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import bolts.Task;
import bolts.TaskCompletionSource;


abstract class BaseUploadPartsTask {

    protected COSDirect mCosDirect;

    // 上传的偏移量
    protected long mOffset;

    // 需要上传的长度
    protected long mSize;

    // 上传的初始 number
    protected int mStartNumber;

    protected String mUploadId;

    protected PutObjectRequest mPutObjectRequest;

    protected Set<COSUploadTask.UploadPart> uploadParts = Collections.synchronizedSet(new HashSet<COSUploadTask.UploadPart>());

    private CosXmlProgressListener progressListener;

    protected String taskId;

    // 分片大小
    protected long mMaxPartSize = 1024 * 1024;

    protected final String TAG = "QCloudUpload";

    BaseUploadPartsTask(COSDirect cosDirect, PutObjectRequest putObjectRequest,
                        long offset, long size, int startPartNumber, String uploadId) {

        mCosDirect = cosDirect;
        mPutObjectRequest = putObjectRequest;
        mOffset = offset;
        mSize = size;
        mStartNumber = startPartNumber;
        mUploadId = uploadId;
    }

    abstract public Set<COSUploadTask.UploadPart> upload() throws Exception;

    abstract public void cancel();


    UploadPartRequest getUploadRequest(int partNumber, long offset, long size) {

        // 已经全部发送了
        if (offset >= mOffset + mSize) {
            return null;
        }

        String filePath = mPutObjectRequest.getSrcPath();
        Uri uri = mPutObjectRequest.getUri();
        String bucket = mPutObjectRequest.getBucket();
        String key = mPutObjectRequest.getCosPath();
        String region = mPutObjectRequest.getRegion();

        UploadPartRequest uploadPartRequest = null;

        // 只有文件路径和 uri 才会分片上传
        if (filePath != null) {
            uploadPartRequest = new UploadPartRequest(bucket, key, partNumber, filePath, offset, size, mUploadId);
        } else if (uri != null) {
            uploadPartRequest = new UploadPartRequest(bucket, key, partNumber, uri, offset, size, mUploadId);
        }

        if (uploadPartRequest != null) {
            if (!TextUtils.isEmpty(region)) {
                uploadPartRequest.setRegion(region);
            }
            uploadPartRequest.setRequestHeaders(getUploadPartHeaders(mPutObjectRequest));
            uploadPartRequest.setLastPart(offset + size >= mOffset + mSize);
        }

        return uploadPartRequest;
    }

    private Map<String, List<String>> getUploadPartHeaders(PutObjectRequest putObjectRequest) {
        Map<String, List<String>> customHeaders = putObjectRequest.getRequestHeaders();
        if (customHeaders == null) {
            return new HashMap<>();
        }
        Map<String, List<String>> headers = new HashMap<>();
        for (String header : customHeaders.keySet()) {
            if (header.startsWith("x-cos-server-side-encryption")
                    || header.equals("x-cos-traffic-limit")) {
                headers.put(header, customHeaders.get(header));
            }
        }
        return headers;
    }

    void notifyProgressChange(long complete, long size) {
        if (progressListener != null) {
            progressListener.onProgress(complete, size);
        }
    }

    public void setProgressListener(CosXmlProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}

/**
 * 顺序上传分片
 */
class SerialUploadPartsTask extends BaseUploadPartsTask {


    private long mStartPointer;

    // 当前上传的 partNumber
    private int mPartNumber;

    private volatile HttpTaskMetrics httpTaskMetrics;

    private UploadPartRequest currentUploadPartRequest;


    public SerialUploadPartsTask(COSDirect cosDirect, PutObjectRequest putObjectRequest,
                            long offset, long size, int startPartNumber, String uploadId) {

        super(cosDirect, putObjectRequest, offset, size, startPartNumber, uploadId);
    }




    @Override
    public Set<COSUploadTask.UploadPart> upload() throws CosXmlClientException, CosXmlServiceException{

        mStartPointer = mOffset;
        mPartNumber = mStartNumber;

        while (mStartPointer < mOffset + mSize) {

            long partSize = Math.min(mMaxPartSize, mOffset + mSize - mStartPointer);
            currentUploadPartRequest = getUploadRequest(mPartNumber, mStartPointer, partSize);
            currentUploadPartRequest.setProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    notifyProgressChange(mStartPointer + complete, mOffset + mSize);
                }
            });
            UploadPartResult uploadPartResult = mCosDirect.uploadPart(currentUploadPartRequest);
            String etag = uploadPartResult.eTag;
            COSTransferTask.loggerInfo(TAG, taskId, "upload part %d, etag=%s", mPartNumber, etag);
            if (TextUtils.isEmpty(etag)) {
                throw new CosXmlClientException(ETAG_NOT_FOUND);
            }
            COSUploadTask.UploadPart uploadPart = new COSUploadTask.UploadPart(
                    etag, mPartNumber, mStartPointer, partSize);
            mStartPointer += partSize;
            mPartNumber++;
            uploadParts.add(uploadPart);
        }
        return uploadParts;
    }

    @Override public void cancel() {
        if (currentUploadPartRequest != null) {
            mCosDirect.cancel(currentUploadPartRequest);
        }
    }

    public void setHttpTaskMetrics(HttpTaskMetrics httpTaskMetrics) {
        this.httpTaskMetrics = httpTaskMetrics;
    }
    
    synchronized private void mergeTaskMetrics(HttpTaskMetrics httpTaskMetrics) {
        if (httpTaskMetrics != null && this.httpTaskMetrics != null) {
            this.httpTaskMetrics.merge(httpTaskMetrics);
        }
    }
}

/**
 * 通过并发的方式上传分片
 */
class ParallelUploadPartsTask extends BaseUploadPartsTask {

    private final Set<UploadPartRequest> runningRequestSet = Collections.synchronizedSet(new HashSet<UploadPartRequest>());
    private TaskCompletionSource<Set<COSUploadTask.UploadPart>> tcs = new TaskCompletionSource<>();
    private SparseArray<Long> uploadPartProgress = new SparseArray<>();
    private AtomicLong mTotalProgress = new AtomicLong(0);

    ParallelUploadPartsTask(COSDirect cosDirect, PutObjectRequest putObjectRequest, long offset, long size, int startPartNumber, String uploadId) {
        super(cosDirect, putObjectRequest, offset, size, startPartNumber, uploadId);
    }

    @Override
    public Set<COSUploadTask.UploadPart> upload() throws Exception {

        final Task<Set<COSUploadTask.UploadPart>> task = tcs.getTask();

        final int partCount = calculatePartNumber(mSize, mMaxPartSize);

        final AtomicInteger completeCounter = new AtomicInteger(0);
        for (int i = 0; i < partCount; i++) {

            final int partNumber = mStartNumber + i;
            final long startPointer = mOffset + i * mMaxPartSize;
            final long partSize = Math.min(mMaxPartSize, mOffset + mSize - startPointer);
            final UploadPartRequest uploadPartRequest = getUploadRequest(partNumber, startPointer, partSize);
            uploadPartRequest.setProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    updateProgress(uploadPartRequest, complete);
                }
            });
            synchronized(runningRequestSet) {
                runningRequestSet.add(uploadPartRequest);
            }

            mCosDirect.uploadPartAsync(uploadPartRequest, new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    synchronized(runningRequestSet) {
                        runningRequestSet.remove(uploadPartRequest);
                    }
                    UploadPartResult uploadPartResult = (UploadPartResult) result;
                    String eTag = uploadPartResult.eTag;
                    COSTransferTask.loggerInfo(TAG, taskId, "upload part %d, etag=%s", partNumber, eTag);

                    // 检查 etag 参数是否存在
                    if (TextUtils.isEmpty(eTag)) {
                        tcs.trySetError(new CosXmlClientException(ETAG_NOT_FOUND));
                        cancelAllUploadingRequests();
                        return;
                    }
                    uploadParts.add(new COSUploadTask.UploadPart(uploadPartResult.eTag, partNumber, startPointer, partSize));

                    if (completeCounter.addAndGet(1) >= partCount) {
                        tcs.trySetResult(uploadParts);
                    }
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    if (clientException != null) {
                        tcs.trySetError(clientException);
                    } else if (serviceException != null) {
                        tcs.trySetError(serviceException);
                    } else {
                        tcs.trySetError(new CosXmlClientException(ClientErrorCode.UNKNOWN));
                    }
                    cancelAllUploadingRequests();
                }
            });

        }

        task.waitForCompletion();

        if (task.isFaulted()) {
            throw task.getError();
        } else if (task.isCancelled()) {
            throw new CosXmlClientException(ClientErrorCode.USER_CANCELLED);
        }

        return task.getResult();
    }

    synchronized private void updateProgress(UploadPartRequest request, long progress) {
        int partNumber = request.getPartNumber();
        long lastComplete = uploadPartProgress.get(partNumber, 0L);
        long delta = progress - lastComplete;
        uploadPartProgress.put(partNumber, progress);
        notifyProgressChange(mOffset + mTotalProgress.addAndGet(delta), mOffset + mSize);
    }


    private int calculatePartNumber(long size, long maxPartSize) {
        int number = (int) (size / maxPartSize);
        if (size % maxPartSize != 0) {
            number++;
        }
        return number;
    }

    @Override
    public void cancel() {
        tcs.trySetCancelled();
        cancelAllUploadingRequests();
    }
    
    private void cancelAllUploadingRequests() {
        synchronized(runningRequestSet) {
            for (UploadPartRequest request : runningRequestSet) {
                mCosDirect.cancel(request);
            }
        }
    }
}


