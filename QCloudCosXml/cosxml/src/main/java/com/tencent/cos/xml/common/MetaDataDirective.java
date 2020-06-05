package com.tencent.cos.xml.common;

/**
 * Created by bradyxiao on 2017/9/20.
 * 是否拷贝元数据
 * <ul>
 *     <li>Copy : 忽略请求头中 Header 中的用户元数据信息，直接复制原对象的元数据</li>
 *     <li>Replaced : 按请求头中 Header 信息修改元数据，当目标路径和原路径一致，即用户试图修改元数据时，必须为 Replaced</li>
 * </ul>
 */
public enum MetaDataDirective {
    /** 直接复制原对象的元数据 */
    COPY("Copy"),
    /** 修改元数据 */
    REPLACED("Replaced");

    String directive;
    MetaDataDirective(String directive){
        this.directive = directive;
    }

    public String getMetaDirective(){
        return directive;
    }

    public static MetaDataDirective fromValue(String value){
        for(MetaDataDirective metaDataDirective : MetaDataDirective.values()){
            if(metaDataDirective.directive.equalsIgnoreCase(value)){
                return metaDataDirective;
            }
        }
        return null;
    }
}
