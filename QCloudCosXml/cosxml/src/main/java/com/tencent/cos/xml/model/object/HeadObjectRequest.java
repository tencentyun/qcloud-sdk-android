package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 获取对应 Object 的 meta 信息数据
 *
 */
final public class HeadObjectRequest extends ObjectRequest {

    public HeadObjectRequest(String bucket, String cosPath){
        super(bucket, cosPath);
    }

    public HeadObjectRequest(){
        super(null, null);
    }

    public void setVersionId(String versionId) {
        if(versionId != null){
            queryParameters.put("versionId",versionId);
        }
    }

    /**
     * <p>
     * 设置If-Modified-Since头部
     * </p>
     * <p>
     * 当 Object 在指定时间后被修改，则返回对应 Object 的 meta 信息，否则返回 304。
     * </p>
     *
     * @param ifModifiedSince If-Modified-Since头部
     */
    public void setIfModifiedSince(String ifModifiedSince){
        if(ifModifiedSince != null){
            addHeader(COSRequestHeaderKey.IF_MODIFIED_SINCE,ifModifiedSince);
        }
    }

    @Override
    public String getMethod() {
        return RequestMethod.HEAD;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

}
