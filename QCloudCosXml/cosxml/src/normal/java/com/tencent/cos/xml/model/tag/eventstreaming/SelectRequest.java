/*
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.

 * According to cos feature, we modify some class，comment, field name, etc.
 */

package com.tencent.cos.xml.model.tag.eventstreaming;


public class SelectRequest {

    private String expressionType;

    private String expression;

    private RequestProgress requestProgress;

    private InputSerialization inputSerialization;

    private OutputSerialization outputSerialization;

    private ScanRange scanRange;


    public SelectRequest(String expressionType, String expression, RequestProgress requestProgress,
                         InputSerialization inputSerialization, OutputSerialization outputSerialization,
                         ScanRange scanRange) {

        this.expressionType = expressionType;
        this.expression = expression;
        this.requestProgress = requestProgress;
        this.inputSerialization = inputSerialization;
        this.outputSerialization = outputSerialization;
        this.scanRange = scanRange;
    }


    public String getExpressionType() {
        return expressionType;
    }

    public String getExpression() {
        return expression;
    }

    public RequestProgress getRequestProgress() {
        return requestProgress;
    }

    public InputSerialization getInputSerialization() {
        return inputSerialization;
    }

    public OutputSerialization getOutputSerialization() {
        return outputSerialization;
    }

    public ScanRange getScanRange() {
        return scanRange;
    }
}
