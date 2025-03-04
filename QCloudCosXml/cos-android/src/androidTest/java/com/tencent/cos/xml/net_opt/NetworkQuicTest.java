package com.tencent.cos.xml.net_opt;

import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_BIG_OBJECT_SIZE;
import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_SMALL_OBJECT_SIZE;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * <p>
 * Created by jordanqin on 2025/1/20 22:07.
 * Copyright 2010-2025 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetworkQuicTest {
    private static final String TAG = "NetworkQuicTest";
    public static final String[] NET_BUCKET_REGION_ARR = new String[]{
//            "ap-hongkong",
//            "ap-tokyo",
            "na-siliconvalley",
//            "eu-frankfurt",
//            "sa-saopaulo"
    };
    private static final int requestCount = 1;
    private static final String[] NET_BUCKET_ARR = new String[NET_BUCKET_REGION_ARR.length];
    private static final ArrayList<NetMetrics> NET_METRICS_ARR = new ArrayList<>();

    @Before
    public void before() {
        for (int i = 0; i < NET_BUCKET_REGION_ARR.length; i++) {
            NET_BUCKET_ARR[i] = "net-" + NET_BUCKET_REGION_ARR[i].split("-")[1] + "-1257101689";
            if ("net-siliconvalley-1257101689".equals(NET_BUCKET_ARR[i])) {
                // 特殊处理，net-siliconvalley-1257101689作为桶名太长
                NET_BUCKET_ARR[i] = "net-siliconvall-1257101689";
            }
        }
    }

    @After
    public void after() {
        File file = new File(TestUtils.getContext().getApplicationContext().getFilesDir().getAbsolutePath(), "MyExcelFile.xls");
        if (file.exists()) {
            file.delete();
        }
        try (OutputStream os = new FileOutputStream(file)) {
            Workbook wb = new Workbook(os, "MyApplication", "1.0");
            Worksheet ws = wb.newWorksheet("net");

//            ws.value(0, 0, "type");
//            ws.value(0, 1, "action");
//            ws.value(0, 2, "region");
//            ws.value(0, 3, "timeConsuming");
//            ws.value(0, 4, "successRate");

            for (int i = 0; i < NET_METRICS_ARR.size(); i++) {
                NetMetrics netMetrics = NET_METRICS_ARR.get(i);
                ws.value(i, 0, netMetrics.type);
                ws.value(i, 1, netMetrics.action);
                ws.value(i, 2, netMetrics.region);
                ws.value(i, 3, netMetrics.timeConsuming);
                ws.value(i, 4, netMetrics.successRate);
            }
            wb.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDefault() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .setDebuggable(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();
        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newMeService(cosXmlServiceConfig);

//        testPutObject(cosXmlService, true, "默认");
        testPutObject(cosXmlService, false, "默认");
//        testGetObject(cosXmlService, true, "默认");
//        testGetObject(cosXmlService, false, "默认");
    }

    @Test
    public void testEo() {
//        testPutObject(null, true, "EO");
        testPutObject(null, false, "EO");
//        testGetObject(null, true, "EO");
//        testGetObject(null, false, "EO");
    }

    @Test
    public void testEoNoQuic() {
//        testPutObject(null, true, "EONoQuic");
        testPutObject(null, false, "EONoQuic");
//        testGetObject(null, true, "EONoQuic");
//        testGetObject(null, false, "EONoQuic");
    }

    private void testPutObject(CosXmlSimpleService cosXmlService, boolean isBig, String type) {
        for (int r = 0; r < NET_BUCKET_REGION_ARR.length; r++) {
            String region = NET_BUCKET_REGION_ARR[r];
            String bucket = NET_BUCKET_ARR[r];

            if (type.startsWith("EO")) {
                // EO 目前只测试境外地域
                if (!("net-tokyo-1257101689".equals(bucket) || "net-singapore-1257101689".equals(bucket) || "net-siliconvall-1257101689".equals(bucket) ||
                        "net-seoul-1257101689".equals(bucket) || "net-saopaulo-1257101689".equals(bucket) || "net-jakarta-1257101689".equals(bucket) ||
                        "net-hongkong-1257101689".equals(bucket) || "net-frankfurt-1257101689".equals(bucket) || "net-bangkok-1257101689".equals(bucket) || "net-ashburn-1257101689".equals(bucket))) {
                    continue;
                }

                CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                        .setDebuggable(true)
                        .enableQuic("EO".equals(type))
                        .setHost(bucket.split("-")[1]+".jordanqin.online")
                        .addNoSignHeaders("Host")
                        .setRegion(region)
//                        .setNetworkSwitchStrategy(CosXmlServiceConfig.RequestNetworkStrategy.Conservative)
                        .builder();
                cosXmlService = ServiceFactory.INSTANCE.newMeService(cosXmlServiceConfig);
            }

            String localPath = TestUtils.localPath("1642166999131.m4a");
            try {
                TestUtils.createFile(localPath, isBig ? PERSIST_BUCKET_BIG_OBJECT_SIZE : PERSIST_BUCKET_SMALL_OBJECT_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int successCount = 0;
            long successTimeConsuming = 0;
            for (int i = 0; i < requestCount; i++) {
                Log.d(TAG, "testPutObject" + (isBig ? "Big" : "Small") + i + " Start: " + region + " " + bucket);
                long startTime = System.currentTimeMillis();
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, isBig ? TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH : TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, localPath);
//                putObjectRequest.setNetworkType(CosXmlRequest.RequestNetworkType.OKHTTP);
                putObjectRequest.setRegion(region);
                try {
                    cosXmlService.putObject(putObjectRequest);
                    successCount++;
                    successTimeConsuming += System.currentTimeMillis() - startTime;
                } catch (CosXmlClientException | CosXmlServiceException e) {
                    Log.e(TAG, "testPutObject" + (isBig ? "Big" : "Small") + i + " Error: " + region + " " + bucket + " " + e.getMessage());
                    e.printStackTrace();
                }
            }
            try {
                DecimalFormat df = new DecimalFormat("#.##");
                Log.d(TAG, type + " testPutObject" + (isBig ? "Big" : "Small") + " End: \t " + region + "\t 平均耗时：" + successTimeConsuming / successCount + "\t 成功率：" + successCount + "/" + requestCount);
                NET_METRICS_ARR.add(new NetMetrics(
                        type,
                        "PutObject" + (isBig ? "Big" : "Small"),
                        region,
                        decimalFormat((double) successTimeConsuming / successCount),
                        decimalFormat((double) successCount / requestCount)
                ));
            } catch (Exception e) {
            }
        }
    }

    private void testGetObject(CosXmlSimpleService cosXmlService, boolean isBig, String type) {
        for (int r = 0; r < NET_BUCKET_REGION_ARR.length; r++) {
            String region = NET_BUCKET_REGION_ARR[r];
            String bucket = NET_BUCKET_ARR[r];

            if (type.startsWith("EO")) {
                // EO 目前只测试境外地域
                if (!("net-tokyo-1257101689".equals(bucket) || "net-singapore-1257101689".equals(bucket) || "net-siliconvall-1257101689".equals(bucket) ||
                        "net-seoul-1257101689".equals(bucket) || "net-saopaulo-1257101689".equals(bucket) || "net-jakarta-1257101689".equals(bucket) ||
                        "net-hongkong-1257101689".equals(bucket) || "net-frankfurt-1257101689".equals(bucket) || "net-bangkok-1257101689".equals(bucket) || "net-ashburn-1257101689".equals(bucket))) {
                    continue;
                }

                CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                        .setDebuggable(true)
                        .enableQuic("EO".equals(type))
                        .setHost(bucket.split("-")[1]+".jordanqin.online")
                        .addNoSignHeaders("Host")
                        .setRegion(region)
                        .builder();
                cosXmlService = ServiceFactory.INSTANCE.newMeService(cosXmlServiceConfig);
            }

            int successCount = 0;
            long successTimeConsuming = 0;
            for (int i = 0; i < requestCount; i++) {
                Log.d(TAG, "testGetObject" + (isBig ? "Big" : "Small") + i + " Start: " + region + " " + bucket);
                long startTime = System.currentTimeMillis();
                GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, isBig ? TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH : TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, TestUtils.localParentPath());
                getObjectRequest.setRegion(region);
                try {
                    cosXmlService.getObject(getObjectRequest);
                    successCount++;
                    successTimeConsuming += System.currentTimeMillis() - startTime;
                } catch (CosXmlClientException | CosXmlServiceException e) {
                    Log.e(TAG, "testGetObject" + (isBig ? "Big" : "Small") + i + " Error: " + region + " " + bucket + " " + e.getMessage());
                    e.printStackTrace();
                }
            }
            try {
                DecimalFormat df = new DecimalFormat("#.##");
                Log.d(TAG, type + " testGetObject" + (isBig ? "Big" : "Small") + " End: \t " + region + "\t 平均耗时：" + successTimeConsuming / successCount + "\t 成功率：" + successCount + "/" + requestCount);
                NET_METRICS_ARR.add(new NetMetrics(
                        type,
                        "GetObject" + (isBig ? "Big" : "Small"),
                        region,
                        decimalFormat((double) successTimeConsuming / successCount),
                        decimalFormat((double) successCount / requestCount)
                ));
            } catch (Exception e) {
            }
        }
    }

    private double decimalFormat(double num) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(num));
    }

    private static class NetMetrics {
        public String type;
        public String action;
        public String region;
        public double timeConsuming;
        public double successRate;

        public NetMetrics(String type, String action, String region, double timeConsuming, double successRate) {
            this.type = type;
            this.action = action;
            this.region = region;
            this.timeConsuming = timeConsuming;
            this.successRate = successRate;
        }
    }
}
