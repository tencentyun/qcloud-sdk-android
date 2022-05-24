 
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

package com.tencent.cos.xml.model;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlBooleanListener;
import com.tencent.cos.xml.model.object.GetSnapshotTestAdapter;
import com.tencent.cos.xml.model.object.NormalPutObjectTestAdapter;
import com.tencent.cos.xml.model.tag.COSMetaData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OtherObjectTest {
    @Test
    public void testDoesObjectExist() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        try {
            boolean result = cosXmlService.doesObjectExist(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
            Assert.assertTrue(result);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }

        try {
            boolean result = cosXmlService.doesObjectExist(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+"notexist");
            Assert.assertFalse(result);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void testDoesObjectExistAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        cosXmlService.doesObjectExistAsync(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, new CosXmlBooleanListener() {
            @Override
            public void onSuccess(boolean bool) {
                Assert.assertTrue(bool);
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(exception, serviceException));
            }
        });

        cosXmlService.doesObjectExistAsync(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+"notexist", new CosXmlBooleanListener() {
            @Override
            public void onSuccess(boolean bool) {
                Assert.assertFalse(bool);
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(exception, serviceException));
            }
        });
    }

    @Test
    public void testDeleteObject() {
        String tmpCosPath = TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+"_delete_object";
        new NormalPutObjectTestAdapter(tmpCosPath).testSyncRequest();

        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        try {
            boolean result = cosXmlService.deleteObject(TestConst.PERSIST_BUCKET, tmpCosPath);
            Assert.assertTrue(result);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void testDeleteObjectAsync() {
        String tmpCosPath = TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+"_delete_object_async";
        new NormalPutObjectTestAdapter(tmpCosPath).testSyncRequest();

        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        cosXmlService.deleteObjectAsync(TestConst.PERSIST_BUCKET, tmpCosPath, new CosXmlBooleanListener() {
            @Override
            public void onSuccess(boolean bool) {
                Assert.assertTrue(bool);
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(exception, serviceException));
            }
        });
    }

    @Test
    public void testUpdateObjectMeta() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        COSMetaData cosMetaData = new COSMetaData();
        cosMetaData.put("aaa", "111");
        cosMetaData.put("bbb", "222");
        cosMetaData.put("ccc", "333");
        try {
            //updateObjectMeta会用appid拼接bucket name所以这里的bucket name需要先把appid去掉
            String bucket = TestConst.PERSIST_BUCKET.replace(("-"+TestConst.COS_APPID),"");
            boolean result = cosXmlService.updateObjectMeta(bucket, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, cosMetaData);
            Assert.assertTrue(result);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void testUpdateObjectMetaAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        COSMetaData cosMetaData = new COSMetaData();
        cosMetaData.put("aaa", "111");
        cosMetaData.put("bbb", "222");
        cosMetaData.put("ccc", "333");
        //updateObjectMeta会用appid拼接bucket name所以这里的bucket name需要先把appid去掉
        String bucket = TestConst.PERSIST_BUCKET.replace(("-"+TestConst.COS_APPID),"");
        cosXmlService.updateObjectMetaAsync(bucket, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, cosMetaData, new CosXmlBooleanListener() {
            @Override
            public void onSuccess(boolean bool) {
                Assert.assertTrue(bool);
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(exception, serviceException));
            }
        });
    }

    @Test
    public void testUpdateObjectMetaData() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        COSMetaData cosMetaData = new COSMetaData();
        cosMetaData.put("aaa", "111");
        cosMetaData.put("bbb", "222");
        cosMetaData.put("ccc", "333");
        try {
            boolean result = cosXmlService.updateObjectMetaData(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, cosMetaData);
            Assert.assertTrue(result);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void testUpdateObjectMetaDataAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        COSMetaData cosMetaData = new COSMetaData();
        cosMetaData.put("aaa", "111");
        cosMetaData.put("bbb", "222");
        cosMetaData.put("ccc", "333");
        cosXmlService.updateObjectMetaDataAsync(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, cosMetaData, new CosXmlBooleanListener() {
            @Override
            public void onSuccess(boolean bool) {
                Assert.assertTrue(bool);
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(exception, serviceException));
            }
        });
    }

    @Test public void testGetSnapshot() {

        new GetSnapshotTestAdapter().testSyncRequest();
    }
}  