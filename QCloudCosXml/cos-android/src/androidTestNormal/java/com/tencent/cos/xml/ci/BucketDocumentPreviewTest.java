package com.tencent.cos.xml.ci;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CIService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateResult;
import com.tencent.cos.xml.model.ci.DescribeDocProcessBucketsRequest;
import com.tencent.cos.xml.model.ci.DescribeDocProcessBucketsResult;
import com.tencent.cos.xml.model.ci.PutBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.PutBucketDPStateResult;

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
}
