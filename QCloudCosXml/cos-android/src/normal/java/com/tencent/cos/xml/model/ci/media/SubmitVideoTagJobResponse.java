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
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class SubmitVideoTagJobResponse {
    
    /**
     *任务的详细信息
     */
    public SubmitVideoTagJobResponseJobsDetail jobsDetail;

    @XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseJobsDetail {
        /**
         *错误码，只有 State 为 Failed 时有意义
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
         *新创建任务的 Tag：VideoTag
         */
        public String tag;

        /**
         *任务的状态，为 Submitted、Running、Success、Failed、Pause、Cancel 其中一��
         */
        public String state;

        /**
         *任务的创建时间
         */
        public String creationTime;

        /**
         *任务的开始时间
         */
        public String startTime;

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
        public SubmitVideoTagJobResponseInput input;

        /**
         *该任务的规则
         */
        public SubmitVideoTagJobResponseOperation operation;

    }

    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseInput {
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
    public static class SubmitVideoTagJobResponseOperation {
        /**
         *同请求中的 Request.Operation.VideoTag
         */
        public OperationVideoTag videoTag;

        /**
         *视频标签分析结果,任务未完成时不返回
         */
        public SubmitVideoTagJobResponseVideoTagResult videoTagResult;

        /**
         *透传用户信息
         */
        public String userData;

        /**
         *任务优先级
         */
        public String jobLevel;

    }

    @XmlBean(name = "VideoTagResult", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseVideoTagResult {
        /**
         *Stream 场景下视频标签任务的结果
         */
        public SubmitVideoTagJobResponseStreamData streamData;

    }

    @XmlBean(name = "StreamData", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseStreamData {
        /**
         *Stream 场景视频标签任务的结果列表
         */
        public SubmitVideoTagJobResponseData data;

        /**
         *算法状态码，成功为0，非0异常
         */
        public String subErrCode;

        /**
         *算法错误描述，成功为 ok , 不成功返回对应错误
         */
        public String subErrMsg;

    }

    @XmlBean(name = "Data", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseData {
        /**
         *视频标签、视频分类信息
         */
        public SubmitVideoTagJobResponseTags tags;

        /**
         *人物标签信息
         */
        @XmlElement(flatListNote = true)
        public List<SubmitVideoTagJobResponsePersonTags> personTags;

        /**
         *场景标签信息
         */
        @XmlElement(flatListNote = true)
        public List<SubmitVideoTagJobResponsePlaceTags> placeTags;

        /**
         *动作标签信息
         */
        @XmlElement(flatListNote = true)
        public List<SubmitVideoTagJobResponseActionTags> actionTags;

        /**
         *物体标签信息
         */
        @XmlElement(flatListNote = true)
        public List<SubmitVideoTagJobResponseObjectTags> objectTags;

    }

    @XmlBean(name = "Tags", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseTags {
        /**
         *标签名称
         */
        public String tag;

        /**
         *标签分类名称
         */
        public String tagCls;

        /**
         *标签模型预测分数，取值范围[0,1]
         */
        public float confidence;

    }

    @XmlBean(name = "PlaceTags", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponsePlaceTags {
        /**
         *视频场景标签信息，可能不返回，内容同 Response.JobsDetail.Operation.VideoTagResult.StreamData.Data.Tags
         */
        @XmlElement(flatListNote = true)
        public List<SubmitVideoTagJobResponseTags> tags;

        /**
         *片段起始时间，单位为秒
         */
        public String startTime;

        /**
         *片段结束时间，单位为秒
         */
        public String endTime;

        /**
         *片段起始帧数
         */
        public String startIndex;

        /**
         *片段结束帧数
         */
        public String endIndex;

        /**
         *单帧识别结果top1
         */
        public String clipFrameResult;

    }

    @XmlBean(name = "PersonTags", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponsePersonTags {
        /**
         *人物名字
         */
        public String name;

        /**
         *标签模型预测分数
         */
        public float confidence;

        /**
         *任务出现频次
         */
        public String count;

        /**
         *具体识别到人物出现的位置和时间信息
         */
        @XmlElement(flatListNote = true)
        public List<SubmitVideoTagJobResponseDetailPerSecond> detailPerSecond;

    }

    @XmlBean(name = "ActionTags", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseActionTags {
        /**
         *视频动作标签信息，可能不返回，内容同 Response.JobsDetail.Operation.VideoTagResult.StreamData.Data.Tags
         */
        @XmlElement(flatListNote = true)
        public List<SubmitVideoTagJobResponseTags> tags;

        /**
         *片段起始时间，单位为秒
         */
        public String startTime;

        /**
         *片段结束时间，单位为秒
         */
        public String endTime;

    }

    @XmlBean(name = "ObjectTags", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseObjectTags {
        /**
         *视频物体标签信息，可能不返回，内容同 Response.JobsDetail.Operation.VideoTagResult.StreamData.Data.Tags
         */
        @XmlElement(flatListNote = true)
        public List<SubmitVideoTagJobResponseObjects> objects;

        /**
         *识别物体时间戳，单位为秒
         */
        public String timeStamp;

    }

    @XmlBean(name = "Objects", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseObjects {
        /**
         *物体名称
         */
        public String name;

        /**
         *标签模型预测分数，取值范围[0,1]
         */
        public float confidence;

        /**
         *左上角为原点，物体的相对坐标, 内容同 Response.JobsDetail.Operation.VideoTagResult.StreamData.Data.PersonTags.DetailPerSecond.BBox
         */
        @XmlElement(flatListNote = true)
        public List<SubmitVideoTagJobResponseBBox> bBox;

    }

    @XmlBean(name = "DetailPerSecond", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseDetailPerSecond {
        /**
         *出现时间，单位为秒
         */
        public String timeStamp;

        /**
         *标签模型预测分数
         */
        public float confidence;

        /**
         *左上角为原点，物体的相对坐标
         */
        @XmlElement(flatListNote = true)
        public List<SubmitVideoTagJobResponseBBox> bBox;

    }

    @XmlBean(name = "BBox", method = XmlBean.GenerateMethod.FROM)
    public static class SubmitVideoTagJobResponseBBox {
        /**
         *坐标X1的相对位置
         */
        public String x1;

        /**
         *坐标Y1的相对位置
         */
        public String y1;

        /**
         *坐标X2的相对位置
         */
        public String x2;

        /**
         *坐标Y2的相对位置
         */
        public String y2;

    }


}
