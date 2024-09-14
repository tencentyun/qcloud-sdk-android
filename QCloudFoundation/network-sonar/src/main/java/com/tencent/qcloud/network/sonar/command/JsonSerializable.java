package com.tencent.qcloud.network.sonar.command;

import org.json.JSONObject;

/**
 * <p>
 * Created by jordanqin on 2024/8/22 17:55.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public interface JsonSerializable {
    JSONObject toJson();
}
