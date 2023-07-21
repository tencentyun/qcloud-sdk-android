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
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

/**
 * 审核规则配置
 */
@XmlBean(name = "Conf", method = XmlBean.GenerateMethod.TO)
public class AuditConf {
    /**
     * 审核的场景类型，有效值：Porn（涉黄）、Terrorism（涉暴恐）、Politics（政治敏感）、Ads（广告）、Illegal（违法）、Abuse（谩骂），可以传入多种类型，不同类型以逗号分隔，例如：Porn,Terrorism
     */
    public String detectType;
    /**
     * 是否异步进行审核，0：同步返回结果，1：异步进行审核。默认值为 0。
     */
    public int async;
    /**
     * 审核结果以回调形式发送至您的回调地址，支持以 http:// 或者 https:// 开头的地址，例如：http://www.callback.com。
     */
    public String callback;
    /**
     * 回调片段类型，有效值：1（回调全部分页）、2（回调违规分页）。默认为 1。
     */
    @XmlElement(ignoreZero = true)
    public int callbackType;
    /**
     * 审核策略，不填写则使用默认策略。可在控制台进行配置，详情请参见 <a href="https://cloud.tencent.com/document/product/436/55206">设置公共审核策略</a>
     */
    public String bizType;

    /**
     * 可通过该字段，设置根据审核结果给出的不同分值，对图片进行自动冻结，仅当input中审核的图片为object时有效。
     */
    public Freeze freeze;

    @XmlBean(name = "Freeze", method = XmlBean.GenerateMethod.TO)
    public static class Freeze {
        /**
         * 取值为[0,100]，表示当色情审核结果大于或等于该分数时，自动进行冻结操作。不填写则表示不自动冻结，默认值为空。
         */
        public int pornScore;
        /**
         * 取值为[0,100]，表示当广告审核结果大于或等于该分数时，自动进行冻结操作。不填写则表示不自动冻结，默认值为空。
         */
        public int adsScore;
        /**
         * 取值为[0,100]，表示当违法审核结果大于或等于该分数时，自动进行冻结操作。不填写则表示不自动冻结，默认值为空。
         */
        public int illegalScore;
        /**
         * 取值为[0,100]，表示当谩骂审核结果大于或等于该分数时，自动进行冻结操作。不填写则表示不自动冻结，默认值为空。
         */
        public int abuseScore;
    }
}
