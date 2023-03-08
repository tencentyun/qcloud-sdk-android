package com.tencent.cos.xml.ci;

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
import com.tencent.cos.xml.model.ci.BaseDescribeQueuesResponse;
import com.tencent.cos.xml.model.ci.ai.CreateWordsGeneralizeJobRequest;
import com.tencent.cos.xml.model.ci.ai.CreateWordsGeneralizeJobResult;
import com.tencent.cos.xml.model.ci.ai.DescribeAiQueuesRequest;
import com.tencent.cos.xml.model.ci.ai.DescribeAiQueuesResult;
import com.tencent.cos.xml.model.ci.ai.DescribeWordsGeneralizeJobRequest;
import com.tencent.cos.xml.model.ci.ai.DescribeWordsGeneralizeJobResult;
import com.tencent.cos.xml.model.ci.ai.OpenBucketAiRequest;
import com.tencent.cos.xml.model.ci.ai.OpenBucketAiResult;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * <p>
 * Created by jordanqin on 2023/2/21 14:38.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AIWordsGeneralizeTest {
    private static String queueId;
    private static String jobId;

    @Test
    public void stage1_openBucketAi() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        OpenBucketAiRequest request = new OpenBucketAiRequest(TestConst.WORDS_GENERALIZE_BUCKET);
        try {
            OpenBucketAiResult result = ciService.openBucketAi(request);
            Assert.assertNotNull(result.openBucketAiResponse);
            TestUtils.printXML(result.openBucketAiResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage1_openBucketAiAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        final TestLocker locker = new TestLocker();
        OpenBucketAiRequest request = new OpenBucketAiRequest(TestConst.WORDS_GENERALIZE_BUCKET);
        ciService.openBucketAiAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                OpenBucketAiResult result = (OpenBucketAiResult) cosResult;
                Assert.assertNotNull(result.openBucketAiResponse);
                TestUtils.printXML(result.openBucketAiResponse);
                locker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                locker.release();
            }
        });
        locker.lock();
    }

    @Test
    public void stage2_describeAiQueues() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        DescribeAiQueuesRequest request = new DescribeAiQueuesRequest(TestConst.WORDS_GENERALIZE_BUCKET);
        request.setPageNumber(1);
        request.setPageSize(10);
        request.setState("Active");
        try {
            DescribeAiQueuesResult result = ciService.describeAiQueues(request);
            Assert.assertNotNull(result.describeAiQueuesResponse);
            for (BaseDescribeQueuesResponse.Queue queue : result.describeAiQueuesResponse.queueList){
                if(queue.state.equals("Active")){
                    AIWordsGeneralizeTest.queueId = queue.queueId;
                }
            }
            TestUtils.printXML(result.describeAiQueuesResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage2_describeAiQueuesAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        final TestLocker locker = new TestLocker();
        DescribeAiQueuesRequest request = new DescribeAiQueuesRequest(TestConst.WORDS_GENERALIZE_BUCKET);
        request.setQueueIds(AIWordsGeneralizeTest.queueId+",ashjdaosdhjiasodj12312"+",ashjdaosdasdashjiasodj12312");
        ciService.describeAiQueuesAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                DescribeAiQueuesResult result = (DescribeAiQueuesResult) cosResult;
                Assert.assertNotNull(result.describeAiQueuesResponse);
                TestUtils.printXML(result.describeAiQueuesResponse);
                locker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                locker.release();
            }
        });
        locker.lock();
    }

    @Test
    public void stage3_createWordsGeneralizeJobs() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        CreateWordsGeneralizeJobRequest request = new CreateWordsGeneralizeJobRequest(TestConst.WORDS_GENERALIZE_BUCKET);
        request.setInputObject(TestConst.WORDS_GENERALIZE_OBJECT);
        request.setQueueId(AIWordsGeneralizeTest.queueId);
        request.setCallBack("https://cloud.tencent.com/document/product/436/47595");
        request.setCallBackFormat("XML");
        request.setUserData("asdasjdiop12389712893123");
        request.setJobLevel(2);
        request.setNerMethod("DL");
        request.setSegMethod("MIX");
        try {
            CreateWordsGeneralizeJobResult result = ciService.createWordsGeneralizeJob(request);
            Assert.assertNotNull(result.createWordsGeneralizeJobResponse);
            AIWordsGeneralizeTest.jobId = result.createWordsGeneralizeJobResponse.jobsDetail.jobId;
            TestUtils.printXML(result.createWordsGeneralizeJobResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage3_createWordsGeneralizeJobsAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        final TestLocker locker = new TestLocker();
        CreateWordsGeneralizeJobRequest request = new CreateWordsGeneralizeJobRequest(TestConst.WORDS_GENERALIZE_BUCKET);
        request.setInputObject(TestConst.WORDS_GENERALIZE_OBJECT);
        request.setQueueId(AIWordsGeneralizeTest.queueId);
        request.setCallBack("no");
        request.setCallBackFormat("XML");
        request.setUserData("asdasjdiop12389712893123");
        request.setJobLevel(1);
        request.setNerMethod("DL");
        request.setSegMethod("MIX");
        ciService.createWordsGeneralizeJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                CreateWordsGeneralizeJobResult result = (CreateWordsGeneralizeJobResult) cosResult;
                Assert.assertNotNull(result.createWordsGeneralizeJobResponse);
                AIWordsGeneralizeTest.jobId = result.createWordsGeneralizeJobResponse.jobsDetail.jobId;
                TestUtils.printXML(result.createWordsGeneralizeJobResponse);
                locker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                locker.release();
            }
        });
        locker.lock();
    }

    @Test
    public void stage4_describeWordsGeneralizeJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        DescribeWordsGeneralizeJobRequest request = new DescribeWordsGeneralizeJobRequest(
                TestConst.WORDS_GENERALIZE_BUCKET, AIWordsGeneralizeTest.jobId);
        try {
            DescribeWordsGeneralizeJobResult result = ciService.describeWordsGeneralizeJob(request);
            Assert.assertNotNull(result.describeWordsGeneralizeJobResponse);
            TestUtils.printXML(result.describeWordsGeneralizeJobResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage4_describeWordsGeneralizeJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        final TestLocker locker = new TestLocker();
        DescribeWordsGeneralizeJobRequest request = new DescribeWordsGeneralizeJobRequest(
                TestConst.WORDS_GENERALIZE_BUCKET, AIWordsGeneralizeTest.jobId);
        ciService.describeWordsGeneralizeJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                DescribeWordsGeneralizeJobResult result = (DescribeWordsGeneralizeJobResult) cosResult;
                Assert.assertNotNull(result.describeWordsGeneralizeJobResponse);
                TestUtils.printXML(result.describeWordsGeneralizeJobResponse);
                locker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                locker.release();
            }
        });
        locker.lock();
    }
}
