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
 * 更新元数据索引
 * <a href="https://cloud.tencent.com/document/product/460/106162">更新元数据索引</a>
 * @see com.tencent.cos.xml.CIService#updateFileMetaIndex(UpdateFileMetaIndexRequest)
 * @see com.tencent.cos.xml.CIService#updateFileMetaIndexAsync(UpdateFileMetaIndexRequest, CosXmlResultListener)
 */
public class UpdateFileMetaIndexRequest extends AppIdRequest {
    private UpdateFileMetaIndex updateFileMetaIndex;


    /**
     * 更新数据集内已索引的一个文件的部分元数据。

并非所有的元数据都允许您自定义更新，在您发起更新请求时需要填写数据集，默认会根据该数据集的算子进行元数据重新提取并更新已存在的索引，此外您也可以更新部分自定义的元数据索引，如CustomTags、CustomId等字段，具体请参考请求参数一节。
     *
     * @param appid  appid
     */
    public UpdateFileMetaIndexRequest(@NonNull String appid) {
        super(appid);
		addHeader(HttpConstants.Header.ACCEPT, HttpConstants.ContentType.JSON);
    }
    /**
     * 设置 更新元数据索引
     * @param updateFileMetaIndex 更新元数据索引
     */
    public void setUpdateFileMetaIndex(@NonNull UpdateFileMetaIndex updateFileMetaIndex){
        this.updateFileMetaIndex = updateFileMetaIndex;
    }

    
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/filemeta";
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return RequestBodySerializer.bytes(HttpConstants.ContentType.JSON, QCloudJsonUtils.toJson(this.updateFileMetaIndex).getBytes("utf-8"));
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.PUT;
    }
    
}
