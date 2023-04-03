package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.tag.pic.PicObject;
import com.tencent.cos.xml.model.tag.pic.PicOriginalInfo;
import com.tencent.cos.xml.model.tag.pic.QRCodeInfo;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * <p>
 * Created by rickenwang on 2021/5/17.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class QRCodeUploadResult extends ImageUploadResult {

    public PicUploadResult picUploadResult;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);

        picUploadResult = QCloudXmlUtils.fromXml(response.byteStream(), PicUploadResult.class);
    }

    public PicUploadResult getPicUploadResult() {
        return picUploadResult;
    }

    @XmlBean(name = "UploadResult", method = XmlBean.GenerateMethod.FROM)
    public static class PicUploadResult {

        @XmlElement(name = "OriginalInfo")
        public PicOriginalInfo originalInfo;

        @XmlElement(name = "ProcessResults")
        public List<QrPicObject> processResults;

    }

    @XmlBean(name = "Object", method = XmlBean.GenerateMethod.FROM)
    public static class QrPicObject extends PicObject {
        /**
         * 二维码识别结果。0表示未识别到二维码，1表示识别到二维码
         */
        public int codeStatus;

        /** 二维码扫描 */
        @XmlElement(name = "QRcodeInfo", flatListNote = true)
        public List<QRCodeInfo> qrCodeInfo;
    }

}
