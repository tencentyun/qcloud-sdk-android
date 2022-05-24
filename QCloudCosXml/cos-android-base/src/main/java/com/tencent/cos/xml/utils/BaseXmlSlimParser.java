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

package com.tencent.cos.xml.utils;

import android.util.Xml;

import com.tencent.cos.xml.model.tag.CosError;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class BaseXmlSlimParser {
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
