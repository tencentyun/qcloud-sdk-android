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
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.BucketLoggingStatus;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * 为源存储桶开启日志记录的请求.
 * @see com.tencent.cos.xml.CosXml#putBucketLogging(PutBucketLoggingRequest)
 * @see com.tencent.cos.xml.CosXml#putBucketLoggingAsync(PutBucketLoggingRequest, CosXmlResultListener)
 */
public class PutBucketLoggingRequest extends BucketRequest {

    private BucketLoggingStatus bucketLoggingStatus;

    public PutBucketLoggingRequest(String bucket) {
        super(bucket);
        bucketLoggingStatus = new BucketLoggingStatus();
    }

    /**
     * 设置存放日志的目标存储桶，可以是同一个存储桶（但不推荐），或同一账户下、同一地域的存储桶
     * @param targetBucket 目标存储桶
     */
    public void setTargetBucket(String targetBucket){
        if(targetBucket == null) return;
        if(bucketLoggingStatus.loggingEnabled == null){
            bucketLoggingStatus.loggingEnabled = new BucketLoggingStatus.LoggingEnabled();
        }
        bucketLoggingStatus.loggingEnabled.targetBucket = targetBucket;
    }

    /**
     * 设置日志存放在目标存储桶的指定路径
     * @param targetPrefix 目标路径
     */
    public void setTargetPrefix(String targetPrefix){
        if(targetPrefix == null) return;
        if(bucketLoggingStatus.loggingEnabled == null){
            bucketLoggingStatus.loggingEnabled = new BucketLoggingStatus.LoggingEnabled();
        }
        bucketLoggingStatus.loggingEnabled.targetPrefix = targetPrefix;
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("logging", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildBucketLogging(bucketLoggingStatus));
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }
}
