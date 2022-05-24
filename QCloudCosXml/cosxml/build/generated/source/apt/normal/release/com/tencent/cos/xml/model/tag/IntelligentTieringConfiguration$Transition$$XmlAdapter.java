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

public class IntelligentTieringConfiguration$Transition$$XmlAdapter implements IXmlAdapter<IntelligentTieringConfiguration.Transition> {
  private HashMap<String, ChildElementBinder<IntelligentTieringConfiguration.Transition>> childElementBinders = new HashMap<>();

  public IntelligentTieringConfiguration$Transition$$XmlAdapter() {
    childElementBinders.put("Days", new ChildElementBinder<IntelligentTieringConfiguration.Transition>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser,
          IntelligentTieringConfiguration.Transition value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.days = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("RequestFrequent", new ChildElementBinder<IntelligentTieringConfiguration.Transition>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser,
          IntelligentTieringConfiguration.Transition value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.requestFrequent = Integer.parseInt(xmlPullParser.getText());
      }
    });
  }

  @Override
  public IntelligentTieringConfiguration.Transition fromXml(XmlPullParser xmlPullParser) throws
      IOException, XmlPullParserException {
    com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration.Transition value = new com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration.Transition();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration.Transition> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("Transition".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, IntelligentTieringConfiguration.Transition value)
      throws IOException, XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "Transition");
    xmlSerializer.startTag("", "Days");
    xmlSerializer.text(String.valueOf(value.days));
    xmlSerializer.endTag("", "Days");
    xmlSerializer.startTag("", "RequestFrequent");
    xmlSerializer.text(String.valueOf(value.requestFrequent));
    xmlSerializer.endTag("", "RequestFrequent");
    xmlSerializer.endTag("", "Transition");
  }
}
