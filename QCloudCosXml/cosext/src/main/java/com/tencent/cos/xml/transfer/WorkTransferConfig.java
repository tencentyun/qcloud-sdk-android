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
import android.os.Parcelable;

import com.tencent.cos.xml.constraints.Constraints;

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
