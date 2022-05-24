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

public class Tagging$$XmlAdapter implements IXmlAdapter<Tagging> {
  private HashMap<String, ChildElementBinder<Tagging>> childElementBinders = new HashMap<>();

  public Tagging$$XmlAdapter() {
    childElementBinders.put("TagSet", new ChildElementBinder<Tagging>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, Tagging value) throws IOException,
          XmlPullParserException {
        value.tagSet = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.Tagging.TagSet.class);
      }
    });
  }

  @Override
  public Tagging fromXml(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
    com.tencent.cos.xml.model.tag.Tagging value = new com.tencent.cos.xml.model.tag.Tagging();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.Tagging> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("Tagging".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, Tagging value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "Tagging");
    if (value.tagSet != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.tagSet);
    }
    xmlSerializer.endTag("", "Tagging");
  }
}
