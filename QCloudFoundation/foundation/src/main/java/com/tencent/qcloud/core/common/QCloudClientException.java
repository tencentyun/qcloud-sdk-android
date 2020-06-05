package com.tencent.qcloud.core.common;

/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public class QCloudClientException extends Exception {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new QCloudClientException with the specified message, and root
     * cause.
     *
     * @param message An error message describing why this exception was thrown.
     * @param t The underlying cause of this exception.
     */
    public QCloudClientException(final String message, final Throwable t) {
        super(message, t);
    }

    /**
     * Creates a new QCloudClientException with the specified message.
     *
     * @param message An error message describing why this exception was thrown.
     */
    public QCloudClientException(final String message) {
        super(message);
    }

    /**
     * Create an QCloudClientException with an exception cause.
     *
     * @param throwable the cause of the exception.
     */
    public QCloudClientException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * Returns a hint as to whether it makes sense to retry upon this exception.
     * Default is true, but subclass may override.
     * @return true if it is retryable.
     */
    public boolean isRetryable() {
        return true;
    }
}
