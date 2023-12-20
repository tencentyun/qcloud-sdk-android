package com.tencent.qcloud.track;

import java.io.File;

/**
 * 常量
 * <p>
 * Created by jordanqin on 2023/9/6 11:53.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class Constants {
    /**
     * 默认简单数据上报灯塔key
     */
    public static final String SIMPLE_DATA_BEACON_APP_KEY = "0AND05KOZX0E3L2H";
    /**
     * SDK启动事件
     */
    public static final String SIMPLE_DATA_EVENT_CODE_START = "qcloud_track_sd_sdk_start";
    /**
     * SDK其他错误事件
     */
    public static final String SIMPLE_DATA_EVENT_CODE_ERROR = "qcloud_track_sd_sdk_error";

    public static final String BEACON_EVENT_CACHE_FILE_NAME = "QCloudBeaconEventCache"+ File.separator + "events.txt";

}
