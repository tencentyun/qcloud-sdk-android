package com.tencent.cos.xml.transfer;


import android.content.Context;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.ListPartsResult;
import com.tencent.cos.xml.model.object.ObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.cos.xml.model.tag.ListParts;
import com.tencent.cos.xml.utils.SharePreferenceUtils;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by bradyxiao on 2017/12/4.
 */
@Deprecated
public class UploadService {
    private static String TAG = "UploadService";
    private CosXmlSimpleService cosXmlService;
    private String bucket;
    private String cosPath;
    private String srcPath;
    private long sliceSize = 1024 * 1024 * 1;
    private String uploadId;
    private long fileLength;
    private static final long SIZE_LIMIT = 2 * 1024 * 1024;
    private CosXmlProgressListener cosXmlProgressListener;
    private Map<Integer, SlicePartStruct> partStructMap;
    private AtomicInteger UPLOAD_PART_COUNT;
    private AtomicLong ALREADY_SEND_DATA_LEN;
    private volatile int ERROR_EXIT_FLAG; //  0(init),1(normal exception),2(manual pause),3(abort)
    private byte[] objectSync = new byte[0];
    private Exception mException;
    private Map<UploadPartRequest,Long> uploadPartRequestLongMap;
    private InitMultipartUploadRequest initMultipartUploadRequest;
    private ListPartsRequest listPartsRequest;
    private CompleteMultiUploadRequest completeMultiUploadRequest;
    private PutObjectRequest putObjectRequest;
    private UploadServiceResult uploadServiceResult;
    private long startTime = -1L;
    private long endTime = -1L;
    ResumeData resumeData;
    private List<String> headers = new ArrayList<>();
    private boolean isNeedMd5 = false;
    private SharePreferenceUtils sharePreferenceUtils;
    private OnUploadInfoListener onUploadInfoListener;
    private EncryptionType encryptionType = EncryptionType.NONE;
    private boolean isSupportAccelerate = false;
    private OnSignatureListener onSignatureListener;
    private OnGetHttpTaskMetrics onGetHttpTaskMetrics;

    public UploadService(CosXmlSimpleService cosXmlService, ResumeData resumeData){
        this.cosXmlService = cosXmlService;
        init(resumeData);
    }

    //用于crash 续传
    public UploadService(CosXmlSimpleService cosXmlService, String bucket, String cosPath, String srcPath, long sliceSize, Context context){
        String uploadId = null;
        if(context != null){
            sharePreferenceUtils = SharePreferenceUtils.instance(context.getApplicationContext());
            String key = getKey(cosXmlService, bucket, cosPath, srcPath, sliceSize);
            if(key != null){
               uploadId = sharePreferenceUtils.getValue(key);
            }
        }
        ResumeData resumeData = new ResumeData();
        resumeData.bucket = bucket;
        resumeData.cosPath = cosPath;
        resumeData.sliceSize = sliceSize;
        resumeData.srcPath = srcPath;
        resumeData.uploadId = uploadId;
        this.cosXmlService = cosXmlService;
        init(resumeData);
    }

    String getKey(CosXmlSimpleService cosXmlService, String bucket, String cosPath, String srcPath, long sliceSize){
        File file = new File(srcPath);
        if(file.exists()){
            StringBuffer stringBuffer = new StringBuffer();
            String appid = cosXmlService != null ? cosXmlService.getAppid() : null;
            stringBuffer.append(appid).append(";").append(bucket).append(";").append(cosPath).append(";")
                    .append(srcPath).append(";").append(file.length()).append(";").append(file.lastModified()).append(";")
                    .append(sliceSize);
            return stringBuffer.toString();
        }
        return null;
    }

    void clearSharePreference(){
        if(sharePreferenceUtils != null){
            sharePreferenceUtils.clear(getKey(cosXmlService, bucket, cosPath, srcPath, sliceSize));
        }
    }

    boolean updateSharePreference(String value){
        if(sharePreferenceUtils != null){
            return sharePreferenceUtils.updateValue(getKey(cosXmlService, bucket, cosPath, srcPath, sliceSize), value);
        }
        return false;
    }

