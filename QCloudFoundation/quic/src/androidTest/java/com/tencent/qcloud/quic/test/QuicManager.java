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
