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
import com.tencent.cos.xml.model.ci.asr.CreateSpeechJobsRequest;
import com.tencent.cos.xml.model.ci.asr.CreateSpeechJobsResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechBucketsRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechBucketsResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobsRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobsResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechQueuesRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechQueuesResult;
import com.tencent.cos.xml.model.tag.CallBackMqConfig;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * <p>
 * Created by jordanqin on 2023/2/21 14:37.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpeechTest {
    private static String queueId;
    private static String jobId;
    private static String flashJobId;

    @Test
    public void stage1_describeSpeechBuckets() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechBucketsRequest request = new DescribeSpeechBucketsRequest();
        request.setPageNumber(1);
        request.setPageSize(10);
        request.setRegions("All");
        try {
            DescribeSpeechBucketsResult result = ciService.describeSpeechBuckets(request);
            Assert.assertNotNull(result.describeSpeechBucketsResponse);
            TestUtils.printXML(result.describeSpeechBucketsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage1_describeSpeechBucketsAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditServiceBySessionCredentials();
        final TestLocker locker = new TestLocker();
        DescribeSpeechBucketsRequest request = new DescribeSpeechBucketsRequest();
        request.setPageNumber(1);
        request.setPageSize(10);
        request.setRegions("All");
        ciService.describeSpeechBucketsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                DescribeSpeechBucketsResult result = (DescribeSpeechBucketsResult) cosResult;
                Assert.assertNotNull(result.describeSpeechBucketsResponse);
                TestUtils.printXML(result.describeSpeechBucketsResponse);
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
    public void stage2_describeSpeechQueues() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechQueuesRequest request = new DescribeSpeechQueuesRequest(TestConst.ASR_BUCKET);
        request.setPageNumber(1);
        request.setPageSize(10);
        request.setState("Active");
        try {
            DescribeSpeechQueuesResult result = ciService.describeSpeechQueues(request);
            Assert.assertNotNull(result.describeSpeechQueuesResponse);
            for (BaseDescribeQueuesResponse.Queue queue : result.describeSpeechQueuesResponse.queueList){
                if(queue.state.equals("Active")){
                    SpeechTest.queueId = queue.queueId;
                }
            }
            TestUtils.printXML(result.describeSpeechQueuesResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage2_describeSpeechQueuesAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        final TestLocker locker = new TestLocker();
        DescribeSpeechQueuesRequest request = new DescribeSpeechQueuesRequest(TestConst.ASR_BUCKET, TestConst.PERSIST_BUCKET_REGION);
        request.setQueueIds(SpeechTest.queueId+",ashjdaosdhjiasodj12312"+",ashjdaosdasdashjiasodj12312");
        ciService.describeSpeechQueuesAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                DescribeSpeechQueuesResult result = (DescribeSpeechQueuesResult) cosResult;
                Assert.assertNotNull(result.describeSpeechQueuesResponse);
                TestUtils.printXML(result.describeSpeechQueuesResponse);
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
    public void stage3_createSpeechJobs() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        CreateSpeechJobsRequest request = new CreateSpeechJobsRequest(TestConst.ASR_BUCKET);
        request.setInputObject(TestConst.ASR_OBJECT_LONG);
        request.setQueueId(SpeechTest.queueId);
        request.setOutput(TestConst.ASR_BUCKET_REGION, TestConst.ASR_BUCKET, TestConst.ASR_OBJECT_OUTPUT);
        request.setEngineModelType("8k_zh");
        request.setChannelNum(1);
        request.setResTextFormat(1);
        request.setFilterDirty(0);
        request.setFilterModal(0);
        request.setConvertNumMode(1);
        request.setSpeakerDiarization(0);
        request.setSpeakerNumber(0);
        request.setFilterPunc(0);
        request.setOutputFileType("txt");
        request.setFirstChannelOnly(1);
        request.setWordInfo(0);
        request.setUserData("userdata");
        request.setJobLevel(0);
        request.setCallBack("no");
        request.setCallBackFormat("XML");
        request.setCallBackType("Url");
        request.setCallBackMqConfig(new CallBackMqConfig());

        try {
            CreateSpeechJobsResult result = ciService.createSpeechJobs(request);
            Assert.assertNotNull(result.createSpeechJobsResponse);
            SpeechTest.jobId = result.createSpeechJobsResponse.jobsDetail.jobId;
            TestUtils.printXML(result.createSpeechJobsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage3_createSpeechJobsAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        final TestLocker locker = new TestLocker();
        CreateSpeechJobsRequest request = new CreateSpeechJobsRequest(TestConst.ASR_BUCKET);
        request.setInputObject(TestConst.ASR_OBJECT_GS_2C);
        request.setQueueId(SpeechTest.queueId);
        request.setOutput(TestConst.ASR_BUCKET_REGION, TestConst.ASR_BUCKET, TestConst.ASR_OBJECT_OUTPUT);
        request.setEngineModelType("8k_zh");
        request.setChannelNum(2);
        request.setResTextFormat(0);
        ciService.createSpeechJobsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                CreateSpeechJobsResult result = (CreateSpeechJobsResult) cosResult;
                Assert.assertNotNull(result.createSpeechJobsResponse);
                SpeechTest.jobId = result.createSpeechJobsResponse.jobsDetail.jobId;
                TestUtils.printXML(result.createSpeechJobsResponse);
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
    public void stage4_createFlashSpeechJobs() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        CreateSpeechJobsRequest request = new CreateSpeechJobsRequest(TestConst.ASR_BUCKET);
        request.setInputObject(TestConst.ASR_OBJECT_GS_2C);
        request.setQueueId(SpeechTest.queueId);
        request.setOutput(TestConst.ASR_BUCKET_REGION, TestConst.ASR_BUCKET, TestConst.ASR_OBJECT_OUTPUT);
        request.setEngineModelType("8k_zh");
        request.setChannelNum(1);
        request.setResTextFormat(1);
        request.setFlashAsr(true);
        request.setFormat("m4a");
        try {
            CreateSpeechJobsResult result = ciService.createSpeechJobs(request);
            Assert.assertNotNull(result.createSpeechJobsResponse);
            SpeechTest.flashJobId = result.createSpeechJobsResponse.jobsDetail.jobId;
            TestUtils.printXML(result.createSpeechJobsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage5_describeSpeechJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechJobRequest request = new DescribeSpeechJobRequest(TestConst.ASR_BUCKET, SpeechTest.jobId);
        try {
            DescribeSpeechJobResult result = ciService.describeSpeechJob(request);
            Assert.assertNotNull(result.describeSpeechJobResponse);
            TestUtils.printXML(result.describeSpeechJobResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage5_describeSpeechJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        final TestLocker locker = new TestLocker();
        DescribeSpeechJobRequest request = new DescribeSpeechJobRequest(TestConst.ASR_BUCKET, SpeechTest.jobId);
        ciService.describeSpeechJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                DescribeSpeechJobResult result = (DescribeSpeechJobResult) cosResult;
                Assert.assertNotNull(result.describeSpeechJobResponse);
                TestUtils.printXML(result.describeSpeechJobResponse);
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
    public void stage6_describeFlashSpeechJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechJobRequest request = new DescribeSpeechJobRequest(TestConst.ASR_BUCKET, SpeechTest.flashJobId);
        try {
            DescribeSpeechJobResult result = ciService.describeSpeechJob(request);
            Assert.assertNotNull(result.describeSpeechJobResponse);
            TestUtils.printXML(result.describeSpeechJobResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage7_describeSpeechJobs() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechJobsRequest request = new DescribeSpeechJobsRequest(TestConst.ASR_BUCKET);
        request.setQueueId(SpeechTest.queueId);
        request.setOrderByTime("Desc");
        request.setNextToken(null);
        request.setSize(50);
        request.setStates("All");
//        request.setStartCreationTime("%Y-%m-%dT%H:%m:%S%z");
//        request.setEndCreationTime("%Y-%m-%dT%H:%m:%S%z");
        try {
            DescribeSpeechJobsResult result = ciService.describeSpeechJobs(request);
            Assert.assertNotNull(result.describeSpeechJobsResponse);
            TestUtils.printXML(result.describeSpeechJobsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage7_describeSpeechJobsAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        final TestLocker locker = new TestLocker();
        DescribeSpeechJobsRequest request = new DescribeSpeechJobsRequest(TestConst.ASR_BUCKET, TestConst.PERSIST_BUCKET_REGION);
        request.setQueueId(SpeechTest.queueId);
        ciService.describeSpeechJobsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                DescribeSpeechJobsResult result = (DescribeSpeechJobsResult) cosResult;
                Assert.assertNotNull(result.describeSpeechJobsResponse);
                TestUtils.printXML(result.describeSpeechJobsResponse);
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
