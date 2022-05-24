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

package com.tencent.cos.xml.doc;


//@RunWith(AndroidJUnit4.class)
//public class COSUploadSnippet {

//    // newQuicTransferService
////    @Test public void testQuicUpload() {
////        QuicConfig quicConfig = new QuicConfig();
////        quicConfig.setCustomProtocol(true);
////        quicConfig.setRaceType(QuicConfig.RACE_TYPE_ONLY_QUIC);
////        quicConfig.setTotalTimeoutSec(1000);
////        QuicProxy.setTnetConfig(quicConfig);
////        testUploadFile(ServiceFactory.INSTANCE.newQuicTransferService(), "mobile-ut-bj-1253960454",
////                TestUtils.localPath("shanshui.jpg"), TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
////    }
//
//    // 测试分块上传
//    @Test public void testMultipartUpload() {
//        testUploadFile(ServiceFactory.INSTANCE.newDefaultTransferService(), "",
//                TestUtils.localPath("shanshui.png"), TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
//    }
//
//    // 测试简单上传
//    @Test public void testSimpleUpload() {
//        testUploadFile(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET,
//                TestUtils.localPath("shanshui.jpg"), TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
//    }
//
//    // 测试暂停和恢复
//    @Test public void testPauseAndResume() {
//        testPauseAndResume(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET,
//                TestUtils.bigFilePath(), TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
//    }
//
//    // 测试加密分块上传
//    @Test public void testCesMultipartUpload() {
//        testUploadFile(ServiceFactory.INSTANCE.newCesTransferService(), TestConst.PERSIST_BUCKET,
//                TestUtils.bigFilePath(), TestConst.PERSIST_BUCKET_CSE_BIG_OBJECT_PATH);
//    }
//
//    // 测试加密简单上传
//    @Test public void testCesSimpleUpload() {
//        testUploadFile(ServiceFactory.INSTANCE.newCesTransferService(), TestConst.PERSIST_BUCKET,
//                TestUtils.smallFilePath(), TestConst.PERSIST_BUCKET_CSE_SMALL_OBJECT_PATH);
//    }
//
//
//    @Test public void testCsePauseAndResume() {
//        testPauseAndResume(ServiceFactory.INSTANCE.newCesTransferService(), TestConst.PERSIST_BUCKET,
//                TestUtils.bigFilePath(), TestConst.PERSIST_BUCKET_CSE_BIG_OBJECT_PATH);
//    }
//
//    private void testUploadFile() {
//
//        // 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
//        TransferConfig transferConfig = new TransferConfig.Builder().build();
//// 初始化 TransferManager
//        TransferService transferService = new TransferService(cosXmlService,
//                transferConfig);
//
//        String bucket = "examplebucket-1250000000"; //存储桶，格式：BucketName-APPID
//        String cosPath = "exampleobject"; //对象在存储桶中的位置标识符，即称对象键
//        String srcPath = new File(context.getCacheDir(), "exampleobject")
//                .toString(); //本地文件的绝对路径
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
//                cosPath, srcPath);
//        final COSUploadTask uploadTask = transferService.upload(putObjectRequest);
//        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
//            @Override
//            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                QCloudLogger.i("QCloudTest", "upload success");
//            }
//
//            @Override
//            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
//            }
//        });
//
//        uploadTask.setTransferStateListener(new TransferStateListener() {
//            @Override
//            public void onStateChanged(TransferState state) {
//                QCloudLogger.i("QCloudTest", "transfer state is " + state);
//            }
//        });
//
//        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
//            @Override
//            public void onProgress(long complete, long target) {
//                QCloudLogger.i("QCloudTest", "transfer progress is " + complete + "/" + target);
//            }
//        });
//    }
//
//    private void testPauseAndResume(Context context) {
//
//        // 高级下载接口支持断点续传，所以会在下载前先发起 HEAD 请求获取文件信息。
//// 如果您使用的是临时密钥或者使用子账号访问，请确保权限列表中包含 HeadObject 的权限。
//
//// 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
//        TransferConfig transferConfig = new TransferConfig.Builder().build();
//// 初始化 TransferManager
//        TransferService transferService = new TransferService(cosXmlService,
//                transferConfig);
//        String bucket = "examplebucket-1250000000"; //存储桶，格式：BucketName-APPID
//        String cosPath = "exampleobject"; //对象在存储桶中的位置标识符，即称对象键
//        String srcPath = context.getCacheDir().getAbsolutePath();
//        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket,
//                cosPath,
//                srcPath);
//        COSDownloadTask downloadTask = transferService.download(getObjectRequest);
//
//        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
//            @Override
//            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//
//            }
//
//            @Override
//            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
//                if (clientException != null) {
//                    clientException.printStackTrace();
//                }
//                if (serviceException != null) {
//                    serviceException.printStackTrace();
//                }
//            }
//        });
//        downloadTask.setTransferStateListener(new TransferStateListener() {
//            @Override
//            public void onStateChanged(TransferState state) {
//                QCloudLogger.i("QCloudTest", "transfer state is " + state);
//            }
//        });
//
//        downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
//            @Override
//            public void onProgress(long complete, long target) {
//                QCloudLogger.i("QCloudTest", "transfer progress is " + complete + "/" + target);
//            }
//        });
//    }


//}
