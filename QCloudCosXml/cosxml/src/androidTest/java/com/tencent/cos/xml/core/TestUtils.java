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


import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class TestUtils {


    public static Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getContext();
    }

    public static boolean createFile(String absolutePath, long fileLength) throws IOException {

        File file = new File(absolutePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        RandomAccessFile accessFile = new RandomAccessFile(absolutePath, "rws");
        accessFile.setLength(fileLength);
        accessFile.seek(fileLength/2);
        accessFile.write(new Random().nextInt(200));
        accessFile.seek(fileLength - 1);
        accessFile.write(new Random().nextInt(200));
        accessFile.close();
        return true;
    }

    public static boolean createRandomComplexFile(String absolutePath, long fileLength) throws IOException {

        File file = new File(absolutePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        RandomAccessFile accessFile = new RandomAccessFile(absolutePath, "rws");
        accessFile.setLength(fileLength);

        for (int i = 0; i < fileLength; i += 1024) {
            accessFile.seek(i);
            accessFile.write(new Random().nextInt(200));
        }
        accessFile.seek(fileLength - 1);
        accessFile.write(new Random().nextInt(200));
        accessFile.close();
        return true;
    }

    public static CosXmlService newDefaultTerminalService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion(TestConfigs.TERMINAL_DEFAULT_REGION)
                .setDebuggable(true)
                // .setAccelerate(true)
                // .dnsCache(false)
                .builder();

        return new CosXmlService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConfigs.TERMINAL_SECRET_ID, TestConfigs.TERMINAL_SECRET_KEY,600) );
    }

    public static CosXmlService newQuicTerminalService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion(TestConfigs.COS_SUB_BUCKET_QUIC_REGION)
                .setDebuggable(true)
                .enableQuic(true)
                .builder();

        CosXmlService cosXmlService = new CosXmlService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConfigs.COS_SUB_SECRET_ID, TestConfigs.COS_SUB_SECRET_KEY,600) );
        try {
            cosXmlService.addCustomerDNS("cos-quic-test-1253960454.cos.ap-beijing.myqcloud.com", new String[] {TestConfigs.COS_QUIC_TEST_IP});
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        return cosXmlService;
    }

    public static CosXmlService newCdnTerminalService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion(TestConfigs.TERMINAL_DEFAULT_REGION)
                .setDebuggable(true)
                .setHostFormat("${bucket}.file.myqcloud.com")
                .addHeader("Host", "android-ut-persist-gz-1253653367.file.myqcloud.com")
                .builder();

        return new CosXmlService(getContext(), cosXmlServiceConfig);
    }

    public static TransferManager newCdnTerminalTransferManager() {
        CosXmlService cosXmlService = newCdnTerminalService();
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferManager(cosXmlService, transferConfig);
    }


    /**
     * 通过 cdn 域名下载，需要 1. 开启 cdn 回源鉴权；2. 开启 cdn 鉴权。详情请参考：
     * https://cloud.tencent.com/document/product/436/18669
     *
     *
     * </p>
     * 注意，这样创建的 TransferManager 只能用于 cdn 下载，不能用于上传，或者通过源站域名下载
     */
    public static TransferManager newCdnTransferManager() {

        /**
         * 假设您的 bucket 为 example-1250000000，地域为 ap-beijing
         */
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion("ap-beijing")
                .setDebuggable(false)
                .setHostFormat("${bucket}.file.myqcloud.com")
                .addHeader("Host", "example-1250000000.file.myqcloud.com")
                .builder();

        /**
         * 通过 cdn 下载，并开启回源鉴权后，
         */
        CosXmlService cosXmlService = new CosXmlService(getContext(), cosXmlServiceConfig);

        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferManager(cosXmlService, transferConfig);
    }


    public static TransferManager newDefaultTerminalTransferManager() {

        CosXmlService cosXmlService = newDefaultTerminalService();
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferManager(cosXmlService, transferConfig);
    }

    public static TransferManager newQuicTerminalTransferManager() {

        CosXmlService cosXmlService = newQuicTerminalService();
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferManager(cosXmlService, transferConfig);
    }

    public static TransferManager newDefaultTerminalTransferManager(long sliceSize) {

        CosXmlService cosXmlService = newDefaultTerminalService();
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setSliceSizeForUpload(sliceSize)
                .build();
        return new TransferManager(cosXmlService, transferConfig);
    }


    public static String mergeExceptionMessage(CosXmlClientException clientException, CosXmlServiceException serviceException) {

        if (clientException != null) {
            return clientException.getMessage();
        }
        if (serviceException != null) {
            return serviceException.getErrorCode() + " : " + serviceException.getErrorMessage();
        }
        return "Unknown Error";
    }


    public static boolean removeLocalFile(String filePath) {

        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
