/*
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.

 * According to cos feature, we modify some class，comment, field name, etc.
 */

package com.tencent.cos.xml.model.tag.eventstreaming;

import android.util.Xml;

import com.tencent.cos.xml.exception.CosXmlServiceException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


public abstract class SelectObjectContentEventUnmarshaller {

    public static SelectObjectContentEvent unmarshalMessage(Message message) throws CosXmlServiceException {

        String messageType = getStringHeader(message, ":message-type");
        if ("error".equals(messageType)) {
            throw unmarshalErrorMessage(message);
        } else if ("event".equals(messageType)) {
            return unmarshalEventMessage(message);
        } else {
            throw new CosXmlServiceException("Service returned unknown message type: " + messageType);
        }
    }

    private static CosXmlServiceException unmarshalErrorMessage(Message message) throws CosXmlServiceException {

        String errorCode = getStringHeader(message, ":error-code");
        String errorMessage = getStringHeader(message, ":error-message");
        SelectObjectContentEventException exception =
                new SelectObjectContentEventException("S3 returned an error: " + errorMessage + " (" + errorCode  + ")");

        exception.setErrorCode(errorCode);
        exception.setErrorMessage(errorMessage);
        return new CosXmlServiceException("Select object content error event", exception);
    }

    private static SelectObjectContentEvent unmarshalEventMessage(Message message) throws CosXmlServiceException {
        String eventType = getStringHeader(message, ":event-type");
        try {
            return SelectObjectContentEventUnmarshaller.forEventType(eventType).unmarshal(message);
        } catch (Exception e) {
            throw new CosXmlServiceException("Failed to read response event of type " + eventType, e);
        }
    }

    public static SelectObjectContentEventUnmarshaller forEventType(String eventType) {
        if ("Records".equals(eventType)) {
            return new RecordsEventUnmarshaller();
        }
        if ("Stats".equals(eventType)) {
            return new StatsEventUnmarshaller();
        }
        if ("Progress".equals(eventType)) {
            return new ProgressEventUnmarshaller();
        }
        if ("Cont".equals(eventType)) {
            return new ContinuationEventUnmarshaller();
        }
        if ("End".equals(eventType)) {
            return new EndEventUnmarshaller();
        }
        return new UnknownEventUnmarshaller();
    }

    private static String getStringHeader(Message message, String headerName) throws CosXmlServiceException {
        HeaderValue header = message.getHeaders().get(headerName);
        if (header == null) {
            throw new CosXmlServiceException("Unexpected lack of '" + headerName + "' header from service.");
        }
        if (header.getType() != HeaderType.STRING) {
            throw new CosXmlServiceException("Unexpected non-string '" + headerName + "' header: " + header.getType());
        }
        return header.getString();
    }

    public abstract SelectObjectContentEvent unmarshal(Message message) throws Exception;

    public static class RecordsEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
        @Override
        public SelectObjectContentEvent.RecordsEvent unmarshal(Message message) {
            return new SelectObjectContentEvent.RecordsEvent().withPayload(ByteBuffer.wrap(message.getPayload()));
        }
    }

    public static class StatsEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
        @Override
        public SelectObjectContentEvent.StatsEvent unmarshal(Message message) throws Exception {
//            StaxUnmarshallerContext context = payloadUnmarshaller(message);
//            return new SelectObjectContentEvent.StatsEvent().withDetails(StatsStaxUnmarshaller.getInstance().unmarshall(context));
            Stats stats = SelectObjectContentEventUnmarshaller.parsePayloadStats(message);
            return new SelectObjectContentEvent.StatsEvent().withDetails(stats);
        }
    }

    public static class ProgressEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
        @Override
        public SelectObjectContentEvent.ProgressEvent unmarshal(Message message) throws Exception {
//            StaxUnmarshallerContext context = payloadUnmarshaller(message);
//            return new SelectObjectContentEvent.ProgressEvent().withDetails(ProgressStaxUnmarshaller.getInstance().unmarshall(context));
            Progress progress = SelectObjectContentEventUnmarshaller.parsePayloadProgress(message);
            return new SelectObjectContentEvent.ProgressEvent().withDetails(progress);
        }
    }

    public static class ContinuationEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
        @Override
        public SelectObjectContentEvent.ContinuationEvent unmarshal(Message message) {
            return new SelectObjectContentEvent.ContinuationEvent();
        }
    }

    public static class EndEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
        @Override
        public SelectObjectContentEvent.EndEvent unmarshal(Message message) {
            return new SelectObjectContentEvent.EndEvent();
        }
    }

    public static class UnknownEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
        @Override
        public SelectObjectContentEvent unmarshal(Message message) {
            return new SelectObjectContentEvent();
        }
    }

//    private static StaxUnmarshallerContext payloadUnmarshaller(Message message) throws XMLStreamException {
//        InputStream payloadStream = new ByteArrayInputStream(message.getPayload());
//
//
//        XMLEventReader xmlEventReader = XmlUtils.getXmlInputFactory().createXMLEventReader(payloadStream);
//        return new StaxUnmarshallerContext(xmlEventReader);
//    }

    private static Stats parsePayloadStats(Message message) throws XmlPullParserException, IOException {

        long[] progress = parsePayloadBytesProgress(message);
        return new Stats(progress[0], progress[1], progress[2]);
    }

    private static Progress parsePayloadProgress(Message message) throws XmlPullParserException, IOException  {

        long[] progress = parsePayloadBytesProgress(message);
        return new Progress(progress[0], progress[1], progress[2]);
    }

    private static long[] parsePayloadBytesProgress(Message message) throws XmlPullParserException, IOException  {

        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(new ByteArrayInputStream(message.getPayload()), "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        long bytesScanned = 0;
        long bytesProcessed = 0;
        long bytesReturned = 0;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("BytesScanned")){
                        xmlPullParser.next();
                        bytesScanned = Long.parseLong(xmlPullParser.getText());
                    } else if(tagName.equalsIgnoreCase("BytesProcessed")){
                        xmlPullParser.next();
                        bytesProcessed = Long.parseLong(xmlPullParser.getText());
                    } else if(tagName.equalsIgnoreCase("BytesReturned")){
                        xmlPullParser.next();
                        bytesReturned = Long.parseLong(xmlPullParser.getText());
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }
        return new long[]{ bytesScanned, bytesProcessed, bytesReturned };
    }
}
