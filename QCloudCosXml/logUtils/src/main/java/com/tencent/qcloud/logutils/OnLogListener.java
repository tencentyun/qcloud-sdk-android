package com.tencent.qcloud.logutils;

import java.io.File;

/**
 * Created by bradyxiao on 2018/10/25.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */

public interface OnLogListener {
    File[] onLoad();
}
