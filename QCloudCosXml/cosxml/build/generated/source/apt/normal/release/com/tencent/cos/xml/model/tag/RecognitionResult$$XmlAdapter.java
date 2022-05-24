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

public class RecognitionResult$$XmlAdapter implements IXmlAdapter<RecognitionResult> {
  private HashMap<String, ChildElementBinder<RecognitionResult>> childElementBinders = new HashMap<>();

  public RecognitionResult$$XmlAdapter() {
    childElementBinders.put("PornInfo", new ChildElementBinder<RecognitionResult>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult value) throws IOException,
          XmlPullParserException {
        value.pornInfo = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.RecognitionResult.PornInfo.class);
      }
    });
    childElementBinders.put("PoliticsInfo", new ChildElementBinder<RecognitionResult>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult value) throws IOException,
          XmlPullParserException {
        value.politicsInfo = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.RecognitionResult.PoliticsInfo.class);
      }
    });
    childElementBinders.put("TerroristInfo", new ChildElementBinder<RecognitionResult>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult value) throws IOException,
          XmlPullParserException {
        value.terroristInfo = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.RecognitionResult.TerroristInfo.class);
      }
    });
    childElementBinders.put("AdsInfo", new ChildElementBinder<RecognitionResult>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult value) throws IOException,
          XmlPullParserException {
        value.adsInfo = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.RecognitionResult.AdsInfo.class);
      }
    });
  }

  @Override
  public RecognitionResult fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.RecognitionResult value = new com.tencent.cos.xml.model.tag.RecognitionResult();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.RecognitionResult> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("RecognitionResult".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, RecognitionResult value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "RecognitionResult");
    if (value.pornInfo != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.pornInfo);
    }
    if (value.politicsInfo != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.politicsInfo);
    }
    if (value.terroristInfo != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.terroristInfo);
    }
    if (value.adsInfo != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.adsInfo);
    }
    xmlSerializer.endTag("", "RecognitionResult");
  }
}
