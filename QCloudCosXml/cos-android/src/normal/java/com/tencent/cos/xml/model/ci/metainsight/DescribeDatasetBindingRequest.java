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
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * 查询数据集与存储桶的绑定关系
 * <a href="https://cloud.tencent.com/document/product/460/106485">查询数据集与存储桶的绑定关系</a>
 * @see com.tencent.cos.xml.CIService#describeDatasetBinding(DescribeDatasetBindingRequest)
 * @see com.tencent.cos.xml.CIService#describeDatasetBindingAsync(DescribeDatasetBindingRequest, CosXmlResultListener)
 */
public class DescribeDatasetBindingRequest extends AppIdRequest {
    

    /**
     * 数据集名称，同一个账户下唯一。;是否必传：是
     */
    public String datasetname;
    /**
     * 资源标识字段，表示需要与数据集绑定的资源，当前仅支持COS存储桶，字段规则：cos://，其中BucketName表示COS存储桶名称，例如（需要进行urlencode）：cos%3A%2F%2Fexample-125000;是否必传：是
     */
    public String uri;

    /**
     * 查询数据集和对象存储（COS）Bucket 绑定关系列表。
     *
     * @param appid  appid
     */
    public DescribeDatasetBindingRequest(@NonNull String appid) {
        super(appid);
        addNoSignHeader("Content-Type");
		addHeader(HttpConstants.Header.ACCEPT, HttpConstants.ContentType.JSON);
    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("datasetname", datasetname);
        queryParameters.put("uri", uri);
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/datasetbinding";
    }
    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }
    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.GET;
    }
    
}
