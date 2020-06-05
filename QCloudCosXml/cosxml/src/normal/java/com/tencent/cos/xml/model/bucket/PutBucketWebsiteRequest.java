package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.WebsiteConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bradyxiao on 2019/8/19.
 * Copyright (c) 2016-2020 Tencent QCloud All rights reserved.
 */
public class PutBucketWebsiteRequest extends BucketRequest {

    private WebsiteConfiguration websiteConfiguration;

    public PutBucketWebsiteRequest(){
        this(null);
    }

    public PutBucketWebsiteRequest(String bucket) {
        super(bucket);
        websiteConfiguration = new WebsiteConfiguration();
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildWebsiteConfiguration(websiteConfiguration));
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    @Override
    public Map<String, String> getQueryString() {
        this.queryParameters.put("website", null);
        return super.getQueryString();
    }

    public void setIndexDocument(String suffix){
        if(suffix != null){
            if(websiteConfiguration.indexDocument == null) websiteConfiguration.indexDocument = new WebsiteConfiguration.IndexDocument();
            websiteConfiguration.indexDocument.suffix = suffix;
        }
    }

    public void setErrorDocument(String key){
        if(key != null){
            if(websiteConfiguration.errorDocument == null)websiteConfiguration.errorDocument = new WebsiteConfiguration.ErrorDocument();
            websiteConfiguration.errorDocument.key = key;
        }
    }

    public void setRedirectAllRequestTo(String protocol){
        if(protocol != null){
            if(websiteConfiguration.redirectAllRequestTo == null) websiteConfiguration.redirectAllRequestTo = new WebsiteConfiguration.RedirectAllRequestTo();
            websiteConfiguration.redirectAllRequestTo.protocol = protocol;
        }
    }

    public void setRoutingRules(List<WebsiteConfiguration.RoutingRule> rules){
        if(rules != null && rules.size() > 0){
            if(websiteConfiguration.routingRules == null) websiteConfiguration.routingRules = new ArrayList<>();
            for(WebsiteConfiguration.RoutingRule rule : rules){
                websiteConfiguration.routingRules.add(rule);
            }
        }
    }
}
