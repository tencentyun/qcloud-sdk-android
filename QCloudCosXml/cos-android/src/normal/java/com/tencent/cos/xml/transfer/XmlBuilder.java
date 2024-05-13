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


import android.text.TextUtils;

import com.tencent.cos.xml.model.tag.AccessControlPolicy;
import com.tencent.cos.xml.model.tag.BucketLoggingStatus;
import com.tencent.cos.xml.model.tag.CORSConfiguration;
import com.tencent.cos.xml.model.tag.CreateBucketConfiguration;
import com.tencent.cos.xml.model.tag.Delete;
import com.tencent.cos.xml.model.tag.DomainConfiguration;
import com.tencent.cos.xml.model.tag.InventoryConfiguration;
import com.tencent.cos.xml.model.tag.LifecycleConfiguration;
import com.tencent.cos.xml.model.tag.ReplicationConfiguration;
import com.tencent.cos.xml.model.tag.RestoreConfigure;
import com.tencent.cos.xml.model.tag.Tagging;
import com.tencent.cos.xml.model.tag.VersioningConfiguration;
import com.tencent.cos.xml.model.tag.WebsiteConfiguration;
import com.tencent.cos.xml.model.tag.eventstreaming.CSVInput;
import com.tencent.cos.xml.model.tag.eventstreaming.CSVOutput;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONInput;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONOutput;
import com.tencent.cos.xml.model.tag.eventstreaming.SelectRequest;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * xml编码器，
 * 用于将实体转为对应的xml字符串
 */

public class XmlBuilder extends XmlSlimBuilder {

    public static String buildPutBucketAccelerateXML(boolean enable) throws XmlPullParserException, IOException {

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "AccelerateConfiguration");
        addElement(xmlSerializer, "Status", enable ? "Enabled" : "Suspended");
        xmlSerializer.endTag("", "AccelerateConfiguration");
        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 ACL信息 转为XML字符串
     * @param accessControlPolicy ACL信息
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildAccessControlPolicyXML(AccessControlPolicy accessControlPolicy) throws XmlPullParserException, IOException {

        if (accessControlPolicy == null) {
            return null;
        }

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "AccessControlPolicy");
        xmlSerializer.startTag("", "Owner");
        addElement(xmlSerializer, "ID", accessControlPolicy.owner.id);
        xmlSerializer.endTag("", "Owner");
        xmlSerializer.startTag("", "AccessControlList");


        for (AccessControlPolicy.Grant grant : accessControlPolicy.accessControlList.grants) {

            xmlSerializer.startTag("", "Grant");

            if (!TextUtils.isEmpty(grant.grantee.uri)) {
                xmlSerializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
                xmlSerializer.startTag("", "Grantee");
                xmlSerializer.attribute("", "xsi:type", "CanonicalUser");
                addElement(xmlSerializer, "URI", grant.grantee.uri);
                xmlSerializer.endTag("", "Grantee");
            } else if (!TextUtils.isEmpty(grant.grantee.id)) {
                xmlSerializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
                xmlSerializer.startTag("", "Grantee");
                xmlSerializer.attribute("", "xsi:type", "Group");
                addElement(xmlSerializer, "ID", grant.grantee.id);
                xmlSerializer.endTag("", "Grantee");
            }

            addElement(xmlSerializer, "Permission", grant.permission);
            xmlSerializer.endTag("", "Grant");
        }
        xmlSerializer.endTag("", "AccessControlList");
        xmlSerializer.endTag("", "AccessControlPolicy");
        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 跨域资源共享配置 转为XML字符串
     * @param corsConfiguration 跨域资源共享配置
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildCORSConfigurationXML(CORSConfiguration corsConfiguration) throws XmlPullParserException, IOException {

        if(corsConfiguration == null)return null;

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "CORSConfiguration");

