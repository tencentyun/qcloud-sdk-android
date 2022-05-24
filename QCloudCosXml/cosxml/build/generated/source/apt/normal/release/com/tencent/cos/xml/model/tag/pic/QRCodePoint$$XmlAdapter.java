package com.tencent.cos.xml.model.tag.pic;

import com.tencent.qcloud.qcloudxml.core.ChildElementBinder;
import com.tencent.qcloud.qcloudxml.core.IXmlAdapter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class QRCodePoint$$XmlAdapter implements IXmlAdapter<QRCodePoint> {
  private HashMap<String, ChildElementBinder<QRCodePoint>> childElementBinders = new HashMap<>();

  public QRCodePoint$$XmlAdapter() {
    childElementBinders.put("Point", new ChildElementBinder<QRCodePoint>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, QRCodePoint value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.point = xmlPullParser.getText();
      }
    });
  }

  @Override
  public QRCodePoint fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.pic.QRCodePoint value = new com.tencent.cos.xml.model.tag.pic.QRCodePoint();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.pic.QRCodePoint> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("Point".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, QRCodePoint value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "Point");
    xmlSerializer.startTag("", "Point");
    xmlSerializer.text(String.valueOf(value.point));
    xmlSerializer.endTag("", "Point");
    xmlSerializer.endTag("", "Point");
  }
}
