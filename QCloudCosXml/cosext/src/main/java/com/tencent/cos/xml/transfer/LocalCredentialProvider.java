package com.tencent.cos.xml.transfer;

import android.os.Parcel;

import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

/**
 * Created by rickenwang on 2019-11-20.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
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
