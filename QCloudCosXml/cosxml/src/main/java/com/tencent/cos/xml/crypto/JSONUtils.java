package com.tencent.cos.xml.crypto;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Created by rickenwang on 2021/7/5.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class JSONUtils {

    static String toJsonString(Map<String, String> map) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        return jsonObject.toString();
    }

    static Map<String, String> toMap(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Map<String, String> map = new HashMap<>();
        Iterator<String> iterator  = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = jsonObject.getString(key);
            map.put(key, value);
        }
        return map;
    }
    
}
