package com.tencent.qcloud.core.http;

import android.util.Log;

import com.tencent.qcloud.core.logger.QCloudLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/12/17.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class HttpConfiguration {

    private static final AtomicLong GLOBAL_TIME_OFFSET = new AtomicLong(0);

    private static final String RFC822_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
    private static ThreadLocal<SimpleDateFormat> gmtFormatters = new ThreadLocal<>();

    public static long calculateGlobalTimeOffset(String sDate, Date deviceDate) {
        long offset = GLOBAL_TIME_OFFSET.get();
        calculateGlobalTimeOffset(sDate, deviceDate, 0);
        return Math.abs(offset - GLOBAL_TIME_OFFSET.get());
    }

    public static void calculateGlobalTimeOffset(String sDate, Date deviceDate, int minOffset) {
        try {
            Date serverDate = getFormatter().parse(sDate);
            long clockSkew = (serverDate.getTime() - deviceDate.getTime()) / 1000;
            if (Math.abs(clockSkew) >= minOffset) {
                GLOBAL_TIME_OFFSET.set(clockSkew);
                QCloudLogger.i(QCloudHttpClient.HTTP_LOG_TAG, "NEW TIME OFFSET is " + clockSkew + "s");
            }
        } catch (ParseException e) {
            // parse error, ignored
        }
    }

    public static long getDeviceTimeWithOffset() {
        long current = System.currentTimeMillis() / 1000 + GLOBAL_TIME_OFFSET.get();
        return current;
    }

    public static String getGMTDate(Date date) {
        return getFormatter().format(date);
    }

    private static SimpleDateFormat getFormatter() {
        SimpleDateFormat gmtFormatter = gmtFormatters.get();
        if (gmtFormatter == null) {
            gmtFormatter = new SimpleDateFormat(RFC822_DATE_PATTERN, Locale.US);
            gmtFormatter.setTimeZone(GMT_TIMEZONE);
            gmtFormatter.setLenient(false);
            gmtFormatters.set(gmtFormatter);
        }
        return gmtFormatter;
    }

    public static Date getGMTDate(String strDate){
        try {
            return getFormatter().parse(strDate);
        } catch (ParseException e) {
//            e.printStackTrace();
            return null;
        }
    }
}
