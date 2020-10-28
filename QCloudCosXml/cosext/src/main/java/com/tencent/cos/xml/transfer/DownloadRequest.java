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

package com.tencent.cos.xml.transfer;

import com.tencent.cos.xml.constraints.Constraints;

import java.util.UUID;

public class DownloadRequest extends TransferRequest {

    private String cosKey;

    private String filePath;

    public DownloadRequest(String id, String region, String bucket, String cosKey, String filePath, Constraints constraints) {
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

        public DownloadRequest build() {

            return new DownloadRequest(UUID.randomUUID().toString(),
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
