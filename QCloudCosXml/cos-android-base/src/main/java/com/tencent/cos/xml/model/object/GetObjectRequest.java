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

import android.net.Uri;

import com.tencent.cos.xml.BaseCosXml;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.Range;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 下载 COS 对象的请求.
 * @see BaseCosXml#getObject(GetObjectRequest)
 * @see BaseCosXml#getObjectAsync(GetObjectRequest, CosXmlResultListener)
 */
public class GetObjectRequest extends ObjectRequest implements SaveLocalRequest, TransferRequest {

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

    private Uri fileContentUri;

    private boolean objectKeySimplifyCheck = true;

    /**
     * GetObjectRequest 构造函数
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param savePath 文件下载到本地文件夹的绝对路径
     */
    public GetObjectRequest(String bucket, String cosPath, String savePath){
        super(bucket, cosPath);
        this.savePath = savePath;
    }

    /**
     * GetObjectRequest 构造函数
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param savePath 文件下载到本地文件夹的绝对路径
     * @param saveFileName 保存到本地的文件名
     */
    public GetObjectRequest(String bucket, String cosPath, String savePath, String saveFileName){
        super(bucket, cosPath);
        this.savePath = savePath;
        this.saveFileName = saveFileName;
    }

    /**
     * GetObjectRequest 构造函数
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param fileContentUri 本地文件 Uri
     */
    public GetObjectRequest(String bucket, String cosPath, Uri fileContentUri) {
        super(bucket, cosPath);
        this.fileContentUri = fileContentUri;
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
        requestHeaders.remove(COSRequestHeaderKey.RANGE);
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

    /**
     * 设置是否校验cosPath归并后是否符合规范，默认为true
     * @param objectKeySimplifyCheck 是否校验cosPath归并后是否符合规范
     */
    public void setObjectKeySimplifyCheck(boolean objectKeySimplifyCheck) {
        this.objectKeySimplifyCheck = objectKeySimplifyCheck;
    }

    public boolean isObjectKeySimplifyCheck() {
        return objectKeySimplifyCheck;
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

    public Uri getFileContentUri() {
        return fileContentUri;
    }

    @Override
    public String getSaveLocalPath() {
        return getDownloadPath();
    }

    @Override
    public Uri getSaveLocalUri() {
        return getFileContentUri();
    }

    @Override
    public long getSaveLocalOffset() {
        return getFileOffset();
    }

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

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return null;
    }

    @Override
    public void setTrafficLimit(long limit) {
        addHeader("x-cos-traffic-limit", String.valueOf(limit));
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();

        if(objectKeySimplifyCheck) {
            String normalizedPath = cosPath;
            try {
                File file = new File("/" + cosPath);
                normalizedPath = file.getCanonicalPath();
            } catch (IOException e) {e.printStackTrace();}
            if ("/".equals(normalizedPath)) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "The key in the getobject is illegal");
            }
        }
    }
}
