package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import com.tencent.cos.xml.common.RequestMethod;

import java.util.Map;

/**
 * Created by bradyxiao on 2018/1/11.
 */

public class ListBucketVersionsRequest extends BucketRequest {

    private String prefix;
    private String keyMarker;
    private String versionIdMarker;
    private String delimiter;
    private String encodingType; // 目前支持 "url", url Encoding
    private String maxKeys = "1000";


    public ListBucketVersionsRequest(String bucket) {
        super(bucket);
    }

    public ListBucketVersionsRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    public void setPrefix(String prefix){
        if(prefix != null){
            this.prefix = prefix;
        }
    }

    public void setKeyMarker(String keyMarker){
        if(keyMarker != null){
            this.keyMarker = keyMarker;
        }
    }

    public void setVersionIdMarker(String versionIdMarker){
        if(versionIdMarker != null){
            this.versionIdMarker = versionIdMarker;
        }
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public void setMaxKeys(int maxKeys) {
        this.maxKeys = String.valueOf(maxKeys);
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("versions", null);
        if(prefix != null){
            queryParameters.put("prefix", prefix);
        }
        if(keyMarker != null){
            queryParameters.put("key-marker", keyMarker);
        }
        if(versionIdMarker != null){
            queryParameters.put("version-id-marker", versionIdMarker);
        }
        if(delimiter != null){
            queryParameters.put("delimiter", delimiter);
        }
        if(encodingType != null){
            queryParameters.put("encoding-type", encodingType);
        }
        if(!maxKeys.equals("1000")){
            queryParameters.put("max-keys", maxKeys);
        }
        return super.getQueryString();
    }
}
