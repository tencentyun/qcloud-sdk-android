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

package com.tencent.cos.xml.model.tag;

import com.tencent.cos.xml.model.tag.pic.ImageInfo;
import com.tencent.cos.xml.model.tag.pic.PicObject;
import com.tencent.cos.xml.model.tag.pic.PicOriginalInfo;
import com.tencent.cos.xml.model.tag.pic.PicUploadResult;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;
import java.util.List;

/**
 * 完成分块上传结果
 */
@XmlBean(name = "CompleteMultipartUploadResult")
public class CompleteMultipartUploadResult {

    /**
     * 原图文件名
     */
    @XmlElement(name = "Key")
    public String key;
    /**
     * 图片路径
     */
    @XmlElement(name = "Location")
    public String location;

    @XmlElement(name = "ETag")
    public String eTag;

    @XmlElement(name = "ImageInfo")
    public ImageInfo imageInfo;

    @XmlElement(name = "ProcessResults")
    public List<PicObject> processResults;

    public PicOriginalInfo getOriginInfo() {
        PicOriginalInfo originalInfo = new PicOriginalInfo();
        originalInfo.location = location;
        originalInfo.key = key;
        originalInfo.etag = eTag;
        originalInfo.imageInfo = imageInfo;
        return originalInfo;
    }

    public PicUploadResult getPicUploadResult() {
        PicUploadResult result = new PicUploadResult();
        result.processResults = processResults;
        result.originalInfo = getOriginInfo();
        return result;
    }
}

