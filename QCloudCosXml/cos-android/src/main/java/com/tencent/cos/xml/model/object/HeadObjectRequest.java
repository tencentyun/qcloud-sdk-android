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

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 获取 COS 对象的元数据信息(meta data)的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#headObject(HeadObjectRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#headObjectAsync(HeadObjectRequest, CosXmlResultListener)
 */
final public class HeadObjectRequest extends ObjectRequest {

    public HeadObjectRequest(String bucket, String cosPath){
        super(bucket, cosPath);
    }

    /**
     * 设置版本ID
     * @param versionId 版本id
     */
    public void setVersionId(String versionId) {
        if(versionId != null){
            queryParameters.put("versionId",versionId);
        }
    }

    /**
     * <p>
     * 设置If-Modified-Since头部
     * </p>
     * <p>
     * 当 Object 在指定时间后被修改，则返回对应 Object 的 meta 信息，否则返回 304。
     * </p>
     *
     * @param ifModifiedSince If-Modified-Since头部
     */
    public void setIfModifiedSince(String ifModifiedSince){
        if(ifModifiedSince != null){
            addHeader(COSRequestHeaderKey.IF_MODIFIED_SINCE,ifModifiedSince);
        }
    }

    @Override
    public String getMethod() {
        return RequestMethod.HEAD;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

}
