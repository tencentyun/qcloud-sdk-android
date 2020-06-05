package com.tencent.cos.xml.model.object;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.model.tag.pic.PicOperations;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.util.ContextHolder;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * <p>
 * 简单上传接口，关于简单上传接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7749">
 * https://cloud.tencent.com/document/product/436/7749.</a><br>
 * </p>
 */
public class PutObjectRequest extends ObjectRequest implements TransferRequest {
    private String srcPath;
    private byte[] data;
    private InputStream inputStream;
    private String strData;
    private URL url;
    private long fileLength;
    private Uri uri;
    private CosXmlProgressListener progressListener;

    private PutObjectRequest(String bucket, String cosPath){
        super(bucket, cosPath);
        setNeedMD5(true);
    }

    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param srcPath 本地文件的绝对路径
     */
    public PutObjectRequest(String bucket, String cosPath, String srcPath){
        this(bucket, cosPath);
        this.srcPath = srcPath;
    }

    public PutObjectRequest(String bucket, String cosPath, Uri uri){
        this(bucket, cosPath);
        this.uri = uri;
    }

    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param data 上传的数据
     */
    public PutObjectRequest(String bucket, String cosPath, byte[] data){
        this(bucket, cosPath);
        this.data = data;
    }

    /**
     * PutObject 构造方法
     *
     * @param bucket 存储桶名称
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param stringBuilder 上传的字符串
     */
    public PutObjectRequest(String bucket, String cosPath, StringBuilder stringBuilder) {
        this(bucket, cosPath);
        strData = stringBuilder.toString();
    }

    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param inputStream 上传的数据流
     */
    public PutObjectRequest(String bucket, String cosPath, InputStream inputStream){
        this(bucket, cosPath);
        this.inputStream = inputStream;
    }


    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param url 上传的url
     */
    public PutObjectRequest(String bucket, String cosPath, URL url) {
        this(bucket, cosPath);
        this.url = url;
    }

    public PutObjectRequest(){
        super(null, null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        if(srcPath != null){
            return RequestBodySerializer.file(getContentType(), new File(srcPath));
        } else if(data != null){
            return RequestBodySerializer.bytes(null, data);
        } else if(inputStream != null){
            return RequestBodySerializer.stream(null, new File(CosXmlSimpleService.appCachePath, String.valueOf(System.currentTimeMillis())),
                    inputStream);
        } else if (strData != null) {
            return RequestBodySerializer.bytes(null, strData.getBytes());
        } else if (url != null) {
            return RequestBodySerializer.url(null, url);
        } else if (uri != null && ContextHolder.getAppContext() != null) {
            return RequestBodySerializer.uri(null, uri, ContextHolder.getAppContext());
        }

        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(srcPath == null && data == null && inputStream == null && strData == null && uri == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "Data Source must not be null");
        }
        if(srcPath != null){
            File file = new File(srcPath);
            if(!file.exists()){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "upload file does not exist");
            }
        }
    }


    /**
     * 上传进度回调
     * @param progressListener 进度监听器 {@link CosXmlProgressListener}
     */
    public void setProgressListener(CosXmlProgressListener progressListener){
        this.progressListener = progressListener;
    }

    public CosXmlProgressListener getProgressListener(){
        return progressListener;
    }

    /**
     * <p>
     * 设置上传的本地文件路径.<br>
     * 可以设置上传本地文件、字节数组或者输入流。每次只能上传一种类型，若同时设置，
     * 则优先级为 本地文件&gt;字节数组&gt;输入流
     * </p>
     * @param srcPath 本地文件路径
     */
    public void setSrcPath(String srcPath){
        this.srcPath = srcPath;
    }

    /**
     * 获取设置的本地文件路径
     * @return String
     */
    public String getSrcPath(){
       return srcPath;
    }

    /**
     * <p>
     * 设置上传的字节数组.<br>
     * 可以设置上传本地文件、字节数组或者输入流。每次只能上传一种类型，若同时设置，
     * 则优先级为 本地文件&gt;字节数组&gt;输入流
     * </p>
     * @param data 需要上传的字节数组
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public String getStrData() {
        return strData;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 获取用户设置的字节数组
     * @return byte[]
     */
    public byte[] getData() {
        return data;
    }


    /**
     * 获取待上传数据的总长度
     * @return long
     */
    public long getFileLength() {
        if(srcPath != null){
            fileLength = new File(srcPath).length();
        }else if(data != null){
            fileLength =  data.length;
        } else if (strData != null) {
            fileLength = strData.getBytes().length;
        }
        return fileLength;
    }

    /**
     * <p>
     * 设置Cache-Control头部
     * </p>
     * @param cacheControl Cache-Control头部
     */
    public void setCacheControl(String cacheControl) {
        if(cacheControl == null)return;
        addHeader(COSRequestHeaderKey.CACHE_CONTROL,cacheControl);
    }

    /**
     * <p>
     * 设置Content-Disposition头部部
     * </p>
     * @param contentDisposition Content-Disposition头部
     */
    public void setContentDisposition(String contentDisposition) {
        if(contentDisposition == null)return;
       addHeader(COSRequestHeaderKey.CONTENT_DISPOSITION,contentDisposition);
    }

    /**
     * <p>
     * 设置Content-Encoding头部
     * </p>
     * @param contentEncoding Content-Encoding头部
     */
    public void setContentEncodeing(String contentEncoding) {
        if(contentEncoding == null)return;
        addHeader(COSRequestHeaderKey.CONTENT_ENCODING,contentEncoding);
    }

    /**
     * <p>
     * 设置Expires头部
     * </p>
     * @param expires Expires头部
     */
    public void setExpires(String expires) {
        if(expires == null)return;
        addHeader(COSRequestHeaderKey.EXPIRES,expires);
    }

    /**
     * <p>
     * 自定义的头部信息
     * </p>
     * @param key 自定义头部key，需要以x-cos-meta-开头
     * @param value 自定义头部value
     */
    public void setXCOSMeta(String key, String value){
        if(key != null && value != null){
            addHeader(key,value);
        }
    }

    /**
     * <p>
     * 设置 object 访问权限
     * </p>
     * <br>
     * 有效值：
     * <ul>
     * <li>private ：私有，默认值</li>
     * <li>public-read ：公有读</li>
     * <li>public-read-write ：公有读写</li>
     * </ul>
     * @param cosacl 访问权限 {@link COSACL}
     */
    public void setXCOSACL(COSACL cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl.getAcl());
        }
    }


    /**
     * <p>
     * 设置 object 访问权限
     * </p>
     * <br>
     * 有效值：
     * <ul>
     * <li>private ：私有，默认值</li>
     * <li>public-read ：公有读</li>
     * <li>public-read-write ：公有读写</li>
     * </ul>
     * @param cosacl 访问权限
     */
    public void setXCOSACL(String cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl);
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
    public void setTrafficLimit(long limit) {
        addHeader("x-cos-traffic-limit", String.valueOf(limit));
    }

    public void setPicOperations(@NonNull PicOperations operations) {
        addHeader("Pic-Operations", operations.toJsonStr());
    }
}
