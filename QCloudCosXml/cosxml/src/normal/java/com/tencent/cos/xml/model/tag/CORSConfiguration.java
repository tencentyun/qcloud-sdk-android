package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class CORSConfiguration {
    public List<CORSRule> corsRules;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{CORSConfiguration:\n");
        if(corsRules != null){
            for(CORSRule corsRule : corsRules){
                if(corsRule != null) stringBuilder.append(corsRule.toString()).append("\n");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class CORSRule{
        public String id;
        public String allowedOrigin;
        public List<String> allowedMethod;
        public List<String> allowedHeader;
        public List<String> exposeHeader;
        public int maxAgeSeconds;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{CORSRule:\n");
            stringBuilder.append("ID:").append(id).append("\n");
            stringBuilder.append("AllowedOrigin:").append(allowedOrigin).append("\n");
            if(allowedMethod != null){
                for (String method : allowedMethod){
                    if(method != null)stringBuilder.append("AllowedMethod:").append(method).append("\n");
                }
            }
            if(allowedHeader != null){
                for (String header : allowedHeader){
                    if(header != null)stringBuilder.append("AllowedHeader:").append(header).append("\n");
                }
            }
            if(exposeHeader != null){
                for (String exposeHeader : exposeHeader){
                    if(exposeHeader != null)stringBuilder.append("ExposeHeader:").append(exposeHeader).append("\n");
                }
            }
            stringBuilder.append("MaxAgeSeconds:").append(maxAgeSeconds).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}
