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

package com.tencent.cos.xml.model.bucket;

import android.content.Context;

import com.tencent.cos.xml.core.TestUtils;;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.*;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.*;
import com.tencent.cos.xml.listener.*;
import com.tencent.cos.xml.model.*;
import com.tencent.cos.xml.model.tag.*;
import com.tencent.qcloud.core.auth.*;
import com.tencent.qcloud.core.http.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.*;
import java.util.*;


@RunWith(AndroidJUnit4.class)
public class BucketACLTest {

    private static Context context;

    @BeforeClass public static void setUp() {
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
            return;
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);

        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);

        String bucket = "examplebucket-1250000000";
        PutBucketRequest putBucketRequest = new PutBucketRequest(bucket);

        //定义存储桶的 ACL 属性。有效值：private，public-read-write，public-read；默认值：private
        putBucketRequest.setXCOSACL("private");

        //赋予被授权者读的权限
        ACLAccount readACLS = new ACLAccount();
        readACLS.addAccount("OwnerUin", "SubUin");
        putBucketRequest.setXCOSGrantRead(readACLS);

        //赋予被授权者写的权限
        ACLAccount writeACLS = new ACLAccount();
        writeACLS.addAccount("OwnerUin", "SubUin");
        putBucketRequest.setXCOSGrantRead(writeACLS);

        //赋予被授权者读写的权限
        ACLAccount writeandReadACLS = new ACLAccount();
        writeandReadACLS.addAccount("OwnerUin", "SubUin");
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
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
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

        context = TestUtils.getContext();
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
            return;
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);

        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);

        String bucket = "examplebucket-1250000000"; //格式：BucketName-APPID
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
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
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
            return;
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);

        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);

        String bucket = "examplebucket-1250000000"; //格式：BucketName-APPID
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
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
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
    public void PutBucketAcl()
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
            return;
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);

        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);

        String bucket = "examplebucket-1250000000"; //格式：BucketName-APPID
        PutBucketACLRequest putBucketACLRequest = new PutBucketACLRequest(bucket);

        //设置 bucket 访问权限
        putBucketACLRequest.setXCOSACL("public-read");

        //赋予被授权者读的权限
        ACLAccount readACLS = new ACLAccount();
        readACLS.addAccount("OwnerUin", "SubUin");
        putBucketACLRequest.setXCOSGrantRead(readACLS);

        //赋予被授权者写的权限
        ACLAccount writeACLS = new ACLAccount();
        writeACLS.addAccount("OwnerUin", "SubUin");
        putBucketACLRequest.setXCOSGrantRead(writeACLS);

        //赋予被授权者读写的权限
        ACLAccount writeandReadACLS = new ACLAccount();
        writeandReadACLS.addAccount("OwnerUin", "SubUin");
        putBucketACLRequest.setXCOSGrantRead(writeandReadACLS);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        putBucketACLRequest.setSignParamsAndHeaders(null, headerKeys);
        // 使用同步方法
        try {
            PutBucketACLResult putBucketACLResult = cosXmlService.putBucketACL(putBucketACLRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

        // 使用异步回调请求
        cosXmlService.putBucketACLAsync(putBucketACLRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo Put Bucket ACL success
                PutBucketACLResult putBucketACLResult = (PutBucketACLResult)result;
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo Put Bucket ACL failed because of CosXmlClientException or CosXmlServiceException...
            }
        });


    }
    public void GetBucketAcl()
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
            return;
        }
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(httpRequest);

        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);

        String bucket = "examplebucket-1250000000"; //格式：BucketName-APPID
        GetBucketACLRequest getBucketACLRequest = new GetBucketACLRequest(bucket);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        getBucketACLRequest.setSignParamsAndHeaders(null, headerKeys);
        // 使用同步方法
        try {
            GetBucketACLResult getBucketACLResult = cosXmlService.getBucketACL(getBucketACLRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

        // 使用异步回调请求
        cosXmlService.getBucketACLAsync(getBucketACLRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo Get Bucket ACL success
                GetBucketACLResult getBucketACLResult = (GetBucketACLResult)result;
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo Get Bucket ACL failed because of CosXmlClientException or CosXmlServiceException...
            }
        });


    }

    @Test
    public void testBucketACL() {
        HeadBucket();
        PutBucketAcl();
        GetBucketAcl();
    }
}
