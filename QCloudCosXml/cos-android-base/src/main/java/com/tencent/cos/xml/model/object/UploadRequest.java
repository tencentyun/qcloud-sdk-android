package com.tencent.cos.xml.model.object;

/**
 * 简单上传和分片上传相关的请求基类
 */
public abstract class UploadRequest extends ObjectRequest {

    /**
     * 对象相关请求基类
     *
     * @param bucket  存储桶名
     * @param cosPath cos上的路径
     */
    public UploadRequest(String bucket, String cosPath) {
        super(bucket, cosPath);
    }
}
