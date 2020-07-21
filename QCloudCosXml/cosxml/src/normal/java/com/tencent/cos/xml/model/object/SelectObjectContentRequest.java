/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.listener.SelectObjectContentListener;
import com.tencent.cos.xml.model.tag.eventstreaming.InputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.OutputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.RequestProgress;
import com.tencent.cos.xml.model.tag.eventstreaming.SelectRequest;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * 使用结构化查询语句从指定对象（CSV 格式或者 JSON 格式）中检索内容的请求.
 * @see com.tencent.cos.xml.CosXml#selectObjectContent(SelectObjectContentRequest)
 * @see com.tencent.cos.xml.CosXml#selectObjectContentAsync(SelectObjectContentRequest, CosXmlResultListener)
 */
public class SelectObjectContentRequest extends ObjectRequest {

    public static final String EXPRESSION_TYPE_SQL = "SQL";

    private String expression;

    private String expressionType;

    private RequestProgress requestProgress;

    private InputSerialization inputSerialization;

    private OutputSerialization outputSerialization;

    private SelectObjectContentListener selectObjectContentProgressListener;

    // 设置后，会将响应打印到该路径的文件下
    private String selectResponseFilePath;

    /**
     * 构建对象检索请求
     * @param bucket 存储桶名
     * @param cosPath 对象cos路径
     * @param expressionType 表达式类型，该项为扩展项，目前只支持 SQL 表达式，仅支持 SQL 参数
     * @param expression SQL 表达式，代表您需要发起的检索操作 请参考：<a herf="https://cloud.tencent.com/document/product/436/37636">Select 命令</a>
     * @param requestProgress 是否需要返回查询进度 QueryProgress 信息，如果选中 COS Select 将周期性返回查询进度
     * @param inputSerialization 描述待检索对象的格式
     * @param outputSerialization 描述检索结果的输出格式
     */
    public SelectObjectContentRequest(String bucket, String cosPath, String expressionType, String expression,
                                      RequestProgress requestProgress, InputSerialization inputSerialization,
                                      OutputSerialization outputSerialization) {

        super(bucket, cosPath);
        this.expression = expression;
        this.expressionType = expressionType;
        this.requestProgress = requestProgress;
        this.inputSerialization = inputSerialization;
        this.outputSerialization = outputSerialization;
    }

    /**
     * 构建对象检索请求（默认SQL检索）
     * @param bucket 存储桶名
     * @param cosPath 对象cos路径
     * @param sql SQL 表达式，代表您需要发起的检索操作 请参考：<a herf="https://cloud.tencent.com/document/product/436/37636">Select 命令</a>
     * @param requestProgress 是否需要返回查询进度 QueryProgress 信息，如果选中 COS Select 将周期性返回查询进度
     * @param inputSerialization 描述待检索对象的格式
     * @param outputSerialization 描述检索结果的输出格式
     */
    public SelectObjectContentRequest(String bucket, String cosPath, String sql,
                                      boolean requestProgress, InputSerialization inputSerialization,
                                      OutputSerialization outputSerialization) {
        this(bucket, cosPath, EXPRESSION_TYPE_SQL, sql, new RequestProgress(requestProgress),
                inputSerialization, outputSerialization);
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
                            requestProgress, inputSerialization, outputSerialization)));
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

    /**
     * 设置检索进度监听器
     * @param selectObjectContentProgressListener 检索进度监听器
     */
    public void setSelectObjectContentProgressListener(SelectObjectContentListener selectObjectContentProgressListener) {
        this.selectObjectContentProgressListener = selectObjectContentProgressListener;
    }

    /**
     * 获取检索进度监听器
     * @return 检索进度监听器
     */
    public SelectObjectContentListener getSelectObjectContentProgressListener() {
        return selectObjectContentProgressListener;
    }

    /**
     * 设置响应保存文件路径
     * @param selectResponseFilePath 响应保存文件路径
     */
    public void setSelectResponseFilePath(String selectResponseFilePath) {
        this.selectResponseFilePath = selectResponseFilePath;
    }

    /**
     * 获取响应保存文件路径
     * @return 响应保存文件路径
     */
    public String getSelectResponseFilePath() {
        return selectResponseFilePath;
    }
}
