package com.tencent.cos.xml.model.ci;

import com.tencent.qcloud.qcloudxml.core.ChildElementBinder;
import com.tencent.qcloud.qcloudxml.core.IXmlAdapter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class QRCodeUploadResult$PicUploadResult$$XmlAdapter implements IXmlAdapter<QRCodeUploadResult.PicUploadResult> {
  private HashMap<String, ChildElementBinder<QRCodeUploadResult.PicUploadResult>> childElementBinders = new HashMap<>();

  public QRCodeUploadResult$PicUploadResult$$XmlAdapter() {
    childElementBinders.put("OriginalInfo", new ChildElementBinder<QRCodeUploadResult.PicUploadResult>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, QRCodeUploadResult.PicUploadResult value)
          throws IOException, XmlPullParserException {
        value.originalInfo = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.pic.PicOriginalInfo.class);
      }
    });
    childElementBinders.put("ProcessResults", new ChildElementBinder<QRCodeUploadResult.PicUploadResult>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, QRCodeUploadResult.PicUploadResult value)
          throws IOException, XmlPullParserException {
        if (value.processResults == null) {
          value.processResults = new java.util.ArrayList<com.tencent.cos.xml.model.ci.QRCodeUploadResult.PicObject>();
        }
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
          switch (eventType) {
            case XmlPullParser.START_TAG: {
              value.processResults.add(com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.ci.QRCodeUploadResult.PicObject.class));
            }
            break;
            case XmlPullParser.END_TAG: {
              if("ProcessResults".equalsIgnoreCase(xmlPullParser.getName())) {
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
  public QRCodeUploadResult.PicUploadResult fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.ci.QRCodeUploadResult.PicUploadResult value = new com.tencent.cos.xml.model.ci.QRCodeUploadResult.PicUploadResult();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.ci.QRCodeUploadResult.PicUploadResult> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("UploadResult".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, QRCodeUploadResult.PicUploadResult value) throws
      IOException, XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "UploadResult");
    if (value.originalInfo != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.originalInfo);
    }
    xmlSerializer.startTag("", "ProcessResults");
    if (value.processResults != null) {
      for (int i =0; i<value.processResults.size(); i++) {
        com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.processResults.get(i));
      }
    }
    xmlSerializer.endTag("", "ProcessResults");
    xmlSerializer.endTag("", "UploadResult");
  }
}
