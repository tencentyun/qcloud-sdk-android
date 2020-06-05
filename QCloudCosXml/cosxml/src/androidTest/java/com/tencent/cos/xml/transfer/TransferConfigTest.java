package com.tencent.cos.xml.transfer;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/9/25.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class TransferConfigTest {

    @Test
    public void testTransferConfig(){
        TransferConfig transferConfig = new TransferConfig.Builder()
                .build();
        assertEquals(transferConfig.divisionForCopy, 5 * 1024 * 1024);
        assertEquals(transferConfig.sliceSizeForCopy, 5 * 1024 * 1024);
        assertEquals(transferConfig.divisionForUpload, 2 * 1024 * 1024);
        assertEquals(transferConfig.sliceSizeForUpload, 1024 * 1024);
    }
}