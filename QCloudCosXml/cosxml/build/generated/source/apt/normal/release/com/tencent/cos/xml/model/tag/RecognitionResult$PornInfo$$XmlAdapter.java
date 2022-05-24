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

public class RecognitionResult$PornInfo$$XmlAdapter implements IXmlAdapter<RecognitionResult.PornInfo> {
  private HashMap<String, ChildElementBinder<RecognitionResult.PornInfo>> childElementBinders = new HashMap<>();

  public RecognitionResult$PornInfo$$XmlAdapter() {
    childElementBinders.put("Code", new ChildElementBinder<RecognitionResult.PornInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.PornInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.code = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Msg", new ChildElementBinder<RecognitionResult.PornInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.PornInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.msg = xmlPullParser.getText();
      }
    });
    childElementBinders.put("HitFlag", new ChildElementBinder<RecognitionResult.PornInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.PornInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.hitFlag = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Score", new ChildElementBinder<RecognitionResult.PornInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.PornInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.score = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Label", new ChildElementBinder<RecognitionResult.PornInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, RecognitionResult.PornInfo value) throws
          IOException, XmlPullParserException {
        xmlPullParser.next();
        value.label = xmlPullParser.getText();
      }
    });
  }

  @Override
  public RecognitionResult.PornInfo fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.RecognitionResult.PornInfo value = new com.tencent.cos.xml.model.tag.RecognitionResult.PornInfo();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.RecognitionResult.PornInfo> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("PornInfo".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, RecognitionResult.PornInfo value) throws
      IOException, XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "PornInfo");
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
    xmlSerializer.endTag("", "PornInfo");
  }
}
