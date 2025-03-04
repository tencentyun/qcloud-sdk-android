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

package com.tencent.cos.xml.transfer;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosTrackService;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.utils.COSUtils;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.cos.xml.utils.FileUtils;
import com.tencent.qcloud.core.common.QCloudTaskStateListener;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.QCloudTask;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 下载传输任务
 */
public final class COSXMLDownloadTask extends COSXMLTask{

    private final static String TAG = COSXMLUploadTask.class.getSimpleName();

    private String localSaveDirPath;
    private String localSaveFileName;

    /** 下载 */
    private long rangeStart = 0L;
    private long rangeEnd = -1L;
    private long fileOffset = 0L;
    private boolean objectKeySimplifyCheck = true;
    private String eTag;
    private long hasWriteDataLen = 0L;
    private long startTime = 0L;
    private HeadObjectRequest headObjectRequest;
    private GetObjectRequest getObjectRequest;
    private SharedPreferences sharedPreferences;
    /**
     * 标记一次请求的客户端唯一标识，用于日志上报和本地日志记录
     */
    private String clientTraceId;


    COSXMLDownloadTask(Context context, CosXmlSimpleService cosXmlService, String region, String bucket, String cosPath, String localSaveDirPath, String localSaveFileName){
        this.region = region;
        this.bucket = bucket;
        this.cosPath = cosPath;
        this.localSaveDirPath = localSaveDirPath;
        this.localSaveFileName = localSaveFileName;
        this.cosXmlService = cosXmlService;
        if(context != null){
            sharedPreferences = context.getSharedPreferences("COSXMLDOWNLOADTASK", 0);
        }
        this.clientTraceId = UUID.randomUUID().toString();
    }

    COSXMLDownloadTask(Context context, CosXmlSimpleService cosXmlService, GetObjectRequest getObjectRequest){

        this(context, cosXmlService, getObjectRequest.getRegion(), getObjectRequest.getBucket(),
                getObjectRequest.getPath(cosXmlService.getConfig()), getObjectRequest.getSavePath(), getObjectRequest.getSaveFileName());
        this.queries = getObjectRequest.getQueryString();
        this.headers = getObjectRequest.getRequestHeaders();
        this.noSignHeaders = getObjectRequest.getNoSignHeaders();
        this.networkType = getObjectRequest.getNetworkType();
        this.host = getObjectRequest.getHost();
        this.credentialProvider = getObjectRequest.getCredentialProvider();
        this.objectKeySimplifyCheck = getObjectRequest.isObjectKeySimplifyCheck();
        this.isNeedMd5 = getObjectRequest.isNeedMD5();
        //需要取出range字段
        if(this.headers != null && this.headers.containsKey(COSRequestHeaderKey.RANGE)){
            List<String> ranges = this.headers.get(COSRequestHeaderKey.RANGE);
            String range = ranges.get(0);
            int index1 = range.indexOf("=");
            int index2 = range.indexOf("-");
            rangeStart = Long.valueOf(range.substring(index1 + 1, index2));
            String end = range.substring(index2 + 1);
            if(!TextUtils.isEmpty(end)){
                rangeEnd = Long.valueOf(end);
            }
        }
        this.fileOffset = getObjectRequest.getFileOffset();
    }

