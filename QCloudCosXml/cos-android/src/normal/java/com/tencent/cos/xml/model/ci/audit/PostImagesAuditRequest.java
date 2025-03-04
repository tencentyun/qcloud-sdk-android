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
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.audit.bean.AuditConf;
import com.tencent.cos.xml.model.tag.audit.post.PostImagesAudit;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 提交图片审核任务的请求.
 * @see com.tencent.cos.xml.CIService#postImagesAudit(PostImagesAuditRequest)
 * @see com.tencent.cos.xml.CIService#postImagesAuditAsync(PostImagesAuditRequest, CosXmlResultListener)
 */
public class PostImagesAuditRequest extends BasePostAuditRequest {
    private final PostImagesAudit postImagesAudit;
    /**
     * 提交图片审核任务请求
     *
     * @param bucket  存储桶名
     */
    public PostImagesAuditRequest(@NonNull String bucket) {
        super(bucket);
        postImagesAudit = new PostImagesAudit();
    }

    /**
     * 添加需要审核的内容
     * @param image 需要审核的内容
     */
    public void addImage(@NonNull PostImagesAudit.ImagesAuditInput image){
        postImagesAudit.input.add(image);
    }

    /**
     * 设置审核规则配置
     * @param conf 审核规则配置
     */
    public void setConfig(@NonNull AuditConf conf){
        postImagesAudit.conf = conf;
    }

    @Deprecated
    public void setDetectType(@NonNull String detectType){
        postImagesAudit.conf.detectType = detectType;
    }

    @Deprecated
    public void setBizType(@NonNull String bizType){
        postImagesAudit.conf.bizType = bizType;
    }

    @Deprecated
    public void setAsync(int async){
        postImagesAudit.conf.async = async;
    }

    @Deprecated
    public void setCallback(@NonNull String callback){
        postImagesAudit.conf.callback = callback;
    }

    @Deprecated
    public void setFreeze(@NonNull AuditConf.Freeze freeze){
        postImagesAudit.conf.freeze = freeze;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/image/auditing";
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                QCloudXmlUtils.toXml(postImagesAudit));
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(postImagesAudit.input != null && postImagesAudit.input.size() <= 0){
        }
    }
}
