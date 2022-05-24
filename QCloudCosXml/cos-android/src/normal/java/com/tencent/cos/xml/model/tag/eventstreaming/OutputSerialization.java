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

import java.io.Serializable;

/**
 * Describes how results of the Select job are serialized.
 */
public class OutputSerialization implements Serializable, Cloneable {

    /**
     * Describes the serialization of CSV-encoded Select results.
     */
    private CSVOutput csv;

    private JSONOutput json;

    public OutputSerialization(CSVOutput csv) {
        this.csv = csv;
    }

    public OutputSerialization(JSONOutput json) {
        this.json = json;
    }

    /**
     * @return the serialization of CSV-encoded Select results.
     */
    public CSVOutput getCsv() {
        return csv;
    }

    /**
     * Sets the serialization of CSV-encoded Select results.
     *
     * @param csv The new csv value.
     */
    public void setCsv(CSVOutput csv) {
        this.csv = csv;
    }

    /**
     * Sets the serialization of CSV-encoded Select results.
     *
     * @param csvOutput The new csv value.
     * @return This object for method chaining.
     */
    public OutputSerialization withCsv(CSVOutput csvOutput) {
        setCsv(csvOutput);
        return this;
    }

    /**
     * Specifies JSON as request's output serialization format.
     */
    public JSONOutput getJson() {
        return json;
    }

    /**
     * Specifies JSON as request's output serialization format.
     */
    public void setJson(JSONOutput json) {
        this.json = json;
    }

    /**
     * Specifies JSON as request's output serialization format.
     */
    public OutputSerialization withJson(JSONOutput json) {
        setJson(json);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ! (obj instanceof OutputSerialization)) {
            return false;
        }

        final OutputSerialization other = (OutputSerialization) obj;

        if (other.getCsv() == null ^ this.getCsv() == null)
            return false;
        if (other.getCsv() != null && !other.getCsv().equals(this.getCsv()))
            return false;
        if (other.getJson() == null ^ this.getJson() == null)
            return false;
        if (other.getJson() != null && !other.getJson().equals(this.getJson()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getCsv() == null) ? 0 : getCsv().hashCode());
        hashCode = prime * hashCode + ((getJson() == null) ? 0 : getJson().hashCode());
        return hashCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getCsv() != null)
            sb.append("CSV: ").append(getCsv());
        if (getJson() != null)
            sb.append("JSON: ").append(getJson());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public OutputSerialization clone() {
        try {
            return (OutputSerialization) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() " + "even though we're Cloneable!", e);
        }
    }
}
