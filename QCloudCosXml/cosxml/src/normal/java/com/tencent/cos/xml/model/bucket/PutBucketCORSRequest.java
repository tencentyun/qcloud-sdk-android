package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.CORSConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设置 Bucket 的跨域资源共享权限。
 * </p>
 *
 * <p>
 * 使用 PutBucketCORS 接口创建的规则权限是覆盖当前的所有规则而不是新增一条权限规则。
 * </p>
 *
*/
final public class PutBucketCORSRequest extends BucketRequest {

    private CORSConfiguration corsConfiguration;

    public PutBucketCORSRequest(String bucket) {
        super(bucket);
        corsConfiguration = new CORSConfiguration();
        corsConfiguration.corsRules = new ArrayList<>();
    }

    public PutBucketCORSRequest(){
        super(null);
        corsConfiguration = new CORSConfiguration();
        corsConfiguration.corsRules = new ArrayList<>();
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("cors", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildCORSConfigurationXML(corsConfiguration));
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    /**
     * 添加多条CORS
     *
     * @param corsRules
     */
    public void addCORSRules(List<CORSConfiguration.CORSRule> corsRules) {
        if(corsRules != null){
            this.corsConfiguration.corsRules.addAll(corsRules);
        }
    }

    /**
     * 添加一条CORSRule
     *
     * @param corsRule
     */
    public void addCORSRule(CORSConfiguration.CORSRule corsRule) {
        if(corsRule != null){
            this.corsConfiguration.corsRules.add(corsRule);
        }
    }

    /**
     * 获取添加的CORSRule列表
     */
    public CORSConfiguration getCorsConfiguration() {
        return corsConfiguration;
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }
}
