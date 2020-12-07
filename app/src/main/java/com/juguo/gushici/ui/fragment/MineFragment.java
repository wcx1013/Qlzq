package com.juguo.gushici.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpFragment;
import com.juguo.gushici.bean.AppConfigBean;
import com.juguo.gushici.bean.VersionUpdataBean;
import com.juguo.gushici.response.VersionUpdataResponse;
import com.juguo.gushici.ui.activity.AboutUsActivity;
import com.juguo.gushici.ui.activity.HelpFeedbackActivity;
import com.juguo.gushici.ui.activity.LoginActivity;
import com.juguo.gushici.ui.activity.SettingActivity;
import com.juguo.gushici.ui.activity.WebUrlActivity;
import com.juguo.gushici.ui.activity.contract.MineContract;
import com.juguo.gushici.ui.activity.presenter.MinePresenter;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.ToastUtils;
import com.juguo.gushici.utils.Util;
import com.juguo.gushici.view.XCRoundImageView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import constant.UiType;
import model.UiConfig;
import model.UpdateConfig;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import update.UpdateAppUtils;

@RuntimePermissions
public class MineFragment extends BaseMvpFragment<MinePresenter> implements MineContract.View {

    @BindView(R.id.img_user)
    public XCRoundImageView img_user;
    @BindView(R.id.tv_user_name)
    public TextView tv_user_name;
    @BindView(R.id.tv_vip)
    public TextView tv_vip;
    @BindView(R.id.tv_vesion)
    public TextView tv_vesion;
    @BindView(R.id.ll_view)
    public LinearLayout ll_view;

    private Context mContext;
    //private ArrayList<MarketPkgsBean> installedMarketPkgs;
    private MySharedPreferences mySharedPreferences;

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mContext = getBindingActivity();
        // 设置布局在状态下方加载
        CommUtils.setImmerseLayout(ll_view, getBindingActivity());
        mySharedPreferences = new MySharedPreferences(mContext, "Shared");
//        EventBus.getDefault().register(this);

