package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.Range;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.io.File;
import java.util.Map;

/**
 * <p>
 * 将 Bucket 中的文件（Object）下载至本地.
 * 关于下载接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7753">https://cloud.tencent.com/document/product/436/7753.</a><br>
 * </p>
 */
public class GetObjectRequest extends ObjectRequest implements TransferRequest {

    private String rspContentType;
    private String rspContentLanguage;
    private String rspExpires;
    private String rspCacheControl;
    private String rspContentDisposition;
    private String rspContentEncoding;
    private String versionId;
    private Range range;
    private long fileOffset = 0L;

    private CosXmlProgressListener progressListener;
    private String savePath;
    private String saveFileName;

    /**
     * GetObjectRequest 构造函数
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param savePath 文件下载到本地文件夹的绝对路径
     */
    public GetObjectRequest(String bucket, String cosPath, String savePath){
        super(bucket, cosPath);
        this.savePath = savePath;
    }

    /**
     * GetObjectRequest 构造函数
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param savePath 文件下载到本地文件夹的绝对路径
     * @param saveFileName 保存到本地的文件名
     */
    public GetObjectRequest(String bucket, String cosPath, String savePath, String saveFileName){
        super(bucket, cosPath);
        this.savePath = savePath;
        this.saveFileName = saveFileName;
    }

    public GetObjectRequest(){
        super(null, null);
    }

    public long getFileOffset() {
        return fileOffset;
    }

    public void setFileOffset(long fileOffset) {
        if(fileOffset > 0L){
            this.fileOffset = fileOffset;
        }
    }



    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    /**
     * 设置响应头部中的 Content-Type 值.
     * @param rspContentType Content-Type
     */
    public void setRspContentType(String rspContentType) {
        this.rspContentType = rspContentType;

    }

    /**
     * 获取设置的响应头Content-Type 值.
     * @return Content-Type 参数.
     */
    public String getRspContentType() {
        return rspContentType;
    }

    /**
     * 设置响应头部中的 Content-Language 值.
     * @param rspContentLanguage Content-Language 参数。
     */
    public void setRspContentLanguage(String rspContentLanguage) {
        this.rspContentLanguage = rspContentLanguage;

    }

    /**
     * 获取用户设置的Content-Language 参数。
     * @return Content-Language 参数。
     */
    public String getRspContentLanguage() {
        return rspContentLanguage;
    }

    /**
     * 设置响应头部中的 Content-Expires 值.
     * @param rspExpires Content-Expires 参数。
     */
    public void setRspExpires(String rspExpires) {
        this.rspExpires = rspExpires;

    }

    /**
     * 获取用户设置的Content-Expires 参数。
     * @return Content-Expires 参数。
     */
    public String getRspExpires() {
        return rspExpires;
    }

    /**
     * 设置响应头部中的 Cache-Control 值.
     * @param rspCacheControl Cache-Control 参数。
     */
    public void setRspCacheControl(String rspCacheControl) {
        this.rspCacheControl = rspCacheControl;

    }

    /**
     * 获取用户设置的Cache-Control 参数。
     * @return Cache-Control 参数。
     */
    public String getRspCacheControl() {
        return rspCacheControl;
    }

    /**
     * 设置响应头部中的 Content-Disposition 值.
     * @param rspContentDispositon Content-Disposition 参数。
     */
    public void setRspContentDispositon(String rspContentDispositon) {
        this.rspContentDisposition = rspContentDispositon;

    }

    /**
     * 获取用户设置的Content-Disposition 参数。
     * @return Content-Disposition 参数。
     */
    public String getRspContentDispositon() {
        return rspContentDisposition;
    }

    /**
     * 设置响应头部中的 Content-Encoding 值.
     * @param rspContentEncoding Content-Encoding 参数。
     */
    public void setRspContentEncoding(String rspContentEncoding) {
        this.rspContentEncoding = rspContentEncoding;
    }

    /**
     * 获取用户设置的 Content-Encoding 参数。
     * @return Content-Encoding 参数。
     */
    public String getRspContentEncoding() {
        return rspContentEncoding;
    }

