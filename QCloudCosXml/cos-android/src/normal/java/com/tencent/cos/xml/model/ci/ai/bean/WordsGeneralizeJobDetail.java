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

package com.tencent.cos.xml.model.ci.ai.bean;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * AI分词识别任务结果的详细信息
 */
@XmlBean(name = "JobsDetail")
public class WordsGeneralizeJobDetail {
    /**
     * 任务的 ID
     */
    public String jobId;
    /**
     * 审核任务的状态
     *  Submitted、Running、Success、Failed、Pause、Cancel 其中一个
     */
    public String state;
    /**
     * 任务的创建时间
     */
    public String creationTime;
    /**
     * 任务的开始时间
     */
    public String startTime;
    /**
     * 任务的结束时间
     */
    public String endTime;
    /**
     * 错误码，只有 State 为 Failed 时有意义
     */
    public String code;
    /**
     * 错误描述，只有 State 为 Failed 时有意义
     */
    public String message;
    /**
     * 任务的 Tag：WordsGeneralize
     */
    public String tag;
    /**
     * 任务所属的队列 ID
     */
    public String queueId;
    /**
     * 该任务的输入资源地址
     */
    public WordsGeneralizeJobInput input;
    /**
     * 操作规则
     */
    public WordsGeneralizeJobDetailOperation operation;

    /**
     * 操作规则
     */
    @XmlBean(name = "Operation")
    public static class WordsGeneralizeJobDetailOperation extends WordsGeneralizeJobOperation {
        /**
         * 分词结果, 任务执行成功时返回
         */
        public WordsGeneralizeResult wordsGeneralizeResult;
    }

    @XmlBean
    public static class WordsGeneralizeResult {
        /**
         * 智能分类结果
         */
        @XmlElement(flatListNote = true)
        public List<WordsGeneralizeLable> wordsGeneralizeLable;

        /**
         * 分词详细结果
         */
        @XmlElement(flatListNote = true)
        public List<WordsGeneralizeToken> wordsGeneralizeToken;
    }

    @XmlBean
    public static class WordsGeneralizeLable {
        /**
         * 类别
         */
        public String category;
        /**
         * 词汇
         */
        public String word;
    }

    @XmlBean
    public static class WordsGeneralizeToken {
        /**
         * 词汇
         */
        public String word;
        /**
         * 偏移量
         */
        public String offset;

        /**
         * 词汇长度
         */
        public String length;

        /**
         * 词性
         */
        public String pos;
    }
}