        if(corsConfiguration.corsRules != null){
            for(CORSConfiguration.CORSRule corsRule : corsConfiguration.corsRules){
                if(corsRule == null) continue;

                xmlSerializer.startTag("", "CORSRule");

                addElement(xmlSerializer, "ID", corsRule.id);
                addElement(xmlSerializer, "AllowedOrigin", corsRule.allowedOrigin);
                if(corsRule.allowedMethod != null){
                    for(String allowedMethod : corsRule.allowedMethod){
                        addElement(xmlSerializer, "AllowedMethod", allowedMethod);
                    }
                }
                if(corsRule.allowedHeader != null){
                    for(String allowedHeader : corsRule.allowedHeader){
                        addElement(xmlSerializer, "AllowedHeader", allowedHeader);
                    }
                }
                if(corsRule.exposeHeader != null){
                    for(String exposeHeader : corsRule.exposeHeader){
                        addElement(xmlSerializer, "ExposeHeader", exposeHeader);
                    }
                }
                addElement(xmlSerializer, "MaxAgeSeconds", String.valueOf(corsRule.maxAgeSeconds));

                xmlSerializer.endTag("", "CORSRule");
            }
        }

        xmlSerializer.endTag("", "CORSConfiguration");

        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 生命周期配置 转为XML字符串
     * @param lifecycleConfiguration 生命周期配置
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildLifecycleConfigurationXML(LifecycleConfiguration lifecycleConfiguration) throws XmlPullParserException, IOException {
        if (lifecycleConfiguration == null)return null;

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "LifecycleConfiguration");

        if(lifecycleConfiguration.rules != null){
            for(LifecycleConfiguration.Rule rule : lifecycleConfiguration.rules){
                if(rule == null)continue;

                xmlSerializer.startTag("", "Rule");

                addElement(xmlSerializer, "ID", rule.id);
                if(rule.filter != null){
                    xmlSerializer.startTag("", "Filter");
                    addElement(xmlSerializer, "Prefix", rule.filter.prefix);
                    xmlSerializer.endTag("", "Filter");
                }
                addElement(xmlSerializer, "Status", rule.status);

                if(rule.transition != null){
                    xmlSerializer.startTag("", "Transition");
                    addElement(xmlSerializer, "Days", String.valueOf(rule.transition.days));
                    addElement(xmlSerializer, "StorageClass", rule.transition.storageClass);
                    addElement(xmlSerializer, "Date", rule.transition.date);
                    xmlSerializer.endTag("", "Transition");
                }
                if(rule.expiration != null){
                    xmlSerializer.startTag("", "Expiration");
                    addElement(xmlSerializer, "Days", String.valueOf(rule.expiration.days));
                    addElement(xmlSerializer, "ExpiredObjectDeleteMarker", rule.expiration.expiredObjectDeleteMarker);
                    addElement(xmlSerializer, "Date", rule.expiration.date);
                    xmlSerializer.endTag("", "Expiration");
                }
                if(rule.noncurrentVersionTransition != null){
                    xmlSerializer.startTag("", "NoncurrentVersionTransition");
                    addElement(xmlSerializer, "NoncurrentDays", String.valueOf(rule.noncurrentVersionTransition.noncurrentDays));
                    addElement(xmlSerializer, "StorageClass", rule.noncurrentVersionTransition.storageClass);
                    xmlSerializer.endTag("", "NoncurrentVersionTransition");
                }
                if(rule.noncurrentVersionExpiration != null){
                    xmlSerializer.startTag("", "NoncurrentVersionExpiration");
                    addElement(xmlSerializer, "NoncurrentDays", String.valueOf(rule.noncurrentVersionExpiration.noncurrentDays));
                    xmlSerializer.endTag("", "NoncurrentVersionExpiration");
                }
                if(rule.abortIncompleteMultiUpload != null){
                    xmlSerializer.startTag("", "AbortIncompleteMultipartUpload");
                    addElement(xmlSerializer, "DaysAfterInitiation", String.valueOf(rule.abortIncompleteMultiUpload.daysAfterInitiation));
                    xmlSerializer.endTag("", "AbortIncompleteMultipartUpload");
                }

                xmlSerializer.endTag("", "Rule");
            }
        }

        xmlSerializer.endTag("", "LifecycleConfiguration");

        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 跨地域配置信息 转为XML字符串
     * @param replicationConfiguration 跨地域配置信息
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildReplicationConfiguration(ReplicationConfiguration replicationConfiguration) throws XmlPullParserException, IOException {
        if (replicationConfiguration == null)return null;

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "ReplicationConfiguration");

