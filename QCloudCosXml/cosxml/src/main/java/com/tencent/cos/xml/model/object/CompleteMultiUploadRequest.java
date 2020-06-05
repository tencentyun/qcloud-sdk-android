package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.tag.CompleteMultipartUpload;
import com.tencent.cos.xml.transfer.XmlSlimBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.task.QCloudTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 完成整个分块上传构造类.该类为{@link com.tencent.cos.xml.SimpleCosXml#completeMultiUpload(CompleteMultiUploadRequest)}
 * 或者 {@link com.tencent.cos.xml.SimpleCosXml#completeMultiUploadAsync(CompleteMultiUploadRequest, CosXmlResultListener)}
 * 方法提供需要的参数.如存储桶名称Bucket、存储到 COS 上的绝对路径 cosPath、初始化分片返回的 uploadId、
 * 各个分片块的编码 partNumber 和 MD5等.
 * @see com.tencent.cos.xml.SimpleCosXml#completeMultiUpload(CompleteMultiUploadRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#completeMultiUploadAsync(CompleteMultiUploadRequest, CosXmlResultListener)
 */
final public class CompleteMultiUploadRequest extends BaseMultipartUploadRequest{

    /**
     * @see CompleteMultipartUpload
     */
    private CompleteMultipartUpload completeMultipartUpload;

    /** 初始化分片返回的 uploadId */
    private String uploadId;

    /**
     * 完成整个分块上传构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param uploadId 初始化分片上传，返回的 uploadId
     * @param partNumberAndETag 分片编号 和对应的分片 MD5 值
     */
    public CompleteMultiUploadRequest(String bucket, String cosPath, String uploadId, Map<Integer,String> partNumberAndETag ){
        super(bucket,cosPath);
        this.uploadId = uploadId;
        completeMultipartUpload = new CompleteMultipartUpload();
        completeMultipartUpload.parts = new ArrayList<CompleteMultipartUpload.Part>();
        setPartNumberAndETag(partNumberAndETag);
    }

    public CompleteMultiUploadRequest() {
        super(null, null);
        completeMultipartUpload = new CompleteMultipartUpload();
        completeMultipartUpload.parts = new ArrayList<CompleteMultipartUpload.Part>();
    }

    /***/
    public CompleteMultipartUpload getCompleteMultipartUpload() {
        return completeMultipartUpload;
    }

    /**
     * 添加单个分块的eTag值
     * @param partNumbers 分块编号
     * @param eTag 该分块的eTag值
     */
    public void setPartNumberAndETag(int partNumbers, String eTag){
        CompleteMultipartUpload.Part part = new CompleteMultipartUpload.Part();
        part.partNumber = partNumbers;
        part.eTag = eTag;
        completeMultipartUpload.parts.add(part);
    }

    /**
     * 添加多个分块的编号 和 eTag值
     */
    public void setPartNumberAndETag(Map<Integer,String> partNumberAndETag){
        if(partNumberAndETag != null){
            CompleteMultipartUpload.Part part;
            for(Map.Entry<Integer,String> entry : partNumberAndETag.entrySet()){
                part = new CompleteMultipartUpload.Part();
                part.partNumber = entry.getKey();
                part.eTag = entry.getValue();
                completeMultipartUpload.parts.add(part);
            }
        }
    }

    /**
     * 设置该分块上传的uploadId
     * @param uploadId 分块上传的UploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * 获取用户设置的该分块上传的uploadId
     * @return 分块上传的UploadId
     */
    public String getUploadId() {
        return uploadId;
    }

    /**
     * @see CosXmlRequest#getMethod()
     */
    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }


    /**
     * @see CosXmlRequest#getQueryString()
     */
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("uploadId", uploadId);
        return queryParameters;
    }

    /**
     * @see CosXmlRequest#getRequestBody()
     */
    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
//            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
//                    XmlSlimBuilder.buildCompleteMultipartUpload(completeMultipartUpload));
            return RequestBodySerializer.bytes(COSRequestHeaderKey.APPLICATION_XML, XmlSlimBuilder.buildCompleteMultipartUpload(completeMultipartUpload).getBytes("utf-8"));
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
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

    @Override
    public int getPriority() {
        return QCloudTask.PRIORITY_HIGH;
    }
}
