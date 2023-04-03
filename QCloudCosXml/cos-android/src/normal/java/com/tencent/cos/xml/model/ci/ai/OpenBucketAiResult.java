package com.tencent.cos.xml.model.ci.ai;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.ci.ai.bean.OpenBucketAiResponse;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * 开通AI 内容识别服务并生成队列的返回结果.
 * @see com.tencent.cos.xml.CIService#openBucketAi(OpenBucketAiRequest)
 * @see OpenBucketAiRequest
 */
public class OpenBucketAiResult extends CosXmlResult {
    public OpenBucketAiResponse openBucketAiResponse;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);
        openBucketAiResponse = QCloudXmlUtils.fromXml(response.byteStream(), OpenBucketAiResponse.class);
    }
}
