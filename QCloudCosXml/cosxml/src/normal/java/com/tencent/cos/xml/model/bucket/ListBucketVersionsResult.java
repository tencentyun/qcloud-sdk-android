package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.ListBucketVersions;
import com.tencent.cos.xml.transfer.XmlParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by bradyxiao on 2018/1/11.
 */

public class ListBucketVersionsResult extends CosXmlResult {

    public ListBucketVersions listBucketVersions;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        listBucketVersions = new ListBucketVersions();
        try {
            XmlParser.parseListBucketVersions(response.byteStream(), listBucketVersions);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    @Override
    public String printResult() {
        if(listBucketVersions != null){
            return listBucketVersions.toString();
        }else{
            return super.printResult();
        }
    }
}
