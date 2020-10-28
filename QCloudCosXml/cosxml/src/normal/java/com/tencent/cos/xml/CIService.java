package com.tencent.cos.xml;

import android.content.Context;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateResult;
import com.tencent.cos.xml.model.ci.GetBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.GetBucketDPStateResult;
import com.tencent.cos.xml.model.ci.PutBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.PutBucketDPStateResult;
import com.tencent.qcloud.core.auth.COSXmlSigner;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.SignerFactory;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/10/15.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class CIService extends CosXmlService {

    private static class CISigner extends COSXmlSigner {
        @Override
        protected String getSessionTokenKey() {
            return "x-ci-security-token";
        }
    }

    public CIService(Context context, CosXmlServiceConfig configuration, QCloudCredentialProvider qCloudCredentialProvider) {
        super(context, configuration, qCloudCredentialProvider);
        this.signerType = "CISigner";
        SignerFactory.registerSigner(signerType, new CISigner());
    }

    public GetBucketDPStateResult getBucketDocumentPreviewState(GetBucketDPStateRequest request)
            throws CosXmlClientException, CosXmlServiceException{
        return execute(request, new GetBucketDPStateResult());
    }

    public PutBucketDPStateResult putBucketDocumentPreviewState(PutBucketDPStateRequest request)
            throws CosXmlClientException, CosXmlServiceException{
        return execute(request, new PutBucketDPStateResult());
    }

    public DeleteBucketDPStateResult deleteBucketDocumentPreviewState(DeleteBucketDPStateRequest request)
            throws CosXmlClientException, CosXmlServiceException{
        return execute(request, new DeleteBucketDPStateResult());
    }
}
