package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * <p>
 * 删除cos上一个文件, 关于删除cos上文件的接口的描述，
 * 请查看 <a href="https://cloud.tencent.com/document/product/436/7743">https://cloud.tencent.com/document/product/436/7743.</a><br>
 * </p>
 */
final public class DeleteObjectRequest extends ObjectRequest {

    public DeleteObjectRequest(String bucket, String cosPath){
        super(bucket, cosPath);
    }

    public DeleteObjectRequest(){
        super(null, null);
    }

    /**
     * @see CosXmlRequest#getMethod()
     */
    @Override
    public String getMethod() {
        return RequestMethod.DELETE;
    }

    /**
     * @see CosXmlRequest#getRequestBody()
     */
    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

    /**
     * 删除指定版本的object.
     * @param versionId
     */
    public void setVersionId(String versionId){
        if(versionId != null){
            this.queryParameters.put("versionId", versionId);
        }
    }

}
