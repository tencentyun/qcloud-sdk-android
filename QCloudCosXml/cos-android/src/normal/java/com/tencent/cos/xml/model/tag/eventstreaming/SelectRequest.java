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

package com.tencent.cos.xml.model.tag.eventstreaming;

/**
 * 使用结构化查询语句从指定对象（CSV 格式或者 JSON 格式）中检索内容的请求
 */
public class SelectRequest {

    private String expressionType;

    private String expression;

    private RequestProgress requestProgress;

    private InputSerialization inputSerialization;

    private OutputSerialization outputSerialization;

    /**
     * 构造SelectRequest
     * @param expressionType 表达式类型，该项为扩展项，目前只支持 SQL 表达式，仅支持 SQL 参数
     * @param expression SQL 表达式，代表您需要发起的检索操作 请参考：<a href="https://cloud.tencent.com/document/product/436/37636">Select 命令</a>
     * @param requestProgress 是否需要返回查询进度 QueryProgress 信息，如果选中 COS Select 将周期性返回查询进度
     * @param inputSerialization 描述待检索对象的格式
     * @param outputSerialization 描述检索结果的输出格式
     */
    public SelectRequest(String expressionType, String expression, RequestProgress requestProgress,
                         InputSerialization inputSerialization, OutputSerialization outputSerialization) {

        this.expressionType = expressionType;
        this.expression = expression;
        this.requestProgress = requestProgress;
        this.inputSerialization = inputSerialization;
        this.outputSerialization = outputSerialization;
    }

    /**
     * 获取表达式类型，该项为扩展项，目前只支持 SQL 表达式，仅支持 SQL 参数
     */
    public String getExpressionType() {
        return expressionType;
    }
    /**
     * 获取SQL 表达式
     */
    public String getExpression() {
        return expression;
    }
    /**
     * 获取是否需要返回查询进度 QueryProgress 信息
     */
    public RequestProgress getRequestProgress() {
        return requestProgress;
    }
    /**
     * 获取描述待检索对象的格式
     */
    public InputSerialization getInputSerialization() {
        return inputSerialization;
    }
    /**
     * 获取描述检索结果的输出格式
     */
    public OutputSerialization getOutputSerialization() {
        return outputSerialization;
    }
}
