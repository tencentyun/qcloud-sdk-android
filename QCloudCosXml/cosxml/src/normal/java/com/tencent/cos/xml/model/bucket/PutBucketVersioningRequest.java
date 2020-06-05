package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.VersioningConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by bradyxiao on 2017/11/6.
 * <p>
 *     Put Bucket Versioning 接口实现启用或者暂停存储桶的版本控制功能。
 * </p>
 * <H1>一旦开启，无法关闭，是不可逆操作</H1>
 *
 */

public class PutBucketVersioningRequest extends BucketRequest {

    private VersioningConfiguration versioningConfiguration;

    public PutBucketVersioningRequest(String bucket){
        super(bucket);
        versioningConfiguration = new VersioningConfiguration();
    }

    public PutBucketVersioningRequest(){
        super(null);
        versioningConfiguration = new VersioningConfiguration();
    }


    /** 版本是否开启，true:开启，false:不开启*/
    public void setEnableVersion(boolean isEnable){
        if(isEnable){
            versioningConfiguration.status = "Enabled";
        }else {
            versioningConfiguration.status = "Suspended";
        }
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("versioning", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildVersioningConfiguration(versioningConfiguration));
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }
}
