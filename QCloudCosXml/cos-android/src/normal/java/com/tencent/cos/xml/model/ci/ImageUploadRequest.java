package com.tencent.cos.xml.model.ci;

import android.net.Uri;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.tag.pic.PicOperations;

import java.io.InputStream;
import java.net.URL;

/**
 * 上传时万象处理请求
 * 
 * <p>
 * Created by rickenwang on 2021/4/28.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
abstract public class ImageUploadRequest extends PutObjectRequest implements PicOperationProvider {

    /**
     * 是否返回原图信息
     */
    public boolean isPicInfo = false;
    
    public ImageUploadRequest(String bucket, String cosPath, String srcPath) {
        super(bucket, cosPath, srcPath);
    }

    public ImageUploadRequest(String bucket, String cosPath, Uri uri) {
        super(bucket, cosPath, uri);
    }

    public ImageUploadRequest(String bucket, String cosPath, byte[] data) {
        super(bucket, cosPath, data);
    }

    public ImageUploadRequest(String bucket, String cosPath, StringBuilder stringBuilder) {super(bucket, cosPath, stringBuilder);}

    public ImageUploadRequest(String bucket, String cosPath, InputStream inputStream) {
        super(bucket, cosPath, inputStream);
    }

    public ImageUploadRequest(String bucket, String cosPath, URL url) {
        super(bucket, cosPath, url);
    }
   

    protected ImageUploadRequest(String bucket, String cosPath) {
        super(bucket, cosPath);
    }
    
    private void buildPicOperations() {
        
        PicOperations picOperations = getPicOperations();
        if (picOperations != null) {
            setPicOperations(picOperations);
        }
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        // 在检查参数时添加 header
        buildPicOperations();
    }
}
