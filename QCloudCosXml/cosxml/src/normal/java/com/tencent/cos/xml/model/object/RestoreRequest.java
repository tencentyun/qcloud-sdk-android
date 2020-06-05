package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.RestoreConfigure;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by bradyxiao on 2017/12/29.
 */

public class RestoreRequest extends ObjectRequest {

    private RestoreConfigure restoreConfigure;

    public RestoreRequest(String bucket, String cosPath) {
        super(bucket, cosPath);
        restoreConfigure = new RestoreConfigure();
        restoreConfigure.casJobParameters = new RestoreConfigure.CASJobParameters();
    }

    public RestoreRequest() {
        super(null, null);
        restoreConfigure = new RestoreConfigure();
        restoreConfigure.casJobParameters = new RestoreConfigure.CASJobParameters();
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("restore", null);
        return queryParameters;
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildRestore(restoreConfigure));
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }

    public void setExpireDays(int days){
        if(days < 0) days = 0;
        restoreConfigure.days = days;
    }

    public void setTier(RestoreConfigure.Tier tier){
        if(tier != null){
            restoreConfigure.casJobParameters.tier = tier.getTier();
        }
    }
}
