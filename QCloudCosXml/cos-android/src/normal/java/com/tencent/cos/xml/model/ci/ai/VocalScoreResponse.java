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
public class VocalScoreResponse {
    
    /**
     * 任务的详细信息
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<VocalScoreResponseJobsDetail> jobsDetail;
    @XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
    public static class VocalScoreResponseJobsDetail {
        /**
         * 错误码，只有 State 为 Failed 时有意义
         */
        public String code;
        /**
         * 错误描述，只有 State 为 Failed 时有意义
         */
        public String message;
        /**
         * 新创建任务的 ID
         */
        public String jobId;
        /**
         * 新创建任务的 Tag：VocalScore
         */
        public String tag;
        /**
         * 任务状态Submitted：已提交，待执行Running：执行中Success：执行成功Failed：执行失败Pause：任务暂停，当暂停队列时，待执行的任务会变为暂停状态Cancel：任务被取消执行
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
         * 任务所属的 队列 ID﻿
         */
        public String queueId;
        /**
         * 同请求中的 Request.Input 节点
         */
        public VocalScore.VocalScoreInput input;
        /**
         * 该任务的规则
         */
        public VocalScoreResponseOperation operation;
    }
    @XmlBean(name = "Operation", method = XmlBean.GenerateMethod.FROM)
    public static class VocalScoreResponseOperation {
        /**
         * 透传用户信息
         */
        public String userData;
        /**
         * 任务优先级
         */
        public String jobLevel;
        /**
         * 同请求中的 Request.Operation.VocalScore
         */
        public VocalScore.VocalScoreVocalScore vocalScore;
        /**
         * 音乐评分任务结果，没有时不返回
         */
        public VocalScoreResponseVocalScoreResult vocalScoreResult;
    }
    @XmlBean(name = "VocalScoreResult", method = XmlBean.GenerateMethod.FROM)
    public static class VocalScoreResponseVocalScoreResult {
        /**
         * 音调评分结果详情
         */
        public VocalScoreResponsePitchScore pitchScore;
        /**
         * 节奏评分结果详情
         */
        public VocalScoreResponseRhythemScore rhythemScore;
    }
    @XmlBean(name = "PitchScore", method = XmlBean.GenerateMethod.FROM)
    public static class VocalScoreResponsePitchScore {
        /**
         * 句子评分详情
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<VocalScoreResponseSentenceScores> sentenceScores;
        /**
         * 音调总分
         */
        public int totalScore;
    }
    @XmlBean(name = "SentenceScores", method = XmlBean.GenerateMethod.FROM)
    public static class VocalScoreResponseSentenceScores {
        /**
         * 句子开始时间
         */
        public float startTime;
        /**
         * 句子结束时间
         */
        public float endTime;
        /**
         * 句子评分
         */
        public int score;
    }
    @XmlBean(name = "RhythemScore", method = XmlBean.GenerateMethod.FROM)
    public static class VocalScoreResponseRhythemScore {
        /**
         * 句子评分详情
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<VocalScoreResponseRhythemScoreSentenceScores> sentenceScores;
        /**
         * 音调总分
         */
        public int totalScore;
    }
    @XmlBean(name = "SentenceScores", method = XmlBean.GenerateMethod.FROM)
    public static class VocalScoreResponseRhythemScoreSentenceScores {
        /**
         * 句子开始时间
         */
        public float startTime;
        /**
         * 句子结束时间
         */
        public float endTime;
        /**
         * 句子评分
         */
        public int score;
    }

}
