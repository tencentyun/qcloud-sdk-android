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

package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.RestoreConfigure;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * 恢复归档对象的请求.
 * @see com.tencent.cos.xml.CosXml#restoreObject(RestoreRequest)
 * @see com.tencent.cos.xml.CosXml#restoreObjectAsync(RestoreRequest, CosXmlResultListener)
 */
public class RestoreRequest extends ObjectRequest {

    private RestoreConfigure restoreConfigure;

    public RestoreRequest(String bucket, String cosPath) {
        super(bucket, cosPath);
        restoreConfigure = new RestoreConfigure();
        restoreConfigure.casJobParameters = new RestoreConfigure.CASJobParameters();
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("restore", null);
        return queryParameters;
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                XmlBuilder.buildRestore(restoreConfigure));
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }

    /**
     * 指定恢复出的临时副本的有效时长，单位为“天”
     * @param days 恢复出的临时副本的有效时长
     */
    public void setExpireDays(int days){
        if(days < 0) days = 0;
        restoreConfigure.days = days;
    }

    /**
     * 设置恢复工作参数
     * @param tier 恢复工作参数
     */
    public void setTier(RestoreConfigure.Tier tier){
        if(tier != null){
            restoreConfigure.casJobParameters.tier = tier.getTier();
        }
    }
}
