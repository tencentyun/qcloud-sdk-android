package com.tencent.cos.xml.transfer;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;

/**
 * Listener interface for transfer state and progress changes.
 *
 * Created by rickenwang on 2018/5/16.
 * <p>
 * Copyright (c) 2010-2017 Tencent Cloud. All rights reserved.
 */
public interface TransferListener {
    /**
     * Called when the state of the transfer is changed.
     *
     * @param id The id of the transfer record.
     * @param state The new state of the transfer.
     */
    void onStateChanged(String id, TransferState state);

    /**
     * Called when more bytes are transferred.
     *
     * @param id The id of the transfer record.
     * @param bytesCurrent Bytes transferred currently.
     * @param bytesTotal The total bytes to be transferred.
     */
    void onProgressChanged(String id, long bytesCurrent, long bytesTotal);

    /**
     * Called when an exception happens.
     *
     * @param id The id of the transfer record.
     * @param clientException An client exception object.
     * @param serviceException An Service exception object.
     */
    void onError(String id, CosXmlClientException clientException, CosXmlServiceException serviceException);
}

