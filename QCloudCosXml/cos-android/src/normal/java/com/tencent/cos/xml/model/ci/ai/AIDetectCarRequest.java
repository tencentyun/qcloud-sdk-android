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
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import java.util.Map;


/**
 * 车辆车牌识别
 * <a href="https://cloud.tencent.com/document/product/460/63225">车辆车牌识别</a>
 * @see com.tencent.cos.xml.CIService#aiDetectCar(AIDetectCarRequest)
 * @see com.tencent.cos.xml.CIService#aiDetectCarAsync(AIDetectCarRequest, CosXmlResultListener)
 */
public class AIDetectCarRequest extends BucketRequest {
    
    protected final String objectKey;

    /**
     * 数据万象处理能力，车辆识别固定为 DetectCar;是否必传：是
     */
    public String ciProcess = "DetectCar";

    /**
     * 车辆车牌检测功能为同步请求方式，您可以通过本接口检测图片中的车辆，识别出车辆的品牌、颜色、位置、车牌位置等信息。该接口属于 GET 请求
     *
     * @param bucket  存储桶名
     */
    public AIDetectCarRequest(@NonNull String bucket , @NonNull String objectKey) {
        super(bucket);
        this.objectKey = objectKey;

    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
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
