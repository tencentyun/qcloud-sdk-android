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

package com.tencent.cos.xml.common;

/**
 * 客户端错误码
 * 请参考：<a href="https://cloud.tencent.com/document/product/436/30443">错误码文档</a>
 */
public enum ClientErrorCode {

    /**
     * 未知错误
     */
    UNKNOWN(-10000, "Unknown Error"),

    /**
     * 参数校验失败，例如必填参数为空
     */
    INVALID_ARGUMENT(10000, "InvalidArgument"),
    /**
     * 密钥信息校验失败，例如密钥为空
     */
    INVALID_CREDENTIALS(10001, "InvalidCredentials"),
    /**
     * SDK 配置错误，例如 APPID，region 配置出错
     */
    BAD_REQUEST(10002, "BadRequest"),
    /**
     * 输入源或者输出源错误，例如上传的文件不存在
     */
    SINK_SOURCE_NOT_FOUND(10003, "SinkSourceNotFound"),

    /**
     * 上传分片时没有返回 Etag 信息，请检查网络环境
     */
    ETAG_NOT_FOUND(10004, "ETagNotFound"),
    
    /**
     * 内部错误，例如 xml 格式数据解析失败
     */
    INTERNAL_ERROR(20000, "InternalError"),
    /**
     * 服务错误，例如返回了非 xml 格式数据
     */
    SERVERERROR(20001, "ServerError"),
    /**
     * 流读写 IO 异常，例如文件读写 IO 异常
     */
    IO_ERROR(20002, "IOError"),
    /**
     * 网络出现异常，例如网络不可用，DNS 解析失败等
     */
    POOR_NETWORK(20003, "NetworkError"),
    /**
     * 网络断开连接
     */
    NETWORK_NOT_CONNECTED(20004, "NetworkNotConnected"),

    /**
     * 用户已取消了请求
     */
    USER_CANCELLED(30000, "UserCancelled"),
    /**
     * 已执行过请求
     */
    ALREADY_FINISHED(30001, "AlreadyFinished"),
    /**
     * 重复任务
     */
    DUPLICATE_TASK(30002, "DuplicateTask"),

    KMS_ERROR(40000, "KMSError");

    private int code;
    private String errorMsg;

    ClientErrorCode(int code, String errorMsg){
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public static ClientErrorCode to(int code) {
        for (ClientErrorCode errorCode : ClientErrorCode.values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        throw new IllegalArgumentException("not error code defined");
    }

    public void setErrorMsg(String errorMsg){
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
