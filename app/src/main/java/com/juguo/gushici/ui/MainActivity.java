package com.juguo.gushici.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseActivity;
import com.juguo.gushici.ui.fragment.CenterFragment;
import com.juguo.gushici.ui.fragment.HomeFragment;
import com.juguo.gushici.ui.fragment.MineFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private LinearLayout mLlHome;
    private LinearLayout mLlCenter;
    private LinearLayout mLlMine;

    private HomeFragment mHomeFragment;
    private CenterFragment mCenterFragment;
    private MineFragment mMineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLlHome = findViewById(R.id.ll_main_home);
        mLlCenter = findViewById(R.id.ll_main_center);
        mLlMine = findViewById(R.id.ll_main_my);

        mLlHome.setOnClickListener(this);
        mLlCenter.setOnClickListener(this);
        mLlMine.setOnClickListener(this);

        mHomeFragment = new HomeFragment();
        mCenterFragment = new CenterFragment();
        mMineFragment = new MineFragment();

        loadMultipleRootFragment(R.id.fl_main_content, 0, mHomeFragment, mCenterFragment, mMineFragment);
        onSelectedHome();
    }


    @Override
    public void onClick(View v) {
        if (v == mLlHome) {
            onSelectedHome();
        } else if (v == mLlCenter) {
            onSelectedCenter();
        }else if (v == mLlMine) {
            onSelectedMine();
        }
    }


    private void onSelectedHome() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        selectedBottom(mLlHome);
        showHideFragment(mHomeFragment);
    }

    private void onSelectedCenter() {
        /*if (!App.getInstance().isLogin()) {
            UIHelper.jumpToLogin(this);
            return;
        }*/
        if (mCenterFragment == null) {
            mCenterFragment = new CenterFragment();
        }
        selectedBottom(mLlCenter);
        showHideFragment(mCenterFragment);
    }

    private void onSelectedMine() {
        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
        }
        selectedBottom(mLlMine);
        showHideFragment(mMineFragment);
    }

    private void selectedBottom(LinearLayout linearLayout) {
        if (linearLayout == mLlHome) {

            mLlHome.setSelected(true);
            mLlCenter.setSelected(false);
            mLlMine.setSelected(false);
        } else if (linearLayout == mLlCenter) {

            mLlHome.setSelected(false);
            mLlCenter.setSelected(true);
            mLlMine.setSelected(false);
        }else if (linearLayout == mLlMine) {

            mLlHome.setSelected(false);
            mLlCenter.setSelected(false);
            mLlMine.setSelected(true);
        }
    }

}