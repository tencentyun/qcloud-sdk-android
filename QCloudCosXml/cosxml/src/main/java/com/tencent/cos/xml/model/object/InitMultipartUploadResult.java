package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.InitiateMultipartUpload;
import com.tencent.cos.xml.transfer.XmlSlimParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * <p>
 * 初始化上传请求返回的结果信息类。
 * 关于初始化分片上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7746">https://cloud.tencent.com/document/product/436/7746.</a><br>
 *</p>
 * 另请参阅{@link InitiateMultipartUpload}
 */
final public class InitMultipartUploadResult extends CosXmlResult {

    /**
     * 初始化上传请求返回的信息
     */
    public InitiateMultipartUpload initMultipartUpload;

    /**
     *  @see CosXmlResult#parseResponseBody(HttpResponse)
     */
    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        initMultipartUpload = new InitiateMultipartUpload();
        try {
            XmlSlimParser.parseInitiateMultipartUploadResult(response.byteStream(), initMultipartUpload);
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
       return initMultipartUpload != null ? initMultipartUpload.toString() : super.printResult();
    }
}
