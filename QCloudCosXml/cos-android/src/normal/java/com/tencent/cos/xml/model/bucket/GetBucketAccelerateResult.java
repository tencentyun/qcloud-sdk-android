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

import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.AccelerateConfiguration;
import com.tencent.cos.xml.transfer.XmlParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 获取查询存储桶的全球加速功能配置的返回结果.
 * <p>
 * 如果您从未在存储桶上启用过全球加速功能，则 GET Bucket Accelerate 请求不返回全球加速功能配置状态。
 * 全球加速功能状态值合法返回值为 Enabled 或者 Suspended，表示开启全球加速功能和暂停全球加速功能。
 *
 * @see com.tencent.cos.xml.CosXml#getBucketTagging(GetBucketTaggingRequest)
 * @see GetBucketTaggingRequest
 */
final public class GetBucketAccelerateResult extends CosXmlResult {
    public AccelerateConfiguration accelerateConfiguration = new AccelerateConfiguration();

    @Override
    protected void xmlParser(HttpResponse response) throws XmlPullParserException, IOException{
        XmlParser.parseAccelerateConfiguration(response.byteStream(), accelerateConfiguration);
    }
}
