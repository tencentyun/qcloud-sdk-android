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

import com.tencent.cos.xml.model.tag.CompleteMultipartUploadResult;
import com.tencent.cos.xml.model.tag.CopyObject;
import com.tencent.cos.xml.model.tag.CosError;
import com.tencent.cos.xml.model.tag.InitiateMultipartUpload;
import com.tencent.cos.xml.model.tag.ListParts;
import com.tencent.cos.xml.model.tag.PostResponse;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * xml解析器，
 * 用于将相应中的xml解析为对应的Result实体
 */

public class XmlSlimParser {

    /**
     * 解析 完成分块上传结果
     * @param inputStream xml输入流
     * @param result 完成分块上传结果
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseCompleteMultipartUploadResult(InputStream inputStream, CompleteMultipartUploadResult result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Location")){
                        xmlPullParser.next();
                        result.location = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        result.bucket = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        result.key = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("ETag")){
                        xmlPullParser.next();
                        result.eTag = xmlPullParser.getText();
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 初始化上传请求返回的信息
     * @param inputStream xml输入流
     * @param result 初始化上传请求返回的信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseInitiateMultipartUploadResult(InputStream inputStream, InitiateMultipartUpload result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        result.bucket = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        result.key = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("UploadId")){
                        xmlPullParser.next();
                        result.uploadId = xmlPullParser.getText();
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 ListParts请求结果的所有信息
     * @param inputStream xml输入流
     * @param result ListParts请求结果的所有信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseListPartsResult(InputStream inputStream, ListParts result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        result.parts = new ArrayList<ListParts.Part>();
        ListParts.Owner owner = null;
        ListParts.Initiator initiator = null;
        ListParts.Part part = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        result.bucket = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Encoding-type")){
                        xmlPullParser.next();
                        result.encodingType = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        result.key = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("UploadId")){
                        xmlPullParser.next();
                        result.uploadId = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Owner")){
                        owner = new ListParts.Owner();
                    }else if(tagName.equalsIgnoreCase("Initiator")){
                        initiator = new ListParts.Initiator();
                    }else if(tagName.equalsIgnoreCase("ID")){
                        xmlPullParser.next();
                        if(owner != null){
                            owner.id = xmlPullParser.getText();
                        }else if(initiator != null){
                            initiator.id = xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("DisplayName")){
                        xmlPullParser.next();
                        if(owner != null){
                            owner.disPlayName = xmlPullParser.getText();
                        }else if(initiator != null){
                            initiator.disPlayName= xmlPullParser.getText();
                        }
                    }else if(tagName.equalsIgnoreCase("PartNumberMarker")){
                        xmlPullParser.next();
                        result.partNumberMarker = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("StorageClass")){
                        xmlPullParser.next();
                        result.storageClass = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("NextPartNumberMarker")){
                        xmlPullParser.next();
                        result.nextPartNumberMarker = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("MaxParts")){
                        xmlPullParser.next();
                        result.maxParts = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("IsTruncated")){
                        xmlPullParser.next();
                        result.isTruncated = Boolean.parseBoolean(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("Part")){
                        part = new ListParts.Part();
                    }else if(tagName.equalsIgnoreCase("PartNumber")){
                        xmlPullParser.next();
                        part.partNumber = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("LastModified")){
                        xmlPullParser.next();
                        part.lastModified = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("ETag")){
                        xmlPullParser.next();
                        part.eTag = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Size")){
                        xmlPullParser.next();
                        part.size = xmlPullParser.getText();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Owner")){
                        result.owner = owner;
                        owner = null;
                    }else if(tagName.equalsIgnoreCase("Initiator")){
                        result.initiator = initiator;
                        initiator = null;
                    }else if(tagName.equalsIgnoreCase("Part")){
                        result.parts.add(part);
                        part = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 使用表单请求上传对象的响应
     * @param inputStream xml输入流
     * @param result 使用表单请求上传对象的响应
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parsePostResponseResult(InputStream inputStream, PostResponse result) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Location")){
                        xmlPullParser.next();
                        result.location = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Bucket")){
                        xmlPullParser.next();
                        result.bucket = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Key")){
                        xmlPullParser.next();
                        result.key = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("ETag")){
                        xmlPullParser.next();
                        result.eTag = xmlPullParser.getText();
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    /**
     * 解析 复制对象结果
     * @param inputStream xml输入流
     * @param result 复制对象结果
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseCopyObjectResult(InputStream inputStream, CopyObject result) throws XmlPullParserException, IOException {
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
     * 解析 COS错误信息
     * @param inputStream xml输入流
     * @param error COS错误信息
     * @throws XmlPullParserException xml解析异常
     * @throws IOException IO异常
     */
    public static void parseError(InputStream inputStream, CosError error) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("Code")){
                        xmlPullParser.next();
                        error.code = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Message")){
                        xmlPullParser.next();
                        error.message= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("Resource")){
                        xmlPullParser.next();
                        error.resource= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("RequestId")){
                    xmlPullParser.next();
                    error.requestId= xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("TraceId")){
                        xmlPullParser.next();
                        error.traceId= xmlPullParser.getText();
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }
}
