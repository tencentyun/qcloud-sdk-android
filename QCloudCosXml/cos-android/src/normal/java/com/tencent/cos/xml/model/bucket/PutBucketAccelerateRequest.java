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


import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * 启用或者暂停存储桶的全球加速功能的请求.
 * @see com.tencent.cos.xml.CosXml#putBucketAccelerate(PutBucketAccelerateRequest)
 * @see com.tencent.cos.xml.CosXml#putBucketAccelerateAsync(PutBucketAccelerateRequest, CosXmlResultListener)
 */
final public class PutBucketAccelerateRequest extends BucketRequest {

    private boolean enable;

    /**
     * @param bucket 存储桶名称
     * @param enable 是否开启全球加速
     */
    public PutBucketAccelerateRequest(String bucket, boolean enable) {
        super(bucket);
        this.enable = enable;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("accelerate", null);
        return super.getQueryString();
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT ;
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                XmlBuilder.buildPutBucketAccelerateXML(enable));
    }
}
