/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  COS请求基类
 * </p>
 */

public abstract class CosXmlRequest{

    /**
     * url query集合
     */
    protected Map<String, String> queryParameters = new LinkedHashMap<>();
    private String queryParameterEncodedString;
    /**
     * 请求http header集合
     */
    protected Map<String, List<String>> requestHeaders = new LinkedHashMap<>();
    /**
     * 不强制签名的header集合
     */
    protected List<String> noSignHeaders = new LinkedList<>();
    /**
     * 签名原料提供器
     */
    protected QCloudSignSourceProvider signSourceProvider;
    private HttpTask httpTask;
    private HttpTaskMetrics metrics;
    private boolean isNeedMD5 = false;
    private boolean isSupportAccelerate = false;

    protected int priority = -1;
    /**
     * 存储桶
     */
    protected String bucket;
    /**
     * 请求URL
     */
    protected String requestURL;
    /**
     * 地域
     */
    protected String region;

    /**
     * 是否将签名信息以 paras 的方式放在 Url 中
     */
    private boolean signInUrl;

    private OnRequestWeightListener onRequestWeightListener;

    /**
     * 任务状态监听器
     */
    protected QCloudTaskStateListener qCloudTaskStateListener;

    /**
     * 设置请求URL
     * @param requestURL 请求URL
     */
    public void setRequestURL(String requestURL){
        this.requestURL = requestURL;
    }

    /**
     * 获取请求URL
     * @return 请求URL
     */
    public String getRequestURL(){
        return requestURL;
    }

    /**
     * 获取HTTP请求方法 {@link com.tencent.cos.xml.common.RequestMethod}
     * @return HTTP请求方法
     */
    public abstract String getMethod();

    /**
     * 获取HTTP URL Path
     * @param config SDK服务配置
     * @return HTTP URL Path
     */
    public abstract String getPath(CosXmlServiceConfig config);

