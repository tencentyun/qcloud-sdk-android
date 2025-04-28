package com.tencent.qcloud.core.logger.channel;

import com.tencent.qcloud.core.logger.LogEntity;

/**
 * <p>
 * Created by jordanqin on 2025/3/21 21:10.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class ListenerChannel extends BaseLogChannel {
    private CosLogListener listener;
    public ListenerChannel(CosLogListener listener) {
        this.listener = listener;
    }

    @Override
    public void log(LogEntity entity) {
        if(!isLoggable(entity)) {
            return;
        }
        this.listener.onLog(entity);
    }

    private boolean isLoggable(LogEntity entity) {
        if (!isEnabled() || entity == null) return false;
        return entity.getLevel().isLoggable(getMinLevel());
    }

    public CosLogListener getListener() {
        return listener;
    }
}
