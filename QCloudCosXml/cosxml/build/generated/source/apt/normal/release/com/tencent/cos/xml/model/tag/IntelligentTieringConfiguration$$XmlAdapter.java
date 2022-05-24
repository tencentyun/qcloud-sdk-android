package com.tencent.cos.xml.model.tag;

import com.tencent.qcloud.qcloudxml.core.ChildElementBinder;
import com.tencent.qcloud.qcloudxml.core.IXmlAdapter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class IntelligentTieringConfiguration$$XmlAdapter implements IXmlAdapter<IntelligentTieringConfiguration> {
  private HashMap<String, ChildElementBinder<IntelligentTieringConfiguration>> childElementBinders = new HashMap<>();

  public IntelligentTieringConfiguration$$XmlAdapter() {
    childElementBinders.put("Status", new ChildElementBinder<IntelligentTieringConfiguration>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, IntelligentTieringConfiguration value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.status = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Transition", new ChildElementBinder<IntelligentTieringConfiguration>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, IntelligentTieringConfiguration value) throws
          IOException, XmlPullParserException {
        value.transition = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration.Transition.class);
      }
    });
  }

  @Override
  public IntelligentTieringConfiguration fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration value = new com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("IntelligentTieringConfiguration".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, IntelligentTieringConfiguration value) throws
      IOException, XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "IntelligentTieringConfiguration");
    xmlSerializer.startTag("", "Status");
    xmlSerializer.text(String.valueOf(value.status));
    xmlSerializer.endTag("", "Status");
    if (value.transition != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.transition);
    }
    xmlSerializer.endTag("", "IntelligentTieringConfiguration");
  }
}
