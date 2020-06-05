package com.tencent.cos.xml.model.service;


import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.ListAllMyBuckets;
import com.tencent.cos.xml.transfer.XmlParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

final public class GetServiceResult extends CosXmlResult {
    /**
     * ListAllMyBuckets <a href="https://www.qcloud.com/document/product/436/8291"></a>
     * {@link ListAllMyBuckets}
     */
    public ListAllMyBuckets listAllMyBuckets;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        try {
            listAllMyBuckets = new ListAllMyBuckets();
            XmlParser.parseListAllMyBucketsResult(response.byteStream(), listAllMyBuckets);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    @Override
    public String printResult() {
        return listAllMyBuckets != null ? listAllMyBuckets.toString() : super.printResult();
    }
}
