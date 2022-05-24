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

package com.tencent.cos.xml.model.service;


import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 获取所属账户下所有存储桶列表的请求.
 * @see com.tencent.cos.xml.CosXml#getService(GetServiceRequest)
 * @see com.tencent.cos.xml.CosXml#getServiceAsync(GetServiceRequest, CosXmlResultListener)
 */
final public class GetServiceRequest extends CosXmlRequest {
    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public String getPath(CosXmlServiceConfig config) {
        return  "/";
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
    }

    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        // return super.getRequestHost(config);
        return "service.cos.myqcloud.com";
    }

//    @Override
//    public String getHost(CosXmlServiceConfig config, boolean isSupportAccelerate){
//        String endpointSuffix = config.getEndpointSuffix(region, isSupportAccelerate);
//        if (endpointSuffix.endsWith("myqcloud.com")) {
//            return "service.cos.myqcloud.com";
//        } else {
//            return "service." + endpointSuffix;
//        }
//    }
//
//    @Override
//    public String getHost(CosXmlServiceConfig config, boolean isSupportAccelerate, boolean isHeader) {
//        String endpointSuffix = config.getEndpointSuffix(region, isSupportAccelerate);
//        if (endpointSuffix.endsWith("myqcloud.com")) {
//            return "service.cos.myqcloud.com";
//        } else {
//            return "service." + endpointSuffix;
//        }
//    }
}
