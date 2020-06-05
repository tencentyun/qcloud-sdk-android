package com.tencent.cos.xml.transfer;

import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.cos.xml.constraints.Constraints;

/**
 * Created by rickenwang on 2019-12-02.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class WorkTransferConfig extends TransferConfig implements Parcelable {

    private Constraints constraints;

    private WorkTransferConfig(Builder builder) {
        super(builder);
        this.constraints = builder.constraints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(divisionForCopy);
        dest.writeLong(sliceSizeForCopy);
        dest.writeLong(divisionForUpload);
        dest.writeLong(sliceSizeForUpload);
    }

    private WorkTransferConfig(Parcel in) {

        this((Builder) new Builder()
                .setDividsionForCopy(in.readLong())
                .setSliceSizeForCopy(in.readLong())
                .setDivisionForUpload(in.readLong())
                .setSliceSizeForUpload(in.readLong()));

    }

    public static final Parcelable.Creator<WorkTransferConfig> CREATOR
            = new Parcelable.Creator<WorkTransferConfig>() {
        public WorkTransferConfig createFromParcel(Parcel in) {
            return new WorkTransferConfig(in);
        }

        public WorkTransferConfig[] newArray(int size) {
            return new WorkTransferConfig[size];
        }
    };

    public Constraints getConstraints() {
        return constraints;
    }

    public static class Builder extends TransferConfig.Builder {

        private Constraints constraints = Constraints.NONE;

        public Builder() {}

        public Builder setConstrains(Constraints constrains) {
            this.constraints = constrains;
            return this;
        }

        @Override
        public WorkTransferConfig build() {
            return new WorkTransferConfig(this);
        }
    }


}
