package com.tencent.cos.xml.model;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.RequestBodySerializer;

public class PresignedUrlRequest extends CosXmlRequest {

    private String requestMethod = RequestMethod.GET;

    private String cosPath = "/";

    public PresignedUrlRequest(String bucket, String cosPath){
        this.bucket = bucket;
        this.cosPath = cosPath;
    }

    public void setRequestMethod(String method){
        this.requestMethod = method;
    }

    public void setCosPath(String cosPath){
        if(cosPath != null){
            if(!cosPath.startsWith("/")){
                this.cosPath = "/" + cosPath;
            }else {
                this.cosPath = cosPath;
            }
        }
    }


    @Override
    public String getMethod() {
        return requestMethod;
    }

    @Override
    public String getPath(CosXmlServiceConfig config) {

        return  config.getUrlPath(bucket, cosPath);
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        if(bucket == null || bucket.length() < 1){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "bucket must not be null ");
        }
        if(cosPath == null || cosPath.length() < 1){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "cosPath must not be null ");
        }
    }
}
