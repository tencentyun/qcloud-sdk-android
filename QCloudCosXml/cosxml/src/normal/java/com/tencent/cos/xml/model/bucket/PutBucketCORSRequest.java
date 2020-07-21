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
import com.tencent.cos.xml.model.tag.CORSConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设置存储桶（Bucket） 的跨域配置信息的请求.
 * @see com.tencent.cos.xml.CosXml#putBucketCORS(PutBucketCORSRequest)
 * @see com.tencent.cos.xml.CosXml#putBucketCORSAsync(PutBucketCORSRequest, CosXmlResultListener)
 */
final public class PutBucketCORSRequest extends BucketRequest {

    private CORSConfiguration corsConfiguration;

    public PutBucketCORSRequest(String bucket) {
        super(bucket);
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
     * 添加多条CORSRule
     * @param corsRules 具体的跨域资源共享配置
     */
    public void addCORSRules(List<CORSConfiguration.CORSRule> corsRules) {
        if(corsRules != null){
            this.corsConfiguration.corsRules.addAll(corsRules);
        }
    }

    /**
     * 添加一条CORSRule
     * @param corsRule 具体的跨域资源共享配置
     */
    public void addCORSRule(CORSConfiguration.CORSRule corsRule) {
        if(corsRule != null){
            this.corsConfiguration.corsRules.add(corsRule);
        }
    }

    /**
     * 获取跨域资源共享配置
     */
    public CORSConfiguration getCorsConfiguration() {
        return corsConfiguration;
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }
}
