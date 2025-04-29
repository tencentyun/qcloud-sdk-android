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

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.audit.post.PostTextAudit;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 提交文本审核任务的请求.
 */
public class PostTextAuditRequest extends BasePostAuditRequest {
    protected final PostTextAudit postTextAudit;
    /**
     * 提交文本审核任务请求
     *
     * @param bucket  存储桶名
     */
    public PostTextAuditRequest(@NonNull String bucket) {
        super(bucket);
        postTextAudit = new PostTextAudit();
    }

    /**
     * 设置需要审核的内容
     * @param input 需要审核的内容
     */
    public void setInput(@NonNull PostTextAudit.TextAuditInput input){
        postTextAudit.input = input;
    }

    /**
     * 设置审核规则配置
     * @param conf 审核规则配置
     */
    public void setConfig(@NonNull PostTextAudit.TextAuditConf conf){
        postTextAudit.conf = conf;
    }

    @Deprecated
    public void setContent(@NonNull String content){
        postTextAudit.input.content = content;
    }

    @Deprecated
    public void setObject(@NonNull String object){
        postTextAudit.input.object = object;
    }

    @Deprecated
    public void setUrl(@NonNull String url){
        postTextAudit.input.url = url;
    }

    @Deprecated
    public void setDataId(@NonNull String dataId){
        postTextAudit.input.dataId = dataId;
    }

    @Deprecated
    public void setDetectType(@NonNull String detectType){
        postTextAudit.conf.detectType = detectType;
    }

    @Deprecated
    public void setCallback(@NonNull String callback){
        postTextAudit.conf.callback = callback;
    }

    @Deprecated
    public void setBizType(@NonNull String bizType){
        postTextAudit.conf.bizType = bizType;
    }

    @Deprecated
    public void setCallbackVersion(@NonNull String callbackVersion){
        postTextAudit.conf.callbackVersion = callbackVersion;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/text/auditing";
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException, CosXmlClientException {
        return RequestBodySerializer.bytes(COSRequestHeaderKey.APPLICATION_XML, QCloudXmlUtils.toXml(this.postTextAudit).getBytes("utf-8"));
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(TextUtils.isEmpty(postTextAudit.input.object) &&
                TextUtils.isEmpty(postTextAudit.input.url) &&
                TextUtils.isEmpty(postTextAudit.input.content)){
        }
    }
}
