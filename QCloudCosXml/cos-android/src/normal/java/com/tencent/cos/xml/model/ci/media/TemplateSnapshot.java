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

package com.tencent.cos.xml.model.ci.media;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class TemplateSnapshot {

    /**
     *模板类型：Snapshot;是否必传：是;
     */
    public String tag = "Snapshot";

    /**
     *模板名称仅支持中文、英文、数字、_、-和*;是否必传：是;
     */
    public String name;

    /**
     *截图;是否必传：是;
     */
    public TemplateSnapshotSnapshot snapshot;

    @XmlBean(name = "Snapshot")
    public static class TemplateSnapshotSnapshot {
        /**
         *截图模式;是否必传：否;默认值：Interval;限制：值范围：{Interval, Average, KeyFrame}Interval 表示间隔模式 Average 表示平均模式 KeyFrame 表示关键帧��式 Interval模式：Start，TimeInterval，Count参数生效。当设置Count，未设置TimeInterval时，表示截取所有帧，共Count张图片Average模式：Start，Count参数生效。表示从Start开始到视频结束，按平均间隔截取共Count张图片;
         */
        public String mode;

        /**
         *开始时间;是否必传：否;默认值：0;限制：[0 视频时长] 单位为秒 支持 float 格式，执行精度精确到毫秒;
         */
        public String start;

        /**
         *截图时间间隔;是否必传：否;默认值：无;限制：(0 3600] 单位为秒 支持 float 格式，执行精度精确到毫秒;
         */
        public String timeInterval;

        /**
         *截图数量;是否必传：是;默认值：无;限制：(0 10000];
         */
        public String count;

        /**
         *宽;是否必传：否;默认值：视频原始宽度;限制：值范围：[128，4096]单位：px若只设置 Width 时，按照视频原始比例计算 Height;
         */
        public String width;

        /**
         *高;是否必传：否;默认值：视频原始高度;限制：值范围：[128，4096]单位：px若只设置 Height 时，按照视频原始比例计算 Width;
         */
        public String height;

        /**
         *截图图片处理参数;是否必传：否;默认值：无;限制：参考 图片处理 例如: imageMogr2/format/png;
         */
        public String cIParam;

        /**
         *是否强制检查截图个数;是否必传：否;默认值：false;限制：使用自定义间隔模式截图时，视频时长不够截取 Count 个截图，可以转为平均截图模式截取 Count 个截图;
         */
        public String isCheckCount;

        /**
         *是否开启黑屏检测;是否必传：否;默认值：false;限制：true/false;
         */
        public String isCheckBlack;

        /**
         *截图黑屏检测参数;是否必传：否;默认值：空;限制：当 IsCheckBlack=true 时有效值参考范围[30，100]，表示黑色像素的占比值，值越小，黑色占比越小Start>0，参数设置无效，不做过滤黑屏Start =0 参数有效，截帧的开始时间为第一帧非黑屏开始;
         */
        public String blackLevel;

        /**
         *截图黑屏检测参数;是否必传：否;默认值：空;限制：当IsCheckBlack=true时有效判断像素点是否为黑色点的阈值，取值范围：[0，255];
         */
        public String pixelBlackThreshold;

        /**
         *截图输出模式参数;是否必传：否;默认值：OnlySnapshot;限制：值范围：{OnlySnapshot, OnlySprite, SnapshotAndSprite}OnlySnapshot 表示仅输出截图模式 OnlySprite 表示仅输出雪碧图模式 SnapshotAndSprite 表示输出截图与雪碧图模式;
         */
        public String snapshotOutMode;

        /**
         *雪碧图输出配置;是否必传：否;默认值：无;限制：无;
         */
        public TemplateSnapshotSpriteSnapshotConfig spriteSnapshotConfig;

    }

    @XmlBean(name = "SpriteSnapshotConfig")
    public static class TemplateSnapshotSpriteSnapshotConfig {
        /**
         *单图宽度;是否必传：否;默认值：截图宽度;限制：值范围：[8，4096]单位：px;
         */
        public String cellWidth;

        /**
         *单图高度;是否必传：否;默认值：截图高度;限制：值范围：[8，4096]单位：px;
         */
        public String cellHeight;

        /**
         *雪碧图内边距大小;是否必传：否;默认值：0;限制：值范围：[0，1024]单位：px;
         */
        public String padding;

        /**
         *雪碧图外边距大小;是否必传：否;默认值：0;限制：值范围：[0，1024]单位：px;
         */
        public String margin;

        /**
         *背景颜色;是否必传：是;默认值：无;限制：支持颜色详见 FFmpeg;
         */
        public String color;

        /**
         *雪碧图列数;是否必传：是;默认值：0;限制：值范围：[1，10000];
         */
        public String columns;

        /**
         *雪碧图行数;是否必传：是;默认值：0;限制：值范围：[1，10000];
         */
        public String lines;

        /**
         *雪碧图缩放模式;是否必传：否;默认值：DirectScale;限制：DirectScale: 指定宽高缩放MaxWHScaleAndPad: 指定最大宽高缩放填充MaxWHScale: 指定最大宽高缩放主动设置CellWidth和CellHeight时生效;
         */
        public String scaleMethod;

    }


   
}
