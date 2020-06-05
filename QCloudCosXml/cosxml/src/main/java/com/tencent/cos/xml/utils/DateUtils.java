package com.tencent.cos.xml.utils;


import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bradyxiao on 2017/12/11.
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
