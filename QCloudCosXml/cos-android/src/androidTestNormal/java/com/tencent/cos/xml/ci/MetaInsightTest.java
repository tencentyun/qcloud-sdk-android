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
import com.tencent.cos.xml.model.ci.metainsight.CreateDataset;
import com.tencent.cos.xml.model.ci.metainsight.CreateDatasetBinding;
import com.tencent.cos.xml.model.ci.metainsight.CreateDatasetBindingRequest;
import com.tencent.cos.xml.model.ci.metainsight.CreateDatasetBindingResult;
import com.tencent.cos.xml.model.ci.metainsight.CreateDatasetRequest;
import com.tencent.cos.xml.model.ci.metainsight.CreateDatasetResult;
import com.tencent.cos.xml.model.ci.metainsight.CreateFileMetaIndex;
import com.tencent.cos.xml.model.ci.metainsight.CreateFileMetaIndexRequest;
import com.tencent.cos.xml.model.ci.metainsight.CreateFileMetaIndexResult;
import com.tencent.cos.xml.model.ci.metainsight.DatasetFaceSearch;
import com.tencent.cos.xml.model.ci.metainsight.DatasetFaceSearchRequest;
import com.tencent.cos.xml.model.ci.metainsight.DatasetFaceSearchResult;
import com.tencent.cos.xml.model.ci.metainsight.DatasetSimpleQuery;
import com.tencent.cos.xml.model.ci.metainsight.DatasetSimpleQueryRequest;
import com.tencent.cos.xml.model.ci.metainsight.DatasetSimpleQueryResult;
import com.tencent.cos.xml.model.ci.metainsight.DeleteDataset;
import com.tencent.cos.xml.model.ci.metainsight.DeleteDatasetBinding;
import com.tencent.cos.xml.model.ci.metainsight.DeleteDatasetBindingRequest;
import com.tencent.cos.xml.model.ci.metainsight.DeleteDatasetBindingResult;
import com.tencent.cos.xml.model.ci.metainsight.DeleteDatasetRequest;
import com.tencent.cos.xml.model.ci.metainsight.DeleteDatasetResult;
import com.tencent.cos.xml.model.ci.metainsight.DeleteFileMetaIndex;
import com.tencent.cos.xml.model.ci.metainsight.DeleteFileMetaIndexRequest;
import com.tencent.cos.xml.model.ci.metainsight.DeleteFileMetaIndexResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetBindingRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetBindingResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetBindingsRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetBindingsResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetsRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetsResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeFileMetaIndexRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeFileMetaIndexResult;
import com.tencent.cos.xml.model.ci.metainsight.SearchImage;
import com.tencent.cos.xml.model.ci.metainsight.SearchImageRequest;
import com.tencent.cos.xml.model.ci.metainsight.SearchImageResult;
import com.tencent.cos.xml.model.ci.metainsight.UpdateDataset;
import com.tencent.cos.xml.model.ci.metainsight.UpdateDatasetRequest;
import com.tencent.cos.xml.model.ci.metainsight.UpdateDatasetResult;
import com.tencent.cos.xml.model.ci.metainsight.UpdateFileMetaIndex;
import com.tencent.cos.xml.model.ci.metainsight.UpdateFileMetaIndexRequest;
import com.tencent.cos.xml.model.ci.metainsight.UpdateFileMetaIndexResult;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>
 * Created by jordanqin on 2024/6/5 19:15.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MetaInsightTest {

    String datasetName = "datasetnametest1";
    @Test
    public void stage1_createDataset() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        CreateDatasetRequest request = new CreateDatasetRequest(TestConst.CI_BUCKET_APPID);
        CreateDataset createDataset = new CreateDataset();// 创建数据集请求体
        createDataset.datasetName = datasetName;
        createDataset.description = "datasetnametest0";
        createDataset.templateId = "Official:FaceSearch";
        request.setCreateDataset(createDataset);// 设置请求

        try {
            CreateDatasetResult result = ciService.createDataset(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            if("dataset already created".equalsIgnoreCase(e.getErrorMessage())){
                datasetName = "dataset"+ System.currentTimeMillis();
                CreateDatasetRequest request1 = new CreateDatasetRequest(TestConst.CI_BUCKET_APPID);
                CreateDataset createDataset1 = new CreateDataset();// 创建数据集请求体
                createDataset1.datasetName = datasetName;
                createDataset1.description = "datasetnametest0";
                createDataset1.templateId = "Official:FaceSearch";
                request1.setCreateDataset(createDataset1);// 设置请求
                try {
                    CreateDatasetResult result = ciService.createDataset(request1);
                    Assert.assertNotNull(result.response);
                    TestUtils.printJson(result.response);
                } catch (CosXmlClientException e1) {
                    Assert.fail(TestUtils.getCosExceptionMessage(e));
                } catch (CosXmlServiceException e1) {
                    Assert.fail(TestUtils.getCosExceptionMessage(e));
                }
            } else {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        }
    }

    @Test
    public void stage1_createDatasetAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        CreateDatasetRequest request = new CreateDatasetRequest(TestConst.CI_BUCKET_APPID);
        CreateDataset createDataset = new CreateDataset();// 创建数据集请求体
        createDataset.datasetName = "datasetnametest6";
        createDataset.description = "datasetnametest0";
        createDataset.templateId = "Official:ImageSearch";
        request.setCreateDataset(createDataset);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.createDatasetAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                CreateDatasetResult result = (CreateDatasetResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stage2_describeDataset() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DescribeDatasetRequest request = new DescribeDatasetRequest(TestConst.CI_BUCKET_APPID);
        request.datasetname = datasetName;// 设置数据集名称，同一个账户下唯一。
        request.statistics = false;// 设置是否需要实时统计数据集中文件相关信息。有效值： false：不统计，返回的文件的总大小、数量信息可能不正确也可能都为0。 true：需要统计，返回数据集中当前的文件的总大小、数量信息。 默认值为false。

        try {
            DescribeDatasetResult result = ciService.describeDataset(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage2_describeDatasetAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DescribeDatasetRequest request = new DescribeDatasetRequest(TestConst.CI_BUCKET_APPID);
        request.datasetname = "datasetnametest6";// 设置数据集名称，同一个账户下唯一。
        request.statistics = false;// 设置是否需要实时统计数据集中文件相关信息。有效值： false：不统计，返回的文件的总大小、数量信息可能不正确也可能都为0。 true：需要统计，返回数据集中当前的文件的总大小、数量信息。 默认值为false。

        final TestLocker testLocker = new TestLocker();
        ciService.describeDatasetAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DescribeDatasetResult result = (DescribeDatasetResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stage3_updateDataset() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        UpdateDatasetRequest request = new UpdateDatasetRequest(TestConst.CI_BUCKET_APPID);
        UpdateDataset updateDataset = new UpdateDataset();// 更新数据集请求体
        updateDataset.datasetName = datasetName;
        updateDataset.description = "datasetnametest1";
        updateDataset.templateId = "Official:FaceSearch";
        request.setUpdateDataset(updateDataset);// 设置请求

        try {
            UpdateDatasetResult result = ciService.updateDataset(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage3_updateDatasetAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        UpdateDatasetRequest request = new UpdateDatasetRequest(TestConst.CI_BUCKET_APPID);
        UpdateDataset updateDataset = new UpdateDataset();// 更新数据集请求体
        updateDataset.datasetName = "datasetnametest6";
        updateDataset.description = "datasetnametest1";
        updateDataset.templateId = "Official:ImageSearch";
        request.setUpdateDataset(updateDataset);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.updateDatasetAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                UpdateDatasetResult result = (UpdateDatasetResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stage4_describeDatasets() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DescribeDatasetsRequest request = new DescribeDatasetsRequest(TestConst.CI_BUCKET_APPID);
        request.maxresults = 100;
        request.prefix = "dataset";// 设置数据集名称前缀。

        try {
            DescribeDatasetsResult result = ciService.describeDatasets(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage4_describeDatasetsAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DescribeDatasetsRequest request = new DescribeDatasetsRequest(TestConst.CI_BUCKET_APPID);
        request.maxresults = 100;
        request.prefix = "dataset";// 设置数据集名称前缀。

        final TestLocker testLocker = new TestLocker();
        ciService.describeDatasetsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DescribeDatasetsResult result = (DescribeDatasetsResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stage5_createDatasetBinding() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        CreateDatasetBindingRequest request = new CreateDatasetBindingRequest(TestConst.CI_BUCKET_APPID);
        CreateDatasetBinding createDatasetBinding = new CreateDatasetBinding();// 绑定存储桶与数据集请求体
        createDatasetBinding.datasetName = datasetName;
        createDatasetBinding.uRI = "cos://"+TestConst.CI_BUCKET;
        request.setCreateDatasetBinding(createDatasetBinding);// 设置请求

        try {
            CreateDatasetBindingResult result = ciService.createDatasetBinding(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage5_createDatasetBindingAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        CreateDatasetBindingRequest request = new CreateDatasetBindingRequest(TestConst.CI_BUCKET_APPID);
        CreateDatasetBinding createDatasetBinding = new CreateDatasetBinding();// 绑定存储桶与数据集请求体
        createDatasetBinding.datasetName = "datasetnametest6";
        createDatasetBinding.uRI = "cos://"+TestConst.CI_BUCKET;
        request.setCreateDatasetBinding(createDatasetBinding);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.createDatasetBindingAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                CreateDatasetBindingResult result = (CreateDatasetBindingResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stage6_describeDatasetBinding() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DescribeDatasetBindingRequest request = new DescribeDatasetBindingRequest(TestConst.CI_BUCKET_APPID);
        request.datasetname = datasetName;// 设置数据集名称，同一个账户下唯一。
        request.uri = "cos://"+TestConst.CI_BUCKET;// 设置资源标识字段，表示需要与数据集绑定的资源，当前仅支持COS存储桶，字段规则：cos://，其中BucketName表示COS存储桶名称，例如（需要进行urlencode）：cos%3A%2F%2Fexample-125000

        try {
            DescribeDatasetBindingResult result = ciService.describeDatasetBinding(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage6_describeDatasetBindingAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DescribeDatasetBindingRequest request = new DescribeDatasetBindingRequest(TestConst.CI_BUCKET_APPID);
        request.datasetname = "datasetnametest6";// 设置数据集名称，同一个账户下唯一。
        request.uri = "cos://"+TestConst.CI_BUCKET;// 设置资源标识字段，表示需要与数据集绑定的资源，当前仅支持COS存储桶，字段规则：cos://，其中BucketName表示COS存储桶名称，例如（需要进行urlencode）：cos%3A%2F%2Fexample-125000

        final TestLocker testLocker = new TestLocker();
        ciService.describeDatasetBindingAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DescribeDatasetBindingResult result = (DescribeDatasetBindingResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stage7_describeDatasetBindings() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DescribeDatasetBindingsRequest request = new DescribeDatasetBindingsRequest(TestConst.CI_BUCKET_APPID);
        request.maxresults = 100;
        request.datasetname = datasetName;// 设置数据集名称，同一个账户下唯一。

        try {
            DescribeDatasetBindingsResult result = ciService.describeDatasetBindings(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage7_describeDatasetBindingsAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DescribeDatasetBindingsRequest request = new DescribeDatasetBindingsRequest(TestConst.CI_BUCKET_APPID);
        request.maxresults = 100;
        request.datasetname = "datasetnametest6";// 设置数据集名称，同一个账户下唯一。

        final TestLocker testLocker = new TestLocker();
        ciService.describeDatasetBindingsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DescribeDatasetBindingsResult result = (DescribeDatasetBindingsResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stage8_createFileMetaIndex() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        CreateFileMetaIndexRequest request = new CreateFileMetaIndexRequest(TestConst.CI_BUCKET_APPID);
        CreateFileMetaIndex createFileMetaIndex = new CreateFileMetaIndex();// 创建元数据索引请求体
        createFileMetaIndex.datasetName = datasetName;
        createFileMetaIndex.callback = "https://github.com/jordanqin";
        createFileMetaIndex.file = new CreateFileMetaIndex.File();
        createFileMetaIndex.file.uRI = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";
        createFileMetaIndex.file.customId = "123456";
        createFileMetaIndex.file.mediaType = "image";
        createFileMetaIndex.file.contentType = "image/jpeg";
        createFileMetaIndex.file.maxFaceNum = 10;
        createFileMetaIndex.file.customLabels = new HashMap<>();
        createFileMetaIndex.file.customLabels.put("age", "18");
        createFileMetaIndex.file.customLabels.put("level", "6");
        createFileMetaIndex.file.persons = new ArrayList<>();
        CreateFileMetaIndex.Persons person1 = new CreateFileMetaIndex.Persons();
        person1.personId = "11111111";
        CreateFileMetaIndex.Persons person2 = new CreateFileMetaIndex.Persons();
        person2.personId = "22222222";
        createFileMetaIndex.file.persons.add(person1);
        createFileMetaIndex.file.persons.add(person2);
        request.setCreateFileMetaIndex(createFileMetaIndex);// 设置请求

        try {
            CreateFileMetaIndexResult result = ciService.createFileMetaIndex(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage8_createFileMetaIndexAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        CreateFileMetaIndexRequest request = new CreateFileMetaIndexRequest(TestConst.CI_BUCKET_APPID);
        CreateFileMetaIndex createFileMetaIndex = new CreateFileMetaIndex();// 创建元数据索引请求体
        createFileMetaIndex.datasetName = "datasetnametest6";
        createFileMetaIndex.callback = "https://github.com/jordanqin";
        createFileMetaIndex.file = new CreateFileMetaIndex.File();
        createFileMetaIndex.file.uRI = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";
        createFileMetaIndex.file.customId = "123456";
        createFileMetaIndex.file.mediaType = "image";
        createFileMetaIndex.file.contentType = "image/jpeg";
        createFileMetaIndex.file.maxFaceNum = 10;
        createFileMetaIndex.file.customLabels = new HashMap<>();
        createFileMetaIndex.file.customLabels.put("age", "18");
        createFileMetaIndex.file.customLabels.put("level", "6");
        createFileMetaIndex.file.persons = new ArrayList<>();
        CreateFileMetaIndex.Persons person1 = new CreateFileMetaIndex.Persons();
        person1.personId = "11111111";
        CreateFileMetaIndex.Persons person2 = new CreateFileMetaIndex.Persons();
        person2.personId = "22222222";
        createFileMetaIndex.file.persons.add(person1);
        createFileMetaIndex.file.persons.add(person2);
        request.setCreateFileMetaIndex(createFileMetaIndex);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.createFileMetaIndexAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                CreateFileMetaIndexResult result = (CreateFileMetaIndexResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stage9_updateFileMetaIndex() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        UpdateFileMetaIndexRequest request = new UpdateFileMetaIndexRequest(TestConst.CI_BUCKET_APPID);
        UpdateFileMetaIndex updateFileMetaIndex = new UpdateFileMetaIndex();// 更新元数据索引请求体
        updateFileMetaIndex.datasetName = datasetName;
        updateFileMetaIndex.callback = "https://github.com/jordanqin";
        updateFileMetaIndex.file = new UpdateFileMetaIndex.File();
        updateFileMetaIndex.file.uRI = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";
        updateFileMetaIndex.file.customId = "123456";
        updateFileMetaIndex.file.mediaType = "image";
        updateFileMetaIndex.file.contentType = "image/jpeg";
        updateFileMetaIndex.file.maxFaceNum = 10;
        updateFileMetaIndex.file.customLabels = new HashMap<>();
        updateFileMetaIndex.file.customLabels.put("age1", "18");
        updateFileMetaIndex.file.customLabels.put("level1", "6");
        updateFileMetaIndex.file.persons = new ArrayList<>();
        UpdateFileMetaIndex.Persons person1 = new UpdateFileMetaIndex.Persons();
        person1.personId = "11111111";
        UpdateFileMetaIndex.Persons person2 = new UpdateFileMetaIndex.Persons();
        person2.personId = "22222222";
        updateFileMetaIndex.file.persons.add(person1);
        updateFileMetaIndex.file.persons.add(person2);
        request.setUpdateFileMetaIndex(updateFileMetaIndex);// 设置请求

        try {
            UpdateFileMetaIndexResult result = ciService.updateFileMetaIndex(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage9_updateFileMetaIndexAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        UpdateFileMetaIndexRequest request = new UpdateFileMetaIndexRequest(TestConst.CI_BUCKET_APPID);
        UpdateFileMetaIndex updateFileMetaIndex = new UpdateFileMetaIndex();// 更新元数据索引请求体
        updateFileMetaIndex.datasetName = "datasetnametest6";
        updateFileMetaIndex.callback = "https://github.com/jordanqin";
        updateFileMetaIndex.file = new UpdateFileMetaIndex.File();
        updateFileMetaIndex.file.uRI = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";
        updateFileMetaIndex.file.customId = "123456";
        updateFileMetaIndex.file.mediaType = "image";
        updateFileMetaIndex.file.contentType = "image/jpeg";
        updateFileMetaIndex.file.maxFaceNum = 10;
        updateFileMetaIndex.file.customLabels = new HashMap<>();
        updateFileMetaIndex.file.customLabels.put("age1", "18");
        updateFileMetaIndex.file.customLabels.put("level1", "6");
        updateFileMetaIndex.file.persons = new ArrayList<>();
        UpdateFileMetaIndex.Persons person1 = new UpdateFileMetaIndex.Persons();
        person1.personId = "11111111";
        UpdateFileMetaIndex.Persons person2 = new UpdateFileMetaIndex.Persons();
        person2.personId = "22222222";
        updateFileMetaIndex.file.persons.add(person1);
        updateFileMetaIndex.file.persons.add(person2);
        request.setUpdateFileMetaIndex(updateFileMetaIndex);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.updateFileMetaIndexAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                UpdateFileMetaIndexResult result = (UpdateFileMetaIndexResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stagea_describeFileMetaIndex() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DescribeFileMetaIndexRequest request = new DescribeFileMetaIndexRequest(TestConst.CI_BUCKET_APPID);
        request.datasetname = datasetName;// 设置数据集名称，同一个账户下唯一。
        request.uri = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";// 设置资源标识字段，表示需要建立索引的文件地址，当前仅支持COS上的文件，字段规则：cos:///，其中BucketName表示COS存储桶名称，ObjectKey表示文件完整路径，例如：cos://exampleTestConst.CI_BUCKET_APPID-1250000000/test1/img.jpg。 注意： 1、仅支持本账号内的COS文件 2、不支持HTTP开头的地址 3、需UrlEncode

        try {
            DescribeFileMetaIndexResult result = ciService.describeFileMetaIndex(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stagea_describeFileMetaIndexAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DescribeFileMetaIndexRequest request = new DescribeFileMetaIndexRequest(TestConst.CI_BUCKET_APPID);
        request.datasetname = "datasetnametest6";// 设置数据集名称，同一个账户下唯一。
        request.uri = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";// 设置资源标识字段，表示需要建立索引的文件地址，当前仅支持COS上的文件，字段规则：cos:///，其中BucketName表示COS存储桶名称，ObjectKey表示文件完整路径，例如：cos://exampleTestConst.CI_BUCKET_APPID-1250000000/test1/img.jpg。 注意： 1、仅支持本账号内的COS文件 2、不支持HTTP开头的地址 3、需UrlEncode

        final TestLocker testLocker = new TestLocker();
        ciService.describeFileMetaIndexAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DescribeFileMetaIndexResult result = (DescribeFileMetaIndexResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stageb_deleteFileMetaIndex() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DeleteFileMetaIndexRequest request = new DeleteFileMetaIndexRequest(TestConst.CI_BUCKET_APPID);
        DeleteFileMetaIndex deleteFileMetaIndex = new DeleteFileMetaIndex();// 删除元数据索引请求体
        deleteFileMetaIndex.datasetName = datasetName;// 设置数据集名称，同一个账户下唯一。
        deleteFileMetaIndex.uRI = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";// 设置资源标识字段，表示需要建立索引的文件地址，当前仅支持COS上的文件，字段规则：cos:///，其中BucketName表示COS存储桶名称，ObjectKey表示文件完整路径，例如：cos://exampleTestConst.CI_BUCKET_APPID-1250000000/test1/img.jpg。 注意： 1、仅支持本账号内的COS文件 2、不支持HTTP开头的地址 3、需UrlEncode
        request.setDeleteFileMetaIndex(deleteFileMetaIndex);// 设置请求

        try {
            DeleteFileMetaIndexResult result = ciService.deleteFileMetaIndex(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stageb_deleteFileMetaIndexAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DeleteFileMetaIndexRequest request = new DeleteFileMetaIndexRequest(TestConst.CI_BUCKET_APPID);
        DeleteFileMetaIndex deleteFileMetaIndex = new DeleteFileMetaIndex();// 删除元数据索引请求体
        deleteFileMetaIndex.datasetName = "datasetnametest6";// 设置数据集名称，同一个账户下唯一。
        deleteFileMetaIndex.uRI = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";// 设置资源标识字段，表示需要建立索引的文件地址，当前仅支持COS上的文件，字段规则：cos:///，其中BucketName表示COS存储桶名称，ObjectKey表示文件完整路径，例如：cos://exampleTestConst.CI_BUCKET_APPID-1250000000/test1/img.jpg。 注意： 1、仅支持本账号内的COS文件 2、不支持HTTP开头的地址 3、需UrlEncode
        request.setDeleteFileMetaIndex(deleteFileMetaIndex);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.deleteFileMetaIndexAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DeleteFileMetaIndexResult result = (DeleteFileMetaIndexResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stagec_datasetSimpleQuery() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DatasetSimpleQueryRequest request = new DatasetSimpleQueryRequest(TestConst.CI_BUCKET_APPID);
        DatasetSimpleQuery datasetSimpleQuery = new DatasetSimpleQuery();// 简单查询请求体
        datasetSimpleQuery.datasetName = datasetName;
        datasetSimpleQuery.sort = "CustomId";
        datasetSimpleQuery.order = "desc";
        datasetSimpleQuery.maxResults = 100;
        datasetSimpleQuery.query = new DatasetSimpleQuery.Query();
        datasetSimpleQuery.query.operation = "and";
        datasetSimpleQuery.query.subQueries = new ArrayList<>();
        DatasetSimpleQuery.SubQueries subQuerie1 = new DatasetSimpleQuery.SubQueries();
        subQuerie1.field = "ContentType";
        subQuerie1.value = "image/jpeg";
        subQuerie1.operation = "eq";
        DatasetSimpleQuery.SubQueries subQuerie2 = new DatasetSimpleQuery.SubQueries();
        subQuerie2.field = "Size";
        subQuerie2.value = "10";
        subQuerie2.operation = "gt";
        datasetSimpleQuery.query.subQueries.add(subQuerie1);
        datasetSimpleQuery.query.subQueries.add(subQuerie2);
        request.setDatasetSimpleQuery(datasetSimpleQuery);// 设置请求

        try {
            DatasetSimpleQueryResult result = ciService.datasetSimpleQuery(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stagec_datasetSimpleQueryAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DatasetSimpleQueryRequest request = new DatasetSimpleQueryRequest(TestConst.CI_BUCKET_APPID);
        DatasetSimpleQuery datasetSimpleQuery = new DatasetSimpleQuery();// 简单查询请求体
        datasetSimpleQuery.datasetName = "datasetnametest6";
        datasetSimpleQuery.sort = "CustomId";
        datasetSimpleQuery.order = "desc";
        datasetSimpleQuery.maxResults = 100;
        datasetSimpleQuery.query = new DatasetSimpleQuery.Query();
        datasetSimpleQuery.query.operation = "and";
        datasetSimpleQuery.query.subQueries = new ArrayList<>();
        DatasetSimpleQuery.SubQueries subQuerie1 = new DatasetSimpleQuery.SubQueries();
        subQuerie1.field = "ContentType";
        subQuerie1.value = "image/jpeg";
        subQuerie1.operation = "eq";
        DatasetSimpleQuery.SubQueries subQuerie2 = new DatasetSimpleQuery.SubQueries();
        subQuerie2.field = "Size";
        subQuerie2.value = "10";
        subQuerie2.operation = "gt";
        datasetSimpleQuery.query.subQueries.add(subQuerie1);
        datasetSimpleQuery.query.subQueries.add(subQuerie2);
        request.setDatasetSimpleQuery(datasetSimpleQuery);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.datasetSimpleQueryAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DatasetSimpleQueryResult result = (DatasetSimpleQueryResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void staged_datasetFaceSearch() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DatasetFaceSearchRequest request = new DatasetFaceSearchRequest(TestConst.CI_BUCKET_APPID);
        DatasetFaceSearch datasetFaceSearch = new DatasetFaceSearch();// 人脸搜索请求体
        datasetFaceSearch.datasetName = datasetName;
        datasetFaceSearch.uRI = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";
        datasetFaceSearch.maxFaceNum = 2;
        datasetFaceSearch.limit = 10;
        datasetFaceSearch.matchThreshold = 80;
        request.setDatasetFaceSearch(datasetFaceSearch);// 设置请求

        try {
            DatasetFaceSearchResult result = ciService.datasetFaceSearch(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            if(e.getErrorMessage().contains("Unable to recognize face")){
                Assert.assertTrue(true);
            } else {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        }
    }

    @Test
    public void staged_datasetFaceSearchAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DatasetFaceSearchRequest request = new DatasetFaceSearchRequest(TestConst.CI_BUCKET_APPID);
        DatasetFaceSearch datasetFaceSearch = new DatasetFaceSearch();// 人脸搜索请求体
        datasetFaceSearch.datasetName = datasetName;
        datasetFaceSearch.uRI = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";
        datasetFaceSearch.maxFaceNum = 2;
        datasetFaceSearch.limit = 10;
        datasetFaceSearch.matchThreshold = 80;
        request.setDatasetFaceSearch(datasetFaceSearch);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.datasetFaceSearchAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DatasetFaceSearchResult result = (DatasetFaceSearchResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(serviceException != null && serviceException.getErrorMessage().contains("Unable to recognize face")){
                    Assert.assertTrue(true);
                } else {
                    Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                }
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test
    public void stagee_searchImage() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        SearchImageRequest request = new SearchImageRequest(TestConst.CI_BUCKET_APPID);
        SearchImage searchImage = new SearchImage();// 图像检索请求体
        searchImage.datasetName = "datasetnametest6";
        searchImage.uRI = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";
        searchImage.mode = "pic";
        searchImage.limit = 10;
        searchImage.matchThreshold = 80;
        request.setSearchImage(searchImage);// 设置请求

        try {
            SearchImageResult result = ciService.searchImage(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stagee_searchImageAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        SearchImageRequest request = new SearchImageRequest(TestConst.CI_BUCKET_APPID);
        SearchImage searchImage = new SearchImage();// 图像检索请求体
        searchImage.datasetName = "datasetnametest6";
        searchImage.uRI = "cos://"+TestConst.CI_BUCKET+"/media/test.jpg";
        searchImage.mode = "pic";
        searchImage.limit = 10;
        searchImage.matchThreshold = 80;
        request.setSearchImage(searchImage);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.searchImageAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                SearchImageResult result = (SearchImageResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stagef_deleteDatasetBinding() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DeleteDatasetBindingRequest request = new DeleteDatasetBindingRequest(TestConst.CI_BUCKET_APPID);
        DeleteDatasetBinding deleteDatasetBinding = new DeleteDatasetBinding();// 解绑存储桶与数据集请求体
        deleteDatasetBinding.datasetName = datasetName;
        deleteDatasetBinding.uRI = "cos://"+TestConst.CI_BUCKET;
        request.setDeleteDatasetBinding(deleteDatasetBinding);// 设置请求

        try {
            DeleteDatasetBindingResult result = ciService.deleteDatasetBinding(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stagef_deleteDatasetBindingAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DeleteDatasetBindingRequest request = new DeleteDatasetBindingRequest(TestConst.CI_BUCKET_APPID);
        DeleteDatasetBinding deleteDatasetBinding = new DeleteDatasetBinding();// 解绑存储桶与数据集请求体
        deleteDatasetBinding.datasetName = "datasetnametest6";
        deleteDatasetBinding.uRI = "cos://"+TestConst.CI_BUCKET;
        request.setDeleteDatasetBinding(deleteDatasetBinding);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.deleteDatasetBindingAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DeleteDatasetBindingResult result = (DeleteDatasetBindingResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
    public void stageg_deleteDataset() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DeleteDatasetRequest request = new DeleteDatasetRequest(TestConst.CI_BUCKET_APPID);
        DeleteDataset deleteDataset = new DeleteDataset();// 删除数据集请求体
        deleteDataset.datasetName = datasetName;
        request.setDeleteDataset(deleteDataset);// 设置请求

        try {
            DeleteDatasetResult result = ciService.deleteDataset(request);
            Assert.assertNotNull(result.response);
            TestUtils.printJson(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stageg_deleteDatasetAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newMetaInsightService();
        DeleteDatasetRequest request = new DeleteDatasetRequest(TestConst.CI_BUCKET_APPID);
        DeleteDataset deleteDataset = new DeleteDataset();// 删除数据集请求体
        deleteDataset.datasetName = "datasetnametest6";
        request.setDeleteDataset(deleteDataset);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.deleteDatasetAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DeleteDatasetResult result = (DeleteDatasetResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printJson(result.response);

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
