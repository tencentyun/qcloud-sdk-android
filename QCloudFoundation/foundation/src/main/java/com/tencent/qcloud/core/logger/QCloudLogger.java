package com.tencent.qcloud.core.logger;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * QCloud Logger Utility.
 *
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public final class QCloudLogger {

    private static final List<LogAdapter> logAdapters = new ArrayList<>();
    private static final AndroidLogcatAdapter logcatAdapter = new AndroidLogcatAdapter();
    static {
        logAdapters.add(logcatAdapter);
    }

    /**
     * Priority constant for the println method; use LogUtils.v.
     */
    public static final int VERBOSE = Log.VERBOSE;

    /**
     * Priority constant for the println method; use LogUtils.d.
     */
    public static final int DEBUG = Log.DEBUG;

    /**
     * Priority constant for the println method; use LogUtils.i.
     */
    public static final int INFO = Log.INFO;

    /**
     * Priority constant for the println method; use LogUtils.w.
     */
    public static final int WARN = Log.WARN;

    /**
     * Priority constant for the println method; use LogUtils.e.
     */
    public static final int ERROR = Log.ERROR;


    private QCloudLogger() {
    }

    /**
     * Add a new log output pipeline.
     *
     * @param adapter log pipeline
     */
    public static void addAdapter(LogAdapter adapter) {
        if (adapter != null) {
            synchronized (LogAdapter.class) {
                boolean addBefore = false;
                for (LogAdapter logAdapter : logAdapters) {
                    if (logAdapter.getClass().equals(adapter.getClass())) {
                        addBefore = true;
                        break;
                    }
                }
                if (!addBefore) {
                    logAdapters.add(adapter);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends LogAdapter> T getAdapter(Class<T> adapterClass) {
        synchronized (LogAdapter.class) {
            for (LogAdapter logAdapter : logAdapters) {
                if (logAdapter.getClass().equals(adapterClass)) {
                    return (T) logAdapter;
                }
            }
            return null;
        }
    }

    /**
     * Send a {@link #VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param format The format of message you would like logged.
     * @param args The arguments of message you would like logged.
     */
    public static void v(String tag, String format, Object... args) {
        print(VERBOSE, tag, null, format, args);
    }

    /**
     * Send a {@link #VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log.
     * @param format The format of message you would like logged.
     * @param args The arguments of message you would like logged.
     */
    public static void v(String tag, Throwable tr, String format, Object... args) {
        print(VERBOSE, tag, tr, format, args);
    }

    /**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param format The format of message you would like logged.
     * @param args The arguments of message you would like logged.
     */
    public static void d(String tag, String format, Object... args) {
        print(DEBUG, tag, null, format, args);
    }

    /**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log.
     * @param format The format of message you would like logged.
     * @param args The arguments of message you would like logged.
     */
    public static void d(String tag, Throwable tr, String format, Object... args) {
        print(DEBUG, tag, tr, format, args);
    }

    /**
     * Send a {@link #INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param format The format of message you would like logged.
     * @param args The arguments of message you would like logged.
     */
    public static void i(String tag, String format, Object... args) {
        print(INFO, tag, null, format, args);
    }

    /**
     * Send a {@link #INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log.
     * @param format The format of message you would like logged.
     * @param args The arguments of message you would like logged.
     */
    public static void i(String tag, Throwable tr, String format, Object... args) {
        print(INFO, tag, tr, format, args);
    }

    /**
     * Send a {@link #WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param format The format of message you would like logged.
     * @param args The arguments of message you would like logged.
     */
    public static void w(String tag, String format, Object... args) {
        print(WARN, tag, null, format, args);
    }

    /**
     * Send a {@link #WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log.
     * @param format The format of message you would like logged.
     * @param args The arguments of message you would like logged.
     */
    public static void w(String tag, Throwable tr, String format, Object... args) {
        print(WARN, tag, tr, format, args);
    }

    /**
     * Send a {@link #ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param format The format of message you would like logged.
     * @param args The arguments of message you would like logged.
     */
    public static void e(String tag, String format, Object... args) {
        print(ERROR, tag, null, format, args);
    }

    /**
     * Send a {@link #ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log.
     * @param format The format of message you would like logged.
     * @param args The arguments of message you would like logged.
     */
    public static void e(String tag, Throwable tr, String format, Object... args) {
        print(ERROR, tag, tr, format, args);
    }

    /**
     * Used to determine whether log should be printed out or not on logcat.
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param tag is the given tag for the log message
     *
     * @return is used to determine if log should printed.
     *         If it is true, it will be printed, otherwise it'll be ignored.
     */
    public static boolean isLoggableOnLogcat(int priority, String tag) {
        return logcatAdapter.isLoggable(priority, tag);
    }

    private static void print(int priority, String tag, @Nullable Throwable tr, String format, Object... args) {
        String message;
        try {
            message = args != null && args.length > 0 ? String.format(format, args) : format;
        } catch (Exception e) {
            message = format + ": !!!! Log format exception: ";
        }
        synchronized (LogAdapter.class) {
            for (LogAdapter adapter : logAdapters) {
                if (adapter.isLoggable(priority, tag)) {
                    adapter.log(priority, tag, message, tr);
                }
            }
        }
    }
}
