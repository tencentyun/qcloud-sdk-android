package com.tencent.cos.xml.common;

/**
 * Created by bradyxiao on 2017/5/7.
 * author bradyxiao
 */
public enum Region {
    /***
     *  cos region
     *  cn-north ：华北
     *  cn-south ：华南
     *  cn-east ：华东
     *  cn-southwest: 西南
     *  sg ：新加坡
     */
    AP_Beijing_1("ap-beijing-1"),

    AP_Beijing("ap-beijing"),

    AP_Shanghai("ap-shanghai"),

    AP_Guangzhou("ap-guangzhou"),

    AP_Guangzhou_2("ap-guangzhou-2"),

    AP_Chengdu("ap-chengdu"),

    AP_Singapore("ap-singapore"),

    AP_Hongkong("ap-hongkong"),

    NA_Toronto("na-toronto"),

    EU_Frankfurt("eu-frankfurt"),

    /**@see #AP_Beijing_1*/
    @Deprecated
    CN_NORTH("cn-north"),

    /**@see #AP_Guangzhou*/
    @Deprecated
    CN_SOUTH("cn-south"),

    /**@see #AP_Shanghai*/
    @Deprecated
    CN_EAST("cn-east"),

    /**@see #AP_Chengdu*/
    @Deprecated
    CN_SOUTHWEST("cn-southwest"),

    /**@see #AP_Singapore*/
    @Deprecated
    SG("sg");
    private String region;
    Region(String region) {
        this.region = region;
    }
    public String getRegion(){
        return region;
    }

    public static Region fromValue(String region){
        for(Region region1 : Region.values()){
            if(region1.region.equalsIgnoreCase(region)){
                return region1;
            }
        }
        return null;
    }

}
