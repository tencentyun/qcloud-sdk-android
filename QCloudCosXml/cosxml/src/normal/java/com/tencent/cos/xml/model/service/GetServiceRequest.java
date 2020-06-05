package com.tencent.cos.xml.model.service;


import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * <p>获取请求者名下的所有存储空间列表（Bucket list）</p>
 *
 */
final public class GetServiceRequest extends CosXmlRequest {

    public GetServiceRequest(){}

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
