package com.tencent.cos.xml.core;

import java.util.concurrent.CountDownLatch;

public class TestLocker {

    private CountDownLatch locker;

    public TestLocker(int count) {
        locker = new CountDownLatch(count);
    }

    public TestLocker() {
        locker = new CountDownLatch(1);
    }

    public void lock() {
        try {
            locker.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void release() {

        if (locker != null && locker.getCount() > 0) {
            locker.countDown();
        }
    }

}
