/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.LifecycleConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设置存储桶（Bucket) 生命周期配置的请求.
 * @see com.tencent.cos.xml.CosXml#putBucketLifecycle(PutBucketLifecycleRequest)
 * @see com.tencent.cos.xml.CosXml#putBucketLifecycleAsync(PutBucketLifecycleRequest, CosXmlResultListener)
 */
final public class PutBucketLifecycleRequest extends BucketRequest {

    private LifecycleConfiguration lifecycleConfiguration;

    public PutBucketLifecycleRequest(String bucket){
        super(bucket);
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
     * @param ruleList 生命周期规则列表
     */
    public void setRuleList(List<LifecycleConfiguration.Rule> ruleList){
        if(ruleList != null){
            this.lifecycleConfiguration.rules.addAll(ruleList);
        }
    }

    /**
     * 添加一条生命周期规则
     *
     * @param rule 生命周期规则
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
