package com.tencent.cos.xml.ssl;

import static com.tencent.cos.xml.core.TestUtils.getContext;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.test.R;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.logger.COSLogger;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.util.ContextHolder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * SSL 自签名证书和双向认证测试
 * 
 * 测试场景说明:
 * - 场景1 (端口8443): 自签名证书 - 单向SSL，客户端需信任CA证书
 * - 场景2 (端口8444): 强制双向认证(mTLS)，客户端必须提供证书
 * 
 * 证书密码统一为: 123456
 * 
 * 注意: 运行测试前需要先部署SSL测试服务器:
 * 1. cd ssl-test-server
 * 2. bash scripts/generate-certs.sh
 * 3. bash scripts/deploy-nginx.sh
 * 
 * Created by jordanqin on 2026/1/28 16:20.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class SslTest {
    private static final String TAG = "SslTest";

    // SSL测试服务器地址（需替换为实际的电脑IP）
    // 模拟器使用: 10.0.2.2 (指向主机localhost)
    // 真机使用: 电脑实际IP地址
    private static final String SSL_TEST_HOST = "10.0.2.2";

    // 测试端口
    private static final int PORT_SELF_SIGNED = 8443;       // 自签名证书（单向SSL）
    private static final int PORT_REQUIRED_MTLS = 8444;     // 强制双向认证

    // 证书密码
    private static final String CERT_PASSWORD = "123456";

    // 测试文件路径
    private static final String SMALL_OBJECT_PATH = "ssl_test/small_object";
    private static final String BIG_OBJECT_PATH = "ssl_test/big_object";

    // 文件大小
    private static final long SMALL_FILE_SIZE = 1024 * 1024;        // 1MB
    private static final long BIG_FILE_SIZE = 10 * 1024 * 1024;     // 10MB

    @Before
    public void setUp() {
        ContextHolder.setContext(TestUtils.getContext());
        COSLogger.enableLogcat(true);
    }

    // ==================== 场景1: 自签名证书测试 ====================

    /**
     * 测试自签名证书 - 小文件上传
     */
    @Test
    public void testSelfSignedSmallUpload() throws IOException {
        QCloudLogger.i(TAG, "===== 测试自签名证书 - 小文件上传 =====");
        TransferManager transferManager = newSelfSignedTransferManager();
        String localPath = TestUtils.localPath("ssl_small_upload_" + System.currentTimeMillis());
        TestUtils.createFile(localPath, SMALL_FILE_SIZE);

        COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                SMALL_OBJECT_PATH,
                localPath,
                null
        );

        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i(TAG, "自签名证书小文件上传成功");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                QCloudLogger.e(TAG, "自签名证书小文件上传失败: " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
        TestUtils.removeLocalFile(localPath);
    }

    /**
     * 测试自签名证书 - 大文件上传（分片上传）
     */
    @Test
    public void testSelfSignedBigUpload() throws IOException {
        QCloudLogger.i(TAG, "===== 测试自签名证书 - 大文件上传 =====");
        TransferManager transferManager = newSelfSignedTransferManager();
        String localPath = TestUtils.localPath("ssl_big_upload_" + System.currentTimeMillis());
        TestUtils.createFile(localPath, BIG_FILE_SIZE);

        COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                BIG_OBJECT_PATH,
                localPath,
                null
        );

        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i(TAG, "自签名证书大文件上传成功");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                QCloudLogger.e(TAG, "自签名证书大文件上传失败: " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
        TestUtils.removeLocalFile(localPath);
    }

    /**
     * 测试自签名证书 - 小文件下载
     */
    @Test
    public void testSelfSignedSmallDownload() {
        QCloudLogger.i(TAG, "===== 测试自签名证书 - 小文件下载 =====");
        TransferManager transferManager = newSelfSignedTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath()
        );

        COSXMLDownloadTask downloadTask = transferManager.download(getContext(), getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i(TAG, "自签名证书小文件下载成功");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                QCloudLogger.e(TAG, "自签名证书小文件下载失败: " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    /**
     * 测试自签名证书 - 大文件下载
     */
    @Test
    public void testSelfSignedBigDownload() {
        QCloudLogger.i(TAG, "===== 测试自签名证书 - 大文件下载 =====");
        TransferManager transferManager = newSelfSignedTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localParentPath()
        );

        COSXMLDownloadTask downloadTask = transferManager.download(getContext(), getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i(TAG, "自签名证书大文件下载成功");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                QCloudLogger.e(TAG, "自签名证书大文件下载失败: " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    // ==================== 场景2: 双向认证测试(mTLS) ====================

    /**
     * 测试双向认证 - 小文件上传
     */
    @Test
    public void testMtlsSmallUpload() throws IOException {
        QCloudLogger.i(TAG, "===== 测试双向认证 - 小文件上传 =====");
        TransferManager transferManager = newMtlsTransferManager();
        String localPath = TestUtils.localPath("ssl_mtls_small_upload_" + System.currentTimeMillis());
        TestUtils.createFile(localPath, SMALL_FILE_SIZE);

        COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                SMALL_OBJECT_PATH,
                localPath,
                null
        );

        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i(TAG, "双向认证小文件上传成功");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                QCloudLogger.e(TAG, "双向认证小文件上传失败: " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
        TestUtils.removeLocalFile(localPath);
    }

    /**
     * 测试双向认证 - 大文件上传（分片上传）
     */
    @Test
    public void testMtlsBigUpload() throws IOException {
        QCloudLogger.i(TAG, "===== 测试双向认证 - 大文件上传 =====");
        TransferManager transferManager = newMtlsTransferManager();
        String localPath = TestUtils.localPath("ssl_mtls_big_upload_" + System.currentTimeMillis());
        TestUtils.createFile(localPath, BIG_FILE_SIZE);

        COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                BIG_OBJECT_PATH,
                localPath,
                null
        );

        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i(TAG, "双向认证大文件上传成功");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                QCloudLogger.e(TAG, "双向认证大文件上传失败: " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
        TestUtils.removeLocalFile(localPath);
    }

    /**
     * 测试双向认证 - 小文件下载
     */
    @Test
    public void testMtlsSmallDownload() {
        QCloudLogger.i(TAG, "===== 测试双向认证 - 小文件下载 =====");
        TransferManager transferManager = newMtlsTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath()
        );

        COSXMLDownloadTask downloadTask = transferManager.download(getContext(), getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i(TAG, "双向认证小文件下载成功");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                QCloudLogger.e(TAG, "双向认证小文件下载失败: " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    /**
     * 测试双向认证 - 大文件下载
     */
    @Test
    public void testMtlsBigDownload() {
        QCloudLogger.i(TAG, "===== 测试双向认证 - 大文件下载 =====");
        TransferManager transferManager = newMtlsTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localParentPath()
        );

        COSXMLDownloadTask downloadTask = transferManager.download(getContext(), getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i(TAG, "双向认证大文件下载成功");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                QCloudLogger.e(TAG, "双向认证大文件下载失败: " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    // ==================== 错误场景测试 ====================

    /**
     * 测试双向认证 - 不提供客户端证书（应该失败）
     */
    @Test
    public void testMtlsWithoutClientCert() throws IOException {
        QCloudLogger.i(TAG, "===== 测试双向认证 - 不提供客户端证书（预期失败） =====");
        
        // 只配置服务端证书信任，不配置客户端证书
        TransferManager transferManager = newSelfSignedTransferManager(PORT_REQUIRED_MTLS);
        String localPath = TestUtils.localPath("ssl_mtls_no_client_cert_" + System.currentTimeMillis());
        TestUtils.createFile(localPath, SMALL_FILE_SIZE);

        COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                SMALL_OBJECT_PATH,
                localPath,
                null
        );

        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.e(TAG, "双向认证-无客户端证书: 不应该成功");
                testLocker.release();
                Assert.fail("双向认证场景下，不提供客户端证书应该失败");
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                QCloudLogger.i(TAG, "双向认证-无客户端证书: 预期失败 - " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
                Assert.assertTrue("不提供客户端证书时应该返回SSL错误", true);
            }
        });

        testLocker.lock();
        TestUtils.removeLocalFile(localPath);
    }

    /**
     * 测试自签名证书 - 不信任服务端证书（应该失败）
     */
    @Test
    public void testSelfSignedWithoutTrustCa() throws IOException {
        QCloudLogger.i(TAG, "===== 测试自签名证书 - 不信任CA（预期失败） =====");
        
        // 不配置CA信任，直接连接自签名服务
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setPort(PORT_SELF_SIGNED)
                .setHost(SSL_TEST_HOST)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                // nginx转发时host为cos真正的host，因此这里需要添加Host头
                .addHeader("Host", "mobile-ut-1253960454.cos.ap-guangzhou.myqcloud.com")
                // 不设置自定义SSLContext，使用默认的证书验证
                .builder();

        CosXmlSimpleService cosXmlService = new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY, 60000));

        TransferConfig transferConfig = new TransferConfig.Builder().build();
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);

        String localPath = TestUtils.localPath("ssl_no_trust_ca_" + System.currentTimeMillis());
        TestUtils.createFile(localPath, SMALL_FILE_SIZE);

        COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                SMALL_OBJECT_PATH,
                localPath,
                null
        );

        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.e(TAG, "不信任CA: 不应该成功");
                testLocker.release();
                Assert.fail("不信任CA证书时应该失败");
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                QCloudLogger.i(TAG, "不信任CA: 预期失败 - " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
                Assert.assertTrue("不信任CA时应该返回SSL证书验证错误", true);
            }
        });

        testLocker.lock();
        TestUtils.removeLocalFile(localPath);
    }

    // ==================== 辅助方法 ====================

    /**
     * 创建自签名证书场景的 TransferManager
     */
    private TransferManager newSelfSignedTransferManager() {
        return newSelfSignedTransferManager(PORT_SELF_SIGNED);
    }

    /**
     * 创建自签名证书场景的 TransferManager（指定端口）
     */
    private TransferManager newSelfSignedTransferManager(int port) {
        CosXmlSimpleService cosXmlService = newSelfSignedService(port);
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .build();
        return new TransferManager(cosXmlService, transferConfig);
    }

    /**
     * 创建双向认证场景的 TransferManager
     */
    private TransferManager newMtlsTransferManager() {
        CosXmlSimpleService cosXmlService = newMtlsService();
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .build();
        return new TransferManager(cosXmlService, transferConfig);
    }

    /**
     * 创建自签名证书场景的 CosXmlSimpleService
     * 只需要信任服务端的CA证书
     */
    private CosXmlSimpleService newSelfSignedService(int port) {
        try {
            // 加载CA证书（BKS格式）用于信任自签名服务端证书
            InputStream caInputStream = getContext().getResources().openRawResource(R.raw.ca_trust);
            KeyStore caKeyStore = KeyStore.getInstance("BKS");
            caKeyStore.load(caInputStream, CERT_PASSWORD.toCharArray());
            caInputStream.close();

            // 创建TrustManager
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(caKeyStore);

            // 创建SSLContext（只配置TrustManager，不配置KeyManager）
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

            X509TrustManager trustManager = (X509TrustManager) tmf.getTrustManagers()[0];

            CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                    .isHttps(true)
                    .setDebuggable(true)
                    .setConnectionTimeout(30000)
                    .setSocketTimeout(60000)
                    .setPort(port)
                    .setHost(SSL_TEST_HOST)
                    .setRegion(TestConst.PERSIST_BUCKET_REGION)
                    .setCustomSSLContext(sslContext, trustManager)
                    // nginx转发时host为cos真正的host，因此这里需要添加Host头
                    .addHeader("Host", "mobile-ut-1253960454.cos.ap-guangzhou.myqcloud.com")
                    .builder();

            return new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                    new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY, 60000));

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | 
                 IOException | KeyManagementException e) {
            QCloudLogger.e(TAG, "创建自签名证书Service失败: " + e.getMessage());
            throw new RuntimeException("创建自签名证书Service失败", e);
        }
    }

    /**
     * 创建双向认证场景的 CosXmlSimpleService
     * 需要同时配置：
     * 1. 信任服务端的CA证书（TrustManager）
     * 2. 客户端证书（KeyManager）
     */
    private CosXmlSimpleService newMtlsService() {
        try {
            // 1. 加载CA证书（BKS格式）用于信任自签名服务端证书
            InputStream caInputStream = getContext().getResources().openRawResource(R.raw.ca_trust);
            KeyStore caKeyStore = KeyStore.getInstance("BKS");
            caKeyStore.load(caInputStream, CERT_PASSWORD.toCharArray());
            caInputStream.close();

            // 创建TrustManager
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(caKeyStore);

            // 2. 加载客户端证书（BKS格式）用于双向认证
            InputStream clientInputStream = getContext().getResources().openRawResource(R.raw.ssl_client);
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(clientInputStream, CERT_PASSWORD.toCharArray());
            clientInputStream.close();

            // 创建KeyManager
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientKeyStore, CERT_PASSWORD.toCharArray());

            // 3. 创建SSLContext（同时配置TrustManager和KeyManager）
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

            X509TrustManager trustManager = (X509TrustManager) tmf.getTrustManagers()[0];

            CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                    .isHttps(true)
                    .setDebuggable(true)
                    .setConnectionTimeout(30000)
                    .setSocketTimeout(60000)
                    .setPort(PORT_REQUIRED_MTLS)
                    .setHost(SSL_TEST_HOST)
                    .setRegion(TestConst.PERSIST_BUCKET_REGION)
                    .setCustomSSLContext(sslContext, trustManager)
                    // nginx转发时host为cos真正的host，因此这里需要添加Host头
                    .addHeader("Host", "mobile-ut-1253960454.cos.ap-guangzhou.myqcloud.com")
                    .builder();

            return new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                    new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY, 60000));

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | 
                 IOException | KeyManagementException | UnrecoverableKeyException e) {
            QCloudLogger.e(TAG, "创建双向认证Service失败: " + e.getMessage());
            throw new RuntimeException("创建双向认证Service失败", e);
        }
    }
}
