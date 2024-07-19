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

import androidx.annotation.NonNull;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import java.util.Map;


/**
 * 活体人脸核身
 * <a href="https://cloud.tencent.com/document/product/460/48641">活体人脸核身</a>
 * @see com.tencent.cos.xml.CIService#livenessRecognition(LivenessRecognitionRequest)
 * @see com.tencent.cos.xml.CIService#livenessRecognitionAsync(LivenessRecognitionRequest, CosXmlResultListener)
 */
public class LivenessRecognitionRequest extends BucketRequest {
    
    protected final String objectKey;

    /**
     * 数据万象处理能力，人脸核身固定为 LivenessRecognition;是否必传：是
     */
    public String ciProcess = "LivenessRecognition";
    /**
     * 身份证号;是否必传：是
     */
    public String idCard;
    /**
     * 姓名;是否必传：是
     */
    public String name;
    /**
     * 活体检测类型，取值：LIP/ACTION/SILENTLIP 为数字模式，ACTION 为动作模式，SILENT 为静默模式，三种模式选择一种传入;是否必传：是
     */
    public String livenessType;
    /**
     * 数字模式传参：数字验证码（1234），需先调用接口获取数字验证码动作模式传参：传动作顺序（2，1 or 1，2），需先调用接口获取动作顺序静默模式传参：空;是否必传：否
     */
    public String validateData;
    /**
     * 需要返回多张最佳截图，取值范围1 - 10，不设置默认返回一张最佳截图;是否必传：否
     */
    public int bestFrameNum;

    /**
     * 集成了活体检测和跟权威库进行比对的能力，传入一段视频和姓名、身份证号信息即可进行验证。对录制的自拍视频进行活体检测，从而确认当前用户为真人，可防止照片、视频、静态3D建模等各种不同类型的攻击。检测为真人后，再判断该视频中的人与权威库的证件照是否属于同一个人，实现用户身份信息核实
     *
     * @param bucket  存储桶名
     */
    public LivenessRecognitionRequest(@NonNull String bucket , @NonNull String objectKey) {
        super(bucket);
        this.objectKey = objectKey;

    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        queryParameters.put("IdCard", idCard);
        queryParameters.put("Name", name);
        queryParameters.put("LivenessType", livenessType);
        queryParameters.put("ValidateData", validateData);
        queryParameters.put("BestFrameNum", String.valueOf(bestFrameNum));
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/" + objectKey;
    }
    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }
    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.GET;
    }
    
}
