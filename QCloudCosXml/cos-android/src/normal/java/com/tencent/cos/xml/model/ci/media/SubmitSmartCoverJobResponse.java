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

import com.tencent.cos.xml.model.ci.common.MediaResult;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class SubmitSmartCoverJobResponse {
    
    /**
     *任务的详细信息
     */
    public SubmitSmartCoverJobResponseJobsDetail jobsDetail;

    @XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitSmartCoverJobResponseJobsDetail {
        /**
         *错误码，只有 State为 Failed 时有意义
         */
        public String code;

        /**
         *错误描述，只有 State 为 Failed 时有意义
         */
        public String message;

        /**
         *新创建任务的 ID
         */
        public String jobId;

        /**
         *新创建任务的 Tag：SmartCover
         */
        public String tag;

        /**
         *任务的状态，为 Submitted、Running、Success、Failed、Pause、Cancel 其中一个
         */
        public String state;

        /**
         *任务的创建时间
         */
        public String creationTime;

        /**
         *任务的结束时间
         */
        public String endTime;

        /**
         *任务所属的队列 ID
         */
        public String queueId;

        /**
         *该任务的输入资源地址
         */
        public SubmitSmartCoverJobResponseInput input;

        /**
         *该任务的规则
         */
        public SubmitSmartCoverJobResponseOperation operation;

    }

    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitSmartCoverJobResponseInput {
        /**
         *存储桶的地域
         */
        public String region;

        /**
         *存储结果的存储桶
         */
        public String bucketId;

        /**
         *输出结果的文件名
         */
        public String object;

    }

    @XmlBean(name = "Operation", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitSmartCoverJobResponseOperation {
        /**
         *同请求中的 Request.Operation.SmartCover
         */
        public String smartCover;

        /**
         *同请求中的 Request.Operation.Output
         */
        public SubmitSmartCoverJob.SubmitSmartCoverJobOutput output;

        /**
         *输出文件的基本信息，任务未完成时不返回
         */
        public MediaResult mediaResult;

        /**
         *透传用户信息
         */
        public String userData;

        /**
         *任务优先级
         */
        public String jobLevel;

    }


}
