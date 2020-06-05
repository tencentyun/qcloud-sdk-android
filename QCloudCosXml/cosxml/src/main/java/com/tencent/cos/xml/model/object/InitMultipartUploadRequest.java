package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * 用于构造初始化分片上传请求。
 * 关于初始化分片上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7746">https://cloud.tencent.com/document/product/436/7746.</a><br>
 * </p>
 */
final public class InitMultipartUploadRequest extends BaseMultipartUploadRequest {

    /**
     * InitMultipartUploadRequest 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     */
    public InitMultipartUploadRequest(String bucket, String cosPath){
        super(bucket, cosPath);
    }


    public InitMultipartUploadRequest(){
        super(null, null);
    }

    /**
     * 设置Cache-Control头部
     * @param cacheControl Cache-Control头部
     */
    public void setCacheControl(String cacheControl) {
        if(cacheControl == null)return;
        addHeader(COSRequestHeaderKey.CACHE_CONTROL,cacheControl);
    }

    /**
     * 设置 Content-Disposition 头部
     * @param contentDisposition Content-Disposition 头部
     */
    public void setContentDisposition(String contentDisposition) {
        if(contentDisposition == null)return;
        addHeader(COSRequestHeaderKey.CONTENT_DISPOSITION,contentDisposition);
    }

    /**
     * 设置 Content-Encoding 头部
     * @param contentEncoding Content-Encoding头部
     */
    public void setContentEncoding(String contentEncoding) {
        if(contentEncoding == null)return;
        addHeader(COSRequestHeaderKey.CONTENT_ENCODING,contentEncoding);
    }

    /**
     * 设置 Expires 头部
     * @param expires Expires 头部
     */
    public void setExpires(String expires) {
        if(expires == null)return;
        addHeader(COSRequestHeaderKey.EXPIRES,expires);
    }

    /**
     * 设置用户自定义头部信息
     * @param key 自定义头部信息的key值，需要以 x-cos-meta- 开头
     * @param value 自定义头部信息的value值。
     */
    public void setXCOSMeta(String key, String value){
        if(key != null && value != null){
            addHeader(key,value);
        }
    }

    /**
     * <p>
     * 设置 Object 的 ACL 属性
     * </p>
     * <p>
     * 有效值：private，public-read-write，public-read；默认值：private
     * </p>
     * @param cosacl ACL属性
     */
    public void setXCOSACL(String cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl);
        }
    }

    /**
     * <p>
     * 设置 Object 的 ACL 属性
     * </p>
     * <p>
     * 有效值：private，public-read-write，public-read；默认值：private
     * </p>
     * @param cosacl ACL枚举值 {@link COSACL}
     */
    public void setXCOSACL(COSACL cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl.getAcl());
        }
    }

    /**
     * <p>
     * 赋予被授权者读权限
     * </p>
     * @param aclAccount 读权限用户列表 {@link ACLAccount}
     */
    public void setXCOSGrantRead(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_READ, aclAccount.getAccount());
        }
    }


    /**
     * <p>
     * 赋予被授权者写权限
     * </p>
     * @param aclAccount 写权限用户列表 {@link ACLAccount}
     */
    public void setXCOSGrantWrite(ACLAccount aclAccount){

        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_WRITE, aclAccount.getAccount());
        }
    }


    /**
     * <p>
     * 赋予被授权者读写权限。
     * </p>
     * @param aclAccount 读写权限用户列表 {@link ACLAccount}
     */
    public void setXCOSReadWrite(ACLAccount aclAccount){

        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_FULL_CONTROL, aclAccount.getAccount());
        }
    }

    /**
     * 设置 存储对象类别
     * @see COSStorageClass
     * @param stroageClass
     */
    public void setStroageClass(COSStorageClass stroageClass)
    {
        addHeader(COSRequestHeaderKey.X_COS_STORAGE_CLASS_, stroageClass.getStorageClass());
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("uploads", null);
        return queryParameters;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return RequestBodySerializer.bytes(null, new byte[0]);
    }
}
