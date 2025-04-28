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

package com.tencent.qcloud.core.http;

import com.tencent.qcloud.core.logger.COSLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

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
                COSLogger.iNetwork(QCloudHttpClient.HTTP_LOG_TAG, "NEW TIME OFFSET is " + clockSkew + "s");
            }
        } catch (ParseException e) {
            // parse error, ignored
        }
    }

    public static long getDeviceTimeWithOffset() {
//        long current = System.currentTimeMillis() / 1000 + GLOBAL_TIME_OFFSET.get();
//        return current;
        // 去掉本地时间校准（有一些Tencent Server返回的date并不准确）
        return System.currentTimeMillis() / 1000;
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
