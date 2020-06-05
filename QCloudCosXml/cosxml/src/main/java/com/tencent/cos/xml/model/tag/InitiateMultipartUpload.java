package com.tencent.cos.xml.model.tag;

/**
 * <p>
 * 初始化上传请求返回的信息
 * </p>
 */

public class InitiateMultipartUpload {
    /**
     * 分片上传的目标 Bucket，由用户自定义字符串和系统生成appid数字串由中划线连接而成，如：mybucket-1250000000.
     */
    public String bucket;
    /**
     * Object 的名称
     */
    public String key;
    /**
     * 在后续上传中使用的 ID
     */
    public String uploadId;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{InitiateMultipartUpload:\n");
        stringBuilder.append("Bucket:").append(bucket).append("\n");
        stringBuilder.append("Key:").append(key).append("\n");
        stringBuilder.append("UploadId:").append(uploadId).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
