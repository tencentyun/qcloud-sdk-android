package com.tencent.qcloud.qcloudxml.core;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * XML适配器（用于解析与编码）
 */
public interface IXmlAdapter<T> {
    T fromXml(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException;

    void toXml(XmlSerializer xmlSerializer, T value) throws XmlPullParserException, IOException;
}
