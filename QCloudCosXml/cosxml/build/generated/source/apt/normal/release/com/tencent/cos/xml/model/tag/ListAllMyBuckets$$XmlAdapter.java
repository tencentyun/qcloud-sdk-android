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

public class ListAllMyBuckets$$XmlAdapter implements IXmlAdapter<ListAllMyBuckets> {
  private HashMap<String, ChildElementBinder<ListAllMyBuckets>> childElementBinders = new HashMap<>();

  public ListAllMyBuckets$$XmlAdapter() {
    childElementBinders.put("Owner", new ChildElementBinder<ListAllMyBuckets>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ListAllMyBuckets value) throws IOException,
          XmlPullParserException {
        value.owner = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.ListAllMyBuckets.Owner.class);
      }
    });
    childElementBinders.put("Buckets", new ChildElementBinder<ListAllMyBuckets>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ListAllMyBuckets value) throws IOException,
          XmlPullParserException {
        if (value.buckets == null) {
          value.buckets = new java.util.ArrayList<com.tencent.cos.xml.model.tag.ListAllMyBuckets.Bucket>();
        }
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
          switch (eventType) {
            case XmlPullParser.START_TAG: {
              value.buckets.add(com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.ListAllMyBuckets.Bucket.class));
            }
            break;
            case XmlPullParser.END_TAG: {
              if("Buckets".equalsIgnoreCase(xmlPullParser.getName())) {
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
  public ListAllMyBuckets fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.ListAllMyBuckets value = new com.tencent.cos.xml.model.tag.ListAllMyBuckets();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.ListAllMyBuckets> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("ListAllMyBucketsResult".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, ListAllMyBuckets value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "ListAllMyBucketsResult");
    if (value.owner != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.owner);
    }
    xmlSerializer.startTag("", "Buckets");
    if (value.buckets != null) {
      for (int i =0; i<value.buckets.size(); i++) {
        com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.buckets.get(i));
      }
    }
    xmlSerializer.endTag("", "Buckets");
    xmlSerializer.endTag("", "ListAllMyBucketsResult");
  }
}
