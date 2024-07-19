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
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class GetAIQueueResponse {
    
    /**
     * 请求的唯一 ID
     */
    public String requestId;
    /**
     * 队列总数
     */
    public int totalCount;
    /**
     * 当前页数，同请求中的 pageNumber
     */
    public int pageNumber;
    /**
     * 每页个数，同请求中的 pageSize
     */
    public int pageSize;
    /**
     * 队列数组
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<GetAIQueueResponseQueueList> queueList;
    /**
     * 不存在的队列 ID 列表
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<String> nonExistPIDs;
    @XmlBean(name = "QueueList", method = XmlBean.GenerateMethod.FROM)
    public static class GetAIQueueResponseQueueList {
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
         * 回调配置
         */
        public GetAIQueueResponseNotifyConfig notifyConfig;
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
    @XmlBean(name = "NotifyConfig", method = XmlBean.GenerateMethod.FROM)
    public static class GetAIQueueResponseNotifyConfig {
        /**
         * 回调地址
         */
        public String url;
        /**
         * 开关状态
         */
        public String state;
        /**
         * 回调类型
         */
        public String type;
        /**
         * 回调事件
         */
        public String event;
        /**
         * 回调类型
         */
        public String resultFormat;
        /**
         * TDMQ 使用模式
         */
        public String mqMode;
        /**
         * TDMQ 所属园区
         */
        public String mqRegion;
        /**
         * TDMQ 主题名称
         */
        public String mqName;
    }

}
