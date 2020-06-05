package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.MTAProxy;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.CopyObject;
import com.tencent.cos.xml.model.tag.CosError;
import com.tencent.cos.xml.transfer.XmlSlimParser;
import com.tencent.cos.xml.utils.CloseUtil;
import com.tencent.qcloud.core.auth.Utils;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bradyxiao on 2017/9/20.
 * author bradyxiao
 */
public class CopyObjectResult extends CosXmlResult {

    public CopyObject copyObject;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        //是否存在error
        InputStream inputStream = null;
        try {
            copyObject = new CopyObject();
            byte[] contents = response.bytes();
            inputStream = new ByteArrayInputStream(contents);
            XmlSlimParser.parseCopyObjectResult(inputStream, copyObject);
            if(copyObject.eTag == null) {
                if (contents != null && contents.length > 0) {
                    inputStream.reset();
                    CosXmlServiceException cosXmlServiceException = new CosXmlServiceException("failed");
                    CosError cosError = new CosError();
                    XmlSlimParser.parseError(inputStream, cosError);
                    cosXmlServiceException.setErrorCode(cosError.code);
                    cosXmlServiceException.setErrorMessage(cosError.message);
                    cosXmlServiceException.setRequestId(cosError.requestId);
                    cosXmlServiceException.setServiceName(cosError.resource);
                    cosXmlServiceException.setStatusCode(response.code());
                    throw cosXmlServiceException;
                }
            }
        }catch (XmlPullParserException e) {
           throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }finally {
            CloseUtil.closeQuietly(inputStream);
        }
    }

    @Override
    public String printResult() {
        return copyObject != null ? copyObject.toString() : super.printResult();
    }
}
