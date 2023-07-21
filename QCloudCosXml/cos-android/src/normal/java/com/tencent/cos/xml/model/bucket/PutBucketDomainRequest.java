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
import com.tencent.cos.xml.model.tag.DomainConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 为存储桶配置自定义域名的请求.
 * @see com.tencent.cos.xml.CosXml#putBucketDomain(PutBucketDomainRequest)
 * @see com.tencent.cos.xml.CosXml#putBucketDomainAsync(PutBucketDomainRequest, CosXmlResultListener)
 */
public class PutBucketDomainRequest extends BucketRequest {
    private DomainConfiguration domainConfiguration;

    public PutBucketDomainRequest(String bucket){
        super(bucket);
        domainConfiguration = new DomainConfiguration();
        domainConfiguration.domainRules = new ArrayList<>();
    }
    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                XmlBuilder.buildDomainConfiguration(domainConfiguration));
    }

    /**
     * 添加自定义域名规则
     * @param domainRule 自定义域名规则
     */
    public void addDomainRule(DomainConfiguration.DomainRule domainRule) {
        if(domainRule != null) {
            domainConfiguration.domainRules.add(domainRule);
        }
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if (domainConfiguration.domainRules.size() > 0)
        {
            for(DomainConfiguration.DomainRule rule : domainConfiguration.domainRules)
            {
                if(rule.status == null) throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "DomainRule.status is null");
                if (rule.name == null) throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "DomainRule.name is null");
                if (rule.type == null) throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "DomainRule.type is null");
            }
        }
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("domain", null);
        return super.getQueryString();
    }
}
