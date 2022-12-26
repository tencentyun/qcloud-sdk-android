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

    /**
     * 转换输出目标文件类型：
     * png，转成 png 格式的图片文件
     * jpg，转成 jpg 格式的图片文件
     * pdf，转成 pdf 格式的图片文件。无法选择页码，page 参数不生效
     * txt，转成 txt 格式文件
     * 如果传入的格式未能识别，默认使用 jpg 格式
     *
     * @param dstType 输出目标文件类型
     */
    public PreviewDocumentRequest setDstType(String dstType) {
        queryParameters.put("dstType", dstType);
        return this;
    }

    /**
     * Office 文档的打开密码，如果需要转换有密码的文档，请设置该字段
     *
     * @param password Office 文档的打开密码
     */
    public PreviewDocumentRequest setPassword(String password) {
        queryParameters.put("password", password);
        return this;
    }

    /**
     * 是否隐藏批注和应用修订，默认为 0
     * 0：隐藏批注，应用修订
     * 1：显示批注和修订
     *
     * @param comment 是否隐藏批注和应用修订
     */
    public PreviewDocumentRequest setComment(int comment) {
        queryParameters.put("comment", String.valueOf(comment));
        return this;
    }

    /**
     * 表格文件参数，转换第 X 个表，默认为1
     *
     * @param sheet 表格文件参数，转换第 X 个表
     */
    public PreviewDocumentRequest setExcelSheet(int sheet) {
        queryParameters.put("sheet", String.valueOf(sheet));
        return this;
    }

    /**
     * 表格文件转换纸张方向，0代表垂直方向，非0代表水平方向，默认为0
     *
     * @param excelPaperDirection 表格文件转换纸张方向
     */
    public PreviewDocumentRequest setExcelPaperDirection(int excelPaperDirection) {
        queryParameters.put("excelPaperDirection", String.valueOf(excelPaperDirection));
        return this;
    }

    /**
     * 值为1表示将所有列放到 1 页进行排版，默认值为 0
     *
     * @param excelRow 值为1表示将所有列放到 1 页进行排版，默认值为 0
     */
    public PreviewDocumentRequest setExcelRow(int excelRow) {
        queryParameters.put("excelRow", String.valueOf(excelRow));
        return this;
    }

    /**
     * 值为1表示将所有行放到 1 页进行排版，默认值为 0
     *
     * @param excelCol 值为1表示将所有行放到 1 页进行排版，默认值为 0
     */
    public PreviewDocumentRequest setExcelCol(int excelCol) {
        queryParameters.put("excelCol", String.valueOf(excelCol));
        return this;
    }

    /**
     * 设置纸张（画布）大小，对应信息为： 0 → A4 、 1 → A2 、 2 → A0 ，默认 A4 纸张 （需配合 excelRow 或 excelCol 一起使用）
     *
     * @param excelPaperSize 纸张（画布）大小
     */
    public PreviewDocumentRequest setExcelPaperSize(int excelPaperSize) {
        queryParameters.put("excelPaperSize", String.valueOf(excelPaperSize));
        return this;
    }

    /**
     * 是否转换成长文本，设置为 true 时，可以将需要导出的页中的文字合并导出，分页范围可以通过 Ranges 控制。默认值为 false ，按页导出 txt。（ dstType="txt" 时生效)
     *
     * @param txtPagination 是否转换成长文本
     */
    public PreviewDocumentRequest setTxtPagination(boolean txtPagination) {
        queryParameters.put("txtPagination", String.valueOf(txtPagination));
        return this;
    }

    /**
     * 转换后的图片处理参数，支持 基础图片处理 所有处理参数，多个处理参数可通过 管道操作符 分隔，从而实现在一次访问中按顺序对图片进行不同处理
     *
     * @param imageParams 图片处理参数
     */
    public PreviewDocumentRequest setImageParams(String imageParams) {
        queryParameters.put("ImageParams", String.valueOf(imageParams));
        return this;
    }

    /**
     * 生成预览图的图片质量，取值范围为 [1, 100]，默认值100。 例如取值为100，代表生成图片质量为100%
     *
     * @param quality 生成预览图的图片质量
     */
    public PreviewDocumentRequest setQuality(int quality) {
        queryParameters.put("quality", String.valueOf(quality));
        return this;
    }

    /**
     * 预览图片的缩放参数，取值范围为 [10, 200]， 默认值100。 例如取值为200，代表图片缩放比例为200% 即放大两倍
     *
     * @param scale 预览图片的缩放参数
     */
    public PreviewDocumentRequest setScale(int scale) {
        queryParameters.put("scale", String.valueOf(scale));
        return this;
    }

    /**
     * 按指定 dpi 渲染图片，该参数与 scale 共同作用，取值范围 96-600 ，默认值为 96 。转码后的图片单边宽度需小于65500像素
     *
     * @param imageDpi 按指定 dpi 渲染图片
     */
    public PreviewDocumentRequest setImageDpi(int imageDpi) {
        queryParameters.put("imageDpi", String.valueOf(imageDpi));
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
