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
 * 人脸搜索
 * <a href="https://cloud.tencent.com/document/product/460/106166">人脸搜索</a>
 * @see com.tencent.cos.xml.CIService#datasetFaceSearch(DatasetFaceSearchRequest)
 * @see com.tencent.cos.xml.CIService#datasetFaceSearchAsync(DatasetFaceSearchRequest, CosXmlResultListener)
 */
public class DatasetFaceSearchRequest extends AppIdRequest {
    private DatasetFaceSearch datasetFaceSearch;


    /**
     * 从数据集中搜索与指定图片最相似的前N张图片并返回人脸坐标可对数据集内文件进行一个或多个人员的人脸识别。
     *
     * @param appid  appid
     */
    public DatasetFaceSearchRequest(@NonNull String appid) {
        super(appid);
        addNoSignHeader("Content-Type");
		addHeader(HttpConstants.Header.ACCEPT, HttpConstants.ContentType.JSON);
    }
    /**
     * 设置 人脸搜索
     * @param datasetFaceSearch 人脸搜索
     */
    public void setDatasetFaceSearch(@NonNull DatasetFaceSearch datasetFaceSearch){
        this.datasetFaceSearch = datasetFaceSearch;
    }

    
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/datasetquery" + "/facesearch";
    }
    @Override
            public RequestBodySerializer getRequestBody() throws CosXmlClientException {
                return RequestBodySerializer.string(HttpConstants.ContentType.JSON,
                        QCloudJsonUtils.toJson(this.datasetFaceSearch));
            }
    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.POST;
    }
    
}
