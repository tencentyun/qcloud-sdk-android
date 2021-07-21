package com.tencent.cos.xml.model.ci;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Created by rickenwang on 2021/5/17.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class SensitiveContentRecognitionRequest extends ImageCloudProcessRequest {

    private Set<String> types = new HashSet<>();

    /**
     * 对象相关请求基类
     *
     * @param bucket  存储桶名
     * @param cosPath cos上的路径
     */
    public SensitiveContentRecognitionRequest(String bucket, String cosPath) {
        super(bucket, cosPath);
    }

    /**
     * 添加审核类型，拥有 porn（涉黄识别）、
     *             terrorist（涉暴恐识别）、
     *             politics（涉政识别）、
     *             ads（广告识别）四种。
     *
     * @param type
     */
    public void addType(String type) {
        types.add(type);
    }

    @Override
    protected void addCiParams() {

        if (types.isEmpty()) {
            types.add("porn");
        }

        StringBuilder typeStr = new StringBuilder();
        int count = 0;
        for (String type: types) {
            count++;
            typeStr.append(type);
            if (count < types.size()) {
                typeStr.append(",");
            }
        }
        queryParameters.put("ci-process", "sensitive-content-recognition");
        queryParameters.put("detect-type", typeStr.toString());
    }
    
}
