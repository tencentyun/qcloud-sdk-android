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

package com.tencent.cos.xml.model.ci.ai;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * Logo 识别
 * <a href="https://cloud.tencent.com/document/product/460/79736">Logo 识别</a>
 * @see com.tencent.cos.xml.CIService#recognizeLogo(RecognizeLogoRequest)
 * @see com.tencent.cos.xml.CIService#recognizeLogoAsync(RecognizeLogoRequest, CosXmlResultListener)
 */
public class RecognizeLogoRequest extends BucketRequest {
    

    /**
     * 数据万象处理能力，Logo识别固定为RecognizeLogo;是否必传：是
     */
    public String ciProcess = "RecognizeLogo";
    /**
     * 待检查图片url;是否必传：是
     */
    public String detectUrl;

    /**
     * 腾讯云数据万象通过 RecognizeLogo 接口实现对图片内电商 Logo 的识别，返回图片中 Logo 的名称、坐标、置信度分值。，返回图片中Logo的名称、坐标、置信度分值。图片Logo识别请求包属于 GET 请求，请求时需要携带签名
     *
     * @param bucket  存储桶名
     */
    public RecognizeLogoRequest(@NonNull String bucket ) {
        super(bucket);
        
    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        queryParameters.put("detect-url", detectUrl);
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/";
    }
    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }
    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.GET;
    }
    
}
