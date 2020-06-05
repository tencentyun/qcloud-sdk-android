package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

import java.util.Arrays;
import java.util.List;

final public class OptionObjectResult extends CosXmlResult {
    private String accessControlAllowOrigin;
    private List<String> accessControlAllowMethods;
    private List<String> accessControlAllowHeaders;
    private List<String> accessControlExposeHeaders;
    private long accessControlMaxAge;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        accessControlAllowOrigin = response.header("Access-Control-Allow-Origin");
        if(response.header("Access-Control-Max-Age") != null){
            accessControlMaxAge = Long.parseLong(response.header("Access-Control-Max-Age"));
        }
        if(response.header("Access-Control-Allow-Methods") != null){
            accessControlAllowMethods = Arrays.asList(response.header("Access-Control-Allow-Methods").split(","));
        }
        if(response.header("Access-Control-Allow-Headers") != null){
            accessControlAllowHeaders = Arrays.asList(response.header("Access-Control-Allow-Headers").split(","));
        }
        if(response.header("Access-Control-Expose-Headers") != null){
            accessControlExposeHeaders = Arrays.asList(response.header("Access-Control-Expose-Headers").split(","));
        }
    }

    @Override
    public String printResult() {
        return super.printResult() + "\n"
                + accessControlAllowOrigin + "\n"
                + accessControlMaxAge + "\n";
    }
}
