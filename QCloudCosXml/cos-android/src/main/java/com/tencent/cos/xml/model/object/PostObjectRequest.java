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

package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.utils.DateUtils;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.qcloud.core.auth.COSXmlSignSourceProvider;
import com.tencent.qcloud.core.auth.QCloudCredentials;
import com.tencent.qcloud.core.auth.QCloudSignSourceProvider;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.MultipartStreamRequestBody;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.util.QCloudHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 使用表单请求上传对象的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#postObject(PostObjectRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#postObjectAsync(PostObjectRequest, CosXmlResultListener)
 */
public class PostObjectRequest extends ObjectRequest implements TransferRequest {

    private FormStruct formStruct = new FormStruct();
    private CosXmlProgressListener progressListener;
    private long offset = 0;
    private long contentLength = -1;

    private PostObjectRequest(String bucket, String cosPath) {
        super(bucket, "/");
        formStruct.key = cosPath;
    }

    public PostObjectRequest(String bucket, String cosPath, String srcPath) {
        this(bucket, cosPath);
        formStruct.srcPath = srcPath;
    }

    public PostObjectRequest(String bucket, String cosPath, byte[] data) {
        this(bucket, cosPath);
        formStruct.data = data;
    }

    public PostObjectRequest(String bucket, String cosPath, InputStream inputStream) {
        this(bucket, cosPath);
        formStruct.inputStream = inputStream;
    }

