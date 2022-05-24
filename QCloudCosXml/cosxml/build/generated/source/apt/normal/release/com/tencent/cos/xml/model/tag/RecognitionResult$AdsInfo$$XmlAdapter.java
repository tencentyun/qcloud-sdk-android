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

public class RecognitionResult$AdsInfo$$XmlAdapter implements IXmlAdapter<RecognitionResult.AdsInfo> {
  private HashMap<String, ChildElementBinder<RecognitionResult.AdsInfo>> childElementBinders = new HashMap<>();

  public RecognitionResult$AdsInfo$$XmlAdapter() {
    childElementBinders.put("Code", new ChildElementBinder<RecognitionResult.AdsInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.AdsInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.code = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Msg", new ChildElementBinder<RecognitionResult.AdsInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.AdsInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.msg = xmlPullParser.getText();
      }
    });
    childElementBinders.put("HitFlag", new ChildElementBinder<RecognitionResult.AdsInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.AdsInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.hitFlag = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Score", new ChildElementBinder<RecognitionResult.AdsInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.AdsInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.score = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Label", new ChildElementBinder<RecognitionResult.AdsInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.AdsInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.label = xmlPullParser.getText();
      }
    });
  }

  @Override
  public RecognitionResult.AdsInfo fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.RecognitionResult.AdsInfo value = new com.tencent.cos.xml.model.tag.RecognitionResult.AdsInfo();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.RecognitionResult.AdsInfo> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("AdsInfo".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, RecognitionResult.AdsInfo value) throws
      IOException, XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "AdsInfo");
    xmlSerializer.startTag("", "Code");
    xmlSerializer.text(String.valueOf(value.code));
    xmlSerializer.endTag("", "Code");
    xmlSerializer.startTag("", "Msg");
    xmlSerializer.text(String.valueOf(value.msg));
    xmlSerializer.endTag("", "Msg");
    xmlSerializer.startTag("", "HitFlag");
    xmlSerializer.text(String.valueOf(value.hitFlag));
    xmlSerializer.endTag("", "HitFlag");
    xmlSerializer.startTag("", "Score");
    xmlSerializer.text(String.valueOf(value.score));
    xmlSerializer.endTag("", "Score");
    xmlSerializer.startTag("", "Label");
    xmlSerializer.text(String.valueOf(value.label));
    xmlSerializer.endTag("", "Label");
    xmlSerializer.endTag("", "AdsInfo");
  }
}
