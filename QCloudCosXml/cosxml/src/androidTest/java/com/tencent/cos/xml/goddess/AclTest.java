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

package com.tencent.cos.xml.goddess;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.bucket.GetBucketACLRequest;
import com.tencent.cos.xml.model.bucket.GetBucketACLResult;
import com.tencent.cos.xml.model.bucket.PutBucketACLRequest;
import com.tencent.cos.xml.model.bucket.PutBucketACLResult;
import com.tencent.cos.xml.model.tag.ACLAccount;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AclTest extends BaseCosXmlServiceTest {

    @Before
    public void init() {
        initCosXmlService(TestUtils.getContext());
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
