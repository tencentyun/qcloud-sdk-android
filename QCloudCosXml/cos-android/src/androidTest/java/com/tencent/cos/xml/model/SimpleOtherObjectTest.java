 
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

package com.tencent.cos.xml.model;

import static com.tencent.cos.xml.core.TestUtils.smallFilePath;

import android.net.Uri;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosTrackService;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.AbortMultiUploadResult;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.ListPartsResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.cos.xml.model.tag.ACLAccount;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class SimpleOtherObjectTest {

    @Test
    public void getObjectBytes() {
        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            byte[] bytes = cosXmlService.getObject(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
            Assert.assertNotNull(bytes);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }

        try {
            byte[] bytes = cosXmlService.getObject(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+"notexist");
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.assertEquals(e.getErrorCode(), "NoSuchKey");
        }
    }

    /**
     * Test initMultipartUpload、ListParts、UploadPart、CompleteMultiUpload
     *
     * @throws CosXmlServiceException
     * @throws CosXmlClientException
     */
    @Test
    public void multiUploadObject() {
        TestUtils.sleep(10000);
        String fileName = TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH;
        String bucketName = TestConst.PERSIST_BUCKET;
        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();

        // 初始化分片上传
        InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucketName, fileName);
        initMultipartUploadRequest.setCacheControl("no-cache");
        initMultipartUploadRequest.setContentDisposition("inline");
        initMultipartUploadRequest.setContentEncoding("deflate");
        initMultipartUploadRequest.setExpires("0");
        initMultipartUploadRequest.setXCOSMeta("x-cos-meta-aaa","aaa");
        initMultipartUploadRequest.setStroageClass(COSStorageClass.STANDARD);
        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
        initMultipartUploadRequest.setXCOSGrantRead(aclAccount);
        initMultipartUploadRequest.setXCOSGrantWrite(null);
        initMultipartUploadRequest.setXCOSReadWrite(aclAccount);
        initMultipartUploadRequest.setXCOSACL(COSACL.DEFAULT);
        InitMultipartUploadResult initMultipartUploadResult = null;
        try {
            initMultipartUploadResult = cosXmlService.initMultipartUpload(initMultipartUploadRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(initMultipartUploadResult);
        Assert.assertNotNull(initMultipartUploadResult.printResult());
        Assert.assertEquals(200, initMultipartUploadResult.httpCode);
        Assert.assertNotNull(initMultipartUploadResult.initMultipartUpload.uploadId);


        // 查询分片上传
        String uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, fileName, uploadId);
        listPartsRequest.setUploadId(uploadId);
        Assert.assertEquals(uploadId, listPartsRequest.getUploadId());
        listPartsRequest.setMaxParts(1000);
        Assert.assertEquals(1000, listPartsRequest.getMaxParts());
//        listPartsRequest.setPartNumberMarker(0);
//        Assert.assertEquals(0, listPartsRequest.getPartNumberMarker());
        listPartsRequest.setEncodingType(null);
        Assert.assertEquals(null, listPartsRequest.getEncodingType());
        ListPartsResult listPartsResult = null;
        try {
            listPartsResult = cosXmlService.listParts(listPartsRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(listPartsResult);
        Assert.assertNotNull(listPartsResult.printResult());
        Assert.assertEquals(200, listPartsResult.httpCode);
        Assert.assertNotNull(listPartsResult.listParts);

        // 上传分片
        int partNumber = 1;
        InputStream inputStream = new ByteArrayInputStream((getBigString()+"asdasdasd").getBytes());
        UploadPartRequest uploadPartRequest1 = null;
        try {
            uploadPartRequest1 = new UploadPartRequest(bucketName, fileName,
                    partNumber, inputStream, uploadId);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        uploadPartRequest1.setTrafficLimit(838860800);
        uploadPartRequest1.setPartNumber(partNumber);
        Assert.assertEquals(partNumber, uploadPartRequest1.getPartNumber());
        uploadPartRequest1.setUploadId(uploadId);
        Assert.assertEquals(uploadId, uploadPartRequest1.getUploadId());
        uploadPartRequest1.setData(getBigString().getBytes());
        Assert.assertNotNull(uploadPartRequest1.getData());
        Assert.assertNotEquals(uploadPartRequest1.getFileLength(), 0);
        uploadPartRequest1.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TestConst.UT_TAG, complete + "/" + target);
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
        Assert.assertNotNull(uploadPartResult1.printResult());
        Assert.assertEquals(200, uploadPartResult1.httpCode);
        Assert.assertNotNull(uploadPartResult1.eTag);

        // 上传分片
        int partNumber2 = 2;
        String localFilePath2 = TestUtils.smallFilePath();
        UploadPartRequest uploadPartRequest2 = new UploadPartRequest(bucketName, fileName, partNumber2, localFilePath2, uploadId);
        uploadPartRequest2.setPartNumber(partNumber2);
        Assert.assertEquals(partNumber2, uploadPartRequest2.getPartNumber());
        uploadPartRequest2.setUploadId(uploadId);
        Assert.assertEquals(uploadId, uploadPartRequest2.getUploadId());
        uploadPartRequest2.setSrcPath(localFilePath2);
        Assert.assertEquals(localFilePath2, uploadPartRequest2.getSrcPath());
        Assert.assertNotEquals(uploadPartRequest2.getFileLength(), 0);
        uploadPartRequest2.setPriorityLow();
        Assert.assertTrue(uploadPartRequest2.isPriorityLow());
        uploadPartRequest2.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TestConst.UT_TAG, complete + "/" + target);
            }
        });
        UploadPartResult uploadPartResult2 = null;
        try {
            uploadPartResult2 = cosXmlService.uploadPart(uploadPartRequest2);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(uploadPartResult2);
        Assert.assertEquals(200, uploadPartResult2.httpCode);
        Assert.assertNotNull(uploadPartResult2.eTag);
        // 上传分片
        int partNumber3 = 3;
        UploadPartRequest uploadPartRequest3 = new UploadPartRequest(bucketName, fileName, partNumber3, getBigString().getBytes(), uploadId);
        uploadPartRequest3.setPartNumber(partNumber3);
        Assert.assertEquals(partNumber3, uploadPartRequest3.getPartNumber());
        uploadPartRequest3.setUploadId(uploadId);
        Assert.assertEquals(uploadId, uploadPartRequest3.getUploadId());
        uploadPartRequest3.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TestConst.UT_TAG, complete + "/" + target);
            }
        });
        UploadPartResult uploadPartResult3 = null;
        try {
            uploadPartResult3 = cosXmlService.uploadPart(uploadPartRequest3);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(uploadPartResult3);
        Assert.assertEquals(200, uploadPartResult3.httpCode);
        Assert.assertNotNull(uploadPartResult3.eTag);
        // 上传分片
        int partNumber4 = 4;
        UploadPartRequest uploadPartRequest4 = new UploadPartRequest(
                bucketName,
                fileName,
                partNumber4,
                Uri.fromFile(new File(smallFilePath())),
                uploadId);
        uploadPartRequest4.setPartNumber(partNumber4);
        uploadPartRequest4.setUploadId(uploadId);
        UploadPartResult uploadPartResult4 = null;
        try {
            uploadPartResult4 = cosXmlService.uploadPart(uploadPartRequest4);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(uploadPartResult4);
        // 上传分片
        int partNumber5 = 5;
        InputStream inputStream5 = new ByteArrayInputStream((getBigString()+"asdasdasd").getBytes());
        UploadPartRequest uploadPartRequest5 = null;
        try {
            uploadPartRequest5 = new UploadPartRequest(
                    bucketName,
                    fileName,
                    partNumber5,
                    inputStream5,
                    1000,
                    uploadId);
        } catch (CosXmlClientException e) {
            throw new RuntimeException(e);
        }
        uploadPartRequest5.setPartNumber(partNumber5);
        uploadPartRequest5.setUploadId(uploadId);
        UploadPartResult uploadPartResult5 = null;
        try {
            uploadPartResult5 = cosXmlService.uploadPart(uploadPartRequest5);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(uploadPartResult5);
        // TODO: 2023/4/26 传url会报错
//        <Error>
//          <Code>MissingContentLength</Code>
//          <Message>You must provide the Content-Length HTTP header.</Message>
//          <Resource>/do_not_remove/big_object</Resource>
//          <RequestId>NjQ0OTE5NGVfOWY0YzMxMWVfNmNjOV8xMzAxNg==</RequestId>
//          <TraceId>OGVmYzZiMmQzYjA2OWNhODk0NTRkMTBiOWVmMDAxODc0OWRkZjk0ZDM1NmI1M2E2MTRlY2MzZDhmNmI5MWI1OTI5MWRkM2I1ZDU3ZWE5Y2YzNDYzZTEzY2JlMjU3NDQ0ZWM3MTJlNThkMDhlYzIwYWI1MjgzZTU4MTc5MmM5MGM=</TraceId>
//        </Error>
//        // 上传分片
//        int partNumber6 = 6;
//        UploadPartRequest uploadPartRequest6 = null;
//        try {
//            uploadPartRequest6 = new UploadPartRequest(
//                    bucketName,
//                    fileName,
//                    partNumber6,
//                    new URL(TestConst.PERSIST_BUCKET_CDN_SMALL_OBJECT_URL),
//                    uploadId);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        uploadPartRequest6.setPartNumber(partNumber6);
//        uploadPartRequest6.setUploadId(uploadId);
//        UploadPartResult uploadPartResult6 = null;
//        try {
//            uploadPartResult6 = cosXmlService.uploadPart(uploadPartRequest6);
//        } catch (CosXmlClientException e) {
//            e.printStackTrace();
//        } catch (CosXmlServiceException e) {
//            e.printStackTrace();
//        }
//        Assert.assertNotNull(uploadPartResult6);
//
//        // 上传分片
//        int partNumber7 = 7;
//        UploadPartRequest uploadPartRequest7 = null;
//        try {
//            uploadPartRequest7 = new UploadPartRequest(
//                    bucketName,
//                    fileName,
//                    partNumber7,
//                    new URL(TestConst.PERSIST_BUCKET_CDN_SMALL_OBJECT_URL),
//                    10,
//                    1000,
//                    uploadId);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        uploadPartRequest7.setPartNumber(partNumber7);
//        uploadPartRequest7.setUploadId(uploadId);
//        UploadPartResult uploadPartResult7 = null;
//        try {
//            uploadPartResult7 = cosXmlService.uploadPart(uploadPartRequest7);
//        } catch (CosXmlClientException e) {
//            e.printStackTrace();
//        } catch (CosXmlServiceException e) {
//            e.printStackTrace();
//        }
//        Assert.assertNotNull(uploadPartResult7);

        // 查询分片上传
        ListPartsRequest listPartsRequest1 = new ListPartsRequest(bucketName, fileName, uploadId);
        ListPartsResult listPartsResult1 = null;
        try {
            listPartsResult1 = cosXmlService.listParts(listPartsRequest1);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(listPartsResult1);
        Assert.assertNotNull(listPartsResult1.printResult());
        Assert.assertNotNull(listPartsResult1.listParts);

        // 完成分片上传
        CompleteMultiUploadRequest completeMultiUploadRequest = new CompleteMultiUploadRequest(bucketName, fileName, uploadId, null);
        completeMultiUploadRequest.setUploadId(uploadId);
        Assert.assertEquals(completeMultiUploadRequest.getUploadId(), uploadId);
        completeMultiUploadRequest.setPartNumberAndETag(1, uploadPartResult1.eTag);
        Map<Integer,String> partNumberAndETag = new HashMap<>();
        partNumberAndETag.put(2, uploadPartResult2.eTag);
        // 覆盖率 只要完成分片3 就会失败 错误码：-283，分片过小
//        partNumberAndETag.put(3, uploadPartResult3.eTag);
        completeMultiUploadRequest.setPartNumberAndETag(partNumberAndETag);
        Assert.assertNotNull(completeMultiUploadRequest.getCompleteMultipartUpload());
        CompleteMultiUploadResult completeMultiUploadResult = null;
        try {
            completeMultiUploadResult = cosXmlService.completeMultiUpload(completeMultiUploadRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(completeMultiUploadResult);
        Assert.assertNotNull(completeMultiUploadResult.printResult());
        Assert.assertEquals(completeMultiUploadResult.httpCode, 200);
        Assert.assertNotNull(completeMultiUploadResult.completeMultipartUpload);

        TestUtils.parseBadResponseBody(initMultipartUploadResult);
        TestUtils.parseBadResponseBody(listPartsResult);
        TestUtils.parseBadResponseBody(uploadPartResult1);
        TestUtils.parseBadResponseBody(completeMultiUploadResult);
    }

    @Test
    public void testAbortMultiUpload() {
        TestUtils.sleep(10000);
        String fileName = TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH;
        String bucketName = TestConst.PERSIST_BUCKET;
        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();

        // 初始化分片上传
        InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucketName, fileName);
        initMultipartUploadRequest.setXCOSACL(COSACL.DEFAULT.getAcl());
        InitMultipartUploadResult initMultipartUploadResult = null;
        try {
            initMultipartUploadResult = cosXmlService.initMultipartUpload(initMultipartUploadRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(initMultipartUploadResult);
        Assert.assertNotNull(initMultipartUploadResult.printResult());
        Assert.assertEquals(200, initMultipartUploadResult.httpCode);
        Assert.assertNotNull(initMultipartUploadResult.initMultipartUpload.uploadId);

        String uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;

        // 上传分片
        int partNumber = 1;
        InputStream inputStream = new ByteArrayInputStream((getBigString()+"asdasdasd").getBytes());
        UploadPartRequest uploadPartRequest1 = null;
        try {
            uploadPartRequest1 = new UploadPartRequest(bucketName, fileName,
                    partNumber, inputStream, uploadId);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        uploadPartRequest1.setTrafficLimit(838860800);
        uploadPartRequest1.setPartNumber(partNumber);
        Assert.assertEquals(partNumber, uploadPartRequest1.getPartNumber());
        uploadPartRequest1.setUploadId(uploadId);
        Assert.assertEquals(uploadId, uploadPartRequest1.getUploadId());
        uploadPartRequest1.setData(getBigString().getBytes());
        Assert.assertNotNull(uploadPartRequest1.getData());
        Assert.assertNotEquals(uploadPartRequest1.getFileLength(), 0);
        uploadPartRequest1.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TestConst.UT_TAG, complete + "/" + target);
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
        Assert.assertNotNull(uploadPartResult1.printResult());
        Assert.assertEquals(200, uploadPartResult1.httpCode);
        Assert.assertNotNull(uploadPartResult1.eTag);

        // 查询分片上传
        ListPartsRequest listPartsRequest1 = new ListPartsRequest(bucketName, fileName, uploadId);
        listPartsRequest1.setPartNumberMarker(0);
        Assert.assertEquals(listPartsRequest1.getPartNumberMarker(), 0);
        ListPartsResult listPartsResult1 = null;
        try {
            listPartsResult1 = cosXmlService.listParts(listPartsRequest1);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(listPartsResult1);
        Assert.assertNotNull(listPartsResult1.printResult());
        Assert.assertNotNull(listPartsResult1.listParts);

        //取消上传
        AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(bucketName, fileName, uploadId);
        abortMultiUploadRequest.setUploadId(uploadId);
        Assert.assertEquals(abortMultiUploadRequest.getUploadId(), uploadId);
        AbortMultiUploadResult abortMultiUploadResult = null;
        try {
            abortMultiUploadResult = cosXmlService.abortMultiUpload(abortMultiUploadRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(abortMultiUploadResult);
        Assert.assertNotNull(abortMultiUploadResult.printResult());
        Assert.assertEquals(abortMultiUploadResult.httpCode, 204);

        TestUtils.parseBadResponseBody(initMultipartUploadResult);
        TestUtils.parseBadResponseBody(uploadPartResult1);
        TestUtils.parseBadResponseBody(abortMultiUploadResult);
    }

    private String getBigString(){
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<100000;i++){
            sb.append("aaasdasdasdaaaasdaddasdasfdaaaaaaaaaaaaaaasdasdadsadasdaaaaadsadasdsadsadaaaaa");
        }
        return sb.toString();

    }

    @Test
    public void testReportError() {
        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            CosTrackService.getInstance().reportError("UT_Test", new IllegalStateException());
            Assert.assertTrue(true);
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testFlatInetAddressList() {
        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        String[] ipAddress = new String[]{"10.1.1.1", "10.1.1.2"};
        try {
            cosXmlService.addCustomerDNS("stackoverflowa.com", ipAddress);
        } catch (CosXmlClientException e) {
            throw new RuntimeException(e);
        }
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, "aaaaaaa");
        getObjectRequest.setRequestURL("https://stackoverflowa.com/");
        try {
            cosXmlService.getObject(getObjectRequest);
        } catch (CosXmlClientException ignored) {
            Assert.assertNull(getObjectRequest.getMetrics().getConnectAddress());
            return;
        } catch (CosXmlServiceException e) {
            Assert.assertNull(getObjectRequest.getMetrics().getConnectAddress());
            return;
        }
        Assert.fail();
    }
}