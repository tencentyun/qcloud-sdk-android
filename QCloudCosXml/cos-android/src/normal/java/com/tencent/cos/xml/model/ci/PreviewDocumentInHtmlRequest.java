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
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.cos.xml.utils.StringUtils;
import com.tencent.qcloud.core.http.HttpConstants;

/**
 * 以HTML格式预览文档的请求.
 *
 * @see com.tencent.cos.xml.CosXmlService#previewDocumentInHtml(PreviewDocumentInHtmlRequest)
 * @see com.tencent.cos.xml.CosXmlService#previewDocumentInHtmlAsync(PreviewDocumentInHtmlRequest, CosXmlResultListener)
 */
public class PreviewDocumentInHtmlRequest extends GetObjectRequest {
    /**
     * 预览请求构造器
     *
     * @param bucket   文档所在存储桶
     * @param cosPath  文档的对象键
     * @param savePath 文档本地保存路径
     */
    public PreviewDocumentInHtmlRequest(String bucket, String cosPath, String savePath) {
        this(bucket, cosPath, savePath, StringUtils.extractNameNoSuffix(cosPath));
    }

    /**
     * 预览请求构造器
     *
     * @param bucket   文档所在存储桶
     * @param cosPath  文档的对象键
     * @param savePath 文档本地保存路径
     * @param fileName 本地文件名称
     */
    public PreviewDocumentInHtmlRequest(String bucket, String cosPath, String savePath, String fileName) {
        super(bucket, cosPath, savePath, fileName);
        queryParameters.put("ci-process", "doc-preview");
        queryParameters.put("dstType", "html");
    }

    /**
     * 指定目标文件类型，支持的文件类型请见下方
     * 目前支持的输入文件类型包含如下格式：
     * 演示文件：pptx、ppt、pot、potx、pps、ppsx、dps、dpt、pptm、potm、ppsm。
     * 文字文件：doc、dot、wps、wpt、docx、dotx、docm、dotm。
     * 表格文件：xls、xlt、et、ett、xlsx、xltx、csv、xlsb、xlsm、xltm、ets。
     * 其他格式文件： pdf、 lrc、 c、 cpp、 h、 asm、 s、 java、 asp、 bat、 bas、 prg、 cmd、 rtf、 txt、 log、 xml、 htm、 html。
     * 输入文件大小限制在200MB之内。
     * 输入文件页数限制在5000页之内。
     *
     * @param srcType 指定目标文件类型
     */
    public PreviewDocumentInHtmlRequest setSrcType(String srcType) {
        queryParameters.put("srcType", srcType);
        return this;
    }

    /**
     * 是否可复制（默认为可复制）
     * 非必选字段
     *
     * @param copyable 是否可复制
     */
    public PreviewDocumentInHtmlRequest setCopyable(boolean copyable) {
        queryParameters.put("copyable", copyable ? "1" : "0");
        return this;
    }

    /**
     * 自定义配置参数，JSON 结构，需要经过 <a href="https://cloud.tencent.com/document/product/460/32832#.E4.BB.80.E4.B9.88.E6.98.AF-url-.E5.AE.89.E5.85.A8.E7.9A.84-base64-.E7.BC.96.E7.A0.81.EF.BC.9F">URL 安全</a> 的 Base64 编码，默认配置为：{ commonOptions: { isShowTopArea: true, isShowHeader: true } }
     * 支持的配置参考 <a href="https://cloud.tencent.com/document/product/436/59408#.E8.87.AA.E5.AE.9A.E4.B9.89.E9.85.8D.E7.BD.AE.E9.80.89.E9.A1.B9">自定义配置项说明</a>
     *
     * @param htmlParams 自定义配置参数
     */
    public PreviewDocumentInHtmlRequest setHtmlParams(String htmlParams) {
        queryParameters.put("htmlParams", htmlParams);
        return this;
    }

    /**
     * 水印文字，需要经过 URL 安全 的 Base64 编码，默认为空
     * 非必选字段
     *
     * @param watermark 水印文字
     */
    public PreviewDocumentInHtmlRequest setWatermark(String watermark) {
        try {
            queryParameters.put("htmlwaterword", DigestUtils.getSecurityBase64(watermark));
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 水印 RGBA（颜色和透明度），默认为：rgba(192,192,192,0.6)
     * 非必选字段
     *
     * @param htmlfillstyle 水印 RGBA（颜色和透明度）
     */
    public PreviewDocumentInHtmlRequest setWatermarkColor(String htmlfillstyle) {
        try {
            queryParameters.put("htmlfillstyle", DigestUtils.getSecurityBase64(htmlfillstyle));
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 水印文字样式，默认为：bold 20px Serif
     * 非必选字段
     *
     * @param font 水印文字样式
     */
    public PreviewDocumentInHtmlRequest setWatermarkFont(String font) {
        try {
            queryParameters.put("htmlfront", DigestUtils.getSecurityBase64(font));
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 旋转角度，0~360，默认315度
     * 非必选字段
     *
     * @param rotate 旋转角度
     */
    public PreviewDocumentInHtmlRequest setWatermarkRotate(int rotate) {
        queryParameters.put("htmlrotate", String.valueOf(rotate));
        return this;
    }

    /**
     * 水印文字水平间距，单位 px，默认为50
     * 非必选字段
     *
     * @param horizontal 水印文字水平间距
     */
    public PreviewDocumentInHtmlRequest setWatermarkHorizontal(int horizontal) {
        queryParameters.put("htmlhorizontal", String.valueOf(horizontal));
        return this;
    }

    /**
     * 水印文字垂直间距，单位 px，默认为100
     * 非必选字段
     *
     * @param vertical 水印文字垂直间距
     */
    public PreviewDocumentInHtmlRequest setWatermarkVertical(int vertical) {
        queryParameters.put("htmlvertical", String.valueOf(vertical));
        return this;
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.GET;
    }
}
