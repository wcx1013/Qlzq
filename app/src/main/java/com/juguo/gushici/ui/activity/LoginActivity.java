package com.juguo.gushici.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

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
import com.juguo.gushici.utils.ToastUtils;
import com.juguo.gushici.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @Author: Administrator
 * @Time: 2019/7/29 15:00
 * @Company：ch
 * @Description: 登录页面
 */

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {

    private MySharedPreferences mySharedPreferences;
    private Context mContext;
    // 登录用户图标
    private String userIcon;
    // 登录用户名昵称
    private String userName;
    // 登录类型
    private String loginType = null;
    // 用户唯一信息
    private String userId;

    @BindView(R.id.img_select)
    ImageView img_select;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    User user = (User) msg.getData().getSerializable("loginParam");
                    mPresenter.login(user);
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void initViewAndData() {
        initData();
    }

    private void initData() {
        mContext = this;
        mySharedPreferences = new MySharedPreferences(this, "Shared");

    }

    @OnClick({R.id.wx_login, R.id.qq_login, R.id.ll_ty, R.id.tv_yhxy, R.id.tv_yszy})
    public void btn_Login_Click(View v) {
        switch (v.getId()) {
            case R.id.wx_login:
                // 微信登录
                if (!img_select.isSelected()) {
                    ToastUtils.shortShowStr(mContext, "请选择同意");
                    return;
                }
                if (Util.isWeixinAvilible(mContext)) {
                    qqLogin(Wechat.NAME);
                    loginType = Wechat.NAME;
                } else {
                    ToastUtils.shortShowStr(mContext, "请先安装微信客户端");
                }
                break;
            case R.id.qq_login:
                // qq登录
                if (!img_select.isSelected()) {
                    ToastUtils.shortShowStr(mContext, "请选择同意");
                    return;
                }
                if (Util.isQQClientAvailable(mContext)) {
                    qqLogin(QQ.NAME);
                    loginType = QQ.NAME;
                } else {
                    ToastUtils.shortShowStr(mContext, "请先安装QQ客户端");
                }
                break;
            case R.id.ll_ty:
                // 是否同意
                if (img_select.isSelected()) {
                    img_select.setSelected(false);
                } else {
                    img_select.setSelected(true);
                }
                break;
            case R.id.tv_yhxy:
                // 用户协议
                Intent intent = new Intent(mContext, WebUrlActivity.class);
                intent.putExtra("url", "file:///android_asset/UserLicenseAgreement.html");
                intent.putExtra("title", "用户许可协议");
                startActivity(intent);
                break;
            case R.id.tv_yszy:
                // 隐私保护指引
                Intent intent1 = new Intent(mContext, WebUrlActivity.class);
                intent1.putExtra("url", "file:///android_asset/PrivacyGuidelines.html");
                intent1.putExtra("title", "隐私保护指引");
                startActivity(intent1);
                break;
        }
    }

    private void qqLogin(String str) {
        Platform platform = ShareSDK.getPlatform(str);
        ShareSDK.setActivity(this);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
                if (action == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    //通过DB获取各种数据
                    String token = platDB.getToken();
                    String userGender = platDB.getUserGender();
                    userIcon = platDB.getUserIcon();
                    userId = platDB.getUserId();
                    userName = platDB.getUserName();

                    User user = new User();
                    UserInfo userInfo = new UserInfo();
                    if (QQ.NAME.equals(str)) {
                        userInfo.setType(4);
                    } else if (Wechat.NAME.equals(str)) {
                        userInfo.setType(3);
                    }
                    userInfo.setUnionInfo(userId);
                    userInfo.setNickName(userName);
                    userInfo.setHeadImgUrl(userIcon);
                    userInfo.setAppId(Constants.WX_APP_ID);
                    user.setParam(userInfo);

                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("loginParam", (Serializable) user);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
        platform.showUser(null);
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
                ToastUtils.shortShowStr(mContext, getResources().getString(R.string.login_failure));
            }
        } else {
            // 保存登录类型
            mySharedPreferences.putValue("loginType", "");
            ToastUtils.shortShowStr(mContext, getResources().getString(R.string.login_failure));
        }
    }

    @Override
    public void httpError(String e) {
        mySharedPreferences.putValue("loginType", "");
        ToastUtils.shortShowStr(mContext, getResources().getString(R.string.erro));
    }

    /**
     * 获取用户账户信息
     *
     * @param response
     */
    @Override
    public void httpCallback(AccountInformationResponse response) {
        if (response.isSuccess()) {
            AccountInformationResponse.AccountInformationInfo result = response.getResult();
            if (result != null) {
                String level = result.getLevel();
                String dueTime = result.getDueTime();
                // 保存登录用户图标
                mySharedPreferences.putValue("userIcon", userIcon);
                // 保存登录用户名
                mySharedPreferences.putValue("userName", userName);
                // 保存登录类型
                mySharedPreferences.putValue("loginType", loginType);
                // 保存登录状态
                mySharedPreferences.putValue("isLogin", true);
                // 保存用户唯一信息
                mySharedPreferences.putValue("userId", userId);
                mySharedPreferences.putValue("MemberUser", result.getId());
                mySharedPreferences.putValue("level", level);
                mySharedPreferences.putValue("dueTime", dueTime);

                if (!TextUtils.isEmpty(level)) {
                    // 已开通会员
                    tz(level, dueTime);
                } else {
                    // 未开通会员
                    tz("", "");
                }
            }
        }
    }

    private void tz(String level, String dueTime) {
        /*EventBusMessage eventBusMessage = new EventBusMessage();
        eventBusMessage.setUserIcon(userIcon);
        eventBusMessage.setUserName(userName);
        eventBusMessage.setLevel(level);
        eventBusMessage.setDueTime(dueTime);
        EventBus.getDefault().post(eventBusMessage);

        // 登录成功关闭未提示
        CloseTsMessage closeTsMessage = new CloseTsMessage();
        closeTsMessage.setTs(true);
        EventBus.getDefault().post(closeTsMessage);

        Intent inten = new Intent(com.juguo.officefamily.ui.activity.LoginActivity.this, MainActivity.class);
        startActivity(inten);
        finish();*/
    }
}
