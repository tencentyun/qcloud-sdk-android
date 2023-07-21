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

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.ReplicationConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 配置跨区域复制的请求.
 * @see com.tencent.cos.xml.CosXml#putBucketReplication(PutBucketReplicationRequest)
 * @see com.tencent.cos.xml.CosXml#putBucketReplicationAsync(PutBucketReplicationRequest, CosXmlResultListener)
 */
public class PutBucketReplicationRequest extends BucketRequest {

    private ReplicationConfiguration replicationConfiguration;

    public PutBucketReplicationRequest(String bucket){
        super(bucket);
        replicationConfiguration = new ReplicationConfiguration();
        replicationConfiguration.rules = new ArrayList<>();
    }

    /**设置 replication的发起者身份标示*/
    public void setReplicationConfigurationWithRole(String ownerUin, String subUin){
        if(ownerUin != null && subUin != null){
            String role = "qcs::cam::uin/" + ownerUin + ":uin/" + subUin;
            replicationConfiguration.role = role;
        }
    }

    /**	具体配置信息，最多支持 1000 个，所有策略只能指向一个目标存储桶*/
    public void setReplicationConfigurationWithRule(RuleStruct ruleStruct){
        if(ruleStruct != null){
            ReplicationConfiguration.Rule rule = new ReplicationConfiguration.Rule();
            rule.id = ruleStruct.id;
            rule.status = ruleStruct.isEnable?"Enabled":"Disabled";
            rule.prefix = ruleStruct.prefix;
            ReplicationConfiguration.Destination destination = new ReplicationConfiguration.Destination();
            destination.storageClass = ruleStruct.storageClass;
            StringBuilder bucket = new StringBuilder();
            bucket.append("qcs::cos:").append(ruleStruct.region).append("::")
                    .append(ruleStruct.bucket);
            destination.bucket = bucket.toString();
            rule.destination = destination;
            replicationConfiguration.rules.add(rule);
        }
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("replication", null);
        return super.getQueryString();
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                XmlBuilder.buildReplicationConfiguration(replicationConfiguration));
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }

    /**
     rule的结构体
     */
    public static class RuleStruct{
        public String appid;
        public String region;
        public String bucket;
        public String storageClass;
        public String id;
        public String prefix;
        public boolean isEnable;
    }

}
