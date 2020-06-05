package com.tencent.cos.xml.model.tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public class COSMetaData {

    private Map<String, String> metaData;

    public COSMetaData() {

        metaData = new HashMap<>();
    }


    public void put(String key, String value) {

        String prefix = "x-cos-meta-";
        if (!key.startsWith(prefix)) {
            key = prefix + key;
        }
        metaData.put(key, value);
    }

    public String get(String key) {

        return metaData.get(key);
    }

    public Set<String> keySet() {

        return metaData.keySet();
    }

}
