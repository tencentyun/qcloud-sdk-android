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

public class Tagging$Tag$$XmlAdapter implements IXmlAdapter<Tagging.Tag> {
  private HashMap<String, ChildElementBinder<Tagging.Tag>> childElementBinders = new HashMap<>();

  public Tagging$Tag$$XmlAdapter() {
    childElementBinders.put("Key", new ChildElementBinder<Tagging.Tag>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, Tagging.Tag value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.key = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Value", new ChildElementBinder<Tagging.Tag>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, Tagging.Tag value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.value = xmlPullParser.getText();
      }
    });
  }

  @Override
  public Tagging.Tag fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.Tagging.Tag value = new com.tencent.cos.xml.model.tag.Tagging.Tag();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.Tagging.Tag> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("Tag".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, Tagging.Tag value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "Tag");
    xmlSerializer.startTag("", "Key");
    xmlSerializer.text(String.valueOf(value.key));
    xmlSerializer.endTag("", "Key");
    xmlSerializer.startTag("", "Value");
    xmlSerializer.text(String.valueOf(value.value));
    xmlSerializer.endTag("", "Value");
    xmlSerializer.endTag("", "Tag");
  }
}
