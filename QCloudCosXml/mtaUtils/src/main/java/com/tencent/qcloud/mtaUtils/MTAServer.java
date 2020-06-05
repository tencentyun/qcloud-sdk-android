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

/**
 * Created by bradyxiao on 2018/9/26.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 * report 2 event:
 * 1, class (parameter: class_name, value: class name);
 * 2, error(parameter: error, value: http code_ error code or client code _ exception name.
 */

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
