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
public class AIDetectCarResponse {
    
    /**
     * 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     */
    public String requestId;
    /**
     * 车辆属性识别的结果数组，支持返回多个车辆信息
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<AIDetectCarResponseCarTags> carTags;
    @XmlBean(name = "CarTags", method = XmlBean.GenerateMethod.FROM)
    public static class AIDetectCarResponseCarTags {
        /**
         * 车系
         */
        public String serial;
        /**
         * 车辆品牌
         */
        public String brand;
        /**
         * 车辆类型
         */
        public String type;
        /**
         * 车辆颜色
         */
        public String color;
        /**
         * 置信度，0 - 100
         */
        public int confidence;
        /**
         * 年份，识别不出年份时返回0
         */
        public int year;
        /**
         * 车辆在图片中的坐标信息，可能返回多个坐标点的值
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<AIDetectCarResponseCarLocation> carLocation;
        /**
         * 车牌信息，包含车牌号、车牌颜色、车牌位置。支持返回多个车牌
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<AIDetectCarResponsePlateContent> plateContent;
    }
    @XmlBean(name = "CarLocation", method = XmlBean.GenerateMethod.FROM)
    public static class AIDetectCarResponseCarLocation {
        /**
         * 横坐标 x
         */
        public int x;
        /**
         * 纵坐标 y
         */
        public int y;
    }
    @XmlBean(name = "PlateContent", method = XmlBean.GenerateMethod.FROM)
    public static class AIDetectCarResponsePlateContent {
        /**
         * 车牌号信息
         */
        public String plate;
        /**
         * 车牌的颜色
         */
        public String color;
        /**
         * 车牌的种类，例如普通蓝牌
         */
        public String type;
        /**
         * 车牌的位置
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<AIDetectCarResponsePlateLocation> plateLocation;
    }
    @XmlBean(name = "PlateLocation", method = XmlBean.GenerateMethod.FROM)
    public static class AIDetectCarResponsePlateLocation {
        /**
         * 定位出的车牌左上角、右上角、左下角、右下角的 X 坐标
         */
        public int x;
        /**
         * 定位出的车牌左上角、右上角、左下角、右下角的 Y 坐标
         */
        public int y;
    }

}
