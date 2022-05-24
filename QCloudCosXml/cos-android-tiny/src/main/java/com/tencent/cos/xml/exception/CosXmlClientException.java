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

import com.tencent.qcloud.core.common.QCloudClientException;

/**
 * <p>
 * 客户端异常 <br>
 * 是由于客户端无法和 COS 服务端正常进行交互所引起。如客户端无法连接到服务端，无法解析服务端返回的数据，读取本地文件发生 IO 异常等.
 * <a href="https://cloud.tencent.com/document/product/436/34539">客户端异常</a>
 */
public class CosXmlClientException extends QCloudClientException {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端错误码，如10000表示参数检验失败，
     * 更多详情请参见 <a href="https://cloud.tencent.com/document/product/436/30443">SDK 错误码</a>
     */
    public final int errorCode;

    public CosXmlClientException(int errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public CosXmlClientException(int errorCode, String message, Throwable cause){
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CosXmlClientException(int errorCode, Throwable cause){
        super(cause);
        this.errorCode = errorCode;
    }
}
