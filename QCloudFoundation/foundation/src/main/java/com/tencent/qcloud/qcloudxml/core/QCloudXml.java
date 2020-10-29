package com.tencent.qcloud.qcloudxml.core;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * XML解析与编码
 */
public final class QCloudXml {
    static final Map<Class<?>, IXmlAdapter<?>> XML_ADAPTERS = new HashMap<>();

    public static <T> T fromXml(InputStream inputStream, Class<T> clazz) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        return fromXml(xmlPullParser, clazz);
    }

    public static <T> T fromXml(XmlPullParser xmlPullParser, Class<T> clazz) throws XmlPullParserException, IOException {
        IXmlAdapter<T> adapter = createXmlAdapter(clazz);
        return adapter.fromXml(xmlPullParser);
    }

    public static <T> String toXml(T value) throws XmlPullParserException, IOException{
        if (value == null) return null;

        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startDocument("UTF-8", null);

        toXml(xmlSerializer, value);

        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    public static <T> void toXml(XmlSerializer xmlSerializer, T value) throws XmlPullParserException, IOException{
        Class<?> clazz = value.getClass();
        IXmlAdapter<T> adapter = createXmlAdapter(clazz);
        adapter.toXml(xmlSerializer, value);
    }

    private static <T> IXmlAdapter<T> createXmlAdapter(Class<?> targetClass) {
        IXmlAdapter<T> adapter = (IXmlAdapter<T>) XML_ADAPTERS.get(targetClass);
        if (adapter != null) {
            return adapter;
        }

        String targetClassName = targetClass.getName();
        try {
            Class<IXmlAdapter<T>> adapterClass = (Class<IXmlAdapter<T>>) Class.forName(targetClassName + "$$XmlAdapter");
            adapter = adapterClass.newInstance();
            XML_ADAPTERS.put(targetClass, adapter);
            return adapter;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("No IXmlAdapter for class "
                    + targetClassName
                    + " found. Expected name of the xml adapter is "
                    + targetClassName + "$$XmlAdapter", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("No IXmlAdapter for class "
                    + targetClassName
                    + " found. Expected name of the xml adapter is "
                    + targetClassName + "$$XmlAdapter", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No IXmlAdapter for class "
                    + targetClassName
                    + " found. Expected name of the xml adapter is "
                    + targetClassName + "$$XmlAdapter", e);
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
