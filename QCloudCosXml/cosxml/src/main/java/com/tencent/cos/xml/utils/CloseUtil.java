package com.tencent.cos.xml.utils;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by bradyxiao on 2017/12/14.
 */

public class CloseUtil {

    public static void closeQuietly(Closeable closeable) throws CosXmlClientException {
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                throw new CosXmlClientException(ClientErrorCode.IO_ERROR.getCode(), e);
            }
        }
    }
}
