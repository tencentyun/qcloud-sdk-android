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

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.ci.CiSaveLocalRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * 图像修复
 * <a href="https://cloud.tencent.com/document/product/460/79042">图像修复</a>
 * @see com.tencent.cos.xml.CIService#imageRepair(ImageRepairRequest)
 * @see com.tencent.cos.xml.CIService#imageRepairAsync(ImageRepairRequest, CosXmlResultListener)
 */
public class ImageRepairRequest extends CiSaveLocalRequest {
    
    protected final String objectKey;
    /**
     * 您可以通过填写 detect-url 处理任意公网可访问的图片链接。不填写 detect-url 时，后台会默认处理 ObjectKey ，填写了 detect-url 时，后台会处理 detect-url 链接，无需再填写 ObjectKey detect-url 示例：http://www.example.com/abc.jpg;是否必传：否
     */
    public String detectUrl;

    /**
     * 固定值：ImageRepair;是否必传：是
     */
    public String ciProcess = "ImageRepair";

    /**
     * 遮罩（白色区域为需要去除的水印位置）图片地址，私有图片需携带签名，需要经过 URL 安全的 Base64 编码。例如，遮罩图片为 http://examplebucket-1250000000.cos.ap-guangzhou.myqcloud.com/shuiyin_2.png ，则该处编码后的字符串为 aHR0cDovL2V4YW1wbGVidWNrZXQtMTI1MDAwMDAwMC5jb3MuYXAtZ3Vhbmd6aG91Lm15cWNsb3VkLmNvbS9zaHVpeWluXzIucG5n
     */
    public String maskPic;

    /**
     * 例如： [[[608, 794], [1024, 794], [1024, 842], [608, 842]],[[1295, 62], [1295, 30], [1597, 32],[1597,64]]] ，顺时针输⼊多边形的每个点的坐标,每个多边形: [[x1, y1], [x2, y2]...] , 形式为三维矩阵（多个多边形： [多边形1,多边形2] ）或⼆维矩阵（单个多边形），且需要经过 URL 安全的 Base64 编码。MaskPoly 同时与 MaskPic 填写时，优先采⽤ MaskPic 的值。
     */
    public String maskPoly;

    /**
     * 腾讯云数据万象通过 ImageRepair 接⼝能检测并擦除图片中常见的标志,并对擦除部分进行智能修复，此功能需携带签名
     *
     * @param bucket  存储桶名
     */
    public ImageRepairRequest(@NonNull String bucket , @NonNull String objectKey) {
        super.bucket = bucket;
        this.objectKey = objectKey;

    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        if(!TextUtils.isEmpty(detectUrl)) {
            queryParameters.put("detect-url", detectUrl);
        }
        queryParameters.put("maskPic", maskPic);
        queryParameters.put("maskPoly", maskPoly);
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        if(TextUtils.isEmpty(objectKey)){
            return "/";
        } else {
            return "/" + objectKey;
        }
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
