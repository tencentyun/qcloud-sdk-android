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
import com.tencent.cos.xml.model.tag.WebsiteConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设置存储桶静态网站的请求.
 * @see com.tencent.cos.xml.CosXml#putBucketWebsite(PutBucketWebsiteRequest)
 * @see com.tencent.cos.xml.CosXml#putBucketWebsiteAsync(PutBucketWebsiteRequest, CosXmlResultListener)
 */
public class PutBucketWebsiteRequest extends BucketRequest {

    private WebsiteConfiguration websiteConfiguration;

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

    /**
     * 设置索引文档的对象键后缀
     * @param suffix 索引文档的对象键后缀
     */
    public void setIndexDocument(String suffix){
        if(suffix != null){
            if(websiteConfiguration.indexDocument == null) websiteConfiguration.indexDocument = new WebsiteConfiguration.IndexDocument();
            websiteConfiguration.indexDocument.suffix = suffix;
        }
    }

    /**
     * 设置错误文档的对象键
     * @param key 错误文档的对象键
     */
    public void setErrorDocument(String key){
        if(key != null){
            if(websiteConfiguration.errorDocument == null)websiteConfiguration.errorDocument = new WebsiteConfiguration.ErrorDocument();
            websiteConfiguration.errorDocument.key = key;
        }
    }

    /**
     * 设置重定向所有请求的目标协议
     * @param protocol 重定向所有请求的目标协议
     */
    public void setRedirectAllRequestTo(String protocol){
        if(protocol != null){
            if(websiteConfiguration.redirectAllRequestTo == null) websiteConfiguration.redirectAllRequestTo = new WebsiteConfiguration.RedirectAllRequestTo();
            websiteConfiguration.redirectAllRequestTo.protocol = protocol;
        }
    }

    /**
     * 设置重定向规则配置
     * @param rules 重定向规则配置
     */
    public void setRoutingRules(List<WebsiteConfiguration.RoutingRule> rules){
        if(rules != null && rules.size() > 0){
            if(websiteConfiguration.routingRules == null) websiteConfiguration.routingRules = new ArrayList<>();
            for(WebsiteConfiguration.RoutingRule rule : rules){
                websiteConfiguration.routingRules.add(rule);
            }
        }
    }
}
