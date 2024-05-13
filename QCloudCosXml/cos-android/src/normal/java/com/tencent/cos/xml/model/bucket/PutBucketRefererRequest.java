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

import androidx.annotation.NonNull;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.RefererConfiguration;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设置对象防盗链的请求.
 * @see com.tencent.cos.xml.CosXml#putBucketReferer(PutBucketRefererRequest)
 * @see com.tencent.cos.xml.CosXml#putBucketRefererAsync(PutBucketRefererRequest, CosXmlResultListener)
 */
public class PutBucketRefererRequest extends BucketRequest {
    private final RefererConfiguration refererConfiguration;

    public PutBucketRefererRequest(String bucket, boolean enabled,
                                   RefererConfiguration.RefererType refererType) {
        super(bucket);
        refererConfiguration = new RefererConfiguration();
        refererConfiguration.setEnabled(enabled);
        refererConfiguration.setRefererType(refererType);
        refererConfiguration.setAllowEmptyRefer(false);
        refererConfiguration.domainList = new ArrayList<>();
    }

    /**
     * 设置是否开启防盗链
     * @param enabled 是否开启
     */
    public void setEnabled(boolean enabled){
        refererConfiguration.setEnabled(enabled);
    }

    public boolean getEnabled(){
        return refererConfiguration.getEnabled();
    }

    /**
     * 设置防盗链类型(黑名单、白名单)
     * @param refererType 防盗链类型
     */
    public void setRefererType(@NonNull RefererConfiguration.RefererType refererType){
        refererConfiguration.setRefererType(refererType);
    }

    public RefererConfiguration.RefererType getRefererType(){
        return refererConfiguration.getRefererType();
    }

    /**
     * 设置是否允许空 Referer 访问
     * @param allowEmptyRefer 是否允许
     */
    public void setAllowEmptyRefer(boolean allowEmptyRefer){
        refererConfiguration.setAllowEmptyRefer(allowEmptyRefer);
    }

    public boolean getAllowEmptyRefer(){
        return refererConfiguration.getAllowEmptyRefer();
    }

    /**
     * 设置域名列表
     * @param domains 域名列表
     */
    public void setDomainList(@NonNull List<RefererConfiguration.Domain> domains){
        refererConfiguration.domainList = domains;
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("referer", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                QCloudXmlUtils.toXml(refererConfiguration));
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(refererConfiguration.domainList == null || refererConfiguration.domainList.size() <= 0){
        }
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }
}
