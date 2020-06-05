package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2019/9/3.
 * Copyright (c) 2016-2020 Tencent QCloud All rights reserved.
 */
public class DomainConfiguration {

    public static final String STATUS_ENABLED = "ENABLED";
    public static final String STATUS_DISABLED = "DISABLED";
    public static final String TYPE_REST = "REST";
    public static final String TYPE_WEBSITE = "WEBSITE";
    public static final String REPLACE_CNAME = "CNAME";
    public static final String REPLACE_TXT = "TXT";

    public List<DomainRule> domainRules;
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
