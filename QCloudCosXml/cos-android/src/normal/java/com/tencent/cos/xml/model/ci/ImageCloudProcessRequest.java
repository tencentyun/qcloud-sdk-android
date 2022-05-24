package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.object.ObjectRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 持久化处理请求
 */
@Deprecated
abstract public class ImageCloudProcessRequest extends ObjectRequest {

    /**
     * 对象相关请求基类
     *
     * @param bucket  存储桶名
     * @param cosPath cos上的路径
     */
    public ImageCloudProcessRequest(String bucket, String cosPath) {
        super(bucket, cosPath);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }
    
    protected abstract void addCiParams();

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        addCiParams();
    }
    
    
}
