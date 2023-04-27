package com.tencent.cos.xml.model.ci;

import android.text.TextUtils;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Created by rickenwang on 2021/5/17.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class SensitiveContentRecognitionRequest extends BucketRequest {
    private String cosPath;
    private String detectUrl;
    private int interval;
    private int maxFrames;
    private final Set<String> types = new HashSet<>();
    private String bizType;
    private boolean largeImageDetect;

    /**
     * 图片同步审核请求
     *
     * @param bucket 存储桶名
     */
    public SensitiveContentRecognitionRequest(String bucket) {
        super(bucket);
    }

    /**
     * 图片同步审核请求
     *
     * @param bucket  存储桶名
     * @param cosPath cos上的路径
     */
    public SensitiveContentRecognitionRequest(String bucket, String cosPath) {
        super(bucket);
        setCosPath(cosPath);
    }

    /**
     * 设置对象在cos上的路径
     *
     * @param cosPath 对象在cos上的路径
     */
    public void setCosPath(String cosPath) {
        this.cosPath = cosPath;
    }

    /**
     * 获取对象在cos上的路径
     *
     * @return 对象在cos上的路径
     */
    public String getCosPath() {
        return cosPath;
    }

    /**
     * 添加审核类型，拥有 porn（涉黄识别）、
     * terrorist（涉暴恐识别）、
     * politics（涉政识别）、
     * ads（广告识别）四种。
     *
     * @param type 审核类型
     */
    public void addType(String type) {
        types.add(type);
    }

    /**
     * 设置审核策略，不填写则使用默认策略。可在控制台进行配置，
     * 详情请参见 <a href="https://cloud.tencent.com/document/product/436/55206">设置公共审核策略</a>
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    /**
     * 您可以通过填写detect-url审核任意公网可访问的图片链接
     * 通过 detect-url 进行审核，会产生图片所在源站对应的外网流量
     * 不填写detect-url时，后台会默认审核cosPath
     * 填写了detect-url时，后台会审核detect-url链接，无需再填写cosPath
     * detect-url示例：http://www.example.com/abc.jpg
     *
     * @param detectUrl 审核任意公网可访问的图片链接
     */
    public void setDetectUrl(String detectUrl) {
        this.detectUrl = detectUrl;
    }

    /**
     * 审核 GIF 动图时，可使用该参数进行截帧配置，代表截帧的间隔。
     * 例如值设为5，则表示从第1帧开始截取，每隔5帧截取一帧，默认值5
     *
     * @param interval 截帧的间隔
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * 针对 GIF 动图审核的最大截帧数量，需大于0。
     * 例如值设为5，则表示最大截取5帧，默认值为5
     *
     * @param maxFrames 动图审核的最大截帧数量
     */
    public void setMaxFrames(int maxFrames) {
        this.maxFrames = maxFrames;
    }

    /**
     * 对于超过大小限制的图片，可通过该参数选择是否需要压缩图片后再审核，压缩为后台默认操作，会产生额外的 基础图片处理用量
     * 注意：最大支持压缩32MB的图片。
     *
     * @param largeImageDetect false（不压缩），true（压缩）。默认为false
     */
    public void setLargeImageDetect(boolean largeImageDetect) {
        this.largeImageDetect = largeImageDetect;
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    @Override
    public String getPath(CosXmlServiceConfig config) {
        return config.getUrlPath(bucket, cosPath==null?"":cosPath);
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if (bucket == null || bucket.length() < 1) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "bucket must not be null ");
        }

        if ((cosPath == null || cosPath.length() < 1) && (detectUrl == null || detectUrl.length() < 1)) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "cosPath or detectUrl must not be null ");
        }

        addCiParams();
    }

    private void addCiParams() {
        StringBuilder typeStr = new StringBuilder();
        int count = 0;
        for (String type : types) {
            count++;
            typeStr.append(type);
            if (count < types.size()) {
                typeStr.append(",");
            }
        }
        queryParameters.put("ci-process", "sensitive-content-recognition");
        if(!TextUtils.isEmpty(this.bizType)){
            queryParameters.put("biz-type", this.bizType);
        }
        if(!TextUtils.isEmpty(this.detectUrl)){
            queryParameters.put("detect-url", this.detectUrl);
        }
        if(interval > 0) {
            queryParameters.put("interval", String.valueOf(this.interval));
        }
        if(maxFrames > 0) {
            queryParameters.put("max-frames", String.valueOf(this.maxFrames));
        }
        queryParameters.put("large-image-detect", this.largeImageDetect ? "1" : "0");
        queryParameters.put("detect-type", typeStr.toString());
    }

}
