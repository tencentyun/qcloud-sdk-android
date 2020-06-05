package com.tencent.cos.xml.model.object;


import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * 实现将一个文件的分块内容从源路径复制到目标路径。
 * </p>
 * <H1>初始化 init multiupload, 获取uploadId</H1>
 * <H1>Upload Part Copy </H1>
 * <H1> 完成 complete multiupload </H1>
 *
 *
 */

public class UploadPartCopyRequest extends CopyObjectRequest {

    /**Specified part number*/
    private int partNumber = -1;
    /**init upload generate' s uploadId by service*/
    private String uploadId = null;

    public UploadPartCopyRequest(String bucket, String cosPath, int partNumber, String uploadId, CopySourceStruct copySourceStruct) throws CosXmlClientException {
       this(bucket, cosPath, partNumber, uploadId, copySourceStruct, -1, -1);
    }

    public UploadPartCopyRequest(String bucket, String cosPath, int partNumber, String uploadId, CopySourceStruct copySourceStruct,
                                 long start, long end){
        super(bucket, cosPath, copySourceStruct);
        this.partNumber = partNumber;
        this.uploadId = uploadId;
        setCopyRange(start, end);
    }

    public UploadPartCopyRequest(){
        super(null, null, null);
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("partNumber", String.valueOf(partNumber));
        queryParameters.put("uploadId",uploadId);
        return super.getQueryString();
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(requestURL != null)return;
        if(partNumber <= 0){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "partNumber must be >= 1");
        }
        if(uploadId == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "uploadID must not be null");
        }
    }

    public void setCopyRange(long start, long end){
        if(start >= 0 && end >= start){
            String bytes = "bytes=" + start + "-" + end;
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE_RANGE, bytes);
        }
    }

}
