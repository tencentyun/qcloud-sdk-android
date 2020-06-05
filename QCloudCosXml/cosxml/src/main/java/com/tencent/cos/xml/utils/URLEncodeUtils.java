package com.tencent.cos.xml.utils;

import android.text.TextUtils;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by bradyxiao on 2017/12/14.
 */

public class URLEncodeUtils {

    public static String cosPathEncode(String cosPath) throws CosXmlClientException {
        if(TextUtils.isEmpty(cosPath))return cosPath; //排除空格
        StringBuilder result = new StringBuilder();
        try {
            String[] slashSegments = cosPath.split("/", -1);
            for(int i = 0, size = slashSegments.length; i < size; i ++){
                if(i == 0 && "".equals(slashSegments[i])){
                    result.append('/');
                    continue;
                }
                if(size > 1 && i == size -1 && "".equals(slashSegments[i])){
                    break;
                }
                if(!"".equals(slashSegments[i])){
                    String[] spaceSegments = slashSegments[i].split(" ", -1);
                    for(int j =0, count = spaceSegments.length; j < count; j ++){
                        if(j == 0 && "".equals(spaceSegments[j])){
                            result.append("%20");
                            continue;
                        }
                        if(count > 1 && j == count -1 && "".equals(spaceSegments[j])){
                            break;
                        }
                        result.append(URLEncoder.encode(spaceSegments[j], "utf-8"));
                        if(j != count -1)result.append("%20");
                    }
                }
                if(i != size -1)result.append("/");
            }
            return result.toString();
        }catch (UnsupportedEncodingException e) {
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
        }
    }
}
