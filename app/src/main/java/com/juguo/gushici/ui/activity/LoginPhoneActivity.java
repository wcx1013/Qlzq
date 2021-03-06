package com.juguo.gushici.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.dragger.bean.User;
import com.juguo.gushici.dragger.bean.UserInfo;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.ui.activity.contract.LoginContract;
import com.juguo.gushici.ui.activity.presenter.LoginPresenter;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.TitleBarUtils;
import com.juguo.gushici.utils.ToastUtils;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginPhoneActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {

    private EditText mEditPhone;
    private EditText mEditPassword;
    private TextView mTvLogin;
    private TextView mTvJumpRegister;
    private MySharedPreferences mySharedPreferences;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==10){
            setResult(10);
            finish();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login_phone;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mySharedPreferences = new MySharedPreferences(this, "Shared");
        mEditPhone = findViewById(R.id.edit_phone);
        mEditPassword = findViewById(R.id.edit_password);
        mTvLogin = findViewById(R.id.tv_login);
        mTvJumpRegister = findViewById(R.id.tv_jump_register);
        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setMiddleTitleText("");
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvJumpRegister.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (mTvLogin == v) {
            login();
        } else if (mTvJumpRegister == v) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent,0);
        }
    }

    private void login() {

        String phone = mEditPhone.getText().toString();
        String password = mEditPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {

            ToastUtils.shortShowStr(this, "??????????????????");
            return;
        }
        if (TextUtils.isEmpty(password)) {

            ToastUtils.shortShowStr(this, "???????????????");
            return;
        }

        User user = new User();
        UserInfo userInfo = new UserInfo();
        userInfo.setType(0);

        userInfo.setAccount(phone);
        userInfo.setPassword(password);
        userInfo.setAppId(Constants.WX_APP_ID);
        user.setParam(userInfo);
        mPresenter.login(user);
    }

    @Override
    public void httpCallback(LoginResponse loginResponse) {
        if (loginResponse.isSuccess()) {
            String result = loginResponse.getResult();
            if (!TextUtils.isEmpty(result)) {
                mySharedPreferences.putValue("token", result);

                // ??????????????????
                mPresenter.getAccountInformation();
            } else {
                // ??????????????????
                mySharedPreferences.putValue("loginType", "");
                ToastUtils.shortShowStr(LoginPhoneActivity.this,"????????????");
            }
        } else {
            // ??????????????????
            mySharedPreferences.putValue("loginType", "");
            ToastUtils.shortShowStr(LoginPhoneActivity.this, "????????????");
        }
    }

    @Override
    public void httpCallback(AccountInformationResponse response) {
        if (response.isSuccess()) {
            AccountInformationResponse.AccountInformationInfo result = response.getResult();
            if (result != null) {
                String level = result.getLevel();
                String dueTime = result.getDueTime();
                // ????????????????????????
                // mySharedPreferences.putValue("userIcon", userIcon);
                // ?????????????????????
                mySharedPreferences.putValue("userName", mEditPhone.getText().toString());
                // ??????????????????
                mySharedPreferences.putValue("loginType", "0");
                // ??????????????????
                mySharedPreferences.putValue("isLogin", true);
                // ????????????????????????
                mySharedPreferences.putValue("userId", mEditPhone.getText().toString());
                mySharedPreferences.putValue("MemberUser", result.getId());
                mySharedPreferences.putValue("level", level);
                mySharedPreferences.putValue("dueTime", dueTime);
                mySharedPreferences.putValue("account", mEditPhone.getText().toString());
                mySharedPreferences.putValue("password", mEditPassword.getText().toString());

//                if (!TextUtils.isEmpty(level)) {
//                    // ???????????????
//                    tz(level, dueTime);
//                } else {
//                    // ???????????????
//                    tz("", "");
//                }
                setResult(10);
                finish();
            }
        }
    }

    @Override
    public void httpError(String e) {

    }
}
