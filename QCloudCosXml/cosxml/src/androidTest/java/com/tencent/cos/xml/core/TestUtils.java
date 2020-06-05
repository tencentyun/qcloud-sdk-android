package com.tencent.cos.xml.core;

import android.support.test.InstrumentationRegistry;

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
                .setAppidAndRegion(TestConfigs.TERMINAL_APPID, TestConfigs.TERMINAL_DEFAULT_REGION)
                .setDebuggable(true)
                // .setAccelerate(true)
                // .dnsCache(false)
                .builder();

        return new CosXmlService(InstrumentationRegistry.getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConfigs.TERMINAL_SECRET_ID, TestConfigs.TERMINAL_SECRET_KEY,600) );
    }


    public static TransferManager newDefaultTerminalTransferManager() {

        CosXmlService cosXmlService = newDefaultTerminalService();
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
