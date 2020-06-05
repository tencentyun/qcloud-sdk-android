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
 * Created by bradyxiao on 2017/11/24.
 */

public class XmlSlimParser {

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
