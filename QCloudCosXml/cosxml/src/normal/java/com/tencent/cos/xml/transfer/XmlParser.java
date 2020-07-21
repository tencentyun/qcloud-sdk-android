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

import com.tencent.cos.xml.model.tag.AccessControlPolicy;
import com.tencent.cos.xml.model.tag.BucketLoggingStatus;
import com.tencent.cos.xml.model.tag.CORSConfiguration;
import com.tencent.cos.xml.model.tag.CopyPart;
import com.tencent.cos.xml.model.tag.DeleteResult;
import com.tencent.cos.xml.model.tag.DomainConfiguration;
import com.tencent.cos.xml.model.tag.InventoryConfiguration;
import com.tencent.cos.xml.model.tag.LifecycleConfiguration;
import com.tencent.cos.xml.model.tag.ListAllMyBuckets;
import com.tencent.cos.xml.model.tag.ListBucket;
import com.tencent.cos.xml.model.tag.ListBucketVersions;
import com.tencent.cos.xml.model.tag.ListInventoryConfiguration;
import com.tencent.cos.xml.model.tag.ListMultipartUploads;
import com.tencent.cos.xml.model.tag.ListVersionResult;
import com.tencent.cos.xml.model.tag.LocationConstraint;
import com.tencent.cos.xml.model.tag.ReplicationConfiguration;
import com.tencent.cos.xml.model.tag.Tagging;
import com.tencent.cos.xml.model.tag.VersioningConfiguration;
import com.tencent.cos.xml.model.tag.WebsiteConfiguration;
import com.tencent.cos.xml.model.tag.pic.PicObject;
import com.tencent.cos.xml.model.tag.pic.PicOriginalInfo;
import com.tencent.cos.xml.model.tag.pic.PicUploadResult;

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
     * 解析 GETService结果的所有信息
     * @param inputStream xml输入流
     * @param result GET Service 结果的所有信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseListAllMyBucketsResult(InputStream inputStream, ListAllMyBuckets result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        ListAllMyBuckets.Bucket bucket = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                /**xml document start*/
                case XmlPullParser.START_DOCUMENT:
                    break;
                /**xml tag start*/
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Owner")){
                        result.owner = new ListAllMyBuckets.Owner();
                    }else if(tagName.equalsIgnoreCase("ID") ){
                        xmlPullParser.next();
                        result.owner.id = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("DisplayName")){
                        xmlPullParser.next();
                        result.owner.disPlayName = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Buckets")){
                        result.buckets = new ArrayList<ListAllMyBuckets.Bucket>();
                    }else if(tagName.equalsIgnoreCase("Bucket")){
                        bucket = new ListAllMyBuckets.Bucket();
                    }else if(tagName.equalsIgnoreCase("Name")){
                        xmlPullParser.next();
                        bucket.name = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Type")){
                        xmlPullParser.next();
                        bucket.type = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Location")){
                        xmlPullParser.next();
                        bucket.location = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("CreationDate")){
                        xmlPullParser.next();
                        bucket.createDate = xmlPullParser.getText();
                    }
                    break;
                /**xml tag end*/
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Bucket")){
                        result.buckets.add(bucket);
                        bucket = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

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
                            version.isLatest = Boolean.valueOf(xmlPullParser.getText());
                        } else if (deleteMarker != null) {
                            deleteMarker.isLatest = Boolean.valueOf(xmlPullParser.getText());
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
                            version.size = Long.valueOf(xmlPullParser.getText());
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
        result.contentsList = new ArrayList<ListBucket.Contents>();
        result.commonPrefixesList = new ArrayList<ListBucket.CommonPrefixes>();
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
                        contents.key = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("LastModified")){
                        xmlPullParser.next();
                        contents.lastModified = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("ETag")){
                        xmlPullParser.next();
                        contents.eTag = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Size")){
                        xmlPullParser.next();
                        contents.size = Long.parseLong(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("StorageClass")){
                        xmlPullParser.next();
                        contents.storageClass = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Owner")){
                       owner = new ListBucket.Owner();
                    }else if(tagName.equalsIgnoreCase("ID")){
                        xmlPullParser.next();
                        owner.id = xmlPullParser.getText();
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
                            contents.owner = owner;
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
        result.accessControlList.grants = new ArrayList<AccessControlPolicy.Grant>();
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
                        grantee.uri = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("Permission")){
                        xmlPullParser.next();
                        grant.permission = xmlPullParser.getText();
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
                        grant.grantee =grantee;
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
        result.corsRules = new ArrayList<CORSConfiguration.CORSRule>();
        CORSConfiguration.CORSRule corsRule = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("CORSRule")){
                        corsRule = new CORSConfiguration.CORSRule();
                    }else if(tagName.equalsIgnoreCase("ID")){
                        xmlPullParser.next();
                        corsRule.id = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("AllowedOrigin")){
                        xmlPullParser.next();
                        corsRule.allowedOrigin = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("AllowedMethod")){
                        xmlPullParser.next();
                        if(corsRule.allowedMethod == null){
                            corsRule.allowedMethod = new ArrayList<String>();
                        }
                        corsRule.allowedMethod.add(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("AllowedHeader")){
                        xmlPullParser.next();
                        if(corsRule.allowedHeader == null){
                            corsRule.allowedHeader = new ArrayList<String>();
                        }
                        corsRule.allowedHeader.add(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("ExposeHeader")){
                        xmlPullParser.next();
                        if(corsRule.exposeHeader == null){
                            corsRule.exposeHeader = new ArrayList<String>();
                        }
                        corsRule.exposeHeader.add(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("MaxAgeSeconds")){
                        xmlPullParser.next();
                        corsRule.maxAgeSeconds = Integer.parseInt(xmlPullParser.getText());
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
        result.rules = new ArrayList<ReplicationConfiguration.Rule>();
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
                        rule.status = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("ID")){
                        xmlPullParser.next();
                        rule.id = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        rule.prefix = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Destination")){
                        destination = new ReplicationConfiguration.Destination();
                    }else if(tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        destination.bucket = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("StorageClass")){
                        xmlPullParser.next();
                        destination.storageClass = xmlPullParser.getText();
                    }
                    break;
                    case XmlPullParser.END_TAG:
                        tagName = xmlPullParser.getName();
                        if(tagName.equalsIgnoreCase("Rule")){
                            result.rules.add(rule);
                            rule = null;
                        }else if(tagName.equalsIgnoreCase("Destination")){
                            rule.destination = destination;
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
        result.rules = new ArrayList<LifecycleConfiguration.Rule>();
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
                       rule.id = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Filter")){
                        filter = new LifecycleConfiguration.Filter();
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        filter.prefix= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Status")){
                        xmlPullParser.next();
                        rule.status = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Transition")){
                        transition = new LifecycleConfiguration.Transition();
                    }else if(tagName.equalsIgnoreCase("Expiration")){
                        expiration = new LifecycleConfiguration.Expiration();
                    }else if(tagName.equalsIgnoreCase("Days")){
                        xmlPullParser.next();
                        if(transition != null){
                            transition.days = Integer.parseInt(xmlPullParser.getText());
                        }else if(rule.expiration != null){
                            expiration.days = Integer.parseInt(xmlPullParser.getText());
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
                        expiration.expiredObjectDeleteMarker = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("AbortIncompleteMultipartUpload")){
                        abortIncompleteMultiUpload = new LifecycleConfiguration.AbortIncompleteMultiUpload();
                    }else if(tagName.equalsIgnoreCase("DaysAfterInitiation")){
                        xmlPullParser.next();
                        abortIncompleteMultiUpload.daysAfterInitiation = Integer.parseInt(xmlPullParser.getText());
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
                        rule.filter = filter;
                        filter = null;
                    }else if (tagName.equalsIgnoreCase("Transition")){
                        rule.transition = transition;
                        transition = null;
                    }else if (tagName.equalsIgnoreCase("NoncurrentVersionExpiration")){
                        rule.noncurrentVersionExpiration = noncurrentVersionExpiration;
                        noncurrentVersionExpiration = null;
                    }else if (tagName.equalsIgnoreCase("NoncurrentVersionTransition")){
                        rule.noncurrentVersionTransition = noncurrentVersionTransition;
                        noncurrentVersionTransition = null;
                    }else if (tagName.equalsIgnoreCase("Expiration")){
                        rule.expiration = expiration;
                        expiration = null;
                    }else if (tagName.equalsIgnoreCase("AbortIncompleteMultipartUpload")){
                        rule.abortIncompleteMultiUpload = abortIncompleteMultiUpload;
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
     * 解析 所有分块上传的信息
     * @param inputStream xml输入流
     * @param result 所有分块上传的信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseListMultipartUploadsResult(InputStream inputStream, ListMultipartUploads result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        ListMultipartUploads.CommonPrefixes commonPrefixes = null;
        ListMultipartUploads.Upload upload = null;
        result.uploads = new ArrayList<ListMultipartUploads.Upload>();
        result.commonPrefixes = new ArrayList<ListMultipartUploads.CommonPrefixes>();
        ListMultipartUploads.Initiator initiator = null;
        ListMultipartUploads.Owner owner = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if (tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        result.bucket = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("Encoding-Type")){
                        xmlPullParser.next();
                        result.encodingType = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("KeyMarker")){
                        xmlPullParser.next();
                        result.keyMarker = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("UploadIdMarker")){
                        xmlPullParser.next();
                        result.uploadIdMarker = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("NextKeyMarker")){
                        xmlPullParser.next();
                        result.nextKeyMarker = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("NextUploadIdMarker")){
                        xmlPullParser.next();
                        result.nextUploadIdMarker = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("MaxUploads")){
                        xmlPullParser.next();
                        result.maxUploads = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("IsTruncated")){
                        xmlPullParser.next();
                        result.isTruncated = Boolean.parseBoolean(xmlPullParser.getText());
                    }else if (tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        if(commonPrefixes == null){
                            result.prefix = xmlPullParser.getText();
                        }else {
                            commonPrefixes.prefix = xmlPullParser.getText();
                        }
                    }else if (tagName.equalsIgnoreCase("Delimiter")){
                        xmlPullParser.next();
                        result.delimiter = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Upload")){
                        upload = new ListMultipartUploads.Upload();
                    }else if (tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        upload.key = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("UploadId")){
                        xmlPullParser.next();
                        upload.uploadID = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("StorageClass")){
                        xmlPullParser.next();
                        upload.storageClass = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("Initiator")){
                        initiator = new ListMultipartUploads.Initiator();
                    }else if (tagName.equalsIgnoreCase("UIN")){
                        xmlPullParser.next();
                        if(initiator != null){
                            initiator.uin = xmlPullParser.getText();
                        }
                    }else if (tagName.equalsIgnoreCase("Owner")){
                        owner = new ListMultipartUploads.Owner();
                    }else if (tagName.equalsIgnoreCase("UID")){
                        xmlPullParser.next();
                        if(owner != null){
                            owner.uid = xmlPullParser.getText();
                        }
                    }else if (tagName.equalsIgnoreCase("ID")){
                        xmlPullParser.next();
                        if(owner != null){
                            owner.id = xmlPullParser.getText();
                        }else if(initiator != null){
                            initiator.id = xmlPullParser.getText();
                        }
                    }else if (tagName.equalsIgnoreCase("DisplayName")){
                        xmlPullParser.next();
                        if(owner != null){
                            owner.displayName = xmlPullParser.getText();
                        }else if(initiator != null){
                            initiator.displayName = xmlPullParser.getText();
                        }
                    }else if (tagName.equalsIgnoreCase("Initiated")){
                        xmlPullParser.next();
                        upload.initiated = xmlPullParser.getText();
                    }else if (tagName.equalsIgnoreCase("CommonPrefixs")){
                        commonPrefixes = new ListMultipartUploads.CommonPrefixes();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Upload")){
                        result.uploads.add(upload);
                        upload = null;
                    }else if(tagName.equalsIgnoreCase("CommonPrefixs")){
                        result.commonPrefixes.add(commonPrefixes);
                        commonPrefixes = null;
                    }else if(tagName.equalsIgnoreCase("Owner")){
                        upload.owner = owner;
                        owner = null;
                    }else if(tagName.equalsIgnoreCase("Initiator")){
                       upload.initiator = initiator;
                        initiator = null;
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
        result.errorList = new ArrayList<DeleteResult.Error>();
        result.deletedList = new ArrayList<DeleteResult.Deleted>();
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
                        deleted.deleteMarker = Boolean.parseBoolean(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("DeleteMarkerVersionId")){
                        xmlPullParser.next();
                        deleted.deleteMarkerVersionId = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Message")){
                        xmlPullParser.next();
                        error.message = xmlPullParser.getText();
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
     * 解析 分块复制结果
     * @param inputStream xml输入流
     * @param result 分块复制结果
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseCopyPartResult(InputStream inputStream, CopyPart result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("ETag")){
                        xmlPullParser.next();
                        result.eTag = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("LastModified")){
                        xmlPullParser.next();
                        result.lastModified = xmlPullParser.getText();
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
                        objectVersion.key= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("VersionId")){
                        xmlPullParser.next();
                        objectVersion.versionId= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("IsLatest")){
                        xmlPullParser.next();
                        objectVersion.isLatest= Boolean.parseBoolean(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("LastModified")){
                        xmlPullParser.next();
                        objectVersion.lastModified= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Owner")){
                        owner = new ListBucketVersions.Owner();
                    }else if(tagName.equalsIgnoreCase("UID")){
                        xmlPullParser.next();
                        owner.uid = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("ETag")){
                        xmlPullParser.next();
                        ((ListBucketVersions.Version)objectVersion).eTag= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Size")){
                        xmlPullParser.next();
                        ((ListBucketVersions.Version)objectVersion).size= Long.parseLong(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("StorageClass")){
                        xmlPullParser.next();
                        ((ListBucketVersions.Version)objectVersion).storageClass= xmlPullParser.getText();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Owner")){
                        objectVersion.owner = owner;
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
                        indexDocument.suffix = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("ErrorDocument")){
                        errorDocument = new WebsiteConfiguration.ErrorDocument();
                    }else if(tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        errorDocument.key = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("RedirectAllRequestsTo")){
                        redirectAllRequestTo = new WebsiteConfiguration.RedirectAllRequestTo();
                    }else if(tagName.equalsIgnoreCase("Protocol")){
                        xmlPullParser.next();
                        if(redirectAllRequestTo != null){
                            redirectAllRequestTo.protocol = xmlPullParser.getText();
                        }else {
                            routingRule.redirect.protocol = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("RoutingRule")){
                        routingRule = new WebsiteConfiguration.RoutingRule();
                    }else if(tagName.equalsIgnoreCase("Condition")){
                        routingRule.contidion = new WebsiteConfiguration.Contidion();
                    }else if(tagName.equalsIgnoreCase("HttpErrorCodeReturnedEquals")){
                        xmlPullParser.next();
                        routingRule.contidion.httpErrorCodeReturnedEquals = Integer.parseInt(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("KeyPrefixEquals")){
                        xmlPullParser.next();
                        routingRule.contidion.keyPrefixEquals = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Redirect")){
                        routingRule.redirect = new WebsiteConfiguration.Redirect();
                    }else if(tagName.equalsIgnoreCase("ReplaceKeyPrefixWith")){
                        xmlPullParser.next();
                        routingRule.redirect.replaceKeyPrefixWith = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("ReplaceKeyWith")){
                        xmlPullParser.next();
                        routingRule.redirect.replaceKeyWith = xmlPullParser.getText();
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
                        result.isTruncated = Boolean.valueOf(xmlPullParser.getText());
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
                        inventoryConfiguration.id = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("IsEnabled")){
                        xmlPullParser.next();
                        inventoryConfiguration.isEnabled = Boolean.valueOf(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("COSBucketDestination")){
                        cosBucketDestination = new InventoryConfiguration.COSBucketDestination();
                    }else if(tagName.equalsIgnoreCase("Format")){
                        xmlPullParser.next();
                        cosBucketDestination.format = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("AccountId")){
                        xmlPullParser.next();
                        cosBucketDestination.accountId = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        cosBucketDestination.bucket = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        if(cosBucketDestination != null){
                            cosBucketDestination.prefix = xmlPullParser.getText();
                        }else if(filter != null){
                            filter.prefix = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Encryption")){
                        xmlPullParser.next();
                        cosBucketDestination.encryption = new InventoryConfiguration.Encryption();
                    }else if(tagName.equalsIgnoreCase("SSE-COS")){
                        xmlPullParser.next();
                        cosBucketDestination.encryption.sSECOS = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Schedule")){
                        schedule = new InventoryConfiguration.Schedule();
                    }else if(tagName.equalsIgnoreCase("Frequency")){
                        xmlPullParser.next();
                        schedule.frequency = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Filter")){
                        inventoryConfiguration.filter = new InventoryConfiguration.Filter();
                    }else if(tagName.equalsIgnoreCase("IncludedObjectVersions")){
                        xmlPullParser.next();
                        inventoryConfiguration.includedObjectVersions = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("OptionalFields")){
                        optionalFields = new InventoryConfiguration.OptionalFields();
                        optionalFields.fields = new HashSet<>(6);
                    }else if(tagName.equalsIgnoreCase("Field")){
                        xmlPullParser.next();
                        optionalFields.fields.add(xmlPullParser.getText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("COSBucketDestination")){
                        inventoryConfiguration.destination = new InventoryConfiguration.Destination();
                        inventoryConfiguration.destination.cosBucketDestination = cosBucketDestination;
                        cosBucketDestination = null;
                    }else if(tagName.equalsIgnoreCase("OptionalFields")){
                        inventoryConfiguration.optionalFields = optionalFields;
                        optionalFields = null;
                    }else if(tagName.equalsIgnoreCase("Filter")){
                        inventoryConfiguration.filter = filter;
                        filter = null;
                    }else if(tagName.equalsIgnoreCase("Schedule")){
                        inventoryConfiguration.schedule = schedule;
                        schedule = null;
                    }else if(tagName.equalsIgnoreCase("InventoryConfiguration")){
                        result.inventoryConfigurations.add(inventoryConfiguration);
                        inventoryConfiguration = null;
                    }
                    break;
            }
            xmlPullParser.next();
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
                        result.isEnabled = Boolean.valueOf(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("COSBucketDestination")){
                        cosBucketDestination = new InventoryConfiguration.COSBucketDestination();
                    }else if(tagName.equalsIgnoreCase("Format")){
                        xmlPullParser.next();
                        cosBucketDestination.format = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("AccountId")){
                        xmlPullParser.next();
                        cosBucketDestination.accountId = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        cosBucketDestination.bucket = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Prefix")){
                        xmlPullParser.next();
                        if(cosBucketDestination != null){
                            cosBucketDestination.prefix = xmlPullParser.getText();
                        }else if(filter != null){
                            filter.prefix = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("Encryption")){
                        cosBucketDestination.encryption = new InventoryConfiguration.Encryption();
                    }else if(tagName.equalsIgnoreCase("SSE-COS")){
                        xmlPullParser.next();
                        cosBucketDestination.encryption.sSECOS = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Schedule")){
                        schedule = new InventoryConfiguration.Schedule();
                    }else if(tagName.equalsIgnoreCase("Frequency")){
                        xmlPullParser.next();
                        schedule.frequency = xmlPullParser.getText();
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
                        optionalFields.fields.add(xmlPullParser.getText());
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
                        loggingEnabled.targetBucket = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("TargetPrefix")){
                        xmlPullParser.next();
                        loggingEnabled.targetPrefix = xmlPullParser.getText();
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
                        domainRule.status = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Name")){
                        xmlPullParser.next();
                        domainRule.name = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Type")){
                        xmlPullParser.next();
                        domainRule.type = xmlPullParser.getText();
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

    public static void parsePicUploadResult(InputStream inputStream, PicUploadResult picUploadResult) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        PicOriginalInfo picOriginalInfo = null;
        PicObject picObject = null;
        List<PicObject> picObjects = new LinkedList<>();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            switch (eventType) {

                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if (tagName.equalsIgnoreCase("OriginalInfo")) {
                        picOriginalInfo = new PicOriginalInfo();
                    } else if (tagName.equalsIgnoreCase("Object")) {
                        picObject = new PicObject();

                    } else if (tagName.equalsIgnoreCase("Key")) {
                        xmlPullParser.next();

                        if (picOriginalInfo != null && picOriginalInfo.key == null) {
                            picOriginalInfo.key = xmlPullParser.getText();
                        } else if (picObject != null && picObject.key == null){
                            picObject.key = xmlPullParser.getText();
                        }
                    } else if (tagName.equalsIgnoreCase("Location")) {
                        xmlPullParser.next();

                        if (picOriginalInfo != null && picOriginalInfo.location == null) {
                            picOriginalInfo.location = xmlPullParser.getText();
                        } else if (picObject != null && picObject.location == null){
                            picObject.location = xmlPullParser.getText();
                        }
                    } else if (tagName.equalsIgnoreCase("Format")) {
                        xmlPullParser.next();

                        if (picObject != null && picObject.format == null) {
                            picObject.format = xmlPullParser.getText();
                        }
                    } else if (tagName.equalsIgnoreCase("Width")) {
                        xmlPullParser.next();

                        if (picObject != null) {
                            picObject.width = Integer.valueOf(xmlPullParser.getText());
                        }
                    } else if (tagName.equalsIgnoreCase("Height")) {
                        xmlPullParser.next();

                        if (picObject != null) {
                            picObject.height = Integer.valueOf(xmlPullParser.getText());
                        }
                    } else if (tagName.equalsIgnoreCase("Size")) {
                        xmlPullParser.next();

                        if (picObject != null) {
                            picObject.size = Integer.valueOf(xmlPullParser.getText());
                        }
                    } else if (tagName.equalsIgnoreCase("Quality")) {
                        xmlPullParser.next();

                        if (picObject != null) {
                            picObject.quality = Integer.valueOf(xmlPullParser.getText());
                        }
                    }

//                case XmlPullParser.END_TAG:
//                    if ()
            }
        }


    }
}
