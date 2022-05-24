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

public class RecognitionResult$PoliticsInfo$$XmlAdapter implements IXmlAdapter<RecognitionResult.PoliticsInfo> {
  private HashMap<String, ChildElementBinder<RecognitionResult.PoliticsInfo>> childElementBinders = new HashMap<>();

  public RecognitionResult$PoliticsInfo$$XmlAdapter() {
    childElementBinders.put("Code", new ChildElementBinder<RecognitionResult.PoliticsInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.PoliticsInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.code = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Msg", new ChildElementBinder<RecognitionResult.PoliticsInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.PoliticsInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.msg = xmlPullParser.getText();
      }
    });
    childElementBinders.put("HitFlag", new ChildElementBinder<RecognitionResult.PoliticsInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.PoliticsInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.hitFlag = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Score", new ChildElementBinder<RecognitionResult.PoliticsInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.PoliticsInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.score = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Label", new ChildElementBinder<RecognitionResult.PoliticsInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.PoliticsInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.label = xmlPullParser.getText();
      }
    });
  }

  @Override
  public RecognitionResult.PoliticsInfo fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.RecognitionResult.PoliticsInfo value = new com.tencent.cos.xml.model.tag.RecognitionResult.PoliticsInfo();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.RecognitionResult.PoliticsInfo> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("PoliticsInfo".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, RecognitionResult.PoliticsInfo value) throws
      IOException, XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "PoliticsInfo");
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
    xmlSerializer.endTag("", "PoliticsInfo");
  }
}
