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

package com.tencent.cos.xml.model.tag;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean
public class RefererConfiguration {
    /**
     * 是否开启防盗链，枚举值：Enabled、Disabled
     */
    public String status;

    /**
     * 防盗链类型，枚举值：Black-List、White-List
     */
    public String refererType;

    /**
     * 生效域名列表， 支持多个域名且为前缀匹配， 支持带端口的域名和 IP， 支持通配符*，做二级域名或多级域名的通配
     */
    public List<Domain> domainList;

    /**
     * 是否允许空 Referer 访问，枚举值：Allow、Deny，默认值为 Deny
     */
    public String emptyReferConfiguration;

    /**
     * 是否开启防盗链
     * @return 是否开启防盗链
     */
    public boolean getEnabled(){
        return "Enabled".equals(status);
    }

    /**
     * 设置是否开启防盗链
     * @param enabled 是否开启防盗链
     */
    public void setEnabled(boolean enabled){
        if(enabled){
            status = "Enabled";
        } else {
            status = "Disabled";
        }
    }

    /**
     * 获取防盗链类型
     * @return 防盗链类型
     */
    public RefererType getRefererType(){
        return RefererType.fromString(refererType);
    }

    /**
     * 设置防盗链类型
     * @param refererType 防盗链类型
     */
    public void setRefererType(RefererType refererType){
        this.refererType = refererType.getTypeStr();
    }

    /**
     * 是否允许空 Referer 访问
     * @return 是否允许空 Referer 访问
     */
    public boolean getAllowEmptyRefer(){
        return "Allow".equals(emptyReferConfiguration);
    }

    /**
     * 设置是否允许空 Referer 访问
     * @param allowEmptyRefer 是否允许空 Referer 访问
     */
    public void setAllowEmptyRefer(boolean allowEmptyRefer){
        if(allowEmptyRefer){
            emptyReferConfiguration = "Allow";
        } else {
            emptyReferConfiguration = "Deny";
        }
    }

    /**
     * 单条生效域名，例如 www.qq.com/example，192.168.1.2:8080， *.qq.com
     */
    @XmlBean
    public static class Domain{
        public Domain() {
        }

        public Domain(String domain) {
            this.domain = domain;
        }
        @XmlElement(ignoreName = true)
        public String domain;
    }

    /**
     * 防盗链类型，枚举值：Black-List、White-List
     */
    public enum RefererType{
        Black("Black-List"),
        White("White-List");

        private final String type;
        RefererType(String type) {
            this.type = type;
        }
        public String getTypeStr(){
            return this.type;
        }
        public static RefererType fromString(String typeStr){
            for(RefererType type : RefererType.values()){
                if(type.getTypeStr().equalsIgnoreCase(typeStr)){
                    return type;
                }
            }
            return null;
        }
    }
}
