package com.tencent.cos.xml;

import android.content.Context;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.util.QCloudHttpUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;


/**
 * Created by bradyxiao on 2018/9/26.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */

public class MTAProxy {
    private static final String TAG = "MTAProxy";
    private final String className = "com.tencent.qcloud.mtaUtils.MTAServer";
    private Object mtaServer;
    private Method reportFailedEvent, reportSendEvent;
    private Context applicationContext;
    private static MTAProxy instance;

    private MTAProxy(Context applicationContext){
        this.applicationContext = applicationContext;
        try {
            Class cls = Class.forName(className);
            Constructor constructor = cls.getConstructor(android.content.Context.class, java.lang.String.class);
            if(constructor != null){
                mtaServer = constructor.newInstance(this.applicationContext, BuildConfig.VERSION_NAME);
            }
            reportFailedEvent = cls.getDeclaredMethod("reportFailedEvent", java.lang.String.class, java.lang.String.class);
            if(reportFailedEvent != null){
                reportFailedEvent.setAccessible(true);
            }
            reportSendEvent = cls.getDeclaredMethod("reportSendEvent", java.lang.String.class);
            if(reportSendEvent != null){
                reportSendEvent.setAccessible(true);
            }
        } catch (ClassNotFoundException e) {
            QCloudLogger.d(TAG, className + " : not found");
        } catch (NoSuchMethodException e) {
            QCloudLogger.d(TAG, e.getMessage() + " : not found");
        } catch (InstantiationException e) {
            QCloudLogger.d(TAG, e.getMessage() + " : not found");
        } catch (IllegalAccessException e) {
            QCloudLogger.d(TAG, e.getMessage() + " : not found");
        } catch (InvocationTargetException e) {
            QCloudLogger.d(TAG, e.getMessage() + " : not found");
        }
    }

    public static void init(Context applicationContext){
        synchronized (MTAProxy.class){
            if(instance == null){
                instance = new MTAProxy(applicationContext);
            }
        }
    }

    public static MTAProxy getInstance(){
        return instance;
    }

    public CosXmlClientException reportXmlClientException(Object cosXmlRequest, QCloudClientException e) {
        CosXmlClientException xmlClientException;
        if (e instanceof CosXmlClientException) {
            xmlClientException = (CosXmlClientException) e;
        } else {
            Throwable causeException = e.getCause();
            if (causeException instanceof IllegalArgumentException) {
                xmlClientException = new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
            } else if (causeException instanceof QCloudAuthenticationException) {
                xmlClientException = new CosXmlClientException(ClientErrorCode.INVALID_CREDENTIALS.getCode(), e);
            } else if (causeException instanceof IOException) {
                ClientErrorCode code;
                if (causeException instanceof FileNotFoundException) {
                    code = ClientErrorCode.SINK_SOURCE_NOT_FOUND;
                } else if (QCloudHttpUtils.isNetworkConditionException(causeException)) {
                    code = ClientErrorCode.POOR_NETWORK;
                } else {
                    code = ClientErrorCode.IO_ERROR;
                }
                xmlClientException = new CosXmlClientException(code.getCode(), e);
            } else {
                xmlClientException = new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
            }
        }
        String reportMessage = String.format(Locale.ENGLISH, "%d %s", xmlClientException.errorCode, e.getCause() == null ?
                e.getClass().getSimpleName() : e.getCause().getClass().getSimpleName());
        reportErrorAction(cosXmlRequest.getClass().getSimpleName(), reportMessage, xmlClientException);

        return xmlClientException;
    }

    public CosXmlServiceException reportXmlServerException(Object cosXmlRequest, QCloudServiceException e){
        CosXmlServiceException serviceException = e instanceof CosXmlServiceException ? (CosXmlServiceException) e
                : new CosXmlServiceException(e);
        reportErrorAction(cosXmlRequest.getClass().getSimpleName(),
                serviceException.getStatusCode() + " " + serviceException.getErrorCode(), serviceException);

        return serviceException;
    }


    /**
     * key - exception message
     * key: class name
     * errorMsg: error code and error msg
     **/
    private void reportErrorAction(String requestClassName, String errorMsg, Exception ex){
        if(mtaServer != null && reportFailedEvent != null && isRequestNeedReport(requestClassName) &&
            isExceptionInterested(ex)){
            try {
                reportFailedEvent.invoke(mtaServer, requestClassName, errorMsg);
            } catch (IllegalAccessException e) {
                QCloudLogger.d(TAG, e.getMessage());
            } catch (InvocationTargetException e) {
                QCloudLogger.d(TAG, e.getMessage());
            }
        }
    }

    /**
     * report send action
     * @param requestClassName class name
     */
    public void reportSendAction(String requestClassName){
        if(mtaServer != null && reportSendEvent != null && isRequestNeedReport(requestClassName)){
            try {
                reportSendEvent.invoke(mtaServer, requestClassName);
            } catch (IllegalAccessException e) {
                QCloudLogger.e(TAG, e.getMessage());
            } catch (InvocationTargetException e) {
                QCloudLogger.e(TAG, e.getMessage());
            }
        }
    }

    private boolean isRequestNeedReport(String requestClassName) {
        return Arrays.asList(
                "HeadObjectRequest",
                "PutObjectRequest",
                "GetObjectRequest",
                "UploadPartRequest",
                "UploadPartCopyRequest",
                "CopyObjectRequest").contains(requestClassName);
    }

    private boolean isExceptionInterested(Exception ex) {
        if (ex instanceof CosXmlClientException) {
            int errorCode = ((CosXmlClientException) ex).errorCode;
            return errorCode == ClientErrorCode.POOR_NETWORK.getCode() ||
                    errorCode == ClientErrorCode.SERVERERROR.getCode() ||
                    errorCode == ClientErrorCode.INTERNAL_ERROR.getCode();
        } else if (ex instanceof CosXmlServiceException) {
            String serverCode = ((CosXmlServiceException) ex).getErrorCode();
            return Arrays.asList(
                    "InvalidDigest",
                    "BadDigest",
                    "InvalidSHA1Digest",
                    "RequestTimeOut").contains(serverCode);
        }

        return false;
    }

}
