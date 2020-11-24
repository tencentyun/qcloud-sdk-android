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

package com.tencent.cos.xml.model.object;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.pic.PicUploadResult;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

/**
 * 简单上传的返回结果.
 * @see com.tencent.cos.xml.SimpleCosXml#putObject(PutObjectRequest)
 * @see PutObjectRequest
 */
final public class PutObjectResult extends CosXmlResult {
    /**
     * 返回文件的 MD5 算法校验值.eTag 的值可以用于检查 Object 在上传过程中是否有损坏
     */
    public String eTag;

    public PicUploadResult picUploadResult;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        eTag = response.header("ETag");
        try {
            picUploadResult = QCloudXml.fromXml(response.byteStream(), PicUploadResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取盲水印结果<br>
     * 详情请参考：<a herf="https://cloud.tencent.com/document/product/1246/45384">盲水印功能</a>
     * @return 盲水印结果
     */
    public @Nullable PicUploadResult picUploadResult() {
        return picUploadResult;
    }
}
