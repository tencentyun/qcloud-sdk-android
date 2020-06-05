package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

public class GetBucketInventoryRequest extends BucketRequest {

    private String inventoryId;

    public GetBucketInventoryRequest(String bucket) {
        super(bucket);
    }

    public GetBucketInventoryRequest(){
        this(null);
    }

    public void setInventoryId(String inventoryId){
        this.inventoryId = inventoryId;
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("inventory", null);
        queryParameters.put("id", inventoryId);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(inventoryId == null)throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "inventoryId == null");
    }
}
