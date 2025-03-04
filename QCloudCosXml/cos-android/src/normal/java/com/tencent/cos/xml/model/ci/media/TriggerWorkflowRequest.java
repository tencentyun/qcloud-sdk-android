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

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * 测试工作流
 * <a href="https://cloud.tencent.com/document/product/460/76864">测试工作流</a>
 * @see com.tencent.cos.xml.CIService#triggerWorkflow(TriggerWorkflowRequest)
 * @see com.tencent.cos.xml.CIService#triggerWorkflowAsync(TriggerWorkflowRequest, CosXmlResultListener)
 */
public class TriggerWorkflowRequest extends BucketRequest {
    /**
     * 需要触发的工作流 ID
     */
    private final String workflowId;
    /**
     * 需要进行工作流处理的对象名称
     */
    private final String object;
    /**
     * 存量触发任务名称，支持中文、英文、数字、—和_，长度限制128字符，默认为空
     */
    private String name;

    /**
     * 测试工作流
     *
     * @param bucket  存储桶名
     */
    public TriggerWorkflowRequest(@NonNull String bucket, String workflowId, String object) {
        super(bucket);
        addNoSignHeader("Content-Type");
        this.workflowId = workflowId;
        this.object = object;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Map<String, String> getQueryString() {
        addQuery("workflowId", workflowId);
        addQuery("object", object);
        if(!TextUtils.isEmpty(name)) {
            addQuery("name", name);
        }
        return super.getQueryString();
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/triggerworkflow";
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.bytes(null, new byte[0]);
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
