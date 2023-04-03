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

package com.tencent.cos.xml.model.tag.audit.post;

import com.tencent.cos.xml.model.tag.audit.bean.AuditConf;
import com.tencent.cos.xml.model.tag.audit.bean.AuditInput;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

/**
 * 视频审核的具体配置项
 */
@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class PostVideoAudit {
    /**
     * 需要审核的内容
     */
    public AuditInput input;
    /**
     * 视频审核规则配置
     */
    public VideoAuditConf conf;

    public PostVideoAudit() {
        this.input = new AuditInput();
        this.conf = new VideoAuditConf();
    }

    /**
     * 视频审核规则配置
     */
    @XmlBean(name = "Conf", method = XmlBean.GenerateMethod.TO)
    public static class VideoAuditConf extends AuditConf {
        public VideoAuditConf() {
            this.snapshot = new Snapshot();
        }
        /**
         * 回调内容的结构，有效值：Simple（回调内容包含基本信息）、Detail（回调内容包含详细信息）。默认为 Simple
         */
        public String callbackVersion;
        /**
         * 视频画面的审核通过视频截帧能力截取出一定量的截图，通过对截图逐一审核而实现的，该参数用于指定视频截帧的配置
         */
        public Snapshot snapshot;
        /**
         * 用于指定是否审核视频声音，当值为0时：表示只审核视频画面截图；值为1时：表示同时审核视频画面截图和视频声音。默认值为0
         */
        public int detectContent;
    }

    /**
     * 视频截帧的配置
     */
    @XmlBean(method = XmlBean.GenerateMethod.TO)
    public static class Snapshot{
        /**
         * 截帧模式。Interval 表示间隔模式；Average 表示平均模式；Fps 表示固定帧率模式。
         * Interval 模式：TimeInterval，Count 参数生效。当设置 Count，未设置 TimeInterval 时，表示截取所有帧，共 Count 张图片
         * Average 模式：Count 参数生效。表示整个视频，按平均间隔截取共 Count 张图片
         * Fps 模式：TimeInterval 表示每秒截取多少帧，Count 表示共截取多少帧
         */
        public String mode;
        /**
         * 视频截帧数量，范围为(0, 10000]
         */
        public int count;
        /**
         * 视频截帧频率，范围为(0, 60]，单位为秒，支持 float 格式，执行精度精确到毫秒
         */
        @XmlElement(ignoreZero = true)
        public float timeInterval;
    }
}
