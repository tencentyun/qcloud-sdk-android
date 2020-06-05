package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

final public class AppendObjectResult extends CosXmlResult {

    private String contentSHA1;
    private String nextAppendPosition;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        contentSHA1 = response.header("x-cos-content-sha1");
        nextAppendPosition = response.header("x-cos-next-append-position");
    }

    @Override
    public String printResult() {
        return super.printResult()
                + "\n"
                + contentSHA1 + "\n"
                + nextAppendPosition + "\n";
    }
}
