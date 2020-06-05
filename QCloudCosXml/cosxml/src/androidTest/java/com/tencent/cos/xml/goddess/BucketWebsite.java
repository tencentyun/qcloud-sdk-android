package com.tencent.cos.xml.goddess;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tencent.cos.xml.BuildConfig;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketTaggingRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketTaggingResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketWebsiteRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketWebsiteResult;
import com.tencent.cos.xml.model.bucket.GetBucketTaggingRequest;
import com.tencent.cos.xml.model.bucket.GetBucketTaggingResult;
import com.tencent.cos.xml.model.bucket.GetBucketWebsiteRequest;
import com.tencent.cos.xml.model.bucket.GetBucketWebsiteResult;
import com.tencent.cos.xml.model.bucket.HeadBucketRequest;
import com.tencent.cos.xml.model.bucket.HeadBucketResult;
import com.tencent.cos.xml.model.bucket.PutBucketRequest;
import com.tencent.cos.xml.model.bucket.PutBucketResult;
import com.tencent.cos.xml.model.bucket.PutBucketTaggingRequest;
import com.tencent.cos.xml.model.bucket.PutBucketTaggingResult;
import com.tencent.cos.xml.model.bucket.PutBucketWebsiteRequest;
import com.tencent.cos.xml.model.bucket.PutBucketWebsiteResult;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.model.tag.WebsiteConfiguration;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.SessionCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.http.HttpRequest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@RunWith(AndroidJUnit4.class)
public class BucketWebsite {

    private static Context context;

    private static void assertError(Exception e, boolean isMatch) {
        if (!isMatch) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void assertError(Exception e) {
        assertError(e, false);
    }

    private String uploadId;
    private String part1Etag;

    @BeforeClass public static void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
               .isHttps(true) // 设置 Https 请求
               .setRegion("ap-guangzhou") // 设置默认的存储桶地域
               .builder();
        
        // 构建一个从临时密钥服务器拉取临时密钥的 Http 请求
        HttpRequest<String> httpRequest = null;
        try {
           httpRequest = new HttpRequest.Builder<String>()
                   .url(new URL(""))
                   .build();
        } catch (MalformedURLException e) {
           e.printStackTrace();
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);
        credentialProvider = new ShortTimeCredentialProvider(BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, 3600); // for ut
        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);
        
        String bucket = "bucket-cssg-android-temp-1253653367";
        PutBucketRequest putBucketRequest = new PutBucketRequest(bucket);
        
        //定义存储桶的 ACL 属性。有效值：private，public-read-write，public-read；默认值：private
        putBucketRequest.setXCOSACL("private");
        
        //赋予被授权者读的权限
        ACLAccount readACLS = new ACLAccount();
        readACLS.addAccount("1278687956", "1278687956");
        putBucketRequest.setXCOSGrantRead(readACLS);
        
        //赋予被授权者写的权限
        ACLAccount writeACLS = new ACLAccount();
        writeACLS.addAccount("1278687956", "1278687956");
        putBucketRequest.setXCOSGrantRead(writeACLS);
        
        //赋予被授权者读写的权限
        ACLAccount writeandReadACLS = new ACLAccount();
        writeandReadACLS.addAccount("1278687956", "1278687956");
        putBucketRequest.setXCOSGrantRead(writeandReadACLS);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        putBucketRequest.setSignParamsAndHeaders(null, headerKeys);
        //使用同步方法
        try {
            PutBucketResult putBucketResult = cosXmlService.putBucket(putBucketRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e, e.getStatusCode() == 409);
        }
        
