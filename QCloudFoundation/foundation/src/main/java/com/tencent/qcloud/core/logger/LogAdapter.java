package com.tencent.qcloud.core.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * <p>
 *     Provides a common interface to emits logs through. This is a required contract for Logger.
 * </p>
 *
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public interface LogAdapter {
    /**
     * Used to determine whether log should be printed out or not.
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param tag is the given tag for the log message
     *
     * @return is used to determine if log should printed.
     *         If it is true, it will be printed, otherwise it'll be ignored.
     */
    boolean isLoggable(int priority, @Nullable String tag);

    /**
     * Each log will use this pipeline
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param tag is the given tag for the log message.
     * @param message is the given message for the log message.
     * @param tr is exception for the given message.
     */
    void log(int priority, @NonNull String tag, @NonNull String message, @Nullable Throwable tr);
}
