package com.tencent.cos.xml.model.ci.ai;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 开通AI 内容识别服务并生成队列的请求.
 * @see com.tencent.cos.xml.CIService#openBucketAi(OpenBucketAiRequest) 
 * @see com.tencent.cos.xml.CIService#openBucketAiAsync(OpenBucketAiRequest, CosXmlResultListener)
 */
public class OpenBucketAiRequest extends BucketRequest {
    public OpenBucketAiRequest(String bucket) {
        this(bucket, null);
    }

    public OpenBucketAiRequest(String bucket, String region) {
        super(bucket);
        this.region = region;
        addNoSignHeader("Content-Type");
        addNoSignHeader("Content-Length");
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/ai_bucket";
    }

    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, bucket, CosXmlServiceConfig.CI_HOST_FORMAT);
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.string(HttpConstants.ContentType.TEXT_PLAIN, "");
    }
}
