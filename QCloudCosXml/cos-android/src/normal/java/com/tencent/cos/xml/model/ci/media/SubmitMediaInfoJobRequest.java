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
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 获取媒体信息
 * <a href="https://cloud.tencent.com/document/product/460/84776">获取媒体信息</a>
 * @see com.tencent.cos.xml.CIService#submitMediaInfoJob(SubmitMediaInfoJobRequest)
 * @see com.tencent.cos.xml.CIService#submitMediaInfoJobAsync(SubmitMediaInfoJobRequest, CosXmlResultListener)
 */
public class SubmitMediaInfoJobRequest extends BucketRequest {
    private SubmitMediaInfoJob submitMediaInfoJob;



    /**
     * 提交一个获取媒体信息任务
     *
     * @param bucket  存储桶名
     */
    public SubmitMediaInfoJobRequest(@NonNull String bucket ) {
        super(bucket);
        addNoSignHeader("Content-Type");
    }

    /**
     * 设置 获取媒体信息
     * @param submitMediaInfoJob 获取媒体信息
     */
    public void setSubmitMediaInfoJob(@NonNull SubmitMediaInfoJob submitMediaInfoJob){
        this.submitMediaInfoJob = submitMediaInfoJob;
    }


    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/jobs";
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                QCloudXmlUtils.toXml(this.submitMediaInfoJob));
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.POST;
    }

    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, bucket, CosXmlServiceConfig.CI_HOST_FORMAT);
    }

}
