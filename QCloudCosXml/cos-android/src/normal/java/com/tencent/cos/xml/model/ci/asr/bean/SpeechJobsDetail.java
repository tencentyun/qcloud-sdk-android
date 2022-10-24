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

import com.tencent.cos.xml.model.ci.ai.bean.WordsGeneralizeJobDetail;
import com.tencent.cos.xml.model.tag.Locator;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * 语音识别任务结果的详细信息
 */
@XmlBean(name = "JobsDetail")
public class SpeechJobsDetail {
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
     * 任务的 Tag：SpeechRecognition
     */
    public String tag;
    /**
     * 任务所属的队列 ID
     */
    public String queueId;
    /**
     * 该任务的输入资源地址
     */
    public SpeechJobsDetailInput input;
    /**
     * 操作规则
     */
    public SpeechJobsDetailOperation operation;

    /**
     * 操作规则
     */
    @XmlBean(name = "Operation")
    public static class SpeechJobsDetailOperation {
        /**
         * 当 Tag 为 SpeechRecognition 时有效，指定该任务的参数
         */
        public SpeechRecognition speechRecognition;

        /**
         * 结果输出地址
         */
        public Locator output;

        /**
         * 透传用户信息
         */
        public String userData;

        /**
         * 任务优先级
         */
        public int jobLevel;

        /**
         * 任务的模板 ID
         */
        public String templateId;

        /**
         * 在 job 的类型为 SpeechRecognition 且 job 状态为 success 时，返回语音识别的识别结果详情。
         */
        public SpeechRecognitionResult speechRecognitionResult;
    }

    @XmlBean
    public static class SpeechJobsDetailInput {
        /**
         * 存储桶的地域
         */
        public String region;

        /**
         * 存储结果的存储桶
         */
        public String bucketId;

        /**
         * 结果文件的名称
         */
        public String object;
    }

    @XmlBean
    public static class SpeechRecognitionResult {
        /**
         * 语音时长
         */
        public double audioTime;

        /**
         * 识别结果
         */
        @XmlElement(name = "result")
        public String result;

        /**
         * 极速语音识别结果
         */
        @XmlElement(flatListNote = true)
        public List<FlashResult> flashResult;

        /**
         * 识别结果详情，包含每个句子中的词时间偏移，一般用于生成字幕的场景。(识别请求中ResTextFormat=1时该字段不为空)
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        @XmlElement(flatListNote = true)
        public List<SentenceDetail> resultDetail;

        /**
         * 分词结果
         */
        public WordsGeneralizeJobDetail.WordsGeneralizeResult wordsGeneralizeResult;
    }

    @XmlBean
    public static class FlashResult {
        /**
         * 声道标识，从0开始，对应音频声道数
         */
        @XmlElement(name = "channel_id")
        public int channelId;

        /**
         * 声道音频完整识别结果
         */
        @XmlElement(name = "text")
        public String text;

        /**
         * Container 数组
         */
        @XmlElement(flatListNote = true, name = "sentence_list")
        public List<FlashSentence> sentenceList;
    }

    @XmlBean
    public static class FlashSentence {
        /**
         * 句子/段落级别文本
         */
        @XmlElement(name = "text")
        public String text;

        /**
         * 开始时间
         */
        @XmlElement(name = "start_time")
        public int startTime;

        /**
         * 结束时间
         */
        @XmlElement(name = "end_time")
        public int endTime;

        /**
         * 说话人 Id（请求中如果设置了 speaker_diarization，可以按照 speaker_id 来区分说话人）
         */
        @XmlElement(name = "speaker_id")
        public int speakerId;

        /**
         * 词级别的识别结果列表
         */
        @XmlElement(flatListNote = true, name = "word_list")
        public List<FlashSentenceWord> wordList;
    }

    @XmlBean
    public static class FlashSentenceWord {
        /**
         * 词级别文本
         */
        @XmlElement(name = "word")
        public String word;

        /**
         * 开始时间
         */
        @XmlElement(name = "start_time")
        public int startTime;

        /**
         * 结束时间
         */
        @XmlElement(name = "end_time")
        public int endTime;
    }

    @XmlBean
    public static class SentenceDetail {
        /**
         * 单句开始时间（毫秒）
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        public String startMs;
        /**
         * 单句结束时间（毫秒）
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        public String endMs;

        /**
         * 单句最终识别结果
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        public String finalSentence;

        /**
         * 单句中间识别结果，使用空格拆分为多个词
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        public String sliceSentence;
        /**
         * 声道或说话人 Id（请求中如果设置了 speaker_diarization或者ChannelNum为双声道，可区分说话人或声道）
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        public String speakerId;
        /**
         * 单句语速，单位：字数/秒
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        public String speechSpeed;
        /**
         * 单句中词个数
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        public String wordsNum;

        /**
         * 单句中词详情
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        @XmlElement(flatListNote = true)
        public List<SentenceWords> words;
    }

    @XmlBean
    public static class SentenceWords {
        /**
         * 在句子中的开始时间偏移量
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        public String offsetStartMs;
        /**
         * 在句子中的结束时间偏移量
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        public String offsetEndMs;

        public String voiceType;

        /**
         * 词文本
         * 注意：此字段可能返回 null，表示取不到有效值。
         */
        public String word;
    }
}
