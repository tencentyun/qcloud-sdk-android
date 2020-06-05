package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
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
 * Created by bradyxiao on 2018/6/11.
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

    public PostObjectRequest() {
        super(null, null);
    }

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
                if(!tmpFile.exists() && tmpFile.createNewFile()){
                    multipartStreamRequestBody.setContent(null, "file", tmpFile.getName(), tmpFile,
                            formStruct.inputStream, offset, contentLength);
                    return RequestBodySerializer.multiPart(multipartStreamRequestBody);
                }
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
     * 上传进度回调
     *
     * @param progressListener 进度监听器 {@link CosXmlProgressListener}
     */
    public void setProgressListener(CosXmlProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public CosXmlProgressListener getProgressListener() {
        return progressListener;
    }

    public void setAcl(String acl) {
        formStruct.acl = acl;
    }

    public void setCacheControl(String cacheControl) {
        formStruct.headers.put("Cache-Control", cacheControl);
    }

    public void setContentType(String contentType) {
        formStruct.headers.put("Content-Type", contentType);
    }

    public void setContentDisposition(String contentDisposition) {
        formStruct.headers.put("Content-Disposition", contentDisposition);
    }

    public void setContentEncoding(String contentEncoding) {
        formStruct.headers.put("Content-Encoding", contentEncoding);
    }

    public void setExpires(String expires) {
        formStruct.headers.put("Expires", expires);
    }

    /**
     * 设置 存储对象类别
     * @see COSStorageClass
     * @param stroageClass
     */
    public void setStroageClass(COSStorageClass stroageClass)
    {
        formStruct.headers.put(COSRequestHeaderKey.X_COS_STORAGE_CLASS_, stroageClass.getStorageClass());
    }

    public void setHeader(String key, String value) {
        if (key != null && value != null) {
            formStruct.headers.put(key, value);
        }
    }

    public void setCustomerHeader(String key, String value) {
        if (key != null && value != null) {
            formStruct.customHeaders.put(key, value);
        }
    }

    public void setCosStorageClass(String cosStorageClass) {
        formStruct.xCosStorageClass = cosStorageClass;
    }

    /**
     * the host you want to redirect
     *
     * @param redirectHost
     */
    public void setSuccessActionRedirect(String redirectHost) {
        formStruct.successActionRedirect = redirectHost;
    }

    /**
     * successHttpCode can be 200, 201, 204, default value 204
     *
     * @param successHttpCode
     */
    public void setSuccessActionStatus(int successHttpCode) {
        formStruct.successActionStatus = String.valueOf(successHttpCode);
    }

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
