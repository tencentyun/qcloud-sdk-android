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

package com.tencent.cos.xml.listener;


import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;

/**
 * <p>
 *  请求结果回调接口<br>
 *  若是请求成功，即返回http code 在[200, 300)之间，回调 {@link #onSuccess(CosXmlRequest, CosXmlResult)}<br>
 *  若是请求失败，回调 {@link #onFail(CosXmlRequest, CosXmlClientException, CosXmlServiceException)}.
 * </p>
 * @see CosXmlRequest
 * @see CosXmlResult
 * @see CosXmlClientException
 * @see CosXmlServiceException
 */
public interface CosXmlResultListener {
    /**
     * 成功回调
     * @param request COS请求 {@link CosXmlRequest}
     * @param result COS结果 {@link CosXmlResult}
     */
    void onSuccess(CosXmlRequest request, CosXmlResult result);

    /**
     * 失败回调
     * @param request COS请求 {@link CosXmlRequest}
     * @param clientException 客户端异常 {@link CosXmlClientException}
     * @param serviceException 服务端异常 {@link CosXmlServiceException}
     */
    void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException);

}
