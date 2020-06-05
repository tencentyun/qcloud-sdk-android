package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * <p>
 * Object 跨域访问配置的预请求。
 * </p>
 * <p>
 * 即在发送跨域请求之前会发送一个 OPTIONS 请求并带上特定的来源域，HTTP 方法和 HEADER 信息等给 COS，以决定是否可以发送真正的跨域请求。
 * 当 CORS 配置不存在时，请求返回 403 Forbidden。
 * </p>
 * <p>
 * 可以通过 PutBucketCORS 接口来开启 Bucket 的 CORS 支持。
 * </p>
 *
 */
final public class OptionObjectRequest extends ObjectRequest {
    private String origin;
    private String accessControlMethod;
    private String accessControlHeaders;
    public OptionObjectRequest(String bucket, String cosPath, String origin, String accessControlMethod){
        super(bucket, cosPath);
        this.origin = origin;
        this.accessControlMethod = accessControlMethod;
        setOrigin(origin);
        setAccessControlMethod(accessControlMethod);
    }

    public OptionObjectRequest(){
        super(null, null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.OPTIONS;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(origin == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "option request origin must not be null");
        }
        if(accessControlMethod == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "option request accessControlMethod must not be null");
        }
    }

    /**
     * <p>
     * 设置模拟跨域访问的请求来源域名。
     * </p>
     *
     * @param origin 请求来源域名
     */
    public void setOrigin(String origin) {
        this.origin = origin;
        if(origin != null){
            addHeader(COSRequestHeaderKey.ORIGIN,origin);
        }
    }

    /**
     * 获取设置的模拟跨域访问的请求来源域名。
     *
     * @return 请求来源域名
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * 设置模拟跨域访问的请求 HTTP 方法
     *
     * @param accessControlMethod 请求 HTTP 方法
     */
    public void setAccessControlMethod(String accessControlMethod) {
        if(accessControlMethod != null){
            this.accessControlMethod = accessControlMethod.toUpperCase();
            addHeader(COSRequestHeaderKey.ACCESS_CONTROL_REQUEST_METHOD,this.accessControlMethod);
        }
    }

    /**
     * 获取设置的模拟跨域访问的请求 HTTP 方法
     *
     * @return 请求 HTTP 方法
     */
    public String getAccessControlMethod() {
        return accessControlMethod;
    }

    /**
     * 设置模拟跨域访问的请求头部
     *
     * @param accessControlHeaders 模拟跨域访问的请求头部
     */
    public void setAccessControlHeaders(String accessControlHeaders) {
        this.accessControlHeaders = accessControlHeaders;
        if(accessControlHeaders != null){
            addHeader(COSRequestHeaderKey.ACCESS_CONTROL_REQUEST_HEADERS,accessControlHeaders);
        }
    }

    /**
     * 获取用户设置的模拟跨域访问的请求头部
     *
     * @return 模拟跨域访问的请求头部
     */
    public String getAccessControlHeaders() {
        return accessControlHeaders;
    }

}
