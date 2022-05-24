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

public class ImageInfo$$XmlAdapter implements IXmlAdapter<ImageInfo> {
  private HashMap<String, ChildElementBinder<ImageInfo>> childElementBinders = new HashMap<>();

  public ImageInfo$$XmlAdapter() {
    childElementBinders.put("Format", new ChildElementBinder<ImageInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ImageInfo value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.format = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Width", new ChildElementBinder<ImageInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ImageInfo value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.width = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Height", new ChildElementBinder<ImageInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ImageInfo value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.height = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Quality", new ChildElementBinder<ImageInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ImageInfo value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.quality = Integer.parseInt(xmlPullParser.getText());
      }
    });
    childElementBinders.put("Ave", new ChildElementBinder<ImageInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ImageInfo value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.ave = xmlPullParser.getText();
      }
    });
    childElementBinders.put("Orientation", new ChildElementBinder<ImageInfo>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, ImageInfo value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.orientation = Integer.parseInt(xmlPullParser.getText());
      }
    });
  }

  @Override
  public ImageInfo fromXml(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
    com.tencent.cos.xml.model.tag.pic.ImageInfo value = new com.tencent.cos.xml.model.tag.pic.ImageInfo();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.pic.ImageInfo> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("ImageInfo".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, ImageInfo value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "ImageInfo");
    xmlSerializer.startTag("", "Format");
    xmlSerializer.text(String.valueOf(value.format));
    xmlSerializer.endTag("", "Format");
    xmlSerializer.startTag("", "Width");
    xmlSerializer.text(String.valueOf(value.width));
    xmlSerializer.endTag("", "Width");
    xmlSerializer.startTag("", "Height");
    xmlSerializer.text(String.valueOf(value.height));
    xmlSerializer.endTag("", "Height");
    xmlSerializer.startTag("", "Quality");
    xmlSerializer.text(String.valueOf(value.quality));
    xmlSerializer.endTag("", "Quality");
    xmlSerializer.startTag("", "Ave");
    xmlSerializer.text(String.valueOf(value.ave));
    xmlSerializer.endTag("", "Ave");
    xmlSerializer.startTag("", "Orientation");
    xmlSerializer.text(String.valueOf(value.orientation));
    xmlSerializer.endTag("", "Orientation");
    xmlSerializer.endTag("", "ImageInfo");
  }
}
