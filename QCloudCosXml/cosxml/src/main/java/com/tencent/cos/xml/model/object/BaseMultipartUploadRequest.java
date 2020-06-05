package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.qcloud.core.auth.STSCredentialScope;

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/12/28.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public abstract class BaseMultipartUploadRequest extends ObjectRequest {

    BaseMultipartUploadRequest(String bucket, String cosPath){
        super(bucket, cosPath);
    }

    @Override
    public STSCredentialScope[] getSTSCredentialScope(CosXmlServiceConfig config) {
        String[] actions = new String[] {
                "name/cos:InitiateMultipartUpload",
                "name/cos:ListParts",
                "name/cos:UploadPart",
                "name/cos:CompleteMultipartUpload",
                "name/cos:AbortMultipartUpload"
        };
        STSCredentialScope[] scopes = new STSCredentialScope[actions.length];
        int i = 0;
        for (String action : actions) {
            scopes[i++] = new STSCredentialScope(action, config.getBucket(bucket),
                    config.getRegion(), getPath(config));
        }
        return scopes;
    }
}
