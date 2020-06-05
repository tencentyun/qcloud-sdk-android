package com.tencent.cos.xml.common;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.qcloud.core.task.TaskExecutors;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Time;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/3/14.
 */
@RunWith(AndroidJUnit4.class)
public class VersionInfoTest {

    @Test
    public void test() throws Exception{
        assertEquals(VersionInfo.platform , VersionInfo.getUserAgent());
    }

    @Test
    public void testPriorityQueue() {
        Executor executor = new ThreadPoolExecutor(3, 3, 5L,
                TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(128));
        for (int i = 0; i < 30; i++) {
            executor.execute(new AtomTask(i % 3));
        }

        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class AtomTask implements Runnable, Comparable<Runnable> {

        private int priority;
        private int taskIdentifier;
        private static AtomicInteger increment = new AtomicInteger(0);

        public AtomTask(int priority) {
            this.priority = priority;
            this.taskIdentifier = increment.addAndGet(1);
        }

        @Override
        public void run() {
            Log.d("UnitTest", "execute task " + taskIdentifier + " p : " + priority);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int compareTo(Runnable o) {
            if (o instanceof AtomTask) {
                int priorityDelta =  ((AtomTask) o).priority - priority;
                if (priorityDelta != 0) {
                    return priorityDelta;
                } else {
                    return taskIdentifier - ((AtomTask) o).taskIdentifier;
                }
            }

            return 0;
        }
    }
}