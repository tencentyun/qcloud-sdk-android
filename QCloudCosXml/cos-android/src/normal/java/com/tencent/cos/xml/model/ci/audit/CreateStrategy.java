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

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class CreateStrategy {
    /**
     *审核策略的名称，支持中文、英文、数字、下划线组合，不超过30个字符。名称不可取值为default，该值为系统保留值。
     */
    public String name;

    /**
     * 审核标签配置，如果不设置Labels代表不开启任何标签。Labels与TextLibs两者至少需要一个才能正常创建策略。
     */
    public Labels labels;

    /**
     * 需要关联的自定义文本库，可填多个，如果不设置TextLibs代表不关联任何文本库。
     */
    @XmlElement(ignoreListNote = true, flatListNote = true)
    public List<String> textLibs;

    @XmlBean(name = "Labels")
    public static class Labels {
        /**
         *图片内容的标签信息。该节点仅在Service的值为Image、Video、Document、Html时有效。
         */
        public LabelInfo image;

        /**
         *文本内容的标签信息。该节点仅在Service的值为Text、Html时有效。
         */
        public LabelInfo text;

        /**
         *音频内容的标签信息。该节点仅在Service的值为Audio、Video时有效。
         */
        public LabelInfo audio;
    }

    /**
     * image 的有效标签如下：
     *
     * 标签（英文）	标签（中文）	子标签（英文）	子标签（中文）
     * Porn	色情	ObsceneBehaviour	性暗示/低俗行为
     * Sexuality	性器官裸露/性行为
     * SexProducts	性用品相关
     * Sexy	性感内容
     * OcrText	OCR色情文本审核
     * Terrorism	暴恐	SpecialDress	特殊着装
     * Uniforms	军警制服
     * Firearms	枪支等热武器
     * LargeWeapons	大型军事武器
     * ColdWeapons	刀剑等冷兵器
     * CrowdGathering	人群聚集
     * BloodyScenes	血腥场景
     * FireExplosion	火灾/爆炸场景
     * TerrorismActs	暴力恐怖行为
     * Delinquenent	不良行为
     * Illegal	违法违规
     * Contraband	违禁品
     * NonMainstream	非主流文化
     * OcrText	OCR暴恐文本审核
     * Politics	政治	PositiveCharacter	正面人物
     * NegativeFigure	负面人物
     * ForeignLeaders	外国/地区领导人
     * Underdog	劣迹艺人
     * Gallery	违规图库
     * PositiveFlagsLogos	正面旗帜&标识
     * NegativeFlagsLogos	负面旗帜&标识
     * ChinaMap	中国地图
     * Building	涉政建筑
     * SpecialItems	特殊物品
     * OcrText	OCR政治文本审核
     * Ads	广告	QRCode	二维码/条形码识别
     * LOGO	LOGO检测识别
     * OcrText	OCR广告文本审核
     *
     * Text 的有效标签如下：
     *
     * 标签（英文）	标签（中文）	子标签（英文）	子标签（中文）
     * Porn	色情	Pornography	严重色情
     * PornographyObscene	色情低俗
     * Terrorism	暴恐	SeriousTerrorism	严重暴恐行为
     * TerrorismObjects	暴恐相关行为与物品
     * Politics	政治	NegativeContent	负面内容
     * PositiveContent	正面内容
     * Ads	广告	AdvertisingPractices	广告行为相关
     * Illegal	违法	Illegal	违法违规内容
     * Abuse	谩骂	MildAbuse	轻度谩骂
     * AbuseSeverely	严重谩骂
     *
     * Audio 的有效标签如下：
     *
     * 标签（英文）	标签（中文）	子标签（英文）	子标签（中文）
     * Porn	色情	Pornography	严重色情
     * PornographyObscene	色情低俗
     * Moan	低俗语音识别
     * Terrorism	暴恐	SeriousTerrorism	严重暴恐行为
     * TerrorismObjects	暴恐相关行为与物品
     * Illegal	违法内容
     * Abuse	谩骂内容
     * Politics	政治	NegativeContent	负面内容
     * PositiveContent	正面内容
     * Ads	广告	AdvertisingPractices	广告行为相关
     */
    @XmlBean
    public static class LabelInfo {
        /**
         *色情一级标签，该节点的值表示开启检测的标签细类，可填多个，具体值及对应描述可见下方表格。
         */
        @XmlElement(ignoreListNote = true, flatListNote = true)
        public List<String> porn;

        /**
         *涉政一级标签，该节点的值表示开启检测的标签细类，可填多个，具体值及对应描述可见下方表格。
         */
        @XmlElement(ignoreListNote = true, flatListNote = true)
        public List<String> politics;

        /**
         *暴恐一级标签，该节点的值表示开启检测的标签细类，可填多个，具体值及对应描述可见下方表格。
         */
        @XmlElement(ignoreListNote = true, flatListNote = true)
        public List<String> terrorism;

        /**
         *广告一级标签，该节点的值表示开启检测的标签细类，可填多个，具体值及对应描述可见下方表格。
         */
        @XmlElement(ignoreListNote = true, flatListNote = true)
        public List<String> ads;

        /**
         *谩骂一级标签，该节点的值表示开启检测的标签细类，可填多个，具体值及对应描述可见下方表格。
         */
        @XmlElement(ignoreListNote = true, flatListNote = true)
        public List<String> abuse;

        /**
         *违法一级标签，该节点的值表示开启检测的标签细类，可填多个，具体值及对应描述可见下方表格。
         */
        @XmlElement(ignoreListNote = true, flatListNote = true)
        public List<String> illegal;
    }
}
