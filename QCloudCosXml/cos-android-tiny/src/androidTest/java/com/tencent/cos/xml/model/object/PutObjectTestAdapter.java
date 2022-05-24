package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class PutObjectTestAdapter{
    public static class PutObjectSrcPathTestAdapter extends RequestTestAdapter<PutObjectRequest, PutObjectResult> {
        @Override
        protected PutObjectRequest newRequestInstance() {
            PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                    TestUtils.smallFilePath());
            return request;
        }

        @Override
        protected void exeAsync(PutObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.putObjectAsync(request, resultListener);
        }
    }

    public static class PutObjectByteTestAdapter extends RequestTestAdapter<PutObjectRequest, PutObjectResult> {
        @Override
        protected PutObjectRequest newRequestInstance() {
            PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                    "this is object".getBytes());
            return request;
        }

        @Override
        protected void exeAsync(PutObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.putObjectAsync(request, resultListener);
        }
    }

//    public static class PutObjectSbTestAdapter extends RequestTestAdapter<PutObjectRequest, PutObjectResult> {
//        @Override
//        protected PutObjectRequest newRequestInstance() {
//            PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
//                    new StringBuilder("this is object"));
//            request.setStrData("this is object");
//            Assert.assertNotNull(request.getStrData());
//            return request;
//        }
//
//        @Override
//        protected void exeAsync(PutObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
//            cosXmlService.putObjectAsync(request, resultListener);
//        }
//    }

    public static class PutObjectInputStreamTestAdapter extends RequestTestAdapter<PutObjectRequest, PutObjectResult> {
        @Override
        protected PutObjectRequest newRequestInstance() {
            InputStream inputStream = new ByteArrayInputStream("this is object".getBytes());
            PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                    inputStream);
            return request;
        }

        @Override
        protected void exeAsync(PutObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.putObjectAsync(request, resultListener);
        }
    }
}