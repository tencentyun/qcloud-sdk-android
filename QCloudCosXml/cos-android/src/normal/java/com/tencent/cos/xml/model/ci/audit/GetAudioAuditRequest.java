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

package com.tencent.cos.xml.model.ci.audit;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.listener.CosXmlResultListener;

/**
 * 查询音频审核任务结果的请求.
 * @see com.tencent.cos.xml.CIService#getAudioAudit(GetAudioAuditRequest)
 * @see com.tencent.cos.xml.CIService#getAudioAuditAsync(GetAudioAuditRequest, CosXmlResultListener)
 */
public class GetAudioAuditRequest extends GetAuditRequest {
    /**
     * 查询音频审核任务结果请求
     *
     * @param bucket  存储桶名
     * @param jobId  审核任务的 ID
     */
    public GetAudioAuditRequest(String bucket, @NonNull String jobId) {
        super(bucket, jobId);
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/audio/auditing/" + jobId;
    }
}
