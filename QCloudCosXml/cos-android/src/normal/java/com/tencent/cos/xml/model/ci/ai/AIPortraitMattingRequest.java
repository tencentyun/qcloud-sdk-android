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
 * 人像抠图
 * <a href="https://cloud.tencent.com/document/product/460/106751">人像抠图</a>
 * @see com.tencent.cos.xml.CIService#aiPortraitMatting(AIPortraitMattingRequest)
 * @see com.tencent.cos.xml.CIService#aiPortraitMattingAsync(AIPortraitMattingRequest, CosXmlResultListener)
 */
public class AIPortraitMattingRequest extends CiSaveLocalRequest {

    protected final String objectKey;

    public String ciProcess = "AIPortraitMatting";
    /**
     * 您可以通过填写 detect-url 处理任意公网可访问的图片链接。不填写 detect-url 时，后台会默认处理 ObjectKey ，填写了 detect-url 时，后台会处理 detect-url 链接，无需再填写 ObjectKey。
     * detect-url 示例：http://www.example.com/abc.jpg，需要进行 UrlEncode，处理后为http%25253A%25252F%25252Fwww.example.com%25252Fabc.jpg
     */
    public String detectUrl;
    /**
     * 抠图主体居中显示；值为true时居中显示，值为false不做处理，默认为false
     */
    public boolean centerLayout;
    /**
     * - 将处理后的图片四边进行留白，形式为 paddingLayout = <dx> x <dy>，左右两边各进行 dx 像素的留白，上下两边各进行 dy 像素的留白，例如：padding-layout = 20 x 10
     * - 默认不进行留白操作，dx、dy 最大值为1000像素
     */
    public String paddingLayout;

    /**
     * 腾讯云数据万象通过 AIPortraitMatting 接口检测图片中的人像主体信息，智能分割图像背景，生成只包含人像主体信息的图片，支持持久化、云上处理及下载时处理
     *
     * @param bucket  存储桶名
     */
    public AIPortraitMattingRequest(@NonNull String bucket , String objectKey) {
        super.bucket = bucket;
        this.objectKey = objectKey;

    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        if(!TextUtils.isEmpty(detectUrl)) {
            queryParameters.put("detect-url", detectUrl);
        }
        queryParameters.put("center-layout", centerLayout?"1":"0");
        if(!TextUtils.isEmpty(paddingLayout)) {
            queryParameters.put("padding-layout", paddingLayout);
        }
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        if(!TextUtils.isEmpty(detectUrl) || TextUtils.isEmpty(objectKey)){
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
