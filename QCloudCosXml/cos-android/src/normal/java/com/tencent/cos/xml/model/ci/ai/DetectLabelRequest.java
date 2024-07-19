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
 * 图片标签
 * <a href="https://cloud.tencent.com/document/product/460/39082">图片标签</a>
 * @see com.tencent.cos.xml.CIService#detectLabel(DetectLabelRequest)
 * @see com.tencent.cos.xml.CIService#detectLabelAsync(DetectLabelRequest, CosXmlResultListener)
 */
public class DetectLabelRequest extends BucketRequest {
    /**
     * 固定值detect-label;是否必传：是
     */
    public String ciProcess = "detect-label";
    /**
     * 对象文件名，例如：folder/document.jpg。
     */
    public String objectKey;
    /**
     * 本次调用支持的识别场景，可选值如下：web，针对网络图片优化；camera，针对手机摄像头拍摄图片优化；album，针对手机相册、网盘产品优化；news，针对新闻、资讯、广电等行业优化；如果不传此参数，则默认为camera。支持多场景（scenes）一起检测，以，分隔。例如，使用 scenes=web，camera 即对一张图片使用两个模型同时检测，输出两套识别结果。;是否必传：否
     */
    public String scenes;
    /**
     * 您可以通过填写 detect-url 处理任意公网可访问的图片链接。不填写 detect-url 时，后台会默认处理 ObjectKey ，填写了 detect-url 时，后台会处理 detect-url 链接，无需再填写 ObjectKey detect-url 示例：http://www.example.com/abc.jpg;是否必传：否
     */
    public String detectUrl;

    /**
     * 图片标签功能通过借助数据万象的持久化处理接口，实现对 COS 存量数据的图片标签识别，返回图片中置信度较高的主题标签。图片标签识别请求包属于 GET 请求，请求时需要携带签名
     *
     * @param bucket  存储桶名
     */
    public DetectLabelRequest(@NonNull String bucket) {
        super(bucket);
    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        queryParameters.put("scenes", scenes);
        if(!TextUtils.isEmpty(detectUrl)) {
            queryParameters.put("detect-url", detectUrl);
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
