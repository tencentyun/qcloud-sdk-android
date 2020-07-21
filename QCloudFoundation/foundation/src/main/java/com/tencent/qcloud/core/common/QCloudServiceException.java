/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.qcloud.core.common;

/**
 * Service Exception
 */

public class QCloudServiceException extends Exception {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    public static final String ERR0R_REQUEST_TIME_TOO_SKEWED = "RequestTimeTooSkewed";
    public static final String ERR0R_REQUEST_IS_EXPIRED = "RequestIsExpired";

    /**
     * The unique qcloud identifier for the service request the caller made. The
     * qcloud request ID can uniquely identify the qcloud request, and is used for
     * reporting an error to qcloud support team.
     */
    private String requestId;

    /**
     * The qcloud error code represented by this exception (ex:
     * InvalidParameterValue).
     */
    private String errorCode;

    /**
     * The error message as returned by the service.
     */
    private String errorMessage;


    /** The HTTP status code that was returned with this error. */
    private int statusCode;

    /**
     * The name of the Amazon service that sent this error response.
     */
    private String serviceName;

    /**
     * Constructs a new QCloudServiceException with the specified message.
     *
     * @param errorMessage An error message describing what went wrong.
     */
    public QCloudServiceException(final String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    /**
     * Constructs a new QCloudServiceException with the specified message and
     * exception indicating the root cause.
     *
     * @param errorMessage An error message describing what went wrong.
     * @param cause The root exception that caused this exception to be thrown.
     */
    public QCloudServiceException(final String errorMessage,
                                  final Exception cause) {
        super(null, cause);
        this.errorMessage = errorMessage;
    }

    /**
     * Sets the qcloud requestId for this exception.
     *
     * @param requestId The unique identifier for the service request the caller
     *            made.
     */
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }

    /**
     * Returns the qcloud request ID that uniquely identifies the service request
     * the caller made.
     *
     * @return The qcloud request ID that uniquely identifies the service request
     *         the caller made.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the name of the service that sent this error response.
     *
     * @param serviceName The name of the service that sent this error response.
     */
    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Returns the name of the service that sent this error response.
     *
     * @return The name of the service that sent this error response.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the qcloud error code represented by this exception.
     *
     * @param errorCode The qcloud error code represented by this exception.
     */
    public QCloudServiceException setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    /**
     * Returns the qcloud error code represented by this exception.
     *
     * @return The qcloud error code represented by this exception.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @return the human-readable error message provided by the service
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the HTTP status code that was returned with this service exception.
     *
     * @param statusCode The HTTP status code that was returned with this
     *            service exception.
     */
    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Returns the HTTP status code that was returned with this service
     * exception.
     *
     * @return The HTTP status code that was returned with this service
     *         exception.
     */
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return getErrorMessage()
                + " (Service: " + getServiceName()
                + "; Status Code: " + getStatusCode()
                + "; Error Code: " + getErrorCode()
                + "; Request ID: " + getRequestId() + ")";
    }

    /**
     * @param errorMessage sets the error message.
     */
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
