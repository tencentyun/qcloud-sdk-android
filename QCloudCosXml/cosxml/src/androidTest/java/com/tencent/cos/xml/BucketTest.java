package com.tencent.cos.xml;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.bucket.DeleteBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketCORSResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketLifecycleResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketResult;
import com.tencent.cos.xml.model.bucket.GetBucketACLRequest;
import com.tencent.cos.xml.model.bucket.GetBucketACLResult;
import com.tencent.cos.xml.model.bucket.GetBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.GetBucketCORSResult;
import com.tencent.cos.xml.model.bucket.GetBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.GetBucketLifecycleResult;
import com.tencent.cos.xml.model.bucket.GetBucketLocationRequest;
import com.tencent.cos.xml.model.bucket.GetBucketLocationResult;
import com.tencent.cos.xml.model.bucket.GetBucketRequest;
import com.tencent.cos.xml.model.bucket.GetBucketResult;
import com.tencent.cos.xml.model.bucket.HeadBucketRequest;
import com.tencent.cos.xml.model.bucket.HeadBucketResult;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsRequest;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsResult;
import com.tencent.cos.xml.model.bucket.PutBucketACLRequest;
import com.tencent.cos.xml.model.bucket.PutBucketACLResult;
import com.tencent.cos.xml.model.bucket.PutBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.PutBucketCORSResult;
import com.tencent.cos.xml.model.bucket.PutBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.PutBucketLifecycleResult;
import com.tencent.cos.xml.model.bucket.PutBucketRequest;
import com.tencent.cos.xml.model.bucket.PutBucketResult;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.AbortMultiUploadResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.model.tag.CORSConfiguration;
import com.tencent.cos.xml.model.tag.LifecycleConfiguration;
import com.tencent.cos.xml.model.tag.ListAllMyBuckets;
import com.tencent.cos.xml.model.tag.ListMultipartUploads;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static com.tencent.cos.xml.QServer.TAG;

/**
 * Created by bradyxiao on 2018/3/13.
 */

@RunWith(AndroidJUnit4.class)
public class BucketTest {

    private Context appContext;
    private String bucketName;

    public void putBucket() throws CosXmlServiceException, CosXmlClientException {
        PutBucketRequest putBucketRequest = new PutBucketRequest(bucketName);
        PutBucketResult putBucketResult = QServer.cosXml.putBucket(putBucketRequest);
        Log.d(TAG, putBucketResult.printResult());
    }

    public void getBucketLocation() throws CosXmlServiceException, CosXmlClientException {
        GetBucketLocationRequest getBucketLocationRequest = new GetBucketLocationRequest(bucketName);
        GetBucketLocationResult getBucketLocationResult = QServer.cosXml.getBucketLocation(getBucketLocationRequest);
        Log.d(TAG, getBucketLocationResult.printResult());
    }

    public void headBucket() throws CosXmlServiceException, CosXmlClientException {
        HeadBucketRequest headBucketRequest = new HeadBucketRequest(bucketName);
        HeadBucketResult headBucketResult = QServer.cosXml.headBucket(headBucketRequest);
        Log.d(TAG, headBucketResult.printResult());
    }

    public void getBucket() throws CosXmlServiceException, CosXmlClientException {
        GetBucketRequest getBucketRequest = new GetBucketRequest(bucketName);
        getBucketRequest.setPrefix("/1251668577/burning/!#$%&'*+,-./,");
        GetBucketResult getBucketResult = QServer.cosXml.getBucket(getBucketRequest);
        Log.d(TAG, getBucketResult.printResult());
    }

