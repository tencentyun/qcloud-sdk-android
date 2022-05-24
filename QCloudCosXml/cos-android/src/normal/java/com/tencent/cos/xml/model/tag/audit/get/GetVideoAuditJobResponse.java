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

package com.tencent.cos.xml.model.tag.audit.get;


import com.tencent.cos.xml.model.tag.audit.bean.AuditJobsDetail;
import com.tencent.cos.xml.model.tag.audit.bean.AuditScenarioInfo;
import com.tencent.cos.xml.model.tag.audit.bean.ImageAuditScenarioInfo;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * 审核返回的具体响应内容
 */
@XmlBean(name = "Response")
public class GetVideoAuditJobResponse {
    /**
     * 每次请求发送时，服务端将会自动为请求生成一个 ID，遇到问题时，该 ID 能更快地协助定位问题。
     */
    public String requestId;
    /**
     * 视频审核任务的详细信息
     */
    public VideoAuditJobsDetail jobsDetail;

    @XmlBean
    public static class VideoAuditJobsDetail extends AuditJobsDetail {
        /**
         * 本次审核的文件名称，创建任务使用 Object 时返回。
         */
        public String object;
        /**
         * 本次审核的文件链接，创建任务使用 Url 时返回
         */
        public String url;
        /**
         * 视频截图的总数量
         */
        public String snapshotCount;
        /**
         * 审核场景为涉黄的审核结果信息
         */
        public AuditScenarioInfo pornInfo;
        /**
         * 审核场景为涉暴恐的审核结果信息
         */
        public AuditScenarioInfo terrorismInfo;
        /**
         * 审核场景为政治敏感的审核结果信息
         */
        public AuditScenarioInfo politicsInfo;
        /**
         * 审核场景为广告引导的审核结果信息
         */
        public AuditScenarioInfo adsInfo;
        /**
         * 该字段用于返回视频中视频画面截图审核的结果。
         */
        @XmlElement(flatListNote = true)
        public List<Snapshot> snapshot;
        /**
         * 该字段用于返回视频中视频声音审核的结果。如果未检测音频，则不返回。
         */
        @XmlElement(flatListNote = true)
        public List<GetAudioAuditJobResponse.AudioSection> audioSection;
    }

    /**
     * 该字段用于返回视频中视频画面截图审核的结果。
     */
    @XmlBean
    public static class Snapshot{
        /**
         * 频截图的访问地址，您可以通过该地址查看该截图内容，地址格式为标准 URL 格式
         */
        public String url;
        /**
         * 该字段用于返回当前截图位于视频中的时间，单位为毫秒。例如5000（视频开始后5000毫秒）
         */
        public int snapshotTime;
        /**
         * 该字段用于返回当前截图的图片 OCR 文本识别的检测结果（仅在审核策略开启文本内容检测时返回），识别上限为5000字节
         */
        public String text;
        /**
         * 审核场景为涉黄的审核结果信息。
         */
        public SnapshotAuditScenarioInfo pornInfo;
        /**
         * 审核场景为涉暴恐的审核结果信息。
         */
        public SnapshotAuditScenarioInfo terrorismInfo;
        /**
         * 审核场景为政治敏感的审核结果信息。
         */
        public SnapshotAuditScenarioInfo politicsInfo;
        /**
         * 审核场景为广告引导的审核结果信息。
         */
        public SnapshotAuditScenarioInfo adsInfo;
    }

    /**
     * 审核场景的截图审核结果信息
     */
    @XmlBean
    public static class SnapshotAuditScenarioInfo extends ImageAuditScenarioInfo {
        /**
         * 该字段为兼容旧版本的保留字段，表示该截图的结果标签（可能为 SubLabel，可能为人物名字等）
         */
        public String label;
    }
}
