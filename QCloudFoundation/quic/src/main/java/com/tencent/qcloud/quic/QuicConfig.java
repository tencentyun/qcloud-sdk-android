package com.tencent.qcloud.quic;

/**
 * <p>
 * Created by rickenwang on 2021/11/22.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class QuicConfig {

    public static final int RACE_TYPE_ONLY_QUIC = 0;
    public static final int RACE_TYPE_QUIC_HTTP = 1;
    public static final int RACE_TYPE_ONLY_HTTP = 2;

    int raceType = RACE_TYPE_QUIC_HTTP;
    boolean isCustomProtocol = false;
    int totalTimeoutSec = 0;


    public void setRaceType(int raceType) {
        this.raceType = raceType;
    }

    public void setCustomProtocol(boolean customProtocol) {
        isCustomProtocol = customProtocol;
    }

    public int getRaceType() {
        return raceType;
    }

    public boolean isCustomProtocol() {
        return isCustomProtocol;
    }

    public void setTotalTimeoutSec(int totalTimeoutSec) {
        this.totalTimeoutSec = totalTimeoutSec;
    }

    public int getTotalTimeoutSec() {
        return totalTimeoutSec;
    }
}



