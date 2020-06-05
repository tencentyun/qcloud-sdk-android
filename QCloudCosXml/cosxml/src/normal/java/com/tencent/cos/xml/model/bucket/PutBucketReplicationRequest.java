package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.ReplicationConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by bradyxiao on 2017/11/6.
 * <p>
 *     用于向开启版本管理的存储桶添加 replication跨域复制配置。
 *     如果存储桶已经拥有 replication 配置，那么该请求会替换现有配置。
 *     使用该接口存储桶，包括目标存储桶，必须已经开启版本管理，版本管理详细请参见
 *     {@link PutBucketVersioningRequest}.
 *     目标存储桶不能与其在同一个区域。
 * </p>
 *
*/

public class PutBucketReplicationRequest extends BucketRequest {

    private ReplicationConfiguration replicationConfiguration;

    public PutBucketReplicationRequest(String bucket){
        super(bucket);
        replicationConfiguration = new ReplicationConfiguration();
        replicationConfiguration.rules = new ArrayList<>();
    }

    public PutBucketReplicationRequest(){
        super(null);
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
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildReplicationConfiguration(replicationConfiguration));
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
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
