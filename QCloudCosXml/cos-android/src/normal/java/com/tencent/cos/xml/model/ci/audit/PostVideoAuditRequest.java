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
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.audit.post.PostVideoAudit;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 提交视频审核任务的请求.
 * @see com.tencent.cos.xml.CIService#postVideoAudit(PostVideoAuditRequest)
 * @see com.tencent.cos.xml.CIService#postVideoAuditAsync(PostVideoAuditRequest, CosXmlResultListener)
 */
public class PostVideoAuditRequest extends BasePostAuditRequest {
    private final PostVideoAudit postVideoAudit;
    /**
     * 提交视频审核任务请求
     *
     * @param bucket  存储桶名
     */
    public PostVideoAuditRequest(@NonNull String bucket) {
        super(bucket);
        postVideoAudit = new PostVideoAudit();
    }

    public void setObject(@NonNull String object){
        postVideoAudit.input.object = object;
    }

    public void setUrl(@NonNull String url){
        postVideoAudit.input.url = url;
    }

    public void setDataId(@NonNull String dataId){
        postVideoAudit.input.dataId = dataId;
    }

    public void setDetectType(@NonNull String detectType){
        postVideoAudit.conf.detectType = detectType;
    }

    public void setCallback(@NonNull String callback){
        postVideoAudit.conf.callback = callback;
    }

    public void setCallbackVersion(@NonNull String callbackVersion){
        postVideoAudit.conf.callbackVersion = callbackVersion;
    }

    public void setBizType(@NonNull String bizType){
        postVideoAudit.conf.bizType = bizType;
    }

    public void setDetectContent(int detectContent){
        postVideoAudit.conf.detectContent = detectContent;
    }

    public void setMode(@NonNull String mode){
        postVideoAudit.conf.snapshot.mode = mode;
    }

    public void setCount(int count){
        postVideoAudit.conf.snapshot.count = count;
    }

    public void setTimeInterval(float timeInterval){
        postVideoAudit.conf.snapshot.timeInterval = timeInterval;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/video/auditing";
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    QCloudXml.toXml(postVideoAudit));
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(TextUtils.isEmpty(postVideoAudit.input.object) &&
                TextUtils.isEmpty(postVideoAudit.input.url)){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "object or url must be non-empty");
        }
        if(postVideoAudit.conf.snapshot.count == 0){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "count cannot be 0");
        }

    }
}