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

package com.tencent.cos.xml.model.tag.audit.bean;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * 该参数用于返回 OCR 检测框在图片中的位置（左上角 xy 坐标、长宽、旋转角度），以方便快速定位识别文字的相关信息。
 */
@XmlBean(name = "Location")
public class AuditOcrLocation {
    /**
     * 该参数用于返回检测框左上角位置的横坐标（x）所在的像素位置，结合剩余参数可唯一确定检测框的大小和位置。
     */
    public int x;
    /**
     * 该参数用于返回检测框左上角位置的纵坐标（y）所在的像素位置，结合剩余参数可唯一确定检测框的大小和位置。
     */
    public int y;
    /**
     * 该参数用于返回检测框的高度（由左上角出发在 y 轴向下延伸的长度），结合剩余参数可唯一确定检测框的大小和位置。
     */
    public int height;
    /**
     * 该参数用于返回检测框的宽度（由左上角出发在 x 轴向右延伸的长度），结合剩余参数可唯一确定检测框的大小和位置。
     */
    public int width;
    /**
     * 该参数用于返回检测框的旋转角度，该参数结合 X 和 Y 两个坐标参数可唯一确定检测框的具体位置；取值：0-360（角度制），方向为逆时针旋转。
     */
    public int rotate;
}
