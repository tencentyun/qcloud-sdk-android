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
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;

import java.util.Map;

/**
 * 分块复制的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#copyObject(UploadPartCopyRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#copyObjectAsync(UploadPartCopyRequest, CosXmlResultListener)
 */
public class UploadPartCopyRequest extends CopyObjectRequest {
    /**Specified part number*/
    private int partNumber = -1;
    /**init upload generate' s uploadId by service*/
    private String uploadId = null;

    public UploadPartCopyRequest(String bucket, String cosPath, int partNumber, String uploadId, CopySourceStruct copySourceStruct) throws CosXmlClientException {
       this(bucket, cosPath, partNumber, uploadId, copySourceStruct, -1, -1);
    }

    public UploadPartCopyRequest(String bucket, String cosPath, int partNumber, String uploadId, CopySourceStruct copySourceStruct,
                                 long start, long end){
        super(bucket, cosPath, copySourceStruct);
        this.partNumber = partNumber;
        this.uploadId = uploadId;
        setCopyRange(start, end);
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("partNumber", String.valueOf(partNumber));
        queryParameters.put("uploadId",uploadId);
        return super.getQueryString();
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(requestURL != null)return;
        if(partNumber <= 0){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "partNumber must be >= 1");
        }
        if(uploadId == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "uploadID must not be null");
        }
    }

    /**
     * 设置源对象的字节范围
     * 例如 bytes=0-9 表示您希望拷贝源对象的开头10个字节的数据
     * @param start 起始字节
     * @param end 结束字节
     */
    public void setCopyRange(long start, long end){
        if(start >= 0 && end >= start){
            String bytes = "bytes=" + start + "-" + end;
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE_RANGE, bytes);
        }
    }
}
