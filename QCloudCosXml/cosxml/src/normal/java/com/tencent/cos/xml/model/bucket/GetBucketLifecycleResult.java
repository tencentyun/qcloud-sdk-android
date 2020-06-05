package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.LifecycleConfiguration;
import com.tencent.cos.xml.transfer.XmlParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

final public class GetBucketLifecycleResult extends CosXmlResult {

    public LifecycleConfiguration lifecycleConfiguration;

    @Override
    public void parseResponseBody(HttpResponse response) throws  CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        lifecycleConfiguration = new LifecycleConfiguration();
        try {
            XmlParser.parseLifecycleConfiguration(response.byteStream(), lifecycleConfiguration);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(),e);
        }
    }

    @Override
    public String printResult() {
        return lifecycleConfiguration != null ? lifecycleConfiguration.toString() : super.printResult();
    }
}
