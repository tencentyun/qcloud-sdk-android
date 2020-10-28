/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

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
