package com.tencent.cos.xml;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.MetaDataDirective;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.bucket.GetBucketRequest;
import com.tencent.cos.xml.model.bucket.GetBucketResult;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.AbortMultiUploadResult;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.CopyObjectResult;
import com.tencent.cos.xml.model.object.DeleteMultiObjectRequest;
import com.tencent.cos.xml.model.object.DeleteMultiObjectResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.DeleteObjectResult;
import com.tencent.cos.xml.model.object.GetObjectACLRequest;
import com.tencent.cos.xml.model.object.GetObjectACLResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.ListPartsResult;
import com.tencent.cos.xml.model.object.OptionObjectRequest;
import com.tencent.cos.xml.model.object.OptionObjectResult;
import com.tencent.cos.xml.model.object.PutObjectACLRequest;
import com.tencent.cos.xml.model.object.PutObjectACLResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.object.UploadPartCopyRequest;
import com.tencent.cos.xml.model.object.UploadPartCopyResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.model.tag.ListBucket;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tencent.cos.xml.QServer.TAG;



/**
 * Created by bradyxiao on 2018/3/13.
 */

@RunWith(AndroidJUnit4.class)
public class ObjectTest {

    Context appContext;

    String bucket;
    String cosPath;
    String srcPath;

    private HttpTaskMetrics newTaskMetrics() {
        return new HttpTaskMetrics() {
            @Override
            public void onDataReady() {
                super.onDataReady();
                QCloudLogger.i("QCloudHttp", toString());
            }
        };
    }

