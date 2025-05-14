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

package com.tencent.cos.xml.common_interface;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * 提交病毒检测任务
 */
@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class PostVirusDetect {
    public VirusDetectInput input;
    public VirusDetectConf conf;

    public PostVirusDetect() {
        this.input = new VirusDetectInput();
        this.conf = new VirusDetectConf();
    }

    @XmlBean(name = "Conf", method = XmlBean.GenerateMethod.TO)
    public static class VirusDetectConf {
        /**
         * 检测的病毒类型，当前固定为：Virus。
         */
        public String detectType = "Virus";
        /**
         * 检测结果回调通知到您设置的地址，支持以 http:// 或者 https:// 开头的地址，例如：http://www.callback.com
         */
        public String callback;
    }

    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.TO)
    public static class VirusDetectInput {
        /**
         * 存储在 COS 存储桶中的病毒文件名称，例如在目录 test 中的文件 virus.doc，则文件名称为 test/virus.doc。
         * 注意：Object 和 Url 只能选择其中一种。
         */
        public String object;
        /**
         * 病毒文件的链接地址，例如：http://examplebucket-1250000000.cos.ap-shanghai.myqcloud.com/virus.doc
         * 注意：Object 和 Url 只能选择其中一种。
         */
        public String url;
    }
}
