package com.tencent.cos.xml.model;

import android.text.TextUtils;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.utils.URLEncodeUtils;
import com.tencent.qcloud.core.auth.COSXmlSignSourceProvider;
import com.tencent.qcloud.core.auth.QCloudSignSourceProvider;
import com.tencent.qcloud.core.auth.STSCredentialScope;
import com.tencent.qcloud.core.common.QCloudTaskStateListener;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.task.QCloudTask;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bradyxiao on 2017/11/30.
 * <p>
 *  请求基类
 * </p>
 * @see com.tencent.cos.xml.model.object.PutObjectRequest
 * @see com.tencent.cos.xml.model.object.InitMultipartUploadRequest
 * @see com.tencent.cos.xml.model.object.UploadPartResult
 * @see com.tencent.cos.xml.model.object.CompleteMultiUploadRequest
 * @see com.tencent.cos.xml.model.object.ListPartsRequest
 * @see com.tencent.cos.xml.model.object.AbortMultiUploadRequest
 * ...
 */

public abstract class CosXmlRequest{

    protected Map<String, String> queryParameters = new LinkedHashMap<>();
    protected Map<String, List<String>> requestHeaders = new LinkedHashMap<>();
    protected QCloudSignSourceProvider signSourceProvider;
    private HttpTask httpTask;
    private HttpTaskMetrics metrics;
    private boolean isNeedMD5 = false;
    private boolean isSupportAccelerate = false;
    protected String bucket;
    protected String requestURL;
    protected String region;

    private OnRequestWeightListener onRequestWeightListener;

    protected QCloudTaskStateListener qCloudTaskStateListener;

    public void setRequestURL(String requestURL){
        this.requestURL = requestURL;
    }

    public String getRequestURL(){
        return requestURL;
    }

    public abstract String getMethod();

    public abstract String getPath(CosXmlServiceConfig config);

    public String getBucket() {
        return bucket;
    }

