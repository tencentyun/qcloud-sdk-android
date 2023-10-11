package com.tencent.cos.xml.core;

import static com.tencent.cos.xml.core.TestUtils.getContext;
import static com.tencent.qcloud.core.http.HttpConstants.Header.AUTHORIZATION;

import android.util.Log;

import com.tencent.cos.xml.BuildConfig;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.crypto.KMSEncryptionMaterialsProvider;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferService;
import com.tencent.msdk.dns.DnsConfig;
import com.tencent.msdk.dns.MSDKDnsResolver;
import com.tencent.qcloud.core.auth.AuthConstants;
import com.tencent.qcloud.core.auth.BasicQCloudCredentials;
import com.tencent.qcloud.core.auth.QCloudCredentials;
import com.tencent.qcloud.core.auth.QCloudSelfSigner;
import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.auth.Utils;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpConfiguration;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.QCloudHttpRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class ServiceFactory {

    public static ServiceFactory INSTANCE = new ServiceFactory();

    public CosXmlSimpleService newDefaultService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setConnectionTimeout(4000)
                .setSocketTimeout(4000)
                .setTransferThreadControl(true)
                .setUploadMaxThreadCount(10)
                .setDownloadMaxThreadCount(6)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return newService(cosXmlServiceConfig);
    }

    public CosXmlSimpleService newTencentcosService() {
        CosXmlSimpleService simpleService = newDefaultService();
        simpleService.setDomain(String.format("%s.cos.%s.tencentcos.cn", TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION));
        return simpleService;
    }

    public CosXmlSimpleService newDefaultServiceBySessionCredentials() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return newServiceBySessionCredentials(cosXmlServiceConfig);
    }

    public CosXmlSimpleService newDnsService() {
        tencentHttpDnsInit();

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();
        CosXmlSimpleService simpleService = newService(cosXmlServiceConfig);
        String host = cosXmlServiceConfig.getDefaultRequestHost(TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET);
        String myqcloudIpsString = MSDKDnsResolver.getInstance().getAddrByName(host);
        try {
            String[] ips = myqcloudIpsString.split(";");
            simpleService.addCustomerDNS(host, ips);
        } catch (CosXmlClientException ignored) {
        }
        simpleService.addCustomerDNSFetch(hostname -> {
            String ips = MSDKDnsResolver.getInstance().getAddrByName(hostname);
            String[] ipArr = ips.split(";");
            if (0 == ipArr.length) {
                return Collections.emptyList();
            }
            List<InetAddress> inetAddressList = new ArrayList<>(ipArr.length);
            for (String ip : ipArr) {
                if ("0".equals(ip)) {
                    continue;
                }
                try {
                    InetAddress inetAddress = InetAddress.getByName(ip);
                    inetAddressList.add(inetAddress);
                } catch (UnknownHostException ignored) {
                }
            }
            return inetAddressList;
        });
        return simpleService;
    }

    public CosXmlSimpleService newQuicService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .enableQuic(true)
                .setPort(443)
                .setRegion(TestConst.QUIC_BUCKET_REGION)
                .builder();

        CosXmlSimpleService cosXmlSimpleService = newService(cosXmlServiceConfig);
        try {
            cosXmlSimpleService.addCustomerDNS("{bucket}.cos.{region}.myqcloud.com"
                    .replace("{bucket}", "mobile-ut-1253960454")
                    .replace("{region}", TestConst.QUIC_BUCKET_REGION),
                    new String[]{TestConst.QUIC_TEST_IP});
        } catch (CosXmlClientException clientException) {
            clientException.printStackTrace();
        }
        return cosXmlSimpleService;
    }


    public CosXmlSimpleService newSelfService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setConnectionTimeout(4000)
                .setSocketTimeout(4000)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                new QCloudSelfSigner() {

                    /**
                     * 对请求进行签名
                     *
                     * @param request 需要签名的请求
                     * @throws QCloudClientException 客户端异常
                     */
                    @Override
                    public void sign(QCloudHttpRequest request) throws QCloudClientException {

                        // 1. 把 request 的请求参数传给服务端计算签名
                        String auth = "get auth from server";

                        // 2. 给请求添加签名
                        request.addHeader(HttpConstants.Header.AUTHORIZATION, auth);
                    }
                });
    }

    public CosXmlSimpleService newSignInUrlService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .setDebuggable(true)
                .setSignInUrl(true)
                .builder();

        return newService(cosXmlServiceConfig);
    }

    public CosXmlSimpleService newSignerService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setConnectionTimeout(4000)
                .setSocketTimeout(4000)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return newSignerService(cosXmlServiceConfig);
    }

    public CosXmlSimpleService newCDNService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .setDebuggable(true)
                .setHostFormat("${bucket}.file.myqcloud.com")
                .builder();

        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig);
    }

    public TransferManager newDefaultTransferManager() {

        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .setVerifyCRC64(true)
                .setSliceSizeForCopy(5242880)
                .setDividsionForCopy(5242880)
                .build();
        Log.d(TestConst.UT_TAG, String.valueOf(transferConfig.getDivisionForCopy()));
        return new TransferManager(newDefaultService(), transferConfig);
    }

    public TransferManager newSlice369TransferManager() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload((1024 * 1024)+369)
                .setVerifyCRC64(true)
                .setSliceSizeForCopy(5242880)
                .setDividsionForCopy(5242880)
                .build();
        Log.d(TestConst.UT_TAG, String.valueOf(transferConfig.getDivisionForCopy()));
        return new TransferManager(newDefaultService(), transferConfig);
    }

    public TransferManager newTencentcosTransferManager() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .setVerifyCRC64(true)
                .setSliceSizeForCopy(5242880)
                .setDividsionForCopy(5242880)
                .build();
        Log.d(TestConst.UT_TAG, String.valueOf(transferConfig.getDivisionForCopy()));
        return new TransferManager(newTencentcosService(), transferConfig);
    }


    public TransferManager newDefaultTransferManagerBySessionCredentials() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .build();
        return new TransferManager(newDefaultServiceBySessionCredentials(), transferConfig);
    }

    public TransferManager newAnonymousTransferManager() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .build();
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();
        CosXmlSimpleService cosXmlService = new CosXmlSimpleService(getContext(), cosXmlServiceConfig);
        return new TransferManager(cosXmlService, transferConfig);
    }

    public TransferManager newDnsTransferManager() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .setVerifyCRC64(true)
                .setSliceSizeForCopy(5242880)
                .setDividsionForCopy(5242880)
                .build();
        return new TransferManager(newDnsService(), transferConfig);
    }

    public TransferService newDefaultTransferService() {

        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .build();
        return new TransferService(newDefaultService(), transferConfig);
    }

    public TransferService newBigSliceSizeTransferService() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(2 * 1024 * 1024)
                .build();
        return new TransferService(newDefaultService(), transferConfig);
    }

    public TransferService newSlice369TransferService() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload((1024 * 1024)+369)
                .build();
        return new TransferService(newDefaultService(), transferConfig);
    }

    public TransferService newBigSliceSize369TransferService() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(4 * 1024 * 1024)
                .setSliceSizeForUpload((2 * 1024 * 1024)+369)
                .build();
        return new TransferService(newDefaultService(), transferConfig);
    }

    public TransferService newAnonymousTransferService() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .build();
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();
        CosXmlSimpleService cosXmlService = new CosXmlSimpleService(getContext(), cosXmlServiceConfig);
        return new TransferService(cosXmlService, transferConfig);
    }

    public TransferManager newSingerTransferManager() {

        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferManager(newSignerService(), transferConfig);
    }

    public TransferService newQuicTransferService() {

        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferService(newQuicService(), transferConfig);
    }

    public TransferService newCesTransferService() {
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferService(newDefaultService(), transferConfig,
                new KMSEncryptionMaterialsProvider("kms-8xy4m0eb"));
    }

    public TransferManager newForceSimpleUploadTransferManager() {

        TransferConfig transferConfig = new TransferConfig.Builder()
                .setForceSimpleUpload(true)
                .build();
        return new TransferManager(newDefaultService(), transferConfig);
    }

    public TransferManager newQuicTransferManager() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion(TestConst.QUIC_BUCKET_REGION)
                .setDebuggable(true)
                .enableQuic(true)
                .builder();

        TransferConfig transferConfig = new TransferConfig.Builder().build();
        CosXmlSimpleService cosXmlService = newService(cosXmlServiceConfig);
