package com.tencent.cos.xml.model.ci;

import android.net.Uri;

import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.object.SaveLocalRequest;

/**
 * 万象处理需要将body保存到本地的请求
 * <p>
 * Created by jordanqin on 2023/12/25 17:51.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public abstract class CiSaveLocalRequest extends CosXmlRequest implements SaveLocalRequest {
    public String saveLocalPath;
    public Uri saveLocalUri;

    @Override
    public String getSaveLocalPath() {
        return saveLocalPath;
    }

    @Override
    public Uri getSaveLocalUri() {
        return saveLocalUri;
    }

    @Override
    public long getSaveLocalOffset() {
        return 0;
    }
}
