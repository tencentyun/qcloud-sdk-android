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

package com.tencent.cos.xml.transfer;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.CosError;
import com.tencent.cos.xml.utils.BaseXmlSlimParser;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.core.http.ResponseBodyConverter;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 响应到{@link CosXmlResult}的转换器
 * @param <T>
 */
public class ResponseXmlS3BodySerializer<T> extends ResponseBodyConverter<T> {

    private CosXmlResult cosXmlResult;

    public ResponseXmlS3BodySerializer(CosXmlResult cosXmlResult) {
       this.cosXmlResult = cosXmlResult;
    }

    @Override
    public T convert(HttpResponse response) throws QCloudClientException, QCloudServiceException {
        parseCOSXMLError(response);
        cosXmlResult.parseResponseBody(response);
        return (T) cosXmlResult;
    }

    private void parseCOSXMLError(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        int httpCode = response.code();
        if(httpCode >= 200 && httpCode < 300)return;
        String message = response.message();
        CosXmlServiceException cosXmlServiceException = new CosXmlServiceException(message);
        cosXmlServiceException.setStatusCode(httpCode);
        cosXmlServiceException.setRequestId(response.header("x-cos-request-id"));
        String contentType = response.header(HttpConstants.Header.CONTENT_TYPE);
        CosError cosError = new CosError();
        if(HttpConstants.ContentType.JSON.equalsIgnoreCase(contentType)){
            try {
                cosError = CosError.fromJson(response.string());
            } catch (JSONException e) {
                throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
            } catch (IOException e) {
                throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
            }
        } else {
            InputStream inputStream = response.byteStream();
            if(inputStream != null){
                try {
                    BaseXmlSlimParser.parseError(inputStream, cosError);
                } catch (XmlPullParserException e) {
                    throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
                } catch (IOException e) {
                    throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
                }
            }
        }
        if(cosError.code != null) cosXmlServiceException.setErrorCode(cosError.code);
        if(cosError.message != null) cosXmlServiceException.setErrorMessage(cosError.message);
        if(cosError.requestId != null) cosXmlServiceException.setRequestId(cosError.requestId);
        if(cosError.resource != null) cosXmlServiceException.setServiceName(cosError.resource);
        throw cosXmlServiceException;
    }

}
