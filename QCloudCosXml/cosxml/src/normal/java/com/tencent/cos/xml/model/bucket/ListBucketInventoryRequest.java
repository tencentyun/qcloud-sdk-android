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

package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * 查询存储桶中所有清单任务的请求.
 * @see com.tencent.cos.xml.CosXml#listBucketInventory(ListBucketInventoryRequest)
 * @see com.tencent.cos.xml.CosXml#listBucketInventoryAsync(ListBucketInventoryRequest, CosXmlResultListener)
 */
public class ListBucketInventoryRequest extends BucketRequest {
    private String continuationToken;

    public ListBucketInventoryRequest(String bucket) {
        super(bucket);
    }

    /**
     * 当 COS 响应体中 IsTruncated 为 true，
     * 且 NextContinuationToken 节点中存在参数值时，
     * 您可以将这个参数作为 continuation-token 参数值，以获取下一页的清单任务信息。
     * 缺省值：None
     */
    public void setContinuationToken(String continuationToken){
        this.continuationToken = continuationToken;
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("inventory", null);
        if(continuationToken != null){
            queryParameters.put("continuation-token", continuationToken);
        }
        return super.getQueryString();
    }
}
