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

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.CosXmlRequest;
import java.util.List;

/**
 * 对象相关请求基类
 */
public abstract class ObjectRequest extends CosXmlRequest {

    /**
     * 对象在cos上的路径
     */
    String cosPath;

    /**
     * 对象相关请求基类
     * @param bucket 存储桶名
     * @param cosPath cos上的路径
     */
    public ObjectRequest(String bucket, String cosPath){
        this.bucket = bucket;
        this.cosPath = cosPath;
    }

    /**
     * 支持 CSP 的 Bucket 可能在 path 上
     * @param config 服务配置
     * @return path
     */
    @Override
    public String getPath(CosXmlServiceConfig config) {
        return config.getUrlPath(bucket, cosPath);
    }


    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(requestURL != null){
            return;
        }
        if(bucket == null || bucket.length() < 1){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT, "bucket must not be null ");
        }
        if(cosPath == null || cosPath.length() < 1){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT, "cosPath must not be null ");
        }
    }

    protected String getContentType() {
        List<String> contentType = requestHeaders.get("Content-Type");
        if (contentType == null || contentType.isEmpty()) {
            contentType = requestHeaders.get("content-type");
        }
        if (contentType == null || contentType.isEmpty()) {
            contentType = requestHeaders.get("Content-type");
        }

        return contentType != null && !contentType.isEmpty() ? contentType.get(0) : null;
    }
}
