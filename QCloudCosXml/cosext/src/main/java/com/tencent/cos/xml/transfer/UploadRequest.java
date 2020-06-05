package com.tencent.cos.xml.transfer;

import com.tencent.cos.xml.constraints.Constraints;

import java.util.UUID;

/**
 * Created by rickenwang on 2019-11-29.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class UploadRequest extends TransferRequest {

    private String cosKey;

    private String filePath;

    public UploadRequest(String id, String region, String bucket, String cosKey, String filePath, Constraints constraints) {
        super(id, region, bucket, constraints);
        this.cosKey = cosKey;
        this.filePath = filePath;
    }

    public static class Builder {

        private String region, bucket, cosKey, filePath;

        private Constraints constraints = Constraints.NETWORK_ANY;

        public Builder(String bucket, String cosKey) {
            setBucket(bucket);
            setCosKey(cosKey);
        }

        public Builder setRegion(String region) {
            this.region = region;
            return this;
        }

        public Builder setBucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public Builder setCosKey(String cosKey) {
            this.cosKey = cosKey;
            return this;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder setConstraints(Constraints constraints) {
            this.constraints = constraints;
            return this;
        }

        public UploadRequest build() {

            return new UploadRequest(UUID.randomUUID().toString(),
                    region, bucket, cosKey, filePath, constraints);
        }
    }

    public String getCosKey() {
        return cosKey;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public Constraints getConstraints() {
        return super.getConstraints();
    }
}
