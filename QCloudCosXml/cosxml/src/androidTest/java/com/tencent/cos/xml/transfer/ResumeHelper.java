package com.tencent.cos.xml.transfer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.Region;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

public class ResumeHelper {
    private final static String TAG = ResumeHelper.class.getSimpleName();
    private Context context;
    String bucket = "examplebucket-1250000000"; //存储桶，格式 BucketName-APPID, 替换为您的 bucket
    String region = Region.AP_Guangzhou.getRegion(); //存储桶所在地域, 填写您的 bucket 所在的地域
    private CosXmlSimpleService cosXmlService;
    private Handler mainHandler;
    private boolean isTriedOnce = false; //是否已重传过，避免无限制重传
    private final int MESSAGE_RETRY = 1;
    public ResumeHelper(Context context){
        this.context = context;
        this.mainHandler = new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MESSAGE_RETRY:
                        /** 已重传过 */
                        if(isTriedOnce)return;
                        isTriedOnce = true;
                        Parameter parameter = (Parameter) msg.obj;
                        /** 再次上传 */
                        upload(parameter.srcPath, parameter.cosPath, parameter.uploadId, parameter.sliceSize);
                        break;
                }
            }
        };
        initCosXml();
    }
    /**
     * 初始化 CosXmlSimpleService
     */
    private void initCosXml(){
        /** 初始化 CosXmlServiceConfig */
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .setDebuggable(true)
                .isHttps(true)
                .setRegion(region)
                .builder();
        //此处为了方便测试，使用了永久密钥，实际应用中建议使用临时密钥
        String secretId = "COS_SECRETID"; //填写您的 云 API 密钥 SecretId
        String secretKey = "COS_SECRETKEY"; //填写您的 云 API 密钥 SecretKey
        /** 初始化 密钥信息 */
        QCloudCredentialProvider qCloudCredentialProvider = new ShortTimeCredentialProvider(secretId, secretKey, 6000);
        /** 初始化 CosXmlService */
        cosXmlService = new CosXmlSimpleService(context, cosXmlServiceConfig, qCloudCredentialProvider);
    }
    /**
     * 上传
     * @param srcPath 本地文件路径
     * @param cosPath 存储在 COS 上的路径
     * @param uploadId 是否续传，若无，则为 null
     * @param sliceSize 分块上传时，设置的分块块大小
     */
    public void upload(final String srcPath, final String cosPath, final String uploadId, long sliceSize){
        /** 设置分块上传时，分块块的大小 */
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setSliceSizeForUpload(sliceSize)
                .build();
        /** 初始化TransferManager */
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);
        /** 开始上传: 若 uploadId != null,则可以进行续传 */
        final COSXMLUploadTask uploadTask = transferManager.upload(bucket, cosPath, srcPath, uploadId);
        /** 显示任务状态信息 */
        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG, "upload task state: " + state.name());
            }
        });
        /** 显示任务上传进度 */
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TAG, "upload task progress: " + complete + "/" + target);
            }
        });
        /** 显示任务上传结果 */
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            /** 任务上传成功 */
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult uploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                Log.d(TAG, "upload task success: " + uploadTaskResult.printResult());
            }
            /** 任务上传失败 */
            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG, "upload task failed: " + (exception == null ? serviceException.getMessage() :
                        (exception.errorCode + "," + exception.getMessage())));
                if(exception != null){
                    /** 若是因为网络导致失败， 则可尝试将分块大小设置为100KB再运行一遍*/
                    if(exception.errorCode == ClientErrorCode.IO_ERROR.getCode()
                            || exception.errorCode == ClientErrorCode.POOR_NETWORK.getCode()){
                        Log.d(TAG, "upload task try again");
                        Message msg = mainHandler.obtainMessage();
                        msg.what = MESSAGE_RETRY;
                        Parameter parameter = new Parameter();
                        parameter.cosPath = cosPath;
                        parameter.srcPath = srcPath;
                        parameter.uploadId = uploadTask.getUploadId();
                        parameter.sliceSize = 100 * 1024L;
                        msg.obj = parameter;
                        mainHandler.sendMessage(msg);
                    }
                }
            }
        });
    }
    private static class Parameter{
        private String cosPath;
        private String srcPath;
        private String uploadId;
        private long sliceSize;
    }
}