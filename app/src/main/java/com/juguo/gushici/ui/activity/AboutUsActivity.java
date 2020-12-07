package com.juguo.gushici.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseActivity;
import com.juguo.gushici.utils.TitleBarUtils;


public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setMiddleTitleText("关于我们");
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
