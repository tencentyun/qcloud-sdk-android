package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * 舍弃一个分块上传且删除已上传的分片块请求返回的结果.
 * @see AbortMultiUploadRequest
 */
final public class AbortMultiUploadResult extends CosXmlResult {

    /**
     * @see CosXmlResult#parseResponseBody(HttpResponse)
     */
    @Override
    public void parseResponseBody(HttpResponse response)  throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);
    }
}
