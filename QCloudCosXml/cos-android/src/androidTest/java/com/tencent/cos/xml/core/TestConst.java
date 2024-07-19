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
    public static final String PERSIST_BUCKET_ROOT_FILE_PATH = "do_not_remove";
    public static final String PERSIST_BUCKET_PIC_PATH = "do_not_remove/image.png";
    public static final String PERSIST_BUCKET_PIC_6M_PATH = "do_not_remove/6m.jpg";
    public static final String PERSIST_BUCKET_QR_PATH = "do_not_remove/qr.png";
    public static final String PERSIST_BUCKET_VIDEO_PATH = "do_not_remove/video.mp4";
    public static final String PERSIST_BUCKET_SELECT_JSON_PATH = "do_not_remove/select.json";
    public static final String PERSIST_BUCKET_SELECT_CSV_PATH = "do_not_remove/select.csv";
    public static final String PERSIST_BUCKET_DOCUMENT_PATH = "do_not_remove/document.docx";
    public static final String PERSIST_BUCKET_DOCUMENT_XLSX_PATH = "do_not_remove/document.xlsx";
    public static final String PERSIST_BUCKET_DOCUMENT_TXT_PATH = "do_not_remove/document.txt";
    public static final String PERSIST_BUCKET_POST_OBJECT_PATH = "do_not_remove/post_object";
    public static final String PERSIST_BUCKET_APPEND_OBJECT_PATH = "do_not_remove/append_object";
    public static final String PERSIST_BUCKET_COPY_OBJECT_DST_PATH = "do_not_remove/copy_dst_object";
    public static final String PERSIST_BUCKET_DEEP_ARCHIVE_OBJECT_PATH = "do_not_remove/small_object_archive";
    public static final String PERSIST_BUCKET_REPLICATION = BuildConfig.PERSIST_BUCKET_REPLICATION;
    public static final String PERSIST_BUCKET_REPLICATION_REGION = BuildConfig.PERSIST_BUCKET_REPLICATION_REGION;

    public static final String CI_BUCKET_REGION = "ap-beijing";
    public static final String CI_BUCKET_APPID = "1253960454";
    public static final String CI_BUCKET = "cos-sdk-citest-1253960454";

    //审核
    public static final String AUDIT_BUCKET_PORN_IMAGE = "logotest1.jpg";
    public static final String AUDIT_BUCKET_IMAGE = " %dimage.jpg";
    public static final String AUDIT_BUCKET_IMAGE_GIF = "image_gif_%d.gif";
    public static final String AUDIT_BUCKET_VIDEO = "example.mp4";
    public static final String AUDIT_BUCKET_AUDIO = "example1672060469857.mp3";
    public static final String AUDIT_BUCKET_TEXT = "test.txt";
    public static final String AUDIT_BUCKET_DOCUMENT = "student.ppt";
    public static final String AUDIT_WEBPAGE = "https://tech.meituan.com/2021/06/10/react-native-hybrid-practice-dsl-in-meituan.html";

    // AI内容识别
    public static final String AI_RECOGNITION_BUCKET = "ai-recognition-1253960454";
    public static final String AI_RECOGNITION_IMAGE = "image.jpg";

    //声音识别
    public static final String ASR_BUCKET_REGION = "ap-guangzhou";
    public static final String ASR_BUCKET = "ci-auditing-sample-1253960454";
    public static final String ASR_OBJECT_FF_1C = "asr/ff-16b-1c-44100hz.aac";
    public static final String ASR_OBJECT_FF_2C = "asr/ff-16b-2c-44100hz.aac";
    public static final String ASR_OBJECT_GS_1C = "asr/gs-16b-1c-8000hz.amr";
    public static final String ASR_OBJECT_GS_2C = "asr/gs-16b-2c-44100hz.aac";
    public static final String ASR_OBJECT_GS_2C_URL = "https://ci-auditing-sample-1253960454.cos.ap-guangzhou.myqcloud.com/asr/gs-16b-2c-44100hz.aac";
    public static final String ASR_OBJECT_LONG = "asr/aaa.m4a";
    public static final String ASR_OBJECT_OUTPUT = "asr/output.txt";

    public static final String MEDIA_BUCKET_VIDEO = "media/video.mp4";
    public static final String MEDIA_BUCKET_IMAGE = "media/test.jpg";
    public static final String MEDIA_BUCKET_M3U8 = "media/playlist.m3u8";
    public static final String MEDIA_BUCKET_JOB_RESULT = "media/job_result/";

    //分词
    public static final String WORDS_GENERALIZE_BUCKET_REGION = "ap-chongqing";
    public static final String WORDS_GENERALIZE_BUCKET = "tinna-media-1253960454";
    public static final String WORDS_GENERALIZE_OBJECT = "test.txt";

    public static final long PERSIST_BUCKET_SMALL_OBJECT_SIZE = 1024 * 1024;
    public static final long PERSIST_BUCKET_BIG_OBJECT_SIZE = 10 * 1024 * 1024;
    public static final long PERSIST_BUCKET_BIG_60M_OBJECT_SIZE = 60 * 1024 * 1024;

    public static final String PERSIST_BUCKET_SMALL_OBJECT_PATH = "do_not_remove/small_object";
    public static final String PERSIST_BUCKET_BIG_OBJECT_PATH = "do_not_remove/big_object";
    public static final String PERSIST_BUCKET_BATCH_OBJECT_PATH = "do_not_remove//batch/small_object";
    public static final String PERSIST_BUCKET_BIG_60M_OBJECT_PATH = "do_not_remove/big_60m_object";
    public static final String PERSIST_BUCKET_CSE_SMALL_OBJECT_PATH = "do_not_remove/cse_small_object";
    public static final String PERSIST_BUCKET_CSE_BIG_OBJECT_PATH = "do_not_remove/cse_big_object";

    public static final String TEMP_BUCKET_REGION = BuildConfig.TEMP_BUCKET_REGION;
    public static final String TEMP_BUCKET = BuildConfig.TEMP_BUCKET;
    public static final String TEMP_BUCKET_MAZ = "mobile-ut-temp-maz-1253960454";

    public static final Boolean WEAK_NETWORK_TEST = BuildConfig.WEAK_NETWORK_TEST;

    public static final Boolean QUIC_TEST = BuildConfig.QUIC_TEST;
    public static final String QUIC_BUCKET_REGION = BuildConfig.QUIC_BUCKET_REGION;
    public static final String QUIC_BUCKET = BuildConfig.QUIC_BUCKET;
    public static final String QUIC_TEST_IP = BuildConfig.QUIC_TEST_IP;

    public static final String PERSIST_BUCKET_CDN_SMALL_OBJECT_PATH = "do_not_remove/cdn/small_object";
    public static final String PERSIST_BUCKET_CDN_BIG_OBJECT_PATH = "do_not_remove/cdn/big_object";
    public static final String PERSIST_BUCKET_CDN_BIG_60M_OBJECT_PATH = "do_not_remove/cdn/big_60m_object";

    public static final String PERSIST_BUCKET_CDN_PIC_PATH = "do_not_remove/cdn/image.png";
    public static final String PERSIST_BUCKET_CDN_SMALL_OBJECT_URL = "https://mobile-ut-1253960454.cos.ap-guangzhou.myqcloud.com/do_not_remove/cdn/small_object";
    public static final String PERSIST_BUCKET_CDN_BIG_OBJECT_URL = "https://mobile-ut-1253960454.cos.ap-guangzhou.myqcloud.com/do_not_remove/cdn/big_object";
    public static final String PERSIST_BUCKET_QR_URL = "https://mobile-ut-1253960454.cos.ap-guangzhou.myqcloud.com/do_not_remove/cdn/qr.png";

    public static final String CALLBACK_SECRET_ID = BuildConfig.CALLBACK_SECRET_ID;
    public static final String CALLBACK_SECRET_KEY = BuildConfig.CALLBACK_SECRET_KEY;
    public static final String CALLBACK_PERSIST_BUCKET_REGION = "ap-shanghai";
    public static final String CALLBACK_PERSIST_BUCKET = "test-callback-1252246555";
}
