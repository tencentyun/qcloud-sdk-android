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
 * 图片二维码生成
 * <a href="https://cloud.tencent.com/document/product/460/53491">图片二维码生成</a>
 * @see com.tencent.cos.xml.CIService#createCRcode(CreateCRcodeRequest)
 * @see com.tencent.cos.xml.CIService#createCRcodeAsync(CreateCRcodeRequest, CosXmlResultListener)
 */
public class CreateCRcodeRequest extends BucketRequest {
    

    /**
     * 数据万象处理能力，二维码生成参数为 qrcode-generate;是否必传：是
     */
    public String ciProcess = "qrcode-generate";
    /**
     * 可识别的二维码文本信息;是否必传：是
     */
    public String qrcodeContent;
    /**
     * 生成的二维码类型，可选值：0或1。0为二维码，1为条形码，默认值为0;是否必传：否
     */
    public int mode;
    /**
     * 指定生成的二维码或条形码的宽度，高度会进行等比压缩;是否必传：是
     */
    public String width;

    /**
     * 数据万象二维码生成功能可根据用户指定的文本信息（URL 或文本），生成对应的二维码或条形码
     *
     * @param bucket  存储桶名
     */
    public CreateCRcodeRequest(@NonNull String bucket ) {
        super(bucket);
        
    }
    
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ci-process", ciProcess);
        queryParameters.put("qrcode-content", qrcodeContent);
        queryParameters.put("mode", String.valueOf(mode));
        queryParameters.put("width", width);
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/";
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
