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

import android.util.Xml;

import com.tencent.cos.xml.model.tag.AccelerateConfiguration;
import com.tencent.cos.xml.model.tag.AccessControlPolicy;
import com.tencent.cos.xml.model.tag.BucketLoggingStatus;
import com.tencent.cos.xml.model.tag.CORSConfiguration;
import com.tencent.cos.xml.model.tag.DeleteResult;
import com.tencent.cos.xml.model.tag.DomainConfiguration;
import com.tencent.cos.xml.model.tag.InventoryConfiguration;
import com.tencent.cos.xml.model.tag.LifecycleConfiguration;
import com.tencent.cos.xml.model.tag.ListBucket;
import com.tencent.cos.xml.model.tag.ListBucketVersions;
import com.tencent.cos.xml.model.tag.ListInventoryConfiguration;
import com.tencent.cos.xml.model.tag.ListVersionResult;
import com.tencent.cos.xml.model.tag.LocationConstraint;
import com.tencent.cos.xml.model.tag.ReplicationConfiguration;
import com.tencent.cos.xml.model.tag.Tagging;
import com.tencent.cos.xml.model.tag.VersioningConfiguration;
import com.tencent.cos.xml.model.tag.WebsiteConfiguration;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * xml解析器，
 * 用于将相应中的xml解析为对应的Result实体
 */

