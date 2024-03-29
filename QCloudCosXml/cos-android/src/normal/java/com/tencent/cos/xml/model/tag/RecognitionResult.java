package com.tencent.cos.xml.model.tag;

import com.tencent.cos.xml.model.tag.audit.bean.AuditLibResults;
import com.tencent.cos.xml.model.tag.audit.bean.AuditOcrResults;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * <p>
 * Created by rickenwang on 2021/5/17.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
@XmlBean(name = "RecognitionResult", method = XmlBean.GenerateMethod.FROM)
public class RecognitionResult {
    /**
     * 图片标识，审核结果会返回原始内容，长度限制为512字节。
     */
    public String dataId;
    /**
     * 图片审核的任务 ID，您可以通过该 ID 主动查询图片审核结果。
     */
    public String jobId;
    /**
     * 审核任务的状态
     */
    public String state;
    /**
     * 存储在 COS 桶中的图片名称，创建任务使用 ObjectKey 时返回。
     */
    public String object;
    /**
     * 图片文件的链接地址，创建任务使用 detect-url 时返回。
     */
    public String url;
    /**
     * 该字段表示本次判定的审核结果，您可以根据该结果，进行后续的操作；建议您按照业务所需，对不同的审核结果进行相应处理。
     * 有效值：0（审核正常），1 （判定为违规敏感文件），2（疑似敏感，建议人工复核）
     */
    public int result;
    /**
     * 该字段用于返回检测结果中所对应的优先级最高的恶意标签，表示模型推荐的审核结果，建议您按照业务所需，对不同违规类型与建议值进行处理。
     * 返回值：Normal 表示正常，Porn 表示色情，Ads 表示广告，以及其他不安全或不适宜的类型。
     */
    public String label;
    /**
     * 该字段为Label的子集，表示审核命中的具体审核类别。例如 Sexy，表示色情标签中的性感类别。
     */
    public String category;
    /**
     * 该图命中的二级标签结果
     */
    public String subLabel;
    /**
     * 该字段表示审核结果命中审核信息的置信度，取值范围：0（置信度最低）-100（置信度最高 ），越高代表该内容越有可能属于当前返回审核信息
     * 例如：色情 99，则表明该内容非常有可能属于色情内容
     */
    public int score;
    /**
     * 图里的文字内容（OCR），当审核策略开启文本内容检测时返回
     */
    public String text;
    /**
     * 该参数表示当前图片是否被压缩处理过，值为 0（未经过压缩处理），1（已经过压缩处理）。
     */
    public int compressionResult;
    /**
     * 审核场景为涉黄的审核结果信息
     */
    @XmlElement(name = "PornInfo")
    public PornInfo pornInfo;
    /**
     * 审核场景为政治敏感的审核结果信息
     */
    @XmlElement(name = "PoliticsInfo")
    public PoliticsInfo politicsInfo;
    /**
     * 审核场景为涉暴恐的审核结果信息
     */
    @XmlElement(name = "TerroristInfo")
    public TerroristInfo terroristInfo;
    /**
     * 审核场景为广告引导的审核结果信息
     */
    @XmlElement(name = "AdsInfo")
    public AdsInfo adsInfo;

    /**
     * 审核场景为低质量的审核结果信息。
     */
    @XmlElement(name = "QualityInfo")
    public QualityInfo qualityInfo;

    @XmlBean(name = "PornInfo", method = XmlBean.GenerateMethod.FROM)
    public static class PornInfo {

        @XmlElement(name = "Code")
        public int code;

        @XmlElement(name = "Msg")
        public String msg;

        @XmlElement(name = "HitFlag")
        public int hitFlag;

        @XmlElement(name = "Score")
        public int score;

        @XmlElement(name = "Label")
        public String label;

        @XmlElement(name = "Category")
        public String category;

        @XmlElement(name = "SubLabel")
        public String subLabel;

        @XmlElement(flatListNote = true)
        public List<AuditOcrResults> ocrResults;

        @XmlElement(flatListNote = true)
        public List<AuditLibResults> libResults;
    }
    @XmlBean(name = "PoliticsInfo", method = XmlBean.GenerateMethod.FROM)
    public static class PoliticsInfo {

        @XmlElement(name = "Code")
        public int code;

        @XmlElement(name = "Msg")
        public String msg;

        @XmlElement(name = "HitFlag")
        public int hitFlag;

        @XmlElement(name = "Score")
        public int score;

        @XmlElement(name = "Label")
        public String label;

        @XmlElement(name = "Category")
        public String category;

        @XmlElement(name = "SubLabel")
        public String subLabel;

        @XmlElement(flatListNote = true)
        public List<AuditOcrResults> ocrResults;

        @XmlElement(flatListNote = true)
        public List<AuditLibResults> libResults;

    }
    @XmlBean(name = "TerroristInfo", method = XmlBean.GenerateMethod.FROM)
    public static class TerroristInfo {

        @XmlElement(name = "Code")
        public int code;

        @XmlElement(name = "Msg")
        public String msg;

        @XmlElement(name = "HitFlag")
        public int hitFlag;

        @XmlElement(name = "Score")
        public int score;

        @XmlElement(name = "Label")
        public String label;

        @XmlElement(name = "Category")
        public String category;

        @XmlElement(name = "SubLabel")
        public String subLabel;

        @XmlElement(flatListNote = true)
        public List<AuditOcrResults> ocrResults;

        @XmlElement(flatListNote = true)
        public List<AuditLibResults> libResults;

    }
    @XmlBean(name = "AdsInfo", method = XmlBean.GenerateMethod.FROM)
    public static class AdsInfo {

        @XmlElement(name = "Code")
        public int code;

        @XmlElement(name = "Msg")
        public String msg;

        @XmlElement(name = "HitFlag")
        public int hitFlag;

        @XmlElement(name = "Score")
        public int score;

        @XmlElement(name = "Label")
        public String label;

        @XmlElement(name = "Category")
        public String category;

        @XmlElement(name = "SubLabel")
        public String subLabel;

        @XmlElement(flatListNote = true)
        public List<AuditOcrResults> ocrResults;

        @XmlElement(flatListNote = true)
        public List<AuditLibResults> libResults;
    }

    @XmlBean(name = "QualityInfo", method = XmlBean.GenerateMethod.FROM)
    public static class QualityInfo {
        @XmlElement(name = "Code")
        public int code;

        @XmlElement(name = "Msg")
        public String msg;

        @XmlElement(name = "HitFlag")
        public int hitFlag;

        @XmlElement(name = "Score")
        public int score;

        @XmlElement(name = "Label")
        public String label;

        @XmlElement(name = "Category")
        public String category;

        @XmlElement(name = "SubLabel")
        public String subLabel;

        @XmlElement(flatListNote = true)
        public List<AuditOcrResults> ocrResults;

        @XmlElement(flatListNote = true)
        public List<AuditLibResults> libResults;
    }
}

