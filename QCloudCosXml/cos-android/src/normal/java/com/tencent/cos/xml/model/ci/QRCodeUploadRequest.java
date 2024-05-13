package com.tencent.cos.xml.model.ci;

import android.net.Uri;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.pic.PicOperationRule;
import com.tencent.cos.xml.model.tag.pic.PicOperations;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

/**
 * 上传时二维码识别请求
 */
public class QRCodeUploadRequest extends ImageUploadRequest {

    /**
     * 存储结果的目标存储桶，格式为 BucketName-APPID，如果不指定的话默认保存到当前存储桶
     */
    public String saveBucket;

    /**
     * 处理结果的文件路径名称，例如以/开头，则存入指定文件夹中，否则存入与原图文件相同的目录位置
     */
    public String fileId;

    /**
     * 二维码覆盖功能。可为0或1。
     */
    private int cover = 0;

    public QRCodeUploadRequest(String bucket, String cosPath, String srcPath) {
        super(bucket, cosPath, srcPath);
    }

    public QRCodeUploadRequest(String bucket, String cosPath, Uri uri) {
        super(bucket, cosPath, uri);
    }

    public QRCodeUploadRequest(String bucket, String cosPath, byte[] data) {
        super(bucket, cosPath, data);
    }

    public QRCodeUploadRequest(String bucket, String cosPath, InputStream inputStream) {
        super(bucket, cosPath, inputStream);
    }

    public QRCodeUploadRequest(String bucket, String cosPath, URL url) {
        super(bucket, cosPath, url);
    }

    @Override
    public PicOperations getPicOperations() {
        PicOperationRule rule = new PicOperationRule("QRcode/cover/" + cover);
        rule.setBucket(saveBucket);
        rule.setFileId(fileId);
        return new PicOperations(isPicInfo, Collections.singletonList(rule));
    }

    /**
     * 设置是否开启二维码覆盖，功能开启后，将对识别出的二维码覆盖上马赛克，默认值 0   
     * 
     * @param cover 0 表示不开启二维码覆盖
     *              1 表示开启二维码覆盖
     */
    public void setCover(int cover) {
        this.cover = cover;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
    }
}
