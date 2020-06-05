package com.tencent.cos.xml.model.tag;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class VersioningConfiguration {
    public String status;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{VersioningConfiguration:\n");
        stringBuilder.append("Status:").append(status).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
