/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated WebPageation files (the "Software"), to deal
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
import com.tencent.cos.xml.model.tag.audit.post.PostWebPageAudit;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 提交网页审核任务的请求.
 * @see com.tencent.cos.xml.CIService#postWebPageAudit(PostWebPageAuditRequest)
 * @see com.tencent.cos.xml.CIService#postWebPageAuditAsync(PostWebPageAuditRequest, CosXmlResultListener)
 */
public class PostWebPageAuditRequest extends BasePostAuditRequest {
    private final PostWebPageAudit postWebPageAudit;
    /**
     * 提交网页审核任务请求
     *
     * @param bucket  存储桶名
     */
    public PostWebPageAuditRequest(@NonNull String bucket) {
        super(bucket);
        postWebPageAudit = new PostWebPageAudit();
    }

    public void setUrl(@NonNull String url){
        postWebPageAudit.input.url = url;
    }

    public void setDetectType(@NonNull String detectType){
        postWebPageAudit.conf.detectType = detectType;
    }

    public void setCallback(@NonNull String callback){
        postWebPageAudit.conf.callback = callback;
    }

    public void setReturnHighlightHtml(boolean returnHighlightHtml){
        postWebPageAudit.conf.returnHighlightHtml = returnHighlightHtml;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/webpage/auditing";
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    QCloudXml.toXml(postWebPageAudit));
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(TextUtils.isEmpty(postWebPageAudit.input.url)){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "url must be non-empty");
        }

        if(TextUtils.isEmpty(postWebPageAudit.conf.detectType)){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "detectType must be non-empty");
        }
    }
}
