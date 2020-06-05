package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.ListParts;
import com.tencent.cos.xml.transfer.XmlSlimParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 查询特定分块上传中的已上传块请求返回的结果.<br>
 * 关于查询特定分块上传中的已上传块接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7747">
 * https://cloud.tencent.com/document/product/436/7747.</a><br>
 * @see ListParts
 */
final public class ListPartsResult extends CosXmlResult {

    /**
     * 本次分块上传的所有信息. {@link ListParts}
     */
    public ListParts listParts;

    /**
     *  @see CosXmlResult#parseResponseBody(HttpResponse)
     */
    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        listParts = new ListParts();
        try {
            XmlSlimParser.parseListPartsResult(response.byteStream(), listParts);
        } catch (XmlPullParserException e) {
           throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    /**
     *  @see CosXmlResult#printResult()
     */
    @Override
    public String printResult() {
        return listParts != null ? listParts.toString() : super.printResult();
    }
}
