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

public class QRCodeInfo$$XmlAdapter implements IXmlAdapter<QRCodeInfo> {
  private HashMap<String, ChildElementBinder<QRCodeInfo>> childElementBinders = new HashMap<>();

  public QRCodeInfo$$XmlAdapter() {
    childElementBinders.put("CodeUrl", new ChildElementBinder<QRCodeInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, QRCodeInfo value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.codeUrl = xmlPullParser.getText();
      }
    });
    childElementBinders.put("CodeLocation", new ChildElementBinder<QRCodeInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, QRCodeInfo value) throws IOException,
          XmlPullParserException {
        value.location = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.pic.QRCodeLocation.class);
      }
    });
  }

  @Override
  public QRCodeInfo fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.pic.QRCodeInfo value = new com.tencent.cos.xml.model.tag.pic.QRCodeInfo();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.pic.QRCodeInfo> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("QRcodeInfo".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, QRCodeInfo value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "QRcodeInfo");
    xmlSerializer.startTag("", "CodeUrl");
    xmlSerializer.text(String.valueOf(value.codeUrl));
    xmlSerializer.endTag("", "CodeUrl");
    if (value.location != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.location);
    }
    xmlSerializer.endTag("", "QRcodeInfo");
  }
}
