package com.tencent.cos.xml.exception;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.qcloud.core.common.QCloudClientException;

/**
 * <p>
 * sdk自定义的客户端异常类，主要是一些参数填写错误导致的。
 * 在进行请求操作之前，sdk会进行参数校验操作，一旦检验失败，就会抛出该异常信息.
 */

public class CosXmlClientException extends QCloudClientException {

    private static final long serialVersionUID = 1L;

    public final int errorCode;

    public CosXmlClientException(int errorCode, String message){
        super(message);
        this.errorCode = ClientErrorCode.to(errorCode).getCode();
    }

    public CosXmlClientException(int errorCode, String message, Throwable cause){
        super(message, cause);
        this.errorCode = ClientErrorCode.to(errorCode).getCode();
    }

    public CosXmlClientException(int errorCode, Throwable cause){
        super(cause);
        this.errorCode = ClientErrorCode.to(errorCode).getCode();
    }
}