    public void putBucketCORS() throws CosXmlServiceException, CosXmlClientException {
        PutBucketCORSRequest putBucketCORSRequest = new PutBucketCORSRequest(bucketName);
        CORSConfiguration.CORSRule corsRule = new CORSConfiguration.CORSRule();
        corsRule.id = "cors" + new Random(System.currentTimeMillis()).nextInt();
        corsRule.maxAgeSeconds = 5000;
        corsRule.allowedOrigin = "cloud.tencent.com";
        corsRule.allowedMethod = new ArrayList<>();
        corsRule.allowedMethod.add("PUT");
        corsRule.allowedMethod.add("GET");
        corsRule.allowedHeader = new ArrayList<>();
        corsRule.allowedHeader.add("Host");
        corsRule.allowedHeader.add("Except");
        corsRule.exposeHeader = new ArrayList<>();
        corsRule.exposeHeader.add("x-cos-meta-1");
        List<CORSConfiguration.CORSRule> corsRules = new ArrayList<>();
        corsRules.add(corsRule);
        putBucketCORSRequest.addCORSRules(corsRules);
        PutBucketCORSResult putBucketCORSResult = QServer.cosXml.putBucketCORS(putBucketCORSRequest);
        Log.d(TAG, putBucketCORSResult.printResult());
    }

    public void getBucketCORS() throws CosXmlServiceException, CosXmlClientException {
        GetBucketCORSRequest getBucketCORSRequest = new GetBucketCORSRequest(bucketName);
        GetBucketCORSResult getBucketCORSResult = QServer.cosXml.getBucketCORS(getBucketCORSRequest);
        Log.d(TAG, getBucketCORSResult.printResult());
    }

    public void deleteBucketCORS() throws CosXmlServiceException, CosXmlClientException {
        DeleteBucketCORSRequest deleteBucketCORSRequest = new DeleteBucketCORSRequest(bucketName);
        DeleteBucketCORSResult deleteBucketCORSResult = QServer.cosXml.deleteBucketCORS(deleteBucketCORSRequest);
        Log.d(TAG, deleteBucketCORSResult.printResult());
    }

    public void putBucketACL() throws CosXmlServiceException, CosXmlClientException {
        PutBucketACLRequest putBucketACLRequest = new PutBucketACLRequest(bucketName);
        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(QServer.ownUin, QServer.ownUin);
        putBucketACLRequest.setXCOSGrantRead(aclAccount);
        putBucketACLRequest.setXCOSGrantWrite(aclAccount);
        putBucketACLRequest.setXCOSACL(COSACL.PRIVATE);
        PutBucketACLResult putBucketACLResult = QServer.cosXml.putBucketACL(putBucketACLRequest);
        Log.d(TAG, putBucketACLResult.printResult());
    }

    public void getBucketACL() throws CosXmlServiceException, CosXmlClientException {
        GetBucketACLRequest getBucketACLRequest = new GetBucketACLRequest(bucketName);
        GetBucketACLResult getBucketACLResult = QServer.cosXml.getBucketACL(getBucketACLRequest);
        Log.d(TAG, getBucketACLResult.printResult());
    }

    public void putBucketLifecycle() throws CosXmlServiceException, CosXmlClientException {
        PutBucketLifecycleRequest putBucketLifecycleRequest = new PutBucketLifecycleRequest(bucketName);
        LifecycleConfiguration.Rule rule = new LifecycleConfiguration.Rule();
        rule.id = "lifecycle_" + new Random(System.currentTimeMillis()).nextInt();
        rule.status = "Enabled";
        rule.transition = new LifecycleConfiguration.Transition();
        rule.transition.days = 1;
        rule.transition.storageClass = COSStorageClass.STANDARD_IA.getStorageClass();
        putBucketLifecycleRequest.setRuleList(rule);
        PutBucketLifecycleResult putBucketLifecycleResult = QServer.cosXml.putBucketLifecycle(putBucketLifecycleRequest);
        Log.d(TAG, putBucketLifecycleResult.printResult());
    }

    public void getBucketLifecycle() throws CosXmlServiceException, CosXmlClientException {
        GetBucketLifecycleRequest getBucketLifecycleRequest = new GetBucketLifecycleRequest(bucketName);
        GetBucketLifecycleResult getBucketLifecycleResult = QServer.cosXml.getBucketLifecycle(getBucketLifecycleRequest);
        Log.d(TAG, getBucketLifecycleResult.printResult());
    }

    public void deleteBucketLifecycle() throws CosXmlServiceException, CosXmlClientException {
        DeleteBucketLifecycleRequest deleteBucketLifecycleRequest = new DeleteBucketLifecycleRequest(bucketName);
        DeleteBucketLifecycleResult deleteBucketLifecycleResult = QServer.cosXml.deleteBucketLifecycle(deleteBucketLifecycleRequest);
        Log.d(TAG, deleteBucketLifecycleResult.printResult());
    }