        initView();
    }

    private void initView() {

        // 获取手机应用商店
        //installedMarketPkgs = CommUtils.getInstalledMarketPkgs(mContext);

        if (CommUtils.isLogin(mContext)) {
            String userIcon = (String) mySharedPreferences.getValue("userIcon", "");
            String userName = (String) mySharedPreferences.getValue("userName", "");

            // 设置用户名和用户icon
            tv_user_name.setText(userName);
            Util.displayCircleCropImgView(mContext, img_user, userIcon, R.mipmap.ic_user_place);
        }
        tv_vesion.setText(CommUtils.getVersionName(mContext));
    }

    @OnClick({  R.id.rl_gwhp, R.id.rl_bzfk, R.id.rl_fxhy,
            R.id.rl_yszc, R.id.rl_gywm, R.id.img_setting, R.id.rl_bbgx,
             R.id.ll_login})
    public void btn_Login_Click(View v) {
        switch (v.getId()) {

            case R.id.rl_bzfk:
                // 帮助反馈
                Intent intent8 = new Intent(mContext, HelpFeedbackActivity.class);
                startActivity(intent8);
                // 跳转至qq聊天窗口
//                if (Util.isQQClientAvailable(mContext)) {
//                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=2058582947";
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                } else {
//                    ToastUtils.shortShowStr(mContext, "请先安装QQ");
//                }
                break;
            case R.id.rl_yszc:
                // 隐私政策
                Intent intent7 = new Intent(mContext, WebUrlActivity.class);
                intent7.putExtra("url", "file:///android_asset/PrivacyAgreement.html");
                intent7.putExtra("title", "隐私政策");
                startActivity(intent7);
                break;
            case R.id.rl_gywm:
                // 关于我们
                Intent intent = new Intent(mContext, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.img_setting:
                // 设置
                Intent intent6 = new Intent(mContext, SettingActivity.class);
                startActivityForResult(intent6, 10);
                break;
            case R.id.rl_fxhy:
                // 分享好友
                shareSelectDialog();
                break;
            case R.id.rl_bbgx:
                // 版本更新
                MineFragmentPermissionsDispatcher.initPermissonWithPermissionCheck(this);
                break;

            case R.id.ll_login:
                // 登录
                if (!CommUtils.isLogin(mContext)) {
                    Intent intent5 = new Intent(mContext, LoginActivity.class);
                    startActivity(intent5);
                }
                break;
        }
    }



    /**
     * 点击分享底部弹窗
     */
    private void shareSelectDialog() {
        final AlertDialog dialog;
        View diaView = View.inflate(mContext, R.layout.spfx_popupwindow, null);
        LinearLayout ll_qq = diaView.findViewById(R.id.ll_qq);
        LinearLayout ll_wx = diaView.findViewById(R.id.ll_wx);
        LinearLayout ll_pyq = diaView.findViewById(R.id.ll_pyq);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(diaView);
        dialog = builder.create();
        dialog.show();

        Display display = getBindingActivity().getWindowManager().getDefaultDisplay();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setWindowAnimations(R.style.popupAnimation);
        lp.gravity = Gravity.BOTTOM;
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);

        // QQ分享
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isQQClientAvailable(mContext)) {
                    share(QQ.NAME);
                } else {
                    ToastUtils.shortShowStr(mContext, "请先安装QQ应用");
                }
                dialog.dismiss();
            }
        });

        // 微信分享
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isWeixinAvilible(mContext)) {
                    share(Wechat.NAME);
                } else {
                    ToastUtils.shortShowStr(mContext, "请先安装微信应用");
                }
                dialog.dismiss();
            }
        });

        // 微信朋友圈分享
        ll_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isWeixinAvilible(mContext)) {
                    share(WechatMoments.NAME);
                } else {
                    ToastUtils.shortShowStr(mContext, "请先安装微信应用");
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 分享
     */
    private void share(String str) {
        String apkUrl = (String) mySharedPreferences.getValue("apkUrl", "");
        String assetsResource = Util.getAssetsResource(mContext, "share_icon.png", R.mipmap.ic_launcher);
        Platform platform = ShareSDK.getPlatform(str);
        Platform.ShareParams shareParams = new Platform.ShareParams();
//        shareParams.setText();
        shareParams.setTitle(getString(R.string.app_name));
        if (TextUtils.isEmpty(apkUrl)) {
            apkUrl = "http://app.91juguo.com/group1/M00/00/00/dxchw18ePbyAT1syAG8-z7YfnuM106.apk";
        }
        shareParams.setUrl(apkUrl);
        shareParams.setImagePath(assetsResource);
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastUtils.shortShowStr(mContext, "分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.shortShowStr(mContext, "分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.shortShowStr(mContext, "取消分享");
            }
        });
        platform.share(shareParams);
    }

    @Override
    public void onResume() {
        super.onResume();
        showHyXx();
        MobclickAgent.onPageStart("我的Fragment");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            showHyXx();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的Fragment");
    }

    private void showHyXx() {
        /*level = (String) mySharedPreferences.getValue("level", "");
        String dueTime = (String) mySharedPreferences.getValue("dueTime", "");
        String str = showTvVip(level, dueTime);
        tv_vip.setText(str);*/
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusMessage event) {
        *//*if (!TextUtils.isEmpty(event.getUserName())) {
            tv_user_name.setText(event.getUserName());
        }
        if (!TextUtils.isEmpty(event.getUserIcon())) {
            Util.displayCircleCropImgView(mContext, img_user, event.getUserIcon(), R.mipmap.user_img);
        }*//*
    }*/

    /**
     * 版本更新
     *
     * @param response
     */
    @Override
    public void httpCallback(VersionUpdataResponse response) {
        if (response.isSuccess()) {
            VersionUpdataResponse.VersionUpdataInfo result = response.getResult();
            if (result != null) {
                String appConfig = result.getAppConfig();
                if (!TextUtils.isEmpty(appConfig)) {
                    AppConfigBean appConfigBean = JSON.parseObject(appConfig, AppConfigBean.class);
                    String ifPay = appConfigBean.getIfPay();
                    mySharedPreferences.putValue("isOpenMember", ifPay);
                }
                VersionUpdataResponse.UpdateVInfo updateV = result.getUpdateV();
                if (updateV != null) {
                    if (!TextUtils.isEmpty(updateV.getUrl())) {
                        mySharedPreferences.putValue("apkUrl", updateV.getUrl());
                        String desc = result.getvRemark();
                        String vIfForceUpd = result.getvType();
                        UiConfig uiConfig = new UiConfig();
                        uiConfig.setUiType(UiType.CUSTOM);
                        uiConfig.setCustomLayoutId(R.layout.view_update_version);
                        UpdateConfig updateConfig = new UpdateConfig();
                        // 是否强制更新
                        if ("1".equals(vIfForceUpd)) {
                            updateConfig.setForce(true);
                        } else {
                            updateConfig.setForce(false);
                        }

                        UpdateAppUtils
                                .getInstance()
                                .apkUrl(updateV.getUrl())
                                .uiConfig(uiConfig)
                                .updateConfig(updateConfig)
                                .update();
                    } else {
                        ToastUtils.shortShowStr(mContext, "已经是最新版本");
                    }
                } else {
                    ToastUtils.shortShowStr(mContext, "已经是最新版本");
                }
            } else {
                ToastUtils.shortShowStr(mContext, "已经是最新版本");
            }
        }
    }


    @Override
    public void httpError(String e) {
        ToastUtils.shortShowStr(mContext, getResources().getString(R.string.erro));
    }

    //有权限时会直接调用改方法，没权限时，会在申请通过后调用
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void initPermisson() {
        getGetAppVersion();
    }

    /**
     * 获取app的版本信息
     */
    private void getGetAppVersion() {
        if (!CommUtils.isNetworkAvailable(mContext)) {
            ToastUtils.shortShowStr(getBindingActivity(), Constants.NET_ERROR);
            return;
        }
        VersionUpdataBean versionUpdataBean = new VersionUpdataBean();
        versionUpdataBean.setParam(new VersionUpdataBean.VersionUpdataInfo(Constants.WX_APP_ID, CommUtils.getVersionName(mContext)));
        mPresenter.settingVersion(versionUpdataBean);
    }

    //重写该方法之后，当弹出授权对话框时，我们点击允许授权成功时，会自动执行注解@NeedsPermission所标注的方法里面的逻辑
    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MineFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 被用户拒绝
     */
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        ToastUtils.shortShowStr(mContext, "权限未授予，部分功能无法使用");
    }
}