public class XmlParser extends XmlSlimParser {
    /**
     * 解析 BucketObjectVersions结果信息
     * @param inputStream xml输入流
     * @param listVersionResult BucketObjectVersions结果信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseGetBucketObjectVersionsResult(InputStream inputStream, ListVersionResult listVersionResult) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;

        List<ListVersionResult.CommonPrefixes> commonPrefixesList = new LinkedList<>();
        List<ListVersionResult.Version> versions = new LinkedList<>();
        List<ListVersionResult.DeleteMarker> deleteMarkers = new LinkedList<>();
        ListVersionResult.CommonPrefixes commonPrefixes = null;
        ListVersionResult.Version version = null;
        ListVersionResult.DeleteMarker deleteMarker = null;
        ListVersionResult.Owner owner = null;
        listVersionResult.commonPrefixes = commonPrefixesList;
        listVersionResult.deleteMarkers = deleteMarkers;
        listVersionResult.versions = versions;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch(eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if (tagName.equalsIgnoreCase("Name")){
                        xmlPullParser.next();
                        listVersionResult.name = xmlPullParser.getText();
                    } else if(tagName.equalsIgnoreCase("Encoding-Type")){
                        xmlPullParser.next();
                        listVersionResult.encodingType = xmlPullParser.getText();
                    } else if(tagName.equalsIgnoreCase("KeyMarker")){
                        xmlPullParser.next();
                        listVersionResult.keyMarker = xmlPullParser.getText();
                    } else if(tagName.equalsIgnoreCase("VersionIdMarker")){
                        xmlPullParser.next();
                        listVersionResult.versionIdMarker = xmlPullParser.getText();
                    } else if(tagName.equalsIgnoreCase("MaxKeys")){
                        xmlPullParser.next();
                        listVersionResult.maxKeys = Integer.parseInt(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("Delimiter")){
                        xmlPullParser.next();
                        listVersionResult.delimiter = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("NextKeyMarker")){
                        xmlPullParser.next();
                        listVersionResult.nextKeyMarker = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("IsTruncated")){
                        xmlPullParser.next();
                        listVersionResult.isTruncated = Boolean.parseBoolean(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("NextVersionIdMarker")){
                        xmlPullParser.next();
                        listVersionResult.nextVersionIdMarker = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        if(commonPrefixes == null){
                            listVersionResult.prefix = xmlPullParser.getText();
                        }else {
                            commonPrefixes.prefix =  xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        if (version != null) {
                            version.key = xmlPullParser.getText();
                        } else if (deleteMarker != null) {
                            deleteMarker.key = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("VersionId")){
                        xmlPullParser.next();
                        if (version != null) {
                            version.versionID = xmlPullParser.getText();
                        } else if (deleteMarker != null) {
                            deleteMarker.versionId = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("IsLatest")){
                        xmlPullParser.next();
                        if (version != null) {
                            version.isLatest = Boolean.parseBoolean(xmlPullParser.getText());
                        } else if (deleteMarker != null) {
                            deleteMarker.isLatest = Boolean.parseBoolean(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("LastModified")){
                        xmlPullParser.next();
                        if (version != null) {
                            version.lastModified = xmlPullParser.getText();
                        } else if (deleteMarker != null) {
                            deleteMarker.lastModified = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("ETag")){
                        xmlPullParser.next();
                        if (version != null) {
                            version.etag = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Size")){
                        xmlPullParser.next();
                        if (version != null) {
                            version.size = Long.parseLong(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("StorageClass")){
                        xmlPullParser.next();
                        if (version != null) {
                            version.storageClass = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Owner")){
                        owner = new ListVersionResult.Owner();
                    }else if(tagName.equalsIgnoreCase("ID")){
                        xmlPullParser.next();
                        if (owner != null) {
                            owner.id = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("DisplayName")){
                        xmlPullParser.next();
                        if (owner != null) {
                            owner.displayName = xmlPullParser.getText();
                        }


                    }else if(tagName.equalsIgnoreCase("DeleteMarker")){
                        deleteMarker = new ListVersionResult.DeleteMarker();
                    }else if(tagName.equalsIgnoreCase("Version")){
                        version = new ListVersionResult.Version();
                    }else if(tagName.equalsIgnoreCase("CommonPrefixes")){
                        commonPrefixes = new ListVersionResult.CommonPrefixes();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Owner")){
                        if (owner != null) {
                            if (deleteMarker != null) {
                                deleteMarker.owner = owner;
                            } else if (version != null) {
                                version.owner = owner;
                            }
                            owner = null;
                        }
                    }else if(tagName.equalsIgnoreCase("DeleteMarker")) {
                        if (deleteMarker != null) {
                            deleteMarkers.add(deleteMarker);
                            deleteMarker = null;
                        }
                    }else if(tagName.equalsIgnoreCase("Version")) {
                        if (version != null) {
                            versions.add(version);
                            version = null;
                        }
                    }else if(tagName.equalsIgnoreCase("CommonPrefixes")) {
                        if (commonPrefixes != null) {
                            commonPrefixesList.add(commonPrefixes);
                            commonPrefixes = null;
                        }
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }

    }

    /**
     * 解析 存储桶列表
     * @param inputStream xml输入流
     * @param result 存储桶列表
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseListBucketResult(InputStream inputStream, ListBucket result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        ListBucket.Contents contents = null;
        ListBucket.CommonPrefixes commonPrefixes = null;
        ListBucket.Owner owner = null;
        result.contentsList = new ArrayList<>();
        result.commonPrefixesList = new ArrayList<>();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch(eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Name")){
                        xmlPullParser.next();
                        result.name = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Encoding-Type")){
                        xmlPullParser.next();
                        result.encodingType = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Marker")){
                        xmlPullParser.next();
                        result.marker = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("MaxKeys")){
                        xmlPullParser.next();
                        result.maxKeys = Integer.parseInt(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("Delimiter")){
                        xmlPullParser.next();
                        result.delimiter = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("NextMarker")){
                        xmlPullParser.next();
                        result.nextMarker = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("IsTruncated")){
                        xmlPullParser.next();
                        result.isTruncated = Boolean.parseBoolean(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        if(commonPrefixes == null){
                            result.prefix = xmlPullParser.getText();
                        }else {
                            commonPrefixes.prefix =  xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Contents")){
                        contents = new ListBucket.Contents();
                    }else if(tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        if (contents != null) {
                            contents.key = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("LastModified")){
                        xmlPullParser.next();
                        if (contents != null) {
                            contents.lastModified = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("ETag")){
                        xmlPullParser.next();
                        if (contents != null) {
                            contents.eTag = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Size")){
                        xmlPullParser.next();
                        if (contents != null) {
                            contents.size = Long.parseLong(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("StorageClass")){
                        xmlPullParser.next();
                        if (contents != null) {
                            contents.storageClass = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Owner")){
                       owner = new ListBucket.Owner();
                    }else if(tagName.equalsIgnoreCase("ID")){
                        xmlPullParser.next();
                        if (owner != null) {
                            owner.id = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("CommonPrefixes")){
                        commonPrefixes = new ListBucket.CommonPrefixes();
                    }
                    break;
                    case XmlPullParser.END_TAG:
                        tagName = xmlPullParser.getName();
                        if(tagName.equalsIgnoreCase("Contents")){
                            result.contentsList.add(contents);
                            contents = null;
                        }else if(tagName.equalsIgnoreCase("Owner")){
                            if (contents != null) {
                                contents.owner = owner;
                            }
                            owner = null;
                        }else if(tagName.equalsIgnoreCase("CommonPrefixes")){
                            result.commonPrefixesList.add(commonPrefixes);
                            commonPrefixes = null;
                        }
                        break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 ACL信息
     * @param inputStream xml输入流
     * @param result ACL信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseAccessControlPolicy(InputStream inputStream, AccessControlPolicy result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        AccessControlPolicy.Owner owner = null;
        result.accessControlList = new AccessControlPolicy.AccessControlList();
        result.accessControlList.grants = new ArrayList<>();
        AccessControlPolicy.Grant grant = null;
        AccessControlPolicy.Grantee grantee = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if (tagName.equalsIgnoreCase("Owner")){
                        owner = new AccessControlPolicy.Owner();
                    }else if (tagName.equalsIgnoreCase("ID")){
                        xmlPullParser.next();
                        if(owner != null){
                            owner.id = xmlPullParser.getText();
                        }else if(grantee != null){
                            grantee.id = xmlPullParser.getText();
                        }
                    }else if (tagName.equalsIgnoreCase("DisplayName")){
                        xmlPullParser.next();
                        if(owner != null){
                            owner.displayName = xmlPullParser.getText();
                        }else if(grantee != null){
                            grantee.displayName = xmlPullParser.getText();
                        }
                    }else if (tagName.equalsIgnoreCase("Grant")){
                        grant = new AccessControlPolicy.Grant();
                    }else if (tagName.equalsIgnoreCase("Grantee")){
                        grantee = new AccessControlPolicy.Grantee();
                    }else if (tagName.equalsIgnoreCase("URI")){
                        xmlPullParser.next();
                        if (grantee != null) {
                            grantee.uri = xmlPullParser.getText();
                        }
                    }else if (tagName.equalsIgnoreCase("Permission")){
                        xmlPullParser.next();
                        if (grant != null) {
                            grant.permission = xmlPullParser.getText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Owner")){
                        result.owner = owner;
                        owner = null;
                    }else if(tagName.equalsIgnoreCase("Grant")){
                        result.accessControlList.grants.add(grant);
                        grant = null;
                    }else if(tagName.equalsIgnoreCase("Grantee")){
                        if (grant != null) {
                            grant.grantee =grantee;
                        }
                        grantee = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 跨域资源共享配置
     * @param inputStream xml输入流
     * @param result 跨域资源共享配置
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseCORSConfiguration(InputStream inputStream, CORSConfiguration result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        result.corsRules = new ArrayList<>();
        CORSConfiguration.CORSRule corsRule = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("CORSRule")){
                        corsRule = new CORSConfiguration.CORSRule();
                    }else if(tagName.equalsIgnoreCase("ID")){
                        xmlPullParser.next();
                        if (corsRule != null) {
                            corsRule.id = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("AllowedOrigin")){
                        xmlPullParser.next();
                        if (corsRule != null) {
                            corsRule.allowedOrigin = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("AllowedMethod")){
                        xmlPullParser.next();
                        if (corsRule != null && corsRule.allowedMethod == null) {
                            corsRule.allowedMethod = new ArrayList<>();
                        }
                        if (corsRule != null) {
                            corsRule.allowedMethod.add(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("AllowedHeader")){
                        xmlPullParser.next();
                        if (corsRule != null && corsRule.allowedHeader == null) {
                            corsRule.allowedHeader = new ArrayList<>();
                        }
                        if (corsRule != null) {
                            corsRule.allowedHeader.add(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("ExposeHeader")){
                        xmlPullParser.next();
                        if (corsRule != null && corsRule.exposeHeader == null) {
                            corsRule.exposeHeader = new ArrayList<>();
                        }
                        if (corsRule != null) {
                            corsRule.exposeHeader.add(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("MaxAgeSeconds")){
                        xmlPullParser.next();
                        if (corsRule != null) {
                            corsRule.maxAgeSeconds = Integer.parseInt(xmlPullParser.getText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("CORSRule")){
                        result.corsRules.add(corsRule);
                        corsRule = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 跨地域配置信息
     * @param inputStream xml输入流
     * @param result 跨地域配置信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseReplicationConfiguration(InputStream inputStream, ReplicationConfiguration result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        result.rules = new ArrayList<>();
        ReplicationConfiguration.Rule rule = null;
        ReplicationConfiguration.Destination destination = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Role")){
                        xmlPullParser.next();
                        result.role = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Rule")){
                        rule = new ReplicationConfiguration.Rule();
                    }else if(tagName.equalsIgnoreCase("Status")){
                        xmlPullParser.next();
                        if (rule != null) {
                            rule.status = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("ID")){
                        xmlPullParser.next();
                        if (rule != null) {
                            rule.id = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        if (rule != null) {
                            rule.prefix = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Destination")){
                        destination = new ReplicationConfiguration.Destination();
                    }else if(tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        if (destination != null) {
                            destination.bucket = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("StorageClass")){
                        xmlPullParser.next();
                        if (destination != null) {
                            destination.storageClass = xmlPullParser.getText();
                        }
                    }
                    break;
                    case XmlPullParser.END_TAG:
                        tagName = xmlPullParser.getName();
                        if(tagName.equalsIgnoreCase("Rule")){
                            result.rules.add(rule);
                            rule = null;
                        }else if(tagName.equalsIgnoreCase("Destination")){
                            if (rule != null) {
                                rule.destination = destination;
                            }
                            destination = null;
                        }
                        break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 版本控制信息
     * @param inputStream xml输入流
     * @param result 版本控制信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseVersioningConfiguration(InputStream inputStream, VersioningConfiguration result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Status")){
                        xmlPullParser.next();
                        result.status = xmlPullParser.getText();
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 标签集合，
     * 返回的数据中只能有一个 TagSet 标签
     * @param inputStream xml输入流
     * @param tagging 标签集合
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseTagging(InputStream inputStream, Tagging tagging) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        Tagging.TagSet tagSet = null;
        Tagging.Tag tag = null;
        String tagKey = null;
        String tagValue = null;
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if ("TagSet".equalsIgnoreCase(tagName)) { // 开始解析 TagSet
                        tagSet = new Tagging.TagSet();
                    } else if ("Tag".equalsIgnoreCase(tagName)) {
                        tag = new Tagging.Tag();
                    } else if ("Key".equalsIgnoreCase(tagName)) {
                        xmlPullParser.next();
                        tagKey = xmlPullParser.getText();
                    } else if ("Value".equalsIgnoreCase(tagName)) {
                        xmlPullParser.next();
                        tagValue = xmlPullParser.getText();
                    }
                    break;

                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if ("TagSet".equalsIgnoreCase(tagName)) {
                        tagging.tagSet = tagSet;
                        tagSet = null;
                    } else if ("Tag".equalsIgnoreCase(tagName)) {
                        if (tagSet != null) tagSet.addTag(tag);
                    } else if ("Key".equalsIgnoreCase(tagName)) {
                        if (tag != null) tag.key = tagKey;
                    } else if ("Value".equalsIgnoreCase(tagName)) {
                        if (tag != null) tag.value = tagValue;
                    }
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 全球加速集合，
     * @param inputStream xml输入流
     * @param accelerateConfiguration 全球集合
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseAccelerateConfiguration(InputStream inputStream, AccelerateConfiguration accelerateConfiguration) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if ("Status".equalsIgnoreCase(tagName)) { // 开始解析
                        xmlPullParser.next();
                        accelerateConfiguration.status = xmlPullParser.getText();
                    } else if ("Type".equalsIgnoreCase(tagName)) {
                        xmlPullParser.next();
                        accelerateConfiguration.type = xmlPullParser.getText();
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }


    /**
     * 解析 生命周期配置
     * @param inputStream xml输入流
     * @param result 生命周期配置
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseLifecycleConfiguration(InputStream inputStream, LifecycleConfiguration result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        result.rules = new ArrayList<>();
        LifecycleConfiguration.Rule rule = null;
        LifecycleConfiguration.Filter filter = null;
        LifecycleConfiguration.Transition transition = null;
        LifecycleConfiguration.Expiration expiration = null;
        LifecycleConfiguration.AbortIncompleteMultiUpload abortIncompleteMultiUpload = null;
        LifecycleConfiguration.NoncurrentVersionExpiration noncurrentVersionExpiration = null;
        LifecycleConfiguration.NoncurrentVersionTransition noncurrentVersionTransition = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Rule")){
                        rule = new LifecycleConfiguration.Rule();
                    }else if(tagName.equalsIgnoreCase("ID")){
                       xmlPullParser.next();
                        if (rule != null) {
                            rule.id = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Filter")){
                        filter = new LifecycleConfiguration.Filter();
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        if (filter != null) {
                            filter.prefix= xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Status")){
                        xmlPullParser.next();
                        if (rule != null) {
                            rule.status = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Transition")){
                        transition = new LifecycleConfiguration.Transition();
                    }else if(tagName.equalsIgnoreCase("Expiration")){
                        expiration = new LifecycleConfiguration.Expiration();
                    }else if(tagName.equalsIgnoreCase("Days")){
                        xmlPullParser.next();
                        if(transition != null){
                            transition.days = Integer.parseInt(xmlPullParser.getText());
                        }else if (rule != null && rule.expiration != null) {
                            if (expiration != null) {
                                expiration.days = Integer.parseInt(xmlPullParser.getText());
                            }
                        }
                    }else if(tagName.equalsIgnoreCase("Date")){
                        xmlPullParser.next();
                        if(transition != null){
                            transition.date = xmlPullParser.getText();
                        }else if(expiration != null){
                            expiration.date = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("ExpiredObjectDeleteMarker")){
                        xmlPullParser.next();
                        if (expiration != null) {
                            expiration.expiredObjectDeleteMarker = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("AbortIncompleteMultipartUpload")){
                        abortIncompleteMultiUpload = new LifecycleConfiguration.AbortIncompleteMultiUpload();
                    }else if(tagName.equalsIgnoreCase("DaysAfterInitiation")){
                        xmlPullParser.next();
                        if (abortIncompleteMultiUpload != null) {
                            abortIncompleteMultiUpload.daysAfterInitiation = Integer.parseInt(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("NoncurrentVersionExpiration")){
                        noncurrentVersionExpiration = new LifecycleConfiguration.NoncurrentVersionExpiration();
                    }else if(tagName.equalsIgnoreCase("NoncurrentVersionTransition")){
                        noncurrentVersionTransition = new LifecycleConfiguration.NoncurrentVersionTransition();
                    }else if(tagName.equalsIgnoreCase("NoncurrentDays")){
                        xmlPullParser.next();
                        if(noncurrentVersionExpiration != null){
                            noncurrentVersionExpiration.noncurrentDays = Integer.parseInt(xmlPullParser.getText());
                        }else if(noncurrentVersionTransition != null){
                            noncurrentVersionTransition.noncurrentDays = Integer.parseInt(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("StorageClass")){
                        xmlPullParser.next();
                        if(transition != null){
                            transition.storageClass = xmlPullParser.getText();
                        }else if(noncurrentVersionTransition != null){
                            noncurrentVersionTransition.storageClass = xmlPullParser.getText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if (tagName.equalsIgnoreCase("Rule")){
                        result.rules.add(rule);
                        rule = null;
                    }else if (tagName.equalsIgnoreCase("Filter")){
                        if (rule != null) {
                            rule.filter = filter;
                        }
                        filter = null;
                    }else if (tagName.equalsIgnoreCase("Transition")){
                        if (rule != null) {
                            rule.transition = transition;
                        }
                        transition = null;
                    }else if (tagName.equalsIgnoreCase("NoncurrentVersionExpiration")){
                        if (rule != null) {
                            rule.noncurrentVersionExpiration = noncurrentVersionExpiration;
                        }
                        noncurrentVersionExpiration = null;
                    }else if (tagName.equalsIgnoreCase("NoncurrentVersionTransition")){
                        if (rule != null) {
                            rule.noncurrentVersionTransition = noncurrentVersionTransition;
                        }
                        noncurrentVersionTransition = null;
                    }else if (tagName.equalsIgnoreCase("Expiration")){
                        if (rule != null) {
                            rule.expiration = expiration;
                        }
                        expiration = null;
                    }else if (tagName.equalsIgnoreCase("AbortIncompleteMultipartUpload")){
                        if (rule != null) {
                            rule.abortIncompleteMultiUpload = abortIncompleteMultiUpload;
                        }
                        abortIncompleteMultiUpload = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 Bucket所在地域
     * @param inputStream xml输入流
     * @param result Bucket所在地域
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseLocationConstraint(InputStream inputStream, LocationConstraint result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("LocationConstraint")){
                        xmlPullParser.next();
                        result.location = xmlPullParser.getText();
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }


    /**
     * 解析 批量删除结果
     * @param inputStream xml输入流
     * @param result 批量删除结果
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseDeleteResult(InputStream inputStream, DeleteResult result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        result.errorList = new ArrayList<>();
        result.deletedList = new ArrayList<>();
        DeleteResult.Deleted deleted = null;
        DeleteResult.Error error = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Deleted")){
                        deleted = new DeleteResult.Deleted();
                    }else if(tagName.equalsIgnoreCase("Error")){
                        error= new DeleteResult.Error();
                    }else if(tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        if(deleted != null){
                            deleted.key = xmlPullParser.getText();
                        }else if(error != null){
                            error.key = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("VersionId")){
                        xmlPullParser.next();
                        if(deleted != null){
                            deleted.versionId = xmlPullParser.getText();
                        }else if(error != null){
                            error.versionId = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("DeleteMarker")){
                        xmlPullParser.next();
                        if (deleted != null) {
                            deleted.deleteMarker = Boolean.parseBoolean(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("DeleteMarkerVersionId")){
                        xmlPullParser.next();
                        if (deleted != null) {
                            deleted.deleteMarkerVersionId = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Message")){
                        xmlPullParser.next();
                        if (error != null) {
                            error.message = xmlPullParser.getText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Deleted")){
                        result.deletedList.add(deleted);
                        deleted = null;
                    }else if(tagName.equalsIgnoreCase("Error")){
                    result.errorList.add(error);
                    error = null;
                }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 GET Bucket Object versions 结果
     * @param inputStream xml输入流
     * @param result GET Bucket Object versions 结果
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseListBucketVersions(InputStream inputStream, ListBucketVersions result) throws XmlPullParserException, IOException{
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        result.objectVersionList = new ArrayList<>();
        ListBucketVersions.ObjectVersion objectVersion = null;
        ListBucketVersions.Owner owner = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Name")){
                        xmlPullParser.next();
                        result.name = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        result.prefix= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("KeyMarker")){
                        xmlPullParser.next();
                        result.keyMarker= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("VersionIdMarker")){
                        xmlPullParser.next();
                        result.versionIdMarker= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("MaxKeys")){
                        xmlPullParser.next();
                        result.maxKeys= Long.parseLong(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("IsTruncated")){
                        xmlPullParser.next();
                        result.isTruncated= Boolean.parseBoolean(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("NextKeyMarker")){
                        xmlPullParser.next();
                        result.nextKeyMarker= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("NextVersionIdMarker")){
                        xmlPullParser.next();
                        result.nextVersionIdMarker= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("DeleteMarker")){
                        objectVersion = new ListBucketVersions.DeleteMarker();
                    }else if(tagName.equalsIgnoreCase("Version")){
                        objectVersion = new ListBucketVersions.Version();
                    }else if(tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        if (objectVersion != null) {
                            objectVersion.key= xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("VersionId")){
                        xmlPullParser.next();
                        if (objectVersion != null) {
                            objectVersion.versionId= xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("IsLatest")){
                        xmlPullParser.next();
                        if (objectVersion != null) {
                            objectVersion.isLatest= Boolean.parseBoolean(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("LastModified")){
                        xmlPullParser.next();
                        if (objectVersion != null) {
                            objectVersion.lastModified= xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Owner")){
                        owner = new ListBucketVersions.Owner();
                    }else if(tagName.equalsIgnoreCase("UID")){
                        xmlPullParser.next();
                        if (owner != null) {
                            owner.uid = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("ETag")){
                        xmlPullParser.next();
                        if (objectVersion != null) {
                            if (objectVersion instanceof ListBucketVersions.Version) {
                                ((ListBucketVersions.Version)objectVersion).eTag= xmlPullParser.getText();
                            }
                        }
                    }else if(tagName.equalsIgnoreCase("Size")){
                        xmlPullParser.next();
                        if (objectVersion != null) {
                            if (objectVersion instanceof ListBucketVersions.Version) {
                                ((ListBucketVersions.Version)objectVersion).size= Long.parseLong(xmlPullParser.getText());
                            }
                        }
                    }else if(tagName.equalsIgnoreCase("StorageClass")){
                        xmlPullParser.next();
                        if (objectVersion != null) {
                            if (objectVersion instanceof ListBucketVersions.Version) {
                                ((ListBucketVersions.Version)objectVersion).storageClass= xmlPullParser.getText();
                            }
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Owner")){
                        if (objectVersion != null) {
                            objectVersion.owner = owner;
                        }
                        owner = null;
                    }else if(tagName.equalsIgnoreCase("DeleteMarker")){
                        result.objectVersionList.add(objectVersion);
                        objectVersion = null;
                    }else if(tagName.equalsIgnoreCase("Version")){
                        result.objectVersionList.add(objectVersion);
                        objectVersion = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 存储桶关联的静态网站配置信息
     * @param inputStream xml输入流
     * @param result 存储桶关联的静态网站配置信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseWebsiteConfig(InputStream inputStream, WebsiteConfiguration result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        result.routingRules = new ArrayList<>();
        WebsiteConfiguration.RoutingRule routingRule = null;
        WebsiteConfiguration.IndexDocument indexDocument = null;
        WebsiteConfiguration.ErrorDocument errorDocument = null;
        WebsiteConfiguration.RedirectAllRequestTo redirectAllRequestTo = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("IndexDocument")){
                        indexDocument = new WebsiteConfiguration.IndexDocument();
                    }else if(tagName.equalsIgnoreCase("Suffix")){
                        xmlPullParser.next();
                        if (indexDocument != null) {
                            indexDocument.suffix = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("ErrorDocument")){
                        errorDocument = new WebsiteConfiguration.ErrorDocument();
                    }else if(tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        if (errorDocument != null) {
                            errorDocument.key = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("RedirectAllRequestsTo")){
                        redirectAllRequestTo = new WebsiteConfiguration.RedirectAllRequestTo();
                    }else if(tagName.equalsIgnoreCase("Protocol")){
                        xmlPullParser.next();
                        if(redirectAllRequestTo != null){
                            redirectAllRequestTo.protocol = xmlPullParser.getText();
                        }else {
                            if (routingRule != null) {
                                routingRule.redirect.protocol = xmlPullParser.getText();
                            }
                        }
                    }else if(tagName.equalsIgnoreCase("RoutingRule")){
                        routingRule = new WebsiteConfiguration.RoutingRule();
                    }else if(tagName.equalsIgnoreCase("Condition")){
                        if (routingRule != null) {
                            routingRule.contidion = new WebsiteConfiguration.Contidion();
                        }
                    }else if(tagName.equalsIgnoreCase("HttpErrorCodeReturnedEquals")){
                        xmlPullParser.next();
                        if (routingRule != null) {
                            routingRule.contidion.httpErrorCodeReturnedEquals = Integer.parseInt(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("KeyPrefixEquals")){
                        xmlPullParser.next();
                        if (routingRule != null) {
                            routingRule.contidion.keyPrefixEquals = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Redirect")){
                        if (routingRule != null) {
                            routingRule.redirect = new WebsiteConfiguration.Redirect();
                        }
                    }else if(tagName.equalsIgnoreCase("ReplaceKeyPrefixWith")){
                        xmlPullParser.next();
                        if (routingRule != null) {
                            routingRule.redirect.replaceKeyPrefixWith = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("ReplaceKeyWith")){
                        xmlPullParser.next();
                        if (routingRule != null) {
                            routingRule.redirect.replaceKeyWith = xmlPullParser.getText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("IndexDocument")){
                        result.indexDocument = indexDocument;
                        indexDocument = null;
                    }else if(tagName.equalsIgnoreCase("ErrorDocument")){
                        result.errorDocument = errorDocument;
                        errorDocument = null;
                    }else if(tagName.equalsIgnoreCase("RedirectAllRequestsTo")){
                        result.redirectAllRequestTo = redirectAllRequestTo;
                        redirectAllRequestTo = null;
                    }else if(tagName.equalsIgnoreCase("RoutingRule")){
                       result.routingRules.add(routingRule);
                        routingRule = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 存储桶中所有清单任务信息的列表
     * @param inputStream xml输入流
     * @param result 存储桶中所有清单任务信息的列表
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseListInventoryConfiguration(InputStream inputStream, ListInventoryConfiguration result) throws IOException, XmlPullParserException {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        result.inventoryConfigurations = new HashSet<>(20);
        InventoryConfiguration inventoryConfiguration = null;
        InventoryConfiguration.Schedule schedule = null;
        InventoryConfiguration.Filter filter = null;
        InventoryConfiguration.OptionalFields optionalFields = null;
        InventoryConfiguration.COSBucketDestination cosBucketDestination = null;
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("IsTruncated")){
                        xmlPullParser.next();
                        result.isTruncated = Boolean.parseBoolean(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("ContinuationToken")){
                        xmlPullParser.next();
                        result.continuationToken = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("NextContinuationToken")){
                        xmlPullParser.next();
                        result.nextContinuationToken = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("InventoryConfiguration")){
                        inventoryConfiguration = new InventoryConfiguration();
                    }else if(tagName.equalsIgnoreCase("Id")){
                        xmlPullParser.next();
                        if (inventoryConfiguration != null) {
                            inventoryConfiguration.id = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("IsEnabled")){
                        xmlPullParser.next();
                        if (inventoryConfiguration != null) {
                            inventoryConfiguration.isEnabled = Boolean.parseBoolean(xmlPullParser.getText());
                        }
                    }else if(tagName.equalsIgnoreCase("COSBucketDestination")){
                        cosBucketDestination = new InventoryConfiguration.COSBucketDestination();
                    }else if(tagName.equalsIgnoreCase("Format")){
                        xmlPullParser.next();
                        if (cosBucketDestination != null) {
                            cosBucketDestination.format = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("AccountId")){
                        xmlPullParser.next();
                        if (cosBucketDestination != null) {
                            cosBucketDestination.accountId = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        if (cosBucketDestination != null) {
                            cosBucketDestination.bucket = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        if(cosBucketDestination != null){
                            cosBucketDestination.prefix = xmlPullParser.getText();
                        }else if(filter != null){
                            filter.prefix = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Encryption")){
                        xmlPullParser.next();
                        if (cosBucketDestination != null) {
                            cosBucketDestination.encryption = new InventoryConfiguration.Encryption();
                        }
                    }else if(tagName.equalsIgnoreCase("SSE-COS")){
                        xmlPullParser.next();
                        if (cosBucketDestination != null) {
                            cosBucketDestination.encryption.sSECOS = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Schedule")){
                        schedule = new InventoryConfiguration.Schedule();
                    }else if(tagName.equalsIgnoreCase("Frequency")){
                        xmlPullParser.next();
                        if (schedule != null) {
                            schedule.frequency = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Filter")){
                        if (inventoryConfiguration != null) {
                            inventoryConfiguration.filter = new InventoryConfiguration.Filter();
                        }
                    }else if(tagName.equalsIgnoreCase("IncludedObjectVersions")){
                        xmlPullParser.next();
                        if (inventoryConfiguration != null) {
                            inventoryConfiguration.includedObjectVersions = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("OptionalFields")){
                        optionalFields = new InventoryConfiguration.OptionalFields();
                        optionalFields.fields = new HashSet<>(6);
                    }else if(tagName.equalsIgnoreCase("Field")){
                        xmlPullParser.next();
                        if (optionalFields != null) {
                            optionalFields.fields.add(xmlPullParser.getText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("COSBucketDestination")){
                        if (inventoryConfiguration != null) {
                            inventoryConfiguration.destination = new InventoryConfiguration.Destination();
                        }
                        if (inventoryConfiguration != null) {
                            inventoryConfiguration.destination.cosBucketDestination = cosBucketDestination;
                        }
                        cosBucketDestination = null;
                    }else if(tagName.equalsIgnoreCase("OptionalFields")){
                        if (inventoryConfiguration != null) {
                            inventoryConfiguration.optionalFields = optionalFields;
                        }
                        optionalFields = null;
                    }else if(tagName.equalsIgnoreCase("Filter")){
                        if (inventoryConfiguration != null) {
                            inventoryConfiguration.filter = filter;
                        }
                        filter = null;
                    }else if(tagName.equalsIgnoreCase("Schedule")){
                        if (inventoryConfiguration != null) {
                            inventoryConfiguration.schedule = schedule;
                        }
                        schedule = null;
                    }else if(tagName.equalsIgnoreCase("InventoryConfiguration")){
                        result.inventoryConfigurations.add(inventoryConfiguration);
                        inventoryConfiguration = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 清单配置参数
     * @param inputStream xml输入流
     * @param result 清单配置参数
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseInventoryConfiguration(InputStream inputStream, InventoryConfiguration result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        InventoryConfiguration.Schedule schedule = null;
        InventoryConfiguration.Filter filter = null;
        InventoryConfiguration.OptionalFields optionalFields = null;
        InventoryConfiguration.COSBucketDestination cosBucketDestination = null;
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Id")){
                        xmlPullParser.next();
                        result.id = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("IsEnabled")){
                        xmlPullParser.next();
                        result.isEnabled = Boolean.parseBoolean(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("COSBucketDestination")){
                        cosBucketDestination = new InventoryConfiguration.COSBucketDestination();
                    }else if(tagName.equalsIgnoreCase("Format")){
                        xmlPullParser.next();
                        if (cosBucketDestination != null) {
                            cosBucketDestination.format = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("AccountId")){
                        xmlPullParser.next();
                        if (cosBucketDestination != null) {
                            cosBucketDestination.accountId = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        if (cosBucketDestination != null) {
                            cosBucketDestination.bucket = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        if(cosBucketDestination != null){
                            cosBucketDestination.prefix = xmlPullParser.getText();
                        }else if(filter != null){
                            filter.prefix = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Encryption")){
                        if (cosBucketDestination != null) {
                            cosBucketDestination.encryption = new InventoryConfiguration.Encryption();
                        }
                    }else if(tagName.equalsIgnoreCase("SSE-COS")){
                        xmlPullParser.next();
                        if (cosBucketDestination != null) {
                            cosBucketDestination.encryption.sSECOS = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Schedule")){
                        schedule = new InventoryConfiguration.Schedule();
                    }else if(tagName.equalsIgnoreCase("Frequency")){
                        xmlPullParser.next();
                        if (schedule != null) {
                            schedule.frequency = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Filter")){
                        filter = new InventoryConfiguration.Filter();
                    }else if(tagName.equalsIgnoreCase("IncludedObjectVersions")){
                        xmlPullParser.next();
                        result.includedObjectVersions = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("OptionalFields")){
                        optionalFields = new InventoryConfiguration.OptionalFields();
                        optionalFields.fields = new HashSet<>(6);
                    }else if(tagName.equalsIgnoreCase("Field")){
                        xmlPullParser.next();
                        if (optionalFields != null) {
                            optionalFields.fields.add(xmlPullParser.getText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("COSBucketDestination")){
                        result.destination = new InventoryConfiguration.Destination();
                        result.destination.cosBucketDestination = cosBucketDestination;
                        cosBucketDestination = null;
                    }else if(tagName.equalsIgnoreCase("OptionalFields")){
                        result.optionalFields = optionalFields;
                        optionalFields = null;
                    }else if(tagName.equalsIgnoreCase("Filter")){
                        result.filter = filter;
                        filter = null;
                    }else if(tagName.equalsIgnoreCase("Schedule")){
                        result.schedule = schedule;
                        schedule = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 存储桶日志状态信息
     * @param inputStream xml输入流
     * @param result 存储桶日志状态信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseBucketLoggingStatus(InputStream inputStream, BucketLoggingStatus result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        BucketLoggingStatus.LoggingEnabled loggingEnabled = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("LoggingEnabled")){
                        loggingEnabled = new BucketLoggingStatus.LoggingEnabled();
                    }else if(tagName.equalsIgnoreCase("TargetBucket")){
                        xmlPullParser.next();
                        if (loggingEnabled != null) {
                            loggingEnabled.targetBucket = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("TargetPrefix")){
                        xmlPullParser.next();
                        if (loggingEnabled != null) {
                            loggingEnabled.targetPrefix = xmlPullParser.getText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("LoggingEnabled")){
                        result.loggingEnabled = loggingEnabled;
                        loggingEnabled = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }

    }

    /**
     * 解析 自定义域名配置
     * @param inputStream xml输入流
     * @param result 自定义域名配置
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseDomainConfiguration(InputStream inputStream, DomainConfiguration result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        result.domainRules = new ArrayList<>();
        DomainConfiguration.DomainRule domainRule = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("DomainRule")){
                        domainRule = new DomainConfiguration.DomainRule();
                    }else if(tagName.equalsIgnoreCase("Status")){
                        xmlPullParser.next();
                        if (domainRule != null) {
                            domainRule.status = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Name")){
                        xmlPullParser.next();
                        if (domainRule != null) {
                            domainRule.name = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Type")){
                        xmlPullParser.next();
                        if (domainRule != null) {
                            domainRule.type = xmlPullParser.getText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("DomainRule")){
                       result.domainRules.add(domainRule);
                       domainRule = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }

    }
}
