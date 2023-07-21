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
public class GetWorkflowDetailResponse {
    
    /**
     *请求 ID
     */
    public String requestId;

    /**
     *工作流实例详细信息
     */
    public GetWorkflowDetailResponseWorkflowExecution workflowExecution;

    @XmlBean(name = "WorkflowExecution", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseWorkflowExecution {
        /**
         *工作流 ID
         */
        public String workflowId;

        /**
         *工作流名称
         */
        public String workflowName;

        /**
         *工作流实例 ID
         */
        public String runId;

        /**
         *创建时间
         */
        public String createTime;

        /**
         *COS 对象地址
         */
        public String object;

        /**
         *工作流实例状态
         */
        public String state;

        /**
         *同创建工作流接口的 Topology
         * 由于结构比较复杂，且类型字段不固定，因此此处返回原始的xml字符串
         */
        // TODO: 2023/7/20 待originalXmlString支持
//        @XmlElement(originalXmlString = true)
//        public String topology;

        /**
         *工作流实例任务
         */
        @XmlElement(flatListNote = true)
        public List<GetWorkflowDetailResponseTasks> tasks;

    }

    @XmlBean(name = "Tasks", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseTasks {
        /**
         *任务所属节点类型
         */
        public String type;

        /**
         *任务状态，当该节点为判断节点时，PartialSuccess 表示部分输入文件通过判断
         */
        public String state;

        /**
         *任务 ID
         */
        public String jobId;

        /**
         *任务创建时间
         */
        public String createTime;

        /**
         *任务开始时间
         */
        public String startTime;

        /**
         *任务结束时间
         */
        public String endTime;

        /**
         *任务的错误码
         */
        public String code;

        /**
         *任务的错误信息
         */
        public String message;

        /**
         *工作流节点name
         */
        public String name;

        /**
         *任务结果详情
         */
        public GetWorkflowDetailResponseResultInfo resultInfo;

        /**
         *判断节点结果详情，当节点为判断节点时返回
         */
        public GetWorkflowDetailResponseJudgementInfo judgementInfo;

        /**
         *判断节点结果详情，当节点为判断节点时返回
         */
        @XmlElement(flatListNote = true)
        public List<GetWorkflowDetailResponseFileInfo> fileInfo;

    }

    @XmlBean(name = "FileInfo", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseFileInfo {
        /**
         *文件基础信息
         */
        public GetWorkflowDetailResponseBasicInfo basicInfo;

        /**
         *音视频信息
         */
        public GetWorkflowDetailResponseMediaInfo mediaInfo;

        /**
         *图片信息
         */
        public GetWorkflowDetailResponseImageInfo imageInfo;

    }

    @XmlBean(name = "ResultInfo", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseResultInfo {
        /**
         *任务生成对象文件个数
         */
        public String objectCount;

        /**
         *仅在截图生成雪碧图有效，表示生成雪碧图个数
         */
        public String spriteObjectCount;

        /**
         *任务生成对象详情
         */
        @XmlElement(flatListNote = true)
        public List<GetWorkflowDetailResponseObjectInfo> objectInfo;

        /**
         *任务生成雪碧图对象详情
         */
        @XmlElement(flatListNote = true)
        public List<GetWorkflowDetailResponseSpriteObjectInfo> spriteObjectInfo;

    }

    @XmlBean(name = "JudgementInfo", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseJudgementInfo {
        /**
         *判断节点输入对象文件个数
         */
        public int objectCount;

        /**
         *判断节点输入对象具体判断结果
         */
        @XmlElement(flatListNote = true)
        public List<GetWorkflowDetailResponseJudgementResult> judgementResult;

    }

    @XmlBean(name = "BasicInfo", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseBasicInfo {
        /**
         *文件类型
         */
        public String contentType;

        /**
         *文件大小
         */
        public String size;

        /**
         *文件Etag
         */
        public String eTag;

        /**
         *文件上修改时间
         */
        public String lastModified;

        /**
         *文件名
         */
        public String object;

    }

    @XmlBean(name = "MediaInfo", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseMediaInfo {
        /**
         *视频信息
         */
        public GetWorkflowDetailResponseVideo video;

        /**
         *音频信息
         */
        public GetWorkflowDetailResponseAudio audio;

        /**
         *格式信息
         */
        public GetWorkflowDetailResponseFormat format;

    }

    @XmlBean(name = "ImageInfo", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseImageInfo {
        /**
         *图片高
         */
        public String format;

        /**
         *图片宽
         */
        public String height;

        /**
         *图片宽
         */
        public String width;

        /**
         *图片Md5
         */
        public String md5;

        /**
         *图片帧数，静态图为1，动图为对应的帧数
         */
        public String frameCount;

    }

    @XmlBean(name = "ObjectInfo", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseObjectInfo {
        /**
         *对象名
         */
        public String objectName;

        /**
         *对象Url
         */
        public String objectUrl;

        /**
         *输入对象名
         */
        public String inputObjectName;

        /**
         *InputObjectName对应错误码，O为处理成功，非0处理失败
         */
        public String code;

        /**
         *InputObjectName对应错误信息，处理失败时有效
         */
        public String message;

    }

    @XmlBean(name = "Video", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseVideo {
        /**
         *视频高
         */
        public String height;

        /**
         *视频宽
         */
        public String width;

        /**
         *视频宽高比
         */
        public String dar;

        /**
         *视频时长，单位秒
         */
        public String duration;

    }

    @XmlBean(name = "Audio", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseAudio {
        /**
         *音频时长，单位秒
         */
        public String duration;

    }

    @XmlBean(name = "Format", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseFormat {
        /**
         *时长，单位秒
         */
        public String duration;

    }

    @XmlBean(name = "SpriteObjectInfo", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseSpriteObjectInfo {
        /**
         *对象名
         */
        public String objectName;

        /**
         *对象Url
         */
        public String objectUrl;

    }

    @XmlBean(name = "JudgementResult", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseJudgementResult {
        /**
         *对象名
         */
        public String objectName;

        /**
         *对象链接
         */
        public String objectUrl;

        /**
         *判断结果状态，Success 表示通过条件判断，Failed 表示未通过条件判断
         */
        public String state;

        /**
         *输入文件的判断参数
         */
        public GetWorkflowDetailResponseInputObjectInfo inputObjectInfo;

    }

    @XmlBean(name = "InputObjectInfo", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowDetailResponseInputObjectInfo {
        /**
         *视频宽
         */
        public String width;

        /**
         *视频高
         */
        public String height;

        /**
         *视频宽高比
         */
        public String dar;

        /**
         *音视频时长
         */
        public String duration;

        /**
         *文件大小
         */
        public String size;

        /**
         *图片宽
         */
        public String imageWidth;

        /**
         *图片高
         */
        public String imageHeight;

        /**
         *图片宽高比
         */
        public String imageDar;

    }


}
