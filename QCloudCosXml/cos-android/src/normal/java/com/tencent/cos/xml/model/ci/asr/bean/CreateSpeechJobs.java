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

package com.tencent.cos.xml.model.ci.asr.bean;

import com.tencent.cos.xml.model.tag.Locator;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * 提交语音识别任务请求实体
 */
@XmlBean(name = "Request")
public class CreateSpeechJobs {
    /**
     * 创建任务的 Tag，目前仅支持：SpeechRecognition
     */
    public String tag = "SpeechRecognition";

    /**
     * 待操作的语音文件
     */
    public CreateSpeechJobsInput input;
    /**
     * 操作规则
     */
    public CreateSpeechJobsOperation operation;

    /**
     * 任务所在的队列 ID
     */
    public String queueId;

    public CreateSpeechJobs() {
        this.input = new CreateSpeechJobsInput();
        this.operation = new CreateSpeechJobsOperation();
    }

    @XmlBean
    public static class CreateSpeechJobsInput {
        /**
         * 文件在 COS 上的 key，Bucket 由 Host 指定
         */
        public String object;
    }

    /**
     * 操作规则
     */
    @XmlBean(name = "Operation")
    public static class CreateSpeechJobsOperation {
        public CreateSpeechJobsOperation() {
            this.speechRecognition = new SpeechRecognition();
            this.output = new Locator();
        }

        /**
         * 当 Tag 为 SpeechRecognition 时有效，指定该任务的参数
         */
        public SpeechRecognition speechRecognition;

        /**
         * 结果输出地址
         */
        public Locator output;

        /**
         * 任务的模板 ID
         */
        public String templateId;
    }
}
