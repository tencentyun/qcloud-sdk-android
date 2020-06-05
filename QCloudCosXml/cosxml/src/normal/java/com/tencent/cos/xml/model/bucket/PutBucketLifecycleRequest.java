package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.cos.xml.model.tag.LifecycleConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;


import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * COS 支持用户以生命周期配置的方式来管理 Bucket 中 Object 的生命周期。
 * </p>
 *
 * <p>
 * 生命周期配置包含一个或多个将应用于一组对象规则的规则集 (其中每个规则为 COS 定义一个操作)。
 * </p>
 *
 * 这些操作分为以下两种：
 * <ul>
 * <li>
 * 转换操作：定义对象转换为另一个存储类的时间。例如，您可以选择在对象创建 30 天后将其转换为 STANDARD_IA (IA，适用于不常访问) 存储类别。
 * </li>
 * <li>
 * 过期操作：指定 Object 的过期时间。COS 将会自动为用户删除过期的 Object。
 * </li>
 * </ul>
 *
 * <p>
 * PutBucketLifecycle 用于为 Bucket 创建一个新的生命周期配置。如果该 Bucket 已配置生命周期，使用该接口创建新的配置的同时则会覆盖原有的配置。
 * </p>
 *
 */
final public class PutBucketLifecycleRequest extends BucketRequest {

    private LifecycleConfiguration lifecycleConfiguration;

    public PutBucketLifecycleRequest(String bucket){
        super(bucket);
        lifecycleConfiguration = new LifecycleConfiguration();
        lifecycleConfiguration.rules = new ArrayList<>();
    }

    public PutBucketLifecycleRequest(){
        super(null);
        lifecycleConfiguration = new LifecycleConfiguration();
        lifecycleConfiguration.rules = new ArrayList<>();
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("lifecycle", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildLifecycleConfigurationXML(lifecycleConfiguration));
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    /**
     * 添加多条生命周期规则
     *
     * @param ruleList
     */
    public void setRuleList(List<LifecycleConfiguration.Rule> ruleList){
        if(ruleList != null){
            this.lifecycleConfiguration.rules.addAll(ruleList);
        }
    }

    /**
     * 添加一条生命周期规则
     *
     * @param rule
     */
    public void setRuleList(LifecycleConfiguration.Rule rule){
        if(rule != null){
            this.lifecycleConfiguration.rules.add(rule);
        }
    }

    /**
     * 获取添加的生命周期规则
     */
    public LifecycleConfiguration getLifecycleConfiguration() {
        return lifecycleConfiguration;
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }
}