    private boolean checkParameter(){
        if(objectKeySimplifyCheck) {
            String normalizedPath = cosPath;
            try {
                File file = new File("/" + cosPath);
                normalizedPath = file.getCanonicalPath();
            } catch (IOException e) {e.printStackTrace();}
            if ("/".equals(normalizedPath)) {
                updateState(TransferState.FAILED, new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "The key in the getobject is illegal"), null, false);
                return false;
            }
        }
        return true;
    }

    /**
     * 下载操作
     */
    protected void download(){
        if(!checkParameter()) return;
        startTime = System.nanoTime();
        this.clientTraceId = UUID.randomUUID().toString();
        run();
    }

    private void realDownload(final long rangeStart, final long rangeEnd, final long fileOffset){
        getObjectRequest = new GetObjectRequest(bucket, cosPath, localSaveDirPath, localSaveFileName);
        getObjectRequest.setRegion(region);
        getObjectRequest.setFileOffset(fileOffset);
        getObjectRequest.setQueryParameters(queries);
        getObjectRequest.setRequestHeaders(headers);
        getObjectRequest.setObjectKeySimplifyCheck(objectKeySimplifyCheck);
        getObjectRequest.addNoSignHeader(noSignHeaders);
        getObjectRequest.setNetworkType(networkType);
        getObjectRequest.setHost(host);
        getObjectRequest.setCredentialProvider(credentialProvider);
        if(rangeEnd > 0 || rangeStart > 0){
            getObjectRequest.setRange(rangeStart, rangeEnd);
            //下载重试时会改变request的range，因此不能让range参与签名
            getObjectRequest.addNoSignHeader(COSRequestHeaderKey.RANGE);
        }

        if(onSignatureListener != null){
            getObjectRequest.setSign(onSignatureListener.onGetSign(getObjectRequest));
        }

        getHttpMetrics(getObjectRequest, "GetObjectRequest");

        getObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                if(cosXmlProgressListener != null){
                    cosXmlProgressListener.onProgress(hasWriteDataLen + complete, hasWriteDataLen + target);
                }
            }
        });
        getObjectRequest.setClientTraceId(this.clientTraceId);
        cosXmlService.internalGetObjectAsync(getObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if(request != getObjectRequest){
                    return;
                }
                CosTrackService.getInstance().reportDownloadTaskSuccess(getObjectRequest, getCosXmlServiceConfigTrackParams());

                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                updateState(TransferState.COMPLETED, null, result, false);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if(request != getObjectRequest){
                    return;
                }

                Exception causeException = null;
                if (clientException != null && taskState != TransferState.PAUSED && taskState != TransferState.CANCELED) {
                    CosTrackService.getInstance().reportDownloadTaskClientException(request, clientException, getCosXmlServiceConfigTrackParams());
                    causeException = clientException;
                }

                if (serviceException != null && taskState != TransferState.PAUSED && taskState != TransferState.CANCELED) {
                    // BeaconService.getInstance().reportDownload(region, BeaconService.EVENT_PARAMS_NODE_GET, serviceException);
                    // CosTrackService.getInstance().reportDownload(region, CosTrackService.EVENT_PARAMS_NODE_GET, serviceException);
                    CosTrackService.getInstance().reportDownloadTaskServiceException(request, serviceException, getCosXmlServiceConfigTrackParams());
                    causeException = serviceException;
                }
                // causeException.printStackTrace();

                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                updateState(TransferState.FAILED, causeException, null, false);
            }
        });
    }


    @Override
    protected CosXmlRequest buildCOSXMLTaskRequest() {
        COSXMLDownloadTaskRequest cosxmlDownloadTaskRequest = new COSXMLDownloadTaskRequest(region, bucket, cosPath,
                localSaveDirPath, localSaveFileName, headers, queries);
       return cosxmlDownloadTaskRequest;
    }

    @Override
    protected CosXmlResult buildCOSXMLTaskResult(CosXmlResult sourceResult) {
        COSXMLDownloadTaskResult cosxmlDownloadTaskResult = new COSXMLDownloadTaskResult();
        if(sourceResult != null){
            cosxmlDownloadTaskResult.httpCode = sourceResult.httpCode;
            cosxmlDownloadTaskResult.httpMessage = sourceResult.httpMessage;
            cosxmlDownloadTaskResult.headers = sourceResult.headers;
            cosxmlDownloadTaskResult.eTag = eTag;
            cosxmlDownloadTaskResult.accessUrl = sourceResult.accessUrl;
        }
        return cosxmlDownloadTaskResult;
    }

    private String getKey(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("download").append("_")
                .append(region).append("_")
                .append(bucket).append("_")
                .append(cosPath).append("_")
                .append(rangeStart).append("_")
                .append(rangeEnd).append("_")
                .append(fileOffset).append("_")
                .append(localSaveDirPath).append("_")
                .append(localSaveFileName).append("_")
                .append(eTag);
        try {
            return DigestUtils.getSha1(stringBuffer.toString());
        } catch (CosXmlClientException e) {
            return stringBuffer.toString();
        }
    }

    /**
     * 第一次下载时，如果当前路径下文件已存在，则先删除
     *
     * @return
     */
