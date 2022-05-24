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

import java.util.List;

/**
 * 自定义域名配置
 */
public class DomainConfiguration {

    public static final String STATUS_ENABLED = "ENABLED";
    public static final String STATUS_DISABLED = "DISABLED";
    public static final String TYPE_REST = "REST";
    public static final String TYPE_WEBSITE = "WEBSITE";
    public static final String REPLACE_CNAME = "CNAME";
    public static final String REPLACE_TXT = "TXT";

    /**
     * 自定义域名规则列表
     */
    public List<DomainRule> domainRules;

    /**
     * 自定义域名规则
     */
    public static class DomainRule
    {
        public String status;
        public String name;
        public String type;
        public String forcedReplacement;

        public DomainRule(String status, String name, String type) {
            this.status = status;
            this.name = name;
            this.type = type;
        }

        public DomainRule() {}
    }
}
