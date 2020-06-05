package com.tencent.cos.xml.model.tag;

/**
 * Created by bradyxiao on 2018/6/12.
 */

public class PostResponse {

    public String location;
    public String bucket;
    public String key;
    public String eTag;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{PostResponse:\n");
        stringBuilder.append("Location:").append(location).append("\n");
        stringBuilder.append("Bucket:").append(bucket).append("\n");
        stringBuilder.append("Key:").append(key).append("\n");
        stringBuilder.append("ETag:").append(eTag).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
