package com.tencent.cos.xml.model.tag;

import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * Callback 的结果
 * <p>
 * Created by jordanqin on 2024/5/10 17:15.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@XmlBean(method = XmlBean.GenerateMethod.FROM)
public class CallbackResult {
    /**
     * Callback 是否成功。枚举值，支持 200、203。200表示上传成功、回调成功；203表示上传成功，回调失败。
     */
    public String status;
    /**
     * Status为200时，说明上传成功、回调成功，返回 CallbackBody。分块上传结果为base64编码后的字符串，普通上传结果为原始字符串，建议通过getCallbackBody()统一获取原始字符串
     */
    public String callbackBody;
    /**
     * callbackBody是否被base64编码
     */
    public boolean callbackBodyNotBase64;
    /**
     * Status为203时，说明Callback，返回 Error，说明回调失败信息。
     */
    public Error error;

    /**
     * 统一获取原始的CallbackBody
     * @return 原始的CallbackBody
     */
    public String getCallbackBody() {
        if(callbackBodyNotBase64){
            // 简单上传
            return callbackBody;
        } else {
            // 分块上传
            return DigestUtils.decodeBase64(callbackBody);
        }
    }

    /**
     * Status为203时，说明Callback，返回 Error，说明回调失败信息。
     */
    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class Error {
        /**
         * 回调失败信息的错误码，例如CallbackFailed
         */
        public String code;
        /**
         * Callback 失败的错误信息。
         */
        public String message;
    }
}
