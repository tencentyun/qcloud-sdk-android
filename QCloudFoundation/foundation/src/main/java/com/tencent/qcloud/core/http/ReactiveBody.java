package com.tencent.qcloud.core.http;

import java.io.IOException;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020-04-21.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public interface ReactiveBody {

    /**
     * 准备请求体
     */
    void prepare() throws IOException;

    /**
     * 结束请求体
     *
     * @throws IOException
     */
    <T> void end(HttpResult<T> httpResult) throws IOException;
}
