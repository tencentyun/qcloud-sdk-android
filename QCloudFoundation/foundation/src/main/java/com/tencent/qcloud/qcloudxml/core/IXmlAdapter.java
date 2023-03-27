package com.tencent.qcloud.qcloudxml.core;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * XML适配器（用于解析与编码）
 */
public abstract class IXmlAdapter<T> {
    public T fromXml(XmlPullParser xmlPullParser, String elementName) throws XmlPullParserException, IOException{
        throw new IllegalStateException("The fromXml method is not implemented");
    }

    public void toXml(XmlSerializer xmlSerializer, T value, String elementName) throws XmlPullParserException, IOException{
        throw new IllegalStateException("The toXml method is not implemented");
    }
}
