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

package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * 获取对象标签的请求.
 * @see com.tencent.cos.xml.CosXml#getObjectTagging(GetObjectTaggingRequest)
 * @see com.tencent.cos.xml.CosXml#getObjectTaggingAsync(GetObjectTaggingRequest, CosXmlResultListener)
 */
final public class GetObjectTaggingRequest extends ObjectRequest {
    private String versionId;

    public GetObjectTaggingRequest(String bucket, String cosPath){
        super(bucket, cosPath);
    }

    /**
     * 设置对象版本 ID
     * @param versionId 对象版本 ID
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("tagging", null);

        if(versionId != null){
            queryParameters.put("versionId",versionId);
        }

        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }
}
