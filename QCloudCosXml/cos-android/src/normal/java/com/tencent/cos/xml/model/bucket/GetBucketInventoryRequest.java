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

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * 获取存储桶中指定清单任务的请求.
 * @see com.tencent.cos.xml.CosXml#getBucketInventory(GetBucketInventoryRequest)
 * @see com.tencent.cos.xml.CosXml#getBucketInventoryAsync(GetBucketInventoryRequest, CosXmlResultListener)
 */
public class GetBucketInventoryRequest extends BucketRequest {

    private String inventoryId;

    public GetBucketInventoryRequest(String bucket) {
        super(bucket);
    }

    /**
     * 设置清单任务的名称。缺省值：None
     * 合法字符：a-z，A-Z，0-9，-，_，.
     * @param inventoryId 清单任务的名称
     */
    public void setInventoryId(String inventoryId){
        this.inventoryId = inventoryId;
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("inventory", null);
        queryParameters.put("id", inventoryId);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
    }
}
