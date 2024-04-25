package com.tencent.qcloud.track;

import java.util.Map;

/**
 * 数据上报接口
 * <p>
 * Created by jordanqin on 2023/9/4 19:36.
 * Copyright 2010-2023 Tencent Cloud. All Rights Reserved.
 */
public interface IReport {
    /**
     * 上报数据
     */
    void report(String eventCode, Map<String, String> params);
}
