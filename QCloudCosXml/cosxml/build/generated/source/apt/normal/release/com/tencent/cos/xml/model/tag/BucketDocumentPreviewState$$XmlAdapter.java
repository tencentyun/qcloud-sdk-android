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

public class BucketDocumentPreviewState$$XmlAdapter implements IXmlAdapter<BucketDocumentPreviewState> {
  private HashMap<String, ChildElementBinder<BucketDocumentPreviewState>> childElementBinders = new HashMap<>();

  public BucketDocumentPreviewState$$XmlAdapter() {
    childElementBinders.put("Name", new ChildElementBinder<BucketDocumentPreviewState>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, BucketDocumentPreviewState value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.Name = xmlPullParser.getText();
      }
    });
    childElementBinders.put("CreateTime", new ChildElementBinder<BucketDocumentPreviewState>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, BucketDocumentPreviewState value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.CreateTime = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Region", new ChildElementBinder<BucketDocumentPreviewState>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, BucketDocumentPreviewState value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.Region = xmlPullParser.getText();
      }
    });
    childElementBinders.put("BucketId", new ChildElementBinder<BucketDocumentPreviewState>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, BucketDocumentPreviewState value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.BucketId = xmlPullParser.getText();
      }
    });
  }

  @Override
  public BucketDocumentPreviewState fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.BucketDocumentPreviewState value = new com.tencent.cos.xml.model.tag.BucketDocumentPreviewState();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.BucketDocumentPreviewState> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("BucketDocumentPreviewState".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, BucketDocumentPreviewState value) throws
      IOException, XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "BucketDocumentPreviewState");
    xmlSerializer.startTag("", "Name");
    xmlSerializer.text(String.valueOf(value.Name));
    xmlSerializer.endTag("", "Name");
    xmlSerializer.startTag("", "CreateTime");
    xmlSerializer.text(String.valueOf(value.CreateTime));
    xmlSerializer.endTag("", "CreateTime");
    xmlSerializer.startTag("", "Region");
    xmlSerializer.text(String.valueOf(value.Region));
    xmlSerializer.endTag("", "Region");
    xmlSerializer.startTag("", "BucketId");
    xmlSerializer.text(String.valueOf(value.BucketId));
    xmlSerializer.endTag("", "BucketId");
    xmlSerializer.endTag("", "BucketDocumentPreviewState");
  }
}
