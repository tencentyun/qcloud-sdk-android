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

public class ListAllMyBuckets$Owner$$XmlAdapter implements IXmlAdapter<ListAllMyBuckets.Owner> {
  private HashMap<String, ChildElementBinder<ListAllMyBuckets.Owner>> childElementBinders = new HashMap<>();

  public ListAllMyBuckets$Owner$$XmlAdapter() {
    childElementBinders.put("ID", new ChildElementBinder<ListAllMyBuckets.Owner>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ListAllMyBuckets.Owner value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.id = xmlPullParser.getText();
      }
    });
    childElementBinders.put("DisplayName", new ChildElementBinder<ListAllMyBuckets.Owner>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ListAllMyBuckets.Owner value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.disPlayName = xmlPullParser.getText();
      }
    });
  }

  @Override
  public ListAllMyBuckets.Owner fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.ListAllMyBuckets.Owner value = new com.tencent.cos.xml.model.tag.ListAllMyBuckets.Owner();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.ListAllMyBuckets.Owner> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("Owner".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, ListAllMyBuckets.Owner value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "Owner");
    xmlSerializer.startTag("", "ID");
    xmlSerializer.text(String.valueOf(value.id));
    xmlSerializer.endTag("", "ID");
    xmlSerializer.startTag("", "DisplayName");
    xmlSerializer.text(String.valueOf(value.disPlayName));
    xmlSerializer.endTag("", "DisplayName");
    xmlSerializer.endTag("", "Owner");
  }
}
