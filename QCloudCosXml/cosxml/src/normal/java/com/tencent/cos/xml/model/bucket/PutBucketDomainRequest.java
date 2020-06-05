package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.DomainConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by bradyxiao on 2019/9/3.
 * Copyright (c) 2016-2020 Tencent QCloud All rights reserved.
 */
public class PutBucketDomainRequest extends BucketRequest {

    private DomainConfiguration domainConfiguration;
    public PutBucketDomainRequest(){
        this(null);
    }

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
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildDomainConfiguration(domainConfiguration));
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    public void addDomainRule(DomainConfiguration.DomainRule domainRule)
    {
        if(domainRule != null)
        {
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
