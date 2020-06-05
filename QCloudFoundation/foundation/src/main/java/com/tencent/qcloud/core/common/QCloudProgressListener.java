package com.tencent.qcloud.core.common;


/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public interface QCloudProgressListener {

    void onProgress(long complete, long target);
}
