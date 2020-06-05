package com.tencent.cos.xml.model.tag;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class CompleteMultipartUploadResult {
    /**
     * 创建的Object的外网访问域名
     */
    public String location;
    /**
     * 分块上传的目标Bucket
     */
    public String bucket;
    /**
     * Object的名称
     */
    public String key;
    /**
     * 合并后文件的 MD5 算法校验值
     */
    public String eTag;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{CompleteMultipartUploadResult:\n");
        stringBuilder.append("Location:").append(location).append("\n");
        stringBuilder.append("Bucket:").append(bucket).append("\n");
        stringBuilder.append("Key:").append(key).append("\n");
        stringBuilder.append("ETag:").append(eTag).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