//        String host = TestConst.QUIC_BUCKET + ".cos." + TestConst.QUIC_BUCKET_REGION + ".myqcloud.com";
//        try {
//            cosXmlService.addCustomerDNS(host, new String[] {TestConst.QUIC_TEST_IP});
//        } catch (CosXmlClientException e) {
//            e.printStackTrace();
//        }
        return new TransferManager(cosXmlService, transferConfig);
    }

    public TransferManager newAccelerateTransferManager() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setAccelerate(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return new TransferManager(newService(cosXmlServiceConfig), new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .build());
    }

    public TransferManager newCdnTransferManager() {
        return new TransferManager(newCDNService(), new TransferConfig.Builder().build());
    }

    public TransferManager newBigSliceSizeTransferManager() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(2 * 1024 * 1024)
                .setVerifyCRC64(true)
                .setSliceSizeForCopy(5242880)
                .setDividsionForCopy(5242880)
                .build();
        Log.d(TestConst.UT_TAG, String.valueOf(transferConfig.getDivisionForCopy()));
        return new TransferManager(newDefaultService(), transferConfig);
    }

    public TransferManager newBigSliceSize369TransferManager() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(4 * 1024 * 1024)
                .setSliceSizeForUpload((2 * 1024 * 1024)+369)
                .setVerifyCRC64(true)
                .setSliceSizeForCopy(5242880)
                .setDividsionForCopy(5242880)
                .build();
        Log.d(TestConst.UT_TAG, String.valueOf(transferConfig.getDivisionForCopy()));
        return new TransferManager(newDefaultService(), transferConfig);
    }


    private CosXmlSimpleService newServiceBySessionCredentials(CosXmlServiceConfig cosXmlServiceConfig) {
//        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
//                new MySessionCredentialProvider());

        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,60000) );
    }

    private CosXmlSimpleService newService(CosXmlServiceConfig cosXmlServiceConfig) {
        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,60000) );

    }

    /** -------------------- 自签名开始 -------------------**/
    private CosXmlSimpleService newSignerService(CosXmlServiceConfig cosXmlServiceConfig) {
        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig, new QCloudSigner() {
            @Override
            public void sign(QCloudHttpRequest request, QCloudCredentials credentials) throws QCloudClientException {
                BasicQCloudCredentials basicQCloudCredentials = getShortTimeCredential();

                StringBuilder authorization = new StringBuilder();
                String keyTime = request.getKeyTime();
                if (keyTime == null) {
                    keyTime = basicQCloudCredentials.getKeyTime();
                }
//                    COSXmlSignSourceProvider sourceProvider = (COSXmlSignSourceProvider) qCloudHttpRequest.getSignProvider();
                MyCOSXmlSignSourceProvider sourceProvider = new MyCOSXmlSignSourceProvider();
                sourceProvider.setSignTime(keyTime);
                String signature = signature(sourceProvider.source(request), basicQCloudCredentials.getSignKey());

                authorization.append(AuthConstants.Q_SIGN_ALGORITHM).append("=").append(AuthConstants.SHA1).append("&")
                        .append(AuthConstants.Q_AK).append("=")
                        .append(basicQCloudCredentials.getSecretId()).append("&")
                        .append(AuthConstants.Q_SIGN_TIME).append("=")
                        .append(keyTime).append("&")
                        .append(AuthConstants.Q_KEY_TIME).append("=")
                        .append(basicQCloudCredentials.getKeyTime()).append("&")
                        .append(AuthConstants.Q_HEADER_LIST).append("=")
                        .append(sourceProvider.getRealHeaderList().toLowerCase(Locale.ROOT)).append("&")
                        .append(AuthConstants.Q_URL_PARAM_LIST).append("=")
                        .append(sourceProvider.getRealParameterList().toLowerCase(Locale.ROOT)).append("&")
                        .append(AuthConstants.Q_SIGNATURE).append("=").append(signature);
                String auth = authorization.toString();

                request.removeHeader(AUTHORIZATION);
                request.addHeader(AUTHORIZATION, auth);
            }
        });
    }

    private static String signature(String source, String signKey) {
        byte[] sha1Bytes = Utils.hmacSha1(source, signKey);
        String signature = "";
        if (sha1Bytes != null) {
            signature = new String(Utils.encodeHex(sha1Bytes));
        }
        return signature;
    }

    private BasicQCloudCredentials getShortTimeCredential(){
        // 使用本地永久秘钥计算得到临时秘钥
        long current = HttpConfiguration.getDeviceTimeWithOffset();
        long expired = current + 600;
        String keyTime = current + ";" + expired;
        String signKey = secretKey2SignKey(TestConst.SECRET_KEY, keyTime);
        return new BasicQCloudCredentials(TestConst.SECRET_ID, TestConst.SECRET_KEY, signKey, keyTime);
    }

    private String secretKey2SignKey(String secretKey, String keyTime) {
        byte[] hmacSha1 = (Utils.hmacSha1(keyTime, secretKey));
        if (hmacSha1 != null) {
            return new String(Utils.encodeHex(hmacSha1)); // 用secretKey来加密keyTime
        }

        return null;
    }
    /** -------------------- 自签名结束 -------------------**/

    private void tencentHttpDnsInit() {
        DnsConfig dnsConfigBuilder = new DnsConfig.Builder()
                //（必填）dns 解析 id，即授权 id，腾讯云官网（https://console.cloud.tencent.com/httpdns）申请获得，用于域名解析鉴权
                .dnsId(BuildConfig.UT_DNS_ID)
                //（必填）dns 解析 key，即授权 id 对应的 key（加密密钥），在申请 SDK 后的邮箱里，腾讯云官网（https://console.cloud.tencent.com/httpdns）申请获得，用于域名解析鉴权
                .dnsKey(BuildConfig.UT_DNS_KEY)
                //（必填）Channel为desHttp()或aesHttp()时使用 119.29.29.98（默认填写这个就行），channel为https()时使用 119.29.29.99
                .dnsIp("119.29.29.98")
                //（可选）channel配置：基于 HTTP 请求的 DES 加密形式，默认为 desHttp()，另有 aesHttp()、https() 可选。（注意仅当选择 https 的 channel 需要选择 119.29.29.99 的dnsip并传入token，例如：.dnsIp('119.29.29.99').https().token('....') ）。
//                .desHttp()
                //（可选，选择 https channel 时进行设置）腾讯云官网（https://console.cloud.tencent.com/httpdns）申请获得，用于 HTTPS 校验。仅当选用https()时进行填写
//                .token("xxx")
                //（可选）日志粒度，如开启Debug打印则传入"Log.DEBUG"
                .logLevel(Log.DEBUG)
                //（可选）预解析域名，填写形式："baidu.com", "qq.com"，建议不要设置太多预解析域名，当前限制为最多 10 个域名。仅在初始化时触发。
//                .preLookupDomains("myqcloud.com")
                //（可选）解析缓存自动刷新, 以域名形式进行配置，填写形式："baidu.com", "qq.com"。配置的域名会在 TTL * 75% 时自动发起解析请求更新缓存，实现配置域名解析时始终命中缓存。此项建议不要设置太多域名，当前限制为最多 10 个域名。与预解析分开独立配置。
//                .persistentCacheDomains("baidu.com", "qq.com")
                // (可选) IP 优选，以 IpRankItem(hostname, port) 组成的 List 配置, port（可选）默认值为 8080。例如：IpRankItem("qq.com", 443)。sdk 会根据配置项进行 socket 连接测速情况对解析 IP 进行排序，IP 优选不阻塞当前解析，在下次解析时生效。当前限制为最多 10 项。
//                .ipRankItems(ipRankItemList)
                //（可选）手动指定网络栈支持情况，仅进行 IPv4 解析传 1，仅进行 IPv6 解析传 2，进行 IPv4、IPv6 双栈解析传 3。默认为根据客户端本地网络栈支持情况发起对应的解析请求。
//                .setCustomNetStack(3)
                //（可选）设置是否允许使用过期缓存，默认false，解析时先取未过期的缓存结果，不满足则等待解析请求完成后返回解析结果。
                // 设置为true时，会直接返回缓存的解析结果，没有缓存则返回0;0，用户可使用localdns（InetAddress）进行兜底。且在无缓存结果或缓存已过期时，会异步发起解析请求更新缓存。因异步API（getAddrByNameAsync，getAddrsByNameAsync）逻辑在回调中始终返回未过期的解析结果，设置为true时，异步API不可使用。建议使用同步API （getAddrByName，getAddrsByName）。
//                .setUseExpiredIpEnable(true)
                //（可选）设置是否启用本地缓存（Room），默认false
//                .setCachedIpEnable(true)
                //（可选）设置域名解析请求超时时间，默认为1000ms
//                .timeoutMills(1000)
                //（可选）是否开启解析异常上报，默认false，不上报
//                .enableReport(true)
                // 以build()结束
                .build();

        MSDKDnsResolver.getInstance().init(getContext(), dnsConfigBuilder);
    }
}
