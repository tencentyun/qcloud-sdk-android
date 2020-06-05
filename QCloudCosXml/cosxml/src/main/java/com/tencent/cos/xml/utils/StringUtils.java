package com.tencent.cos.xml.utils;

import android.text.TextUtils;

import com.tencent.qcloud.core.util.QCloudHttpUtils;

import java.util.Map;

/**
 * Created by bradyxiao on 2017/2/28.
 *
 */
public class StringUtils {

    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'};
    public static String toHexString(byte[] data){
        StringBuilder result = new StringBuilder(data.length * 2);
        for(byte b : data){
            result.append(StringUtils.HEX_DIGITS[(b & 0xf0) >>> 4]);
            result.append(StringUtils.HEX_DIGITS[(b & 0x0f)]);
        }
        return result.toString();
    }

    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }


    public static String flat(Map<String, String> paras) {

        if (paras == null) {
            return "";
        }

        StringBuilder flat = new StringBuilder();
        boolean isFirst = true;

        for (Map.Entry<String, String> entry : paras.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!isFirst) {
                flat.append("&");
            }
            isFirst = false;
            flat.append(key);
            if (!TextUtils.isEmpty(value)) {
                flat.append("=").append(QCloudHttpUtils.urlEncodeString(value));
            }
        }

        return flat.toString();
    }

}
