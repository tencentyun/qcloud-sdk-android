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
 * 实时文字翻译
 * <a href="https://cloud.tencent.com/document/product/460/83547">实时文字翻译</a>
 * @see com.tencent.cos.xml.CIService#autoTranslationBlock(AutoTranslationBlockRequest)
 * @see com.tencent.cos.xml.CIService#autoTranslationBlockAsync(AutoTranslationBlockRequest, CosXmlResultListener)
 */
public class AutoTranslationBlockRequest extends BucketRequest {
    

    /**
     * 数据万象处理能力，文本块翻译固定为 AutoTranslationBlock。;是否必传：是
     */
    public String ciProcess = "AutoTranslationBlock";
    /**
     * 待翻译的文本;是否必传：是
     */
    public String inputText;
    /**
     * 输入语言，如 "zh";是否必传：是
     */
    public String sourceLang;
    /**
     * 输出语言，如 "en";是否必传：是
     */
    public String targetLang;
    /**
     * 文本所属业务领域，如: "ecommerce", //缺省值为 general;是否必传：否
     */
    public String textDomain;
    /**
     * 文本类型，如: "title", //缺省值为 sentence;是否必传：否
     */
    public String textStyle;

    /**
     * 腾讯云数据万象通过 AutoTranslationBlock  接口对文字块进行翻译，请求时需要携带签名
     *
     * @param bucket  存储桶名
     */
    public AutoTranslationBlockRequest(@NonNull String bucket ) {
        super(bucket);
        
    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        queryParameters.put("InputText", inputText);
        queryParameters.put("SourceLang", sourceLang);
        queryParameters.put("TargetLang", targetLang);
        queryParameters.put("TextDomain", textDomain);
        queryParameters.put("TextStyle", textStyle);
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
