package com.tencent.cos.xml.model.ci.common;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * <p>
 * Created by jordanqin on 2023/6/29 11:37.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@XmlBean
public class AudioMix {
    /**
     * 需要被混音的音轨媒体地址, 需要做 URLEncode
     * 需与 Input 媒体文件存储于同一 bucket
     */
    public String audioSource;
    /**
     * 混音模式
     * Repeat: 混音循环
     * Once: 混音一次播放
     */
    public String mixMode;
    /**
     * 是否用混音音轨媒体替换Input媒体文件的原音频
     * true/false
     */
    public String replace;
    /**
     * 混音淡入淡出配置
     */
    public EffectConfig effectConfig;

    /**
     * 混音淡入淡出配置
     */
    @XmlBean
    public static class EffectConfig {
        /**
         * 开启淡入
         * true/false
         */
        public String enableStartFadein;
        /**
         * 淡入时长
         * 大于0, 支持浮点数
         */
        public String startFadeinTime;
        /**
         * 开启淡出
         * true/false
         */
        public String enableEndFadeout;
        /**
         * 淡出时长
         * 大于0, 支持浮点数
         */
        public String endFadeoutTime;
        /**
         * 开启 bgm 转换淡入
         * true/false
         */
        public String enableBgmFade;
        /**
         * bgm 转换淡入时长
         * 大于0, 支持浮点数
         */
        public String bgmFadeTime;
    }
}
