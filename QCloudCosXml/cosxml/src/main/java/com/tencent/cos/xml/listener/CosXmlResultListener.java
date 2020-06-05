package com.tencent.cos.xml.listener;


import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;

/**
 * Created by bradyxiao on 2017/12/1.
 * <p>
 *  请求结果回调接口<br>
 *  若是请求成功，即返回http code 在[200, 300)之间，回调 {@link #onSuccess(CosXmlRequest, CosXmlResult)}<br>;
 *  若是请求失败，回调 {@link #onFail(CosXmlRequest, CosXmlClientException, CosXmlServiceException)}.
 * </p>
 * @see CosXmlRequest
 * @see CosXmlResult
 * @see CosXmlClientException
 * @see CosXmlServiceException
 */

public interface CosXmlResultListener {
    void onSuccess(CosXmlRequest request, CosXmlResult result);
    void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException);

}
