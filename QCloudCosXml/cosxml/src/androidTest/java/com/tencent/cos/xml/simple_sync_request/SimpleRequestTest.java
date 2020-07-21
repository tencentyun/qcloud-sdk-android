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

package com.tencent.cos.xml.simple_sync_request;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.core.TestConfigs;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.bucket.DeleteBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketCORSResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketLifecycleResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketResult;
import com.tencent.cos.xml.model.bucket.GetBucketACLRequest;
import com.tencent.cos.xml.model.bucket.GetBucketACLResult;
import com.tencent.cos.xml.model.bucket.GetBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.GetBucketCORSResult;
import com.tencent.cos.xml.model.bucket.GetBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.GetBucketLifecycleResult;
import com.tencent.cos.xml.model.bucket.GetBucketLocationRequest;
import com.tencent.cos.xml.model.bucket.GetBucketLocationResult;
import com.tencent.cos.xml.model.bucket.GetBucketObjectVersionsRequest;
import com.tencent.cos.xml.model.bucket.GetBucketObjectVersionsResult;
import com.tencent.cos.xml.model.bucket.GetBucketRequest;
import com.tencent.cos.xml.model.bucket.GetBucketResult;
import com.tencent.cos.xml.model.bucket.HeadBucketRequest;
import com.tencent.cos.xml.model.bucket.HeadBucketResult;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsRequest;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsResult;
import com.tencent.cos.xml.model.bucket.PutBucketACLRequest;
import com.tencent.cos.xml.model.bucket.PutBucketACLResult;
import com.tencent.cos.xml.model.bucket.PutBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.PutBucketCORSResult;
import com.tencent.cos.xml.model.bucket.PutBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.PutBucketLifecycleResult;
import com.tencent.cos.xml.model.bucket.PutBucketRequest;
import com.tencent.cos.xml.model.bucket.PutBucketResult;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.AbortMultiUploadResult;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.CopyObjectResult;
import com.tencent.cos.xml.model.object.DeleteMultiObjectRequest;
import com.tencent.cos.xml.model.object.DeleteMultiObjectResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.DeleteObjectResult;
import com.tencent.cos.xml.model.object.GetObjectACLRequest;
import com.tencent.cos.xml.model.object.GetObjectACLResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.ListPartsResult;
import com.tencent.cos.xml.model.object.OptionObjectRequest;
import com.tencent.cos.xml.model.object.OptionObjectResult;
import com.tencent.cos.xml.model.object.PutObjectACLRequest;
import com.tencent.cos.xml.model.object.PutObjectACLResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.model.tag.AccessControlPolicy;
import com.tencent.cos.xml.model.tag.CORSConfiguration;
import com.tencent.cos.xml.model.tag.LifecycleConfiguration;
import com.tencent.cos.xml.model.tag.ListMultipartUploads;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.tencent.cos.xml.QServer.TAG;

@RunWith(AndroidJUnit4.class)
public class SimpleRequestTest {

    private static CosXmlService cosXmlService;

    @BeforeClass public static void setUp() {

        cosXmlService = TestUtils.newDefaultTerminalService();
    }