        addElement(xmlSerializer, "Role", replicationConfiguration.role);
        if(replicationConfiguration.rules != null){
            for(ReplicationConfiguration.Rule rule : replicationConfiguration.rules){
                if(rule == null)continue;
                xmlSerializer.startTag("", "Rule");

                addElement(xmlSerializer, "Status", rule.status);
                addElement(xmlSerializer, "ID", rule.id);
                addElement(xmlSerializer, "Prefix", rule.prefix);
                if(rule.destination != null){
                    xmlSerializer.startTag("", "Destination");
                    addElement(xmlSerializer, "Bucket", rule.destination.bucket);
                    addElement(xmlSerializer, "StorageClass", rule.destination.storageClass);
                    xmlSerializer.endTag("", "Destination");
                }
                xmlSerializer.endTag("", "Rule");
            }
        }

        xmlSerializer.endTag("", "ReplicationConfiguration");

        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 版本控制信息 转为XML字符串
     * @param versioningConfiguration 版本控制信息
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildVersioningConfiguration(VersioningConfiguration versioningConfiguration) throws XmlPullParserException, IOException {
        if (versioningConfiguration == null)return null;

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "VersioningConfiguration");
        addElement(xmlSerializer, "Status", versioningConfiguration.status);
        xmlSerializer.endTag("", "VersioningConfiguration");

        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 PUT Bucket 操作信息 转为XML字符串
     * @param createBucketConfiguration PUT Bucket 操作信息
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildCreateBucketConfiguration(CreateBucketConfiguration createBucketConfiguration) throws XmlPullParserException, IOException {

        if (createBucketConfiguration == null) {
            return null;
        }

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "CreateBucketConfiguration");
        addElement(xmlSerializer, "BucketAZConfig", createBucketConfiguration.bucketAzConfig);
        xmlSerializer.endTag("", "CreateBucketConfiguration");
        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 批量删除对象数据 转为XML字符串
     * @param delete 批量删除对象数据
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildDelete(Delete delete) throws XmlPullParserException, IOException {
        if (delete == null)return null;

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "Delete");
        addElement(xmlSerializer, "Quiet", String.valueOf(delete.quiet));
        if(delete.deleteObjects != null){
            for(Delete.DeleteObject deleteObject : delete.deleteObjects){
                if(deleteObject == null)continue;
                xmlSerializer.startTag("", "Object");
                addElement(xmlSerializer, "Key", deleteObject.key);
                addElement(xmlSerializer, "VersionId", deleteObject.versionId);
                xmlSerializer.endTag("", "Object");
            }
        }

