package com.tencent.qcloud.track.service;

import com.tencent.qcloud.track.IReport;

/**
 * 抽象数据上报类
 * <p>
 * Created by jordanqin on 2023/9/4 20:50.
 * Copyright 2010-2023 Tencent Cloud. All Rights Reserved.
 */
public abstract class ATrackService implements IReport {
    private boolean debug = false;
    private boolean isCloseReport = false;
    // 是否初始化
    protected boolean isInit = false;

    /**
     * 设置是否关闭上报
     * @param isCloseReport 是否关闭上报
     */
    public void setIsCloseReport(boolean isCloseReport){
        this.isCloseReport = isCloseReport;
    }

    /**
     * 获取是否已关闭上报
     * @return 是否已关闭上报
     */
    public boolean isCloseReport() {
        return isCloseReport;
    }

    /**
     * 获取是否是debug模式
     * @return 是否是debug模式
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * 设置是否是debug模式
     * @param debug 是否是debug模式
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
