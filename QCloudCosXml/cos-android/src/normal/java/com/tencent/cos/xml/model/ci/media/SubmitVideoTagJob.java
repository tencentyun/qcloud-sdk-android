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

import com.tencent.cos.xml.model.tag.CallBackMqConfig;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class SubmitVideoTagJob {

    /**
     *创建任务的 Tag：VideoTag;是否必传：是;
     */
    public String tag = "VideoTag";

    /**
     *待操作的文件信息;是否必传：是;
     */
    public SubmitVideoTagJobInput input;

    /**
     *操作规则;是否必传：是;
     */
    public SubmitVideoTagJobOperation operation;

    /**
     *任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否;
     */
    public String callBackFormat;

    /**
     *任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否;
     */
    public String callBackType;

    /**
     *任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否;
     */
    public String callBack;

    /**
     *任务回调 TDMQ 配置，当 CallBackType 为 TDMQ 时必填。详情见 CallBackMqConfig;是否必传：否;
     */
    public CallBackMqConfig callBackMqConfig;

    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.TO)
    public static class SubmitVideoTagJobInput {
        /**
         *执行视频标签任务的文件路径，目前支持 mp4、avi、mkv、wmv、rmvb、flv、mov 封装格式，视频时长超过30min的视频请 提交工单 处理;是否必传：是;
         */
        public String object;

    }

    @XmlBean(name = "Operation", method = XmlBean.GenerateMethod.TO)
    public static class SubmitVideoTagJobOperation {
        /**
         *任务参数;是否必传：是;
         */
        public OperationVideoTag videoTag;

        /**
         *任务优先级，级别限制：0 、1 、2。级别越大任务优先级越高，默认为0;是否必传：否;
         */
        public String jobLevel;

        /**
         *透传用户信息;是否必传：否;
         */
        public String userData;
    }
}
