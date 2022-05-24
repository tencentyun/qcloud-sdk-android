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

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.qcloud.core.auth.STSCredentialScope;

/**
 * 对象分块上传请求基类
 */
public abstract class BaseMultipartUploadRequest extends UploadRequest {

    BaseMultipartUploadRequest(String bucket, String cosPath){
        super(bucket, cosPath);
    }

    @Override
    public STSCredentialScope[] getSTSCredentialScope(CosXmlServiceConfig config) {
        String[] actions = new String[] {
                "name/cos:InitiateMultipartUpload",
                "name/cos:ListParts",
                "name/cos:UploadPart",
                "name/cos:CompleteMultipartUpload",
                "name/cos:AbortMultipartUpload"
        };
        STSCredentialScope[] scopes = new STSCredentialScope[actions.length];
        int i = 0;
        for (String action : actions) {
            scopes[i++] = new STSCredentialScope(action, config.getBucket(bucket),
                    config.getRegion(), getPath(config));
        }
        return scopes;
    }
}
