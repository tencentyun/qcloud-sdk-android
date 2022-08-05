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

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.tag.pic.PicUploadResult;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 简单上传的返回结果.
 * @see SimpleCosXml#putObject(PutObjectRequest)
 * @see PutObjectRequest
 */
final public class PutObjectResult extends BasePutObjectResult {
    public PicUploadResult picUploadResult;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        try {
            picUploadResult = QCloudXml.fromXml(response.byteStream(), PicUploadResult.class);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    /**
     * 图片处理印结果<br>
     * @return 图片处理
     */
    public @Nullable PicUploadResult picUploadResult() {
        return picUploadResult;
    }
}
