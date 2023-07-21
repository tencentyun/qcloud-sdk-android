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
import com.tencent.cos.xml.model.tag.LocationConstraint;
import com.tencent.cos.xml.transfer.XmlParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 获取存储桶（Bucket) 所在的地域信息的返回结果.
 * @see com.tencent.cos.xml.CosXml#getBucketLocation(GetBucketLocationRequest)
 * @see GetBucketLocationRequest
 */
final public class GetBucketLocationResult extends CosXmlResult {
    /**
     * Bucket 所在地域
     */
    public LocationConstraint locationConstraint = new LocationConstraint();

    @Override
    protected void xmlParser(HttpResponse response) throws XmlPullParserException, IOException{
        XmlParser.parseLocationConstraint(response.byteStream(), locationConstraint);
    }

    @Override
    public String printResult() {
        return locationConstraint != null ? locationConstraint.toString() : super.printResult();
    }
}
