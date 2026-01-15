package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.pic.PicUploadResult;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * 云上图片处理结果
 *
 * <p>
 * Created by jordanqin on 2025/12/6 19:29.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class ImageProcessResult extends CosXmlResult {
    public PicUploadResult picUploadResult;
    public String eTag;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);
        eTag = response.header("ETag");
        picUploadResult = QCloudXmlUtils.fromXml(response.byteStream(), PicUploadResult.class);
    }

    public PicUploadResult getPicUploadResult() {
        return picUploadResult;
    }
}
