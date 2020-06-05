package com.tencent.cos.xml.transfer;

/**
 * Created by bradyxiao on 2018/9/13.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */

public interface TransferStateListener {

    void onStateChanged(TransferState state);
}
