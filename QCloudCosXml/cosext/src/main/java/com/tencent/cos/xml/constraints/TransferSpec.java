package com.tencent.cos.xml.constraints;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.tencent.cos.xml.transfer.DownloadRequest;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.UploadRequest;

import java.util.UUID;

/**
 * Created by rickenwang on 2019-11-25.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@Entity
public class TransferSpec {

    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public String region;       //

    @NonNull
    public String bucket;       //

    @NonNull
    public String key;          //

    @NonNull
    public String filePath;

    @Nullable
    public String uploadId;     // 分片上传 id

    @Nullable
    public String headers;      // 通过一个 JSON 字符串保存

    @Nullable
    public String etag;

    @Nullable
    public String workerId;     // worker request id

    @NonNull
    public boolean isUpload; // upload or download

    @NonNull
    public TransferState state = TransferState.UNKNOWN;  // 任务的传输状态

    @Deprecated
    @NonNull
    public long complete;

    @Deprecated
    @NonNull
    public long target;

    @Nullable public String transferServiceEgg; // 用于反序列化 TransferManager

    @Embedded
    @NonNull
    public Constraints constraints = Constraints.NONE;

    @Ignore
    public TransferSpec(String id, String region, String bucket, String key, String filePath, String headers, boolean isUpload) {

        this.id  = id;
        this.region = region;
        this.bucket = bucket;
        this.key = key;
        this.filePath = filePath;
        this.headers = headers;
        this.isUpload = isUpload;

        state = TransferState.UNKNOWN;
    }


    @Ignore
    public TransferSpec(TransferSpec transferSpec) {

        this(transferSpec.id, transferSpec.region, transferSpec.bucket, transferSpec.key, transferSpec.filePath, transferSpec.headers, transferSpec.isUpload);
        uploadId = transferSpec.uploadId;

    }

    /**
     * @hide
     */
    // @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public TransferSpec() { // stub required for room
    }

    @Ignore
    public TransferSpec(UploadRequest uploadRequest) {

        this(UUID.randomUUID().toString(),
                uploadRequest.getRegion(),
                uploadRequest.getBucket(),
                uploadRequest.getCosKey(),
                uploadRequest.getFilePath(),
                null, true);
        this.constraints = uploadRequest.getConstraints();
    }

    public TransferSpec(DownloadRequest downloadRequest) {}

    public void setServiceEgg(@NonNull byte[] serviceEgg) {
        this.transferServiceEgg = new String(serviceEgg);
    }

    @Nullable
    public byte[] getServiceEgg() {
        return transferServiceEgg != null ? transferServiceEgg.getBytes() : null;
    }
}
