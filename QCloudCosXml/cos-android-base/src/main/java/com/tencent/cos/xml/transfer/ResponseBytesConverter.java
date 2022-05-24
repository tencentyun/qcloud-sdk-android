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
import com.tencent.cos.xml.model.object.GetObjectBytesResult;
import com.tencent.cos.xml.model.tag.CosError;
import com.tencent.cos.xml.utils.BaseXmlSlimParser;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.core.http.ResponseBodyConverter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 响应到字节数组的转换器
 * @param <T>
 */
public class ResponseBytesConverter<T> extends ResponseBodyConverter<T> {

    private GetObjectBytesResult getObjectBytesResult;

    public ResponseBytesConverter(GetObjectBytesResult getObjectBytesResult) {
        this.getObjectBytesResult = getObjectBytesResult;
    }

    @Override
    public T convert(HttpResponse<T> response) throws QCloudClientException, QCloudServiceException {
        parseCOSXMLError(response);
        getObjectBytesResult.parseResponseBody(response);
        return (T) getObjectBytesResult;
    }

    private void parseCOSXMLError(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        int httpCode = response.code();
        if(httpCode >= 200 && httpCode < 300)return;
        String message = response.message();
        CosXmlServiceException cosXmlServiceException = new CosXmlServiceException(message);
        cosXmlServiceException.setStatusCode(httpCode);
        cosXmlServiceException.setRequestId(response.header("x-cos-request-id"));
        InputStream inputStream = response.byteStream();
        if(inputStream != null){
            CosError cosError = new CosError();
            try {
                BaseXmlSlimParser.parseError(inputStream, cosError);
                cosXmlServiceException.setErrorCode(cosError.code);
                cosXmlServiceException.setErrorMessage(cosError.message);
                cosXmlServiceException.setRequestId(cosError.requestId);
                cosXmlServiceException.setServiceName(cosError.resource);
            } catch (XmlPullParserException e) {
                throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
            } catch (IOException e) {
                throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
            }
        }
        throw cosXmlServiceException;
    }
}
