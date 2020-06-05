package com.tencent.cos.xml.model.bucket;

import android.text.TextUtils;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

public class GetBucketObjectVersionsRequest extends BucketRequest {

    private String prefix;

    private String delimiter;

    private String encodingType;

    private String keyMarker;

    private String versionIdMarker;

    private int maxKeys = 1000;

    public GetBucketObjectVersionsRequest(String bucket) {
        super(bucket);
    }

    public GetBucketObjectVersionsRequest(String bucket, String prefix, String delimiter, String keyMarker, String versionIdMarker) {
        super(bucket);
        this.prefix = prefix;
        this.delimiter = delimiter;
        this.keyMarker = keyMarker;
        this.versionIdMarker = versionIdMarker;
    }

    public GetBucketObjectVersionsRequest(String bucket, String delimiter, String keyMarker, String versionIdMarker) {
        this(bucket, "", delimiter, keyMarker, versionIdMarker);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("versions", null);
        addQuery("prefix", prefix);
        addQuery("delimiter", delimiter);
        addQuery("encoding-type", encodingType);
        addQuery("key-marker", keyMarker);
        addQuery("version-id-marker", versionIdMarker);
        addQuery("max-keys", String.valueOf(maxKeys));

        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }


    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public void setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
    }

    public void setVersionIdMarker(String versionIdMarker) {
        this.versionIdMarker = versionIdMarker;
    }

    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }

    private void addQuery(String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            queryParameters.put(key, value);
        }
    }
}
