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

package com.tencent.qcloud.core.http.interceptor;

import static com.tencent.qcloud.core.http.QCloudHttpClient.HTTP_LOG_TAG;

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.http.HttpUtil;
import com.tencent.qcloud.core.http.NetworkProxy;
import com.tencent.qcloud.core.logger.COSLogger;
import com.tencent.qcloud.core.task.TaskExecutors;
import com.tencent.qcloud.core.task.TaskManager;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Request;
import okhttp3.Response;

public class QCloudTrafficControlInterceptor {
    private TrafficStrategy uploadTrafficStrategy = new ModerateTrafficStrategy("UploadStrategy-", TaskExecutors.UPLOAD_THREAD_COUNT);
    private TrafficStrategy downloadTrafficStrategy = new AggressiveTrafficStrategy("DownloadStrategy-", TaskExecutors.DOWNLOAD_THREAD_COUNT);

    private static class ResizableSemaphore extends Semaphore {

        ResizableSemaphore(int permit, boolean fair) {
            super(permit, fair);
        }

        @Override
        protected void reducePermits(int reduction) {
            super.reducePermits(reduction);
        }
    }

    /**
     * 流量控制策略
     */
    private abstract static class TrafficStrategy {

        private final int maxConcurrent;
        private final String name;

        static final int SINGLE_THREAD_SAFE_SPEED = 100; // 单请求安全速度 100KB/S
        static final long BOOST_MODE_DURATION = TimeUnit.SECONDS.toNanos(3);

        private ResizableSemaphore controller;
        private AtomicInteger concurrent;
        private long boostModeExhaustedTime; // 多线程模式结束时间

        TrafficStrategy(String name, int concurrent, int maxConcurrent) {
            this.name = name;
            this.maxConcurrent = maxConcurrent;
            controller = new ResizableSemaphore(concurrent, true);
            this.concurrent = new AtomicInteger(concurrent);
            COSLogger.dProcess(HTTP_LOG_TAG, name + " init concurrent is " + concurrent);
        }

        void reportException(Request request, IOException exception) {
            controller.release();
        }

        void reportTimeOut(Request request) {
            // 出现超时直接降到单线程
            adjustConcurrent(1, true);
        }

        void reportSpeed(Request request, double averageSpeed) {
            if (averageSpeed > 0) {
                COSLogger.dProcess(HTTP_LOG_TAG, name + " %s streaming speed is %1.3f KBps", request, averageSpeed);

                // 根据最新的平均速度切换并行任务个数
                int concurrent = this.concurrent.get();
                if (averageSpeed > 2.4 * SINGLE_THREAD_SAFE_SPEED && concurrent < maxConcurrent) {
                    // 达到安全速度2.4倍时，增加线程数
                    boostModeExhaustedTime = System.nanoTime() + BOOST_MODE_DURATION;
                    adjustConcurrent(concurrent + 1, true);
                } else if (averageSpeed > 1.2 * SINGLE_THREAD_SAFE_SPEED && boostModeExhaustedTime > 0) {
                    // 延续多线程模式
                    boostModeExhaustedTime = System.nanoTime() + BOOST_MODE_DURATION;
                    controller.release();
                } else if (averageSpeed > 0 && concurrent > 1 && averageSpeed < 0.7 * SINGLE_THREAD_SAFE_SPEED) {
                    // 低于安全速度的 70% 时，降低线程数
                    adjustConcurrent(concurrent - 1, true);
                } else {
                    controller.release();
                }
            } else {
                controller.release();
            }
        }

        void waitForPermit() {
            try {
                if (concurrent.get() > 1 && System.nanoTime() > boostModeExhaustedTime) {
                    adjustConcurrent(1, false);
                }
                controller.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private synchronized void adjustConcurrent(int expect, boolean release) {
            int current = concurrent.get();
            int delta = expect - current;
            if (delta == 0) {
                if (release) {
                    controller.release();
                }
            } else {
                concurrent.set(expect);
                if (delta > 0) {
                    if (release) {
                        controller.release(1 + delta);
                    }
                } else {
                    delta *= -1;
                    controller.reducePermits(delta);
                    if (release) {
                        controller.release();
                    }
                }
                COSLogger.dProcess(HTTP_LOG_TAG, name + "set concurrent to " + expect);
            }
        }
    }

    /**
     * 激进的流量控制策略
     */
    private static class AggressiveTrafficStrategy extends TrafficStrategy {
        AggressiveTrafficStrategy(String name, int maxConcurrent) {
            super(name, maxConcurrent, maxConcurrent);
        }
    }

    /**
     * 保守的流量控制策略
     */
    private static class ModerateTrafficStrategy extends TrafficStrategy {
        ModerateTrafficStrategy(String name, int maxConcurrent) {
            super(name, 1, maxConcurrent);
        }
    }

    private TrafficStrategy getSuitableStrategy(HttpTask task) {
        if (!task.isEnableTraffic()) {
            return null;
        }
        if(task.isDownloadTask()){
            return downloadTrafficStrategy;
        } else if(task.isUploadTask()){
            return uploadTrafficStrategy;
        } else {
            return null;
        }
    }

    private double getAverageStreamingSpeed(HttpTask task, long networkMillsTook) {
        // unit: KB/s
        if (networkMillsTook == 0) {
            return 0;
        }
        return ((double) task.getTransferBodySize() / 1024) / ((double) networkMillsTook / 1000);
    }

    public Response intercept(NetworkProxy networkProxy, Request request) throws IOException {
        HttpTask task = (HttpTask) TaskManager.getInstance().get((String) request.tag());

        if (task == null || task.isCanceled()) {
            throw new IOException("CANCELED");
        }

        TrafficStrategy strategy = getSuitableStrategy(task);

        // wait for traffic control if necessary
        if (strategy != null) {
            strategy.waitForPermit();
        }
        COSLogger.iNetwork(HTTP_LOG_TAG, " %s begin to execute", request);
        IOException e;
        Response response = null;
        try {
            long startNs = System.nanoTime();
            response = processRequest(networkProxy, request);
            // because we want to calculate the whole task duration, including downloading procedure,
            // we put download operation here
            if (task.isDownloadTask()) {
                task.convertResponse(response);
            }
            if (strategy != null) {
                if (response.isSuccessful()) {
                    long networkMillsTook = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
                    strategy.reportSpeed(request, getAverageStreamingSpeed(task, networkMillsTook));
                } else {
                    strategy.reportException(request, null);
                }
            }
            return response;
        }  catch (QCloudClientException e1) {
            e = e1.getCause() instanceof IOException ? (IOException) e1.getCause() : new IOException(e1);
        } catch (QCloudServiceException e2) {
            e = e2.getCause() instanceof IOException ? (IOException) e2.getCause() : new IOException(e2);
        } catch (IOException exception) {
            e = exception;
        } catch (Exception exception){
            // 捕获更大范围的异常，但是因为intercept定义只能抛出IOException，这里进行转换
            // （进度回调中可能有异常）
            e = new IOException(exception);
        }

        if (strategy != null) {
            if (HttpUtil.isNetworkTimeoutError(e)) {
                strategy.reportTimeOut(request);
            } else {
                strategy.reportException(request, e);
            }
        }
        //解决okhttp 3.14 以上版本报错 cannot make a new request because the previous response is still open: please call response.close()
        if (response != null && response.body() != null) {
            response.close();
        }
        throw e;
    }

    private Response processRequest(NetworkProxy networkProxy, Request request) throws IOException {
        return networkProxy.callHttpRequest(request);
    }
}
