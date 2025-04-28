package com.tencent.qcloud.core.logger;

/**
 * <p>
 * Created by jordanqin on 2025/3/21 21:03.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public enum LogLevel {
    VERBOSE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5);

    private final int priority;

    public int getPriority() {
        return priority;
    }

    LogLevel(int priority) { this.priority = priority; }

    public boolean isLoggable(LogLevel minLevel) {
        if(minLevel == null) return true;

        return this.priority >= minLevel.priority;
    }
}