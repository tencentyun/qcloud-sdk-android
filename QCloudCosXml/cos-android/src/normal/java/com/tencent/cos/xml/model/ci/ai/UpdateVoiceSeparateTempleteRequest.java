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

package com.tencent.cos.xml.model.ci.ai;

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
 * 更新模板
 * <a href="https://cloud.tencent.com/document/product/460/84757">更新模板</a>
 * @see com.tencent.cos.xml.CIService#updateVoiceSeparateTemplete(UpdateVoiceSeparateTempleteRequest)
 * @see com.tencent.cos.xml.CIService#updateVoiceSeparateTempleteAsync(UpdateVoiceSeparateTempleteRequest, CosXmlResultListener)
 */
public class UpdateVoiceSeparateTempleteRequest extends BucketRequest {
    private UpdateVoiceSeparateTemplete updateVoiceSeparateTemplete;
    protected final String templateId;


    /**
     * 更新人声分离转码模板
     *
     * @param bucket  存储桶名
     */
    public UpdateVoiceSeparateTempleteRequest(@NonNull String bucket , @NonNull String templateId) {
        super(bucket);
        this.templateId = templateId;
        addNoSignHeader("Content-Type");
        addNoSignHeader("Content-Length");
    }
    /**
     * 设置 更新模板
     * @param updateVoiceSeparateTemplete 更新模板
     */
    public void setUpdateVoiceSeparateTemplete(@NonNull UpdateVoiceSeparateTemplete updateVoiceSeparateTemplete){
        this.updateVoiceSeparateTemplete = updateVoiceSeparateTemplete;
    }

    
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/template" + "/" + templateId;
    }
    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                QCloudXmlUtils.toXml(this.updateVoiceSeparateTemplete));
    }
    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.PUT;
    }
    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, bucket, CosXmlServiceConfig.CI_HOST_FORMAT);
    }

}
