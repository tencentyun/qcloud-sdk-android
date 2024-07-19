package com.tencent.cos.xml.model.object;

import android.net.Uri;

/**
 * 需要将body保存到本地的请求
 * <p>
 * Created by jordanqin on 2023/12/25 17:30.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public interface SaveLocalRequest {
    /**
     * 获取文件本地保存路径
     * @return 本地保存路径
     */
    String getSaveLocalPath();

    /**
     * 获取文件本地保存uri
     * @return 本地保存uri
     */
    Uri getSaveLocalUri();

    long getSaveLocalOffset();
}
