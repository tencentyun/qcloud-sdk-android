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

import android.os.Parcel;

import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

@Deprecated
public class LocalCredentialProvider extends ShortTimeCredentialProvider implements ParcelableCredentialProvider {

    private String secretId;

    private String secretKey;

    private long keyDuration;

    @Deprecated
    public LocalCredentialProvider(String secretId, String secretKey, long keyDuration) {
        super(secretId, secretKey, keyDuration);

        this.secretId = secretId;
        this.secretKey = secretKey;
        this.keyDuration = keyDuration;
    }

    private LocalCredentialProvider(Parcel in) {
        this(in.readString(), in.readString(), in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(secretId);
        dest.writeString(secretKey);
        dest.writeLong(keyDuration);
    }

    public static final Creator<LocalCredentialProvider> CREATOR
            = new Creator<LocalCredentialProvider>() {
        public LocalCredentialProvider createFromParcel(Parcel in) {
            return new LocalCredentialProvider(in);
        }

        public LocalCredentialProvider[] newArray(int size) {
            return new LocalCredentialProvider[size];
        }
    };

    public long getKeyDuration() {
        return keyDuration;
    }

    public String getSecretId() {
        return secretId;
    }

    @Override
    public String getSecretKey() {
        return secretKey;
    }
}
