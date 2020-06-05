package com.tencent.cos.xml.transfer;

import android.content.Context;

import com.tencent.cos.xml.CosXml;
import com.tencent.cos.xml.SimpleCosXml;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.bucket.HeadBucketResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.cos.xml.utils.SharePreferenceUtils;

import java.io.File;
import java.util.List;

/**
 * Created by bradyxiao on 2018/3/20.
 * 断点下载工具
 */
@Deprecated
public class Downloader {

    private CosXml cosXmlServer;
    private String bucket;
    private String cosPath;
    private String localPath;
    private String localFileName;
    private long rangeStart = 0L;
    private ListenerHandler listenerHandler = new ListenerHandler();
    private GetObjectRequest getObjectRequest;
    private SharePreferenceUtils sharePreferedUtils;

    public Downloader(Context appContext, CosXml cosXmlServer){
        sharePreferedUtils = SharePreferenceUtils.instance(appContext);
        this.cosXmlServer = cosXmlServer;
    }

    public void setProgress(CosXmlProgressListener cosXmlProgressListener){
       listenerHandler.setCosXmlProgressListener(cosXmlProgressListener);
    }

    public GetObjectResult download(String bucket, String cosPath, String localPath,
                                    String localFileName) throws CosXmlClientException, CosXmlServiceException {
        this.bucket = bucket;
        this.cosPath = cosPath;
        this.localPath = localPath;
        this.localFileName = localFileName;
        checkParameters();
        HeadObjectRequest headObjectRequest = new HeadObjectRequest(bucket, cosPath);
        HeadObjectResult headObjectResult = cosXmlServer.headObject(headObjectRequest);
        List<String> realEtags = headObjectResult.headers.get("ETag");
        String realEtag = null;
        if(realEtags != null && realEtags.size() > 0){
            realEtag = realEtags.get(0);
        }
        getObjectRequest = new GetObjectRequest(bucket, cosPath, localPath, localFileName);
        rangeStart = 0L;
        String realLocalPath = getObjectRequest.getDownloadPath();
        if(realEtag != null){
            String sourceEtag = sharePreferedUtils.getValue(realLocalPath);
            if(sourceEtag == null || !realEtag.equals(sourceEtag)){
                sharePreferedUtils.updateValue(realLocalPath, realEtag);
            }else {
                rangeStart = getRange(realLocalPath);
            }
        }
        getObjectRequest.setRange(rangeStart);
        getObjectRequest.setProgressListener(listenerHandler);
        GetObjectResult getObjectResult = cosXmlServer.getObject(getObjectRequest);
        sharePreferedUtils.clear(realLocalPath);
        return getObjectResult;
    }

    public void download(String bucket, String cosPath, String localPath,
                         String localFileName, CosXmlResultListener cosXmlResultListener){
        this.bucket = bucket;
        this.cosPath = cosPath;
        this.localPath = localPath;
        this.localFileName = localFileName;
        listenerHandler.setCosXmlResultListener(cosXmlResultListener);
        try {
            checkParameters();
        } catch (CosXmlClientException e) {
            listenerHandler.onFail(getObjectRequest, e, null);
            return;
        }
        getObjectRequest = new GetObjectRequest(bucket, cosPath, localPath, localFileName);
        HeadObjectRequest headObjectRequest = new HeadObjectRequest(bucket, cosPath);
        cosXmlServer.headObjectAsync(headObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                HeadObjectResult headObjectResult = (HeadObjectResult) result;
                List<String> realEtags = headObjectResult.headers.get("ETag");
                String realEtag = null;
                if(realEtags != null && realEtags.size() > 0){
                    realEtag = realEtags.get(0);
                }
                rangeStart = 0L;
                String realLocalPath = getObjectRequest.getDownloadPath();
                if(realEtag != null){
                    String sourceEtag = sharePreferedUtils.getValue(realLocalPath);
                    if(sourceEtag == null || !realEtag.equals(sourceEtag)){
                        sharePreferedUtils.updateValue(realLocalPath, realEtag);
                    }else {
                        rangeStart = getRange(realLocalPath);
                    }
                }
                getObjectRequest.setRange(rangeStart);
                getObjectRequest.setProgressListener(listenerHandler);
                cosXmlServer.getObjectAsync(getObjectRequest, listenerHandler);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                listenerHandler.onFail(getObjectRequest, exception, serviceException);
            }
        });
    }

    public void cancel(){
        if(getObjectRequest != null && cosXmlServer != null){
            cosXmlServer.cancel(getObjectRequest);
        }
    }

    private long getRange(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            return file.length();
        }
        return 0;
    }

    private void checkParameters() throws CosXmlClientException{
        if(bucket == null) throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "bucket must not be null ");
        if(cosPath == null) throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "cosPath must not be null ");
        if(localPath == null) throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "localPath must not be null ");
    }

    private class ListenerHandler implements CosXmlProgressListener, CosXmlResultListener{

        private CosXmlProgressListener cosXmlProgressListener;
        private CosXmlResultListener cosXmlResultListener;

        public ListenerHandler(){}

        public void setCosXmlProgressListener(CosXmlProgressListener cosXmlProgressListener){
            this.cosXmlProgressListener = cosXmlProgressListener;
        }

        public void setCosXmlResultListener(CosXmlResultListener cosXmlResultListener){
            this.cosXmlResultListener = cosXmlResultListener;
        }

        @Override
        public void onProgress(long complete, long target) {
            if(cosXmlProgressListener != null){
                cosXmlProgressListener.onProgress(rangeStart + complete, rangeStart + target);
            }
        }

        @Override
        public void onSuccess(CosXmlRequest request, CosXmlResult result) {
            sharePreferedUtils.clear(getObjectRequest.getDownloadPath());
            if(cosXmlResultListener != null){
                cosXmlResultListener.onSuccess(request, result);
            }
        }

        @Override
        public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
            if(cosXmlResultListener != null){
                cosXmlResultListener.onFail(request, exception, serviceException);
            }
        }

    }
}
