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

import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.model.tag.AccessControlPolicy;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * 设置 COS 对象的访问权限信息（Access Control List, ACL）的请求.
 * @see com.tencent.cos.xml.CosXml#putObjectACL(PutObjectACLRequest)
 * @see com.tencent.cos.xml.CosXml#putObjectACLAsync(PutObjectACLRequest, CosXmlResultListener)
 */
final public class PutObjectACLRequest extends ObjectRequest {
    private AccessControlPolicy accessControlPolicy;

    public PutObjectACLRequest(String bucket, String cosPath){
        super(bucket, cosPath);
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("acl", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {

        if (accessControlPolicy == null) {
            return RequestBodySerializer.bytes(null, new byte[0]);
        } else {
            try {
                return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                        XmlBuilder.buildAccessControlPolicyXML(accessControlPolicy));
            } catch (XmlPullParserException e) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
            } catch (IOException e) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
            }
        }
    }

    /**
     * 设置acl信息
     * @param accessControlPolicy acl信息
     */
    public void setAccessControlPolicy(AccessControlPolicy accessControlPolicy) {
        this.accessControlPolicy = accessControlPolicy;
    }

    /**
     * 定义对象的访问控制列表（ACL）属性。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/30752#.E9.A2.84.E8.AE.BE.E7.9A.84-acl">ACL 概述</a> 文档中对象的预设 ACL 部分，例如 default，private，public-read 等，默认为 default
     * @param cosacl COS 访问权限
     */
    public void setXCOSACL(COSACL cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL, cosacl.getAcl());
        }
    }

    /**
     * 同{@link #setXCOSACL(COSACL)}
     * @param cosacl COS 访问权限
     */
    public void setXCOSACL(String cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl);
        }
    }

    /**
     * 赋予被授权者操作对象的读取权限
     * @param aclAccount ACL授权账号列表
     */
    public void setXCOSGrantRead(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_READ, aclAccount.getAccount());
        }
    }

    /**
     * 赋予被授权者操作对象的写入权限
     * @param aclAccount ACL授权账号列表
     */
    public void setXCOSGrantWrite(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_WRITE, aclAccount.getAccount());
        }
    }

    /**
     * 赋予被授权者读取对象的访问控制列表（ACL）的权限
     * @param aclAccount 用户权限列表
     */
    public void setXCOSGrantReadACP(ACLAccount aclAccount) {
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_READ_ACP, aclAccount.getAccount());
        }
    }

    /**
     * 赋予被授权者写入对象的访问控制列表（ACL）的权限
     * @param aclAccount 用户权限列表
     */
    public void setXCOSGrantWriteACP(ACLAccount aclAccount) {
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_WRITE_ACP, aclAccount.getAccount());
        }
    }

    /**
     * 赋予被授权者操作对象的所有权限
     * @param aclAccount 用户权限列表
     */
    public void setXCOSReadFullControl(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_FULL_CONTROL, aclAccount.getAccount());
        }
    }

    /**
     * 赋予被授权者操作对象的所有权限
     * @param aclAccount 用户权限列表
     */
    public void setXCOSReadWrite(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_FULL_CONTROL, aclAccount.getAccount());
        }
    }
}
