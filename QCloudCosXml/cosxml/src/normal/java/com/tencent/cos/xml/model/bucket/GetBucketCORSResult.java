package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.CORSConfiguration;
import com.tencent.cos.xml.transfer.XmlParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * <p>
 * 获取Bucket的跨域规则CORS结果类
 * </p>
 */
final public class GetBucketCORSResult extends CosXmlResult {

   public CORSConfiguration corsConfiguration;

   @Override
   public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
      super.parseResponseBody(response);
      corsConfiguration = new CORSConfiguration();
      try {
         XmlParser.parseCORSConfiguration(response.byteStream(), corsConfiguration);
      } catch (XmlPullParserException e) {
         throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
      } catch (IOException e) {
         throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
      }
   }

   @Override
   public String printResult() {
      return corsConfiguration != null ? corsConfiguration.toString() : super.printResult();
   }
}
