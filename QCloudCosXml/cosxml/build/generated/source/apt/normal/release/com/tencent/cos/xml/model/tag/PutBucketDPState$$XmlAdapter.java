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

public class PutBucketDPState$$XmlAdapter implements IXmlAdapter<PutBucketDPState> {
  private HashMap<String, ChildElementBinder<PutBucketDPState>> childElementBinders = new HashMap<>();

  public PutBucketDPState$$XmlAdapter() {
    childElementBinders.put("RequestId", new ChildElementBinder<PutBucketDPState>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PutBucketDPState value) throws IOException,
          XmlPullParserException {
        xmlPullParser.next();
        value.RequestId = xmlPullParser.getText();
      }
    });
    childElementBinders.put("DocBucket", new ChildElementBinder<PutBucketDPState>() {
      @Override
      public void fromXml(XmlPullParser xmlPullParser, PutBucketDPState value) throws IOException,
          XmlPullParserException {
        value.DocBucket = com.tencent.qcloud.qcloudxml.core.QCloudXml.fromXml(xmlPullParser, com.tencent.cos.xml.model.tag.BucketDocumentPreviewState.class);
      }
    });
  }

  @Override
  public PutBucketDPState fromXml(XmlPullParser xmlPullParser) throws IOException,
      XmlPullParserException {
    com.tencent.cos.xml.model.tag.PutBucketDPState value = new com.tencent.cos.xml.model.tag.PutBucketDPState();
    int eventType = xmlPullParser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      switch (eventType) {
        case XmlPullParser.START_TAG: {
          String tagName = xmlPullParser.getName();
          ChildElementBinder<com.tencent.cos.xml.model.tag.PutBucketDPState> childElementBinder = childElementBinders.get(tagName);
          if (childElementBinder != null) {
            childElementBinder.fromXml(xmlPullParser, value);
          }
        }
        break;
        case XmlPullParser.END_TAG: {
          if("Response".equalsIgnoreCase(xmlPullParser.getName())) {
            return value;
          }
        }
      }
      eventType = xmlPullParser.next();
    }
    return value;
  }

  @Override
  public void toXml(XmlSerializer xmlSerializer, PutBucketDPState value) throws IOException,
      XmlPullParserException {
    if (value == null) {
      return;
    }
    xmlSerializer.startTag("", "Response");
    xmlSerializer.startTag("", "RequestId");
    xmlSerializer.text(String.valueOf(value.RequestId));
    xmlSerializer.endTag("", "RequestId");
    if (value.DocBucket != null) {
      com.tencent.qcloud.qcloudxml.core.QCloudXml.toXml(xmlSerializer, value.DocBucket);
    }
    xmlSerializer.endTag("", "Response");
  }
}
