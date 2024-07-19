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

package com.tencent.cos.xml.model.ci.ai;

import com.tencent.cos.xml.model.ci.common.VideoTargetRec;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class PostVideoTargetRecResponse {
    
    /**
     * 任务的详细信息
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<PostVideoTargetRecResponseJobsDetail> jobsDetail;
    @XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
    public static class PostVideoTargetRecResponseJobsDetail {
        /**
         * 错误码，只有 State 为 Failed 时有意义
         */
        public String code;
        /**
         * 错误描述，只有 State 为 Failed 时有意义
         */
        public String message;
        /**
         * 新创建任务的 ID
         */
        public String jobId;
        /**
         * 新创建任务的 Tag：VideoTargetRec
         */
        public String tag;
        /**
         * 任务状态Submitted：已提交，待执行Running：执行中Success：执行成功Failed：执行失败Pause：任务暂停，当暂停队列时，待执行的任务会变为暂停状态Cancel：任务被取消执行
         */
        public String state;
        /**
         * 任务的创建时间
         */
        public String creationTime;
        /**
         * 任务的开始时间
         */
        public String startTime;
        /**
         * 任务的结束时间
         */
        public String endTime;
        /**
         * 任务所属的队列 ID
         */
        public String queueId;
        /**
         * 该任务的规则
         */
        public PostVideoTargetRecResponseOperation operation;
    }
    @XmlBean(name = "Operation", method = XmlBean.GenerateMethod.FROM)
    public static class PostVideoTargetRecResponseOperation {
        /**
         * 任务的模板 ID
         */
        public String templateId;
        /**
         * 任务的模板名称, 当 TemplateId 存在时返回
         */
        public String templateName;
        /**
         * 同请求中的 Request.Operation.VideoTargetRec
         */
        public VideoTargetRec videoTargetRec;
        /**
         * 视频目标检测结果
         */
        public PostVideoTargetRecResponseVideoTargetRecResult videoTargetRecResult;
        /**
         * 透传用户信息
         */
        public String userData;
        /**
         * 任务优先级
         */
        public String jobLevel;
    }
    @XmlBean(name = "VideoTargetRecResult", method = XmlBean.GenerateMethod.FROM)
    public static class PostVideoTargetRecResponseVideoTargetRecResult {
        /**
         * 人体识别结果
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<PostVideoTargetRecResponseBodyRecognition> bodyRecognition;
        /**
         * 宠物识别结果
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<PostVideoTargetRecResponsePetRecognition> petRecognition;
        /**
         * 车辆识别结果
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<PostVideoTargetRecResponseCarRecognition> carRecognition;
    }
    @XmlBean(name = "BodyRecognition", method = XmlBean.GenerateMethod.FROM)
    public static class PostVideoTargetRecResponseBodyRecognition {
        /**
         * 截图的时间点，单位为秒
         */
        public String time;
        /**
         * 截图 URL
         */
        public String url;
        /**
         * 人体识别结果，可能有多个
         */
        public PostVideoTargetRecResponseBodyInfo bodyInfo;
    }
    @XmlBean(name = "BodyInfo", method = XmlBean.GenerateMethod.FROM)
    public static class PostVideoTargetRecResponseBodyInfo {
        /**
         * 识别类型
         */
        public String name;
        /**
         * 识别的置信度，取值范围为[0-100]。值越高概率越大。
         */
        public int score;
        /**
         * 图中识别到人体的坐标
         */
        public PostVideoTargetRecResponseLocation location;
    }
    @XmlBean(name = "Location", method = XmlBean.GenerateMethod.FROM)
    public static class PostVideoTargetRecResponseLocation {
        /**
         * X坐标
         */
        public String x;
        /**
         * Y坐标
         */
        public String y;
        /**
         * (X,Y)坐标距离高度
         */
        public String height;
        /**
         * (X,Y)坐标距离长度
         */
        public String width;
    }
    @XmlBean(name = "CarRecognition", method = XmlBean.GenerateMethod.FROM)
    public static class PostVideoTargetRecResponseCarRecognition {
        /**
         * 截图的时间点，单位为秒
         */
        public String time;
        /**
         * 截图 URL
         */
        public String url;
        /**
         * 车辆识别结果，可能有多个
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<PostVideoTargetRecResponseCarInfo> carInfo;
    }
    @XmlBean(name = "CarInfo", method = XmlBean.GenerateMethod.FROM)
    public static class PostVideoTargetRecResponseCarInfo {
        /**
         * 识别类型
         */
        public String name;
        /**
         * 识别的置信度，取值范围为[0-100]。值越高概率越大。
         */
        public int score;
        /**
         * 图中识别到车辆的坐标
         */
        public PostVideoTargetRecResponseLocation location;
    }
    @XmlBean(name = "PetRecognition", method = XmlBean.GenerateMethod.FROM)
    public static class PostVideoTargetRecResponsePetRecognition {
        /**
         * 截图的时间点，单位为秒
         */
        public String time;
        /**
         * 截图 URL
         */
        public String url;
        /**
         * 宠物识别结果结果，可能有多个
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<PostVideoTargetRecResponsePetInfo> petInfo;
    }
    @XmlBean(name = "PetInfo", method = XmlBean.GenerateMethod.FROM)
    public static class PostVideoTargetRecResponsePetInfo {
        /**
         * 识别类型
         */
        public String name;
        /**
         * 识别的置信度，取值范围为[0-100]。值越高概率越大。
         */
        public int score;
        /**
         * 图中识别到宠物的坐标
         */
        public PostVideoTargetRecResponseLocation location;
    }

}
