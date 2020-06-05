package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * GetBucket 请求用于列出该 Bucket 下的部分或者全部 Object。
 * </p>
 *
 */
final public class GetBucketRequest extends BucketRequest {
    /** Prefix match, used to specify the prefix address of the returned file */
    private String prefix = null;

    /** Delimiter is a sign，
     *  If Prefix exists, the same paths between Prefix and delimiter will be grouped as the same
     *  type and defined Common Prefix, then all Common Prefixes will be listed.
     *  If Prefix doesn't exist, the listing process will start from the beginning of the path
     */
    private String delimiter = null;

    /** Specify the encoding method of the returned value */
    private String encodingType;

    /** Entries are listed using UTF-8 binary order by default, starting from the marker */
    private String marker = null;

    /** Max number of entries returned each time, default is 1000 */
    private String maxKeys = "1000";

    public GetBucketRequest(String bucket){
        super(bucket);
    }

    public GetBucketRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {

        if(prefix != null){
            queryParameters.put("prefix",prefix);
        }
        if(delimiter != null){
            queryParameters.put("delimiter",delimiter);
        }
        if(encodingType != null){
            queryParameters.put("encoding-type",encodingType);
        }
        if(marker != null){
            queryParameters.put("marker",marker);
        }
        if(maxKeys != null){
            queryParameters.put("max-keys",maxKeys);
        }
        if(prefix != null){
            queryParameters.put("prefix",prefix);
        }

        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

    /**
     * 设置匹配前缀，用来规定返回的文件前缀地址。
     *
     * @param prefix 前缀
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 获取请求的匹配前缀。
     *
     * @return 请求前缀
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * <p>
     * 设置定界符 delimiter
     * </p>
     * <p>
     * 定界符为一个符号，如果有 Prefix，则将 Prefix 到 delimiter 之间的相同路径归为一类，定义为 Common Prefix，然后列出所有 Common Prefix。如果没有 Prefix，则从路径起点开始。
     * </p>
     * @see #setDelimiter(String)
     * @param delimiter
     */
    @Deprecated
    public void setDelimiter(char delimiter) {
        setDelimiter(String.valueOf(delimiter));
    }

    public void setDelimiter(String delimiter){
        this.delimiter = delimiter;
    }

    /**
     * 获取请求的定界符
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * 设置返回值的编码方式，可选值：url
     *
     * @param encodingType
     */
    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    /**
     * 获取返回值的编码类型。
     */
    public String getEncodingType() {
        return encodingType;
    }

    /**
     * <p>
     * 设置列出文件的marker
     * </p>
     * <p>
     * 默认以 UTF-8 二进制顺序列出条目，所有列出条目从marker开始
     * </p>
     *
     * @param marker
     */
    public void setMarker(String marker) {
        this.marker = marker;
    }

    /**
     * 获取请求的marker
     */
    public String getMarker() {
        return marker;
    }

    /**
     * 设置单次返回的最大条目数。默认为1000
     *
     * @param maxKeys
     */
    public void setMaxKeys(long maxKeys) {
        this.maxKeys = String.valueOf(maxKeys);
    }

    /**
     * 获取请求的最大条目数
     */
    public long getMaxKeys() {
        return Long.parseLong(maxKeys);
    }
}
