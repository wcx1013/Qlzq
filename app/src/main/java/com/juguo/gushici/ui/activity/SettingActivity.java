package com.juguo.gushici.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.CloseTsMessage;
import com.juguo.gushici.ui.activity.contract.SettingContract;
import com.juguo.gushici.ui.activity.presenter.SettingPresenter;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.TitleBarUtils;
import com.juguo.gushici.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 设置
 */
public class SettingActivity extends BaseMvpActivity<SettingPresenter> implements SettingContract.View {

    @BindView(R.id.rl_tcdl)
    RelativeLayout rl_tcdl;
    @BindView(R.id.rl_zx)
    RelativeLayout rl_zx;

    private Context mContext;
    private MySharedPreferences mySharedPreferences;
    private boolean isLogout = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {
        mContext = this;

        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setMiddleTitleText("设置");
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mySharedPreferences = new MySharedPreferences(mContext, "Shared");

        rl_tcdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogout = true;
                logout();
            }
        });

        rl_zx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogout = false;
                logout();
            }
        });
    }

    /**
     * 退出登录
     */
    private void logout() {
        if (CommUtils.isLogin(mContext)) {
            mPresenter.logOut();
        } else {
            ToastUtils.shortShowStr(mContext, "你还没有登录哟！！！");
        }
    }

    @Override
    public void httpCallback(BaseResponse response) {
        if (!response.isSuccess()) {
            ToastUtils.shortShowStr(mContext, response.getMsg());
        } else {
            CloseTsMessage closeTsMessage = new CloseTsMessage();
            closeTsMessage.setTs(false);
            EventBus.getDefault().post(closeTsMessage);
            String loginType = (String) mySharedPreferences.getValue("loginType", "");
            loginOut(loginType);
            MobclickAgent.onProfileSignOff();
        }
    }

    /**
     * 退出登录
     */
    private void loginOut(String loginType) {
        if (QQ.NAME.equals(loginType)) {
            // QQ退出
            Platform platform = ShareSDK.getPlatform(QQ.NAME);
            platform.removeAccount(true);
            if (isLogout) {
                ToastUtils.shortShowStr(mContext, "退出登录成功");
            }else {
                ToastUtils.shortShowStr(mContext, "注销成功");
            }
        } else if (Wechat.NAME.equals(loginType)) {
            // 微信退出
            Platform platform = ShareSDK.getPlatform(Wechat.NAME);
            platform.removeAccount(true);
            if (isLogout) {
                ToastUtils.shortShowStr(mContext, "退出登录成功");
            }else {
                ToastUtils.shortShowStr(mContext, "注销成功");
            }
        } else {
            Intent intent = new Intent(mContext, NotLoginActivity.class);
            startActivity(intent);
        }
        clearSP();
        setResult(10);
        finish();
    }

    /**
     * 清楚相应的sp中缓存数据
     */
    private void clearSP() {
//        mySharedPreferences.remove("token");
        mySharedPreferences.remove("userIcon");
        mySharedPreferences.remove("userName");
        mySharedPreferences.remove("loginType");
        mySharedPreferences.remove("isLogin");
        mySharedPreferences.remove("userId");
        mySharedPreferences.remove("MemberUser");
        mySharedPreferences.remove("level");
        mySharedPreferences.remove("dueTime");
        mySharedPreferences.remove("isYg");

//        removeALLActivity();
    }

    @Override
    public void httpError(String e) {
        ToastUtils.shortShowStr(mContext, e);
    }
}
