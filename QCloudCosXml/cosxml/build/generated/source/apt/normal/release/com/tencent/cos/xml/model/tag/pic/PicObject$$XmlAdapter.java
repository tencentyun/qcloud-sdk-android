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

public class PicObject$$XmlAdapter implements IXmlAdapter<PicObject> {
  private HashMap<String, ChildElementBinder<PicObject>> childElementBinders = new HashMap<>();

  public PicObject$$XmlAdapter() {
    childElementBinders.put("Key", new ChildElementBinder<PicObject>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicObject value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.key = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Location", new ChildElementBinder<PicObject>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicObject value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.location = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Format", new ChildElementBinder<PicObject>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicObject value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.format = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Width", new ChildElementBinder<PicObject>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicObject value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.width = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Height", new ChildElementBinder<PicObject>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicObject value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.height = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Size", new ChildElementBinder<PicObject>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicObject value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.size = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Quality", new ChildElementBinder<PicObject>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicObject value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.quality = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("ETag", new ChildElementBinder<PicObject>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PicObject value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.etag = xmlPullParser.getText();
      }
    });
  }

  @Override
  public PicObject fromXml(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
    com.tencent.cos.xml.model.tag.pic.PicObject value = new com.tencent.cos.xml.model.tag.pic.PicObject();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.pic.PicObject> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("Object".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, PicObject value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "Object");
    xmlSerializer.startTag("", "Key");
    xmlSerializer.text(String.valueOf(value.key));
    xmlSerializer.endTag("", "Key");
    xmlSerializer.startTag("", "Location");
    xmlSerializer.text(String.valueOf(value.location));
    xmlSerializer.endTag("", "Location");
    xmlSerializer.startTag("", "Format");
    xmlSerializer.text(String.valueOf(value.format));
    xmlSerializer.endTag("", "Format");
    xmlSerializer.startTag("", "Width");
    xmlSerializer.text(String.valueOf(value.width));
    xmlSerializer.endTag("", "Width");
    xmlSerializer.startTag("", "Height");
    xmlSerializer.text(String.valueOf(value.height));
    xmlSerializer.endTag("", "Height");
    xmlSerializer.startTag("", "Size");
    xmlSerializer.text(String.valueOf(value.size));
    xmlSerializer.endTag("", "Size");
    xmlSerializer.startTag("", "Quality");
    xmlSerializer.text(String.valueOf(value.quality));
    xmlSerializer.endTag("", "Quality");
    xmlSerializer.startTag("", "ETag");
    xmlSerializer.text(String.valueOf(value.etag));
    xmlSerializer.endTag("", "ETag");
    xmlSerializer.endTag("", "Object");
  }
}
