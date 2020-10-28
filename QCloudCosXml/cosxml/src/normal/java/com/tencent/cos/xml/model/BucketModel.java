package com.tencent.cos.xml.model;

import com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration;

/**
 * <p>
 * Created by rickenwang on 2020/9/29.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class BucketModel {


    /**
     * 存储桶智能分层
     */
    public static class BucketIntelligentTiering {

        public static class Request {

            private IntelligentTieringConfiguration intelligentTieringConfiguration;
        }


        public static class Result {


        }
    }



}
