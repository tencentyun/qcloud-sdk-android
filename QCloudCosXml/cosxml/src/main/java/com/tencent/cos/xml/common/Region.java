/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.common;

/**
 * 地域
 * 请参考：<a herf="https://cloud.tencent.com/document/product/436/6224">地域和访问域名</a>
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