    private void putBucketTest() {

        PutBucketRequest putBucketRequest = new PutBucketRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        PutBucketResult putBucketResult = null;

        try {
            putBucketResult = cosXmlService.putBucket(putBucketRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            if (e.getErrorCode().equals("BucketAlreadyOwnedByYou")) {
                return;
            }
        }

        Assert.assertNotNull(putBucketResult);
        Assert.assertEquals(putBucketResult.httpCode, 200);
    }

    private void headBucketTest() {

        HeadBucketRequest headBucketRequest = new HeadBucketRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        HeadBucketResult headBucketResult = null;
        try {
            headBucketResult = cosXmlService.headBucket(headBucketRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(headBucketResult);
        Assert.assertEquals(headBucketResult.httpCode, 200);
    }


    private void getBucketTest() {

        GetBucketRequest getBucketRequest = new GetBucketRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        GetBucketResult getBucketResult = null;
        try {
            getBucketResult = cosXmlService.getBucket(getBucketRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(getBucketResult);
    }

    @Test
    public void getBucketObjectVersionsTest() {

        GetBucketObjectVersionsRequest getBucketObjectVersionsRequest = new GetBucketObjectVersionsRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        getBucketObjectVersionsRequest.setPrefix("versionsFolder/");
        getBucketObjectVersionsRequest.setDelimiter("/");
        GetBucketObjectVersionsResult getBucketObjectVersionsResult = null;

        try {
            getBucketObjectVersionsResult = cosXmlService.getBucketObjectVersions(getBucketObjectVersionsRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(getBucketObjectVersionsResult);
    }


    @Test public  void getServiceTest() {

        GetServiceRequest getServiceRequest = new GetServiceRequest();
        GetServiceResult getServiceResult = null;
        try {
            getServiceResult = cosXmlService.getService(getServiceRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(getServiceResult);
        Assert.assertEquals(200, getServiceResult.httpCode);
    }

    private void putBucketLifecycle() {

        PutBucketLifecycleRequest putBucketLifecycleRequest = new PutBucketLifecycleRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        LifecycleConfiguration.Rule rule = new LifecycleConfiguration.Rule();
        rule.id = "lifecycle_" + new Random(System.currentTimeMillis()).nextInt();
        rule.status = "Enabled";
        rule.expiration = new LifecycleConfiguration.Expiration();
        rule.expiration.days = 1;
        rule.filter = new LifecycleConfiguration.Filter();
        rule.filter.prefix = "logs";
        putBucketLifecycleRequest.setRuleList(rule);
        PutBucketLifecycleResult putBucketLifecycleResult = null;
        try {
            putBucketLifecycleResult = cosXmlService.putBucketLifecycle(putBucketLifecycleRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(200, putBucketLifecycleResult.httpCode);
    }


    private void getBucketLifecycle() {
        GetBucketLifecycleRequest getBucketLifecycleRequest = new GetBucketLifecycleRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        GetBucketLifecycleResult getBucketLifecycleResult = null;
        try {
            getBucketLifecycleResult = cosXmlService.getBucketLifecycle(getBucketLifecycleRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(200, getBucketLifecycleResult.httpCode);
        Assert.assertNotNull(getBucketLifecycleResult.lifecycleConfiguration);
    }


    private void deleteBucketLifecycle()  {
        DeleteBucketLifecycleRequest deleteBucketLifecycleRequest = new DeleteBucketLifecycleRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        DeleteBucketLifecycleResult deleteBucketLifecycleResult = null;
        try {
            deleteBucketLifecycleResult = cosXmlService.deleteBucketLifecycle(deleteBucketLifecycleRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(deleteBucketLifecycleResult);
        Assert.assertTrue(204 == deleteBucketLifecycleResult.httpCode);
    }

    private void putBucketCORS() {

        PutBucketCORSRequest putBucketCORSRequest = new PutBucketCORSRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        CORSConfiguration.CORSRule corsRule = new CORSConfiguration.CORSRule();
        corsRule.id = "cors" + new Random(System.currentTimeMillis()).nextInt();
        corsRule.maxAgeSeconds = 5000;
        corsRule.allowedOrigin = "cloud.tencent.com";
        corsRule.allowedMethod = new ArrayList<>();
        corsRule.allowedMethod.add("PUT");
        corsRule.allowedMethod.add("GET");
        corsRule.allowedHeader = new ArrayList<>();
        corsRule.allowedHeader.add("Host");
        corsRule.allowedHeader.add("Except");
        corsRule.exposeHeader = new ArrayList<>();
        corsRule.exposeHeader.add("x-cos-meta-1");
        List<CORSConfiguration.CORSRule> corsRules = new ArrayList<>();
        corsRules.add(corsRule);
        putBucketCORSRequest.addCORSRules(corsRules);
        PutBucketCORSResult putBucketCORSResult = null;
        try {
            putBucketCORSResult = cosXmlService.putBucketCORS(putBucketCORSRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(putBucketCORSRequest);
        Assert.assertEquals(200, putBucketCORSResult.httpCode);
    }

    private void getBucketCORS()  {
        GetBucketCORSRequest getBucketCORSRequest = new GetBucketCORSRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        GetBucketCORSResult getBucketCORSResult = null;
        try {
            getBucketCORSResult = cosXmlService.getBucketCORS(getBucketCORSRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(getBucketCORSResult);
        Assert.assertEquals(200, getBucketCORSResult.httpCode);
        Assert.assertNotNull(getBucketCORSResult.corsConfiguration);
    }

    private void deleteBucketCORS() {
        DeleteBucketCORSRequest deleteBucketCORSRequest = new DeleteBucketCORSRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        DeleteBucketCORSResult deleteBucketCORSResult = null;
        try {
            deleteBucketCORSResult = cosXmlService.deleteBucketCORS(deleteBucketCORSRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(deleteBucketCORSResult);
        Assert.assertTrue(204 == deleteBucketCORSResult.httpCode);
    }

    private void putBucketACL() {
        PutBucketACLRequest putBucketACLRequest = new PutBucketACLRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(QServer.ownUin, QServer.ownUin);
        putBucketACLRequest.setXCOSGrantRead(aclAccount);
        putBucketACLRequest.setXCOSGrantWrite(aclAccount);
        putBucketACLRequest.setXCOSACL(COSACL.PRIVATE);
        PutBucketACLResult putBucketACLResult = null;
        try {
            putBucketACLResult = cosXmlService.putBucketACL(putBucketACLRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(putBucketACLResult);
        Assert.assertEquals(200, putBucketACLResult.httpCode);
    }

    private void getBucketACL() {
        GetBucketACLRequest getBucketACLRequest = new GetBucketACLRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        GetBucketACLResult getBucketACLResult = null;
        try {
            getBucketACLResult = cosXmlService.getBucketACL(getBucketACLRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(getBucketACLResult);
        Assert.assertEquals(200, getBucketACLResult.httpCode);
        Assert.assertNotNull(getBucketACLResult.accessControlPolicy);
    }

    private void getBucketLocation() {
        GetBucketLocationRequest getBucketLocationRequest = new GetBucketLocationRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        GetBucketLocationResult getBucketLocationResult = null;
        try {
            getBucketLocationResult = cosXmlService.getBucketLocation(getBucketLocationRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(getBucketLocationResult);
        Assert.assertEquals(200, getBucketLocationResult.httpCode);
        Assert.assertNotNull(getBucketLocationResult.locationConstraint);
    }


    private void putObject() {

        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConfigs.TERMINAL_TEMP_BUCKET, TestConfigs.COS_TXT_1M_PATH, TestConfigs.LOCAL_TXT_1M_PATH);
        putObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TAG, complete + "/" + target);
            }
        });
        PutObjectResult putObjectResult = null;
        try {
            putObjectResult = cosXmlService.putObject(putObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(putObjectResult);
        Assert.assertEquals(200, putObjectResult.httpCode);
    }


    private void headObject() {

        HeadObjectRequest headObjectRequest = new HeadObjectRequest(TestConfigs.TERMINAL_TEMP_BUCKET, TestConfigs.COS_TXT_1M_PATH);
        HeadObjectResult headObjectResult = null;
        try {
            headObjectResult = cosXmlService.headObject(headObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(headObjectResult);
        Assert.assertEquals(200, headObjectResult.httpCode);
    }


    private void optionObject() {

        String origin = "cloud.tencent.com";
        String method = "GET";
        OptionObjectRequest optionObjectRequest = new OptionObjectRequest(TestConfigs.TERMINAL_TEMP_BUCKET, TestConfigs.COS_TXT_1M_PATH, origin, method);
        // optionObjectRequest.setAccessControlHeaders("Authorization");
        OptionObjectResult optionObjectResult = null;
        try {
            optionObjectResult = cosXmlService.optionObject(optionObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(optionObjectResult);
        Assert.assertEquals(200, optionObjectResult.httpCode);
    }

    @Test public  void putObjectACL()  {
        PutObjectACLRequest putObjectACLRequest = new PutObjectACLRequest(TestConfigs.TERMINAL_PERSIST_BUCKET, "batch_download_sample");
//        putObjectACLRequest.setXCOSACL(COSACL.PRIVATE);
//        ACLAccount aclAccount = new ACLAccount();
//        aclAccount.addAccount(QServer.ownUin, QServer.ownUin);
//        putObjectACLRequest.setXCOSGrantRead(aclAccount);
//        putObjectACLRequest.setXCOSGrantWrite(aclAccount);
        AccessControlPolicy accessControlPolicy = new AccessControlPolicy();
        AccessControlPolicy.Owner owner = new AccessControlPolicy.Owner();
        owner.id = String.format("qcs::cam::uin/%s:uin/%s", "1278687956",  "1278687956");

        AccessControlPolicy.AccessControlList accessControlList = new AccessControlPolicy.AccessControlList();
        List<AccessControlPolicy.Grant> grants = new LinkedList<>();
        accessControlList.grants = grants;
        accessControlPolicy.accessControlList = accessControlList;
        accessControlPolicy.owner = owner;
        PutObjectACLResult putObjectACLResult = null;

        // 添加用户权限
        //for (ObjectPermissions.UserPermission userPermission : userPermissions) {

            //for (String per : userPermission.permissions) {
                AccessControlPolicy.Grant grant = new AccessControlPolicy.Grant();
                AccessControlPolicy.Grantee grantee = new AccessControlPolicy.Grantee();
                grantee.id = String.format("qcs::cam::uin/%s:uin/%s", "1059310816", "1059310816");
                grant.grantee = grantee;
                grant.permission = "READ";
                grants.add(grant);

        AccessControlPolicy.Grant grant3 = new AccessControlPolicy.Grant();
        AccessControlPolicy.Grantee grantee3 = new AccessControlPolicy.Grantee();
        grantee3.uri = "http://cam.qcloud.com/groups/global/AllUsers";
        grant3.grantee = grantee3;
        grant3.permission = "READ";
        grants.add(grant3);

        AccessControlPolicy.Grant grant2 = new AccessControlPolicy.Grant();
        AccessControlPolicy.Grantee grantee2 = new AccessControlPolicy.Grantee();
        grantee2.id = String.format("qcs::cam::uin/%s:uin/%s", "1059310816", "1059310816");
        grant2.grantee = grantee2;
        grant2.permission = "WRITE";
        grants.add(grant2);
            //}
        //}
        putObjectACLRequest.setAccessControlPolicy(accessControlPolicy);
        try {
            putObjectACLResult = cosXmlService.putObjectACL(putObjectACLRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(putObjectACLResult);
        Assert.assertEquals(200, putObjectACLResult.httpCode);
    }



    private void getObjectACL()  {
        GetObjectACLRequest getObjectACLRequest = new GetObjectACLRequest(TestConfigs.TERMINAL_TEMP_BUCKET, TestConfigs.COS_TXT_1M_PATH);
        GetObjectACLResult getObjectACLResult = null;
        try {
            getObjectACLResult = cosXmlService.getObjectACL(getObjectACLRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(getObjectACLResult);
        Assert.assertEquals(200, getObjectACLResult.httpCode);
        Assert.assertNotNull(getObjectACLResult.accessControlPolicy);
    }

    private void copyObject() {

        String destCosPath = "/copy/" + TestConfigs.COS_TXT_1M_PATH;
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConfigs.TERMINAL_APPID, TestConfigs.TERMINAL_TEMP_BUCKET, TestConfigs.TERMINAL_DEFAULT_REGION, TestConfigs.COS_TXT_1M_PATH);
        CopyObjectRequest copyObjectRequest = null;
        CopyObjectResult copyObjectResult = null;
        try {
            copyObjectRequest = new CopyObjectRequest(TestConfigs.TERMINAL_TEMP_BUCKET, destCosPath, copySourceStruct);
            copyObjectResult = cosXmlService.copyObject(copyObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
//        copyObjectRequest.setCopyMetaDataDirective(MetaDataDirective.REPLACED);
//        copyObjectRequest.setRequestHeaders("x-cos-meta-xml", "cos");
        Assert.assertNotNull(copyObjectResult);
        Assert.assertEquals(200, copyObjectResult.httpCode);
        Assert.assertNotNull(copyObjectResult.copyObject);
    }


    private void getObject() {

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConfigs.TERMINAL_TEMP_BUCKET, TestConfigs.COS_TXT_1M_PATH, TestConfigs.LOCAL_FILE_DIRECTORY);
        getObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TAG, complete + "/" + target);
            }
        });
        GetObjectResult getObjectResult = null;
        try {
            getObjectResult = cosXmlService.getObject(getObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(getObjectResult);
        Assert.assertEquals(200, getObjectResult.httpCode);
        File file = new File(TestConfigs.LOCAL_TXT_1M_PATH);
        System.out.println("file path is " + getObjectRequest.getDownloadPath());
        Assert.assertEquals(file.length(), 1024 * 1024);

        //QServer.deleteLocalFile(getObjectRequest.getDownloadPath());
    }


    /**
     * Test initMultipartUpload、ListParts、UploadPart、CompleteMultiUpload
     *
     * @throws CosXmlServiceException
     * @throws CosXmlClientException
     */
    private void multiUploadObject() {

        String fileName = TestConfigs.COS_TXT_1M_PATH;
        String bucketName = TestConfigs.TERMINAL_TEMP_BUCKET;

        // 初始化分片上传
        InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucketName, fileName);
        InitMultipartUploadResult initMultipartUploadResult = null;
        try {
            initMultipartUploadResult = cosXmlService.initMultipartUpload(initMultipartUploadRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(initMultipartUploadResult);
        Assert.assertEquals(200, initMultipartUploadResult.httpCode);
        Assert.assertNotNull(initMultipartUploadResult.initMultipartUpload.uploadId);

        // 查询分片上传
        String uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, fileName, uploadId);
        ListPartsResult listPartsResult = null;
        try {
            listPartsResult = cosXmlService.listParts(listPartsRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(listPartsResult);
        Assert.assertEquals(200, listPartsResult.httpCode);
        Assert.assertNotNull(listPartsResult.listParts);

        // 上传分片
        int partNumber = 1;
        String localFilePath = TestConfigs.LOCAL_TXT_1M_PATH;
        UploadPartRequest uploadPartRequest1 = new UploadPartRequest(bucketName, fileName, partNumber, localFilePath, uploadId);
        uploadPartRequest1.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TAG, complete + "/" + target);
            }
        });
        UploadPartResult uploadPartResult1 = null;
        try {
            uploadPartResult1 = cosXmlService.uploadPart(uploadPartRequest1);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(uploadPartResult1);
        Assert.assertEquals(200, uploadPartResult1.httpCode);
        Assert.assertNotNull(uploadPartResult1.eTag);

        // 完成分片上传
        CompleteMultiUploadRequest completeMultiUploadRequest = new CompleteMultiUploadRequest(bucketName, fileName, uploadId, null);
        completeMultiUploadRequest.setPartNumberAndETag(1, uploadPartResult1.eTag);
        CompleteMultiUploadResult completeMultiUploadResult = null;
        try {
            completeMultiUploadResult = cosXmlService.completeMultiUpload(completeMultiUploadRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(completeMultiUploadResult);
        Assert.assertEquals(completeMultiUploadResult.httpCode, 200);
        Assert.assertNotNull(completeMultiUploadResult.completeMultipartUpload);
    }

    private void deleteAllUploadIdOfBucket() {

        ListMultiUploadsRequest listMultiUploadsRequest = new ListMultiUploadsRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        ListMultiUploadsResult listMultiUploadsResult = null;
        try {
            listMultiUploadsResult = cosXmlService.listMultiUploads(listMultiUploadsRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Log.d(TAG, listMultiUploadsResult.printResult());
        List<ListMultipartUploads.Upload> uploadList = listMultiUploadsResult.listMultipartUploads.uploads;
        if( uploadList != null && uploadList.size() > 0){
            for(ListMultipartUploads.Upload upload : uploadList){
                String uploadId = upload.uploadID;
                String key = upload.key;
                AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(TestConfigs.TERMINAL_TEMP_BUCKET, key, uploadId);
                AbortMultiUploadResult abortMultiUploadResult = null;
                try {
                    abortMultiUploadResult = cosXmlService.abortMultiUpload(abortMultiUploadRequest);
                } catch (CosXmlClientException e) {
                    e.printStackTrace();
                } catch (CosXmlServiceException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, abortMultiUploadResult.printResult());
            }
        }
        if(listMultiUploadsResult.listMultipartUploads.isTruncated){
            deleteAllUploadIdOfBucket();
        }
    }

    private void deleteObject() {

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(TestConfigs.TERMINAL_TEMP_BUCKET, TestConfigs.COS_TXT_1M_PATH);
        DeleteObjectResult deleteObjectResult = null;
        try {
            deleteObjectResult = cosXmlService.deleteObject(deleteObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(deleteObjectResult);
        Assert.assertTrue(204 == deleteObjectResult.httpCode);
    }

    private void deleteMultiObject() {

        DeleteMultiObjectRequest deleteMultiObjectRequest = new DeleteMultiObjectRequest(TestConfigs.TERMINAL_TEMP_BUCKET, null);
        deleteMultiObjectRequest.setObjectList(TestConfigs.COS_TXT_1M_PATH);
        deleteMultiObjectRequest.setQuiet(false);
        DeleteMultiObjectResult deleteMultiObjectResult = null;
        try {
            deleteMultiObjectResult = cosXmlService.deleteMultiObject(deleteMultiObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(deleteMultiObjectResult);
        Assert.assertEquals(200, deleteMultiObjectResult.httpCode);
    }

    private void deleteBucketTest() {

        DeleteBucketRequest deleteBucketRequest = new DeleteBucketRequest(TestConfigs.TERMINAL_TEMP_BUCKET);
        DeleteBucketResult deleteBucketResult = null;

        try {
            deleteBucketResult = cosXmlService.deleteBucket(deleteBucketRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            if (e.getErrorCode().equals("BucketNotEmpty")) {
                return;
            }
        }

        Assert.assertNotNull(deleteBucketResult);
        Assert.assertTrue(deleteBucketResult.httpCode == 204);
    }


    /**
     * 同步简单请求测试
     */
    @Test
    public void testSimpleSyncMethod() {
        // if(!QServer.cspTest) return;
        putBucketTest();
        headBucketTest();
        getBucketTest();
        getServiceTest();

        putBucketLifecycle();
        getBucketLifecycle();

        putBucketCORS();
        getBucketCORS();


        putBucketACL();
        getBucketACL();

        getBucketLocation();


        putObject();
        headObject();
        optionObject();

        multiUploadObject();
        putObjectACL();
        getObjectACL();
        copyObject();
        getObject();

        deleteAllUploadIdOfBucket();
        deleteObject();
        deleteMultiObject();

        deleteBucketCORS();
        deleteBucketLifecycle();
        deleteBucketTest();

    }


    public void testCspTrunkedDownload() {
        if(!QServer.cspTest)return;
        putBucketTest();
        putObject();
        getObject();
    }



//    // TODO: 2018/9/3  tce don't need
//    private void postObject() {
//
//        String cosPath = postFileName;
//        String srcPath = null;
//        try {
//            srcPath = QServer.createFile(context, 1024 * 1024);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        byte[] data = new byte[0];
//        try {
//            data = "this is post object test".getBytes("utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        PostObjectRequest postObjectRequest = new PostObjectRequest(bucketName, cosPath, data);
//        postObjectRequest.setProgressListener(new CosXmlProgressListener() {
//            @Override
//            public void onProgress(long complete, long target) {
//                Log.d("XIAO", "progress =" + complete / target);
//            }
//        });
//
//        PostObjectResult postObjectResult = null;
//        try {
//            postObjectResult = cosXmlService.postObject(postObjectRequest);
//        } catch (CosXmlClientException e) {
//            e.printStackTrace();
//        } catch (CosXmlServiceException e) {
//            e.printStackTrace();
//        }
//        QServer.deleteLocalFile(srcPath);
//    }


//    @Test public void partCopyObject() throws CosXmlServiceException, CosXmlClientException {
//
//        String destCosPath = "part_copy_" + fileName;
//        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
//                QServer.appid, bucketName, QServer.region, fileName);
//        InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucketName, destCosPath);
//        InitMultipartUploadResult initMultipartUploadResult = cosXmlService.initMultipartUpload(initMultipartUploadRequest);
//        String uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;
//        UploadPartCopyRequest uploadPartCopyRequest = new UploadPartCopyRequest(bucketName, destCosPath, 1, uploadId,
//                copySourceStruct);
//        UploadPartCopyResult uploadPartCopyResult = cosXmlService.copyObject(uploadPartCopyRequest);
//        String eTag = uploadPartCopyResult.copyObject.eTag;
//        Map<Integer, String> partNumberAndETag = new HashMap<>();
//        partNumberAndETag.put(1, eTag);
//        CompleteMultiUploadRequest completeMultiUploadRequest = new CompleteMultiUploadRequest(bucketName, destCosPath,
//                uploadId, partNumberAndETag);
//        CompleteMultiUploadResult completeMultiUploadResult = cosXmlService.completeMultiUpload(completeMultiUploadRequest);
//        Log.d(TAG, completeMultiUploadResult.printResult());
//        //QServer.deleteCOSObject(context, bucket, destCosPath);
//    }


}
