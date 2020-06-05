package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.utils.FileUtils;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * <p>
 * 追加上传的对象。
 * </p>
 *
 * <p>
 * 追加上传的对象每个分块最小为 4K，建议大小 1M-5G。如果 Position 的值和当前对象的长度不致，COS 会返回 409 错误。
 * 如果追加一个 normal 属性的文件，COS 会返回 409 ObjectNotAppendable。
 * </p>
 * <p>
 * appendable 的对象不可以被复制，不参与版本管理，不参与生命周期管理，不可跨区域复制。
 * </p>
 *
*/
final public class AppendObjectRequest extends ObjectRequest {

    /** 追加的起始点 */
    private long position = 0;
    private String srcPath;
    private byte[] data;
    public InputStream inputStream;
    private long fileLength;

    private CosXmlProgressListener progressListener;

    private AppendObjectRequest(String bucket, String cosPath){
        super(bucket, cosPath);
    }

    public AppendObjectRequest(String bucket, String cosPath, String srcPath, long position){
        this(bucket, cosPath);
        this.srcPath = srcPath;
        this.position = position;
    }

    public AppendObjectRequest(String bucket, String cosPath, byte[] data, long position){
        this(bucket, cosPath);
        this.data = data;
        this.position = position;
    }

    public AppendObjectRequest(String bucket, String cosPath, InputStream inputStream, long position) throws CosXmlClientException {
        this(bucket, cosPath);
        this.inputStream = inputStream;
        //this.srcPath= FileUtils.tempCache(inputStream);
        this.position = position;
    }

    public AppendObjectRequest() {
        super(null, null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("append", null);
        queryParameters.put("position", String.valueOf(position));
        return queryParameters;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        if(srcPath != null){
            return RequestBodySerializer.file(getContentType(), new File(srcPath));
        }else if(data != null){
           return RequestBodySerializer.bytes(null, data);
        }else if(inputStream != null){
            return RequestBodySerializer.stream(null, new File(CosXmlSimpleService.appCachePath),
                    inputStream);
        }
        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(srcPath == null && data == null && inputStream == null){
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
     * 追加操作的起始点，单位：字节；
     * 首次追加 position=0，后续追加 position= 当前 Object 的 content-length
     *
     * @param position
     */
    public void setPosition(long position) {
        if(position < 0)this.position = 0;
        this.position = position;
    }

    /**
     * 获取设置的追加操作起点
     *
     * @return long
     */
    public long getPosition() {
        return position;
    }

    /**
     * <p>
     * 设置上传的本地文件路径
     * </p>
     * <p>
     * 可以设置上传本地文件、字节数组或者输入流。每次只能上传一种类型，若同时设置，
     * 则优先级为 本地文件 &gt; 字节数组
     * </p>
     *
     * @param srcPath 本地文件路径
     * @see AppendObjectRequest#setData(byte[])
     */
    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    /**
     * 获取设置的本地文件路径
     *
     * @return String
     */
    public String getSrcPath() {
       return srcPath;
    }

    /**
     * <p>
     * 设置上传的字节数组
     * </p>
     * <p>
     * 可以设置上传本地文件、字节数组或者输入流。每次只能上传一种类型，若同时设置，
     * 则优先级为 本地文件&gt;字节数组&gt;输入流
     * </p>
     *
     * @param data 需要上传的字节数组
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * 获取用户设置的字节数组
     *
     * @return byte[]
     */
    public byte[] getData() {
        return data;
    }

    /**
     * 获取用户设置的输入流读取的字节长度
     *
     * @return long
     */
    public long getFileLength() {
        if(srcPath != null){
            File file = new File(srcPath);
            fileLength = file.length();
        }else if(data != null){
            fileLength = data.length;
        }
        return fileLength;
    }

    /**
     * 设置上传进度监听
     *
     * @param progressListener
     */
    public void setProgressListener(CosXmlProgressListener progressListener){
        this.progressListener = progressListener;
    }

    /**
     * 获取用户设置的进度监听
     *
     * @return CosXmlProgressListener
     */
    public CosXmlProgressListener getProgressListener() {
        return progressListener;
    }

    /**
     * <p>
     * 设置缓存策略
     * </p>
     * <p>
     * 即RFC 2616 中定义的Cache-Control头部
     * </p>
     *
     * @param cacheControl Cache-Control头部
     */
    public void setCacheControl(String cacheControl) {
        if(cacheControl == null)return;
        addHeader(COSRequestHeaderKey.CACHE_CONTROL,cacheControl);
    }

    /**
     * <p>
     * 设置文件名称。
     * </p>
     * <p>
     * 即RFC 2616 中定义的Content-Disposition头部
     * </p>
     *
     * @param contentDisposition Content-Disposition头部
     */
    public void setContentDisposition(String contentDisposition) {
        if(contentDisposition == null)return;
        addHeader(COSRequestHeaderKey.CONTENT_DISPOSITION,contentDisposition);
    }

    /**
     * <p>
     * 设置编码格式。
     * </p>
     * <p>
     * 即RFC 2616 中定义的Content-Encoding头部
     * </p>
     *
     * @param contentEncoding Content-Encoding头部
     */
    public void setContentEncodeing(String contentEncoding) {
        if(contentEncoding == null)return;
        addHeader(COSRequestHeaderKey.CONTENT_ENCODING,contentEncoding);
    }

    /**
     * <p>
     * 设置过期时间。
     * </p>
     * <p>
     * 即RFC 2616 中定义的Expires头部
     * </p>
     *
     * @param expires Expires头部
     */
    public void setExpires(String expires) {
        if(expires == null)return;
        addHeader(COSRequestHeaderKey.EXPIRES, expires);
    }

    /**
     * <p>
     * 自定义的头部信息
     * </p>
     * <p>
     * key需要以x-cos-meta-开头
     * </p>
     *
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
     * 设置Bucket访问权限
     * </p>
     *
     * <br>
     * 有效值：
     * <ul>
     * <li>private ：私有，默认值</li>
     * <li>public-read ：公有读</li>
     * <li>public-read-write ：公有读写</li>
     * </ul>
     * <br>
     *
     * @param cosacl acl字符串
     */
    public void setXCOSACL(String cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl);
        }
    }

    /**
     * 设置Bucket的ACL信息
     *
     * @param cosacl acl枚举
     */
    public void setXCOSACL(COSACL cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl.getAcl());
        }
    }

    /**
     * <p>
     * 单独明确赋予用户读权限
     * </p>
     *
     * @param aclAccount 读权限用户列表
     */
    public void setXCOSGrantRead(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_READ, aclAccount.getAccount());
        }
    }


    /**
     * <p>
     * 赋予被授权者写的权限
     * </p>
     *
     * @param aclAccount 写权限用户列表
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
     *
     * @param aclAccount 读写用户权限列表
     */
    public void setXCOSReadWrite(ACLAccount aclAccount){

        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_FULL_CONTROL, aclAccount.getAccount());
        }
    }
}
