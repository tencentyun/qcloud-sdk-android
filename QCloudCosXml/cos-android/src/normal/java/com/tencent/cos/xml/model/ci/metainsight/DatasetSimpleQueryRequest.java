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

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 简单查询
 * <a href="https://cloud.tencent.com/document/product/460/106375">简单查询</a>
 * @see com.tencent.cos.xml.CIService#datasetSimpleQuery(DatasetSimpleQueryRequest)
 * @see com.tencent.cos.xml.CIService#datasetSimpleQueryAsync(DatasetSimpleQueryRequest, CosXmlResultListener)
 */
public class DatasetSimpleQueryRequest extends AppIdRequest {
    private DatasetSimpleQuery datasetSimpleQuery;


    /**
     * 可以根据已提取的文件元数据（包含文件名、标签、路径、自定义标签、文本等字段）查询和统计数据集内文件，支持逻辑关系表达方式。
     *
     * @param appid  appid
     */
    public DatasetSimpleQueryRequest(@NonNull String appid) {
        super(appid);
		addHeader(HttpConstants.Header.ACCEPT, HttpConstants.ContentType.JSON);
    }
    /**
     * 设置 简单查询
     * @param datasetSimpleQuery 简单查询
     */
    public void setDatasetSimpleQuery(@NonNull DatasetSimpleQuery datasetSimpleQuery){
        this.datasetSimpleQuery = datasetSimpleQuery;
    }

    
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/datasetquery" + "/simple";
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return RequestBodySerializer.bytes(HttpConstants.ContentType.JSON, QCloudJsonUtils.toJson(this.datasetSimpleQuery).getBytes("utf-8"));
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.POST;
    }
    
}
