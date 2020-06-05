package com.tencent.cos.xml.model.bucket;


import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.CosXmlRequest;

/**
 * Created by bradyxiao on 2017/11/30.
 */

public abstract class BucketRequest extends CosXmlRequest {

    public BucketRequest(String bucket){
        this.bucket = bucket;
    }

    @Override
    public String getPath(CosXmlServiceConfig config) {
        return config.getUrlPath(bucket, "/");
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        if(requestURL != null){
            return;
        }
        if(bucket == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "bucket must not be null");
        }
    }
}
