package com.tencent.qcloud.core.http;

import com.tencent.qcloud.core.common.QCloudProgressListener;

/**
 * <p>
 * </p>
 * Created by wjielai on 2017/12/5.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public interface ProgressBody {

    void setProgressListener(QCloudProgressListener progressListener);

    long getBytesTransferred();
}
