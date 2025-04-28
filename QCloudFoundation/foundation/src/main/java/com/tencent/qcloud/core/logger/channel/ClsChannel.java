package com.tencent.qcloud.core.logger.channel;

import android.util.Log;

import com.tencent.qcloud.core.logger.LogEntity;
import com.tencent.qcloud.track.service.ClsTrackService;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Created by jordanqin on 2025/3/21 21:10.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class ClsChannel extends BaseLogChannel {
    private static final String EVENT_CODE_TRACK_COS_SDK_LOG = "qcloud_track_cos_sdk_log";
    private final ClsTrackService clsTrackService;

    public ClsChannel(ClsTrackService clsTrackService) {
        this.clsTrackService = clsTrackService;
    }

    @Override
    public void log(LogEntity entity) {
        if(!isLoggable(entity)) {
            return;
        }

        Map<String, String> map = new HashMap<>(entity.getExtras());
        map.put("timestamp", String.valueOf(entity.getTimestamp()));
        map.put("level", entity.getLevel().name());
        map.put("category", entity.getCategory().name());
        map.put("tag", entity.getTag());
        map.put("message", entity.getMessage());
        map.put("threadName", entity.getThreadName());
        if(entity.getThrowable() != null){
            map.put("throwable", Log.getStackTraceString(entity.getThrowable()));
        }
        clsTrackService.report(EVENT_CODE_TRACK_COS_SDK_LOG, map);
    }

    private boolean isLoggable(LogEntity entity) {
        if (!isEnabled() || entity == null) return false;
        return entity.getLevel().isLoggable(getMinLevel());
    }
}
