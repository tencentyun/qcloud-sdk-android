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
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import java.util.Map;


/**
 * 卡证识别
 * <a href="https://cloud.tencent.com/document/product/460/96767">卡证识别</a>
 * @see com.tencent.cos.xml.CIService#aiLicenseRec(AILicenseRecRequest)
 * @see com.tencent.cos.xml.CIService#aiLicenseRecAsync(AILicenseRecRequest, CosXmlResultListener)
 */
public class AILicenseRecRequest extends BucketRequest {
    
    protected final String objectKey;

    /**
     * 数据万象处理能力，卡证识别固定为AILicenseRec;是否必传：是
     */
    public String ciProcess = "AILicenseRec";
    /**
     * 您可以通过填写 detect-url 处理任意公网可访问的图片链接。不填写 detect-url 时，后台会默认处理 ObjectKey ，填写了 detect-url 时，后台会处理 detect-url 链接，无需再填写 ObjectKey detect-url 示例：http://www.example.com/abc.jpg;是否必传：否
     */
    public String detectUrl;
    /**
     * 卡证识别类型，有效值为IDCard，DriverLicense。<br>IDCard表示身份证；DriverLicense表示驾驶证，默认：DriverLicense;是否必传：是
     */
    public String cardType;

    /**
     * 本接口支持中国大陆居民二代身份证正面（暂不支持背面）、驾驶证主页（暂不支持副页）所有字段的自动定位，暂不支持文本识别，用于对特定字段的抹除、屏蔽，以及进一步的文本识别
     *
     * @param bucket  存储桶名
     */
    public AILicenseRecRequest(@NonNull String bucket , @NonNull String objectKey) {
        super(bucket);
        this.objectKey = objectKey;

    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        queryParameters.put("detect-url", detectUrl);
        queryParameters.put("CardType", cardType);
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        if(!TextUtils.isEmpty(detectUrl) || TextUtils.isEmpty(objectKey)){
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
