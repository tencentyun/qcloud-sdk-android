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

package com.tencent.cos.xml.model.object;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.SimpleCosXml;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.tag.CallbackResult;
import com.tencent.cos.xml.model.tag.pic.PicUploadResult;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpResponse;

import java.io.IOException;

/**
 * 简单上传的返回结果.
 * @see SimpleCosXml#putObject(PutObjectRequest)
 * @see PutObjectRequest
 */
final public class PutObjectResult extends BasePutObjectResult {
    public PicUploadResult picUploadResult;

    public CallbackResult callbackResult;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        try {
            String contentType = response.header("Content-Type");
            if("application/xml".equalsIgnoreCase(contentType)){
                if(httpCode == 203){
                    // xml内容 且http状态码为203 任务是回调失败
                    CallbackResult.Error callbackResultError = QCloudXmlUtils.fromXml(response.byteStream(), CallbackResult.Error.class);
                    callbackResult = new CallbackResult();
                    callbackResult.status = "203";
                    callbackResult.error = callbackResultError;
                } else {
                    // xml内容 非203保持之前逻辑  进行万象响应解析（万象的响应有没有可能http状态码是203?）
                    picUploadResult = QCloudXmlUtils.fromXml(response.byteStream(), PicUploadResult.class);
                }
            } else {
                String responseString;
                try {
                    responseString = response.string();
                } catch (IOException e) {throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);}
                if(!TextUtils.isEmpty(responseString)){
                    // 不是xml且body不为空 认为是回调成功
                    callbackResult = new CallbackResult();
                    callbackResult.status = "200";

                    callbackResult.callbackBody = responseString;
                    // PutObject 不是base64
                    callbackResult.callbackBodyNotBase64 = true;
                }
            }
        } catch (Exception e){
            if(e instanceof CosXmlClientException){
                throw e;
            } else {
                throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
            }
        }
    }

    /**
     * 图片处理印结果<br>
     * @return 图片处理
     */
    public @Nullable PicUploadResult picUploadResult() {
        return picUploadResult;
    }
}
