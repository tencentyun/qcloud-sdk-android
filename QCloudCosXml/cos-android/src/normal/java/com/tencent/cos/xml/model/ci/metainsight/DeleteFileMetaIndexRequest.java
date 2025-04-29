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
 * 删除元数据索引
 * <a href="https://cloud.tencent.com/document/product/460/106163">删除元数据索引</a>
 * @see com.tencent.cos.xml.CIService#deleteFileMetaIndex(DeleteFileMetaIndexRequest)
 * @see com.tencent.cos.xml.CIService#deleteFileMetaIndexAsync(DeleteFileMetaIndexRequest, CosXmlResultListener)
 */
public class DeleteFileMetaIndexRequest extends AppIdRequest {
    private DeleteFileMetaIndex deleteFileMetaIndex;


    /**
     * 从数据集内删除一个文件的元信息。无论该文件的元信息是否在数据集内存在，均会返回删除成功。


     *
     * @param appid  appid
     */
    public DeleteFileMetaIndexRequest(@NonNull String appid) {
        super(appid);
		addHeader(HttpConstants.Header.ACCEPT, HttpConstants.ContentType.JSON);
    }
    /**
     * 设置 删除元数据索引
     * @param deleteFileMetaIndex 删除元数据索引
     */
    public void setDeleteFileMetaIndex(@NonNull DeleteFileMetaIndex deleteFileMetaIndex){
        this.deleteFileMetaIndex = deleteFileMetaIndex;
    }

    
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/filemeta";
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return RequestBodySerializer.bytes(HttpConstants.ContentType.JSON, QCloudJsonUtils.toJson(this.deleteFileMetaIndex).getBytes("utf-8"));
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.DELETE;
    }
    
}
