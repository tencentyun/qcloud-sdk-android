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

package com.tencent.cos.xml.model.ci.ai;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class PostTranslation {

    /**
     * 创建任务的 Tag：Translation;是否必传：是
     */
    public String tag = "Translation";

    /**
     * 待操作的对象信息;是否必传：是
     */
    public PostTranslationInput input;

    /**
     * 操作规则;是否必传：是
     */
    public PostTranslationOperation operation;

    /**
     * 任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
     */
    public String callBackFormat;

    /**
     * 任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
     */
    public String callBackType;

    /**
     * 任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
     */
    public String callBack;

    /**
     * 任务回调TDMQ配置，当 CallBackType 为 TDMQ 时必填。详情见 CallBackMqConfig;是否必传：否
     */
    public CallBackMqConfig callBackMqConfig;

    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.ALL)
    public static class PostTranslationInput {
        /**
         * 源文档文件名单文件（docx/xlsx/html/markdown/txt）：不超过800万字符有页数的（pdf/pptx）：不超过300页文本文件（txt）：不超过10MB二进制文件（pdf/docx/pptx/xlsx）：不超过60MB图片文件（jpg/jpeg/png/webp）：不超过10MB;是否必传：是
         */
        public String object;

        /**
         * 文档语言类型zh：简体中文zh-hk：繁体中文zh-tw：繁体中文zh-tr：繁体中文en：英语ar：阿拉伯语de：德语es：西班牙语fr：法语id：印尼语it：意大利语ja：日语pt：葡萄牙语ru：俄语ko：韩语km：高棉语lo：老挝语;是否必传：是
         */
        public String lang;

        /**
         * 文档类型pdfdocxpptxxlsxtxtxmlhtml：只能翻译 HTML 里的文本节点，需要通过 JS 动态加载的不进行翻译markdownjpgjpegpngwebp;是否必传：是
         */
        public String type;

        /**
         * 原始文档类型仅在 Type=pdf/jpg/jpeg/png/webp 时使用，当值为pdf时，仅支持 docx、pptx当值为jpg/jpeg/png/webp时，仅支持txt;是否必传：否
         */
        public String basicType;

    }
    @XmlBean(name = "Operation", method = XmlBean.GenerateMethod.TO)
    public static class PostTranslationOperation {
        /**
         * 翻译参数;是否必传：是
         */
        public PostTranslationTranslation translation;

        /**
         * 结果输出地址，当NoNeedOutput为true时非必选;是否必传：否
         */
        public PostTranslationOutput output;

        /**
         * 透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
         */
        public String userData;

        /**
         * 任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
         */
        public String jobLevel;

        /**
         * 仅输出结果，不生成结果文件。取值：true/false。该参数原文档类型为图片时有效。默认值 false;是否必传：否
         */
        public String noNeedOutput;

    }
    @XmlBean(name = "Output", method = XmlBean.GenerateMethod.ALL)
    public static class PostTranslationOutput {
        /**
         * 存储桶的地域;是否必传：是
         */
        public String region;

        /**
         * 存储结果的存储桶;是否必传：是
         */
        public String bucket;

        /**
         * 输出结果的文件名;是否必传：是
         */
        public String object;

    }
    @XmlBean(name = "Translation", method = XmlBean.GenerateMethod.ALL)
    public static class PostTranslationTranslation {
        /**
         * 目标语言类型源语言类型为 zh/zh-hk/zh-tw/zh-tr 时支持：en、ar、de、es、fr、id、it、ja、it、ru、ko、km、lo、pt源语言类型为 en 时支持：zh、zh-hk、zh-tw、zh-tr、ar、de、es、fr、id、it、ja、it、ru、ko、km、lo、pt其他类型时支持：zh、zh-hk、zh-tw、zh-tr、en;是否必传：是
         */
        public String lang;

        /**
         * 文档类型，源文件类型与目标文件类型映射关系如下：docx：docxpptx：pptxxlsx：xlsxtxt：txtxml：xmlhtml：htmlmarkdown：markdownpdf：pdf、docxpng：txtjpg：txtjpeg：txtwebp：txt;是否必传：是
         */
        public String type;

    }
    @XmlBean(name = "CallBackMqConfig", method = XmlBean.GenerateMethod.TO)
    public static class CallBackMqConfig {
        /**
         * 消息队列所属园区，目前支持园区 sh（上海）、bj（北京）、gz（广州）、cd（成都）、hk（中国香港）;是否必传：是
         */
        public String mqRegion;

        /**
         * 消息队列使用模式，默认 Queue ：主题订阅：Topic队列服务: Queue;是否必传：是
         */
        public String mqMode;

        /**
         * TDMQ 主题名称;是否必传：是
         */
        public String mqName;

    }

   
}
