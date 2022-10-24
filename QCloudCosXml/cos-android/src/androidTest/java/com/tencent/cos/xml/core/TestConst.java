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
    public static final String PERSIST_BUCKET_PIC_PATH = "do_not_remove/image.png";
    public static final String PERSIST_BUCKET_QR_PATH = "do_not_remove/qr.png";
    public static final String PERSIST_BUCKET_VIDEO_PATH = "do_not_remove/video.mp4";
    public static final String PERSIST_BUCKET_SELECT_JSON_PATH = "do_not_remove/select.json";
    public static final String PERSIST_BUCKET_SELECT_CSV_PATH = "do_not_remove/select.csv";
    public static final String PERSIST_BUCKET_DOCUMENT_PATH = "do_not_remove/document.docx";
    public static final String PERSIST_BUCKET_POST_OBJECT_PATH = "do_not_remove/post_object";
    public static final String PERSIST_BUCKET_APPEND_OBJECT_PATH = "do_not_remove/append_object";
    public static final String PERSIST_BUCKET_COPY_OBJECT_DST_PATH = "do_not_remove/copy_dst_object";
    public static final String PERSIST_BUCKET_DEEP_ARCHIVE_OBJECT_PATH = "do_not_remove/small_object_archive";
    public static final String PERSIST_BUCKET_REPLICATION = BuildConfig.PERSIST_BUCKET_REPLICATION;
    public static final String PERSIST_BUCKET_REPLICATION_REGION = BuildConfig.PERSIST_BUCKET_REPLICATION_REGION;

    //审核
    public static final String AUDIT_BUCKET_REGION = "ap-chengdu";
    public static final String AUDIT_BUCKET = "00000000000000-1253960454";
    public static final String AUDIT_BUCKET_IMAGE = " %dimage.jpg";
    public static final String AUDIT_BUCKET_IMAGE_GIF = "image_gif_%d.gif";
    public static final String AUDIT_BUCKET_VIDEO = "test.mp4";
    public static final String AUDIT_BUCKET_AUDIO = "16k_ch_and_en.mp3";
    public static final String AUDIT_BUCKET_TEXT = "test.txt";
    public static final String AUDIT_BUCKET_DOCUMENT = "03_路由.pdf";
    public static final String AUDIT_WEBPAGE = "https://tech.meituan.com/2021/06/10/react-native-hybrid-practice-dsl-in-meituan.html";

    //声音识别
    public static final String ASR_BUCKET_REGION = "ap-guangzhou";
    public static final String ASR_BUCKET = "ci-auditing-sample-1253960454";
    public static final String ASR_OBJECT_FF_1C = "asr/ff-16b-1c-44100hz.aac";
    public static final String ASR_OBJECT_FF_2C = "asr/ff-16b-2c-44100hz.aac";
    public static final String ASR_OBJECT_GS_1C = "asr/gs-16b-1c-8000hz.amr";
    public static final String ASR_OBJECT_GS_2C = "asr/gs-16b-2c-44100hz.aac";
    public static final String ASR_OBJECT_LONG = "asr/aaa.m4a";
    public static final String ASR_OBJECT_OUTPUT = "asr/output.txt";
    public static final String ASR_QUEUE_ID = "p74b5265ab1df455782b7b355007d0dfc";

    //声音识别（极速识别）
    public static final String ASR_FLASH_BUCKET_REGION = "ap-chongqing";
    public static final String ASR_FLASH_BUCKET = "tinna-media-1253960454";
    public static final String ASR_FLASH_OBJECT_FF_1C = "asr/ff-16b-1c-44100hz.aac";
    public static final String ASR_FLASH_OBJECT_FF_2C = "asr/ff-16b-2c-44100hz.aac";
    public static final String ASR_FLASH_OBJECT_GS_1C = "asr/gs-16b-1c-8000hz.amr";
    public static final String ASR_FLASH_OBJECT_GS_2C = "asr/gs-16b-2c-44100hz.aac";
    public static final String ASR_FLASH_OBJECT_LONG = "asr/aaa.m4a";
    public static final String ASR_FLASH_OBJECT_OUTPUT = "asr/output.txt";
    public static final String ASR_FLASH_QUEUE_ID = "pf4bb837618c84b5b905a1df5c8a56d20";

    //分词
    public static final String WORDS_GENERALIZE_BUCKET_REGION = "ap-chongqing";
    public static final String WORDS_GENERALIZE_BUCKET = "tinna-media-1253960454";
    public static final String WORDS_GENERALIZE_QUEUE_ID = "p4c62b2f626f347d982d1166695766d26";
    public static final String WORDS_GENERALIZE_OBJECT = "test.txt";
    public static final String WORDS_GENERALIZE_JOB_ID = "af77abe4a239a11eda0c10f20b65ba637";

    public static final long PERSIST_BUCKET_SMALL_OBJECT_SIZE = 1024 * 1024;
    public static final long PERSIST_BUCKET_BIG_OBJECT_SIZE = 10 * 1024 * 1024;
    public static final String PERSIST_BUCKET_SMALL_OBJECT_PATH = "do_not_remove/small_object";
    public static final String PERSIST_BUCKET_BIG_OBJECT_PATH = "do_not_remove/big_object";

    public static final String PERSIST_BUCKET_CSE_SMALL_OBJECT_PATH = "do_not_remove/cse_small_object";
    public static final String PERSIST_BUCKET_CSE_BIG_OBJECT_PATH = "do_not_remove/cse_big_object";

    public static final String TEMP_BUCKET_REGION = BuildConfig.TEMP_BUCKET_REGION;
    public static final String TEMP_BUCKET = BuildConfig.TEMP_BUCKET;

    public static final Boolean WEAK_NETWORK_TEST = BuildConfig.WEAK_NETWORK_TEST;

    public static final Boolean QUIC_TEST = BuildConfig.QUIC_TEST;
    public static final String QUIC_BUCKET_REGION = BuildConfig.QUIC_BUCKET_REGION;
    public static final String QUIC_BUCKET = BuildConfig.QUIC_BUCKET;
    public static final String QUIC_TEST_IP = BuildConfig.QUIC_TEST_IP;

}
