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
 * 图片搜索接口
 * <a href="https://cloud.tencent.com/document/product/460/63901">图片搜索接口</a>
 * @see com.tencent.cos.xml.CIService#getSearchImage(GetSearchImageRequest)
 * @see com.tencent.cos.xml.CIService#getSearchImageAsync(GetSearchImageRequest, CosXmlResultListener)
 */
public class GetSearchImageRequest extends BucketRequest {
    
    protected final String objectKey;

    /**
     * 出参 Score 中，只有超过 MatchThreshold 值的结果才会返回。默认为0;是否必传：否
     */
    public int matchThreshold;
    /**
     * 起始序号，默认值为0;是否必传：否
     */
    public int offset;
    /**
     * 返回数量，默认值为10，最大值为100;是否必传：否
     */
    public int limit;
    /**
     * 针对入库时提交的 Tags 信息进行条件过滤。支持>、>=、<、<=、=、!=，多个条件之间支持 AND 和 OR 进行连接;是否必传：否
     */
    public String filter;

    /**
     * 固定值：ImageSearch;是否必传：是
     */
    public String ciProcess = "ImageSearch";
    /**
     * 固定值：AddImage;是否必传：是
     */
    public String action = "SearchImage";

    /**
     * 该接口用于检索图片
     *
     * @param bucket  存储桶名
     */
    public GetSearchImageRequest(@NonNull String bucket , @NonNull String objectKey) {
        super(bucket);
        this.objectKey = objectKey;

    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        queryParameters.put("action", action);
        queryParameters.put("MatchThreshold", String.valueOf(matchThreshold));
        queryParameters.put("Offset", String.valueOf(offset));
        queryParameters.put("Limit", String.valueOf(limit));
        if(!TextUtils.isEmpty(filter)){
            queryParameters.put("Filter", filter);
        }
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/" + objectKey;
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
