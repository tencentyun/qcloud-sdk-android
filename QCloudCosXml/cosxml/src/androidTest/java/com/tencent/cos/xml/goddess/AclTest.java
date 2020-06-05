package com.tencent.cos.xml.goddess;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.Permission;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.bucket.GetBucketACLRequest;
import com.tencent.cos.xml.model.bucket.GetBucketACLResult;
import com.tencent.cos.xml.model.bucket.PutBucketACLRequest;
import com.tencent.cos.xml.model.bucket.PutBucketACLResult;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.model.tag.AccessControlPolicy;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * A place for everything, and everything in its place.
 * <p>
 *
 * Created by rickenwang on 2019-09-11.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@RunWith(AndroidJUnit4.class)
public class AclTest extends BaseCosXmlServiceTest {

    @Before
    public void init() {
        initCosXmlService(InstrumentationRegistry.getContext());
    }

    @After
    public void clear() {
        clearCosXmlService();
    }

    @Test
    public void putBucketACL() {
        PutBucketACLRequest putBucketACLRequest = new PutBucketACLRequest(bucket);
//        try {
//            putBucketACLRequest.setRequestHeaders("Host", "cosxml", false);
//        } catch (CosXmlClientException e) {
//            e.printStackTrace();
//        }
        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(QServer.ownUin, QServer.ownUin);
        putBucketACLRequest.setXCOSGrantRead(aclAccount);
        putBucketACLRequest.setXCOSGrantWrite(aclAccount);
        putBucketACLRequest.setXCOSACL(COSACL.PRIVATE);
        PutBucketACLResult putBucketACLResult = null;
        try {
            putBucketACLResult = cosXmlService.putBucketACL(putBucketACLRequest);
        } catch (CosXmlClientException | CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(putBucketACLResult);
        Assert.assertEquals(200, putBucketACLResult.httpCode);
    }

    @Test public void getBucketACL() {
        GetBucketACLRequest getBucketACLRequest = new GetBucketACLRequest(bucket);
        GetBucketACLResult getBucketACLResult = null;
        try {
            getBucketACLResult = cosXmlService.getBucketACL(getBucketACLRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(getBucketACLResult);
        Assert.assertEquals(200, getBucketACLResult.httpCode);
        Assert.assertNotNull(getBucketACLResult.accessControlPolicy);
    }



}
