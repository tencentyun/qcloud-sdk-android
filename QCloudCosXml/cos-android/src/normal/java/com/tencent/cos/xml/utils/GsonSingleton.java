package com.tencent.cos.xml.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * <p>
 * Created by jordanqin on 2024/6/5 17:19.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class GsonSingleton {
    private static Gson gsonInstance = null;

    private GsonSingleton() {
    }

    public static synchronized Gson getInstance() {
        if (gsonInstance == null) {
            gsonInstance = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create();
        }
        return gsonInstance;
    }
}
