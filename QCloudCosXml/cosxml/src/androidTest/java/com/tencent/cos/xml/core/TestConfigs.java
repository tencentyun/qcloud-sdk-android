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

    public static final String UNIT_TEST_TAG = "QCloudHttp";

    // 终端申请的账号信息
    public static final String TERMINAL_UIN = BuildConfig.COS_UIN;
    public static final String TERMINAL_APPID = BuildConfig.COS_APPID;
    public static final String TERMINAL_SECRET_ID = BuildConfig.COS_SECRET_ID;
    public static final String TERMINAL_SECRET_KEY = BuildConfig.COS_SECRET_KEY;
    public static final String TERMINAL_DEFAULT_REGION = BuildConfig.REGION;
    public static final String TERMINAL_PERSIST_BUCKET = BuildConfig.BUCKET_PERSIST; // "android-ut-persist-bucket";
    public static final String TERMINAL_PERSIST_BUCKET_BJ = BuildConfig.BUCKET_PERSIST_BJ;
    public static final String TERMINAL_TEMP_BUCKET = BuildConfig.BUCKET_TEMP; //"android-ut-temp-bucket";

    public static final String COS_SUB_SECRET_ID = BuildConfig.COS_SUB_SECRET_ID;
    public static final String COS_SUB_SECRET_KEY = BuildConfig.COS_SUB_SECRET_KEY;
    public static final String COS_SUB_BUCKET_PERSIST = BuildConfig.COS_SUB_BUCKET_PERSIST;
    public static final String COS_SUB_BUCKET_PERSIST_REGION = BuildConfig.COS_SUB_BUCKET_PERSIST_REGION;

    public static final String COS_SUB_BUCKET_QUIC = BuildConfig.COS_SUB_BUCKET_QUIC;
    public static final String COS_SUB_BUCKET_QUIC_REGION = BuildConfig.COS_SUB_BUCKET_QUIC_REGION;

    public static final String COS_SUB_BUCKET_CDN_SIGN = BuildConfig.COS_SUB_BUCKET_CDN_SIGN;
    public static final String COS_QUIC_TEST_IP = BuildConfig.COS_QUIC_TEST_IP;

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
            // downloadPic();

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
