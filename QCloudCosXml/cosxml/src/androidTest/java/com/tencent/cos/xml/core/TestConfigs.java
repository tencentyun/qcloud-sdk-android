package com.tencent.cos.xml.core;

import android.os.Environment;

import com.tencent.cos.xml.BuildConfig;
import com.tencent.cos.xml.CosXmlService;

import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;

import org.junit.Assert;

import java.io.File;
import java.io.IOException;

public class TestConfigs {

    public static final String UNIT_TEST_TAG = "UnitTest";

    // 终端申请的账号信息
    public static final String TERMINAL_UIN = BuildConfig.COS_UIN;
    public static final String TERMINAL_APPID = BuildConfig.COS_APPID;
    public static final String TERMINAL_SECRET_ID = BuildConfig.COS_SECRET_ID;
    public static final String TERMINAL_SECRET_KEY = BuildConfig.COS_SECRET_KEY;
    public static final String TERMINAL_DEFAULT_REGION = BuildConfig.REGION;
    public static final String TERMINAL_PERSIST_BUCKET = BuildConfig.BUCKET_PERSIST; // "android-ut-persist-bucket";
    public static final String TERMINAL_TEMP_BUCKET = BuildConfig.BUCKET_TEMP; //"android-ut-temp-bucket";


    // 默认保存在 TERMINAL_TEMP_BUCKET/ut_files/ 目录下

    public static final String COS_PIC_PATH = "/ut_files/pic.png";  // COS 文件地址
    public static final String COS_TXT_1M_PATH = "/ut_files/1M.txt";  // COS 1M 文件地址
    public static final String COS_TXT_100M_PATH = "/ut_files/100M.txt"; // COS 100M 文件地址
    public static final String COS_TXT_1G_PATH = "/ut_files/1G.txt"; // COS 1G 文件地址

    public static final String LOCAL_FILE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ut_files";
    public static final String LOCAL_PIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ut_files/pic.png";  // 本地图片文件地址，通过 COS_PIC_PATH 下载
    public static final String LOCAL_TXT_1M_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ut_files/1M.txt";  // 本地 1M 文件地址，本地生成
    public static final String LOCAL_TXT_100M_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ut_files/100M.txt"; // 本地 100M 文件地址，本地生成
    public static final String LOCAL_TXT_1G_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ut_files/1G.txt"; // 本地 1G 文件地址，本地生成


    static {



        try {
            boolean mkDirs = new File(LOCAL_FILE_DIRECTORY).mkdirs();
            TestUtils.createFile(LOCAL_TXT_1M_PATH, 1024 * 1024);
            TestUtils.createFile(LOCAL_TXT_100M_PATH, 100 * 1024 * 1024);
            TestUtils.createFile(LOCAL_TXT_1G_PATH, 1024 * 1024 * 1024);
            downloadPic();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void downloadPic() {
        CosXmlService cosXmlService = TestUtils.newDefaultTerminalService();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TERMINAL_PERSIST_BUCKET, COS_PIC_PATH, LOCAL_PIC_PATH);
        try {
            GetObjectResult getObjectResult = cosXmlService.getObject(getObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
