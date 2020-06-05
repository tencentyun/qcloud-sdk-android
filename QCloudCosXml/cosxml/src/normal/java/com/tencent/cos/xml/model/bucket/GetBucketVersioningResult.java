package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.VersioningConfiguration;
import com.tencent.cos.xml.transfer.XmlParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by bradyxiao on 2017/11/6.
 */

public class GetBucketVersioningResult extends CosXmlResult {

    public VersioningConfiguration versioningConfiguration;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        versioningConfiguration = new VersioningConfiguration();
        try {
            XmlParser.parseVersioningConfiguration(response.byteStream(), versioningConfiguration);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    @Override
    public String printResult() {
        return versioningConfiguration != null ? versioningConfiguration.toString() : super.printResult();
    }
}
