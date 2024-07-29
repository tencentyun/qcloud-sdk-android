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

package com.tencent.cos.xml.transfer;

import static com.tencent.cos.xml.core.TestUtils.smallFilePath;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.MyOnSignatureListener;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectBytesRequest;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.InetAddress;

@RunWith(AndroidJUnit4.class)
public class DownloadTest {

    @After public void clearDownloadFiles() {
        //TestUtils.clearDir(new File(TestUtils.localParentPath()));
    }

    @Test public void testSmallDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
       // getObjectRequest.setRange(100, 200);

        try {
            getObjectRequest.setRequestHeaders("test", "value", false);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }

        /// getObjectRequest.addNoSignHeader("Range");
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        // downloadTask.startTimeoutTimer(100);
        downloadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {
                InetAddress socketAddress = httpTaskMetrics.getConnectAddress();
                if (socketAddress != null) {
                    QCloudLogger.i(TestConst.UT_TAG, "connect ip is " + socketAddress.getHostAddress());
                }
            }
        });

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                File file = new File(getObjectRequest.getDownloadPath());
                TestUtils.print("download file size:"+file.length());
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        downloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i("QCloudTest", "transfer state is " + state);
            }
        });



        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testFailSmallDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+"notexist",
                TestUtils.localParentPath());
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
                Assert.assertTrue(true);
            }
        });

        testLocker.lock();

        COSXMLDownloadTask downloadTask1 = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath()+"notexist");
        downloadTask1.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
                Assert.assertTrue(true);
            }
        });
        testLocker.lock();
    }

    @Test public void testBigDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localParentPath());
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    /**
     * 测试续传
     *
     * 下载 2s 后点击暂停，并记录当前的下载进度，等待 1s 后重新下载，进度必须大于之前记录的进度
     */
    @Test public void testContinueDownload() {
        TestUtils.sleep(10000);
        String localFileName = TestUtils.extractName(TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH);

        TestUtils.removeLocalFile(TestUtils.localPath(localFileName));

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                TestUtils.localParentPath(), localFileName);

        TestUtils.sleep(4000);
        downloadTask.pause();
        TestUtils.sleep(4000);
//        final StringBuilder errorMessage = new StringBuilder();

        final COSXMLDownloadTask continueTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                TestUtils.localParentPath(), localFileName);

        final TestLocker testLocker = new TestLocker();

        final long currentProgress = new File(TestUtils.localParentPath(), localFileName).length();
//        continueTask.setCosXmlProgressListener(new CosXmlProgressListener() {
//            @Override
//            public void onProgress(long complete, long target) {
//
//                if (complete < currentProgress) {
//                    errorMessage.append("continue complete is " + complete + ", but current progress is " + currentProgress);
//                    testLocker.release();
//                }
//            }
//        });

        continueTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
//        TestUtils.assertErrorMessageNull(errorMessage);
        TestUtils.sleep(10000);
        TestUtils.assertCOSXMLTaskSuccess(continueTask);
    }

