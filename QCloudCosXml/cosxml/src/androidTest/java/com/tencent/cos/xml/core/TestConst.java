package com.tencent.cos.xml.core;

import com.tencent.cos.xml.BuildConfig;

/**
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class TestConst {

    public static final String UT_TAG = "QCloudTest";

    public static final String COS_APPID = BuildConfig.COS_APPID;
    public static final String OWNER_UIN = BuildConfig.OWNER_UIN;

    public static final String SECRET_ID = BuildConfig.COS_SECRET_ID;
    public static final String SECRET_KEY = BuildConfig.COS_SECRET_KEY;

    public static final String PERSIST_BUCKET_REGION = BuildConfig.PERSIST_BUCKET_REGION;
    public static final String PERSIST_BUCKET = BuildConfig.PERSIST_BUCKET;
    public static final String PERSIST_BUCKET_CDN_SIGN = BuildConfig.PERSIST_BUCKET_CDN_SIGN;
    public static final String PERSIST_BUCKET_PIC_PATH = "/do_not_remove/image.png";
    public static final String PERSIST_BUCKET_SELECT_JSON_PATH = "/do_not_remove/select.json";
    public static final String PERSIST_BUCKET_SELECT_CSV_PATH = "/do_not_remove/select.csv";
    public static final String PERSIST_BUCKET_DOCUMENT_PATH = "/do_not_remove/document.docx";
    public static final String PERSIST_BUCKET_POST_OBJECT_PATH = "/do_not_remove/post_object";
    public static final String PERSIST_BUCKET_COPY_OBJECT_DST_PATH = "/do_not_remove/copy_dst_object";
    public static final String PERSIST_BUCKET_DEEP_ARCHIVE_OBJECT_PATH = "/do_not_remove/small_object_archive";
    public static final String PERSIST_BUCKET_REPLICATION = BuildConfig.PERSIST_BUCKET_REPLICATION;
    public static final String PERSIST_BUCKET_REPLICATION_REGION = BuildConfig.PERSIST_BUCKET_REPLICATION_REGION;

    public static final long PERSIST_BUCKET_SMALL_OBJECT_SIZE = 1024;
    public static final long PERSIST_BUCKET_BIG_OBJECT_SIZE = 100 * 1024 * 1024;
    public static final String PERSIST_BUCKET_SMALL_OBJECT_PATH = "/do_not_remove/small_object";
    public static final String PERSIST_BUCKET_BIG_OBJECT_PATH = "/do_not_remove/big_object";

    public static final String TEMP_BUCKET_REGION = BuildConfig.TEMP_BUCKET_REGION;
    public static final String TEMP_BUCKET = BuildConfig.TEMP_BUCKET;

    public static final Boolean WEAK_NETWORK_TEST = BuildConfig.WEAK_NETWORK_TEST;

    public static final Boolean QUIC_TEST = BuildConfig.QUIC_TEST;
    public static final String QUIC_BUCKET_REGION = BuildConfig.QUIC_BUCKET_REGION;
    public static final String QUIC_BUCKET = BuildConfig.QUIC_BUCKET;
    public static final String QUIC_TEST_IP = BuildConfig.QUIC_TEST_IP;

}
