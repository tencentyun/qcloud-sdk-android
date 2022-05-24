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

public class ListAllMyBuckets$Bucket$$XmlAdapter implements IXmlAdapter<ListAllMyBuckets.Bucket> {
  private HashMap<String, ChildElementBinder<ListAllMyBuckets.Bucket>> childElementBinders = new HashMap<>();

  public ListAllMyBuckets$Bucket$$XmlAdapter() {
    childElementBinders.put("Name", new ChildElementBinder<ListAllMyBuckets.Bucket>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ListAllMyBuckets.Bucket value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.name = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Location", new ChildElementBinder<ListAllMyBuckets.Bucket>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ListAllMyBuckets.Bucket value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.location = xmlPullParser.getText();
      }
    });
    childElementBinders.put("CreationDate", new ChildElementBinder<ListAllMyBuckets.Bucket>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ListAllMyBuckets.Bucket value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.createDate = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Type", new ChildElementBinder<ListAllMyBuckets.Bucket>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ListAllMyBuckets.Bucket value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.type = xmlPullParser.getText();
      }
    });
  }

  @Override
  public ListAllMyBuckets.Bucket fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.ListAllMyBuckets.Bucket value = new com.tencent.cos.xml.model.tag.ListAllMyBuckets.Bucket();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.ListAllMyBuckets.Bucket> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("Bucket".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, ListAllMyBuckets.Bucket value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "Bucket");
    xmlSerializer.startTag("", "Name");
    xmlSerializer.text(String.valueOf(value.name));
    xmlSerializer.endTag("", "Name");
    xmlSerializer.startTag("", "Location");
    xmlSerializer.text(String.valueOf(value.location));
    xmlSerializer.endTag("", "Location");
    xmlSerializer.startTag("", "CreationDate");
    xmlSerializer.text(String.valueOf(value.createDate));
    xmlSerializer.endTag("", "CreationDate");
    xmlSerializer.startTag("", "Type");
    xmlSerializer.text(String.valueOf(value.type));
    xmlSerializer.endTag("", "Type");
    xmlSerializer.endTag("", "Bucket");
  }
}
