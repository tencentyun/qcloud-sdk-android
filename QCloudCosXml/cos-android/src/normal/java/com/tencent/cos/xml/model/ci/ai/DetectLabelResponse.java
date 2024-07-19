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

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class DetectLabelResponse {
    
    /**
     * Web网络版标签结果数组。如未选择web场景，则不存在该项。此字段可能为空，表示取不到有效值
     */
    public DetectLabelResponseLabels webLabels;
    /**
     * Camera摄像头版标签结果数组。如未选择camera场景，则不存在该项。此字段可能为空，表示取不到有效值
     */
    public DetectLabelResponseLabels cameraLabels;
    /**
     * Album相册版标签结果数组。如未选择album场景，则不存在该项。此字段可能为空，表示取不到有效值
     */
    public DetectLabelResponseLabels albumLabels;
    /**
     * News新闻版标签结果数组。如未选择news场景，则不存在该项。此字段可能为空，表示取不到有效值
     */
    public DetectLabelResponseLabels newsLabels;
    @XmlBean(name = "Labels", method = XmlBean.GenerateMethod.FROM)
    public static class DetectLabelResponseLabels {
        /**
         * 标签信息
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<DetectLabelResponseLabelsItem> labels;
    }
    @XmlBean(name = "undefined", method = XmlBean.GenerateMethod.FROM)
    public static class DetectLabelResponseLabelsItem {
        /**
         * 该标签的置信度分数，分数越高则该标签准确度越高
         */
        public int confidence;
        /**
         * 识别出的图片标签
         */
        public String name;
        /**
         * 标签的一级分类
         */
        public String firstCategory;
        /**
         * 标签的二级分类
         */
        public String secondCategory;
    }

}
