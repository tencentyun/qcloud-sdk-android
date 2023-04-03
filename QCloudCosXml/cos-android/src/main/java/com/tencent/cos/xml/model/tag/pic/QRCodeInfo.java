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

package com.tencent.cos.xml.model.tag.pic;

import android.graphics.Point;

import androidx.annotation.Nullable;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * 二维码识别结果
 */
@XmlBean(name = "QRcodeInfo", method = XmlBean.GenerateMethod.FROM)
public class QRCodeInfo {
    /**
     * 二维码的内容。可能识别不出
     */
    public String codeUrl;

    /**
     * 图中识别到的二维码位置坐标
     */
    public List<QRCodePoint> codeLocation;

    @XmlBean(name = "Point", method = XmlBean.GenerateMethod.FROM)
    public static class QRCodePoint {
        @XmlElement(ignoreName = true)
        public String point;

        public @Nullable
        Point point() {
            String[] xy = point.split(",");
            return xy.length == 2 ? new Point(Integer.parseInt(xy[0]),
                    Integer.parseInt(xy[1])) : null;
        }
    }
}