    public void setQueryParameters(java.util.Map<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public void setRequestHeaders(Map<String, List<String>> headers){
        if(headers != null){
            this.requestHeaders.putAll(headers);
        }
    }

    public Map<String, String> getQueryString(){
        return queryParameters;
    }

    public Map<String, List<String>> getRequestHeaders(){
        return requestHeaders;
    }

    public abstract RequestBodySerializer getRequestBody() throws CosXmlClientException;

    /**
     * sdk 参数校验
     * @throws CosXmlClientException
     */
    public abstract void checkParameters() throws CosXmlClientException;

    public boolean isNeedMD5(){
        return isNeedMD5;
    }

    public void setNeedMD5(boolean isNeedMD5){
        this.isNeedMD5 = isNeedMD5;
    }

    public void setTaskStateListener(QCloudTaskStateListener qCloudTaskStateListener){
        this.qCloudTaskStateListener = qCloudTaskStateListener;
    }

    public HttpTaskMetrics getMetrics() {
        return metrics;
    }

    /**
     * 设置请求性能参数捕获器
     *
     * @param metrics
     */
    public void attachMetrics(HttpTaskMetrics metrics) {
        this.metrics = metrics;
    }

    /**
     *  @see CosXmlRequest#setRequestHeaders(String, String, boolean)
     * @param key
     * @param value
     * @throws CosXmlClientException
     */
    @Deprecated
    public void setRequestHeaders(String key, String value) throws CosXmlClientException {
        if(key != null && value != null){
            value = URLEncodeUtils.cosPathEncode(value);
            addHeader(key, value);
        }
    }

    /**
     * set header value, and need to urlEncoder or not.
     * @param key
     * @param value
     * @param isUrlEncoder true, it need url encoder,otherwise, do not need
     */
    public void setRequestHeaders(String key, String value, boolean isUrlEncoder) throws CosXmlClientException {
        if(key != null && value != null){
            if(isUrlEncoder){
                value = URLEncodeUtils.cosPathEncode(value);
            }
            addHeader(key, value);

        }
    }

    protected void addHeader(String key, String value){
        List<String> values;
        if(requestHeaders.containsKey(key)){
            values = requestHeaders.get(key);
        }else {
            values = new ArrayList<String>();
        }
        values.add(value);
        requestHeaders.put(key, values);
    }

//    public String getHost(CosXmlServiceConfig config, boolean isSupportAccelerate) throws CosXmlClientException {
//        return getHost(config, isSupportAccelerate, false);
//    }
//
//    public String getHost(CosXmlServiceConfig config, boolean isSupportAccelerate, boolean isHeader) throws CosXmlClientException{
//        if (TextUtils.isEmpty(bucket)) {
//            throw new IllegalArgumentException("bucket is null");
//        }
//        return config.getHost(bucket, region, isSupportAccelerate, isHeader);
//    }

    public String getRequestHost(CosXmlServiceConfig config) {

        return config.getRequestHost(region, bucket, isSupportAccelerate);
    }


    public void isSupportAccelerate(boolean isSupportAccelerate){
        this.isSupportAccelerate = isSupportAccelerate;
    }

    public boolean isSupportAccelerate() {
        return isSupportAccelerate;
    }

    public void setSign(String sign){
        addHeader(HttpConstants.Header.AUTHORIZATION, sign);
    }

    public QCloudSignSourceProvider getSignSourceProvider() {
        if(signSourceProvider == null){
            signSourceProvider = new COSXmlSignSourceProvider();
        }
        return signSourceProvider;
    }

    public void setSignSourceProvider(QCloudSignSourceProvider cosXmlSignSourceProvider){
        this.signSourceProvider = cosXmlSignSourceProvider;
    }

    public STSCredentialScope[] getSTSCredentialScope(CosXmlServiceConfig config) {
        String action = "name/cos:" + getClass().getSimpleName().replace("Request", "");
        STSCredentialScope scope = new STSCredentialScope(action, config.getBucket(bucket),
                config.getRegion(), getPath(config));
        return scope.toArray();
    }

    /**
     * @deprecated 签名有效时间跟随密钥有效时间，无需设置，如果需要设置签名的参数字段和头部字段，
     * 请调用 {@link #setSignParamsAndHeaders(Set, Set)} 方法。
     *
     * @param signDuration
     */
    @Deprecated
    public void setSign(long signDuration){
    }

    /**
     * @deprecated 签名有效时间跟随密钥有效时间，无需设置，如果需要设置签名的参数字段和头部字段，
     * 请调用 {@link #setSignParamsAndHeaders(Set, Set)} 方法。
     *
     * @param startTime
     * @param endTime
     */
    @Deprecated
    public void setSign(long startTime, long endTime){
    }

    /**
     * @deprecated 签名有效时间跟随密钥有效时间，无需设置，如果需要设置签名的参数字段和头部字段，
     * 请调用 {@link #setSignParamsAndHeaders(Set, Set)} 方法代替。
     *
     * @param signDuration
     * @param parameters
     * @param headers
     */
    @Deprecated
    public void setSign(long signDuration, Set<String> parameters, Set<String> headers){
        setSignParamsAndHeaders(parameters, headers);
    }

    /**
     * @deprecated 签名有效时间跟随密钥有效时间，无需设置，如果需要设置签名的参数字段和头部字段，
     * 请调用 {@link #setSignParamsAndHeaders(Set, Set)} 方法代替。
     *
     * @param startTime
     * @param endTime
     * @param parameters
     * @param headers
     */
    @Deprecated
    public void setSign(long startTime, long endTime, Set<String> parameters, Set<String> headers){
        setSignParamsAndHeaders(parameters, headers);
    }

    /**
     * 设置参与签名的参数字段和头部字段
     *
     * @param parameters 参与签名的参数字段
     * @param headers 参与签名的头部字段
     */
    public void setSignParamsAndHeaders(Set<String> parameters, Set<String> headers) {
        COSXmlSignSourceProvider cosXmlSignSourceProvider = new COSXmlSignSourceProvider();
        cosXmlSignSourceProvider.parameters(parameters);
        cosXmlSignSourceProvider.headers(headers);
        signSourceProvider = cosXmlSignSourceProvider;
    }

    public void setRegion(String region){
        if(!TextUtils.isEmpty(region)){
            this.region = region;
        }
    }

    public String getRegion(){
        return region;
    }

    public void setTask(HttpTask httpTask){
        this.httpTask = httpTask;
        httpTask.addStateListener(qCloudTaskStateListener);
        httpTask.attachMetric(metrics);
    }

    public HttpTask getHttpTask(){
        return httpTask;
    }
    public int getPriority() {
       return -1;
    }

    public int getWeight() {
        return onRequestWeightListener != null ? onRequestWeightListener.onWeight() :
                QCloudTask.WEIGHT_LOW;
    }

    public void setOnRequestWeightListener(OnRequestWeightListener onRequestWeightListener) {
        this.onRequestWeightListener = onRequestWeightListener;
    }

    public interface OnRequestWeightListener {
        int onWeight();
    }
}
