/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.utils;

import android.text.TextUtils;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * URL编码工具类
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
