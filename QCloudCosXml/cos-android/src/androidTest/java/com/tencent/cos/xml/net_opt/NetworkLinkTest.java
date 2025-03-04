package com.tencent.cos.xml.net_opt;

import static com.tencent.cos.xml.core.TestConst.NET_BUCKET_REGION_ARR;
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
import com.tencent.qcloud.core.http.HttpTaskMetrics;

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
public class NetworkLinkTest {
    private static final String TAG = "NetworkLinkTest";
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
        File file = new File(TestUtils.localParentPath(), "MyExcelFile.xls");
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
//            ws.value(0, 5, "httpTookTime");
//            ws.value(0, 6, "dnsLookupTookTime");
//            ws.value(0, 7, "httpConnect");
//            ws.value(0, 8, "httpSecureConnect");
//            ws.value(0, 9, "httpReadHeader");
//            ws.value(0, 10, "httpReadBody");
//            ws.value(0, 11, "httpWriteHeader");
//            ws.value(0, 12, "httpWriteBody");

            for (int i = 0; i < NET_METRICS_ARR.size(); i++) {
                NetMetrics netMetrics = NET_METRICS_ARR.get(i);
                ws.value(i, 0, netMetrics.type);
                ws.value(i, 1, netMetrics.action);
                ws.value(i, 2, netMetrics.region);
                ws.value(i, 3, netMetrics.timeConsuming);
                ws.value(i, 4, netMetrics.successRate);
                ws.value(i, 5, netMetrics.httpTookTime);
                ws.value(i, 6, netMetrics.dnsLookupTookTime);
                ws.value(i, 7, netMetrics.httpConnect);
                ws.value(i, 8, netMetrics.httpSecureConnect);
                ws.value(i, 9, netMetrics.httpReadHeader);
                ws.value(i, 10, netMetrics.httpReadBody);
                ws.value(i, 11, netMetrics.httpWriteHeader);
                ws.value(i, 12, netMetrics.httpWriteBody);
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

        testPutObject(cosXmlService, true, "默认");
//        testPutObject(cosXmlService, false, "默认");
//        testGetObject(cosXmlService, true, "默认");
//        testGetObject(cosXmlService, false, "默认");
    }