    /**
     * 获取存储桶名
     * @return 存储桶名
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * 设置url query集合
     * @param queryParameters url query集合
     */
    public void setQueryParameters(java.util.Map<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    /**
     * 设置 query 字符串
     * @param queryParameterString
     */
    public void setQueryEncodedString(String queryParameterString) {
        this.queryParameterEncodedString = queryParameterString;
    }

    /**
     * 设置请求http header集合
     * @param headers 请求http header集合
     */
    public void setRequestHeaders(Map<String, List<String>> headers){
        if(headers != null){
            this.requestHeaders.putAll(headers);
        }
    }

    /**
     * 添加不强制签名的header键
     * @param key 不强制签名的header键
     */
    public void addNoSignHeader(String key) {
        if (!TextUtils.isEmpty(key)) {
            noSignHeaders.add(key);
        }
    }

    /**
     * 获取不强制签名的header集合
     * @return 不强制签名的header集合
     */
    public List<String> getNoSignHeaders() {
        return noSignHeaders;
    }

    /**
     * 获取url query集合
     * @return url query集合
     */
    public Map<String, String> getQueryString(){
        return queryParameters;
    }

    public String getQueryEncodedString(){
        return queryParameterEncodedString;
    }

    /**
     * 获取请求http header集合
     * @return 请求http header集合
     */
    public Map<String, List<String>> getRequestHeaders(){
        return requestHeaders;
    }

    /**
     * 获取请求体
     * @return 请求体
     * @throws CosXmlClientException 客户异常
     */
    public abstract RequestBodySerializer getRequestBody() throws CosXmlClientException;

    /**
     * sdk 参数校验
     * @throws CosXmlClientException cosXmlClientException
     */
    public abstract void checkParameters() throws CosXmlClientException;

    /**
     * 针对有请求体的 PUT 和 POST 请求，此头部是必选项。
     * 针对 GET、HEAD、DELETE 和 OPTIONS 请求，不能指定此头部。
     * RFC 1864中定义的请求体内容的16字节二进制 MD5 哈希值的 Base64 编码形式，用于完整性检查，
     * 验证请求体在传输过程中是否发生变化，最终的取值长度应为24个字符，请注意在编写代码时使用正确的方法和参数，例如ZzD3iDJdrMAAb00lgLLeig==
     */
    public boolean isNeedMD5(){
        return isNeedMD5;
    }

    /**
     * 针对有请求体的 PUT 和 POST 请求，此头部是必选项。
     * 针对 GET、HEAD、DELETE 和 OPTIONS 请求，不能指定此头部。
     * RFC 1864中定义的请求体内容的16字节二进制 MD5 哈希值的 Base64 编码形式，用于完整性检查，
     * 验证请求体在传输过程中是否发生变化，最终的取值长度应为24个字符，请注意在编写代码时使用正确的方法和参数，例如ZzD3iDJdrMAAb00lgLLeig==
     */
    public void setNeedMD5(boolean isNeedMD5){
        this.isNeedMD5 = isNeedMD5;
    }

    /**
     * 设置任务状态监听器
     * @param qCloudTaskStateListener 任务状态监听器
     */
    public void setTaskStateListener(QCloudTaskStateListener qCloudTaskStateListener){
        this.qCloudTaskStateListener = qCloudTaskStateListener;
    }

    /**
     * 获取请求性能参数捕获器
     * @return 请求性能参数捕获器
     */
    public HttpTaskMetrics getMetrics() {
        return metrics;
    }

    /**
     * 设置请求性能参数捕获器
     *
     * @param metrics 请求性能参数捕获器
     */
    public void attachMetrics(HttpTaskMetrics metrics) {
        this.metrics = metrics;
    }

    public void setSignInUrl(boolean signInUrl) {
        this.signInUrl = signInUrl;
    }

    public boolean isSignInUrl() {
        return signInUrl;
    }

    @Deprecated
    public void setRequestHeaders(String key, String value) throws CosXmlClientException {
        if(key != null && value != null){
            value = URLEncodeUtils.cosPathEncode(value);
            addHeader(key, value);
        }
    }

    /**
     * 添加请求http header
     * @param key 键
     * @param value 值
     * @param isUrlEncoder 是否进行URL编码
     * @throws CosXmlClientException 客户端异常
     */
    public void setRequestHeaders(String key, String value, boolean isUrlEncoder) throws CosXmlClientException {
        if(key != null && value != null){
            if(isUrlEncoder){
                value = URLEncodeUtils.cosPathEncode(value);
            }
            addHeader(key, value);
        }
    }

    /**
     * 添加请求http header
     * @param key 键
     * @param value 值
     */
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

    /**
     * 获取请求host
     * @param config SDK服务配置
     * @return 请求host
     */
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, bucket, isSupportAccelerate);
    }


    /**
     * 是否启用全球加速
     * @param isSupportAccelerate 是否启用全球加速
     */
    public void isSupportAccelerate(boolean isSupportAccelerate){
        this.isSupportAccelerate = isSupportAccelerate;
    }

    /**
     * 是否启用全球加速
     * @return 是否启用全球加速
     */
    public boolean isSupportAccelerate() {
        return isSupportAccelerate;
    }

    /**
     * 设置签名Authorization
     * @param sign Authorization
     */
    public void setSign(String sign){
        addHeader(HttpConstants.Header.AUTHORIZATION, sign);
    }

    /**
     * 获取签名原料提供器
     * @return 签名原料提供器
     */
    public QCloudSignSourceProvider getSignSourceProvider() {
        if(signSourceProvider == null){
            signSourceProvider = new COSXmlSignSourceProvider();
        }
        return signSourceProvider;
    }

    /**
     * 设置签名原料提供器
     * @param cosXmlSignSourceProvider 签名原料提供器
     */
    public void setSignSourceProvider(QCloudSignSourceProvider cosXmlSignSourceProvider){
        this.signSourceProvider = cosXmlSignSourceProvider;
    }

    /**
     * 获取STS范围限制信息
     * @param config SDK服务配置
     * @return STS范围限制信息
     */
    public STSCredentialScope[] getSTSCredentialScope(CosXmlServiceConfig config) {
        String action = "name/cos:" + getClass().getSimpleName().replace("Request", "");
        STSCredentialScope scope = new STSCredentialScope(action, config.getBucket(bucket),
                config.getRegion(), getPath(config));
        return scope.toArray();
    }

    /**
     * 签名有效时间跟随密钥有效时间，无需设置，如果需要设置签名的参数字段和头部字段，
     * 请调用 {@link #setSignParamsAndHeaders(Set, Set)} 方法。
     */
    @Deprecated
    public void setSign(long signDuration){
    }

    /**
     * 签名有效时间跟随密钥有效时间，无需设置，如果需要设置签名的参数字段和头部字段，
     * 请调用 {@link #setSignParamsAndHeaders(Set, Set)} 方法。
     */
    @Deprecated
    public void setSign(long startTime, long endTime){
    }

    /**
     * 签名有效时间跟随密钥有效时间，无需设置，如果需要设置签名的参数字段和头部字段，
     * 请调用 {@link #setSignParamsAndHeaders(Set, Set)} 方法代替。
     */
    @Deprecated
    public void setSign(long signDuration, Set<String> parameters, Set<String> headers){
        setSignParamsAndHeaders(parameters, headers);
    }

    /**
     * 签名有效时间跟随密钥有效时间，无需设置，如果需要设置签名的参数字段和头部字段，
     * 请调用 {@link #setSignParamsAndHeaders(Set, Set)} 方法代替。
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

    /**
     * 设置地域
     * @param region 地域
     */
    public void setRegion(String region){
        if(!TextUtils.isEmpty(region)){
            this.region = region;
        }
    }

    /**
     * 获取地域
     * @return 地域
     */
    public String getRegion(){
        return region;
    }

    /**
     * 设置执行任务
     * @param httpTask 执行任务
     */
    public void setTask(HttpTask httpTask){
        this.httpTask = httpTask;
        httpTask.addStateListener(qCloudTaskStateListener);
        httpTask.attachMetric(metrics);
    }

    /**
     * 获取执行任务
     * @return 执行任务
     */
    public HttpTask getHttpTask(){
        return httpTask;
    }

    /**
     * 获取请求优先级<br>
     */
    public int getPriority() {
       return priority;
    }

    /**
     * 获取请求的重量级
     * @return 请求的重量级
     */
    public int getWeight() {
        return onRequestWeightListener != null ? onRequestWeightListener.onWeight() :
                QCloudTask.WEIGHT_LOW;
    }

    /**
     * 设置获取请求重量级的监听接口
     * @param onRequestWeightListener 获取请求重量级的监听接口
     */
    public void setOnRequestWeightListener(OnRequestWeightListener onRequestWeightListener) {
        this.onRequestWeightListener = onRequestWeightListener;
    }

    /**
     * 获取请求重量级的监听接口
     */
    public interface OnRequestWeightListener {
        int onWeight();
    }
}
