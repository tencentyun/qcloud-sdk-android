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

/**
 * 提交一个分词任务请求实体
 */
@XmlBean(name = "Request")
public class CreateWordsGeneralizeJob {
    /**
     * 创建任务的 Tag，目前仅支持：WordsGeneralize
     */
    public String tag = "WordsGeneralize";

    /**
     * 待操作的对象信息
     */
    public WordsGeneralizeJobInput input;
    /**
     * 操作规则
     */
    public WordsGeneralizeJobOperation operation;

    /**
     * 任务所在的队列 ID
     */
    public String queueId;

    /**
     * 任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调
     */
    public String callBack;

    /**
     * 任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式
     */
    public String callBackFormat;

    public CreateWordsGeneralizeJob() {
        this.input = new WordsGeneralizeJobInput();
        this.operation = new WordsGeneralizeJobOperation();
    }
}
