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
 * 图像超分
 * <a href="https://cloud.tencent.com/document/product/460/83793">图像超分</a>
 * @see com.tencent.cos.xml.CIService#aiSuperResolution(AISuperResolutionRequest)
 * @see com.tencent.cos.xml.CIService#aiSuperResolutionAsync(AISuperResolutionRequest, CosXmlResultListener)
 */
public class AISuperResolutionRequest extends CiSaveLocalRequest {
    /**
     * 数据万象处理能力，只能裁剪参固定为AISuperResolution。;是否必传：否
     */
    public String ciProcess = "AISuperResolution";
    /**
     * 对象文件名，例如：folder/document.jpg。
     */
    public String objectKey;
    /**
     * 您可以通过填写 detect-url 处理任意公网可访问的图片链接。不填写 detect-url 时，后台会默认处理 ObjectKey ，填写了 detect-url 时，后台会处理 detect-url 链接，无需再填写 ObjectKey，detect-url 示例：http://www.example.com/abc.jpg;是否必传：否
     */
    public String detectUrl;
    /**
     * 目标放大倍数，默认为2，范围[2,4]，仅支持偶数
     */
    public int magnify = -1;

    /**
     * 腾讯云数据万象通过 AISuperResolution 接口对图像进行超分辨率处理，当前默认超分为宽高的2倍
     *
     * @param bucket  存储桶名
     */
    public AISuperResolutionRequest(@NonNull String bucket) {
        super.bucket = bucket;
    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        if(!TextUtils.isEmpty(detectUrl)) {
            queryParameters.put("detect-url", detectUrl);
        }
        if(magnify != -1) {
            queryParameters.put("magnify", String.valueOf(magnify));
        }
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
