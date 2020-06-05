package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.MTAProxy;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.CompleteMultipartUploadResult;
import com.tencent.cos.xml.model.tag.CosError;
import com.tencent.cos.xml.transfer.XmlSlimParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

/**
 * 完成整个分片上传返回的结果.<br>
 * 关于完成整个分片上传接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7742">https://cloud.tencent.com/document/product/436/7742.</a><br>
 */
final public class CompleteMultiUploadResult extends CosXmlResult {

    /**
     * 完成整个分片上传返回的所有信息, {@link com.tencent.cos.xml.model.tag.CompleteMultipartUploadResult}
     */
    public CompleteMultipartUploadResult completeMultipartUpload;

    /**
     *  @see CosXmlResult#parseResponseBody(HttpResponse)
     */
    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        InputStream inputStream = null;
        try {
            completeMultipartUpload = new CompleteMultipartUploadResult();
            byte[] contents = response.bytes();
            inputStream = new ByteArrayInputStream(contents);
            XmlSlimParser.parseCompleteMultipartUploadResult(inputStream, completeMultipartUpload);
            if(completeMultipartUpload.eTag == null || completeMultipartUpload.key == null
                    || completeMultipartUpload.bucket == null){
                if(contents != null && contents.length > 0){
                    inputStream.reset();
                    CosXmlServiceException cosXmlServiceException = new CosXmlServiceException("failed");
                    CosError cosError = new CosError();
                    XmlSlimParser.parseError(inputStream, cosError);
                    cosXmlServiceException.setErrorCode(cosError.code);
                    cosXmlServiceException.setErrorMessage(cosError.message);
                    cosXmlServiceException.setRequestId(cosError.requestId);
                    cosXmlServiceException.setServiceName(cosError.resource);
                    cosXmlServiceException.setStatusCode(response.code());
                    throw cosXmlServiceException;
                }
            }
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    /**
     *  @see CosXmlResult#printResult()
     */
    @Override
    public String printResult() {
        return completeMultipartUpload != null ? completeMultipartUpload.toString() : super.printResult();
    }
}
