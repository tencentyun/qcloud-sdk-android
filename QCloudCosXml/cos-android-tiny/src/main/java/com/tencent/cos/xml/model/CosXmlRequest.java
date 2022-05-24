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
import com.tencent.qcloud.core.auth.COSXmlSignSourceProvider;
import com.tencent.qcloud.core.auth.QCloudSignSourceProvider;
import com.tencent.qcloud.core.common.QCloudTaskStateListener;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpTask;
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

public abstract class  CosXmlRequest{

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
    public boolean isNeedMD5 = false;
    public boolean isSupportAccelerate = false;

    public int priority = -1;
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
    public String region;

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
    public void checkParameters() throws CosXmlClientException {

    }

    /**
     * 添加请求http header
     * @param key 键
     * @param value 值
     * @throws CosXmlClientException 客户端异常
     */
    public void setRequestHeaders(String key, String value) {
        addHeader(key, value);
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

    /**
     * 获取请求host
     * @param config SDK服务配置
     * @return 请求host
     */
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, bucket, isSupportAccelerate);
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
     * 设置执行任务
     * @param httpTask 执行任务
     */
    public void setTask(HttpTask httpTask){
        this.httpTask = httpTask;
        httpTask.addStateListener(qCloudTaskStateListener);
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
     * header是否有不安全的非Ascii字符
     * @return header是否有不安全的非Ascii字符
     */
    public boolean headersHasUnsafeNonAscii(){
        return false;
    }
}
