package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * 上传某个分块请求返回的结果.<br>
 * 关于上传某个分块接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7750">https://cloud.tencent.com/document/product/436/7750.</a><br>
 */
final public class UploadPartResult extends CosXmlResult {

    /**
     * 上传某个分片返回的 eTag
     */
    public String eTag;

    /**
     *  @see CosXmlResult#parseResponseBody(HttpResponse)
     */
    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        eTag = response.header("ETag");
    }

    /**
     *  @see CosXmlResult#printResult()
     */
    @Override
    public String printResult() {
        return eTag;
    }
}