    @Test
    public void testAccelerate() {
        CosXmlServiceConfig cosXmlServiceConfigAccelerate = new CosXmlServiceConfig.Builder()
                .setDebuggable(true)
                .setAccelerate(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();
        CosXmlSimpleService cosXmlServiceAccelerate = ServiceFactory.INSTANCE.newMeService(cosXmlServiceConfigAccelerate);

        testPutObject(cosXmlServiceAccelerate, true, "全球加速");
//        testPutObject(cosXmlServiceAccelerate, false, "全球加速");
//        testGetObject(cosXmlServiceAccelerate, true, "全球加速");
//        testGetObject(cosXmlServiceAccelerate, false, "全球加速");
    }

    @Test
    public void testEo() {
        testPutObject(null, true, "EO");
//        testPutObject(null, false, "EO");
//        testGetObject(null, true, "EO");
//        testGetObject(null, false, "EO");
    }

    @Test
    public void testEoAccelerate() {
        testPutObject(null, true, "EOAccelerate");
//        testPutObject(null, false, "EOAccelerate");
//        testGetObject(null, true, "EOAccelerate");
//        testGetObject(null, false, "EOAccelerate");
    }

    private void testPutObject(CosXmlSimpleService cosXmlService, boolean isBig, String type) {
        for (int r = 0; r < NET_BUCKET_REGION_ARR.length; r++) {
            String region = NET_BUCKET_REGION_ARR[r];
            String bucket = NET_BUCKET_ARR[r];

            if ("全球加速".equals(type) || "EOAccelerate".equals(type)) {
                // 下面两个地域不支持全球加速
                if ("net-jakarta-1257101689".equals(bucket) || "net-saopaulo-1257101689".equals(bucket)) {
                    continue;
                }
            }

            if (type.startsWith("EO")) {
                // EO 目前只测试境外地域
                if (!("net-tokyo-1257101689".equals(bucket) || "net-singapore-1257101689".equals(bucket) || "net-siliconvall-1257101689".equals(bucket) ||
                        "net-seoul-1257101689".equals(bucket) || "net-saopaulo-1257101689".equals(bucket) || "net-jakarta-1257101689".equals(bucket) ||
                        "net-hongkong-1257101689".equals(bucket) || "net-frankfurt-1257101689".equals(bucket) || "net-bangkok-1257101689".equals(bucket) || "net-ashburn-1257101689".equals(bucket))) {
                    continue;
                }

                String host;
                if("EOAccelerate".equals(type)){
                    host = "accelerate." + bucket.split("-")[1]+".jordanqin.online";
                } else {
                    host = bucket.split("-")[1]+".jordanqin.online";
                }

                CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                        .setDebuggable(true)
                        .setHost(host)
                        .addNoSignHeaders("Host")
                        .setRegion(region)
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
            double http_took_time = 0;
            double dnsLookupTookTime = 0;
            double http_connect = 0;
            double http_secure_connect = 0;
            double http_read_header = 0;
            double http_read_body = 0;
            double http_write_header = 0;
            double http_write_body = 0;
            for (int i = 0; i < requestCount; i++) {
                Log.d(TAG, "testPutObject" + (isBig ? "Big" : "Small") + i + " Start: " + region + " " + bucket);
                long startTime = System.currentTimeMillis();
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, isBig ? TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH : TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, localPath);
                putObjectRequest.setRegion(region);
                try {
                    cosXmlService.putObject(putObjectRequest);
                    successCount++;
                    successTimeConsuming += System.currentTimeMillis() - startTime;

                    HttpTaskMetrics httpTaskMetrics = putObjectRequest.getMetrics();
                    http_took_time += httpTaskMetrics.httpTaskFullTime() * 1000;
                    dnsLookupTookTime += httpTaskMetrics.dnsLookupTookTime() * 1000;
                    http_connect += httpTaskMetrics.connectTookTime() * 1000;
                    http_secure_connect += httpTaskMetrics.secureConnectTookTime() * 1000;
                    http_read_header += httpTaskMetrics.readResponseHeaderTookTime() * 1000;
                    http_read_body += httpTaskMetrics.readResponseBodyTookTime() * 1000;
                    http_write_header += httpTaskMetrics.writeRequestHeaderTookTime() * 1000;
                    http_write_body += httpTaskMetrics.writeRequestBodyTookTime() * 1000;
                } catch (CosXmlClientException | CosXmlServiceException e) {
                    Log.e(TAG, "testPutObject" + (isBig ? "Big" : "Small") + i + " Error: " + region + " " + bucket + " " + e.getMessage());
                    e.printStackTrace();
                }
            }
            try {
                DecimalFormat df = new DecimalFormat("#.##");
                Log.d(TAG, type + " testPutObject" + (isBig ? "Big" : "Small") + " End: \t " + region + "\t 平均耗时：" + successTimeConsuming / successCount + "\t 成功率：" + successCount + "/" + requestCount);
                Log.d(TAG, type + " testPutObject" + (isBig ? "Big" : "Small") + " End: \t " + region + "\t http平均耗时：" + df.format(http_took_time / successCount) + "\t dns平均耗时：" + df.format(dnsLookupTookTime / successCount) + "\t 连接平均耗时：" + df.format(http_connect / successCount) + "\t 安全连接平均耗时：" + df.format(http_secure_connect / successCount) +
                        "\t 读header平均耗时：" + df.format(http_read_header / successCount) + "\t 读body平均耗时：" + df.format(http_read_body / successCount) + "\t 写header平均耗时：" + df.format(http_write_header / successCount) + "\t 写body平均耗时：" + df.format(http_write_body / successCount));
                NET_METRICS_ARR.add(new NetMetrics(
                        type,
                        "PutObject" + (isBig ? "Big" : "Small"),
                        region,
                        decimalFormat((double) successTimeConsuming / successCount),
                        decimalFormat((double) successCount / requestCount),
                        decimalFormat(http_took_time / successCount),
                        decimalFormat(dnsLookupTookTime / successCount),
                        decimalFormat(http_connect / successCount),
                        decimalFormat(http_secure_connect / successCount),
                        decimalFormat(http_read_header / successCount),
                        decimalFormat(http_read_body / successCount),
                        decimalFormat(http_write_header / successCount),
                        decimalFormat(http_write_body / successCount)
                ));
            } catch (Exception e) {
            }
        }
    }

    private void testGetObject(CosXmlSimpleService cosXmlService, boolean isBig, String type) {
        for (int r = 0; r < NET_BUCKET_REGION_ARR.length; r++) {
            String region = NET_BUCKET_REGION_ARR[r];
            String bucket = NET_BUCKET_ARR[r];

            if ("全球加速".equals(type)) {
                // 下面两个地域不支持全球加速
                if ("net-jakarta-1257101689".equals(bucket) || "net-saopaulo-1257101689".equals(bucket)) {
                    continue;
                }
            }

            if (type.startsWith("EO")) {
                // EO 目前只测试境外地域
                if (!("net-tokyo-1257101689".equals(bucket) || "net-singapore-1257101689".equals(bucket) || "net-siliconvall-1257101689".equals(bucket) ||
                        "net-seoul-1257101689".equals(bucket) || "net-saopaulo-1257101689".equals(bucket) || "net-jakarta-1257101689".equals(bucket) ||
                        "net-hongkong-1257101689".equals(bucket) || "net-frankfurt-1257101689".equals(bucket) || "net-bangkok-1257101689".equals(bucket) || "net-ashburn-1257101689".equals(bucket))) {
                    continue;
                }

                String host;
                if("EOAccelerate".equals(type)){
                    host = "accelerate." + bucket.split("-")[1]+".jordanqin.online";
                } else {
                    host = bucket.split("-")[1]+".jordanqin.online";
                }

                CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                        .setDebuggable(true)
                        .setHost(host)
                        .addNoSignHeaders("Host")
                        .setRegion(region)
                        .builder();
                cosXmlService = ServiceFactory.INSTANCE.newMeService(cosXmlServiceConfig);
            }

            int successCount = 0;
            long successTimeConsuming = 0;
            double http_took_time = 0;
            double dnsLookupTookTime = 0;
            double http_connect = 0;
            double http_secure_connect = 0;
            double http_read_header = 0;
            double http_read_body = 0;
            double http_write_header = 0;
            double http_write_body = 0;
            for (int i = 0; i < requestCount; i++) {
                Log.d(TAG, "testGetObject" + (isBig ? "Big" : "Small") + i + " Start: " + region + " " + bucket);
                long startTime = System.currentTimeMillis();
                GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, isBig ? TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH : TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, TestUtils.localParentPath());
                getObjectRequest.setRegion(region);
                try {
                    cosXmlService.getObject(getObjectRequest);
                    successCount++;
                    successTimeConsuming += System.currentTimeMillis() - startTime;

                    HttpTaskMetrics httpTaskMetrics = getObjectRequest.getMetrics();
                    http_took_time += httpTaskMetrics.httpTaskFullTime() * 1000;
                    dnsLookupTookTime += httpTaskMetrics.dnsLookupTookTime() * 1000;
                    http_connect += httpTaskMetrics.connectTookTime() * 1000;
                    http_secure_connect += httpTaskMetrics.secureConnectTookTime() * 1000;
                    http_read_header += httpTaskMetrics.readResponseHeaderTookTime() * 1000;
                    http_read_body += httpTaskMetrics.readResponseBodyTookTime() * 1000;
                    http_write_header += httpTaskMetrics.writeRequestHeaderTookTime() * 1000;
                    http_write_body += httpTaskMetrics.writeRequestBodyTookTime() * 1000;
                } catch (CosXmlClientException | CosXmlServiceException e) {
                    Log.e(TAG, "testGetObject" + (isBig ? "Big" : "Small") + i + " Error: " + region + " " + bucket + " " + e.getMessage());
                    e.printStackTrace();
                }
            }
            try {
                DecimalFormat df = new DecimalFormat("#.##");
                Log.d(TAG, type + " testGetObject" + (isBig ? "Big" : "Small") + " End: \t " + region + "\t 平均耗时：" + successTimeConsuming / successCount + "\t 成功率：" + successCount + "/" + requestCount);
                Log.d(TAG, type + " testGetObject" + (isBig ? "Big" : "Small") + " End: \t " + region + "\t http平均耗时：" + df.format(http_took_time / successCount) + "\t dns平均耗时：" + df.format(dnsLookupTookTime / successCount) + "\t 连接平均耗时：" + df.format(http_connect / successCount) + "\t 安全连接平均耗时：" + df.format(http_secure_connect / successCount) +
                        "\t 读header平均耗时：" + df.format(http_read_header / successCount) + "\t 读body平均耗时：" + df.format(http_read_body / successCount) + "\t 写header平均耗时：" + df.format(http_write_header / successCount) + "\t 写body平均耗时：" + df.format(http_write_body / successCount));
                NET_METRICS_ARR.add(new NetMetrics(
                        type,
                        "GetObject" + (isBig ? "Big" : "Small"),
                        region,
                        decimalFormat((double) successTimeConsuming / successCount),
                        decimalFormat((double) successCount / requestCount),
                        decimalFormat(http_took_time / successCount),
                        decimalFormat(dnsLookupTookTime / successCount),
                        decimalFormat(http_connect / successCount),
                        decimalFormat(http_secure_connect / successCount),
                        decimalFormat(http_read_header / successCount),
                        decimalFormat(http_read_body / successCount),
                        decimalFormat(http_write_header / successCount),
                        decimalFormat(http_write_body / successCount)
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
        public double httpTookTime;
        public double dnsLookupTookTime;
        public double httpConnect;
        public double httpSecureConnect;
        public double httpReadHeader;
        public double httpReadBody;
        public double httpWriteHeader;
        public double httpWriteBody;

        public NetMetrics(String type, String action, String region, double timeConsuming, double successRate, double httpTookTime, double dnsLookupTookTime, double httpConnect, double httpSecureConnect, double httpReadHeader, double httpReadBody, double httpWriteHeader, double httpWriteBody) {
            this.type = type;
            this.action = action;
            this.region = region;
            this.timeConsuming = timeConsuming;
            this.successRate = successRate;
            this.httpTookTime = httpTookTime;
            this.dnsLookupTookTime = dnsLookupTookTime;
            this.httpConnect = httpConnect;
            this.httpSecureConnect = httpSecureConnect;
            this.httpReadHeader = httpReadHeader;
            this.httpReadBody = httpReadBody;
            this.httpWriteHeader = httpWriteHeader;
            this.httpWriteBody = httpWriteBody;
        }
    }
}
