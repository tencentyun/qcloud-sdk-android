package com.tencent.qcloud.network.sonar;

/**
 * <p>
 * Created by jordanqin on 2024/8/16 17:15.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class SonarResult<T> {
    private final SonarType type;
    private final boolean success;
    private Exception exception;
    private T result;

    public SonarResult(SonarType type, T result) {
        this.type = type;
        this.success = true;
        this.result = result;
    }

    public SonarResult(SonarType type, Exception exception) {
        this.type = type;
        this.success = false;
        this.exception = exception;
    }

    public SonarType getType() {
        return type;
    }

    public boolean isSuccess() {
        return success;
    }

    public Exception getException() {
        return exception;
    }

    public T getResult() {
        return result;
    }
}
