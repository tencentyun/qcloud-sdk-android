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

package com.tencent.cos.xml.model.ci.metainsight;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.appid.AppIdRequest;
import com.tencent.cos.xml.utils.QCloudJsonUtils;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 解绑存储桶与数据集
 * <a href="https://cloud.tencent.com/document/product/460/106160">解绑存储桶与数据集</a>
 * @see com.tencent.cos.xml.CIService#deleteDatasetBinding(DeleteDatasetBindingRequest)
 * @see com.tencent.cos.xml.CIService#deleteDatasetBindingAsync(DeleteDatasetBindingRequest, CosXmlResultListener)
 */
public class DeleteDatasetBindingRequest extends AppIdRequest {
    private DeleteDatasetBinding deleteDatasetBinding;


    /**
     * 解绑数据集和对象存储（COS）Bucket ，解绑会导致 COS Bucket新增的变更不会同步到数据集，请谨慎操作。
     *
     * @param appid  appid
     */
    public DeleteDatasetBindingRequest(@NonNull String appid) {
        super(appid);
        addNoSignHeader("Content-Type");
		addHeader(HttpConstants.Header.ACCEPT, HttpConstants.ContentType.JSON);
    }
    /**
     * 设置 解绑存储桶与数据集
     * @param deleteDatasetBinding 解绑存储桶与数据集
     */
    public void setDeleteDatasetBinding(@NonNull DeleteDatasetBinding deleteDatasetBinding){
        this.deleteDatasetBinding = deleteDatasetBinding;
    }

    
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/datasetbinding";
    }
    @Override
            public RequestBodySerializer getRequestBody() throws CosXmlClientException {
                return RequestBodySerializer.string(HttpConstants.ContentType.JSON,
                        QCloudJsonUtils.toJson(this.deleteDatasetBinding));
            }
    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.DELETE;
    }
    
}
