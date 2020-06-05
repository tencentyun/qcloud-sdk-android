package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * 舍弃一个分块上传且删除已上传的分片块构造类. 该类为{@link com.tencent.cos.xml.SimpleCosXml#abortMultiUpload(AbortMultiUploadRequest)}
 * 或者 {@link com.tencent.cos.xml.SimpleCosXml#completeMultiUploadAsync(CompleteMultiUploadRequest, CosXmlResultListener)}
 * 方法提供需要的参数.如存储桶名称Bucket、存储到 COS 上的绝对路径 cosPath、初始化分片返回的 uploadId
 * 等.
 * @see com.tencent.cos.xml.SimpleCosXml#abortMultiUpload(AbortMultiUploadRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#abortMultiUploadAsync(AbortMultiUploadRequest, CosXmlResultListener)
*/
final public class AbortMultiUploadRequest extends BaseMultipartUploadRequest {

    /** 初始化分片返回的 uploadId */
    private String uploadId;

    /**
     * 舍弃一个分块上传且删除已上传的分片块构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param uploadId 初始化分片返回的 uploadId
     */
    public AbortMultiUploadRequest(String bucket, String cosPath, String uploadId) {
        super(bucket, cosPath);
        this.uploadId = uploadId;
    }

    public AbortMultiUploadRequest() {
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
     * @see CosXmlRequest#getQueryString()
     */
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("uploadId",uploadId);
        return queryParameters;
    }

    /**
     * @see CosXmlRequest#getRequestBody()
     */
    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

    /**
     * @see CosXmlRequest#checkParameters()
     */
    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(requestURL != null){
            return;
        }
        if(uploadId == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "uploadID must not be null");
        }
    }


    /**
     * 舍弃一个上传分块且删除已上传的分块，需要指定是那个分块上传对象的uploadId.
     * @param uploadId 设置分片上传的uploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * 获取设置的分片上传的uploadId
     */
    public String getUploadId() {
        return uploadId;
    }

}
