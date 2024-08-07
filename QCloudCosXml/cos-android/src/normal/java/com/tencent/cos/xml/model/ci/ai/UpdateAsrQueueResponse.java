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

import com.tencent.cos.xml.model.ci.common.NotifyConfig;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class UpdateAsrQueueResponse {
    
    /**
     * 请求的唯一 ID
     */
    public String requestId;
    /**
     * 队列信息
     */
    public UpdateAsrQueueResponseQueue queue;
    @XmlBean(name = "Queue", method = XmlBean.GenerateMethod.FROM)
    public static class UpdateAsrQueueResponseQueue {
        /**
         * 队列 ID
         */
        public String queueId;
        /**
         * 队列名字
         */
        public String name;
        /**
         * 当前状态，Active 或者 Paused
         */
        public String state;
        /**
         * 回调配置，同 Request.NotifyConfig
         */
        public NotifyConfig notifyConfig;
        /**
         * 队列最大长度
         */
        public int maxSize;
        /**
         * 当前队列最大并行执行的任务数
         */
        public int maxConcurrent;
        /**
         * 队列类型
         */
        public String category;
        /**
         * 更新时间
         */
        public String updateTime;
        /**
         * 创建时间
         */
        public String createTime;
    }

}