    void init(ResumeData resumeData){
        bucket = resumeData.bucket;
        cosPath = resumeData.cosPath;
        srcPath = resumeData.srcPath;
        sliceSize = resumeData.sliceSize;
        uploadId = resumeData.uploadId;
        UPLOAD_PART_COUNT = new AtomicInteger(0);
        ALREADY_SEND_DATA_LEN = new AtomicLong(0);
        ERROR_EXIT_FLAG = 0;
        partStructMap = new LinkedHashMap<Integer, SlicePartStruct>();
        uploadPartRequestLongMap = new LinkedHashMap<UploadPartRequest, Long>();
        this.resumeData = resumeData;
    }

    private void checkParameter() throws CosXmlClientException {
        if(srcPath != null){
            File file = new File(srcPath);
            if(file.exists()){
                fileLength = file.length();
                return;
            }
        }
        throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "srcPath :" + srcPath + " is invalid or is not exist");
    }

    public void setSign(long startTime, long endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void setSignTime(CosXmlRequest cosXmlRequest){
        if(cosXmlRequest != null){
            if(startTime > 0 && endTime >= startTime){
                cosXmlRequest.setSign(startTime, endTime);
            }
        }
    }

    public void setOnSignatureListener(OnSignatureListener onSignatureListener){
        this.onSignatureListener = onSignatureListener;
    }

    public void setOnGetHttpTaskMetrics(OnGetHttpTaskMetrics onGetHttpTaskMetrics){
        this.onGetHttpTaskMetrics = onGetHttpTaskMetrics;
    }

    private void getHttpMetrics(CosXmlRequest cosXmlRequest, final String requestName){
        if(onGetHttpTaskMetrics != null){
            cosXmlRequest.attachMetrics(new HttpTaskMetrics(){
                @Override
                public void onDataReady() {
                    super.onDataReady();
                    onGetHttpTaskMetrics.onGetHttpMetrics(requestName, this);
                }
            });
        }
    }

    public void setRequestHeaders(String key, String value){
        if(key != null && value != null){
            headers.add(key);
            headers.add(value);
        }
    }

    public void setNeedMd5(boolean isNeed){
        this.isNeedMd5 = isNeed;
    }

    public void setCOSServerSideEncryptionType(EncryptionType encryptionType){
        this.encryptionType = encryptionType;
    }

    public void isSupportAccelerate(boolean isSupportAccelerate){
        this.isSupportAccelerate = isSupportAccelerate;
    }

    private void setEncryption(CosXmlRequest cosXmlRequest) throws CosXmlClientException {
        if(cosXmlRequest == null)return;
        switch (encryptionType){
            case NONE:
                break;
            case SSE:
                ((ObjectRequest)cosXmlRequest).setCOSServerSideEncryption();
                break;
            case SSEC:
                ((ObjectRequest)cosXmlRequest).setCOSServerSideEncryptionWithCustomerKey(resumeData.customerKeyForSSEC);
                break;
            case SSEKMS:
                ((ObjectRequest)cosXmlRequest).setCOSServerSideEncryptionWithKMS(resumeData.customerKeyIdForSSEKMS, resumeData.jsonContentForSSEKMS);
                break;
        }
    }

    public void setProgressListener(CosXmlProgressListener cosXmlProgressListener){
        this.cosXmlProgressListener = cosXmlProgressListener;
    }

    public void setOnUploadInfoListener(OnUploadInfoListener onUploadInfoListener){
        this.onUploadInfoListener = onUploadInfoListener;
    }

    private void setRequestHeaders(CosXmlRequest cosXmlRequest) throws CosXmlClientException {
        if(cosXmlRequest != null){
            int size = headers.size();
            for(int i = 0; i < size - 2; i += 2){
                cosXmlRequest.setRequestHeaders(headers.get(i), headers.get(i + 1), false);
            }
        }
    }

    private void setSupportAccelerate(CosXmlRequest cosXmlRequest){
        if(cosXmlRequest != null && isSupportAccelerate){
            cosXmlRequest.isSupportAccelerate(isSupportAccelerate);
        }
    }

    public UploadServiceResult upload() throws CosXmlClientException, CosXmlServiceException {
        checkParameter();
        if(fileLength < SIZE_LIMIT){
            return putObject(bucket, cosPath, srcPath);
        }else {
            return multiUploadParts();
        }
    }

    public CosXmlResult resume(ResumeData resumeData) throws CosXmlServiceException, CosXmlClientException {
        init(resumeData);
        return upload();
    }

    public ResumeData pause(){
        ERROR_EXIT_FLAG = 2;
        ResumeData resumeData = new ResumeData();
        resumeData.bucket = bucket;
        resumeData.cosPath = cosPath;
        resumeData.sliceSize = sliceSize;
        resumeData.srcPath = srcPath;
        resumeData.uploadId = uploadId;
        resumeData.customerKeyForSSEC = this.resumeData.customerKeyForSSEC;
        resumeData.customerKeyIdForSSEKMS = this.resumeData.customerKeyIdForSSEKMS;
        resumeData.jsonContentForSSEKMS = this.resumeData.jsonContentForSSEKMS;
        return resumeData;
    }

    public void abort(CosXmlResultListener cosXmlResultListener){
        ERROR_EXIT_FLAG = 3;
        abortMultiUpload(cosXmlResultListener);
    }

    private void clear(){
        putObjectRequest = null;
        initMultipartUploadRequest = null;
        listPartsRequest = null;
        completeMultiUploadRequest = null;
        partStructMap.clear();
        uploadPartRequestLongMap.clear();
    }

    /**
     * small file using put object api
     */
    private UploadServiceResult putObject(final String bucket, String cosPath, String srcPath) throws CosXmlClientException, CosXmlServiceException {
        UPLOAD_PART_COUNT.set(1);
        putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
        putObjectRequest.setProgressListener(cosXmlProgressListener);
        //calculation sign
        if(onSignatureListener != null){
            putObjectRequest.setSign(onSignatureListener.onGetSign(putObjectRequest));
        }else {
            setSignTime(putObjectRequest);
        }

        getHttpMetrics(putObjectRequest, "PutObjectRequest");

        setRequestHeaders(putObjectRequest);
        setSupportAccelerate(putObjectRequest);
        setEncryption(putObjectRequest);
        putObjectRequest.setNeedMD5(isNeedMd5);
        cosXmlService.putObjectAsync(putObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                synchronized (objectSync){
                    PutObjectResult putObjectResult = (PutObjectResult) result;
                    if(uploadServiceResult == null)uploadServiceResult = new UploadServiceResult();
                    uploadServiceResult.httpCode = putObjectResult.httpCode;
                    uploadServiceResult.httpMessage = putObjectResult.httpMessage;
                    uploadServiceResult.headers = putObjectResult.headers;
                    uploadServiceResult.eTag = putObjectResult.eTag;
                }
                UPLOAD_PART_COUNT.decrementAndGet();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                synchronized (objectSync){
                    if(exception != null){
                        mException = exception;
                    }else{
                        mException = serviceException;
                    }
                    ERROR_EXIT_FLAG = 1;
                }
            }
        });

        //wait upload parts complete.
        while (UPLOAD_PART_COUNT.get() > 0 && ERROR_EXIT_FLAG == 0);
        //if error throw exception
        if(ERROR_EXIT_FLAG > 0){
            switch (ERROR_EXIT_FLAG){
                case 2:
                    realCancel();
                    clear();
                    throw new CosXmlClientException(ClientErrorCode.USER_CANCELLED.getCode(), "request is cancelled by manual pause");
                case 3:
                    throw new CosXmlClientException(ClientErrorCode.USER_CANCELLED.getCode(), "request is cancelled by abort request");
                case 1:
                    realCancel();
                    if(mException != null){
                        if(mException instanceof CosXmlClientException){
                            throw (CosXmlClientException)mException;
                        }
                        if(mException instanceof CosXmlServiceException){
                            throw (CosXmlServiceException)mException;
                        }
                    }else{
                        throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), "unknown exception");
                    }
            }
        }

        uploadServiceResult.accessUrl = cosXmlService.getAccessUrl(putObjectRequest);
        return uploadServiceResult;
    }

    private UploadServiceResult multiUploadParts() throws CosXmlClientException, CosXmlServiceException {
        initSlicePart();
        if(uploadId != null){
            ListPartsResult listPartsResult = listPart();
            //breakpoint transmission
            updateSlicePart(listPartsResult);
        }else {
            InitMultipartUploadResult initMultipartUploadResult = initMultiUpload();
            uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;
        }
        if(onUploadInfoListener != null){
            ResumeData resumeData = new ResumeData();
            resumeData.bucket = bucket;
            resumeData.cosPath = cosPath;
            resumeData.sliceSize = sliceSize;
            resumeData.srcPath = srcPath;
            resumeData.uploadId = uploadId;
            resumeData.customerKeyForSSEC = this.resumeData.customerKeyForSSEC;
            resumeData.customerKeyIdForSSEKMS = this.resumeData.customerKeyIdForSSEKMS;
            resumeData.jsonContentForSSEKMS = this.resumeData.jsonContentForSSEKMS;
            onUploadInfoListener.onInfo(resumeData);
        }
        updateSharePreference(uploadId);

        for(final Map.Entry<Integer, SlicePartStruct> entry : partStructMap.entrySet()){
            final SlicePartStruct slicePartStruct = entry.getValue();
            if(!slicePartStruct.isAlreadyUpload){
                uploadPart(slicePartStruct.partNumber, slicePartStruct.offset, slicePartStruct.sliceSize,
                        new CosXmlResultListener() {
                            @Override
                            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                                synchronized (objectSync){
                                    slicePartStruct.eTag = ((UploadPartResult)result).eTag;
                                    slicePartStruct.isAlreadyUpload = true;
                                }
                                UPLOAD_PART_COUNT.decrementAndGet();
                            }

                            @Override
                            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                                synchronized (objectSync){
                                    if(exception != null){
                                        mException = exception;
                                    }else{
                                        mException = serviceException;
                                    }
                                    ERROR_EXIT_FLAG = 1;
                                }
                            }
                        });
            }
        }

        //wait upload parts complete.
        while (UPLOAD_PART_COUNT.get() > 0 && ERROR_EXIT_FLAG == 0);

        //clear sharePreference
        clearSharePreference();

        //if error throw exception
        if(ERROR_EXIT_FLAG > 0){
            switch (ERROR_EXIT_FLAG){
                case 2:
                    realCancel();
                    clear();
                    throw new CosXmlClientException(ClientErrorCode.USER_CANCELLED.getCode(), "request is cancelled by manual pause");
                case 3:
                    throw new CosXmlClientException(ClientErrorCode.USER_CANCELLED.getCode(), "request is cancelled by abort request");
                case 1:
                    realCancel();
                    if(mException != null){
                        if(mException instanceof CosXmlClientException){
                            throw (CosXmlClientException)mException;
                        }
                        if(mException instanceof CosXmlServiceException){
                            throw (CosXmlServiceException)mException;
                        }
                    }else{
                        throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), "unknown exception");
                    }
            }
        }
        CompleteMultiUploadResult completeMultiUploadResult = completeMultiUpload();
        if(uploadServiceResult == null)uploadServiceResult = new UploadServiceResult();
        uploadServiceResult.httpCode = completeMultiUploadResult.httpCode;
        uploadServiceResult.httpMessage = completeMultiUploadResult.httpMessage;
        uploadServiceResult.headers = completeMultiUploadResult.headers;
        uploadServiceResult.eTag = completeMultiUploadResult.completeMultipartUpload.eTag;
        uploadServiceResult.accessUrl = cosXmlService.getAccessUrl(completeMultiUploadRequest);
        return uploadServiceResult;
    }

    /**
     * init multi,then get uploadId
     */
    private InitMultipartUploadResult initMultiUpload() throws CosXmlServiceException, CosXmlClientException {
        initMultipartUploadRequest = new InitMultipartUploadRequest(bucket,
                cosPath);

        //calculation sign
        if(onSignatureListener != null){
            initMultipartUploadRequest.setSign(onSignatureListener.onGetSign(initMultipartUploadRequest));
        }else {
            setSignTime(initMultipartUploadRequest);
        }

        getHttpMetrics(initMultipartUploadRequest, "InitMultipartUploadRequest");

        setRequestHeaders(initMultipartUploadRequest);
        setSupportAccelerate(initMultipartUploadRequest);
        setEncryption(initMultipartUploadRequest);
        return cosXmlService.initMultipartUpload(initMultipartUploadRequest);
    }

    /**
     * List Parts, check which parts have been uploaded.
     */
    private ListPartsResult listPart() throws CosXmlServiceException, CosXmlClientException {
        listPartsRequest = new ListPartsRequest(bucket, cosPath, uploadId);

        //calculation sign
        if(onSignatureListener != null){
            listPartsRequest.setSign(onSignatureListener.onGetSign(listPartsRequest));
        }else {
            setSignTime(listPartsRequest);
        }

        getHttpMetrics(listPartsRequest, "ListPartsRequest");

        setRequestHeaders(listPartsRequest);
        setSupportAccelerate(listPartsRequest);
        return cosXmlService.listParts(listPartsRequest);
    }

    /**
     * upload Part,  concurrence upload file parts.
     */
    private void uploadPart(final int partNumber, long offset, long contentLength, CosXmlResultListener cosXmlResultListener){
        final UploadPartRequest uploadPartRequest = new UploadPartRequest(bucket, cosPath, partNumber,
                srcPath, offset, contentLength, uploadId);
        uploadPartRequestLongMap.put(uploadPartRequest, 0L);
        uploadPartRequest.setNeedMD5(isNeedMd5);

        //calculation sign
        if(onSignatureListener != null){
            uploadPartRequest.setSign(onSignatureListener.onGetSign(uploadPartRequest));
        }else {
            setSignTime(uploadPartRequest);
        }

        getHttpMetrics(uploadPartRequest, "UploadPartRequest");

        try {
            setRequestHeaders(uploadPartRequest);
            setSupportAccelerate(uploadPartRequest);
            setEncryption(uploadPartRequest);
        } catch (CosXmlClientException e) {
            cosXmlResultListener.onFail(putObjectRequest, e, null);
            return;
        }
        uploadPartRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                synchronized (objectSync){
                    try {
                        long dataLen = ALREADY_SEND_DATA_LEN.addAndGet(complete - uploadPartRequestLongMap.get(uploadPartRequest));
                        uploadPartRequestLongMap.put(uploadPartRequest, complete);
                        if(cosXmlProgressListener != null){
                            cosXmlProgressListener.onProgress(dataLen,fileLength);
                        }
                    }catch (Exception e){
                        if(ERROR_EXIT_FLAG > 0){
                            QCloudLogger.d(TAG, "upload file has been abort");
                        }
                    }

                }
            }
        });
        cosXmlService.uploadPartAsync(uploadPartRequest, cosXmlResultListener);
    }

    /**
     * complete multi upload.
     */
    private CompleteMultiUploadResult completeMultiUpload() throws CosXmlServiceException, CosXmlClientException {
        completeMultiUploadRequest = new CompleteMultiUploadRequest(bucket, cosPath,
                uploadId, null);
        for(Map.Entry<Integer, SlicePartStruct> entry : partStructMap.entrySet()){
            SlicePartStruct slicePartStruct = entry.getValue();
            completeMultiUploadRequest.setPartNumberAndETag(slicePartStruct.partNumber, slicePartStruct.eTag);
        }

        //calculation sign
        if(onSignatureListener != null){
            completeMultiUploadRequest.setSign(onSignatureListener.onGetSign(completeMultiUploadRequest));
        }else {
            setSignTime(completeMultiUploadRequest);
        }

        getHttpMetrics(completeMultiUploadRequest, "CompleteMultiUploadResult");

        setRequestHeaders(completeMultiUploadRequest);
        setSupportAccelerate(completeMultiUploadRequest);
        //setEncryption(completeMultiUploadRequest);
        completeMultiUploadRequest.setNeedMD5(isNeedMd5);
        return cosXmlService.completeMultiUpload(completeMultiUploadRequest);
    }

    /**
     * abort multi upload
     */
    private void abortMultiUpload(final CosXmlResultListener cosXmlResultListener){
        if(uploadId == null) return;
        AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(bucket, cosPath,
                uploadId);

        //calculation sign
        if(onSignatureListener != null){
            abortMultiUploadRequest.setSign(onSignatureListener.onGetSign(abortMultiUploadRequest));
        }else {
            setSignTime(abortMultiUploadRequest);
        }

        getHttpMetrics(abortMultiUploadRequest, "AbortMultiUploadRequest");

        try {
            setRequestHeaders(abortMultiUploadRequest);
            setSupportAccelerate(abortMultiUploadRequest);
        } catch (CosXmlClientException e) {
            cosXmlResultListener.onFail(abortMultiUploadRequest, e, null);
            return;
        }
        cosXmlService.abortMultiUploadAsync(abortMultiUploadRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                cosXmlResultListener.onSuccess(request, result);
                realCancel();
                clear();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                cosXmlResultListener.onFail(request, exception, serviceException);
                realCancel();
                clear();
            }
        });
    }

    private void realCancel(){
        cosXmlService.cancel(putObjectRequest);
        cosXmlService.cancel(initMultipartUploadRequest);
        cosXmlService.cancel(listPartsRequest);
        cosXmlService.cancel(completeMultiUploadRequest);
        if(uploadPartRequestLongMap != null){
            Set<UploadPartRequest> set = uploadPartRequestLongMap.keySet();
            Iterator<UploadPartRequest> iterator = set.iterator();
            while(iterator.hasNext()){
                cosXmlService.cancel(iterator.next());
            }
        }
    }

    /**
     * init slice part
     */
    private void initSlicePart() throws CosXmlClientException {
        if(srcPath != null){
            File file = new File(srcPath);
            if(!file.exists()){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "upload file does not exist");
            }
            fileLength = file.length();
        }
        if(fileLength > 0 && sliceSize > 0){
            int count = (int) (fileLength / sliceSize);
            int i = 1;
            for(; i < count; ++ i){
                SlicePartStruct slicePartStruct = new SlicePartStruct();
                slicePartStruct.isAlreadyUpload = false;
                slicePartStruct.partNumber = i;
                slicePartStruct.offset = (i - 1) * sliceSize;
                slicePartStruct.sliceSize = sliceSize;
                partStructMap.put(i, slicePartStruct);
            }
            SlicePartStruct slicePartStruct = new SlicePartStruct();
            slicePartStruct.isAlreadyUpload = false;
            slicePartStruct.partNumber = i;
            slicePartStruct.offset = (i - 1) * sliceSize;
            slicePartStruct.sliceSize = fileLength - slicePartStruct.offset;
            partStructMap.put(i, slicePartStruct);
            UPLOAD_PART_COUNT.set(i);
            return;
        }
        throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "file size or slice size less than 0");
    }

    private void updateSlicePart(ListPartsResult listPartsResult){
        if(listPartsResult != null && listPartsResult.listParts != null){
            List<ListParts.Part> parts = listPartsResult.listParts.parts;
            if(parts != null){
                for(ListParts.Part part : parts){
                    if(partStructMap.containsKey(Integer.valueOf(part.partNumber))){
                        SlicePartStruct slicePartStruct = partStructMap.get(Integer.valueOf(part.partNumber));
                        slicePartStruct.isAlreadyUpload = true;
                        slicePartStruct.eTag = part.eTag;
                        UPLOAD_PART_COUNT.decrementAndGet();
                        ALREADY_SEND_DATA_LEN.addAndGet(Long.parseLong(part.size));
                    }
                }
            }
        }
    }

    public static class ResumeData {
        public String bucket;
        public String cosPath;
        public String srcPath;
        public String uploadId;
        public long sliceSize;
        public String customerKeyForSSEC;
        public String customerKeyIdForSSEKMS;
        public String jsonContentForSSEKMS;
    }

    private static class SlicePartStruct{
        public int partNumber;
        public boolean isAlreadyUpload;
        public long offset;
        public long sliceSize;
        public String eTag;
    }

    public static class UploadServiceResult extends CosXmlResult{
        public String eTag;

        @Override
        public String printResult() {
            return super.printResult() + "\n"
                    + "eTag:" + eTag + "\n"
                    + "accessUrl:" + accessUrl;
        }
    }

    public enum EncryptionType{
        SSE,
        SSEC,
        SSEKMS,
        NONE;
    }

    void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public static interface OnUploadInfoListener{
        void onInfo(ResumeData resumeData);
    }

    public interface OnSignatureListener{
        /**
         * @see PutObjectRequest
         * @see InitMultipartUploadRequest
         * @see ListPartsRequest
         * @see UploadPartRequest
         * @see CompleteMultiUploadRequest
         * @see AbortMultiUploadRequest
         * @param cosXmlRequest request
         * @return String
         */
        String onGetSign(CosXmlRequest cosXmlRequest);
    }

    public interface OnGetHttpTaskMetrics{
        void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics);
    }
}
