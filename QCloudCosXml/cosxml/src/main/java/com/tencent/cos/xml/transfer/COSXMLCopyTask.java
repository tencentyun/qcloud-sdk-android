package com.tencent.cos.xml.transfer;


import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.CopyObjectResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.ListPartsResult;
import com.tencent.cos.xml.model.object.UploadPartCopyRequest;
import com.tencent.cos.xml.model.object.UploadPartCopyResult;
import com.tencent.cos.xml.model.tag.ListParts;
import com.tencent.qcloud.core.common.QCloudTaskStateListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bradyxiao on 2018/9/19.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */

public final class COSXMLCopyTask extends COSXMLTask {

    /** 是否分片拷贝Limit */
    protected long multiCopySizeDivision;
    /** 拷贝的数据源 */
    private CopyObjectRequest.CopySourceStruct copySourceStruct;
    /** 数据源的长度 */
    private long fileLength;
    /** 获取源文件属性 */
    private HeadObjectRequest headObjectRequest;

    /** 小文件拷贝 */
    private CopyObjectRequest copyObjectRequest;

    /** 大文件拷贝 */
    private boolean isLargeCopy = false;
    /** 分片uploadId 属性 */
    private String uploadId;
    /** 初始化分片上传 */
    private InitMultipartUploadRequest initMultipartUploadRequest;
    private ListPartsRequest listPartsRequest;
    protected long sliceSize;
    private Map<Integer, CopyPartStruct> copyPartStructMap; //必须有序
    private List<UploadPartCopyRequest> uploadPartCopyRequestList;
    /** 完成所有上传分片 */
    private CompleteMultiUploadRequest completeMultiUploadRequest;
    private AtomicBoolean IS_EXIT = new AtomicBoolean(false);
    private AtomicInteger UPLOAD_PART_COUNT;
    private Object SYNC_UPLOAD_PART = new Object();
    private LargeCopyStateListener largeCopyStateListenerHandler = new LargeCopyStateListener(){
        @Override
        public void onInit() {
            uploadPartCopy(cosXmlService);
        }

        @Override
        public void onListParts() {
            uploadPartCopy(cosXmlService);
        }

        @Override
        public void onUploadPartCopy() {
            completeMultiUpload(cosXmlService);
        }

        @Override
        public void onCompleted(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
            updateState(TransferState.COMPLETED, null, cosXmlResult, false);
        }

        @Override
        public void onFailed(CosXmlRequest cosXmlRequest, CosXmlClientException exception, CosXmlServiceException serviceException) {
            Exception causeException = exception == null ? serviceException : exception;
            updateState(TransferState.FAILED, causeException, null, false);
        }
    };

    COSXMLCopyTask( CosXmlSimpleService cosXmlService, String region,String bucket, String cosPath,
                          CopyObjectRequest.CopySourceStruct copySourceStruct){
        this.cosXmlService = cosXmlService;
        this.region = region;
        this.bucket = bucket;
        this.cosPath = cosPath;
        this.copySourceStruct = copySourceStruct;
    }

    COSXMLCopyTask( CosXmlSimpleService cosXmlService, CopyObjectRequest copyObjectRequest){
        this(cosXmlService, copyObjectRequest.getRegion(), copyObjectRequest.getBucket(), copyObjectRequest.getPath(cosXmlService.getConfig()),
                copyObjectRequest.getCopySource());
        this.queries = copyObjectRequest.getQueryString();
        this.headers = copyObjectRequest.getRequestHeaders();
        this.isNeedMd5 = copyObjectRequest.isNeedMD5();
    }

    protected void copy(){
        run();
    }

    private void smallFileCopy(){
        copyObjectRequest = new CopyObjectRequest(bucket, cosPath, copySourceStruct);
        copyObjectRequest.setRegion(region);

        copyObjectRequest.setRequestHeaders(headers);

        if(onSignatureListener != null){
            copyObjectRequest.setSign(onSignatureListener.onGetSign(copyObjectRequest));
        }

        getHttpMetrics(copyObjectRequest, "CopyObjectRequest");

        cosXmlService.copyObjectAsync(copyObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if(request != copyObjectRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                updateState(TransferState.COMPLETED, null, result, false);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                if(request != copyObjectRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                Exception causeException = exception == null ? serviceException : exception;
                updateState(TransferState.FAILED, causeException, null, false);
            }
        });
    }

    private void largeFileCopy(CosXmlSimpleService cosXmlService){
        initCopyPart();
        if(uploadId == null){
            //重新跑
            initMultiUpload(cosXmlService);
        }else {
            //继续跑
            listMultiUpload(cosXmlService);
        }
    }

