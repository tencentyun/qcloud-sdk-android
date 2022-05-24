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


import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间工具类
 */

public class DateUtils {
    private static final String GMT_TIME_FORMAT =  "EEE, dd MMM yyyy HH:mm:ss 'GMT'";

    public static Date toDate(String gmt) throws CosXmlClientException {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GMT_TIME_FORMAT, Locale.ENGLISH);
            return simpleDateFormat.parse(gmt);
        }catch (ParseException e) {
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
        }
    }

    public static String toString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GMT_TIME_FORMAT, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

    public static String toString(long dateSeconds){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GMT_TIME_FORMAT, Locale.ENGLISH);
        return simpleDateFormat.format(new Date(dateSeconds));
    }

    public static String getFormatTime(String dateFormat, long timeMills){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return simpleDateFormat.format(new Date(timeMills));
    }

}
