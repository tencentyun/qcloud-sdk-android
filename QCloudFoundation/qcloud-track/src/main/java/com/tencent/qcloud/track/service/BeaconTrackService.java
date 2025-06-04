package com.tencent.qcloud.track.service;

import android.content.Context;
import android.util.Log;

import com.tencent.beacon.event.open.BeaconEvent;
import com.tencent.beacon.event.open.BeaconReport;
import com.tencent.beacon.event.open.EventResult;
import com.tencent.beacon.event.open.EventType;
import com.tencent.qcloud.track.Constants;
import com.tencent.qcloud.track.TrackExecutors;
import com.tencent.qcloud.track.utils.EventFileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 灯塔数据上报
 * <p>
 * Created by jordanqin on 2023/9/4 19:36.
 * Copyright 2010-2023 Tencent Cloud. All Rights Reserved.
 */
public class BeaconTrackService extends ATrackService {
    private static final String TAG = "BeaconTrackService";
    private Context context;
    private String beaconKey;

    public void init(String beaconKey){
        if(isInit) return;

        isInit = true;
        this.beaconKey = beaconKey;
        if (BeaconTrackService.isInclude()) {
            //是否打开日志
            BeaconReport.getInstance().setLogAble(isDebug());
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private boolean isSimpleDataTrackService(){
        return Constants.SIMPLE_DATA_BEACON_APP_KEY.equals(beaconKey);
    }

    @Override
    public void report(String eventCode, Map<String, String> params) {
        // 如果上报的时候还未初始化，先将数据保存到本地文件
        if(!isInit && !isSimpleDataTrackService()){
            TrackExecutors.COMMAND_EXECUTOR.execute(() ->
                    EventFileUtils.appendEventToFile(context, eventCode, params)
            );
        }

        if(isCloseReport() || !isInit || !isInclude()) return;

        // 如果本地存在事件缓存 则进行上报
        if(!isSimpleDataTrackService() && EventFileUtils.eventFileExists(context)){
            TrackExecutors.COMMAND_EXECUTOR.execute(() -> {
                List<String> eventCodes = new ArrayList<>();
                List<Map<String, String>> paramsList = new ArrayList<>();
                EventFileUtils.readEventFromFile(context, eventCodes, paramsList);
                for (int i = 0; i< eventCodes.size(); i++){
                    directReport(eventCodes.get(i), paramsList.get(i));
                }
            });
        }

        directReport(eventCode, params);
    }

    private void directReport(String eventCode, Map<String, String> params) {
        BeaconEvent.Builder builder = BeaconEvent.builder()
                .withAppKey(this.beaconKey)
                .withCode(eventCode)
                .withType(EventType.NORMAL)
                .withParams(params);
        EventResult result = BeaconReport.getInstance().report(builder.build());
        if (isDebug()) {
            StringBuilder mapAsString = new StringBuilder("{");
            for (String key : params.keySet()) {
                mapAsString.append(key + "=" + params.get(key) + ", ");
            }
            mapAsString.delete(mapAsString.length() - 2, mapAsString.length()).append("}");
            Log.i(TAG, String.format("beaconKey: %s, eventCode: %s, params: %s => result{ eventID: %s, errorCode: %d, errorMsg: %s}",
                    beaconKey, eventCode, mapAsString, result.eventID, result.errorCode, result.errMsg));
        }
    }

    @Override
    public void setDebug(boolean debug) {
        super.setDebug(debug);
        if (BeaconTrackService.isInclude()) {
            BeaconReport.getInstance().setLogAble(debug);
        }
    }

    /**
     * 获取是否包含灯塔模块
     * @return 是否包含灯塔模块
     */
    public static boolean isInclude() {
        try {
            Class.forName("com.tencent.beacon.event.open.BeaconReport");
            return true;
        } catch (ClassNotFoundException e){
            return false;
        }
    }
}
