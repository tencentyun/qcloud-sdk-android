package com.tencent.cos.xml.model.ci.ai;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.ci.ai.bean.OpenBucketAiResponse;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

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
        try {
            openBucketAiResponse = QCloudXml.fromXml(response.byteStream(), OpenBucketAiResponse.class);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }
}
