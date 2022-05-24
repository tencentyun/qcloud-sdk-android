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

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.task.TaskExecutors;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import bolts.CancellationToken;
import bolts.CancellationTokenSource;
import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

import static com.tencent.cos.xml.common.ClientErrorCode.INTERNAL_ERROR;

/**
 * 1. 并发上传分块；
 * 2. 根据网络情况来自适应分块大小；
 * 3. 由于是自适应分块大小，因此分块数不是固定的，所以必须是连续分块才能续传，否则 partNumber 可能会产生冲突；
 * 4. 采用滑动窗口机制控制上传请求，并保证发送的请求尽可能连续；
 */
class ParallelUploadTask {

    private List<UploadPartRequest> mRequests;
    private List<UploadPartTask> mUploadPartTasks;
    private Task<?> mTask;
    private TaskCompletionSource<?> mTaskCompletionSource;
    private CancellationTokenSource mCancellationTokenSource;
    private CosXmlSimpleService mCosXmlSimpleService;

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

    private SlidingWindow slidingWindow;

    private Set<COSUploadTask.UploadPart> uploadParts = Collections.synchronizedSet(new HashSet<COSUploadTask.UploadPart>());

    private volatile HttpTaskMetrics httpTaskMetrics;

    private String taskId;

    public ParallelUploadTask(CosXmlSimpleService cosXmlSimpleService, PutObjectRequest putObjectRequest,
                              long offset, long size, int startPartNumber, String uploadId) {

        mPartNumber = startPartNumber;
        mUploadId = uploadId;
        mOffset = offset;
        mSize = size;
        slidingWindow = new SlidingWindow(startPartNumber, 3);
        mOffsetPointer = mOffset;

        mPutObjectRequest = putObjectRequest;
        mTaskCompletionSource = new TaskCompletionSource<>();
        mTask = mTaskCompletionSource.getTask();
        mCosXmlSimpleService = cosXmlSimpleService;
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

        final AtomicInteger count = new AtomicInteger(0);
        final AtomicInteger progress = new AtomicInteger(0);
        final AtomicBoolean sendComplete = new AtomicBoolean(false);

        while (!mTask.isCompleted()) {

            final int nextNumber = slidingWindow.getNextNumber();
            final UploadPartRequest uploadPartRequest = nextUploadPartRequest(nextNumber);

            // 所有的分块上传请求都已经发送
            if (uploadPartRequest == null) {
                sendComplete.set(true);
                break;
            }
            uploadPartRequest.attachMetrics(new HttpTaskMetrics());
            count.addAndGet(1);

            UploadPartTask uploadPartTask = new UploadPartTask(mCosXmlSimpleService, uploadPartRequest, mCancellationTokenSource.getToken());
            mUploadPartTasks.add(uploadPartTask);
            uploadPartTask.getTask().continueWith(new Continuation<UploadPartResult, Void>() {
                @Override
                public Void then(Task<UploadPartResult> task) throws Exception {

                    slidingWindow.complete(nextNumber);

                    if (task.isFaulted()) {
                        mTaskCompletionSource.trySetError(task.getError());
                    }

                    if (task.isCompleted()) {
                        UploadPartResult uploadPartResult = task.getResult();
                        COSUploadTask.UploadPart uploadPart = new COSUploadTask.UploadPart(uploadPartResult.eTag,
                                uploadPartRequest.getPartNumber(), uploadPartRequest.getFileOffset(), uploadPartRequest.getFileContentLength());
                        uploadParts.add(uploadPart);
                        mergeTaskMetrics(uploadPartRequest.getMetrics());
                    }

                    // 全部任务都已经回调
                    if (count.get() == progress.addAndGet(1) && sendComplete.get()) {
                        mTaskCompletionSource.trySetResult(null);
                    }
                    return null;
                }
            });
            uploadPartRequest.setProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    updatePartProgress(uploadPartRequest, complete);
                }
            });
            TaskExecutors.UPLOAD_EXECUTOR.execute(uploadPartTask);
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
     *
     * 如果可用窗口为 0，会阻塞
     *
     * @return
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

        UploadPartRequest uploadPartRequest = null;
        if (filePath != null) {
            uploadPartRequest = new UploadPartRequest(bucket, key, partNumber, filePath, offset, sliceSize, mUploadId);
        } else if (uri != null) {
            uploadPartRequest = new UploadPartRequest(bucket, key, partNumber, uri, offset, sliceSize, mUploadId);
        }

        if (uploadPartRequest != null && !TextUtils.isEmpty(region)) {
            uploadPartRequest.setRegion(region);
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

        private bolts.TaskCompletionSource<UploadPartResult> tcs;
        private CancellationToken mCancellationToken;
        private CosXmlSimpleService cosXmlSimpleService;
        private UploadPartRequest uploadPartRequest;

        public UploadPartTask(CosXmlSimpleService cosXmlSimpleService, UploadPartRequest uploadPartRequest, CancellationToken cancellationToken) {
            this.cosXmlSimpleService = cosXmlSimpleService;
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
                tcs.setResult(cosXmlSimpleService.uploadPart(uploadPartRequest));
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
            cosXmlSimpleService.cancel(uploadPartRequest);
        }

        @Override
        public int compareTo(UploadPartTask o) {
            return 0;
        }
    }

    // 发送窗口   [start, next)
    // 可用窗口   [next, start+width)
    static class SlidingWindow {

        private int mWidth;
        private int mStart = 0;
        private int mNext = 0;
        private boolean[] completes;
        

        SlidingWindow(int start, int width) {
            mStart = start;
            mNext = start;
            mWidth = width;
            completes = new boolean[mWidth];
        }

        synchronized int getNextNumber() {
            while (mNext >= mStart + mWidth) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return mNext++;
        }

        synchronized void complete(int number) {

            completes[number - mStart] = true;

            //
            int delta = 0;
            for (int i = 0; i < completes.length; i++) {
                if (!completes[i]) {
                    break;
                }
                delta++;
            }

            if (delta > 0) {
                mStart += delta;
                // 左移 delta
                int size = completes.length;
                for (int i = 0; i < size - delta; i++) {
                    completes[i] = completes[i+delta];
                }
                for (int i = size - delta; i < size; i++) {
                    completes[i] = false;
                }

                notifyAll();
            }
        }


    }

}
