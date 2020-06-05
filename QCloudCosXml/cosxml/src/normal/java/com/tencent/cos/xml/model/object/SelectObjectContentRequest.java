package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.SelectObjectContentListener;
import com.tencent.cos.xml.model.tag.eventstreaming.InputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.OutputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.RequestProgress;
import com.tencent.cos.xml.model.tag.eventstreaming.ScanRange;
import com.tencent.cos.xml.model.tag.eventstreaming.SelectRequest;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

public class SelectObjectContentRequest extends ObjectRequest {

    public static final String EXPRESSION_TYPE_SQL = "SQL";

    private String expression;

    private String expressionType;

    private RequestProgress requestProgress;

    private InputSerialization inputSerialization;

    private OutputSerialization outputSerialization;

    private ScanRange scanRange;


    private SelectObjectContentListener selectObjectContentProgressListener;

    // 设置后，会将响应打印到该路径的文件下
    private String selectResponseFilePath;


    public SelectObjectContentRequest(String bucket, String cosPath, String expressionType, String expression,
                                      RequestProgress requestProgress, InputSerialization inputSerialization,
                                      OutputSerialization outputSerialization, ScanRange scanRange) {

        super(bucket, cosPath);
        this.expression = expression;
        this.expressionType = expressionType;
        this.requestProgress = requestProgress;
        this.inputSerialization = inputSerialization;
        this.outputSerialization = outputSerialization;
        this.scanRange = scanRange;
    }

    public SelectObjectContentRequest(String bucket, String cosPath, String sql,
                                      boolean requestProgress, InputSerialization inputSerialization,
                                      OutputSerialization outputSerialization) {
        this(bucket, cosPath, EXPRESSION_TYPE_SQL, sql, new RequestProgress(requestProgress),
                inputSerialization, outputSerialization, null);
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("select", null);
        queryParameters.put("select-type", "2");
        return queryParameters;
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildSelectRequest(new SelectRequest(expressionType, expression,
                            requestProgress, inputSerialization, outputSerialization, scanRange)));
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.IO_ERROR.getCode(), e);
        }
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }

    public void setSelectObjectContentProgressListener(SelectObjectContentListener selectObjectContentProgressListener) {
        this.selectObjectContentProgressListener = selectObjectContentProgressListener;
    }

    public SelectObjectContentListener getSelectObjectContentProgressListener() {
        return selectObjectContentProgressListener;
    }


    public void setSelectResponseFilePath(String selectResponseFilePath) {
        this.selectResponseFilePath = selectResponseFilePath;
    }

    public String getSelectResponseFilePath() {
        return selectResponseFilePath;
    }
}
