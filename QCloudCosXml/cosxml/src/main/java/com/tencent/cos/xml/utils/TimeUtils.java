package com.tencent.cos.xml.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    private static final String TAG = "TimeUtil";

    public static long getTookTime(long start){
        return TimeUnit.MILLISECONDS.convert(System.nanoTime() - start,TimeUnit.NANOSECONDS);
    }
}