        xmlSerializer.endTag("", "Delete");

        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 Object restore 操作的所有请求信息 转为XML字符串
     * @param restoreConfigure Object restore 操作的所有请求信息
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildRestore(RestoreConfigure restoreConfigure) throws XmlPullParserException, IOException {
        if(restoreConfigure == null)return  null;

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "RestoreRequest");
        addElement(xmlSerializer, "Days", String.valueOf(restoreConfigure.days));
        if(restoreConfigure.casJobParameters != null){
            xmlSerializer.startTag("", "CASJobParameters");
            addElement(xmlSerializer, "Tier", restoreConfigure.casJobParameters.tier);
            xmlSerializer.endTag("", "CASJobParameters");
        }
        xmlSerializer.endTag("", "RestoreRequest");

        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 存储桶日志状态信息 转为XML字符串
     * @param bucketLoggingStatus 存储桶日志状态信息
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildBucketLogging(BucketLoggingStatus bucketLoggingStatus) throws XmlPullParserException, IOException {
        if(bucketLoggingStatus == null) return null;
        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "BucketLoggingStatus");
        if(bucketLoggingStatus.loggingEnabled != null){
            xmlSerializer.startTag("", "LoggingEnabled");
            addElement(xmlSerializer, "TargetBucket", bucketLoggingStatus.loggingEnabled.targetBucket);
            addElement(xmlSerializer, "TargetPrefix", bucketLoggingStatus.loggingEnabled.targetPrefix);
            xmlSerializer.endTag("", "LoggingEnabled");
        }
        xmlSerializer.endTag("", "BucketLoggingStatus");
        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 标签集合 转为XML字符串
     * @param tagging 标签集合
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildTagging(Tagging tagging) throws XmlPullParserException, IOException {

        if (tagging == null) {
            return null;
        }

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "Tagging");
        xmlSerializer.startTag("", "TagSet");
        if (!tagging.tagSet.tag.isEmpty()) {
            for (Tagging.Tag tag : tagging.tagSet.tag) {
                xmlSerializer.startTag("", "Tag");
                addElement(xmlSerializer, "Key", tag.key);
                addElement(xmlSerializer, "Value", tag.value);
                xmlSerializer.endTag("", "Tag");
            }
        }
        xmlSerializer.endTag("", "TagSet");
        xmlSerializer.endTag("", "Tagging");
        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 存储桶关联的静态网站配置信息 转为XML字符串
     * @param websiteConfiguration 存储桶关联的静态网站配置信息
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildWebsiteConfiguration(WebsiteConfiguration websiteConfiguration) throws XmlPullParserException, IOException {
        if(websiteConfiguration == null)return null;
        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        xmlSerializer.startDocument("UTF-8", null);
        xmlSerializer.startTag("", "WebsiteConfiguration");

        if(websiteConfiguration.indexDocument != null){
            xmlSerializer.startTag("", "IndexDocument");
            if(websiteConfiguration.indexDocument.suffix != null) addElement(xmlSerializer, "Suffix", websiteConfiguration.indexDocument.suffix);
            xmlSerializer.endTag("", "IndexDocument");
        }

        if(websiteConfiguration.errorDocument != null){
            xmlSerializer.startTag("", "ErrorDocument");
            if(websiteConfiguration.errorDocument.key != null)addElement(xmlSerializer, "Key", websiteConfiguration.errorDocument.key);
            xmlSerializer.endTag("", "ErrorDocument");
        }

        if(websiteConfiguration.redirectAllRequestTo != null){
            xmlSerializer.startTag("", "RedirectAllRequestTo");
            if(websiteConfiguration.redirectAllRequestTo.protocol != null)addElement(xmlSerializer, "Protocol", websiteConfiguration.redirectAllRequestTo.protocol);
            xmlSerializer.endTag("", "RedirectAllRequestTo");
        }

        if(websiteConfiguration.routingRules != null && websiteConfiguration.routingRules.size() > 0){
            xmlSerializer.startTag("", "RoutingRules");
            for(WebsiteConfiguration.RoutingRule routingRule : websiteConfiguration.routingRules){
                xmlSerializer.startTag("", "RoutingRule");
                if(routingRule.contidion != null){
                    xmlSerializer.startTag("", "Condition");
                    if(routingRule.contidion.httpErrorCodeReturnedEquals != -1) addElement(xmlSerializer, "HttpErrorCodeReturnedEquals", String.valueOf(routingRule.contidion.httpErrorCodeReturnedEquals));
                    if(routingRule.contidion.keyPrefixEquals != null) addElement(xmlSerializer, "KeyPrefixEquals", routingRule.contidion.keyPrefixEquals);
                    xmlSerializer.endTag("", "Condition");
                }
                if(routingRule.redirect != null){
                    xmlSerializer.startTag("", "Redirect");
                    if(routingRule.redirect.protocol != null)addElement(xmlSerializer, "Protocol", routingRule.redirect.protocol);
                    if(routingRule.redirect.replaceKeyPrefixWith != null)addElement(xmlSerializer, "ReplaceKeyPrefixWith", routingRule.redirect.replaceKeyPrefixWith);
                    if(routingRule.redirect.replaceKeyWith != null)addElement(xmlSerializer, "ReplaceKeyWith", routingRule.redirect.replaceKeyWith);
                    xmlSerializer.endTag("", "Redirect");
                }
                xmlSerializer.endTag("", "RoutingRule");
            }
            xmlSerializer.endTag("", "RoutingRules");
        }

        xmlSerializer.endTag("", "WebsiteConfiguration");
        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 清单配置参数 转为XML字符串
     * @param inventoryConfiguration 清单配置参数
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildInventoryConfiguration(InventoryConfiguration inventoryConfiguration) throws IOException, XmlPullParserException {
        if(inventoryConfiguration == null)return null;
        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        xmlSerializer.startDocument("UTF-8", null);
        xmlSerializer.startTag("", "InventoryConfiguration");
        if(inventoryConfiguration.id != null)addElement(xmlSerializer, "Id", inventoryConfiguration.id);
        addElement(xmlSerializer, "IsEnabled", inventoryConfiguration.isEnabled ? "true" : "false");
        if(inventoryConfiguration.destination != null){
            xmlSerializer.startTag("", "Destination");
            if(inventoryConfiguration.destination.cosBucketDestination != null){
                xmlSerializer.startTag("", "COSBucketDestination");
                if(inventoryConfiguration.destination.cosBucketDestination.format != null)
                    addElement(xmlSerializer, "Format", inventoryConfiguration.destination.cosBucketDestination.format);
                if(inventoryConfiguration.destination.cosBucketDestination.accountId != null)
                    addElement(xmlSerializer, "AccountId", inventoryConfiguration.destination.cosBucketDestination.accountId);
                if(inventoryConfiguration.destination.cosBucketDestination.bucket != null)
                    addElement(xmlSerializer, "Bucket", inventoryConfiguration.destination.cosBucketDestination.bucket);
                if(inventoryConfiguration.destination.cosBucketDestination.prefix != null){
                    addElement(xmlSerializer, "Prefix", inventoryConfiguration.destination.cosBucketDestination.prefix);
                }
                if(inventoryConfiguration.destination.cosBucketDestination.encryption != null){
                    xmlSerializer.startTag("", "Encryption");
                    addElement(xmlSerializer, "SSE-COS", inventoryConfiguration.destination.cosBucketDestination.encryption.sSECOS);
                    xmlSerializer.endTag("", "Encryption");
                }
                xmlSerializer.endTag("", "COSBucketDestination");
            }
            xmlSerializer.endTag("", "Destination");
        }
        if(inventoryConfiguration.schedule != null && inventoryConfiguration.schedule.frequency != null){
            xmlSerializer.startTag("", "Schedule");
            addElement(xmlSerializer, "Frequency", inventoryConfiguration.schedule.frequency);
            xmlSerializer.endTag("", "Schedule");
        }
        if(inventoryConfiguration.filter != null && inventoryConfiguration.filter.prefix != null){
            xmlSerializer.startTag("", "Filter");
            addElement(xmlSerializer, "Prefix", inventoryConfiguration.filter.prefix);
            xmlSerializer.endTag("", "Filter");
        }
        if(inventoryConfiguration.includedObjectVersions != null){
            addElement(xmlSerializer, "IncludedObjectVersions", inventoryConfiguration.includedObjectVersions);
        }
        if(inventoryConfiguration.optionalFields != null && inventoryConfiguration.optionalFields.fields != null){
            xmlSerializer.startTag("", "OptionalFields");
            for(String field : inventoryConfiguration.optionalFields.fields){
                addElement(xmlSerializer, "Field", field);
            }
            xmlSerializer.endTag("", "OptionalFields");
        }
        xmlSerializer.endTag("", "InventoryConfiguration");
        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 自定义域名配置 转为XML字符串
     * @param domainConfiguration 自定义域名配置
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildDomainConfiguration(DomainConfiguration domainConfiguration) throws IOException, XmlPullParserException {
        if(domainConfiguration == null)return null;
        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);
        xmlSerializer.startTag("", "DomainConfiguration");
        if (domainConfiguration.domainRules != null && domainConfiguration.domainRules.size() > 0)
        {
            for(DomainConfiguration.DomainRule rule : domainConfiguration.domainRules) {
                xmlSerializer.startTag("", "DomainRule");
                addElement(xmlSerializer, "Status", rule.status);
                addElement(xmlSerializer, "Name", rule.name);
                addElement(xmlSerializer, "Type", rule.type);
                addElement(xmlSerializer, "ForcedReplacement", rule.forcedReplacement);
                xmlSerializer.endTag("", "DomainRule");
            }
        }
        xmlSerializer.endTag("", "DomainConfiguration");
        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 将 结构化查询请求信息 转为XML字符串
     * @param selectRequest 结构化查询请求信息
     * @return XML字符串
     * @throws IOException IO异常
     * @throws XmlPullParserException XML转换异常
     */
    public static String buildSelectRequest(SelectRequest selectRequest) throws IOException, XmlPullParserException {

        if (selectRequest == null) {
            return null;
        }

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        // select object content api加了\r\n检测，不允许body中有\r\n
//        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        xmlSerializer.startTag("", "SelectRequest");

        addElement(xmlSerializer, "Expression", selectRequest.getExpression());
        addElement(xmlSerializer, "ExpressionType", selectRequest.getExpressionType());

        xmlSerializer.startTag("", "InputSerialization");
        addElement(xmlSerializer, "CompressionType", selectRequest.getInputSerialization().getCompressionType());
        if (selectRequest.getInputSerialization().getCsv() != null) {

            CSVInput csvInput = selectRequest.getInputSerialization().getCsv();
            xmlSerializer.startTag("", "CSV");
            addElement(xmlSerializer, "FileHeaderInfo", csvInput.getFileHeaderInfo());
            addElement(xmlSerializer, "RecordDelimiter", csvInput.getRecordDelimiterAsString());
            addElement(xmlSerializer, "FieldDelimiter", csvInput.getFieldDelimiterAsString());
            addElement(xmlSerializer, "QuoteCharacter", csvInput.getQuoteCharacterAsString());
            addElement(xmlSerializer, "QuoteEscapeCharacter", csvInput.getQuoteEscapeCharacterAsString());
            addElement(xmlSerializer, "Comments", csvInput.getCommentsAsString());
            addElement(xmlSerializer, "AllowQuotedRecordDelimiter", csvInput.getAllowQuotedRecordDelimiter() ? "TRUE" : "FALSE");
            xmlSerializer.endTag("", "CSV");

        } else if (selectRequest.getInputSerialization().getJson() != null) {

            JSONInput jsonInput = selectRequest.getInputSerialization().getJson();

            xmlSerializer.startTag("", "JSON");
            addElement(xmlSerializer, "Type", jsonInput.getType());
            xmlSerializer.endTag("", "JSON");
        }
        xmlSerializer.endTag("", "InputSerialization");

        xmlSerializer.startTag("", "OutputSerialization");
        if (selectRequest.getOutputSerialization().getCsv() != null) {
            CSVOutput csvOutput = selectRequest.getOutputSerialization().getCsv();
            xmlSerializer.startTag("", "CSV");
            addElement(xmlSerializer, "QuoteFields", csvOutput.getQuoteFields());
            addElement(xmlSerializer, "RecordDelimiter", csvOutput.getRecordDelimiterAsString());
            addElement(xmlSerializer, "FieldDelimiter", csvOutput.getFieldDelimiterAsString());
            addElement(xmlSerializer, "QuoteCharacter", csvOutput.getQuoteCharacterAsString());
            addElement(xmlSerializer, "QuoteEscapeCharacter", csvOutput.getQuoteEscapeCharacterAsString());
            xmlSerializer.endTag("", "CSV");

        } else if (selectRequest.getOutputSerialization().getJson() != null) {

            JSONOutput jsonOutput = selectRequest.getOutputSerialization().getJson();
            xmlSerializer.startTag("", "JSON");
            addElement(xmlSerializer, "RecordDelimiter", jsonOutput.getRecordDelimiterAsString());
            xmlSerializer.endTag("", "JSON");
        }
        xmlSerializer.endTag("", "OutputSerialization");

        xmlSerializer.startTag("", "RequestProgress");
        addElement(xmlSerializer, "Enabled", String.valueOf(selectRequest.getRequestProgress().getEnabled()));
        xmlSerializer.endTag("", "RequestProgress");

        xmlSerializer.endTag("", "SelectRequest");
        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 增加XML节点
     */
    private static void addElement(XmlSerializer xmlSerializer, String tag, String value) throws IOException {
        if(value != null){
            xmlSerializer.startTag("", tag);
            xmlSerializer.text(value);
            xmlSerializer.endTag("", tag);
        }
    }

    /**
     * 删除XML头
     */
    private static String removeXMLHeader(String xmlContent){
        if(xmlContent != null){
            if(xmlContent.startsWith("<?xml")){
                int index = xmlContent.indexOf("?>");
                xmlContent = xmlContent.substring(index + 2);
            }
        }
        return xmlContent;
    }

}
