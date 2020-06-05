package com.tencent.cos.xml.transfer;


import com.tencent.cos.xml.constraints.Constraints;

import java.util.Map;

/**
 * Created by rickenwang on 2019-11-29.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class TransferRequest {

    private String region;

    private String bucket;

    private Map<String, String> headers;

    private COSXMLTask.OnSignatureListener onSignatureListener;

    private Constraints constraints;

    public Constraints getConstraints() {
        return constraints;
    }

    public String getRegion() {
        return region;
    }

    public String getBucket() {
        return bucket;
    }


    public TransferRequest(String id, String region, String bucket, Constraints constraints) {
        this.region = region;
        this.bucket = bucket;
        this.constraints = constraints;
    }

    public COSXMLTask.OnSignatureListener getOnSignatureListener() {
        return onSignatureListener;
    }


    public Map<String, String> getHeaders() {
        return headers;
    }
}
