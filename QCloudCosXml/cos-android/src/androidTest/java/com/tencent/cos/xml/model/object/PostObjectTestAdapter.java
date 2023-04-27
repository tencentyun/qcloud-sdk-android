package com.tencent.cos.xml.model.object;

import android.util.Log;

import androidx.collection.ArraySet;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;
import com.tencent.cos.xml.utils.DateUtils;

import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

public class PostObjectTestAdapter{
    public static class PostObjectByteTestAdapter extends RequestTestAdapter<PostObjectRequest, PostObjectResult> {
        @Override
        protected PostObjectRequest newRequestInstance() {
            PostObjectRequest request = new PostObjectRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_POST_OBJECT_PATH, "this is post object".getBytes());
            request.setRange(0, -1);
            request.setCacheControl("no-cache");
            request.setContentDisposition("inline");
            request.setContentEncoding("deflate");
            request.setExpires("0");
            request.setContentType("application");
            request.setStroageClass(COSStorageClass.STANDARD);
            request.setCosStorageClass(COSStorageClass.STANDARD.getStorageClass());
            request.setAcl("default");
            request.setHeader("hk1", "hv1");
            request.setCustomerHeader("ck1","cv1");
            request.setTrafficLimit(838860800);
            request.setSuccessActionRedirect(null);
            request.setSuccessActionStatus(200);
            PostObjectRequest.Policy policy = new PostObjectRequest.Policy();
            policy.setExpiration(System.currentTimeMillis());
            policy.setExpiration(DateUtils.getFormatTime("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", System.currentTimeMillis()));
            try {
                policy.addConditions("testk", "testv", true);
            } catch (CosXmlClientException e) {
                e.printStackTrace();
            }
            policy.addContentConditions(0, -1);
            Assert.assertNotNull(policy.content());
            request.setPolicy(policy);
            try {
                Assert.assertNotNull(request.testFormParameters());
            } catch (CosXmlClientException e) {
                e.printStackTrace();
            }
            request.setProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    Log.d(TestConst.UT_TAG, complete + "/" + target);
                }
            });
            Set<String> parameters = new ArraySet<>();
            parameters.add("parameters1");
            parameters.add("parameters2");
            Set<String> headers = new ArraySet<>();
            headers.add("headers1");
            headers.add("headers2");
            request.setSignParamsAndHeaders(parameters, headers);
            return request;
        }

        @Override
        protected PostObjectResult exeSync(PostObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.postObject(request);
        }

        @Override
        protected void exeAsync(PostObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.postObjectAsync(request, resultListener);
        }
    }

    public static class PostObjectSrcPathTestAdapter extends RequestTestAdapter<PostObjectRequest, PostObjectResult> {
        @Override
        protected PostObjectRequest newRequestInstance() {
            PostObjectRequest request = new PostObjectRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_POST_OBJECT_PATH, TestUtils.smallFilePath());

            return request;
        }

        @Override
        protected PostObjectResult exeSync(PostObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.postObject(request);
        }

        @Override
        protected void exeAsync(PostObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.postObjectAsync(request, resultListener);
        }
    }

    public static class PostObjectStreamTestAdapter extends RequestTestAdapter<PostObjectRequest, PostObjectResult> {
        @Override
        protected PostObjectRequest newRequestInstance() {
            InputStream inputStream = new ByteArrayInputStream("this is object".getBytes());
            PostObjectRequest request = new PostObjectRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_POST_OBJECT_PATH, inputStream);

            return request;
        }

        @Override
        protected PostObjectResult exeSync(PostObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.postObject(request);
        }

        @Override
        protected void exeAsync(PostObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.postObjectAsync(request, resultListener);
        }
    }
}