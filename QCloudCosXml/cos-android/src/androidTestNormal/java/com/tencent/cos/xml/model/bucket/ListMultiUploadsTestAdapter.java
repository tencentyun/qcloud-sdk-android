 
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

package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

import org.junit.Assert;

public class ListMultiUploadsTestAdapter extends NormalRequestTestAdapter<ListMultiUploadsRequest, ListMultiUploadsResult> {
    @Override
    protected ListMultiUploadsRequest newRequestInstance() {
        ListMultiUploadsRequest request = new ListMultiUploadsRequest(TestConst.PERSIST_BUCKET);
        request.setDelimiter("/");
        Assert.assertEquals("/", request.getDelimiter());
        request.setPrefix("do_not_remove/");
        Assert.assertEquals("do_not_remove/", request.getPrefix());
        request.setEncodingType("url");
        Assert.assertEquals("url", request.getEncodingType());
        request.setKeyMarker(null);
        Assert.assertNull(request.getKeyMarker());
        request.setMaxUploads("1000");
        Assert.assertEquals("1000", request.getMaxUploads());
        request.setUploadIdMarker("testaaa");
        Assert.assertEquals("testaaa", request.getUploadIdMarker());
        return request;
    }

    @Override
    protected ListMultiUploadsResult exeSync(ListMultiUploadsRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.listMultiUploads(request);
    }

    @Override
    protected void exeAsync(ListMultiUploadsRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.listMultiUploadsAsync(request, resultListener);
    }
}  