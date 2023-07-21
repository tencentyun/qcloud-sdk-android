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

package com.tencent.cos.xml.model.ci.audit;

import com.tencent.cos.xml.model.tag.audit.bean.AuditListInfo;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class GetLiveVideoAuditResponse {
    
    /**
     *直播审核任务的详细信息。
     */
    public GetLiveVideoAuditResponseJobsDetail jobsDetail;

    /**
     *每次请求发送时，服务端将会自动为请求生成一个 ID，遇到问题时，该 ID 能更快地协助定位问题。
     */
    public String requestId;

    @XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
    public static class GetLiveVideoAuditResponseJobsDetail {
        /**
         *错误码，只有 State 为 Failed 时返回。详情请查看 错误码列表。
         */
        public String code;

        /**
         *错误描述，只有 State 为 Failed 时返回。
         */
        public String message;

        /**
         *提交任务时设置了 DataId 参数时返回，返回原始内容，长度限制为512字节。您可以使用该字段对待审核的数据进行唯一业务标识。
         */
        public String dataId;

        /**
         *直播审核任务的 ID。
         */
        public String jobId;

        /**
         *直播审核任务的状态，值为 Submitted（已提交审核）、Success（审核成功）、Failed（审核失败）、Auditing（审核中）其中一个。
         */
        public String state;

        /**
         *直播审核任务的创建时间。
         */
        public String creationTime;

        /**
         *当审核的直播包含视频流时，该字段表示直播截图的总数量。
         */
        public String snapshotCount;

        /**
         *该字段用于返回检测结果中所对应的优先级最高的恶意标签，表示模型推荐的审核结果，建议您按照业务所需，对不同违规类型与建议值进行处理。 返回值：Normal：正常，Porn：色情，Ads：广告，以及其他不安全或不适宜的类型。
         */
        public String label;

        /**
         *该字段表示本次判定的审核结果，您可以根据该结果，进行后续的操作；建议您按照业务所需，对不同的审核结果进行相应处理。有效值：0（审核正常），1 （判定为违规敏感内容），2（疑似敏感内容，建议人工复核）。
         */
        public int result;

        /**
         *审核场景为涉黄的审核结果信息。
         */
        public GetLiveVideoAuditResponseInfo pornInfo;

        /**
         *审核场景为广告引导的审核结果信息。
         */
        public GetLiveVideoAuditResponseInfo adsInfo;

        /**
         *审核场景为低质量的审核结果信息。
         */
        public GetLiveVideoAuditResponseInfo meaninglessInfo;

        /**
         *该字段用于返回直播中画面截图审核的结果。
         */
        @XmlElement(flatListNote = true)
        public List<GetLiveVideoAuditResponseSnapshot> snapshot;

        /**
         *当审核的直播包含音频流时，该字段用于返回直播中声音审核的结果。如果未检测音频，则不返回。
         */
        @XmlElement(flatListNote = true)
        public List<GetLiveVideoAuditResponseAudioSection> audioSection;

        /**
         *用户业务字段。创建任务未设置 UserInfo 时无此字段。
         */
        public GetLiveVideoAuditResponseUserInfo userInfo;

        /**
         * 账号黑白名单结果。
         */
        public AuditListInfo listInfo;

        /**
         *审核类型，直播审核固定为 live_video。
         */
        public String type;

    }

    @XmlBean(name = "UserInfo", method = XmlBean.GenerateMethod.FROM)
    public static class GetLiveVideoAuditResponseUserInfo {
        /**
         *一般用于表示账号信息，长度不超过128字节。
         */
        public String tokenId;

        /**
         *一般用于表示昵称信息，长度不超过128字节。
         */
        public String nickname;

        /**
         *一般用于表示设备信息，长度不超过128字节。
         */
        public String deviceId;

        /**
         *一般用于表示 App 的唯一标识，长度不超过128字节。
         */
        public String appId;

        /**
         *一般用于表示房间号信息，长度不超过128字节。
         */
        public String room;

        /**
         *一般用于表示 IP 地址信息，长度不超过128字节。
         */
        public String iP;

        /**
         *一般用于表示业务类型，长度不超过128字节。
         */
        public String type;

        /**
         *一般用于表示接收消息的用户账号，长度不超过128字节。
         */
        public String receiveTokenId;

        /**
         *一般用于表示性别信息，长度不超过128字节。
         */
        public String gender;

        /**
         *一般用于表示等级信息，长度不超过128字节。
         */
        public String level;

        /**
         *一般用于表示角色信息，长度不超过128字节。
         */
        public String role;

    }

    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class GetLiveVideoAuditResponseInfo {
        /**
         *是否命中该审核分类，0表示未命中，1表示命中，2表示疑似。
         */
        public int hitFlag;

        /**
         *命中该审核分类的截图张数。
         */
        public int count;
    }

    @XmlBean(name = "Snapshot", method = XmlBean.GenerateMethod.FROM)
    public static class GetLiveVideoAuditResponseSnapshot {
        /**
         *直播截图的访问地址，您可以通过该地址查看该截图内容，地址格式为标准 URL 格式。注意：每次查看数据的有效期为2小时，2小时后如还需查看，请重新发起查询请求。
         */
        public String url;

        /**
         *该字段返回当前截图的时间戳，单位为毫秒。例如1649387157000。
         */
        public int snapshotTime;

        /**
         *该字段用于返回当前截图的图片 OCR 文本识别的检测结果，仅在审核策略开启文本内容检测时返回。
         */
        public String text;

        /**
         *该字段用于返回检测结果中所对应的优先级最高的恶意标签，表示模型推荐的审核结果，建议您按照业务所需，对不同违规类型与建议值进行处理。 返回值：Normal：正常，Porn：色情，Ads：广告，以及其他不安全或不适宜的类型。
         */
        public String label;

        /**
         *该字段表示本次判定的审核结果，您可以根据该结果，进行后续的操作；建议您按照业务所需，对不同的审核结果进行相应处理。有效值：0（审核正常），1 （判定为违规敏感内容），2（疑似敏感内容，建议人工复核）。
         */
        public int result;

        /**
         *审核场景为涉黄的审核结果信息。
         */
        public GetLiveVideoAuditResponseSnapshotInfo pornInfo;

        /**
         *审核场景为广告引导的审核结果信息。
         */
        public GetLiveVideoAuditResponseSnapshotInfo adsInfo;

        /**
         *审核场景为低质量的审核结果信息。
         */
        public GetLiveVideoAuditResponseSnapshotInfo meaninglessInfo;
    }

    @XmlBean(name = "AudioSection", method = XmlBean.GenerateMethod.FROM)
    public static class GetLiveVideoAuditResponseAudioSection {
        /**
         *直播声音片段的访问地址，您可以通过该地址获取该声音片段的内容，地址格式为标准 URL 格式。注意：每次查看数据的有效期为2小时，2小时后如还需查看，请重新发起查询请求。
         */
        public String url;

        /**
         *该字段用于返回当前直播声音片段的 ASR 文本识别的检测结果。
         */
        public String text;

        /**
         *该字段返回当前声音片段的时间戳，单位为毫秒，例如1649387157000。
         */
        public int offsetTime;

        /**
         *当前声音片段的长度，单位毫秒。
         */
        public int duration;

        /**
         *该字段用于返回检测结果中所对应的优先级最高的恶意标签，表示模型��荐的审核结果，建议您按照业务所需，对不同违规类型与建议值进行处理。 返回值：Normal：正常，Porn：色情，Ads：广告，以及其他不安全或不适宜的类型。
         */
        public String label;

        /**
         *该字段表示本次判定的审核结果，您可以根据该结果，进行后续的操作；建议您按照业务所需，对不同的审核结果进行相应处理。有效值：0（审核正常），1 （判定为违规敏感内容），2（疑似敏感内容，建议人工复核）。
         */
        public int result;

        /**
         *审核场景为涉黄的审核结果信息。
         */
        public GetLiveVideoAuditResponseAudioSectionInfo pornInfo;

        /**
         *审核场景为广告引导的审核结果信息。
         */
        public GetLiveVideoAuditResponseAudioSectionInfo adsInfo;

    }

    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class GetLiveVideoAuditResponseSnapshotInfo {
        /**
         *是否命中该审核分类，0表示未命中，1表示命中，2表示疑似。
         */
        public int hitFlag;

        /**
         *该字段表示审核结果命中审核信息的置信度，取值范围：0（置信度最低）-100（置信度最高 ），越高代表该内容越有可能属于当前返回的审核信息。例如：色情 99，则表明该内容非常有可能属于色情内容。
         */
        public int score;

        /**
         *该字段表示该截图的综合结果标签（可能为 SubLabel，可能为人物名字等）。
         */
        public String label;

        /**
         *该字段表示命中的审核类别。注意：该字段可能返回空。
         */
        public String category;

        /**
         *该字段表示审核命中的具体子标签，例如：Porn 下的 SexBehavior 子标签。注意：该字段可能返回空，表示未命中具体的子标签。
         */
        public String subLabel;

        /**
         *该字段表示 OCR 文本识别的详细检测结果，包括文本坐标信息、文本识别结果等信息，有相关违规内容时返回。
         */
        @XmlElement(flatListNote = true)
        public List<SnapshotInfoOcrResults> ocrResults;

        /**
         *该字段用于返回基于风险库识别的结果。注意：未命中风险库中样本时，此字段不返回。
         */
        @XmlElement(flatListNote = true)
        public List<SnapshotInfoLibResults> libResults;

        @XmlElement(flatListNote = true)
        public List<SnapshotInfoObjectResults> objectResults;
    }

    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class GetLiveVideoAuditResponseAudioSectionInfo {
        /**
         *是否命中该审核分类，0表示未命中，1表示命中，2表示疑似。
         */
        public String hitFlag;

        /**
         *该字段表示审核结果命中审核信息的置信度，取值范围：0（置信度最低）-100（置信度最高 ），越高代表该内容越有可能属于当前返回审核信息例如：色情 99，则表明该内容非常有可能属于色情内容。
         */
        public String score;

        /**
         *该字段表示命中的审核类别。注意：该字段可能返回空。
         */
        public String category;

        /**
         *在当前审核场景下命中的关键词，没有时不返回。
         */
        @XmlElement(flatListNote = true)
        public List<String> keywords;

        /**
         *该字段用于返回基于风险库识别的结果。注意：未命中风险库中样本时，此字段不返回。
         */
        @XmlElement(flatListNote = true)
        public List<AudioSectionInfoLibResults> libResults;

    }

    @XmlBean(name = "LibResults", method = XmlBean.GenerateMethod.FROM)
    public static class SnapshotInfoLibResults {
        /**
         *该字段表示命中的风险库中的图片样本 ID。
         */
        public String imageId;

        /**
         *该字段用于返回当前标签下的置信度，取值范围：0（置信度最低）-100（置信度最高 ），越高代表当前的图片越有可能命中库中的样本；如：色情 99，则表明该数据非常有可能命中库中的色情样本。
         */
        public int score;

    }

    @XmlBean(name = "OcrResults", method = XmlBean.GenerateMethod.FROM)
    public static class SnapshotInfoOcrResults {
        /**
         *图片 OCR 文本识别出的具体文本内容。
         */
        public String text;

        /**
         *在当前审核场景下命中的关键词。
         */
        @XmlElement(flatListNote = true)
        public List<String> keywords;

        /**
         *该参数用于返回 OCR 检测框在图片中的位置（左上角 xy 坐标、长宽、旋转角度），以方便快速定位识别文字的相关信息。
         */
        public SnapshotInfoOcrResultsLocation location;

    }

    @XmlBean(name = "ObjectResults", method = XmlBean.GenerateMethod.FROM)
    public static class SnapshotInfoObjectResults {
        /**
         * 该标签用于返回所识别出的实体名称，例如人名。
         */
        public String name;

        /**
         *该参数用于返回 OCR 检测框在图片中的位置（左上角 xy 坐标、长宽、旋转角度），以方便快速定位识别文字的相关信息。
         */
        public SnapshotInfoOcrResultsLocation location;

    }

    @XmlBean(name = "ObjectResults", method = XmlBean.GenerateMethod.FROM)
    public static class SnapshotInfoOcrResultsLocation {
        /**
         * 该参数用于返回检测框左上角位置的横坐标（x）所在的像素位置，结合剩余参数可唯一确定检测框的大小和位置。
         */
        public float x;

        /**
         * 该参数用于返回检测框左上角位置的纵坐标（y）所在的像素位置，结合剩余参数可唯一确定检测框的大小和位置。
         */
        public float y;

        /**
         * 该参数用于返回检测框的高度（由左上角出发在 y 轴向下延伸的长度），结合剩余参数可唯一确定检测框的大小和位置。
         */
        public float height;

        /**
         * 该参数用于返回检测框的宽度（由左上角出发在 x 轴向右延伸的长度），结合剩余参数可唯一确定检测框的大小和位置。
         */
        public float width;

        /**
         * 该参数用于返回检测框的旋转角度，该参数结合 X 和 Y 两个坐标参数可唯一确定检测框的具体位置；取值：0 - 360（角度制），方向为逆时针旋转。
         */
        public float rotate;

    }

    @XmlBean(name = "LibResults", method = XmlBean.GenerateMethod.FROM)
    public static class AudioSectionInfoLibResults {
        /**
         *命中的风险库类型，取值为1（预设风险库）和2（自定义风险库）。
         */
        public int libType;

        /**
         *命中的风险库名称。
         */
        public String libName;

        /**
         *命中的库中关键词。该参数可能会有多个返回值，代表命中的多个关键词。
         */
        @XmlElement(flatListNote = true)
        public List<String> keywords;

    }


}
