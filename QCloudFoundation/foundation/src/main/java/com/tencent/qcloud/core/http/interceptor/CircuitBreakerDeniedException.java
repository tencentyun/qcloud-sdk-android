package com.tencent.qcloud.core.http.interceptor;

import java.io.IOException;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020-01-15.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class CircuitBreakerDeniedException extends IOException {

    CircuitBreakerDeniedException(String message) {
        super(message);
    }
}
