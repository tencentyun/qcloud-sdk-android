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

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference工具类
 */

public class SharePreferenceUtils {
    private SharedPreferences sharedPreferences;
    private static SharePreferenceUtils instance;

    private SharePreferenceUtils(Context context){
       sharedPreferences = context.getSharedPreferences("upload_download", Context.MODE_PRIVATE);
    }

    public static SharePreferenceUtils instance(Context context){
        synchronized (SharePreferenceUtils.class){
            if(instance == null){
                instance = new SharePreferenceUtils(context);
            }
        }
        return instance;
    }

    public synchronized boolean updateValue(String key, String value){
        if(key != null){
            return sharedPreferences.edit().putString(key, value).commit();
        }
        return false;
    }

    public synchronized String getValue(String key){
        if(key != null){
            return sharedPreferences.getString(key, null);
        }else {
            return null;
        }
    }

    public synchronized boolean clear(String key){
        if(key != null){
           return sharedPreferences.edit().remove(key).commit();
        }
        return false;
    }

}
