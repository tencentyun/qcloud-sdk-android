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

package com.tencent.cos.xml.model.ci.media;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class UpdateMediaQueue {

    /**
     *队列名称，长度不超过128;是否必传：是;默认值：无;限制：无;
     */
    public String name;

    /**
     *1. Active 表示队列内的作业会被媒体处理服务调度执行2. Paused 表示队列暂停，作业不再会被媒体处理调度执行，队列内的所有作业状态维持在暂停状态，已经执行中的任务不受影响;是否必传：是;默认值：无;限制：无;
     */
    public String state;

    /**
     *回调配置;是否必传：是;默认值：无;限制：无;
     */
    public UpdateMediaQueueNotifyConfig notifyConfig;

    @XmlBean(name = "NotifyConfig")
    public static class UpdateMediaQueueNotifyConfig {
        /**
         *回调开关，Off/On;是否必传：否;默认值：Off;限制：On/Off;
         */
        public String state;

        /**
         *回调事件;是否必传：当 State=On 时，必选;默认值：无;限制：任务完成：TaskFinish；工作流完成：WorkflowFinish;
         */
        public String event;

        /**
         *回调格式;是否必传：否;默认值：XML;限制：JSON/XML;
         */
        public String resultFormat;

        /**
         *回调类型;是否必传：当 State=On 时，必选;默认值：无;限制：Url 或 TDMQ;
         */
        public String type;

        /**
         *回调地址;是否必传：当 State=On，且 Type=Url 时，必选;默认值：无;限制：不能为内网地址;
         */
        public String url;

        /**
         *TDMQ 使用模式;是否必传：当 State=On，且 Type=TDMQ 时，必选;默认值：Queue;限制：主题订阅：Topic 队列服务: Queue;
         */
        public String mqMode;

        /**
         *TDMQ 所属园区;是否必传：当 State=On，且 Type=TDMQ 时，必选;默认值：无;限制：目前支持园区 sh（上海）、bj（北京）、gz（广州）、cd（成都）、hk（中国香港）;
         */
        public String mqRegion;

        /**
         *TDMQ 主题名称;是否必传：当 State=On，且 Type=TDMQ 时，必选;默认值：无;限制：无;
         */
        public String mqName;

    }


   
}
