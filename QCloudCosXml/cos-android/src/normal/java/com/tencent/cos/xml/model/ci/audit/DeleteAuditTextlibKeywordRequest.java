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
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.ArrayList;

/**
 * 删除文本库关键词
 * @see com.tencent.cos.xml.CIService#deleteAuditTextlibKeyword(DeleteAuditTextlibKeywordRequest)
 * @see com.tencent.cos.xml.CIService#deleteAuditTextlibKeywordAsync(DeleteAuditTextlibKeywordRequest, CosXmlResultListener)
 */
public class DeleteAuditTextlibKeywordRequest extends BucketRequest {
    /**
     *要删除的文本库的ID。
     */
    private final String libID;

    private DeleteAuditTextlibKeyword deleteAuditTextlibKeyword = new DeleteAuditTextlibKeyword();

    /**
     * 删除文本库关键词
     *
     * @param bucket  存储桶名
     */
    public DeleteAuditTextlibKeywordRequest(@NonNull String bucket, @NonNull String libID) {
        super(bucket);
        addNoSignHeader("Content-Type");
        this.libID = libID;
        deleteAuditTextlibKeyword.keywordIDs = new ArrayList<>();
    }

    /**
     * 添加要删除的关键词id
     * @param keywordID 关键词id
     */
    public void addKeywordID(@NonNull String keywordID){
        deleteAuditTextlibKeyword.keywordIDs.add(keywordID);
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return String.format("/audit/textlib/%s/keyword", this.libID);
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                QCloudXmlUtils.toXml(this.deleteAuditTextlibKeyword));
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.DELETE;
    }

    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, bucket, CosXmlServiceConfig.CI_HOST_FORMAT);
    }

}
