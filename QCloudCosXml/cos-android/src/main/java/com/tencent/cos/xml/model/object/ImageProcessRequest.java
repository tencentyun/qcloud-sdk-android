package com.tencent.cos.xml.model.object;

import androidx.annotation.NonNull;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.pic.PicOperations;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import java.util.Map;

/**
 * 云上图片处理请求
 *
 * <p>
 * Created by jordanqin on 2025/12/6 19:29.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class ImageProcessRequest extends ObjectRequest {
    private static final String TAG = "ImageProcessRequest";

    private PicOperations operations;
    /**
     * 云上图片处理请求
     *
     * @param bucket  存储桶名
     * @param cosPath cos上的路径
     * @param operations 图片处理操作
     */
    public ImageProcessRequest(String bucket, String cosPath, @NonNull PicOperations operations) {
        super(bucket, cosPath);
        this.operations = operations;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("image_process", null);
        return queryParameters;
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.bytes(null, new byte[0]);
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if (operations == null) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "operations is null");
        }
        addHeader("Pic-Operations", operations.toJsonStr());
    }
}
