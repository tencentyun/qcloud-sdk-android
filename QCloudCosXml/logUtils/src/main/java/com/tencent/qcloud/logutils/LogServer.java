package com.tencent.qcloud.logutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by bradyxiao on 2018/10/22.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */

public class LogServer implements Application.ActivityLifecycleCallbacks, View.OnClickListener{

    private static String TAG = LogServer.class.getSimpleName();
    private int activeActivityCount = 0;
    private boolean isAppForeground = false;
    private Application application;
    private ClipboardManager clipboardManager;
    protected static final String FILE_PARENT_PATH_KEY = "FILE_PARENT_PATH";
    protected static final String FILE_NAME_KEY = "FILE_NAME";
    private OnLogListener onLogListener;
    private Context currentContext;
    private AlertDialog alertDialog;
    private static final String KEY_LOG = "##qcloud-cos-log-ispct##";

    public LogServer(Context context){
        application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(this);
        clipboardManager = (ClipboardManager) application.getSystemService(Context.CLIPBOARD_SERVICE);
        createAlertDialog(context);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        activeActivityCount ++;
        if(!isAppForeground){
            isAppForeground = true; // background switch foreground
            Log.d(TAG, "background to foreground");
            if(clipboardManager != null && clipboardManager.hasPrimaryClip()){
                ClipData clipData = clipboardManager.getPrimaryClip();
                if(clipData == null) return;
                ClipData.Item item = clipData.getItemAt(0);
                if(item == null) return;
                CharSequence content = item.getText();
                if(content == null)return;
                content = content.toString().trim();
                Log.d(TAG, "clip content: " + content);
                if(KEY_LOG.equals(content)){
                    Log.d(TAG, "hit it");
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
                    currentContext = activity;
                    tipsDialog();
                }
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        activeActivityCount --;
        if(activeActivityCount < 0)activeActivityCount = 0;
        if(activeActivityCount == 0){
            isAppForeground = false; // foreground switch background
            Log.d(TAG, "foreground switch background");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //是否完全退出
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.yesId){
            showLog(currentContext == null ? application : currentContext);
        }
    }

    public void setOnLogListener(OnLogListener onLogListener){
        Log.d(TAG, "setOnLogListener");
        this.onLogListener = onLogListener;
    }

    public void destroy(){
        Log.d(TAG, "destroy");
        if(application != null){
            application.unregisterActivityLifecycleCallbacks(this);
        }
    }

    private void tipsDialog(){
        if(alertDialog != null && !alertDialog.isShowing()){
            alertDialog.show();
        }
    }

    private void showLog(Context context){
        Intent intent = new Intent();
        File[] files = null;
        if(onLogListener != null){
            files = onLogListener.onLoad();
        }
        ArrayList<String> fileNameList = null;
        String parentPath = null;
        if(files != null && files.length > 0){
            fileNameList = new ArrayList<>();
            parentPath = files[0].getParent();
            for(File file : files){
                fileNameList.add(file.getName());
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString(LogServer.FILE_PARENT_PATH_KEY, parentPath);
        bundle.putStringArrayList(LogServer.FILE_NAME_KEY, fileNameList);
        intent.putExtras(bundle);
        intent.setClass(context, LogActivity.class);
        context.startActivity(intent);
    }



    private void createAlertDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_title)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLog(currentContext == null ? application : currentContext);
                        alertDialog.dismiss();
                    }
                });
        alertDialog = builder.create();
    }

}
