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

import android.util.Xml;

import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.ListMultipartUploads;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 查询存储桶（Bucket）中正在进行中的分块上传对象的返回结果.
 * @see com.tencent.cos.xml.CosXml#listMultiUploads(ListMultiUploadsRequest)
 * @see ListMultiUploadsRequest
 */
final public class ListMultiUploadsResult extends CosXmlResult {
    /**
     * 所有分块上传的信息
     */
    public ListMultipartUploads listMultipartUploads = new ListMultipartUploads();

    @Override
    protected void xmlParser(HttpResponse response) throws XmlPullParserException, IOException{
        parseListMultipartUploadsResult(response.byteStream(), listMultipartUploads);
    }

    @Override
    public String printResult() {
        if(listMultipartUploads != null){
            return listMultipartUploads.toString();
        }else{
            return super.printResult();
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
}
