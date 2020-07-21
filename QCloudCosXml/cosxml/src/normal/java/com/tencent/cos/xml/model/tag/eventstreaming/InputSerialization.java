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
 * 描述待检索对象的格式
 */
public class InputSerialization {

    private String compressionType;

    /**
     * Describes the serialization of a CSV-encoded object.
     */
    private CSVInput csv;

    /**
     * Specifies JSON as object's input serialization format.
     */
    private JSONInput json;

    /**
     * Specifies Parquet as object's input serialization format.
     */
    private ParquetInput parquet;

    public InputSerialization(CompressionType compressionType, CSVInput csv) {
        this.csv = csv;
        this.compressionType = compressionType.toString();
    }

    public InputSerialization(CompressionType compressionType, JSONInput json) {
        this.compressionType = compressionType.toString();
        this.json = json;
    }

    public InputSerialization(String compressionType, CSVInput csv) {
        this.csv = csv;
        this.compressionType = compressionType;
    }

    public InputSerialization(String compressionType, JSONInput json) {
        this.compressionType = compressionType;
        this.json = json;
    }

    /**
     * @return the serialization of a CSV-encoded object.
     */
    public CSVInput getCsv() {
        return csv;
    }

    /**
     * Sets the serialization of a CSV-encoded object.
     *
     * @param csv The new csv value.
     */
    public void setCsv(CSVInput csv) {
        this.csv = csv;
    }

    /**
     * Sets the serialization of a CSV-encoded object.
     *
     * @param csvInput The new csv value.
     * @return This object for method chaining.
     */
    public InputSerialization withCsv(CSVInput csvInput) {
        setCsv(csvInput);
        return this;
    }

    /**
     * Specifies JSON as object's input serialization format.
     */
    public JSONInput getJson() {
        return json;
    }

    /**
     * Specifies JSON as object's input serialization format.
     */
    public void setJson(JSONInput json) {
        this.json = json;
    }

    /**
     * Specifies JSON as object's input serialization format.
     */
    public InputSerialization withJson(JSONInput json) {
        setJson(json);
        return this;
    }

    /**
     * Specifies Parquet as object's input serialization format.
     */
    public ParquetInput getParquet() {
        return parquet;
    }

    /**
     * Specifies Parquet as object's input serialization format.
     */
    public void setParquet(ParquetInput parquet) {
        this.parquet = parquet;
    }

    /**
     * Specifies Parquet as object's input serialization format.
     */
    public InputSerialization withParquet(ParquetInput parquet) {
        setParquet(parquet);
        return this;
    }

    /**
     * Specifies object's compression format. Valid values: NONE, GZIP. Default Value: NONE.
     */
    public String getCompressionType() {
        return compressionType;
    }

    /**
     * Specifies object's compression format. Valid values: NONE, GZIP. Default Value: NONE.
     */
    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    /**
     * Specifies object's compression format. Valid values: NONE, GZIP. Default Value: NONE.
     */
    public void setCompressionType(CompressionType compressionType) {
        setCompressionType(compressionType == null ? null : compressionType.toString());
    }

    /**
     * Specifies object's compression format. Valid values: NONE, GZIP. Default Value: NONE.
     */
    public InputSerialization withCompressionType(String compressionType) {
        setCompressionType(compressionType);
        return this;
    }

    /**
     * Specifies object's compression format. Valid values: NONE, GZIP. Default Value: NONE.
     */
    public InputSerialization withCompressionType(CompressionType compressionType) {
        setCompressionType(compressionType);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ! (obj instanceof InputSerialization)) {
            return false;
        }

        final InputSerialization other = (InputSerialization) obj;

        if (other.getCsv() == null ^ this.getCsv() == null)
            return false;
        if (other.getCsv() != null && !other.getCsv().equals(this.getCsv()))
            return false;
        if (other.getJson() == null ^ this.getJson() == null)
            return false;
        if (other.getJson() != null && !other.getJson().equals(this.getJson()))
            return false;
        if (other.getCompressionType() == null ^ this.getCompressionType() == null)
            return false;
        if (other.getCompressionType() != null && !other.getCompressionType().equals(this.getCompressionType()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getCsv() == null) ? 0 : getCsv().hashCode());
        hashCode = prime * hashCode + ((getJson() == null) ? 0 : getJson().hashCode());
        hashCode = prime * hashCode + ((getCompressionType() == null) ? 0 : getCompressionType().hashCode());
        return hashCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getCsv() != null)
            sb.append("Csv: ").append(getCsv());
        if (getJson() != null)
            sb.append("Json: ").append(getJson());
        if (getCompressionType() != null)
            sb.append("CompressionType: ").append(getCompressionType());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public InputSerialization clone() {
        try {
            return (InputSerialization) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() " + "even though we're Cloneable!", e);
        }
    }
}
