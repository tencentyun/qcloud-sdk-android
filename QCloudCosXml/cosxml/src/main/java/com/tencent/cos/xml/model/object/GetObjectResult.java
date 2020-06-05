package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * 下载请求返回结果，
 * 关于下载接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7753">https://cloud.tencent.com/document/product/436/7753.</a><br>
 */
final public class GetObjectResult extends CosXmlResult {

    /**
     *  @see CosXmlResult#parseResponseBody(HttpResponse)
     */
    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
    }

    /**
     *  @see CosXmlResult#printResult()
     */
    @Override
    public String printResult() {
        return super.printResult();
    }
}
