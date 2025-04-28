package com.tencent.qcloud.core.logger.channel;

import com.tencent.qcloud.core.logger.LogEntity;
import com.tencent.qcloud.core.logger.LogLevel;

/**
 * <p>
 * Created by jordanqin on 2025/3/21 21:09.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public abstract class BaseLogChannel {
    private boolean enabled = true;
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private LogLevel minLevel = null;;

    public LogLevel getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(LogLevel minLevel) {
        this.minLevel = minLevel;
    }

    public abstract void log(LogEntity entity);
}