//    private synchronized String removeIfExist(){
//        if(sharedPreferences != null){
//
//            String filePath = sharedPreferences.getString(getKey(), null);
//            if (filePath == null) { // 第一次下载
//                FileUtils.deleteFileIfExist(getDownloadPath());
//            }
//            return filePath;
//        }
//        return null;
//    }

    private synchronized String hasExisted(){
        if(sharedPreferences != null){
            return sharedPreferences.getString(getKey(), null);
        }
        return null;
    }

    private synchronized void clear(){
        if(sharedPreferences != null){
            sharedPreferences.edit().remove(getKey()).commit();
        }
    }

    private void cancelAllRequest(boolean now){
        HeadObjectRequest tempHeadObjectRequest = headObjectRequest;
        cosXmlService.cancel(tempHeadObjectRequest, now);
        GetObjectRequest tempGetObjectRequest = getObjectRequest;
        cosXmlService.cancel(tempGetObjectRequest, now);
    }

    private synchronized void save(String absolutePath){

        if(sharedPreferences != null){
            sharedPreferences.edit().putString(getKey(), absolutePath).commit();
        }
    }
    private String getDownloadPath(){
        String path  = null;
        if(localSaveDirPath != null){
            if(!localSaveDirPath.endsWith("/")){
                path = localSaveDirPath + "/";
            }else{
                path = localSaveDirPath;
            }
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            if (localSaveFileName != null){
                path = path + localSaveFileName;
                return path;
            }
            if(cosPath != null){
                int separator = cosPath.lastIndexOf("/");
                if(separator >= 0){
                    path = path + cosPath.substring(separator + 1);
                }else{
                    path = path + cosPath;
                }
            }
        }
        return path;
    }

    protected void run() {

        headObjectRequest = new HeadObjectRequest(bucket, cosPath);
        headObjectRequest.setRequestHeaders(headers);
        headObjectRequest.addNoSignHeader(noSignHeaders);
        headObjectRequest.setNetworkType(networkType);
        headObjectRequest.setHost(host);
        headObjectRequest.setCredentialProvider(credentialProvider);
        headObjectRequest.setQueryParameters(queries);
        headObjectRequest.setRegion(region);
        final String downloadPath = getDownloadPath();

        if(onSignatureListener != null){
            headObjectRequest.setSign(onSignatureListener.onGetSign(headObjectRequest));
        }

        getHttpMetrics(headObjectRequest, "HeadObjectRequest");

        headObjectRequest.setTaskStateListener(new QCloudTaskStateListener() {
            @Override
            public void onStateChanged(String taskId, int state) {
                if(IS_EXIT.get())return;
                if (state != QCloudTask.STATE_QUEUEING) {
                    updateState(TransferState.IN_PROGRESS, null, null, false);
                }
            }
        });
        headObjectRequest.setClientTraceId(this.clientTraceId);

        cosXmlService.headObjectAsync(headObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if(request != headObjectRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                List<String> eTags = result.headers.get("ETag");
                if(eTags != null && eTags.size() > 0){
                    eTag = eTags.get(0);
                }
                String absolutePath = hasExisted();
                if(absolutePath != null){
                    File file = new File(absolutePath);
                    if(file.exists()){
                        long fileLength = file.length();
                        List<String> contentLengths = result.headers.get("Content-Length");
                        if(contentLengths != null && contentLengths.size() > 0 && Long.valueOf(contentLengths.get(0)) == fileLength){
                            if(cosXmlProgressListener != null){
                                cosXmlProgressListener.onProgress(fileLength, fileLength);
                            }
                            IS_EXIT.set(true);
                            updateState(TransferState.COMPLETED, null, result, false);
                            return;
                        }else {
                            hasWriteDataLen = fileLength - fileOffset;
                            realDownload(rangeStart + hasWriteDataLen, rangeEnd, fileOffset + hasWriteDataLen);
                            return;
                        }
                    }
                }
                // 第一次下载文件，会先删除本地文件
                FileUtils.deleteFileIfExist(downloadPath);
                save(getDownloadPath()); // 第一次下载
                hasWriteDataLen = 0L;
                realDownload(rangeStart, rangeEnd, fileOffset);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if(request != headObjectRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                Exception causeException = clientException == null ? serviceException : clientException;
                causeException.printStackTrace();
                updateState(TransferState.FAILED, causeException, null, false);
                QCloudLogger.i(TAG, "head " + cosPath + "failed !, exception is " + causeException.getMessage());
            }
        });
    }

    @Override
    protected void internalCompleted() {
        clear();
    }

    @Override
    protected void internalFailed() {
        cancelAllRequest(false);
    }

    @Override
    protected void internalPause(boolean now) {
        if (getObjectRequest != null) {
            CosTrackService.getInstance().reportDownloadTaskSuccess(getObjectRequest, getCosXmlServiceConfigTrackParams());
        }
        cancelAllRequest(now);
    }

    @Override
    protected void internalCancel(boolean now) {
        cancelAllRequest(now);
        clear();
    }

    @Override
    protected void internalResume() {
        taskState = TransferState.WAITING;
        IS_EXIT.set(false);
        download();
    }

    @Override
    protected void encounterError(@Nullable CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
        if(IS_EXIT.get())return;
        IS_EXIT.set(true);
        updateState(TransferState.FAILED, COSUtils.mergeException(clientException, serviceException), null, false);
    }

    /**
     * 下载传输任务的请求
     */
    public static class COSXMLDownloadTaskRequest extends GetObjectRequest{
        protected COSXMLDownloadTaskRequest(String region, String bucket, String cosPath, String savePath, String saveFileName, Map<String, List<String>> headers,
                                            Map<String, String> queryStr){
            super(bucket, cosPath, savePath, saveFileName);
            this.setRegion(region);
            this.setRequestHeaders(headers);
            this.setQueryParameters(queryStr);
        }
    }

    /**
     * 下载传输任务的返回结果
     */
    public static class COSXMLDownloadTaskResult extends CosXmlResult{
        protected COSXMLDownloadTaskResult(){}
        public String eTag;
    }
}
