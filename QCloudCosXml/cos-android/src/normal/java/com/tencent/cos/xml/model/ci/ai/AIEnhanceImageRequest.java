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

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.ci.CiSaveLocalRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * 图像增强
 * <a href="https://cloud.tencent.com/document/product/460/83792">图像增强</a>
 * @see com.tencent.cos.xml.CIService#aiEnhanceImage(AIEnhanceImageRequest)
 * @see com.tencent.cos.xml.CIService#aiEnhanceImageAsync(AIEnhanceImageRequest, CosXmlResultListener)
 */
public class AIEnhanceImageRequest extends CiSaveLocalRequest {
    /**
     * 数据万象处理能力，只能裁剪参固定为 AIEnhanceImage。;是否必传：是
     */
    public String ciProcess = "AIEnhanceImage";
    /**
     * 对象文件名，例如：folder/document.jpg。
     */
    public String objectKey;
    /**
     * 去噪强度值，取值范围为 0 - 5 之间的整数，值为 0 时不进行去噪操作，默认值为3。;是否必传：否
     */
    public int denoise;
    /**
     * 锐化强度值，取值范围为 0 - 5 之间的整数，值为 0 时不进行锐化操作，默认值为3。;是否必传：否
     */
    public int sharpen;
    /**
     * 您可以通过填写 detect-url 处理任意公网可访问的图片链接。不填写 detect-url  时，后台会默认处理 ObjectKey ，填写了detect-url 时，后台会处理 detect-url链接，无需再填写 ObjectKey ，detect-url 示例：http://www.example.com/abc.jpg;是否必传：否
     */
    public String detectUrl;
    /**
     * ;是否必传：否
     */
    public int ignoreError;

    /**
     * 腾讯云数据万象通过 AIEnhanceImage 接口对图像进行增强处理
     *
     * @param bucket  存储桶名
     */
    public AIEnhanceImageRequest(@NonNull String bucket) {
        super.bucket = bucket;
    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        queryParameters.put("denoise", String.valueOf(denoise));
        queryParameters.put("sharpen", String.valueOf(sharpen));
        if(!TextUtils.isEmpty(detectUrl)) {
            queryParameters.put("detect-url", detectUrl);
        }
        queryParameters.put("ignore-error", String.valueOf(ignoreError));
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        if(TextUtils.isEmpty(objectKey)){
            return "/";
        } else {
            return "/" + objectKey;
        }
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
