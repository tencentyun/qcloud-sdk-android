package com.tencent.cos.xml.common;

/**
 * 服务端加密类型
 *
 * <p>
 * Created by rickenwang on 2020/9/1.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public enum ServerEncryptType {

    /* 不加密*/
    NONE("NONE"),

    SSE_C("SSE-C"),

    SSE_COS("SSE-COS"),

    SSE_KMS("SSE-KMS");

    private String type;

    ServerEncryptType(String type) {

        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ServerEncryptType fromString(String type) {

        for (ServerEncryptType serverEncryptType : ServerEncryptType.values()) {
            if (serverEncryptType.type.equalsIgnoreCase(type)) {
                return serverEncryptType;
            }
        }
        return null;
    }
}
