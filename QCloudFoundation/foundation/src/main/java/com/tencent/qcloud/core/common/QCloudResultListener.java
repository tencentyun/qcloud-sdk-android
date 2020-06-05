package com.tencent.qcloud.core.common;

/**
 *
 * 异步发送任务时，网络请求结果监听
 *
 *
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public interface QCloudResultListener<T> {

    /**
     * 网络请求成功回调
     *
     * @param result
     */
    void onSuccess(T result);

    /**
     * 网络请求失败回调
     *
     * @param clientException 客户端异常，通常是指没有拿到服务器Response就发生的异常。
     * @param serviceException 服务器异常，通常是指服务器Response异常。
     */
    void onFailure(QCloudClientException clientException, QCloudServiceException serviceException);



}
