package com.tencent.cos.xml.listener;


import com.tencent.cos.xml.model.tag.eventstreaming.SelectObjectContentEvent;

/**
 * Created by rickenwang on 2019-11-06.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public interface SelectObjectContentListener {

    /**
     * 通过这个接口来周期返回查询的结果
     *
     * @param event
     */
    void onProcess(SelectObjectContentEvent event);
}
