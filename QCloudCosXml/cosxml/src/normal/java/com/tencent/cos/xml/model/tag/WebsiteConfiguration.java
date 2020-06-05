package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2019/8/19.
 * Copyright (c) 2016-2020 Tencent QCloud All rights reserved.
 */
public class WebsiteConfiguration {

    public IndexDocument indexDocument;
    public ErrorDocument errorDocument;
    public RedirectAllRequestTo redirectAllRequestTo;
    public List<RoutingRule> routingRules;

    public static class IndexDocument{
        public String suffix;
    }

    public static class ErrorDocument{
        public String key;
    }

    public static class RedirectAllRequestTo{
        public String protocol;
    }

    public static class RoutingRule{
        public Contidion contidion;
        public Redirect redirect;
    }

    public static class Contidion{
        public int httpErrorCodeReturnedEquals = -1;
        public String keyPrefixEquals;
    }

    public static class Redirect{
        public String protocol;
        public String replaceKeyWith;
        public String replaceKeyPrefixWith;
    }
}
