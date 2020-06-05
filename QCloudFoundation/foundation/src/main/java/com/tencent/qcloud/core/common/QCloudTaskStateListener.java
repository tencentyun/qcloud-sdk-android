package com.tencent.qcloud.core.common;

/**
 * <p>
 *     QCloudTask 的状态监听器
 * </p>
 * Created by wjielai on 2018/9/13.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public interface QCloudTaskStateListener {

    void onStateChanged(String taskId, int state);
}