    public void listMultiUploads() throws CosXmlServiceException, CosXmlClientException {
        ListMultiUploadsRequest listMultiUploadsRequest = new ListMultiUploadsRequest(bucketName);
        ListMultiUploadsResult listMultiUploadsResult = QServer.cosXml.listMultiUploads(listMultiUploadsRequest);
        Log.d(TAG, listMultiUploadsResult.printResult());
    }

    public void deleteAllUploadIdOfBucket() throws CosXmlServiceException, CosXmlClientException {
        ListMultiUploadsRequest listMultiUploadsRequest = new ListMultiUploadsRequest(bucketName);
        ListMultiUploadsResult listMultiUploadsResult = QServer.cosXml.listMultiUploads(listMultiUploadsRequest);
        Log.d(TAG, listMultiUploadsResult.printResult());
        List<ListMultipartUploads.Upload> uploadList = listMultiUploadsResult.listMultipartUploads.uploads;
        if( uploadList != null && uploadList.size() > 0){
            for(ListMultipartUploads.Upload upload : uploadList){
                String uploadId = upload.uploadID;
                String key = upload.key;
                AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(bucketName, key, uploadId);
                AbortMultiUploadResult abortMultiUploadResult = QServer.cosXml.abortMultiUpload(abortMultiUploadRequest);
                Log.d(TAG, abortMultiUploadResult.printResult());
            }
        }
        if(listMultiUploadsResult.listMultipartUploads.isTruncated){
            deleteAllUploadIdOfBucket();
        }
    }


    public void deleteBucket() throws CosXmlServiceException, CosXmlClientException {
        DeleteBucketRequest deleteBucketRequest = new DeleteBucketRequest(bucketName);
        DeleteBucketResult deleteBucketResult = QServer.cosXml.deleteBucket(deleteBucketRequest);
        Log.d(TAG, deleteBucketResult.printResult());
    }

    public void deleteAllBucketsOfAppid() throws CosXmlServiceException, CosXmlClientException {
        GetServiceRequest getServiceRequest = new GetServiceRequest();
        GetServiceResult getServiceResult = QServer.cosXml.getService(getServiceRequest);
        Log.d(TAG, getServiceResult.printResult());
        List<ListAllMyBuckets.Bucket> bucketList = getServiceResult.listAllMyBuckets.buckets;
        if(bucketList != null && bucketList.size() > 0){
            for(ListAllMyBuckets.Bucket bucket : bucketList){
                String bucketName = bucket.name;
                String region = bucket.location;
                if(bucketName != null && bucketName.startsWith("android") && region.equalsIgnoreCase(QServer.region) ){
                    if(bucketName.startsWith(QServer.persistBucket))
                        continue;
                    try {
                        DeleteBucketRequest deleteBucketRequest = new DeleteBucketRequest(bucketName);
                        DeleteBucketResult deleteBucketResult = QServer.cosXml.deleteBucket(deleteBucketRequest);
                        Log.d(TAG, deleteBucketResult.printResult());
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
    }

    @Test
    public void testBucket() throws Exception{
        appContext = InstrumentationRegistry.getContext();
        QServer.init(appContext);
        bucketName = QServer.tempBucket;

        try {
            putBucket();
        } catch (CosXmlServiceException e) {
//            if (e.getStatusCode() != 409) {
//                throw e;
//            }
            e.printStackTrace();
        }

        getBucket();
        getBucketLocation();
        headBucket();

        putBucketCORS();
        Thread.sleep(1000);
        getBucketCORS();
        deleteBucketCORS();

        putBucketACL();
        Thread.sleep(1000);
        getBucketACL();

        putBucketLifecycle();
        Thread.sleep(1000);
        getBucketLifecycle();
        deleteBucketLifecycle();

        listMultiUploads();
        deleteAllUploadIdOfBucket();
        try {
            deleteBucket();
        }catch (CosXmlServiceException e){
//            if (e.getStatusCode() != 409) {
//                throw e;
//            }
            e.printStackTrace();
        }

        // deleteAllBucketsOfAppid();
    }
}
