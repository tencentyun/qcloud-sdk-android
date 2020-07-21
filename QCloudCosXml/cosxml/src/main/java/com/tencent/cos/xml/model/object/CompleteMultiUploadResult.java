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

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.CompleteMultipartUploadResult;
import com.tencent.cos.xml.model.tag.CosError;
import com.tencent.cos.xml.transfer.XmlSlimParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 完成整个分块上传的返回的结果.
 * @see com.tencent.cos.xml.SimpleCosXml#completeMultiUpload(CompleteMultiUploadRequest)
 * @see CompleteMultiUploadRequest
 */
final public class CompleteMultiUploadResult extends CosXmlResult {

    /**
     * 完成整个分片上传返回的所有信息, {@link com.tencent.cos.xml.model.tag.CompleteMultipartUploadResult}
     */
    public CompleteMultipartUploadResult completeMultipartUpload;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        InputStream inputStream = null;
        try {
            completeMultipartUpload = new CompleteMultipartUploadResult();
            byte[] contents = response.bytes();
            inputStream = new ByteArrayInputStream(contents);
            XmlSlimParser.parseCompleteMultipartUploadResult(inputStream, completeMultipartUpload);
            if(completeMultipartUpload.eTag == null || completeMultipartUpload.key == null
                    || completeMultipartUpload.bucket == null){
                if(contents != null && contents.length > 0){
                    inputStream.reset();
                    CosXmlServiceException cosXmlServiceException = new CosXmlServiceException("failed");
                    CosError cosError = new CosError();
                    XmlSlimParser.parseError(inputStream, cosError);
                    cosXmlServiceException.setErrorCode(cosError.code);
                    cosXmlServiceException.setErrorMessage(cosError.message);
                    cosXmlServiceException.setRequestId(cosError.requestId);
                    cosXmlServiceException.setServiceName(cosError.resource);
                    cosXmlServiceException.setStatusCode(response.code());
                    throw cosXmlServiceException;
                }
            }
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    @Override
    public String printResult() {
        return completeMultipartUpload != null ? completeMultipartUpload.toString() : super.printResult();
    }
}
