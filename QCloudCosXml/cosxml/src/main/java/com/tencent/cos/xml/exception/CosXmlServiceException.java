package com.tencent.cos.xml.exception;

import com.tencent.qcloud.core.common.QCloudServiceException;

/**
 * <p>
 * sdk自定义的服务端异常信息类,主要是请求返回失败导致，如请求cos服务时，cos服务端返回的http code不在[200, 300）之间。
 * 通过该异常类的 requestId 属性可以查到详细的log信息.
 * @see QCloudServiceException
 */

public class CosXmlServiceException extends QCloudServiceException {

    private static final long serialVersionUID = 1L;

    private String httpMsg;

    public CosXmlServiceException(String httpMsg) {
        super(null);
        this.httpMsg = httpMsg;
    }

    public CosXmlServiceException(String errorMessage, Exception cause) {
        super(errorMessage, cause);
    }

    public String getHttpMessage(){
        return httpMsg;
    }

    public CosXmlServiceException(QCloudServiceException qcloudServiceException){
        super(null);
        this.setErrorCode(qcloudServiceException.getErrorCode());
        this.setErrorMessage(qcloudServiceException.getErrorMessage());
        this.setRequestId(qcloudServiceException.getRequestId());
        this.setServiceName(qcloudServiceException.getServiceName());
        this.setStatusCode(qcloudServiceException.getStatusCode());
    }

    @Override
    public String getMessage() {
        return getErrorMessage()
                + " (Service: " + getServiceName()
                + "; Status Code: " + getStatusCode()
                + "; Status Message: " + httpMsg
                + "; Error Code: " + getErrorCode()
                + "; Request ID: " + getRequestId() + ")";
    }
}