    private void initMultiUpload(CosXmlSimpleService cosXmlService){
        initMultipartUploadRequest = new InitMultipartUploadRequest(bucket, cosPath);
        initMultipartUploadRequest.setRegion(region);

        initMultipartUploadRequest.setRequestHeaders(headers);

        if(onSignatureListener != null){
            initMultipartUploadRequest.setSign(onSignatureListener.onGetSign(initMultipartUploadRequest));
        }

        getHttpMetrics(initMultipartUploadRequest, "InitMultipartUploadRequest");

        cosXmlService.initMultipartUploadAsync(initMultipartUploadRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // notify -> upload part
                if(request != initMultipartUploadRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                uploadId = ((InitMultipartUploadResult)result).initMultipartUpload.uploadId;
                largeCopyStateListenerHandler.onInit();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                // notify -> exit caused by failed
                if(request != initMultipartUploadRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                largeCopyStateListenerHandler.onFailed(request, exception, serviceException);
            }
        });
    }

    private synchronized void initCopyPart() {
        int count = (int) (fileLength / sliceSize);
        int i = 1;
        for(; i < count; ++ i){
            CopyPartStruct slicePartStruct = new CopyPartStruct();
            slicePartStruct.isAlreadyUpload = false;
            slicePartStruct.partNumber = i;
            slicePartStruct.start = (i - 1) * sliceSize;
            slicePartStruct.end = i * sliceSize - 1;
            copyPartStructMap.put(i, slicePartStruct);
        }
        CopyPartStruct slicePartStruct = new CopyPartStruct();
        slicePartStruct.isAlreadyUpload = false;
        slicePartStruct.partNumber = i;
        slicePartStruct.start = (i - 1) * sliceSize;
        slicePartStruct.end = fileLength - 1;
        copyPartStructMap.put(i, slicePartStruct);
        UPLOAD_PART_COUNT.set(i);
    }

    private void listMultiUpload(CosXmlSimpleService cosXmlService){
        listPartsRequest = new ListPartsRequest(bucket, cosPath, uploadId);

        listPartsRequest.setRequestHeaders(headers);

        if(onSignatureListener != null){
            listPartsRequest.setSign(onSignatureListener.onGetSign(listPartsRequest));
        }

        getHttpMetrics(listPartsRequest, "ListPartsRequest");

        cosXmlService.listPartsAsync(listPartsRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                //update list part, then upload part.
                if(request != listPartsRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                updateSlicePart((ListPartsResult)result);
                largeCopyStateListenerHandler.onListParts();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                if(request != listPartsRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                largeCopyStateListenerHandler.onFailed(request, exception, serviceException);
            }
        });
    }

    private void updateSlicePart(ListPartsResult listPartsResult){
        if(listPartsResult != null && listPartsResult.listParts != null){
            List<ListParts.Part> parts = listPartsResult.listParts.parts;
            if(parts != null){
                for(ListParts.Part part : parts){
                    if(copyPartStructMap.containsKey(Integer.valueOf(part.partNumber))){
                        CopyPartStruct slicePartStruct = copyPartStructMap.get(Integer.valueOf(part.partNumber));
                        slicePartStruct.isAlreadyUpload = true;
                        slicePartStruct.eTag = part.eTag;
                        UPLOAD_PART_COUNT.decrementAndGet();
                    }
                }
            }
        }
    }

