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

package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.qcloud.core.http.HttpConstants;

/**
 * 预览文档的请求.
 * @see com.tencent.cos.xml.CosXmlService#previewDocument(PreviewDocumentRequest)
 * @see com.tencent.cos.xml.CosXmlService#previewDocumentAsync(PreviewDocumentRequest, CosXmlResultListener)
 */
public class PreviewDocumentRequest extends GetObjectRequest {

    private int page;

    /**
     * 预览请求构造器
     *
     * @param bucket 文档所在存储桶
     * @param cosPath 文档的对象键
     * @param savePath 文档本地保存路径
     * @param page 需转换的文档页码，从 1 开始
     */
    public PreviewDocumentRequest(String bucket, String cosPath, String savePath, int page) {
        this(bucket, cosPath, savePath, page + ".jpg", page);
    }

    /**
     * 预览请求构造器
     *
     * @param bucket 文档所在存储桶
     * @param cosPath 文档的对象键
     * @param savePath 文档本地保存路径
     * @param fileName 本地文件名称
     * @param page 需转换的文档页码，从 1 开始
     */
    public PreviewDocumentRequest(String bucket, String cosPath, String savePath, String fileName, int page) {
        super(bucket, cosPath, savePath, fileName);

        queryParameters.put("ci-process", "doc-preview");
        this.page = page;
        queryParameters.put("page", String.valueOf(page));
    }

    /**
     * 非必选字段，当前文档转换根据 COS 对象的后缀名来确定源数据类型。当 COS 对象没有后缀名时，可以设置该值。
     *
     * @param srcType 源数据的后缀类型
     */
    public PreviewDocumentRequest setSrcType(String srcType) {
        queryParameters.put("srcType", srcType);
        return this;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();

        if (page < 1) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),
                    "Please set a valid page number");
        }
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.GET;
    }

}
