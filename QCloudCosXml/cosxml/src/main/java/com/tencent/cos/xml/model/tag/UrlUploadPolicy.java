package com.tencent.cos.xml.model.tag;

/**
 * URL上传策略
 * <p>
 *
 * @author jordanqin
 * @since 2021/1/25
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class UrlUploadPolicy {
    private final Type downloadType;
    private final long fileLength;

    public UrlUploadPolicy(Type downloadType, long fileLength) {
        this.downloadType = downloadType;
        this.fileLength = fileLength;
    }

    public Type getDownloadType() {
        return downloadType;
    }

    public long getFileLength() {
        return fileLength;
    }

    public enum Type {
        NOTSUPPORT, // 不支持
        RANGE,   //  范围
        ENTIRETY //  整体
    }
}
