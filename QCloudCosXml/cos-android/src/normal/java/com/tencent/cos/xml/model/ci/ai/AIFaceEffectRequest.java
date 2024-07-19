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
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * 人脸特效
 * <a href="https://cloud.tencent.com/document/product/460/47197">人脸特效</a>
 * @see com.tencent.cos.xml.CIService#aiFaceEffect(AIFaceEffectRequest)
 * @see com.tencent.cos.xml.CIService#aiFaceEffectAsync(AIFaceEffectRequest, CosXmlResultListener)
 */
public class AIFaceEffectRequest extends BucketRequest {
    /**
     * 万象处理能力，人脸特效固定为face-effect;是否必传：是
     */
    public String ciProcess = "face-effect";
    /**
     * 对象文件名，例如：folder/document.jpg。
     */
    public String objectKey;
    /**
     * 您可以通过填写 detect-url 处理任意公网可访问的图片链接。不填写 detect-url 时，后台会默认处理 ObjectKey ，填写了 detect-url 时，后台会处理 detect-url 链接，无需再填写 ObjectKey detect-url 示例：http://www.example.com/abc.jpg。;是否必传：否
     */
    public String detectUrl;
    /**
     * 人脸特效类型，人脸美颜：face-beautify；人脸性别转换：face-gender-transformation；人脸年龄变化：face-age-transformation；人像分割：face-segmentation;是否必传：是
     */
    public String type;
    /**
     * type为face-beautify时生效，美白程度，取值范围[0,100]。0不美白，100代表最高程度。默认值30;是否必传：否
     */
    public int whitening;
    /**
     * type为face-beautify时生效，磨皮程度，取值范围[0,100]。0不磨皮，100代表最高程度。默认值10;是否必传：否
     */
    public int smoothing;
    /**
     * type为face-beautify时生效，瘦脸程度，取值范围[0,100]。0不瘦脸，100代表最高程度。默认值70;是否必传：否
     */
    public int faceLifting;
    /**
     * type为face-beautify时生效，大眼程度，取值范围[0,100]。0不大眼，100代表最高程度。默认值70;是否必传：否
     */
    public int eyeEnlarging;
    /**
     * type为face-gender-transformation时生效，选择转换方向，0：男变女，1：女变男。无默认值，为必选项。限制：仅对图片中面积最大的人脸进行转换。;是否必传：否
     */
    public int gender;
    /**
     * type为face-age-transformation时生效，变化到的人脸年龄,[10,80]。无默认值，为必选项。限制：仅对图片中面积最大的人脸进行转换。;是否必传：否
     */
    public int age;

    /**
     * 人脸特效，提供人脸美颜、人像变换、人像分割功能
     *
     * @param bucket  存储桶名
     */
    public AIFaceEffectRequest(@NonNull String bucket) {
        super(bucket);
    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        if(!TextUtils.isEmpty(detectUrl)) {
            queryParameters.put("detect-url", detectUrl);
        }
        queryParameters.put("type", type);
        queryParameters.put("whitening", String.valueOf(whitening));
        queryParameters.put("smoothing", String.valueOf(smoothing));
        queryParameters.put("faceLifting", String.valueOf(faceLifting));
        queryParameters.put("eyeEnlarging", String.valueOf(eyeEnlarging));
        queryParameters.put("gender", String.valueOf(gender));
        queryParameters.put("age", String.valueOf(age));
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
