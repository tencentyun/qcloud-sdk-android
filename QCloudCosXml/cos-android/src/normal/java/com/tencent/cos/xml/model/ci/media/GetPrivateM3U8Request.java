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

package com.tencent.cos.xml.model.ci.media;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * 获取私有 m3u8
 * <a href="https://cloud.tencent.com/document/product/460/63738">获取私有 m3u8</a>
 * @see com.tencent.cos.xml.CIService#getPrivateM3U8(GetPrivateM3U8Request)
 * @see com.tencent.cos.xml.CIService#getPrivateM3U8Async(GetPrivateM3U8Request, CosXmlResultListener)
 */
public class GetPrivateM3U8Request extends BucketRequest {
    protected final String objectKey;

    /**
     * 操作类型，固定使用 pm3u8
     */
    public String ci_process = "pm3u8";
    /**
     * 私有 ts 资源 url 下载凭证的相对有效期，单位为秒，范围为[3600, 43200]
     */
    public String expires;

    /**
     * GetPrivateM3U8 接口用于获取私有 M3U8 ts 资源的下载授权。（此方式通过对象存储转发请求至数据万象）
     *
     * @param bucket  存储桶名
     */
    public GetPrivateM3U8Request(@NonNull String bucket , @NonNull String objectKey) {
        super(bucket);
        this.objectKey = objectKey;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ci_process);
        queryParameters.put("expires", expires);
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
