package com.juguo.gushici.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.RegisterBean;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.ui.activity.contract.RegisterContract;
import com.juguo.gushici.ui.activity.presenter.RegisterPresenter;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.TitleBarUtils;
import com.juguo.gushici.utils.ToastUtils;

public class RegisterActivity extends BaseMvpActivity<RegisterPresenter> implements RegisterContract.View {

    private EditText mEditPhone;
    private EditText mEditPassword;
    private EditText mEditConfirmPassword;
    private TextView mTvRegister;
    private MySharedPreferences mySharedPreferences;


    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mySharedPreferences = new MySharedPreferences(this, "Shared");
        mTvRegister = findViewById(R.id.tv_register);
        mEditPhone = findViewById(R.id.edit_phone);
        mEditPassword = findViewById(R.id.edit_password);
        mEditConfirmPassword = findViewById(R.id.edit_confirm_password);
        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setMiddleTitleText("注册");
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestRegister();
            }
        });
    }

    private void requestRegister() {
        String phone = mEditPhone.getText().toString();
        String password = mEditPassword.getText().toString();
        String confirmPassword = mEditConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(phone)) {

            ToastUtils.shortShowStr(this, "请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {

            ToastUtils.shortShowStr(this, "请输入密码");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {

            ToastUtils.shortShowStr(this, "请再次输入密码");
            return;
        }
        if(!password.equals(confirmPassword)){

            ToastUtils.shortShowStr(this, "两次密码输入不一致");
            return;
        }
        RegisterBean registerBean = new RegisterBean();
        RegisterBean.Bean bean = new RegisterBean.Bean();
        bean.setAccount(phone);
        bean.setPassword(password);
        bean.setAppId(Constants.WX_APP_ID);
        registerBean.setParam(bean);
        mPresenter.register(registerBean);
    }


    @Override
    public void httpError(String e) {

    }

    @Override
    public void httpCallback(BaseResponse o) {

    }

    @Override
    public void httpCallback(LoginResponse loginResponse) {

        if (loginResponse.isSuccess()) {
            String result = loginResponse.getResult();
            if (!TextUtils.isEmpty(result)) {
                mySharedPreferences.putValue("token", result);

                // 获取用户信息
                mPresenter.getAccountInformation();
            } else {
                // 保存登录类型
                mySharedPreferences.putValue("loginType", "");
                ToastUtils.shortShowStr(RegisterActivity.this,"注册失败");
            }
        } else {
            // 保存登录类型
            mySharedPreferences.putValue("loginType", "");
            ToastUtils.shortShowStr(RegisterActivity.this, "注册失败");
        }
    }

    @Override
    public void httpCallback(AccountInformationResponse response) {
        if (response.isSuccess()) {
            AccountInformationResponse.AccountInformationInfo result = response.getResult();
            if (result != null) {
                String level = result.getLevel();
                String dueTime = result.getDueTime();
                // 保存登录用户图标
               // mySharedPreferences.putValue("userIcon", userIcon);
                // 保存登录用户名
                mySharedPreferences.putValue("userName", mEditPhone.getText().toString());
                // 保存登录类型
                mySharedPreferences.putValue("loginType", "0");
                // 保存登录状态
                mySharedPreferences.putValue("isLogin", true);
                // 保存用户唯一信息
                mySharedPreferences.putValue("userId", mEditPhone.getText().toString());
                mySharedPreferences.putValue("MemberUser", result.getId());
                mySharedPreferences.putValue("level", level);
                mySharedPreferences.putValue("dueTime", dueTime);
                mySharedPreferences.putValue("account", mEditPhone.getText().toString());
                mySharedPreferences.putValue("password", mEditConfirmPassword.getText().toString());

//                if (!TextUtils.isEmpty(level)) {
//                    // 已开通会员
//                    tz(level, dueTime);
//                } else {
//                    // 未开通会员
//                    tz("", "");
//                }
                setResult(10);
                finish();
            }
        }
    }
}