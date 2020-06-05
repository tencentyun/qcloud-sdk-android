package com.tencent.cos.xml.model.tag;

/**
 * Created by bradyxiao on 2017/11/26.
 */

public class LocationConstraint {
    public String location;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{LocationConstraint:\n");
        stringBuilder.append("Location:").append(location).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
