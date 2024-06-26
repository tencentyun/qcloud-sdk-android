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

import android.util.Log;

import com.tencent.cos.xml.BaseCosXml;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;

/**
 * 下载 COS 对象到字节数组的请求.
 * @see BaseCosXml#getObject(String, String)
 */
final public class GetObjectBytesRequest extends ObjectRequest {
    private boolean objectKeySimplifyCheck = true;

    /**
     * 设置是否校验cosPath归并后是否符合规范，默认为true
     * @param objectKeySimplifyCheck 是否校验cosPath归并后是否符合规范
     */
    public void setObjectKeySimplifyCheck(boolean objectKeySimplifyCheck) {
        this.objectKeySimplifyCheck = objectKeySimplifyCheck;
    }

    public GetObjectBytesRequest(String bucket, String cosPath) {
        super(bucket, cosPath);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();

        if(objectKeySimplifyCheck) {
            String normalizedPath = cosPath;
            try {
                File file = new File("/" + cosPath);
                normalizedPath = file.getCanonicalPath();
            } catch (IOException e) {e.printStackTrace();}
            if ("/".equals(normalizedPath)) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "The key in the getobject is illegal");
            }
        }
    }
}
