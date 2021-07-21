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

package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.qcloud.core.http.HttpConstants;

/**
 * 预览文档的请求.
 * @see com.tencent.cos.xml.CosXmlService#getSnapshot(GetSnapshotRequest)
 * @see com.tencent.cos.xml.CosXmlService#getSnapshotAsync(GetSnapshotRequest, CosXmlResultListener)
 */
public class GetSnapshotRequest extends GetObjectRequest {

    private float time;

    /**
     * 预览请求构造器
     *
     * @param bucket 文档所在存储桶
     * @param cosPath 文档的对象键
     * @param savePath 文档本地保存路径
     * @param fileName 本地文件名称
     * @param time 截帧的时间
     */
    public GetSnapshotRequest(String bucket, String cosPath, String savePath, String fileName,
                              float time) {
        super(bucket, cosPath, savePath, fileName);
        this.time = time;
        queryParameters.put("ci-process", "snapshot");
        queryParameters.put("time", String.valueOf(time));
    }


    /**
     * 截图的宽。默认为0
     * @param width 截图的宽
     */
    public void setWidth(int width) {
        if (width <= 0) {
            return;
        }
        queryParameters.put("width", String.valueOf(width));
    }

    /**
     * 截图的高。默认为0
     * @param height 截图的高
     */
    public void setHeight(int height) {
        if (height <= 0) {
            return;
        }
        queryParameters.put("height", String.valueOf(height));
    }

    /**
     * 截图的格式，支持 jpg 和 png，默认 jpg
     * @param format 截图的格式
     */
    public void setFormat(String format) {
        queryParameters.put("format", format);
    }

    /**
     * 图片旋转方式
     * auto：按视频旋转信息进行自动旋转
     * off：不旋转
     * 默认值为 auto
     *
     * @param rotate 图片旋转方式
     */
    public void setRotate(String rotate) {
        queryParameters.put("rotate", rotate);
    }

    /**
     * 截帧方式
     * keyframe：截取指定时间点之前的最近的一个关键帧
     * exactframe：截取指定时间点的帧
     * 默认值为 exactframe
     *
     * @param mode 截帧方式
     */
    public void setMode(String mode) {
        queryParameters.put("mode", mode);
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();

        if (time < 0) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),
                    "Please set a valid time");
        }
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.GET;
    }

}
