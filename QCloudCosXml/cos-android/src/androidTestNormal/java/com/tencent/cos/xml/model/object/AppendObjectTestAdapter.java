package com.tencent.cos.xml.model.object;

import android.util.Log;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.ACLAccount;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AppendObjectTestAdapter {
    public static class AppendObjectByteTestAdapter extends NormalRequestTestAdapter<AppendObjectRequest, AppendObjectResult> {
        @Override
        protected AppendObjectRequest newRequestInstance() {
            AppendObjectRequest request = new AppendObjectRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_APPEND_OBJECT_PATH, "this is append object".getBytes(), 0);
            request.setCacheControl("no-cache");
            request.setContentDisposition("inline");
            request.setExpires("0");
            request.setContentEncodeing("deflate");
            ACLAccount aclAccount = new ACLAccount();
            aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
            request.setXCOSGrantRead(aclAccount);
            request.setXCOSGrantWrite(null);
            request.setXCOSReadWrite(aclAccount);
            request.setProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    Log.d(TestConst.UT_TAG, complete + "/" + target);
                }
            });
            return request;
        }

        @Override
        protected AppendObjectResult exeSync(AppendObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.appendObject(request);
        }

        @Override
        protected void exeAsync(AppendObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.appendObjectAsync(request, resultListener);
        }
    }

    public static class AppendObjectSrcPathTestAdapter extends NormalRequestTestAdapter<AppendObjectRequest, AppendObjectResult> {
        @Override
        protected AppendObjectRequest newRequestInstance() {
            AppendObjectRequest request = new AppendObjectRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_APPEND_OBJECT_PATH, TestUtils.smallFilePath(), 21);

            return request;
        }

        @Override
        protected AppendObjectResult exeSync(AppendObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.appendObject(request);
        }

        @Override
        protected void exeAsync(AppendObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.appendObjectAsync(request, resultListener);
        }
    }

    public static class AppendObjectStreamTestAdapter extends NormalRequestTestAdapter<AppendObjectRequest, AppendObjectResult> {
        @Override
        protected AppendObjectRequest newRequestInstance() {
            InputStream inputStream = new ByteArrayInputStream("this is object".getBytes());
            AppendObjectRequest request = new AppendObjectRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_APPEND_OBJECT_PATH, inputStream, 1048597);

            return request;
        }

        @Override
        protected AppendObjectResult exeSync(AppendObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.appendObject(request);
        }

        @Override
        protected void exeAsync(AppendObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.appendObjectAsync(request, resultListener);
        }
    }

    public static class DeleteAppendObjectTestAdapter extends NormalRequestTestAdapter<DeleteObjectRequest, DeleteObjectResult> {
        @Override
        protected DeleteObjectRequest newRequestInstance() {
            return new DeleteObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_APPEND_OBJECT_PATH);
        }

        @Override
        protected DeleteObjectResult exeSync(DeleteObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.deleteObject(request);
        }

        @Override
        protected void exeAsync(DeleteObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.deleteObjectAsync(request, resultListener);
        }
    }
}