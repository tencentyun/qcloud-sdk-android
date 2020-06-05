package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.BucketLoggingStatus;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

public class PutBucketLoggingRequest extends BucketRequest {

    private BucketLoggingStatus bucketLoggingStatus;

    public PutBucketLoggingRequest(String bucket) {
        super(bucket);
        bucketLoggingStatus = new BucketLoggingStatus();
    }

    public PutBucketLoggingRequest(){
        super(null);
        bucketLoggingStatus = new BucketLoggingStatus();
    }

    public void setTargetBucket(String targetBucket){
        if(targetBucket == null) return;
        if(bucketLoggingStatus.loggingEnabled == null){
            bucketLoggingStatus.loggingEnabled = new BucketLoggingStatus.LoggingEnabled();
        }
        bucketLoggingStatus.loggingEnabled.targetBucket = targetBucket;
    }

    public void setTargetPrefix(String targetPrefix){
        if(targetPrefix == null) return;
        if(bucketLoggingStatus.loggingEnabled == null){
            bucketLoggingStatus.loggingEnabled = new BucketLoggingStatus.LoggingEnabled();
        }
        bucketLoggingStatus.loggingEnabled.targetPrefix = targetPrefix;
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("logging", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildBucketLogging(bucketLoggingStatus));
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
}
