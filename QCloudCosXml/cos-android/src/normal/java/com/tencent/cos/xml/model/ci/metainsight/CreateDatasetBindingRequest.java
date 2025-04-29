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
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.appid.AppIdRequest;
import com.tencent.cos.xml.utils.QCloudJsonUtils;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 绑定存储桶与数据集
 * <a href="https://cloud.tencent.com/document/product/460/106159">绑定存储桶与数据集</a>
 *
 * @see com.tencent.cos.xml.CIService#createDatasetBinding(CreateDatasetBindingRequest)
 * @see com.tencent.cos.xml.CIService#createDatasetBindingAsync(CreateDatasetBindingRequest, CosXmlResultListener)
 */
public class CreateDatasetBindingRequest extends AppIdRequest {
    private CreateDatasetBinding createDatasetBinding;


    /**
     * 本文档介绍创建数据集（Dataset）和对象存储（COS）Bucket 的绑定关系，绑定后将使用创建数据集时所指定算子对文件进行处理。
     * 绑定关系创建后，将对 COS 中新增的文件进行准实时的增量追踪扫描，使用创建数据集时所指定算子对文件进行处理，抽取文件元数据信息进行索引。通过此方式为文件建立索引后，您可以使用元数据查询API对元数据进行查询、管理和统计。
     *
     * @param appid appid
     */
    public CreateDatasetBindingRequest(@NonNull String appid) {
        super(appid);
        addHeader(HttpConstants.Header.ACCEPT, HttpConstants.ContentType.JSON);
    }

    /**
     * 设置 绑定存储桶与数据集
     *
     * @param createDatasetBinding 绑定存储桶与数据集
     */
    public void setCreateDatasetBinding(@NonNull CreateDatasetBinding createDatasetBinding) {
        this.createDatasetBinding = createDatasetBinding;
    }


    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/datasetbinding";
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return RequestBodySerializer.bytes(HttpConstants.ContentType.JSON, QCloudJsonUtils.toJson(this.createDatasetBinding).getBytes("utf-8"));
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.POST;
    }

}
