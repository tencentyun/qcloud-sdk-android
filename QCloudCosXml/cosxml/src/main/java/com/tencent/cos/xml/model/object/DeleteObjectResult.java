package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * 删除cos上文件返回的结果，
 * 关于删除cos上文件的接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7743">https://cloud.tencent.com/document/product/436/7743.</a><br>
 */
final public class DeleteObjectResult extends CosXmlResult {

    /**
     * @see CosXmlResult#parseResponseBody(HttpResponse)
     */
    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
    }

}
