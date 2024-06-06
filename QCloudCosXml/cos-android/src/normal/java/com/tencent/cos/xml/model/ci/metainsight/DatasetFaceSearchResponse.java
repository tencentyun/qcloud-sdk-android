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

package com.tencent.cos.xml.model.ci.metainsight;

import java.util.List;

public class DatasetFaceSearchResponse {
    
    /**
     * 人脸检索识别结果信息列表。
     */
    public List<FaceResult> faceResult;
    /**
     * 请求 ID。
     */
    public String requestId;
    public static class FaceResult {
        /**
         * 相关人脸信息列表。
         */
        public List<FaceInfos> faceInfos;
        /**
         * 输入图片的人脸框位置。
         */
        public FaceBoundary inputFaceBoundary;
    }
    public static class FaceBoundary {
        /**
         * 人脸高度。
         */
        public int height;
        /**
         * 人脸宽度。
         */
        public int width;
        /**
         * 人脸框左上角横坐标。
         */
        public int left;
        /**
         * 人脸框左上角纵坐标。
         */
        public int top;
    }
    public static class FaceInfos {
        /**
         * 自定义人物ID。
         */
        public String personId;
        /**
         * 相关人脸框位置。
         */
        public FaceBoundary faceBoundary;
        /**
         * 人脸ID。
         */
        public String faceId;
        /**
         * 相关人脸匹配得分。
         */
        public int score;
        /**
         * 资源标识字段，表示需要建立索引的文件地址。
         */
        public String uRI;
    }

}
