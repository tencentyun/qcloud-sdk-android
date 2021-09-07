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

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.crypto.COSDirect;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.TaskExecutors;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;

import bolts.CancellationToken;
import bolts.CancellationTokenSource;
import bolts.Task;
import bolts.TaskCompletionSource;

import static com.tencent.cos.xml.common.ClientErrorCode.INTERNAL_ERROR;

/**
 * 顺序上传
 */
class SerialUploadTask {

    private List<UploadPartTask> mUploadPartTasks;
    private Task<?> mTask;
    private TaskCompletionSource<?> mTaskCompletionSource;
    private CancellationTokenSource mCancellationTokenSource;
    private COSDirect apiDirect;

    private final Object mCheckingLock = new Object();

    private CosXmlProgressListener mProgressListener;

    private Map<UploadPartRequest, Long> mUploadCompleteMap;

    private long mTotalComplete = 0;

    private long mOffset, mSize;

    private long mOffsetPointer;

    // slice
    private long mNormalNetworkSliceSize = 1024 * 1024;
    private long mPoolNetworkSliceSize = 1024 * 1024;

    private PutObjectRequest mPutObjectRequest;

    // 初始化为网络较差的情况，如果上传速度很快，则使用正常分片大小
    private volatile boolean isPoolNetwork = true;

    // 速度大于这个值的认为是正常网络状况
    // 为保证上传的成功率，一次速度小于 normalNetworkSpeed，isPoolNetwork 即为 true，
    // 连续 3 次速度大于 normalNetworkSpeed，isPoolNetwork 才会由 true 变为 false
    private final long normalNetworkSpeed = 100 * 1024;

    private final int mPartNumber;
    private final String mUploadId;

    private Set<COSUploadTask.UploadPart> uploadParts = Collections.synchronizedSet(new HashSet<COSUploadTask.UploadPart>());

    private volatile HttpTaskMetrics httpTaskMetrics;

    private String taskId;

    public SerialUploadTask(COSDirect apiDirect, PutObjectRequest putObjectRequest,
                            long offset, long size, int startPartNumber, String uploadId) {

        mPartNumber = startPartNumber;
        mUploadId = uploadId;
        mOffset = offset;
        mSize = size;
        mOffsetPointer = mOffset;

        mPutObjectRequest = putObjectRequest;
        mTaskCompletionSource = new TaskCompletionSource<>();
        mTask = mTaskCompletionSource.getTask();
        this.apiDirect = apiDirect;
        mCancellationTokenSource = new CancellationTokenSource();
        mUploadPartTasks = new LinkedList<>();
        mUploadCompleteMap = new HashMap<>();
    }

    public void setProgressListener(CosXmlProgressListener progressListener) {
        this.mProgressListener = progressListener;
    }

