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

package com.tencent.cos.xml.common_interface;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class PostVirusDetectResponse {
    
    /**
     *病毒检测任务任务的详细信息。
     */
    public PostVirusDetectResponseJobsDetail jobsDetail;

    @XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
    public static class PostVirusDetectResponseJobsDetail {
        /**
         *本次病毒检测任务的 ID。
         */
        public String jobId;

        /**
         *病毒检测任务的状态，值为 Submitted（已提交检测）、Success（检测成功）、Failed（检测失败）、Auditing（检测中）其中一个。
         */
        public String state;

        /**
         *病毒检测任务的创建时间。
         */
        public String creationTime;

        /**
         *本次检测的文件名称。
         */
        public String object;

    }


}
