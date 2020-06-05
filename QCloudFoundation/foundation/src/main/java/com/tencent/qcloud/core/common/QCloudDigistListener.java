package com.tencent.qcloud.core.common;

import java.io.IOException;

public interface QCloudDigistListener {

    String onGetMd5() throws IOException;
}