    /**
     * 设置下载的范围
     *
     * @param start 起点
     * @param end 终点
     */
    public void setRange(long start, long end) {
        if(start < 0) start = 0;
        Range range = new Range(start, end);
        addHeader(COSRequestHeaderKey.RANGE,range.getRange());
        this.range = range;
    }

    /**
     * 设置下载的范围
     * @param start 起点
     */
    public void setRange(long start) {
       setRange(start, -1);
    }

    /**
     * 获取设置的下载范围
     * @return 下载范围 {@link Range}
     */
    public Range getRange(){
        return range;
    }
    /**
     * 设置下载请求的 If-Modified-Since 头部.<br>
     * 如果文件修改时间早于或等于指定时间，才返回文件内容。否则返回 412 (precondition failed)
     * @param ifModifiedSince
     */
    public void setIfModifiedSince(String ifModifiedSince){
        if(ifModifiedSince != null){
            addHeader(COSRequestHeaderKey.IF_MODIFIED_SINCE,ifModifiedSince);
        }
    }

    public void setIfUnmodifiedSince(String ifUnmodifiedSince){
        if(ifUnmodifiedSince != null){
            addHeader(COSRequestHeaderKey.IF_UNMODIFIED_SINCE,ifUnmodifiedSince);
        }
    }

    public void setIfMatch(String ifMatch){
        if(ifMatch != null){
            addHeader(COSRequestHeaderKey.IF_MATCH,ifMatch);
        }
    }

    public void setIfNONEMatch(String ifNONEMatch){
        if(ifNONEMatch != null){
            addHeader(COSRequestHeaderKey.IF_NONE_MATCH,ifNONEMatch);
        }
    }

    /**
     * 设置进度监听器
     * @param progressListener
     */
    public void setProgressListener(CosXmlProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    /**
     * 获取设置的请求进度监听器
     */
    public CosXmlProgressListener getProgressListener() {
        return progressListener;
    }

    /**
     * 设置文件的本地保存路径
     * @param savePath
     */
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    /**
     * 获取设置的文件本地保存路径
     * @return String
     */
    public String getSavePath() {
        return savePath;
    }

    /**
     * 设置文件存储在本地的文件名
     * @param saveFileName
     */
    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    /**
     * 获取文件存储在本地的文件名
     * 如果用户没有设置，则返回null
     * @return String
     */
    public String getSaveFileName() {
        return saveFileName;
    }

    public String getDownloadPath(){
        String path  = null;
        if(savePath != null){
            if(!savePath.endsWith("/")){
                path = savePath + "/";
            }else{
                path = savePath;
            }
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            if (saveFileName != null){
                path = path + saveFileName;
                return path;
            }
            if(cosPath != null){
                int separator = cosPath.lastIndexOf("/");
                if(separator >= 0){
                    path = path + cosPath.substring(separator + 1);
                }else{
                    path = path + cosPath;
                }
            }
        }
        return path;
    }

    /**
     * @see CosXmlRequest#getMethod()
     */
    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        if(versionId != null){
            queryParameters.put("versionId",versionId);
        }
        if(rspContentType != null){
            queryParameters.put("response-content-type",rspContentType);
        }
        if(rspContentLanguage != null){
            queryParameters.put("response-content-language",rspContentLanguage);
        }
        if(rspExpires != null){
            queryParameters.put("response-expires",rspExpires);
        }
        if(rspCacheControl != null){
            queryParameters.put("response-cache-control",rspCacheControl);
        }
        if(rspContentDisposition != null){
            queryParameters.put("response-content-disposition",rspContentDisposition);
        }
        if(rspContentEncoding != null){
            queryParameters.put("response-content-encoding",rspContentEncoding);
        }
        return super.getQueryString();
    }

    /**
     * @see CosXmlRequest#getRequestBody()
     */
    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

    @Override
    public void setTrafficLimit(long limit) {
        addHeader("x-cos-traffic-limit", String.valueOf(limit));
    }
}
