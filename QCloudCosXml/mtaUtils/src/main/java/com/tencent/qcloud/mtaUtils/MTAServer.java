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

package com.tencent.qcloud.mtaUtils;

import android.content.Context;
import android.util.Log;

import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatServiceImpl;
import com.tencent.stat.StatSpecifyReportedInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class MTAServer {
    private static final String TAG = "MTAServer";
    private String mtaAppKey = "AX23PXJD16SJ";
    private String sendEventId = "request_sent";
    private String failEventId = "request_fail";
    private String errorParameterName = "error";
    private String classParameterName = "class_name";
    private String sdkVersion = "1.0";
    private String sdkChannel = "COS-ANDROID-SDK";
    private Context applicationContext;
    private ThreadLocal<DateFormat> dateFormatThreadLocal = new ThreadLocal<>();
    private String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private StatSpecifyReportedInfo sdkReportInfo;
    public String fmtTime(long secondMillis){
        DateFormat dateFormat = dateFormatThreadLocal.get();
        if(dateFormat == null){
            dateFormat = new SimpleDateFormat(TIME_FORMAT, Locale.ENGLISH);
            dateFormatThreadLocal.set(dateFormat);
        }
        return dateFormat.format(new Date(secondMillis));
    }

    public MTAServer(Context applicationContext, String sdkVersion){
        this.applicationContext = applicationContext;
        this.sdkVersion = sdkVersion;
        sdkReportInfo = new StatSpecifyReportedInfo();
        sdkReportInfo.setAppKey(mtaAppKey);
        sdkReportInfo.setInstallChannel(sdkChannel);
        sdkReportInfo.setVersion(this.sdkVersion);
//        sdkReportInfo.setSendImmediately(true);
        StatConfig.setMaxBatchReportCount(30);
        StatConfig.setEnableSmartReporting(false);
        StatConfig.setStatSendStrategy(StatReportStrategy.BATCH);
        Log.d(TAG, "MTAServer instance success: (" + this.sdkVersion + ", " + this.sdkChannel + ")" );
    }
    
    /**
     * report event for failed
     * @param className: class name
     * @param errorMsg: error message
     */
    public void reportFailedEvent(String className, String errorMsg){
        if(className != null && errorMsg != null){
            Properties properties = new Properties();
            properties.setProperty(classParameterName, className);
            properties.setProperty(errorParameterName, errorMsg);
            StatServiceImpl.trackCustomKVEvent(applicationContext, failEventId, properties, sdkReportInfo);
            Log.d(TAG, className + ": " + errorMsg);
        }
    }

    /**
     *  report event for sending
     * @param className:  upload or download or copy
     */
    public void reportSendEvent(String className){
        if(className != null){
            Properties properties = new Properties();
            properties.setProperty(classParameterName, className);
            StatServiceImpl.trackCustomKVEvent(applicationContext, sendEventId, properties, sdkReportInfo);
            Log.d(TAG, className + ": " + className);
        }
    }
}
