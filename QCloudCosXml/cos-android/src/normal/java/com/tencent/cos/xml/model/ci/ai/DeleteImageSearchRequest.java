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
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * 删除图库图片
 * <a href="https://cloud.tencent.com/document/product/460/63902">删除图库图片</a>
 * @see com.tencent.cos.xml.CIService#deleteImageSearch(DeleteImageSearchRequest)
 * @see com.tencent.cos.xml.CIService#deleteImageSearchAsync(DeleteImageSearchRequest, CosXmlResultListener)
 */
public class DeleteImageSearchRequest extends BucketRequest {
    private DeleteImageSearch deleteImageSearch;
    protected final String objectKey;
    /**
     * 固定值：ImageSearch;是否必传：是
     */
    public String ciProcess = "ImageSearch";
    /**
     * 固定值：AddImage;是否必传：是
     */
    public String action = "DeleteImage";

    /**
     * 该接口用于删除图库图片
     *
     * @param bucket  存储桶名
     */
    public DeleteImageSearchRequest(@NonNull String bucket , @NonNull String objectKey) {
        super(bucket);
        this.objectKey = objectKey;
    }
    /**
     * 设置 删除图库图片
     * @param deleteImageSearch 删除图库图片
     */
    public void setDeleteImageSearch(@NonNull DeleteImageSearch deleteImageSearch){
        this.deleteImageSearch = deleteImageSearch;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        queryParameters.put("action", action);
        return super.getQueryString();
    }
    
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/" + objectKey;
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException, CosXmlClientException {
        return RequestBodySerializer.bytes(COSRequestHeaderKey.APPLICATION_XML, QCloudXmlUtils.toXml(this.deleteImageSearch).getBytes("utf-8"));
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.POST;
    }
    
}
