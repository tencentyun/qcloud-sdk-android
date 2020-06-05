package com.tencent.qcloud.core.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by rickenwang on 2020-01-03.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class ContextHolder {

    private static Context appContext;

    public static void setContext(@NonNull Context context) {
        appContext = context.getApplicationContext();
    }

    @Nullable public static Context getAppContext() {
        return appContext;
    }
}