    /**
     * 等待任务执行完成，执行错误，或者取消时会抛出异常
     *
     * @throws CosXmlClientException
     */
    public Set<COSUploadTask.UploadPart> waitForComplete() throws CosXmlClientException, CosXmlServiceException {

        int nextNumber = mPartNumber;

        while (!mTask.isCompleted()) {

            final UploadPartRequest uploadPartRequest = nextUploadPartRequest(nextNumber);

            // 所有的分块上传请求都已经发送
            if (uploadPartRequest == null) {
                mTaskCompletionSource.trySetResult(null);
                break;
            }
            uploadPartRequest.attachMetrics(new HttpTaskMetrics());
            uploadPartRequest.setProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    updatePartProgress(uploadPartRequest, complete);
                }
            });

            UploadPartTask uploadPartTask = new UploadPartTask(apiDirect, uploadPartRequest, mCancellationTokenSource.getToken());
            mUploadPartTasks.add(uploadPartTask);

            QCloudLogger.i(COSUploadTask.TAG, "[%s]: start upload part, part number %d, offset %d, size %d",
                    taskId, uploadPartRequest.getPartNumber(), uploadPartRequest.getFileOffset(), uploadPartRequest.getFileContentLength());
            TaskExecutors.UPLOAD_EXECUTOR.execute(uploadPartTask);

            try {
                Task<UploadPartResult> task = uploadPartTask.getTask();
                task.waitForCompletion();
                if (task.isFaulted()) {
                    QCloudLogger.i(COSUploadTask.TAG, "[%s]: upload part failed, part number %d, offset %d: %s", taskId, uploadPartRequest.getPartNumber(),
                            uploadPartRequest.getFileOffset(), task.getError().getMessage());
                    mTaskCompletionSource.trySetError(task.getError());
                } else if (task.isCancelled()) {
                    QCloudLogger.i(COSUploadTask.TAG, "[%s]: upload part cancelled, part number %d, offset %d", taskId, uploadPartRequest.getPartNumber(),
                            uploadPartRequest.getFileOffset());
                } else if (task.isCompleted()) {
                    QCloudLogger.i(COSUploadTask.TAG, "[%s]: upload part success, part number %d, offset %d", taskId, uploadPartRequest.getPartNumber(),
                            uploadPartRequest.getFileOffset());
                    UploadPartResult uploadPartResult = task.getResult();
                    COSUploadTask.UploadPart uploadPart = new COSUploadTask.UploadPart(uploadPartResult.eTag,
                            uploadPartRequest.getPartNumber(), uploadPartRequest.getFileOffset(), uploadPartRequest.getFileContentLength());
                    uploadParts.add(uploadPart);
                    mergeTaskMetrics(uploadPartRequest.getMetrics());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            nextNumber++;
        }


        try {
            mTask.waitForCompletion();
            if (mTask.isFaulted()) {
                checkoutException(mTask.getError());
                cancelAllUploadTask();
            } else if (mTask.isCancelled()) {
                throw CosXmlClientException.manualCancelException();
            }

        } catch (InterruptedException e) {
            throw new CosXmlClientException(INTERNAL_ERROR.getCode(), INTERNAL_ERROR.getErrorMsg() + ": " + e.getMessage());
        }

        return uploadParts;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void cancel() {

        mCancellationTokenSource.cancel();
        mTaskCompletionSource.setCancelled();
        cancelAllUploadTask();
    }

    public void setHttpTaskMetrics(HttpTaskMetrics httpTaskMetrics) {
        this.httpTaskMetrics = httpTaskMetrics;
    }

    
    
    synchronized private void mergeTaskMetrics(HttpTaskMetrics httpTaskMetrics) {
        if (httpTaskMetrics != null && this.httpTaskMetrics != null) {
            this.httpTaskMetrics.merge(httpTaskMetrics);
        }
    }

    synchronized private void updatePartProgress(UploadPartRequest request, long progress) {
        long lastComplete = 0;
        if (mUploadCompleteMap.containsKey(request)) {
            lastComplete = mUploadCompleteMap.get(request);
        }
        long delta = progress - lastComplete;
        mUploadCompleteMap.put(request, progress);
        mTotalComplete += delta;

        if (mProgressListener != null) {
            mProgressListener.onProgress(mTotalComplete + mOffset, -1);
        }
    }

    /**
     * 获取指定分块的上传请求
     */
    private @Nullable UploadPartRequest nextUploadPartRequest(int partNumber) throws CosXmlClientException {

        // 已经全部发送了
        if (mOffsetPointer >= mOffset + mSize) {
            return null;
        }
        
        long offset = mOffsetPointer;
        long sliceSize = isPoolNetwork ? mPoolNetworkSliceSize : mNormalNetworkSliceSize;
        sliceSize = Math.min(sliceSize, mOffset + mSize - mOffsetPointer);
        
        mOffsetPointer += sliceSize;
        String filePath = mPutObjectRequest.getSrcPath();
        Uri uri = mPutObjectRequest.getUri();
        String bucket = mPutObjectRequest.getBucket();
        String key = mPutObjectRequest.getCosPath();
        String region = mPutObjectRequest.getRegion();


        // isLastPart, uploadId, fileLength, partNumber,

        UploadPartRequest uploadPartRequest = null;
        if (filePath != null) {
            uploadPartRequest = new UploadPartRequest(bucket, key, partNumber, filePath, offset, sliceSize, mUploadId);
        } else if (uri != null) {
            uploadPartRequest = new UploadPartRequest(bucket, key, partNumber, uri, offset, sliceSize, mUploadId);
        }

        if (uploadPartRequest != null) {
            if (!TextUtils.isEmpty(region)) {
                uploadPartRequest.setRegion(region);
            }
            uploadPartRequest.setLastPart(mOffsetPointer >= mOffset + mSize);
        }
        
        return uploadPartRequest;
    }
    
    public void setNormalNetworkSliceSize(long normalNetworkSliceSize) {
        this.mNormalNetworkSliceSize = normalNetworkSliceSize;
    }

    public void setPoolNetworkSliceSize(long poolNetworkSliceSize) {
        this.mPoolNetworkSliceSize = poolNetworkSliceSize;
    }

    private void cancelAllUploadTask() {

        List<UploadPartTask> snapshot = new LinkedList<>(mUploadPartTasks);
        
        synchronized (mCheckingLock) {
            for (UploadPartTask task : snapshot) {
                task.cancel();
            }
        }
    }

    private void checkoutException(Exception e) throws CosXmlClientException, CosXmlServiceException{

        if (e instanceof CosXmlClientException) {
            throw (CosXmlClientException) e;
        } else if (e instanceof CosXmlServiceException) {
            throw (CosXmlServiceException) e;
        } else throw new CosXmlClientException(INTERNAL_ERROR.getCode(), e.getMessage());
    }


    private static class UploadPartTask implements Runnable, Comparable<UploadPartTask> {

        private TaskCompletionSource<UploadPartResult> tcs;
        private CancellationToken mCancellationToken;
        private COSDirect apiDirect;
        private UploadPartRequest uploadPartRequest;

        public UploadPartTask(COSDirect apiDirect, UploadPartRequest uploadPartRequest, CancellationToken cancellationToken) {
            this.apiDirect = apiDirect;
            this.uploadPartRequest = uploadPartRequest;
            this.tcs = new TaskCompletionSource<>();
            this.mCancellationToken = cancellationToken;
        }

        @Override
        public void run() {

            if (mCancellationToken.isCancellationRequested()) {
                return;
            }

            try {
                tcs.setResult(apiDirect.uploadPart(uploadPartRequest));
            } catch (CancellationException e) {
                tcs.setCancelled();
            } catch (Exception e) {
                tcs.setError(e);
            }
        }

        public Task<UploadPartResult> getTask() {
            return tcs.getTask();
        }

        public void cancel() {
            apiDirect.cancel(uploadPartRequest);
        }

        @Override
        public int compareTo(UploadPartTask o) {
            return 0;
        }
    }

}
