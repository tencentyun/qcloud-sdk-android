package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.PostResponse;
import com.tencent.cos.xml.transfer.XmlSlimParser;
import com.tencent.qcloud.core.http.HttpResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bradyxiao on 2018/6/12.
 */

public class PostObjectResult extends CosXmlResult {
    /**
     * 返回文件的 MD5 算法校验值.eTag 的值可以用于检查 Object 在上传过程中是否有损坏
     */
    public String eTag;

    /**
     * 若指定了上传 success_action_redirect 则返回对应的值，若无指定则返回对象完整的路径
     */
    public String location;
    public PostResponse postResponse;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);
        eTag = response.header("ETag");
        location = response.header("Location");
        postResponse = new PostResponse();
        try {
            InputStream inputStream = response.byteStream();
            if(inputStream != null){
                XmlSlimParser.parsePostResponseResult(response.byteStream(), postResponse);
            }
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    @Override
    public String printResult() {
        return super.printResult();
    }
}