    private void uploadPartCopy(CosXmlSimpleService cosXmlService){
        boolean isCopyFinished = true;
        for(Map.Entry<Integer, CopyPartStruct> entry : copyPartStructMap.entrySet()){
            final CopyPartStruct copyPartStruct = entry.getValue();
            if(!copyPartStruct.isAlreadyUpload && !IS_EXIT.get()){
                isCopyFinished = false;
                final UploadPartCopyRequest uploadPartCopyRequest = new UploadPartCopyRequest(bucket,
                        cosPath, copyPartStruct.partNumber, uploadId, copySourceStruct, copyPartStruct.start,
                        copyPartStruct.end);
                uploadPartCopyRequest.setRegion(region);

                uploadPartCopyRequest.setRequestHeaders(headers);

                if(onSignatureListener != null){
                    uploadPartCopyRequest.setSign(onSignatureListener.onGetSign(uploadPartCopyRequest));
                }

                getHttpMetrics(uploadPartCopyRequest, "UploadPartCopyRequest");

                uploadPartCopyRequestList.add(uploadPartCopyRequest);

                cosXmlService.copyObjectAsync(uploadPartCopyRequest, new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        if(request != uploadPartCopyRequest){
                            return;
                        }
                        if(IS_EXIT.get())return;
                        copyPartStruct.eTag = ((UploadPartCopyResult)result).copyObject.eTag;
                        copyPartStruct.isAlreadyUpload = true;
                        synchronized (SYNC_UPLOAD_PART){
                            UPLOAD_PART_COUNT.decrementAndGet();
                            if(UPLOAD_PART_COUNT.get() == 0){
                                largeCopyStateListenerHandler.onUploadPartCopy();
                            }
                        }
                    }

                    @Override
                    public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                        if(request != uploadPartCopyRequest){
                            return;
                        }
                        if(IS_EXIT.get())return;//已经上报失败了
                        IS_EXIT.set(true);
                        largeCopyStateListenerHandler.onFailed(request, exception, serviceException);
                    }
                });
            }
        }
        if(isCopyFinished && !IS_EXIT.get()){
            largeCopyStateListenerHandler.onUploadPartCopy();
        }
    }

    private void completeMultiUpload(CosXmlSimpleService cosXmlService){
        completeMultiUploadRequest = new CompleteMultiUploadRequest(bucket, cosPath,
                uploadId, null);
        for(Map.Entry<Integer, CopyPartStruct> entry : copyPartStructMap.entrySet()){
            CopyPartStruct copyPartStruct = entry.getValue();
            completeMultiUploadRequest.setPartNumberAndETag(copyPartStruct.partNumber, copyPartStruct.eTag);
        }

        completeMultiUploadRequest.setNeedMD5(isNeedMd5);
        completeMultiUploadRequest.setRequestHeaders(headers);

        if(onSignatureListener != null){
            completeMultiUploadRequest.setSign(onSignatureListener.onGetSign(completeMultiUploadRequest));
        }

        getHttpMetrics(completeMultiUploadRequest, "CompleteMultiUploadRequest");

        cosXmlService.completeMultiUploadAsync(completeMultiUploadRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if(request != completeMultiUploadRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                largeCopyStateListenerHandler.onCompleted(request, result);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                if(request != completeMultiUploadRequest){
                    return;
                }
                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                largeCopyStateListenerHandler.onFailed(request, exception, serviceException);
            }
        });
    }

    /**
     *  暂停、失败、取消调用
     * @param cosXmlService cosXmlSimpleService
     */
    private void cancelAllRequest(CosXmlSimpleService cosXmlService){
        HeadObjectRequest tempHeadObjectRequest = headObjectRequest;
        if(tempHeadObjectRequest != null){
            cosXmlService.cancel(tempHeadObjectRequest);
        }
        CopyObjectRequest tempCopyObjectRequest = copyObjectRequest;
        if(tempCopyObjectRequest != null){
            cosXmlService.cancel(tempCopyObjectRequest);
        }
        InitMultipartUploadRequest tempInitMultipartRequest = initMultipartUploadRequest;
        if(tempInitMultipartRequest != null){
            cosXmlService.cancel(tempInitMultipartRequest);
        }
        ListPartsRequest tempListPartsRequest = listPartsRequest;
        if(tempListPartsRequest != null){
            cosXmlService.cancel(tempListPartsRequest);
        }
        if(uploadPartCopyRequestList != null) {
            Iterator<UploadPartCopyRequest> iterator = uploadPartCopyRequestList.iterator();
            while (iterator.hasNext()){
                cosXmlService.cancel(iterator.next());
            }
        }
        CompleteMultiUploadRequest tempCompleteMultiUploadRequest = completeMultiUploadRequest;
        if(tempCompleteMultiUploadRequest != null){
            cosXmlService.cancel(tempCompleteMultiUploadRequest);
        }
    }

    /**
     * 取消时调用
     * @param cosXmlService CosXmlSimpleService
     */
    private void abortMultiUpload(CosXmlSimpleService cosXmlService){
        if(uploadId == null) return;
        //clear copy part list on cos
        AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(bucket, cosPath,
                uploadId);

        if(onSignatureListener != null){
            abortMultiUploadRequest.setSign(onSignatureListener.onGetSign(abortMultiUploadRequest));
        }

        getHttpMetrics(abortMultiUploadRequest, "AbortMultiUploadRequest");

        cosXmlService.abortMultiUploadAsync(abortMultiUploadRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // abort success
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                // abort failed
            }
        });
    }

    //clear all
    private void clear(){
        if(uploadPartCopyRequestList != null){
            uploadPartCopyRequestList.clear();
        }
        if(copyPartStructMap != null){
            copyPartStructMap.clear();
        }
    }

    @Override
    protected void internalCompleted() {
        clear();
    }

    @Override
    protected void internalFailed() {
        cancelAllRequest(cosXmlService);
    }

    @Override
    protected void internalPause() {
        cancelAllRequest(cosXmlService);
    }

    @Override
    protected void internalCancel() {
        cancelAllRequest(cosXmlService);
        if(isLargeCopy)abortMultiUpload(cosXmlService);
    }

    @Override
    protected void internalResume() {
        taskState = TransferState.WAITING;
        IS_EXIT.set(false); //初始化
        copy();
    }

    @Override
    protected CosXmlRequest buildCOSXMLTaskRequest() {
        COSXMLCopyTaskRequest cosxmlCopyTaskRequest = new COSXMLCopyTaskRequest(region, bucket, cosPath, copySourceStruct,
                headers, queries);
        return cosxmlCopyTaskRequest;
    }

    @Override
    protected CosXmlResult buildCOSXMLTaskResult(CosXmlResult sourceResult) {
        COSXMLCopyTaskResult cosxmlCopyTaskResult = new COSXMLCopyTaskResult();
        if(sourceResult != null && sourceResult instanceof CopyObjectResult){
            CopyObjectResult copyObjectResult = (CopyObjectResult) sourceResult;
            cosxmlCopyTaskResult.httpCode = copyObjectResult.httpCode;
            cosxmlCopyTaskResult.httpMessage = copyObjectResult.httpMessage;
            cosxmlCopyTaskResult.headers = copyObjectResult.headers;
            cosxmlCopyTaskResult.eTag = copyObjectResult.copyObject.eTag;
            cosxmlCopyTaskResult.accessUrl = copyObjectResult.accessUrl;
        }else if(sourceResult != null && sourceResult instanceof CompleteMultiUploadResult){
            CompleteMultiUploadResult completeMultiUploadResult = (CompleteMultiUploadResult) sourceResult;
            cosxmlCopyTaskResult.httpCode = completeMultiUploadResult.httpCode;
            cosxmlCopyTaskResult.httpMessage = completeMultiUploadResult.httpMessage;
            cosxmlCopyTaskResult.headers = completeMultiUploadResult.headers;
            cosxmlCopyTaskResult.eTag = completeMultiUploadResult.completeMultipartUpload.eTag;
            cosxmlCopyTaskResult.accessUrl = completeMultiUploadResult.accessUrl;
        }
        return cosxmlCopyTaskResult;
    }

    public String getUploadId() {
        return uploadId;
    }

    protected void run() {
        headObjectRequest = new HeadObjectRequest(copySourceStruct.bucket, copySourceStruct.cosPath);
        headObjectRequest.setRegion(copySourceStruct.region);

        if(onSignatureListener != null){
            headObjectRequest.setSign(onSignatureListener.onGetSign(headObjectRequest));
        }

        getHttpMetrics(headObjectRequest, "HeadObjectRequest");

        headObjectRequest.setTaskStateListener(new QCloudTaskStateListener() {
            @Override
            public void onStateChanged(String taskId, int state) {
                if(IS_EXIT.get())return;
                updateState(TransferState.IN_PROGRESS, null, null, false);
            }
        });
        cosXmlService.headObjectAsync(headObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if(IS_EXIT.get())return;
                List<String> contentLengths = result.headers.get("Content-Length");
                if(contentLengths != null && contentLengths.size() > 0){
                    fileLength = Long.parseLong(contentLengths.get(0));
                }
                if(fileLength > multiCopySizeDivision){
                    isLargeCopy = true;
                    if(copyPartStructMap != null){
                        copyPartStructMap.clear();
                    }else {
                        copyPartStructMap = new LinkedHashMap<>();
                    }
                    if(uploadPartCopyRequestList != null){
                        uploadPartCopyRequestList.clear();
                    }else {
                        uploadPartCopyRequestList = new ArrayList<>();
                    }
                    if(UPLOAD_PART_COUNT != null){
                        UPLOAD_PART_COUNT.set(0);
                    }else {
                        UPLOAD_PART_COUNT = new AtomicInteger(0);
                    }
                    largeFileCopy(cosXmlService);
                }else {
                    smallFileCopy();
                }
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                if(IS_EXIT.get())return;
                IS_EXIT.set(true);
                Exception causeException = exception == null ? serviceException : exception;
                updateState(TransferState.FAILED, causeException, null, false);
            }
        });
    }

    private interface LargeCopyStateListener{
        void onInit();
        void onListParts();
        void onUploadPartCopy();
        void onCompleted(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult);
        void onFailed(CosXmlRequest cosXmlRequest, CosXmlClientException exception, CosXmlServiceException serviceException);
    }

    private static class CopyPartStruct{
        public int partNumber;
        public boolean isAlreadyUpload;
        public long start;
        public long end;
        public String eTag;
    }

    public static class COSXMLCopyTaskResult extends CosXmlResult{
        protected COSXMLCopyTaskResult(){}
        public String eTag;
    }

    public static class COSXMLCopyTaskRequest extends CopyObjectRequest{

        protected COSXMLCopyTaskRequest(String region, String bucket, String cosPath, CopySourceStruct copySourceStruct, Map<String, List<String>>headers,
                                        Map<String, String> queryStr) {
            super(bucket, cosPath, copySourceStruct);
            this.setRegion(region);
            this.setRequestHeaders(headers);
            this.setQueryParameters(queryStr);
        }
    }
}
