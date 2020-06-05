package com.tencent.qcloud.logutils;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by bradyxiao on 2018/10/24.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */

public class LogDialog extends Dialog{

    private View.OnClickListener onClickListener;

    public LogDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_log, null);
        setContentView(view);

        TextView title = view.findViewById(R.id.titleId);
        title.getPaint().setFakeBoldText(true);

        Button noBtn = view.findViewById(R.id.noId);
        noBtn.getPaint().setFakeBoldText(true);
        noBtn.setOnClickListener(onClickListener);

        Button yesBtn = view.findViewById(R.id.yesId);
        yesBtn.getPaint().setFakeBoldText(true);
        yesBtn.setOnClickListener(onClickListener);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        lp.y = 20;
        dialogWindow.setAttributes(lp);
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }


}