        // 使用异步回调请求
        cosXmlService.putBucketAsync(putBucketRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo Put Bucket success
                PutBucketResult putBucketResult = (PutBucketResult)result;
            }
        
            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo Put Bucket failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
        
    }

    @AfterClass public static void tearDown() {
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
               .isHttps(true) // 设置 Https 请求
               .setRegion("ap-guangzhou") // 设置默认的存储桶地域
               .builder();
        
        // 构建一个从临时密钥服务器拉取临时密钥的 Http 请求
        HttpRequest<String> httpRequest = null;
        try {
           httpRequest = new HttpRequest.Builder<String>()
                   .url(new URL(""))
                   .build();
        } catch (MalformedURLException e) {
           e.printStackTrace();
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);
        credentialProvider = new ShortTimeCredentialProvider(BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, 3600); // for ut
        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);
        
        String bucket = "bucket-cssg-android-temp-1253653367"; //格式：BucketName-APPID
        DeleteBucketRequest deleteBucketRequest = new DeleteBucketRequest(bucket);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        deleteBucketRequest.setSignParamsAndHeaders(null, headerKeys);
        // 使用同步方法
        try {
            DeleteBucketResult deleteBucketResult = cosXmlService.deleteBucket(deleteBucketRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e);
        }
        
        // 使用异步回调请求
        cosXmlService.deleteBucketAsync(deleteBucketRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo Delete Bucket success
          DeleteBucketResult deleteBucketResult = (DeleteBucketResult)result;
            }
        
            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo Delete Bucket failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
        
    }

    @Test
    public void HeadBucket()
    {
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
               .isHttps(true) // 设置 Https 请求
               .setRegion("ap-guangzhou") // 设置默认的存储桶地域
               .builder();
        
        // 构建一个从临时密钥服务器拉取临时密钥的 Http 请求
        HttpRequest<String> httpRequest = null;
        try {
           httpRequest = new HttpRequest.Builder<String>()
                   .url(new URL(""))
                   .build();
        } catch (MalformedURLException e) {
           e.printStackTrace();
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);
        credentialProvider = new ShortTimeCredentialProvider(BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, 3600); // for ut
        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);
        
        String bucket = "bucket-cssg-android-temp-1253653367"; //格式：BucketName-APPID
        HeadBucketRequest headBucketRequest = new HeadBucketRequest(bucket);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        headBucketRequest.setSignParamsAndHeaders(null, headerKeys);
        //使用同步方法
        try {
            HeadBucketResult headBucketResult = cosXmlService.headBucket(headBucketRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e);
        }
        
        // 使用异步回调请求
        cosXmlService.headBucketAsync(headBucketRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo Head Bucket success
          HeadBucketResult headBucketResult = (HeadBucketResult)result;
            }
        
            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo Head Bucket failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
        
    }
    public void PutBucketWebsite()
    {
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
               .isHttps(true) // 设置 Https 请求
               .setRegion("ap-guangzhou") // 设置默认的存储桶地域
               .builder();
        
        // 构建一个从临时密钥服务器拉取临时密钥的 Http 请求
        HttpRequest<String> httpRequest = null;
        try {
           httpRequest = new HttpRequest.Builder<String>()
                   .url(new URL(""))
                   .build();
        } catch (MalformedURLException e) {
           e.printStackTrace();
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);
        credentialProvider = new ShortTimeCredentialProvider(BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, 3600); // for ut
        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);
        
        String bucket = "bucket-cssg-android-temp-1253653367"; //格式：BucketName-APPID
        PutBucketWebsiteRequest putBucketWebsiteRequest = new PutBucketWebsiteRequest(bucket);
        putBucketWebsiteRequest.setIndexDocument("index.html");

        // 使用同步方法
        try {
            PutBucketWebsiteResult putBucketWebsiteResult = cosXmlService.putBucketWebsite(putBucketWebsiteRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e);
        }

        // 使用异步回调请求
        cosXmlService.putBucketWebsiteAsync(putBucketWebsiteRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo Put Bucket ACL success
                PutBucketWebsiteResult putBucketWebsiteResult = (PutBucketWebsiteResult) result;
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo Put Bucket ACL failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
        
        
    }
    public void GetBucketWebsite()
    {
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
               .isHttps(true) // 设置 Https 请求
               .setRegion("ap-guangzhou") // 设置默认的存储桶地域
               .builder();
        
        // 构建一个从临时密钥服务器拉取临时密钥的 Http 请求
        HttpRequest<String> httpRequest = null;
        try {
           httpRequest = new HttpRequest.Builder<String>()
                   .url(new URL(""))
                   .build();
        } catch (MalformedURLException e) {
           e.printStackTrace();
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);
        credentialProvider = new ShortTimeCredentialProvider(BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, 3600); // for ut
        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);
        
        String bucket = "bucket-cssg-android-temp-1253653367"; //格式：BucketName-APPID
        GetBucketWebsiteRequest getBucketWebsiteRequest = new GetBucketWebsiteRequest(bucket);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        getBucketWebsiteRequest.setSignParamsAndHeaders(null, headerKeys);
        // 使用同步方法
        try {
            GetBucketWebsiteResult getBucketWebsiteResult = cosXmlService.getBucketWebsite(getBucketWebsiteRequest);
            System.out.println();
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e);
        }
        
        // 使用异步回调请求
        cosXmlService.getBucketWebsiteAsync(getBucketWebsiteRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo Get Bucket ACL success
                GetBucketWebsiteResult getBucketWebsiteResult = (GetBucketWebsiteResult)result;
            }
        
            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo Get Bucket ACL failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
        
        
    }

    public void DeleteBucketWebsite()
    {
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true) // 设置 Https 请求
                .setRegion("ap-guangzhou") // 设置默认的存储桶地域
                .builder();

        // 构建一个从临时密钥服务器拉取临时密钥的 Http 请求
        HttpRequest<String> httpRequest = null;
        try {
            httpRequest = new HttpRequest.Builder<String>()
                    .url(new URL(""))
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);
        credentialProvider = new ShortTimeCredentialProvider(BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, 3600); // for ut
        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);

        String bucket = "bucket-cssg-android-temp-1253653367"; //格式：BucketName-APPID
        DeleteBucketWebsiteRequest deleteBucketWebsiteRequest = new DeleteBucketWebsiteRequest(bucket);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        deleteBucketWebsiteRequest.setSignParamsAndHeaders(null, headerKeys);
        // 使用同步方法
        try {
            DeleteBucketWebsiteResult deleteBucketWebsiteResult = cosXmlService.deleteBucketWebsite(deleteBucketWebsiteRequest);
            System.out.println();
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e);
        }

        // 使用异步回调请求
        cosXmlService.deleteBucketWebsiteAsync(deleteBucketWebsiteRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo Get Bucket ACL success
                GetBucketWebsiteResult getBucketWebsiteResult = (GetBucketWebsiteResult)result;
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo Get Bucket ACL failed because of CosXmlClientException or CosXmlServiceException...
            }
        });


    }

    @Test public void testBucketACL() {
        HeadBucket();
        PutBucketWebsite();
        GetBucketWebsite();
        DeleteBucketWebsite();
    }
}