//    @Test public void testContinueDownloadExisted() {
//        String localFileName = TestUtils.big60mFilePath();
//        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
//        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
//                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
//                localFileName);
//
//        TestUtils.sleep(4000);
//        downloadTask.pause();
//        TestUtils.sleep(4000);
//
//        TestUtils.big60mFilePath();
//        final COSXMLDownloadTask continueTask = transferManager.download(TestUtils.getContext(),
//                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
//                localFileName);
//
//        final TestLocker testLocker = new TestLocker();
//
//        continueTask.setCosXmlResultListener(new CosXmlResultListener() {
//            @Override
//            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                testLocker.release();
//            }
//
//            @Override
//            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
//                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
//                testLocker.release();
//            }
//        });
//
//        testLocker.lock();
//        TestUtils.assertCOSXMLTaskSuccess(continueTask);
//    }

    @Test public void testAnonymousDownload() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newAnonymousTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_CDN_BIG_OBJECT_PATH,
                TestUtils.localParentPath());
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);
        downloadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {
                InetAddress socketAddress = httpTaskMetrics.getConnectAddress();
                if (socketAddress != null) {
                    QCloudLogger.i(TestConst.UT_TAG, "connect ip is " + socketAddress.getHostAddress());
                }
            }
        });

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        downloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i("QCloudTest", "transfer state is " + state);
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testSmallDownloadSignature() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath(), "test", new MyOnSignatureListener());

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testSmallDownloadRange() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
         getObjectRequest.setRange(0, 10);
        getObjectRequest.addNoSignHeader("Range");
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testSmallDownloadRangeFaild() {
        // 能通过head  但是get服务端非200-300
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
//        getObjectRequest.setIfModifiedSince("Wed, 21 Oct 2009 07:28:00 GMT");
//        getObjectRequest.setIfUnmodifiedSince("Wed, 21 Oct 2052 07:28:00 GMT");
        getObjectRequest.setIfMatch("none_match_etag");
//        getObjectRequest.setIfNONEMatch("none_match_etag");
        getObjectRequest.addNoSignHeader("Range");
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                Assert.assertTrue(true);
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test public void testDownloadClientError() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                "aaaaaaa");
        final TestLocker testLocker = new TestLocker();
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(clientException != null){
                    Assert.assertTrue(true);
                } else {
                    Assert.fail();
                }
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test public void testSmallDownloadToUri() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                Uri.fromFile(new File(smallFilePath())));
        Assert.assertNotNull(getObjectRequest.getFileContentUri());
        try {
            cosXmlSimpleService.getObject(getObjectRequest);
        } catch (CosXmlClientException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (CosXmlServiceException e) {
            Assert.fail(e.getMessage());
            return;
        }
        Assert.assertTrue(true);
    }

    @Test public void testSmallDownloadToFile() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
        try {
            cosXmlSimpleService.getObject(getObjectRequest);
        } catch (CosXmlClientException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (CosXmlServiceException e) {
            Assert.fail(e.getMessage());
            return;
        }
        Assert.assertTrue(true);
    }

    @Test public void testSmallDownloadToFileByVerifySSLNotEnabled() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .setVerifySSLEnable(false)
                .builder();
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newService(cosXmlServiceConfig);

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
        try {
            cosXmlSimpleService.getObject(getObjectRequest);
        } catch (CosXmlClientException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (CosXmlServiceException e) {
            Assert.fail(e.getMessage());
            return;
        }
        Assert.assertTrue(true);
    }

    @Test public void testDownloadToFileTimeout() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setConnectionTimeout(3000)
                .setSocketTimeout(3000)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newService(cosXmlServiceConfig);

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_1G_OBJECT_PATH,
                TestUtils.localParentPath());
        try {
            cosXmlSimpleService.getObject(getObjectRequest);
        } catch (CosXmlClientException e) {
            if(e.getMessage().contains("timeout")){
                Assert.assertTrue(true);
            } else {
                Assert.fail(e.getMessage());
            }
            return;
        } catch (CosXmlServiceException e) {
            Assert.fail(e.getMessage());
            return;
        }
        Assert.assertTrue(true);
    }

    @Test public void testDownloadPath() {
        testDownloadBtPath("/", true, true);
        testDownloadBtPath("///////", true, true);
        testDownloadBtPath("/abc/../", true, true);
        testDownloadBtPath("/./", true, true);
        testDownloadBtPath("///abc/.//def//../../", true, true);
        testDownloadBtPath("/././///abc/.//def//../../", true, true);
        testDownloadBtPath(TestConst.PERSIST_BUCKET_ROOT_FILE_PATH, false, true);
        testDownloadBtPath(TestConst.PERSIST_BUCKET_PIC_PATH, false, true);
        testDownloadBtPath("/"+TestConst.PERSIST_BUCKET_PIC_PATH, false, true);
        testDownloadBtPath("do_not_remove/", false, true);
        testDownloadBtPath("/do_not_remove/", false, true);

        testDownloadBtPath("/", true, false);
        testDownloadBtPath("///////", true, false);
        testDownloadBtPath("/abc/../", true, false);
        testDownloadBtPath("/./", true, false);
        testDownloadBtPath("///abc/.//def//../../", true, false);
        testDownloadBtPath("/././///abc/.//def//../../", true, false);
        testDownloadBtPath(TestConst.PERSIST_BUCKET_ROOT_FILE_PATH, false, false);
        testDownloadBtPath(TestConst.PERSIST_BUCKET_PIC_PATH, false, false);
        testDownloadBtPath("/"+TestConst.PERSIST_BUCKET_PIC_PATH, false, false);
        testDownloadBtPath("do_not_remove/", false, false);
        testDownloadBtPath("/do_not_remove/", false, false);

        // 为了覆盖getCanonicalPath()的IOException
        testDownloadBtPath("\u0000", true, true);

        Assert.assertTrue(true);
    }

    private void testDownloadBtPath(String cosPath, boolean isFail, boolean isObjectKeySimplifyCheck){
        try {
            TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

            GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                    cosPath,
                    TestUtils.localParentPath(), "test");
            getObjectRequest.setObjectKeySimplifyCheck(isObjectKeySimplifyCheck);

            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    getObjectRequest);
            final TestLocker testLocker = new TestLocker();
            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    testLocker.release();
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    String error = TestUtils.getCosExceptionMessage(clientException, serviceException);
                    TestUtils.printError(error);
                    if(serviceException != null){
                        // 如果开启校验且应该校验失败 就不应该走到服务端，其他情况不关注后端错误
                        if(isObjectKeySimplifyCheck && isFail){
                            Assert.fail(error);
                        }
                    }
                    if(clientException != null){
                        if(isFail) {
                            if (!"The key in the getobject is illegal".equals(clientException.getMessage())) {
                                Assert.fail(clientException.getMessage());
                            } else {
                                if(!isObjectKeySimplifyCheck){
                                    Assert.fail(clientException.getMessage());
                                }
                            }
                        } else {
                            Assert.fail(clientException.getMessage());
                        }
                    }
                    testLocker.release();
                }
            });
            testLocker.lock();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test public void testGetObjectPath() {
        getObjectByPath("/", true, true);
        getObjectByPath("///////", true, true);
        getObjectByPath("/abc/../", true, true);
        getObjectByPath("/./", true, true);
        getObjectByPath("///abc/.//def//../../", true, true);
        getObjectByPath("/././///abc/.//def//../../", true, true);
        getObjectByPath(TestConst.PERSIST_BUCKET_ROOT_FILE_PATH, false, true);
        getObjectByPath(TestConst.PERSIST_BUCKET_PIC_PATH, false, true);
        getObjectByPath("/"+TestConst.PERSIST_BUCKET_PIC_PATH, false, true);
        getObjectByPath("do_not_remove/", false, true);
        getObjectByPath("/do_not_remove/", false, true);

        getObjectByPath("/", true, false);
        getObjectByPath("///////", true, false);
        getObjectByPath("/abc/../", true, false);
        getObjectByPath("/./", true, false);
        getObjectByPath("///abc/.//def//../../", true, false);
        getObjectByPath("/././///abc/.//def//../../", true, false);
        getObjectByPath(TestConst.PERSIST_BUCKET_ROOT_FILE_PATH, false, false);
        getObjectByPath(TestConst.PERSIST_BUCKET_PIC_PATH, false, false);
        getObjectByPath("/"+TestConst.PERSIST_BUCKET_PIC_PATH, false, false);
        getObjectByPath("do_not_remove/", false, false);
        getObjectByPath("/do_not_remove/", false, false);

        Assert.assertTrue(true);
    }
    private void getObjectByPath(String cosPath, boolean isFail, boolean isObjectKeySimplifyCheck) {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                cosPath,
                TestUtils.localParentPath(), "test");
        getObjectRequest.setObjectKeySimplifyCheck(isObjectKeySimplifyCheck);
        try {
            cosXmlSimpleService.getObject(getObjectRequest);
        } catch (CosXmlClientException e) {
            TestUtils.printError(e.getMessage());
            if(isFail) {
                if (!"The key in the getobject is illegal".equals(e.getMessage())) {
                    Assert.fail(e.getMessage());
                } else {
                    if(!isObjectKeySimplifyCheck){
                        Assert.fail(e.getMessage());
                    }
                }
            } else {
                Assert.fail(e.getMessage());
            }
        } catch (CosXmlServiceException e) {
            // 如果开启校验且应该校验失败 就不应该走到服务端，其他情况不关注后端错误
            if(isObjectKeySimplifyCheck && isFail){
                Assert.fail(e.getMessage());
            }
        }
    }

    @Test public void testGetObjectBytesPath() {
        getObjectBytesByPath("/", true, true);
        getObjectBytesByPath("///////", true, true);
        getObjectBytesByPath("/abc/../", true, true);
        getObjectBytesByPath("/./", true, true);
        getObjectBytesByPath("///abc/.//def//../../", true, true);
        getObjectBytesByPath("/././///abc/.//def//../../", true, true);
        getObjectBytesByPath(TestConst.PERSIST_BUCKET_ROOT_FILE_PATH, false, true);
        getObjectBytesByPath(TestConst.PERSIST_BUCKET_PIC_PATH, false, true);
        getObjectBytesByPath("/"+TestConst.PERSIST_BUCKET_PIC_PATH, false, true);
        getObjectBytesByPath("do_not_remove/", false, true);
        getObjectBytesByPath("/do_not_remove/", false, true);

        getObjectBytesByPath("/", true, false);
        getObjectBytesByPath("///////", true, false);
        getObjectBytesByPath("/abc/../", true, false);
        getObjectBytesByPath("/./", true, false);
        getObjectBytesByPath("///abc/.//def//../../", true, false);
        getObjectBytesByPath("/././///abc/.//def//../../", true, false);
        getObjectBytesByPath(TestConst.PERSIST_BUCKET_ROOT_FILE_PATH, false, false);
        getObjectBytesByPath(TestConst.PERSIST_BUCKET_PIC_PATH, false, false);
        getObjectBytesByPath("/"+TestConst.PERSIST_BUCKET_PIC_PATH, false, false);
        getObjectBytesByPath("do_not_remove/", false, false);
        getObjectBytesByPath("/do_not_remove/", false, false);

        Assert.assertTrue(true);
    }
    private void getObjectBytesByPath(String cosPath, boolean isFail, boolean isObjectKeySimplifyCheck) {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        GetObjectBytesRequest getObjectBytesRequest = new GetObjectBytesRequest(TestConst.PERSIST_BUCKET, cosPath);
        getObjectBytesRequest.setObjectKeySimplifyCheck(isObjectKeySimplifyCheck);
        try {
            cosXmlSimpleService.getObject(getObjectBytesRequest);
        } catch (CosXmlClientException e) {
            TestUtils.printError(e.getMessage());
            if(isFail) {
                if (!"The key in the getobject is illegal".equals(e.getMessage())) {
                    Assert.fail(e.getMessage());
                } else {
                    if(!isObjectKeySimplifyCheck){
                        Assert.fail(e.getMessage());
                    }
                }
            } else {
                Assert.fail(e.getMessage());
            }
        } catch (CosXmlServiceException e) {
            // 如果开启校验且应该校验失败 就不应该走到服务端，其他情况不关注后端错误
            if(isObjectKeySimplifyCheck && isFail){
                Assert.fail(e.getMessage());
            }
        }
    }
}