    public void putObject() throws CosXmlServiceException, CosXmlClientException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
        putObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TAG, complete + "/" + target);
            }
        });

        putObjectRequest.attachMetrics(newTaskMetrics());
        PutObjectResult putObjectResult = QServer.cosXml.putObject(putObjectRequest);
        Log.d(TAG, putObjectResult.printResult());
    }

    public void headObject() throws CosXmlServiceException, CosXmlClientException {
        HeadObjectRequest headObjectRequest = new HeadObjectRequest(bucket, cosPath);
        HeadObjectResult headObjectResult = QServer.cosXml.headObject(headObjectRequest);
        Log.d(TAG, headObjectResult.printResult());
    }

    public void optionObject() throws CosXmlServiceException, CosXmlClientException {
        String origin = " http://cloud.tencent.com";
        String method = "PUT";
        OptionObjectRequest optionObjectRequest = new OptionObjectRequest(bucket, cosPath,origin, method);
        optionObjectRequest.setAccessControlHeaders("Authorization");
        OptionObjectResult optionObjectResult = QServer.cosXml.optionObject(optionObjectRequest);
        Log.d(TAG, optionObjectResult.printResult());
    }

    public void putObjectACL() throws CosXmlServiceException, CosXmlClientException {
        PutObjectACLRequest putObjectACLRequest = new PutObjectACLRequest(bucket, cosPath);
        putObjectACLRequest.setXCOSACL(COSACL.PRIVATE);
        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(QServer.ownUin, "1131975903");
        putObjectACLRequest.setXCOSGrantRead(aclAccount);
        putObjectACLRequest.setXCOSGrantWrite(aclAccount);
        PutObjectACLResult putObjectACLResult = QServer.cosXml.putObjectACL(putObjectACLRequest);
        Log.d(TAG, putObjectACLResult.printResult());
    }

    public void getObjectACL() throws CosXmlServiceException, CosXmlClientException {
        GetObjectACLRequest getObjectACLRequest = new GetObjectACLRequest(bucket, cosPath);
        GetObjectACLResult getObjectACLResult = QServer.cosXml.getObjectACL(getObjectACLRequest);
        Log.d(TAG, getObjectACLResult.printResult());
    }

    public void copyObject() throws CosXmlClientException, CosXmlServiceException {
        String destCosPath = "copy_" + cosPath;
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
              QServer.appid, QServer.persistBucket, QServer.region, cosPath);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucket, destCosPath, copySourceStruct);
        copyObjectRequest.setCopyMetaDataDirective(MetaDataDirective.COPY);
        copyObjectRequest.setRequestHeaders("x-cos-xml", "cos");
        CopyObjectResult copyObjectResult = QServer.cosXml.copyObject(copyObjectRequest);
        Log.d(TAG, copyObjectResult.printResult());
        QServer.deleteCOSObject(appContext, bucket, destCosPath);
    }

    public void partCopyObject() throws CosXmlServiceException, CosXmlClientException {
        String destCosPath = "copy_" + cosPath;
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                QServer.appid, bucket, QServer.region, cosPath);
        InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucket, destCosPath);
        InitMultipartUploadResult initMultipartUploadResult = QServer.cosXml.initMultipartUpload(initMultipartUploadRequest);
        String uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;
        UploadPartCopyRequest uploadPartCopyRequest = new UploadPartCopyRequest(bucket, destCosPath, 1, uploadId,
                copySourceStruct);
        UploadPartCopyResult uploadPartCopyResult = QServer.cosXml.copyObject(uploadPartCopyRequest);
        String eTag = uploadPartCopyResult.copyObject.eTag;
        Map<Integer, String> partNumberAndETag = new HashMap<>();
        partNumberAndETag.put(1, eTag);
        CompleteMultiUploadRequest completeMultiUploadRequest = new CompleteMultiUploadRequest(bucket, destCosPath,
               uploadId, partNumberAndETag);
        CompleteMultiUploadResult completeMultiUploadResult = QServer.cosXml.completeMultiUpload(completeMultiUploadRequest);
        Log.d(TAG, completeMultiUploadResult.printResult());
        QServer.deleteCOSObject(appContext, bucket, destCosPath);
    }

    public void getObject() throws CosXmlServiceException, CosXmlClientException {



        String savePath = QServer.localParentDirectory(appContext).getPath();
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, cosPath, savePath);
        getObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TAG, complete + "/" + target);
            }
        });
        GetObjectResult getObjectResult = QServer.cosXml.getObject(getObjectRequest);
        Log.d(TAG, getObjectResult.printResult());
        QServer.deleteLocalFile(getObjectRequest.getDownloadPath());
    }

    public void deleteObject() throws CosXmlServiceException, CosXmlClientException {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, cosPath);
        DeleteObjectResult deleteObjectResult = QServer.cosXml.deleteObject(deleteObjectRequest);
        Log.d(TAG, deleteObjectResult.printResult());
    }

    public void deleteMultiObject() throws CosXmlServiceException, CosXmlClientException {
        DeleteMultiObjectRequest deleteMultiObjectRequest = new DeleteMultiObjectRequest(bucket, null);
        deleteMultiObjectRequest.setObjectList(cosPath);
        deleteMultiObjectRequest.setQuiet(false);
        DeleteMultiObjectResult deleteMultiObjectResult = QServer.cosXml.deleteMultiObject(deleteMultiObjectRequest);
        Log.d(TAG, deleteMultiObjectResult.printResult());
    }

    public void sliceUploadObject() throws CosXmlServiceException, CosXmlClientException {
        InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucket, cosPath);
        InitMultipartUploadResult initMultipartUploadResult = QServer.cosXml.initMultipartUpload(initMultipartUploadRequest);
        String uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;
        Log.d(TAG, initMultipartUploadResult.printResult());
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucket, cosPath, uploadId);
        ListPartsResult listPartsResult = QServer.cosXml.listParts(listPartsRequest);
        Log.d(TAG, listPartsResult.printResult());

        int partNumber = 1;
        UploadPartRequest uploadPartRequest = new UploadPartRequest(bucket, cosPath, partNumber, srcPath, uploadId);
        uploadPartRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TAG, complete + "/" + target);
            }
        });
        UploadPartResult uploadPartResult = QServer.cosXml.uploadPart(uploadPartRequest);
        String eTag = uploadPartResult.eTag;
        CompleteMultiUploadRequest completeMultiUploadRequest = new CompleteMultiUploadRequest(bucket, cosPath, uploadId, null);
        completeMultiUploadRequest.setPartNumberAndETag(partNumber, eTag);
        CompleteMultiUploadResult completeMultiUploadResult = QServer.cosXml.completeMultiUpload(completeMultiUploadRequest);
        Log.d(TAG, completeMultiUploadResult.printResult());
        QServer.deleteCOSObject(appContext, bucket, cosPath);
    }

    public void abortSliceUploadObject() throws CosXmlServiceException, CosXmlClientException {
        InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucket, cosPath);
        InitMultipartUploadResult initMultipartUploadResult = QServer.cosXml.initMultipartUpload(initMultipartUploadRequest);
        String uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;

        ListPartsRequest listPartsRequest = new ListPartsRequest(bucket, cosPath, uploadId);
        ListPartsResult listPartsResult = QServer.cosXml.listParts(listPartsRequest);
        Log.d(TAG, listPartsResult.printResult());

        int partNumber = 1;
        UploadPartRequest uploadPartRequest = new UploadPartRequest(bucket, cosPath, partNumber, srcPath, uploadId);
        uploadPartRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TAG, complete + "/" + target);
            }
        });
        UploadPartResult uploadPartResult = QServer.cosXml.uploadPart(uploadPartRequest);

        AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(bucket, cosPath, uploadId);
        AbortMultiUploadResult abortMultiUploadResult = QServer.cosXml.abortMultiUpload(abortMultiUploadRequest);
        Log.d(TAG, abortMultiUploadResult.printResult());
    }

    public void deleteAllObjectsOfBucket() throws CosXmlServiceException, CosXmlClientException {
        GetBucketRequest getBucketRequest = new GetBucketRequest(bucket);
        GetBucketResult getBucketResult = QServer.cosXml.getBucket(getBucketRequest);
        Log.d(TAG, getBucketResult.printResult());
        List<ListBucket.Contents> objectList = getBucketResult.listBucket.contentsList;
        if(objectList != null && objectList.size() > 0){
            DeleteMultiObjectRequest deleteMultiObjectRequest = new DeleteMultiObjectRequest(bucket, null);
            deleteMultiObjectRequest.setQuiet(false);
            for(ListBucket.Contents object : objectList){
                deleteMultiObjectRequest.setObjectList(object.key);
            }
            DeleteMultiObjectResult deleteMultiObjectResult = QServer.cosXml.deleteMultiObject(deleteMultiObjectRequest);
            Log.d(TAG, deleteMultiObjectResult.printResult());
        }

    }

    @Test(expected = CosXmlClientException.class)
    public void deleteObjectWithEmptyString() throws Exception {
        appContext = InstrumentationRegistry.getContext();
        bucket = QServer.persistBucket;
        cosPath = "";
        srcPath = QServer.createFile(appContext, 1024 * 1024);
        QServer.init(appContext);
        deleteObject();
    }

    @Test(expected = CosXmlClientException.class)
    public void deleteObjectWithNull() throws Exception {
        appContext = InstrumentationRegistry.getContext();
        bucket = QServer.persistBucket;
        cosPath = null;
        srcPath = QServer.createFile(appContext, 1024 * 1024);
        QServer.init(appContext);
        deleteObject();
    }

    @org.junit.Test
    public void testObject() throws Exception{
        appContext = InstrumentationRegistry.getContext();
        bucket = QServer.persistBucket;
        cosPath = "ip.txt";
        srcPath = QServer.createFile(appContext, 1024 * 1024);
        QServer.init(appContext);
        putObject();
        headObject();
//        optionObject();
//         putObjectACL();
        getObjectACL();
        copyObject();
        partCopyObject();
        getObject();
        deleteObject();
        deleteMultiObject();
        sliceUploadObject();
        abortSliceUploadObject();
        deleteAllObjectsOfBucket();
        QServer.deleteLocalFile(srcPath);
    }

}
