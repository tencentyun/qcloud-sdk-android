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

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * 预览文档的返回结果.
 * @see com.tencent.cos.xml.CosXmlService#previewDocument(PreviewDocumentRequest)
 * @see PreviewDocumentRequest
 */
public class PreviewDocumentResult extends GetObjectResult {

    private int totalPage;
    private String contentType;
    private String errNo;
    private int totalSheet;
    private String sheetName;
    private String previewFilePath;

    public PreviewDocumentResult(String previewFilePath) {
        this.previewFilePath = previewFilePath;
    }

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);

        String sTotalPage = response.header("X-Total-Page");
        if (sTotalPage != null) {
            try {
                totalPage = Integer.parseInt(sTotalPage);
            } catch (Error e) {}
        }
        contentType = response.header("Content-Type");
        errNo = response.header("X-ErrNo\t");
        String sTotalSheet = response.header("X-Total-Sheet");
        if (sTotalSheet != null) {
            try {
                totalSheet = Integer.parseInt(sTotalSheet);
            } catch (Error e) {}
        }
        sheetName = response.header("X-Sheet-Name");
    }

    /**
     * 返回文档总页数，异常时为空
     * @return 文档总页数
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 根据图片实际的格式，返回不同的值，例如 image/jpeg、image/webp，异常时保持现在值即可
     * @return 图片实际的格式，例如 image/jpeg、image/webp
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 当异常时返回错误码
     * @return 错误码
     */
    public String getErrNo() {
        return errNo;
    }

    /**
     * 表格文件返回参数，返回文档中总表数
     * @return 返回文档中总表数
     */
    public int getTotalSheet() {
        return totalSheet;
    }

    /**
     * 表格文件返回参数，返回当前 sheet 名
     * @return 返回当前 sheet 名
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * 获取预览文档地址
     * @return 预览文档地址
     */
    public String getPreviewFilePath() {
        return previewFilePath;
    }
}
