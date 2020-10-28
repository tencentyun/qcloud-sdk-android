package com.tencent.qcloud.qcloudxml.compiler;

public enum TypeXmlKind {
    BOOLEAN,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    CHAR,
    STRING,
    NORMAL_BEAN,
    ARRAY,
    Set,
    LIST,
    MAP;

    public boolean isBaseType() {
        switch (ordinal()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
        }
        return false;
    }
}
