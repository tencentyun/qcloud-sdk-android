 
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

import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_DOCUMENT_XLSX_PATH;
import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_PIC_PATH;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlBooleanListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.listener.SelectObjectContentListener;
import com.tencent.cos.xml.model.ci.FormatConversionRequest;
import com.tencent.cos.xml.model.object.GetSnapshotTestAdapter;
import com.tencent.cos.xml.model.object.NormalPutObjectTestAdapter;
import com.tencent.cos.xml.model.object.SelectObjectContentRequest;
import com.tencent.cos.xml.model.tag.COSMetaData;
import com.tencent.cos.xml.model.tag.eventstreaming.CompressionType;
import com.tencent.cos.xml.model.tag.eventstreaming.InputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONInput;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONOutput;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONType;
import com.tencent.cos.xml.model.tag.eventstreaming.OutputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.SelectObjectContentEvent;
import com.tencent.cos.xml.model.tag.pic.PicOperations;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

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

    @Test
    public void testPreviewDocumentInHtmlBytes() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        try {
            byte[] bytes = cosXmlService.previewDocumentInHtmlBytes(TestConst.PERSIST_BUCKET, PERSIST_BUCKET_DOCUMENT_XLSX_PATH);
            Assert.assertNotNull(bytes);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void testFormatConversion() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        FormatConversionRequest request = new FormatConversionRequest(TestConst.PERSIST_BUCKET, PERSIST_BUCKET_PIC_PATH, "jpg");
        request.saveBucket = TestConst.PERSIST_BUCKET;
        request.fileId = "/formatConversion";
        PicOperations picOperations = request.getPicOperations();
        Assert.assertNotNull(picOperations);
        cosXmlService.formatConversionAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.assertNotNull(result);
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
            }
        });
    }

    @Test
    public void testSelectObjectContentJson404() {
        JSONInput jsonInput = new JSONInput(JSONType.DOCUMENT);
        jsonInput.withType(JSONType.DOCUMENT).withType(JSONType.DOCUMENT.toString()).setType(JSONType.DOCUMENT);
        jsonInput = new JSONInput(JSONType.DOCUMENT.toString());
        Assert.assertEquals(jsonInput.getType(), JSONType.DOCUMENT.toString());

        JSONOutput jsonOutput = new JSONOutput();
        jsonOutput.withRecordDelimiter("\n").withRecordDelimiter('\n').setRecordDelimiter("\n");
        jsonOutput.setRecordDelimiter('\n');
        jsonOutput = new JSONOutput("\n");
        Assert.assertEquals(jsonOutput.getRecordDelimiter().toString(), "\n");

        InputSerialization inputSerialization = new InputSerialization(CompressionType.NONE, jsonInput);
        inputSerialization.withJson(jsonInput).setJson(jsonInput);
        inputSerialization = new InputSerialization(CompressionType.NONE.toString(), jsonInput);

        OutputSerialization outputSerialization = new OutputSerialization(jsonOutput);
        outputSerialization.withJson(jsonOutput).setJson(jsonOutput);

        SelectObjectContentRequest selectObjectContentRequest = new SelectObjectContentRequest(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SELECT_JSON_PATH+"-404",
                "Select s.name from COSObject s", true,
                inputSerialization, outputSerialization
        );

        String localPath = new File(TestUtils.localParentPath(), "select.json").getAbsolutePath();
        try {
            TestUtils.createFile(localPath, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectObjectContentRequest.setSelectResponseFilePath(localPath);
        selectObjectContentRequest.setSelectObjectContentProgressListener(new SelectObjectContentListener() {
            @Override
            public void onProcess(SelectObjectContentEvent event) {
                System.out.println("SelectObjectContentEvent type " + event.getClass().getSimpleName());
            }
        });

        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        try {
            cosXmlService.selectObjectContent(selectObjectContentRequest);
        } catch (CosXmlClientException e) {
            Assert.fail();
        } catch (CosXmlServiceException e) {
            Assert.assertEquals("NoSuchKey", e.getErrorCode());
        }
    }
}  