package com.tencent.cos.xml.constraints;

import androidx.room.TypeConverter;

import com.tencent.cos.xml.transfer.TransferState;

/**
 * Created by rickenwang on 2019-11-28.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class TransferStateConverters {

    @TypeConverter
    public TransferState convert2Status(String status) {

        return TransferState.getState(status);
    }

    @TypeConverter
    public String conver2Str(TransferState transferStatus) {

        return transferStatus.name();
    }
}
