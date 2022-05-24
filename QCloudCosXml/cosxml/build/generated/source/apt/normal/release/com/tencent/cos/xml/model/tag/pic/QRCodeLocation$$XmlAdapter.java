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

public class QRCodeLocation$$XmlAdapter implements IXmlAdapter<QRCodeLocation> {
  private HashMap<String, ChildElementBinder<QRCodeLocation>> childElementBinders = new HashMap<>();

  public QRCodeLocation$$XmlAdapter() {
    childElementBinders.put("CodeLocation", new ChildElementBinder<QRCodeLocation>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, QRCodeLocation value) throws IOException,
          XmlPullParserException {
        if (value.points == null) {
          value.points = new java.util.ArrayList<com.tencent.cos.xml.model.tag.pic.QRCodePoint>();
        }
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
          switch (eventType) {
            case XmlPullParser.START_TAG: {
              value.points.add(com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.pic.QRCodePoint.class));
            }
            break;
            case XmlPullParser.END_TAG: {
              if("CodeLocation".equalsIgnoreCase(xmlPullParser.getName())) {
                return;
              }
            }
          }
          eventType = xmlPullParser.next();
        }
      }
    });
  }

  @Override
  public QRCodeLocation fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.pic.QRCodeLocation value = new com.tencent.cos.xml.model.tag.pic.QRCodeLocation();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.pic.QRCodeLocation> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("CodeLocation".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, QRCodeLocation value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "CodeLocation");
    xmlSerializer.startTag("", "CodeLocation");
    if (value.points != null) {
      for (int i =0; i<value.points.size(); i++) {
        com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.points.get(i));
      }
    }
    xmlSerializer.endTag("", "CodeLocation");
    xmlSerializer.endTag("", "CodeLocation");
  }
}
