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
public class COSOCRResponse {
    
    /**
     * 检测到的文本信息，包括文本行内容、置信度、文本行坐标以及文本行旋转纠正后的坐标。
     */
    public COSOCRResponseTextDetections textDetections;
    /**
     * 检测到的语言类型，目前支持的语言类型参考入参 language-type 说明。
     */
    public String language;
    /**
     * 图片旋转角度（角度制），文本的水平方向为0°；顺时针为正，逆时针为负。
     */
    public float angel;
    /**
     * 图片为PDF时，返回PDF的总页数，默认为0。
     */
    public int pdfPageSize;
    /**
     * 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     */
    public String requestId;
    @XmlBean(name = "TextDetections", method = XmlBean.GenerateMethod.FROM)
    public static class COSOCRResponseTextDetections {
        /**
         * 识别出的文本行内容
         */
        public String detectedText;
        /**
         * 置信度 0 ~100
         */
        public int confidence;
        /**
         * 文本行坐标，以四个顶点坐标表示注意：此字段可能返回 null，表示取不到有效值。
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<COSOCRResponsePolygon> polygon;
        /**
         * 文本行在旋转纠正之后的图像中的像素坐标，表示为（左上角x, 左上角y，宽width，高height）
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<COSOCRResponseItemPolygon> itemPolygon;
        /**
         * 识别出来的单字信息包括单字（包括单字Character和单字置信度confidence）， 支持识别的接口：general、accurate
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<COSOCRResponseWords> words;
        /**
         * 字的坐标数组，以四个顶点坐标表示。注意：此字段可能返回 null，表示取不到有效值。支持识别的类型：handwriting
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<COSOCRResponseWordPolygon> wordPolygon;
    }
    @XmlBean(name = "ItemPolygon", method = XmlBean.GenerateMethod.FROM)
    public static class COSOCRResponseItemPolygon {
        /**
         * 左上角x
         */
        public int x;
        /**
         * 左上角y
         */
        public int y;
        /**
         * 宽width
         */
        public int width;
        /**
         * 高height
         */
        public int height;
    }
    @XmlBean(name = "Polygon", method = XmlBean.GenerateMethod.FROM)
    public static class COSOCRResponsePolygon {
        /**
         * 横坐标
         */
        public int x;
        /**
         * 纵坐标
         */
        public int y;
    }
    @XmlBean(name = "WordPolygon", method = XmlBean.GenerateMethod.FROM)
    public static class COSOCRResponseWordPolygon {
        /**
         * 左上顶点坐标
         */
        public COSOCRResponsePolygon leftTop;
        /**
         * 左上顶点坐标
         */
        public COSOCRResponsePolygon rightTop;
        /**
         * 左上顶点坐标
         */
        public COSOCRResponsePolygon rightBottom;
        /**
         * 左上顶点坐标
         */
        public COSOCRResponsePolygon leftBottom;
    }
    @XmlBean(name = "Words", method = XmlBean.GenerateMethod.FROM)
    public static class COSOCRResponseWords {
        /**
         * 置信度 0 ~100
         */
        public int confidence;
        /**
         * 候选字Character
         */
        public String character;
        /**
         * 单字在原图中的四点坐标， 支持识别的接口：general、accurate
         */
        public COSOCRResponseWordCoordPoint wordCoordPoint;
    }
    @XmlBean(name = "WordCoordPoint", method = XmlBean.GenerateMethod.FROM)
    public static class COSOCRResponseWordCoordPoint {
        /**
         * 单字在原图中的坐标，以四个顶点坐标表示，以左上角为起点，顺时针返回。
         */
        public COSOCRResponseWordCoordinate wordCoordinate;
    }
    @XmlBean(name = "WordCoordinate", method = XmlBean.GenerateMethod.FROM)
    public static class COSOCRResponseWordCoordinate {
        /**
         * 横坐标
         */
        public int x;
        /**
         * 纵坐标
         */
        public int y;
    }

}
