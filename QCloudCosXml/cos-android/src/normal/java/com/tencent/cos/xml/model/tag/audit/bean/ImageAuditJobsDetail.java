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
 * 图片审核任务的详细信息
 */
@XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
public class ImageAuditJobsDetail extends AuditJobsDetail {
    /**
     * 该字段表示审核结果命中审核信息的置信度，取值范围：0（置信度最低）-100（置信度最高 ），越高代表该内容越有可能属于当前返回审核信息
     * 例如：色情 99，则表明该内容非常有可能属于色情内容
     */
    public int score;
    /**
     * 该图里的文字内容（OCR），当审核策略开启文本内容检测时返回
     */
    public String text;
    /**
     * 图片文件的链接地址，创建任务使用Url时返回
     */
    public String url;
    /**
     * 存储在 COS 存储桶中的图片文件名称，创建任务使用Object时返回。
     */
    public String object;
    /**
     * 图片是否被压缩处理，值为 0（未压缩），1（正常压缩）。
     */
    public int compressionResult;
    /**
     * 审核场景为涉黄的审核结果信息。
     */
    public ImagesAuditScenarioInfo pornInfo;
    /**
     * 审核场景为涉暴恐的审核结果信息。
     */
    public ImagesAuditScenarioInfo terrorismInfo;
    /**
     * 审核场景为政治敏感的审核结果信息。
     */
    public ImagesAuditScenarioInfo politicsInfo;
    /**
     * 审核场景为广告引导的审核结果信息。
     */
    public ImagesAuditScenarioInfo adsInfo;
    /**
     * 审核场景为低质量的审核结果信息。
     */
    public ImagesAuditScenarioInfo qualityInfo;

    public static class ImagesAuditScenarioInfo extends ImageAuditScenarioInfo {
        /**
         * 单个审核场景的错误码，0为成功，其他为失败。详情请参见 错误码
         */
        public int code;
        /**
         * 具体错误信息，如正常则为 OK
         */
        public String msg;
        /**
         * 该图的结果标签（为综合标签，可能为 SubLabel，可能为人物名字等）
         */
        public String label;
    }
}
