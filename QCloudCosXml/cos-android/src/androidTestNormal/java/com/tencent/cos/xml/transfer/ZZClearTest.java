 
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

package com.tencent.cos.xml.transfer;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.bucket.GetBucketObjectVersionsRequest;
import com.tencent.cos.xml.model.bucket.GetBucketObjectVersionsResult;
import com.tencent.cos.xml.model.object.DeleteMultiObjectRequest;
import com.tencent.cos.xml.model.tag.ListVersionResult;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ZZClearTest {
    //com.tencent.cos.xml.transfer.ZClearTest是为了在transfer测试类最后执行

    @Test
    public void clearTestObjects() {
        //清理copy和upload文件夹中的文件
        List<String> deleteKeys = new ArrayList<>();
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();

        GetBucketObjectVersionsRequest requestUpload = new GetBucketObjectVersionsRequest(TestConst.PERSIST_BUCKET);
        requestUpload.setPrefix("upload/");
        try {
            GetBucketObjectVersionsResult result = cosXmlService.getBucketObjectVersions(requestUpload);
            if(result.listVersionResult.versions!=null) {
                for (ListVersionResult.Version version : result.listVersionResult.versions) {
                    deleteKeys.add(version.key);
                }
            }
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

        GetBucketObjectVersionsRequest requestCopy = new GetBucketObjectVersionsRequest(TestConst.PERSIST_BUCKET);
        requestCopy.setPrefix("copy/");
        try {
            GetBucketObjectVersionsResult result = cosXmlService.getBucketObjectVersions(requestCopy);
            if(result.listVersionResult.versions!=null) {
                for (ListVersionResult.Version version : result.listVersionResult.versions) {
                    deleteKeys.add(version.key);
                }
            }
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

        DeleteMultiObjectRequest deleteMultiObjectRequest = new DeleteMultiObjectRequest(TestConst.PERSIST_BUCKET, deleteKeys);
        try {
            cosXmlService.deleteMultiObject(deleteMultiObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
    }


}  