package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.tag.pic.PicOriginalInfo;
import com.tencent.cos.xml.model.tag.pic.QRCodeInfo;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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

        try {
            picUploadResult = QCloudXml.fromXml(response.byteStream(), PicUploadResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public PicUploadResult getPicUploadResult() {
        return picUploadResult;
    }

    @XmlBean(name = "UploadResult")
    public static class PicUploadResult {

        @XmlElement(name = "OriginalInfo")
        public PicOriginalInfo originalInfo;

        @XmlElement(name = "ProcessResults")
        public List<PicObject> processResults;

    }

    @XmlBean(name = "Object")
    public static class PicObject {

        /** 文件名 */
        @XmlElement(name = "Key")
        public String key;

        /** 图片路径 */
        @XmlElement(name = "Location")
        public String location;

        /** 图片格式 */
        @XmlElement(name = "Format")
        public String format;

        /** 图片宽度 */
        @XmlElement(name = "Width")
        public int width;

        /** 图片高度 */
        @XmlElement(name = "Height")
        public int height;

        /** 图片大小 */
        @XmlElement(name = "Size")
        public int size;

        /** 图片质量 */
        @XmlElement(name = "Quality")
        public int quality;

        /** 图片质量 */
        @XmlElement(name = "ETag")
        public String etag;

        /** 二维码扫描 */
        @XmlElement(name = "QRcodeInfo")
        public QRCodeInfo qrCodeInfo;


        /**
         * 构造盲水印图片处理结果
         * @param key 文件名
         * @param location 图片路径
         * @param format 图片格式
         * @param width 图片宽度
         * @param height 图片高度
         * @param size 图片大小
         * @param quality 图片质量
         */
        public PicObject(String key, String location, String format, int width, int height, int size, int quality) {
            this.key = key;
            this.location = location;
            this.format = format;
            this.width = width;
            this.height = height;
            this.size = size;
            this.quality = quality;
        }

        public PicObject() {}
    }

}
