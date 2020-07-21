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

package com.tencent.cos.xml.exception;

import com.tencent.qcloud.core.common.QCloudServiceException;

/**
 * <p>
 * 服务端异常<br>
 * 是客户端和 COS 服务端交互正常，但操作 COS 资源失败。如客户端访问一个不存在 Bucket ，删除一个不存在的文件，没有权限进行某个操作等.
 * 请参考：<a herf="https://cloud.tencent.com/document/product/436/34539">服务端异常</a>
 */
public class CosXmlServiceException extends QCloudServiceException {

    private static final long serialVersionUID = 1L;

    private String httpMsg;

    public CosXmlServiceException(String httpMsg) {
        super(null);
        this.httpMsg = httpMsg;
    }

    public CosXmlServiceException(String errorMessage, Exception cause) {
        super(errorMessage, cause);
    }

    public String getHttpMessage(){
        return httpMsg;
    }

    public CosXmlServiceException(QCloudServiceException qcloudServiceException){
        super(null);
        this.setErrorCode(qcloudServiceException.getErrorCode());
        this.setErrorMessage(qcloudServiceException.getErrorMessage());
        this.setRequestId(qcloudServiceException.getRequestId());
        this.setServiceName(qcloudServiceException.getServiceName());
        this.setStatusCode(qcloudServiceException.getStatusCode());
    }

    @Override
    public String getMessage() {
        return getErrorMessage()
                + " (Service: " + getServiceName()
                + "; Status Code: " + getStatusCode()
                + "; Status Message: " + httpMsg
                + "; Error Code: " + getErrorCode()
                + "; Request ID: " + getRequestId() + ")";
    }
}
