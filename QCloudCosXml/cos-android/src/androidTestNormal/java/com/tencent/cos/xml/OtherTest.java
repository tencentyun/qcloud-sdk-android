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

package com.tencent.cos.xml;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.bucket.GetBucketRequest;
import com.tencent.cos.xml.model.bucket.GetBucketResult;
import com.tencent.cos.xml.model.tag.Tagging;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OtherTest {
    @Test
    public void testTagging(){
        Tagging tagging1 = new Tagging();
        Tagging.TagSet tagSet1 = new Tagging.TagSet();
        Tagging.Tag tag11 = new Tagging.Tag("key1", "value1");
        Tagging.Tag tag12 = new Tagging.Tag("key2", "value2");
        Tagging.Tag tag13 = new Tagging.Tag("key3", "value3");
        tagSet1.addTag(tag11);
        tagSet1.addTag(tag12);
        tagSet1.addTag(tag13);
        tagging1.tagSet = tagSet1;

        Tagging tagging2 = new Tagging();
        Tagging.TagSet tagSet2 = new Tagging.TagSet();
        Tagging.Tag tag21 = new Tagging.Tag("key1", "value1");
        Tagging.Tag tag22 = new Tagging.Tag("key2", "value2");
        Tagging.Tag tag23 = new Tagging.Tag("key3", "value3");
        tagSet2.addTag(tag21);
        tagSet2.addTag(tag22);
        tagSet2.addTag(tag23);
        tagging2.tagSet = tagSet2;

        Assert.assertEquals(tagging1, tagging2);
    }

    @Test public void testListDirectory() {
        String region = TestConst.PERSIST_BUCKET_REGION;
        String bucket = TestConst.PERSIST_BUCKET;
        String cosPath = "do_not_remove/";

        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();

        boolean isTruncated = true;
        String marker = null;
        GetBucketRequest getBucketRequest = new GetBucketRequest(region, bucket, cosPath);
        // 设置分页信息
        getBucketRequest.setMarker(marker);
        // 设置不查询子目录
        getBucketRequest.setDelimiter("/");
        getBucketRequest.setMaxKeys(2);
        GetBucketResult getBucketResult;
        try {
            getBucketResult = cosXmlService.getBucket(getBucketRequest);
            isTruncated = getBucketResult.listBucket.isTruncated;
            marker = getBucketResult.listBucket.nextMarker;
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(e.getMessage());
        } catch (CosXmlServiceException e) {
            Assert.fail(e.getMessage());
        }
    }
}
