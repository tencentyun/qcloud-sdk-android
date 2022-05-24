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

public class PicOriginalInfo$$XmlAdapter implements IXmlAdapter<PicOriginalInfo> {
  private HashMap<String, ChildElementBinder<PicOriginalInfo>> childElementBinders = new HashMap<>();

  public PicOriginalInfo$$XmlAdapter() {
    childElementBinders.put("Key", new ChildElementBinder<PicOriginalInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicOriginalInfo value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.key = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Location", new ChildElementBinder<PicOriginalInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicOriginalInfo value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.location = xmlPullParser.getText();
      }
    });
    childElementBinders.put("ETag", new ChildElementBinder<PicOriginalInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicOriginalInfo value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.etag = xmlPullParser.getText();
      }
    });
    childElementBinders.put("ImageInfo", new ChildElementBinder<PicOriginalInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicOriginalInfo value) throws IOException,
          XmlPullParserException {
        value.imageInfo = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.pic.ImageInfo.class);
      }
    });
  }

  @Override
  public PicOriginalInfo fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.pic.PicOriginalInfo value = new com.tencent.cos.xml.model.tag.pic.PicOriginalInfo();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.pic.PicOriginalInfo> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("OriginalInfo".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, PicOriginalInfo value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "OriginalInfo");
    xmlSerializer.startTag("", "Key");
    xmlSerializer.text(String.valueOf(value.key));
    xmlSerializer.endTag("", "Key");
    xmlSerializer.startTag("", "Location");
    xmlSerializer.text(String.valueOf(value.location));
    xmlSerializer.endTag("", "Location");
    xmlSerializer.startTag("", "ETag");
    xmlSerializer.text(String.valueOf(value.etag));
    xmlSerializer.endTag("", "ETag");
    if (value.imageInfo != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.imageInfo);
    }
    xmlSerializer.endTag("", "OriginalInfo");
  }
}
