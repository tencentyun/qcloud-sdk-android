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

package com.tencent.cos.xml.model.tag.audit.bean;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * 需要审核的内容
 */
@XmlBean(name = "Input", method = XmlBean.GenerateMethod.TO)
public class AuditInput {
    /**
     * 当前 COS 存储桶中的文件名称，例如在目录 test 中的文件 video.mp4，则文件名称为 test/video.mp4。
     * 文本文件仅支持UTF8编码和 GBK 编码的内容，且文件大小不得超过1MB
     * Object 和 Url 只能选择其中一种。
     */
    public String object;
    /**
     * 文件的链接地址，例如 http://examplebucket-1250000000.cos.ap-shanghai.myqcloud.com/test.mp4。
     * Object 和 Url 只能选择其中一种。
     */
    public String url;
    /**
     *该 字段在审核结果中会返回原始内容，长度限制为512字节。您可以使用该字段对待审核的数据进行唯一业务标识。
     */
    public String dataId;
}