    /**
     * 设置文件长度范围
     */
    public void setRange(long offset, long contentSize) {
        this.offset = offset;
        this.contentLength = contentSize;
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        MultipartStreamRequestBody multipartStreamRequestBody = new MultipartStreamRequestBody();
        multipartStreamRequestBody.setBodyParameters(formStruct.getFormParameters());
        if(formStruct.srcPath != null){
           File file = new File(formStruct.srcPath);
           multipartStreamRequestBody.setContent(null, "file", file.getName(),
                   file, offset, contentLength);
           return RequestBodySerializer.multiPart(multipartStreamRequestBody);
        }else if(formStruct.data != null){
            multipartStreamRequestBody.setContent(null, "file", "data.txt",
                    formStruct.data, offset, contentLength);
            return RequestBodySerializer.multiPart(multipartStreamRequestBody);
        }else if(formStruct.inputStream != null){
            try {
                File tmpFile = new File(CosXmlSimpleService.appCachePath, String.valueOf(System.currentTimeMillis()));
                multipartStreamRequestBody.setContent(null, "file", tmpFile.getName(), tmpFile,
                        formStruct.inputStream, offset, contentLength);
                return RequestBodySerializer.multiPart(multipartStreamRequestBody);
            } catch (IOException e) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
            }
        }
        return null;
    }

    @Override
    public void setSignParamsAndHeaders(Set<String> parameters, Set<String> headers) {
        PostCosXmlSignSourceProvider cosXmlSignSourceProvider = new PostCosXmlSignSourceProvider();
        cosXmlSignSourceProvider.parameters(parameters);
        cosXmlSignSourceProvider.headers(headers);
        cosXmlSignSourceProvider.setHeaderPairsForSign(QCloudHttpUtils.transformToMultiMap(
                formStruct.getFormParameters()));
        setSignSourceProvider(cosXmlSignSourceProvider);
    }

    @Override
    public QCloudSignSourceProvider getSignSourceProvider() {
        if(signSourceProvider == null){
            signSourceProvider = new PostCosXmlSignSourceProvider();
            ((COSXmlSignSourceProvider)signSourceProvider).setHeaderPairsForSign(QCloudHttpUtils.transformToMultiMap(
                    formStruct.getFormParameters()));
        }
        return signSourceProvider;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        formStruct.checkParameters();
    }

    /**
     * 设置上传进度回调监听
     * @param progressListener 进度监听器 {@link CosXmlProgressListener}
     */
    public void setProgressListener(CosXmlProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    /**
     * 获取上传进度回调监听
     * @return 上传进度回调监听
     */
    public CosXmlProgressListener getProgressListener() {
        return progressListener;
    }

    /**
     * 定义对象的访问控制列表（ACL）属性。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/30752#.E9.A2.84.E8.AE.BE.E7.9A.84-acl">ACL 概述</a> 文档中对象的预设 ACL 部分，例如 default，private，public-read 等，默认为 default
     * @param acl COS 访问权限
     */
    public void setAcl(String acl) {
        formStruct.acl = acl;
    }

    /**
     * <p>
     * 设置Cache-Control头部
     * </p>
     * @param cacheControl Cache-Control头部
     */
    public void setCacheControl(String cacheControl) {
        formStruct.headers.put("Cache-Control", cacheControl);
    }

    /**
     * <p>
     * 设置Content-Type头部
     * </p>
     * @param contentType Content-Type头部
     */
    public void setContentType(String contentType) {
        formStruct.headers.put("Content-Type", contentType);
    }

    /**
     * <p>
     * 设置Content-Disposition头部
     * </p>
     * @param contentDisposition Content-Disposition头部
     */
    public void setContentDisposition(String contentDisposition) {
        formStruct.headers.put("Content-Disposition", contentDisposition);
    }

    /**
     * <p>
     * 设置Content-Encoding头部
     * </p>
     * @param contentEncoding Content-Encoding头部
     */
    public void setContentEncoding(String contentEncoding) {
        formStruct.headers.put("Content-Encoding", contentEncoding);
    }

    /**
     * <p>
     * 设置Expires头部
     * </p>
     * @param expires Expires头部
     */
    public void setExpires(String expires) {
        formStruct.headers.put("Expires", expires);
    }

    /**
     * 设置对象的存储类型。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/33417">存储类型</a> 文档，例如 STANDARD_IA，ARCHIVE。默认值：STANDARD
     * @param stroageClass COS存储类型
     */
    public void setStroageClass(COSStorageClass stroageClass)
    {
        formStruct.headers.put(COSRequestHeaderKey.X_COS_STORAGE_CLASS_, stroageClass.getStorageClass());
    }

    /**
     * 设置HTTP头部
     * @param key 键
     * @param value 值
     */
    public void setHeader(String key, String value) {
        if (key != null && value != null) {
            formStruct.headers.put(key, value);
        }
    }

    /**
     * 设置from表单内容
     * @param key 键
     * @param value 值
     */
    public void setCustomerHeader(String key, String value) {
        if (key != null && value != null) {
            formStruct.customHeaders.put(key, value);
        }
    }

    /**
     * 设置对象的存储类型。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/33417">存储类型</a> 文档，例如 STANDARD_IA，ARCHIVE。默认值：STANDARD
     * @param cosStorageClass COS存储类型
     */
    public void setCosStorageClass(String cosStorageClass) {
        formStruct.xCosStorageClass = cosStorageClass;
    }

    /**
     * 设置上传成功时重定向的目标 URL 地址
     * @param redirectHost 上传成功时重定向的目标 URL 地址
     */
    public void setSuccessActionRedirect(String redirectHost) {
        formStruct.successActionRedirect = redirectHost;
    }

    /**
     * 设置上传成功时返回的 HTTP 状态码
     * @param successHttpCode 上传成功时返回的 HTTP 状态码
     */
    public void setSuccessActionStatus(int successHttpCode) {
        formStruct.successActionStatus = String.valueOf(successHttpCode);
    }

    /**
     * 设置策略
     * @param policy 策略
     */
    public void setPolicy(Policy policy) {
        formStruct.policy = policy;
    }

    @Override
    public void setTrafficLimit(long limit) {
        addHeader("x-cos-traffic-limit", String.valueOf(limit));
    }

    private static class PostCosXmlSignSourceProvider extends COSXmlSignSourceProvider {
        @Override
        public <T> void onSignRequestSuccess(HttpRequest<T> request, QCloudCredentials credentials, String authorization) {
            super.onSignRequestSuccess(request, credentials, authorization);
            MultipartStreamRequestBody requestBody = (MultipartStreamRequestBody) request.getRequestBody();
            requestBody.setSign(authorization);
            request.removeHeader("Authorization");
        }
    }

    // for unit test
    Map<String, String> testFormParameters() throws CosXmlClientException {
        return formStruct.getFormParameters();
    }

    private class FormStruct {
        String acl;
        Map<String, String> headers;
        String key;
        String successActionRedirect;
        String successActionStatus;
        Map<String, String> customHeaders;
        String xCosStorageClass;
        Policy policy;
        String srcPath;
        byte[] data;
        InputStream inputStream;

        public FormStruct() {
            headers = new LinkedHashMap<>();
            customHeaders = new LinkedHashMap<>();
        }

        public Map<String, String> getFormParameters() {
            Map<String, String> formParameters = new LinkedHashMap<>();
            if (acl != null) {
                formParameters.put("acl", acl);
            }
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                formParameters.put(entry.getKey(), entry.getValue());
            }
            formParameters.put("key", key);
            if (successActionRedirect != null) {
                formParameters.put("success_action_redirect", successActionRedirect);
            }
            if (successActionStatus != null) {
                formParameters.put("success_action_status", successActionStatus);
            }
            else {
                formParameters.put("success_action_status", "204");
            }
            for (Map.Entry<String, String> entry : customHeaders.entrySet()) {
                formParameters.put(entry.getKey(), entry.getValue());
            }
            if (xCosStorageClass != null) {
                formParameters.put("x-cos-storage-class", xCosStorageClass);
            }
            if (policy != null) {
                try {
                    formParameters.put("policy", DigestUtils.getBase64(policy.content()));
                } catch (CosXmlClientException ignore){}
            }
            return formParameters;
        }

        public void checkParameters() throws CosXmlClientException{
            if(formStruct.key == null){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "cosPath = null ");
            }
            if(srcPath == null && data == null && inputStream == null){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "data souce = null");
            }
            if(srcPath != null){
                File file = new File(srcPath);
                if(!file.exists() || !file.isFile()){
                    throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "srcPath is invalid");
                }
            }
        }
    }

    /**
     * 策略
     * <p>
     * 详情请参考：<a href="https://cloud.tencent.com/document/product/436/14690">POST Object</a>中的 构造“策略”（Policy）
     */
    public static class Policy {
        private String expiration;
        private JSONArray conditions = new JSONArray();

        public void setExpiration(long endTimeMills) {
            this.expiration = DateUtils.getFormatTime("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", endTimeMills);
        }

        public void setExpiration(String formatEndTime) {
            this.expiration = formatEndTime;
        }

        public void addConditions(String key, String value, boolean isPrefixMatch) throws CosXmlClientException {
            if (isPrefixMatch) {
                JSONArray content = new JSONArray();
                content.put("starts-with");
                content.put(key);
                content.put(value);
                this.conditions.put(content);
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(key, value);
                } catch (JSONException e) {
                    throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
                }
                this.conditions.put(jsonObject);
            }
        }

        public void addContentConditions(int start, int end) {
            JSONArray content = new JSONArray();
            content.put("content-length-range");
            content.put(start);
            content.put(end);
            this.conditions.put(content);
        }

        public String content() {
            JSONObject jsonObject = new JSONObject();
            try {
                if (expiration != null) {
                    jsonObject.put("expiration", expiration);
                }
                jsonObject.put("conditions", conditions);
                return jsonObject.toString();
            } catch (JSONException ignore) {}
            return null;
        }
    }
}
