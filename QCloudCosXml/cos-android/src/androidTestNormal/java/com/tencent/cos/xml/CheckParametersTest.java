package com.tencent.cos.xml;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.AppendObjectRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.PreBuildConnectionRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * Created by jordanqin on 2023/4/28 18:40.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class CheckParametersTest {
    @Test
    public void checkParameters() {
        PreBuildConnectionRequest preBuildConnectionRequest = new PreBuildConnectionRequest(null);
        try {
            preBuildConnectionRequest.checkParameters();
        } catch (CosXmlClientException e) {
            Assert.assertTrue(e.getMessage().startsWith("bucket must not be null"));
        }

        CompleteMultiUploadRequest completeMultiUploadRequest1 = new CompleteMultiUploadRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_APPEND_OBJECT_PATH, null, null);
        try {
            completeMultiUploadRequest1.checkParameters();
        } catch (CosXmlClientException e) {
            Assert.assertTrue(e.getMessage().startsWith("uploadID must not be null"));
        }

        ListPartsRequest listPartsRequest1 = new ListPartsRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_APPEND_OBJECT_PATH, null);
        try {
            listPartsRequest1.checkParameters();
        } catch (CosXmlClientException e) {
            Assert.assertTrue(e.getMessage().startsWith("uploadID must not be null"));
        }

        AppendObjectRequest request1 = new AppendObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_APPEND_OBJECT_PATH, (String) null, 0);
        try {
            request1.checkParameters();
        } catch (CosXmlClientException e) {
            Assert.assertTrue(e.getMessage().startsWith("Data Source must not be null"));
        }

        AppendObjectRequest request2 = new AppendObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_APPEND_OBJECT_PATH, "file://test.jpg", 0);
        try {
            request2.checkParameters();
        } catch (CosXmlClientException e) {
            Assert.assertTrue(e.getMessage().startsWith("upload file does not exist"));
        }

        AbortMultiUploadRequest abortMultiUploadRequest1 = new AbortMultiUploadRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH, null);
        try {
            abortMultiUploadRequest1.checkParameters();
        } catch (CosXmlClientException e) {
            Assert.assertTrue(e.getMessage().startsWith("uploadID must not be null"));
        }


    }
}
