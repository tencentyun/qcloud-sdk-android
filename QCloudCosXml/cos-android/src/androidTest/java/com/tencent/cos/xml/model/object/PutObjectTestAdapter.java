package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;
import com.tencent.cos.xml.model.tag.ACLAccount;

import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class PutObjectTestAdapter{
    private static void setRequest(PutObjectRequest request){
        request.setCacheControl("no-cache");
        request.setContentDisposition("inline");
        request.setContentEncodeing("deflate");
        request.setExpires("0");
        request.setXCOSMeta("x-cos-meta-aaa","aaa");
        request.setStroageClass(COSStorageClass.STANDARD);

        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
        request.setXCOSGrantRead(aclAccount);
        request.setXCOSGrantWrite(null);
        request.setXCOSReadWrite(aclAccount);
        request.setTrafficLimit(838860800);
    }

    public static class PutObjectSrcPathTestAdapter extends RequestTestAdapter<PutObjectRequest, PutObjectResult> {
        @Override
        protected PutObjectRequest newRequestInstance() {
            PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                    TestUtils.smallFilePath());
            request.setNeedMD5(false);
            request.setSrcPath(TestUtils.smallFilePath());
            Assert.assertNotNull(request.getSrcPath());
            setRequest(request);
            request.setCOSServerSideEncryption();
            Assert.assertTrue(request.getFileLength()>0);
            request.setXCOSACL(COSACL.DEFAULT);
            return request;
        }

        @Override
        protected PutObjectResult exeSync(PutObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.putObject(request);
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
            request.setData("this is object".getBytes());
            Assert.assertNotNull(request.getData());
            setRequest(request);
            Assert.assertTrue(request.getFileLength()>0);
            request.setXCOSACL(COSACL.DEFAULT.getAcl());
            return request;
        }

        @Override
        protected PutObjectResult exeSync(PutObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.putObject(request);
        }

        @Override
        protected void exeAsync(PutObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.putObjectAsync(request, resultListener);
        }
    }

    public static class PutObjectSbTestAdapter extends RequestTestAdapter<PutObjectRequest, PutObjectResult> {
        @Override
        protected PutObjectRequest newRequestInstance() {
            PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                    new StringBuilder("this is object"));
            request.setStrData("this is object");
            Assert.assertNotNull(request.getStrData());
            setRequest(request);
            return request;
        }

        @Override
        protected PutObjectResult exeSync(PutObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.putObject(request);
        }

        @Override
        protected void exeAsync(PutObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.putObjectAsync(request, resultListener);
        }
    }

    public static class PutObjectInputStreamTestAdapter extends RequestTestAdapter<PutObjectRequest, PutObjectResult> {
        @Override
        protected PutObjectRequest newRequestInstance() {
            InputStream inputStream = new ByteArrayInputStream("this is object".getBytes());
            PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                    inputStream);
            request.setInputStream(inputStream);
            Assert.assertNotNull(request.getInputStream());
            setRequest(request);
            return request;
        }

        @Override
        protected PutObjectResult exeSync(PutObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.putObject(request);
        }

        @Override
        protected void exeAsync(PutObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.putObjectAsync(request, resultListener);
        }
    }

    public static class PutObjectUrlTestAdapter extends RequestTestAdapter<PutObjectRequest, PutObjectResult> {
        @Override
        protected PutObjectRequest newRequestInstance() {
            URL url = null;
            try {
                url = new URL("https://qcloudimg.tencent-cloud.cn/raw/a2685874ce596a0630c0d012676d5cfd.png");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, url);

            request.setUrl(url);
            Assert.assertNotNull(request.getUrl());
            setRequest(request);
            return request;
        }

        @Override
        protected PutObjectResult exeSync(PutObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.putObject(request);
        }

        @Override
        protected void exeAsync(PutObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.putObjectAsync(request, resultListener);
        }
    }
}