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

/**
 * 盲水印图片处理结果
 */
public class PicObject {
    /** 文件名 */
    public String key;
    /** 图片路径 */
    public String location;
    /** 图片格式 */
    public String format;
    /** 图片宽度 */
    public int width;
    /** 图片高度 */
    public int height;
    /** 图片大小 */
    public int size;
    /** 图片质量 */
    public int quality;

    /**
     * 构造盲水印图片处理结果
     * @param key 文件名
     * @param location 图片路径
     * @param format 图片格式
     * @param width 图片宽度
     * @param height 图片高度
     * @param size 图片大小
     * @param quality 图片质量
     */
    public PicObject(String key, String location, String format, int width, int height, int size, int quality) {
        this.key = key;
        this.location = location;
        this.format = format;
        this.width = width;
        this.height = height;
        this.size = size;
        this.quality = quality;
    }

    public PicObject() {}
}
