package com.tencent.cos.xml.transfer;


/**
 * <p>
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public class TransferObserver {

    private final String transferId;

    private TransferListener transferListener;

    private TransferState transferState;

    protected TransferObserver(String id) {
        transferId = id;
    }

    public TransferListener getTransferListener() {
        return transferListener;
    }

    public void setTransferListener(TransferListener transferListener) {
        this.transferListener = transferListener;
    }

    public String getTransferId() {
        return transferId;
    }

    protected void setTransferState(TransferState transferState) {
        this.transferState = transferState;
    }

    public TransferState getTransferState() {
        return transferState;
    }
}
