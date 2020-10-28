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

package com.tencent.qcloud.quic.test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 开启一个线程池跑request
 */
public class QuicManager {

    private ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new DefaultThreadFactory("COS_TQUIC", false));
    private ConnectPool connectPool = new ConnectPool();

    public void init(boolean isEnableDebugLog){
        connectPool.init(isEnableDebugLog);
        QLog.isDebug = isEnableDebugLog;
    }

    public void execute(QuicRequest quicRequest, QuicResponse quicResponse) throws QuicException{
        QuicImpl quic = new QuicImpl(connectPool);
        quic.setQuicRequest(quicRequest);
        quic.setQuicResponse(quicResponse);
        Future<QuicResponse> future = executorService.submit(quic);
        try {
            future.get();
        } catch (ExecutionException e) {
            throw new QuicException(e);
        } catch (InterruptedException e) {
            throw new QuicException(e);
        }
    }

    public void enqueue(QuicRequest quicRequest, QuicResponse quicResponse, ResultCallback callback){
        QuicImpl quic = new QuicImpl(connectPool);
        quic.setQuicRequest(quicRequest);
        quic.setQuicResponse(quicResponse);
        quic.setResultCallback(callback);
        executorService.submit(quic);
    }

    public void destroy(){
        connectPool.destroy();
    }


    private static class DefaultThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private String namePrefix;
        private boolean isDaemon;

        DefaultThreadFactory(String namePrefix, boolean isDaemon) {
           this.namePrefix = namePrefix;
           this.isDaemon = isDaemon;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r,
                    namePrefix + "-" + threadNumber.getAndIncrement());
            t.setDaemon(isDaemon);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

}
