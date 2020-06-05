package com.tencent.cos.xml.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bradyxiao on 2018/3/20.
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
