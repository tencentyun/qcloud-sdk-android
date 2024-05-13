package com.tencent.cos.xml.ci;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CIService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateResult;
import com.tencent.cos.xml.model.ci.DescribeDocProcessBucketsRequest;
import com.tencent.cos.xml.model.ci.DescribeDocProcessBucketsResult;
import com.tencent.cos.xml.model.ci.PutBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.PutBucketDPStateResult;
import com.tencent.cos.xml.model.tag.BucketDocumentPreviewState;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * <p>
 * Created by jordanqin on 2023/2/17 11:38.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BucketDocumentPreviewTest {
    @Test
    public void stage1_putBucketDocumentPreviewState() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PutBucketDPStateRequest request = new PutBucketDPStateRequest(TestConst.PERSIST_BUCKET);
        try {
            Assert.assertNotNull(ciService.getPresignedURL(request));
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        try {
            PutBucketDPStateResult result = ciService.putBucketDocumentPreviewState(request);
            TestUtils.parseBadResponseBody(result);
            TestUtils.printXML(result.getDocumentPreviewState());
            Assert.assertNotNull(result.getDocumentPreviewState());
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage2_getBucketDocumentPreviewState() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeDocProcessBucketsRequest request = new DescribeDocProcessBucketsRequest();
        request.setPageNumber(1);
        request.setPageSize(20);
        try {
            DescribeDocProcessBucketsResult result = ciService.describeDocProcessBuckets(request);
            TestUtils.parseBadResponseBody(result);
            TestUtils.printXML(result.describeDocProcessBuckets);
            Assert.assertNotNull(result.describeDocProcessBuckets);
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage3_deleteBucketDocumentPreviewState() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DeleteBucketDPStateRequest request = new DeleteBucketDPStateRequest(TestConst.PERSIST_BUCKET);
        try {
            DeleteBucketDPStateResult result = ciService.deleteBucketDocumentPreviewState(request);
            TestUtils.parseBadResponseBody(result);
            Assert.assertNotNull(result.printResult());
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage4_putBucketDocumentPreviewStateAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PutBucketDPStateRequest request = new PutBucketDPStateRequest(TestConst.PERSIST_BUCKET);
        final TestLocker testLocker = new TestLocker();
        ciService.putBucketDocumentPreviewStateAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                PutBucketDPStateResult putBucketDPStateResult = (PutBucketDPStateResult)result;
                TestUtils.parseBadResponseBody(putBucketDPStateResult);
                TestUtils.printXML(putBucketDPStateResult.getDocumentPreviewState());
                Assert.assertNotNull(putBucketDPStateResult.getDocumentPreviewState());
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test
    public void stage5_getBucketDocumentPreviewStateAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeDocProcessBucketsRequest request = new DescribeDocProcessBucketsRequest(TestConst.PERSIST_BUCKET_REGION);
        request.setPageNumber(1);
        request.setPageSize(20);
//        request.setRegions("ap-chongqing,ap-beijing");
        request.setRegions("All");
        request.setBucketName(TestConst.PERSIST_BUCKET.substring(0,3));
        final TestLocker testLocker = new TestLocker();
        ciService.describeDocProcessBucketsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                DescribeDocProcessBucketsResult describeDocProcessBucketsResult = (DescribeDocProcessBucketsResult)result;
                TestUtils.parseBadResponseBody(describeDocProcessBucketsResult);
                TestUtils.printXML(describeDocProcessBucketsResult.describeDocProcessBuckets);
                Assert.assertNotNull(describeDocProcessBucketsResult.describeDocProcessBuckets);
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test
    public void stage6_getBucketDocumentPreviewStateAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeDocProcessBucketsRequest request = new DescribeDocProcessBucketsRequest();
        request.setPageNumber(1);
        request.setPageSize(1);
        request.setRegions(TestConst.PERSIST_BUCKET_REGION);
        request.setBucketNames(TestConst.PERSIST_BUCKET);
        final TestLocker testLocker = new TestLocker();
        ciService.describeDocProcessBucketsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                DescribeDocProcessBucketsResult describeDocProcessBucketsResult = (DescribeDocProcessBucketsResult)result;
                TestUtils.parseBadResponseBody(describeDocProcessBucketsResult);
                TestUtils.printXML(describeDocProcessBucketsResult.describeDocProcessBuckets);

                if(describeDocProcessBucketsResult.describeDocProcessBuckets.docBucketList != null &&
                        describeDocProcessBucketsResult.describeDocProcessBuckets.docBucketList.size()>0) {
                    BucketDocumentPreviewState bucketDocumentPreviewState =
                            describeDocProcessBucketsResult.describeDocProcessBuckets.docBucketList.get(0);
                    Assert.assertEquals(bucketDocumentPreviewState.Name, TestConst.PERSIST_BUCKET);
                } else {
                    Assert.assertTrue(true);
                }
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test
    public void stage7_deleteBucketDocumentPreviewStateAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DeleteBucketDPStateRequest request = new DeleteBucketDPStateRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION);
        final TestLocker testLocker = new TestLocker();
        ciService.deleteBucketDocumentPreviewStateAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TestUtils.parseBadResponseBody(result);
                Assert.assertNotNull(result.printResult());
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
    }
}
