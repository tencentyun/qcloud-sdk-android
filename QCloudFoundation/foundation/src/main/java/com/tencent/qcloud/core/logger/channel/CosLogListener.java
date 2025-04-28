package com.tencent.qcloud.core.logger.channel;

import com.tencent.qcloud.core.logger.LogEntity;

/**
 * <p>
 * Created by jordanqin on 2025/4/2 22:16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public interface CosLogListener {
    void onLog(LogEntity entity);
}
