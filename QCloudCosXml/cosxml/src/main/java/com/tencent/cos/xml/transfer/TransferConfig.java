package com.tencent.cos.xml.transfer;

import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.cos.xml.CosXmlServiceConfig;

/**
 * Created by bradyxiao on 2018/9/21.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */

public class TransferConfig {

    protected long divisionForCopy;
    protected long sliceSizeForCopy;
    protected long divisionForUpload;
    protected long sliceSizeForUpload;

    TransferConfig(Builder builder){
        this.divisionForCopy = builder.divisionForCopy;
        this.sliceSizeForCopy = builder.sliceSizeForCopy;
        this.divisionForUpload = builder.divisionForUpload;
        this.sliceSizeForUpload = builder.sliceSizeForUpload;
    }


    public long getDivisionForCopy() {
        return divisionForCopy;
    }

    public static class Builder{
        private long divisionForCopy = 5242880; // 5M
        private long sliceSizeForCopy = 5242880; //5M
        private long divisionForUpload = 2097152; //2M
        private long sliceSizeForUpload = 1048576; // 1M

        public Builder(){

        }

        public Builder setDividsionForCopy(long division){
            if(division > 0){
                this.divisionForCopy = division;
            }
            return this;
        }

        public Builder setDivisionForUpload(long division){
            if(division > 0){
                this.divisionForUpload = division;
            }
            return this;
        }

        public Builder setSliceSizeForCopy(long sliceSizee){
            if(sliceSizee > 0){
                this.sliceSizeForCopy = sliceSizee;
            }
            return this;
        }

        public Builder setSliceSizeForUpload(long sliceSizee){
            if(sliceSizee > 0){
                this.sliceSizeForUpload = sliceSizee;
            }
            return this;
        }

        public TransferConfig build(){
            return new TransferConfig(this);
        }
    }
}
