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

public class Tagging$TagSet$$XmlAdapter implements IXmlAdapter<Tagging.TagSet> {
  private HashMap<String, ChildElementBinder<Tagging.TagSet>> childElementBinders = new HashMap<>();

  public Tagging$TagSet$$XmlAdapter() {
    childElementBinders.put("Tags", new ChildElementBinder<Tagging.TagSet>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, Tagging.TagSet value) throws IOException,
          XmlPullParserException {
        if (value.tags == null) {
          value.tags = new java.util.ArrayList<com.tencent.cos.xml.model.tag.Tagging.Tag>();
        }
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
          switch (eventType) {
            case XmlPullParser.START_TAG: {
              value.tags.add(com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.Tagging.Tag.class));
            }
            break;
            case XmlPullParser.END_TAG: {
              if("Tags".equalsIgnoreCase(xmlPullParser.getName())) {
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
  public Tagging.TagSet fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.Tagging.TagSet value = new com.tencent.cos.xml.model.tag.Tagging.TagSet();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.Tagging.TagSet> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("TagSet".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, Tagging.TagSet value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "TagSet");
    if (value.tags != null) {
      for (int i =0; i<value.tags.size(); i++) {
        com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.tags.get(i));
      }
    }
    xmlSerializer.endTag("", "TagSet");
  }
}
