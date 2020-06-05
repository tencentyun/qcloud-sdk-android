package com.tencent.cos.xml.listener;

import com.tencent.qcloud.core.common.QCloudProgressListener;

/**
 * Created by bradyxiao on 2017/12/1.
 * <p>
 * 进度回调接口，请参阅 {@link #onProgress(long, long)},
 * 第一个参数表示已上传或者已下载的数据长度，第二个参数表示总的数据长度.
 */

public interface CosXmlProgressListener extends QCloudProgressListener{
}
