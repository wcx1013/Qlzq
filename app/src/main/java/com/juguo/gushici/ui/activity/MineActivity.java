package com.juguo.gushici.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.AddPayOrderBean;
import com.juguo.gushici.bean.AppConfigBean;
import com.juguo.gushici.bean.CloseTsMessage;
import com.juguo.gushici.bean.EventBusMessage;
import com.juguo.gushici.bean.MarketPkgsBean;
import com.juguo.gushici.bean.NewWXSignOrderBean;
import com.juguo.gushici.bean.SignOrderBean;
import com.juguo.gushici.bean.VersionUpdataBean;
import com.juguo.gushici.bean.WxPayMessageBean;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.AddPayOrderResponse;
import com.juguo.gushici.response.MemberLevelResponse;
import com.juguo.gushici.response.QueryOrderResponse;
import com.juguo.gushici.response.VersionUpdataResponse;
import com.juguo.gushici.ui.activity.contract.MineContract;
import com.juguo.gushici.ui.activity.presenter.MinePresenter;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.GwhpPopupwindowAdapter;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.NoScrollGridView;
import com.juguo.gushici.utils.PayResult;
import com.juguo.gushici.utils.TitleBarUtils;
import com.juguo.gushici.utils.ToastUtils;
import com.juguo.gushici.utils.Util;
import com.juguo.gushici.utils.WeChatField;
import com.juguo.gushici.view.XCRoundImageView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
public class MineActivity extends BaseMvpActivity<MinePresenter> implements MineContract.View {

    @BindView(R.id.img_user)
    public XCRoundImageView img_user;
    @BindView(R.id.tv_user_name)
    public TextView tv_user_name;
    @BindView(R.id.tv_vesion)
    public TextView tv_vesion;
    @BindView(R.id.ll_view)
    public LinearLayout ll_view;
    @BindView(R.id.rl_tcdl)
    RelativeLayout rl_tcdl;
    @BindView(R.id.rl_zx)
    RelativeLayout rl_zx;
    @BindView(R.id.iv_buy_vip)
    ImageView mIvBuyVip;
    @BindView(R.id.ll_vip)
    LinearLayout mLlVip;
    @BindView(R.id.tv_vip)
    TextView mTvVip;

    private Context mContext;
    //private ArrayList<MarketPkgsBean> installedMarketPkgs;
    private MySharedPreferences mySharedPreferences;
    private boolean isLogout = false;

    private String level;
    private String isOpenMember;
    // ????????????
    private int zfType = 1;
    // ????????????
    private String hyType = "????????????";
    private String prodCode;
    private String orderId;
    private String yjCode;
    private int querySum = 3;
    private final int SDK_PAY_FLAG = 1;
    private String mPrice;
    private String mPriceYj;
    private String mPriceNd;
    private int mOriginalPriceYj;//????????????
    private int mOriginalPriceNd;//????????????
    private String resId;
    private String ndCode;
    private String aliPay;
    private String wxPay;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    switch (payResult.getResultStatus()) {
                        case "9000":
                            MobclickAgent.onEvent(mContext, "payment_success", "????????????");
                            mPresenter.queryOrder(orderId);
                            break;
                        case "6004":
                        case "8000":
                            ToastUtils.shortShowStr(mContext, "???????????????");
                            break;
                        case "4000":
                            MobclickAgent.onEvent(mContext, "payment_fali", "????????????");
                            ToastUtils.shortShowStr(mContext, "??????????????????");
                            break;
                        case "5000":
                            ToastUtils.shortShowStr(mContext, "????????????");
                            break;
                        case "6001":
                            ToastUtils.shortShowStr(mContext, "???????????????");
                            break;
                        case "6002":
                            ToastUtils.shortShowStr(mContext, "??????????????????");
                            break;
                        default:
                            MobclickAgent.onEvent(mContext, "payment_fali", "????????????");
                            ToastUtils.shortShowStr(mContext, "????????????");
                            break;
                    }
                }
            }
            return false;
        }
    });

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mContext = this;
        // ?????????????????????????????????D
        //CommUtils.setImmerseLayout(ll_view, this);
        mIvBuyVip = findViewById(R.id.iv_buy_vip);
        mySharedPreferences = new MySharedPreferences(mContext, "Shared");
//        EventBus.getDefault().register(this);
        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.getTitleView().setBackgroundColor(Color.TRANSPARENT);
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        mPresenter.getMemberLevel();
        mPresenter.getAccountInformation();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void initView() {

        // ????????????????????????
        //installedMarketPkgs = CommUtils.getInstalledMarketPkgs(mContext);

        aliPay = (String) mySharedPreferences.getValue("ali", "");
        wxPay = (String) mySharedPreferences.getValue("wx", "");
        if (CommUtils.isLogin(mContext)) {
            String userIcon = (String) mySharedPreferences.getValue("userIcon", "");
            String userName = (String) mySharedPreferences.getValue("userName", "");
            String level = (String) mySharedPreferences.getValue("level", "");
            String dueTime= (String) mySharedPreferences.getValue("dueTime","");
            long count = Util.timeCompare(dueTime);

            // ????????????????????????icon
            tv_user_name.setText(userName);
            Util.displayCircleCropImgView(mContext, img_user, userIcon, R.mipmap.ic_user_place);
            if (pageTo()) {
                mIvBuyVip.setVisibility(View.GONE);
                mLlVip.setVisibility(View.VISIBLE);
            } else {
                mIvBuyVip.setVisibility(View.VISIBLE);
                mLlVip.setVisibility(View.GONE);
            }
            if ("9".equals(level)) {
                mTvVip.setText("????????????");
            } else if ("4".equals(level)) {
                String tip="";
                // ????????????
                if (count <= 0) {
                    tip= "VIP/???????????????????????????????????????";
                    mIvBuyVip.setVisibility(View.VISIBLE);
                    mLlVip.setVisibility(View.VISIBLE);
                } else {
                    tip= String.format("????????????/??????%d???", count);
                }
                mTvVip.setText(tip);
            }
        } else {
            mIvBuyVip.setVisibility(View.VISIBLE);
            mLlVip.setVisibility(View.GONE);
            tv_user_name.setText("???????????????????????????");
            img_user.setImageDrawable(getResources().getDrawable(R.mipmap.ic_user_place));
        }
        tv_vesion.setText(CommUtils.getVersionName(mContext));
        // ????????????????????????
        installedMarketPkgs = CommUtils.getInstalledMarketPkgs(mContext);
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
     * ????????????
     */
    private void logout() {
        if (CommUtils.isLogin(mContext)) {
            mPresenter.logOut();
        } else {
            ToastUtils.shortShowStr(mContext, "??????????????????????????????");
        }
    }


    private ArrayList<MarketPkgsBean> installedMarketPkgs;

    @OnClick({R.id.rl_gwhp, R.id.rl_bzfk, R.id.rl_fxhy,
            R.id.rl_yszc, R.id.rl_gywm, R.id.rl_bbgx,
            R.id.ll_login, R.id.iv_buy_vip})
    public void btn_Login_Click(View v) {
        switch (v.getId()) {
            case R.id.iv_buy_vip:
                showVipDialog();
                break;
            case R.id.rl_gwhp:
                // ????????????
//                showDialog();
                if (installedMarketPkgs != null && installedMarketPkgs.size() > 0) {
                    showSelectDialog();
                } else {
                    ToastUtils.shortShowStr(mContext, "????????????????????????");
                }
                break;
            case R.id.rl_bzfk:
                // ????????????
                Intent intent8 = new Intent(mContext, HelpFeedbackActivity.class);
                startActivity(intent8);
                // ?????????qq????????????
//                if (Util.isQQClientAvailable(mContext)) {
//                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=2058582947";
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                } else {
//                    ToastUtils.shortShowStr(mContext, "????????????QQ");
//                }
                break;
            case R.id.rl_yszc:
                // ????????????
                Intent intent7 = new Intent(mContext, WebUrlActivity.class);
                intent7.putExtra("url", "file:///android_asset/PrivacyAgreement.html");
                intent7.putExtra("title", "????????????");
                startActivity(intent7);
                break;
            case R.id.rl_gywm:
                // ????????????
                Intent intent = new Intent(mContext, AboutUsActivity.class);
                startActivity(intent);
                break;
           /* case R.id.img_setting:
                // ??????
                Intent intent6 = new Intent(mContext, SettingActivity.class);
                startActivityForResult(intent6, 10);
                break;*/
            case R.id.rl_fxhy:
                // ????????????
                shareSelectDialog();
                break;
            case R.id.rl_bbgx:
                // ????????????
                MineActivityPermissionsDispatcher.initPermissonWithPermissionCheck(this);
                break;

            case R.id.ll_login:
                // ??????
                if (!CommUtils.isLogin(mContext)) {
                    Intent intent5 = new Intent(mContext, LoginActivity.class);
                    startActivity(intent5);
                } else {
                    Intent intent6 = new Intent(mContext, EditUserInfoActivity.class);
                    startActivity(intent6);
                }
                break;
        }
    }

    /**
     * ????????????????????????
     */
    private void showSelectDialog() {
        final AlertDialog dialog;
        View diaView = View.inflate(mContext, R.layout.gwhp_popupwindow, null);
        NoScrollGridView grid_view = diaView.findViewById(R.id.grid_view);

        GwhpPopupwindowAdapter gwhpPopupwindowAdapter = new GwhpPopupwindowAdapter(mContext, installedMarketPkgs);
        grid_view.setAdapter(gwhpPopupwindowAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(diaView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Display display = getWindowManager().getDefaultDisplay();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setWindowAnimations(R.style.popupAnimation);
        lp.gravity = Gravity.BOTTOM;
        lp.width = (int) (display.getWidth()); //????????????
        dialog.getWindow().setAttributes(lp);

        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ??????????????????
                MarketPkgsBean marketPkgsBean = installedMarketPkgs.get(position);
                CommUtils.launchAppDetail(mContext, CommUtils.getApkPkgName(mContext), marketPkgsBean.getPkgName());
                dialog.dismiss();
            }
        });
    }

    /**
     * ????????????????????????
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

        Display display = getWindowManager().getDefaultDisplay();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setWindowAnimations(R.style.popupAnimation);
        lp.gravity = Gravity.BOTTOM;
        lp.width = (int) (display.getWidth()); //????????????
        dialog.getWindow().setAttributes(lp);

        // QQ??????
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isQQClientAvailable(mContext)) {
                    share(QQ.NAME);
                } else {
                    ToastUtils.shortShowStr(mContext, "????????????QQ??????");
                }
                dialog.dismiss();
            }
        });

        // ????????????
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isWeixinAvilible(mContext)) {
                    share(Wechat.NAME);
                } else {
                    ToastUtils.shortShowStr(mContext, "????????????????????????");
                }
                dialog.dismiss();
            }
        });

        // ?????????????????????
        ll_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isWeixinAvilible(mContext)) {
                    share(WechatMoments.NAME);
                } else {
                    ToastUtils.shortShowStr(mContext, "????????????????????????");
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * ??????
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
                ToastUtils.shortShowStr(mContext, "????????????");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.shortShowStr(mContext, "????????????");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.shortShowStr(mContext, "????????????");
            }
        });
        platform.share(shareParams);
    }

    @Override
    public void onResume() {
        super.onResume();
        showHyXx();
        MobclickAgent.onPageStart("??????Fragment");
    }

   /* @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            showHyXx();
        }
    }*/

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("??????Fragment");
    }

    private void showHyXx() {
        /*level = (String) mySharedPreferences.getValue("level", "");
        String dueTime = (String) mySharedPreferences.getValue("dueTime", "");
        String str = showTvVip(level, dueTime);
        tv_vip.setText(str);*/
        if (CommUtils.isLogin(mContext)) {
            String userIcon = (String) mySharedPreferences.getValue("userIcon", "");
            String userName = (String) mySharedPreferences.getValue("userName", "");
            String level = (String) mySharedPreferences.getValue("level", "");
            String dueTime= (String) mySharedPreferences.getValue("dueTime","");
            long count = Util.timeCompare(dueTime);
            // ????????????????????????icon
            tv_user_name.setText(userName);
            Util.displayCircleCropImgView(mContext, img_user, userIcon, R.mipmap.ic_user_place);
            if (pageTo()) {
                mIvBuyVip.setVisibility(View.GONE);
                mLlVip.setVisibility(View.VISIBLE);
            } else {
                mIvBuyVip.setVisibility(View.VISIBLE);
                mLlVip.setVisibility(View.GONE);
            }
            if ("9".equals(level)) {
                mTvVip.setText("????????????");
            } else if ("4".equals(level)) {
                String tip="";
                // ????????????
                if (count <= 0) {
                    tip= "VIP/???????????????????????????????????????";
                    mIvBuyVip.setVisibility(View.VISIBLE);
                    mLlVip.setVisibility(View.VISIBLE);
                } else {
                    tip= String.format("????????????/??????%d???", count);
                }
                mTvVip.setText(tip);
            }
        }else {
            mIvBuyVip.setVisibility(View.VISIBLE);
            mLlVip.setVisibility(View.GONE);
            tv_user_name.setText("???????????????????????????");
            img_user.setImageDrawable(getResources().getDrawable(R.mipmap.ic_user_place));
            //Util.displayCircleCropImgView(mContext, img_user, userIcon, R.mipmap.ic_user_place);
        }
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
     * ????????????
     *
     * @param response
     */
    @Override
    public void httpCallback(VersionUpdataResponse response) {
        dialogDismiss();
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
                        // ??????????????????
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
                        ToastUtils.shortShowStr(mContext, "?????????????????????");
                    }
                } else {
                    ToastUtils.shortShowStr(mContext, "?????????????????????");
                }
            } else {
                ToastUtils.shortShowStr(mContext, "?????????????????????");
            }
        }
    }

    @Override
    public void httpCallback(BaseResponse response) {
        dialogDismiss();
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


    @Override
    public void httpError(String e) {
        ToastUtils.shortShowStr(mContext, getResources().getString(R.string.erro));
    }

    @Override
    public void httpCallback(AddPayOrderResponse response) {
        if (response.isSuccess()) {
            AddPayOrderResponse.AddPayOrderInfo result = response.getResult();
            if (result != null) {
                String payerType = result.getPayerType();
                orderId = result.getOrderId();
                if ("ALI".equals(payerType)) {
                    payV2(result.getSignOrder());
                } else if ("WX".equals(payerType)) {
                    String signOrder = result.getSignOrder();
//                    NewWXSignOrderBean signOrderBean = JSON.parseObject(signOrder, NewWXSignOrderBean.class);
//
//                    wxPay(signOrderBean);
                    SignOrderBean signOrderBean = JSON.parseObject(signOrder, SignOrderBean.class);
                    wxPay(signOrderBean.getReturn_code(), signOrderBean.getReturn_msg(), signOrderBean.getPrepay_id(), signOrderBean.getNonce_str());

                }
            }
        } else {
            ToastUtils.shortShowStr(mContext, response.getMsg());
        }
    }

    private void wxPay(String return_code, String return_msg, String prepay_id, String nonce_str) {
        //1??? ??????????????????APPID
        IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, null);
        wxapi.registerApp(Constants.WX_APP_ID);

        if ("SUCCESS".equalsIgnoreCase(return_code) && "OK".equalsIgnoreCase(return_msg)) {
            PayReq req = new PayReq();
            req.appId = Constants.WX_APP_ID;//APPID
            req.partnerId = Constants.WX_MCH_ID;//?????????
            req.prepayId = prepay_id;
            req.nonceStr = nonce_str;
            String time = System.currentTimeMillis() / 1000 + "";
            req.timeStamp = time;//???????????????????????????long??????????????????10???
            req.packageValue = "Sign=WXPay";//??????????????????????????????
            SortedMap<String, String> sortedMap = new TreeMap<String, String>();//????????????????????????????????????????????????????????????
            sortedMap.put("appid", Constants.WX_APP_ID);
            sortedMap.put("partnerid", Constants.WX_MCH_ID);
            sortedMap.put("prepayid", prepay_id);
            sortedMap.put("noncestr", nonce_str);
            sortedMap.put("timestamp", time);
            sortedMap.put("package", "Sign=WXPay");
            req.sign = WeChatField.getSign(sortedMap);//???????????????sign??????????????????????????????????????????????????????????????????-1?????????????????????????????????????????????????????????????????????-1
            //????????????
            wxapi.sendReq(req);
        } else {
            ToastUtils.shortShowStr(mContext, "??????????????????,???????????????!");
        }
    }
    /**
     * ????????????
     */
    private void wxPay(NewWXSignOrderBean signOrderBean) {
        //1??? ??????????????????APPID
        IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, null);
        wxapi.registerApp(Constants.WX_APP_ID);

        //if ("SUCCESS".equalsIgnoreCase(return_code) && "OK".equalsIgnoreCase(return_msg)) {
        if (signOrderBean != null) {
            PayReq req = new PayReq();
            req.appId = Constants.WX_APP_ID;//APPID
            req.partnerId = Constants.WX_MCH_ID;//?????????
            req.prepayId = signOrderBean.getPrepayid();
            req.nonceStr = signOrderBean.getNoncestr();
            String time = System.currentTimeMillis() / 1000 + "";
            req.timeStamp = time;//???????????????????????????long??????????????????10???
            req.packageValue = "Sign=WXPay";//??????????????????????????????
            SortedMap<String, String> sortedMap = new TreeMap<String, String>();//????????????????????????????????????????????????????????????
            sortedMap.put("appid", Constants.WX_APP_ID);
            sortedMap.put("partnerid", Constants.WX_MCH_ID);
            sortedMap.put("prepayid", signOrderBean.getPrepayid());
            sortedMap.put("noncestr", signOrderBean.getNoncestr());
            sortedMap.put("timestamp", time);
            sortedMap.put("package", "Sign=WXPay");
            req.sign = WeChatField.getSign(sortedMap);//???????????????sign??????????????????????????????????????????????????????????????????-1?????????????????????????????????????????????????????????????????????-1
            wxapi.sendReq(req);//???????????????sign??????????????????????????????????????????????????????????????????-1?????????????????????????????????????????????????????????????????????-1

        } else {
            ToastUtils.shortShowStr(mContext, "??????????????????,???????????????!");
        }
    }

    @Override
    public void httpCallback(QueryOrderResponse response) {
        if (response.isSuccess()) {
            QueryOrderResponse.QueryOrderInfo result = response.getResult();
            if (result != null) {
                String orderStatus = result.getOrderStatus();
                if ("2".equals(orderStatus) || "3".equals(orderStatus)) {
                    querySum = 3;
                    // ????????????
                    ToastUtils.shortShowStr(mContext, "????????????");
                    mySharedPreferences.putValue("isOpenMember", "1");
                    mPresenter.getAccountInformation();
                } else {
                    // ????????????
                    if (querySum > 0) {
                        querySum--;
                        mPresenter.queryOrder(orderId);
                    } else {
                        querySum = 3;
                        ToastUtils.shortShowStr(mContext, "????????????");
                        ToastUtils.shortShowStr(mContext, "??????????????????????????????????????????");
                    }
                }
            }
        }
    }

    @Override
    public void httpCallback(MemberLevelResponse response) {
        if (response.isSuccess()) {
            List<MemberLevelResponse.MemberLevelInfo> list = response.getList();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    MemberLevelResponse.MemberLevelInfo memberLevelInfo = list.get(i);
                    if (memberLevelInfo != null) {
                        String code = memberLevelInfo.getCode();
                        int originalPrice = memberLevelInfo.getOriginalPrice();
                        int price = memberLevelInfo.getPrice();
                        if ("9".equals(code)) {
                            yjCode = memberLevelInfo.getGoodId();
                            //prodCode = yjCode;
                            mPriceYj = String.format("?? %s", Util.changeF2Y(price));
                            mOriginalPriceYj = originalPrice;
//                            if(!pageTo()){
//                                showVipDialog();
//                            }
                            //tv_yjhy_yj.setText(String.format("?????? ??%s", Util.changeF2Y(originalPrice)));
                            //tv_yjhy_price.setText(String.format("?? %s", Util.changeF2Y(price)));
                            //tv_price.setText(String.format("?? %s", Util.changeF2Y(price)));
                        } else if ("4".equals(code)) {
                            ndCode = memberLevelInfo.getGoodId();
                            mPriceNd = String.format("?? %s", Util.changeF2Y(price));
                            mOriginalPriceNd = originalPrice;
                            //v_ndhy_yj.setText(String.format("?????? ??%s", Util.changeF2Y(originalPrice)));
                            //tv_ndhy_price.setText(String.format("?? %s", Util.changeF2Y(price)));
                        } else if ("2".equals(code)) {
                            //ydCode = memberLevelInfo.getGoodId();
                            //tv_ydhy_yj.setText(String.format("?????? ??%s", Util.changeF2Y(originalPrice)));
                            //tv_ydhy_price.setText(String.format("?? %s", Util.changeF2Y(price)));
                        }
                    }
                }
            }
        } else {
            ToastUtils.shortShowStr(mContext, response.getMsg());
        }
    }

    @Override
    public void httpCallback(AccountInformationResponse response) {
        if (response.isSuccess()) {
            AccountInformationResponse.AccountInformationInfo result = response.getResult();
            if (result != null) {
                mySharedPreferences.putValue("MemberUser", result.getId());
                mySharedPreferences.putValue("level", result.getLevel());
                mySharedPreferences.putValue("dueTime", result.getDueTime());
                String level = result.getLevel();
                if (!TextUtils.isEmpty(level)) {
                    long count = Util.timeCompare(result.getDueTime());
                    // ???????????????
                    EventBusMessage eventBusMessage = new EventBusMessage();
                    eventBusMessage.setLevel(level);
                    eventBusMessage.setDueTime(result.getDueTime());
                    EventBus.getDefault().post(eventBusMessage);
                    if (pageTo()) {
                        mIvBuyVip.setVisibility(View.GONE);
                        mLlVip.setVisibility(View.VISIBLE);
                    } else {
                        mIvBuyVip.setVisibility(View.VISIBLE);
                        mLlVip.setVisibility(View.GONE);
                    }
                    if ("9".equals(level)) {
                        mTvVip.setText("????????????");
                    } else if ("4".equals(level)) {
                        String tip="";
                        // ????????????
                        if (count <= 0) {
                            tip= "VIP/???????????????????????????????????????";
                            mIvBuyVip.setVisibility(View.VISIBLE);
                            mLlVip.setVisibility(View.VISIBLE);
                        } else {
                            tip= String.format("????????????/??????%d???", count);
                        }
                        mTvVip.setText(tip);
                    }
                }else {
                    mTvVip.setVisibility(View.GONE);
                    mLlVip.setVisibility(View.GONE);
                }
                //showGmcgDialog();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WxPayMessageBean event) {
        if (event.isWxPay()) {
            String requestGet = Util.requestGet(orderId, true);
            QueryOrderResponse queryOrderResponse = JSON.parseObject(requestGet, QueryOrderResponse.class);
            if (queryOrderResponse != null && queryOrderResponse.isSuccess()) {
                QueryOrderResponse.QueryOrderInfo queryOrderInfo = queryOrderResponse.getResult();
                if (queryOrderInfo != null) {
                    String orderStatus = queryOrderInfo.getOrderStatus();
                    if ("2".equals(orderStatus) || "3".equals(orderStatus)) {
                        querySum = 3;
                        // ????????????
                        ToastUtils.shortShowStr(mContext, "????????????");
                        // ??????????????????
                        String requestStr = Util.requestGet("", false);
                        AccountInformationResponse accountInformationResponse = JSON.parseObject(requestStr, AccountInformationResponse.class);
                        if (accountInformationResponse != null && accountInformationResponse.isSuccess()) {
                            AccountInformationResponse.AccountInformationInfo informationInfo = accountInformationResponse.getResult();
                            mySharedPreferences.putValue("MemberUser", informationInfo.getId());
                            mySharedPreferences.putValue("level", informationInfo.getLevel());
                            mySharedPreferences.putValue("dueTime", informationInfo.getDueTime());
                            String level = informationInfo.getLevel();
                            if (!TextUtils.isEmpty(level)) {
                                // ???????????????
                                EventBusMessage eventBusMessage = new EventBusMessage();
                                eventBusMessage.setLevel(level);
                                eventBusMessage.setDueTime(informationInfo.getDueTime());
                                EventBus.getDefault().post(eventBusMessage);
                                //mPresenter.getTemplate(resId);
                                //mIvBuyVip.setVisibility(View.GONE);
                                mPresenter.getAccountInformation();
                            }
                            //showGmcgDialog();
                        }

//                        if (spxq) {
//                            setResult(10);
//                        }
//                        mPresenter.getAccountInformation();
                    } else {
                        // ????????????
                        if (querySum > 2) {
                            querySum--;
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String req = Util.requestGet(orderId, true);
                            QueryOrderResponse queryOrderResponse1 = JSON.parseObject(req, QueryOrderResponse.class);
                            if (queryOrderResponse1 != null && queryOrderResponse1.isSuccess()) {
                                QueryOrderResponse.QueryOrderInfo queryOrderInfo1 = queryOrderResponse1.getResult();
                                if (queryOrderInfo1 != null) {
                                    String orderStatus1 = queryOrderInfo1.getOrderStatus();
                                    if ("2".equals(orderStatus1) || "3".equals(orderStatus1)) {
                                        querySum = 3;
                                        // ????????????
                                        ToastUtils.shortShowStr(mContext, "????????????");
                                        // ??????????????????
                                        String requestStr = Util.requestGet("", false);
                                        AccountInformationResponse accountInformationResponse = JSON.parseObject(requestStr, AccountInformationResponse.class);
                                        if (accountInformationResponse != null && accountInformationResponse.isSuccess()) {
                                            AccountInformationResponse.AccountInformationInfo informationInfo = accountInformationResponse.getResult();
                                            mySharedPreferences.putValue("MemberUser", informationInfo.getId());
                                            mySharedPreferences.putValue("level", informationInfo.getLevel());
                                            mySharedPreferences.putValue("dueTime", informationInfo.getDueTime());
                                            String level = informationInfo.getLevel();
                                            if (!TextUtils.isEmpty(level)) {
                                                // ???????????????
                                                EventBusMessage eventBusMessage = new EventBusMessage();
                                                eventBusMessage.setLevel(level);
                                                eventBusMessage.setDueTime(informationInfo.getDueTime());
                                                EventBus.getDefault().post(eventBusMessage);
                                            }
                                            //showGmcgDialog();
                                        }
//                                        if (spxq) {
//                                            setResult(10);
//                                        }
//                                        mPresenter.getAccountInformation();
                                    } else {
                                        querySum = 3;
                                        ToastUtils.longShowStr(mContext, "???????????????????????????????????????");
                                    }
                                }
                            }
                        }
                    }
                }
            }
//            mPresenter.queryOrder(orderId);
        }
    }

    //?????????????????????????????????????????????????????????????????????????????????
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void initPermisson() {
        getGetAppVersion();
    }

    /**
     * ??????app???????????????
     */
    private void getGetAppVersion() {
        if (!CommUtils.isNetworkAvailable(mContext)) {
            ToastUtils.shortShowStr(this, Constants.NET_ERROR);
            return;
        }
        VersionUpdataBean versionUpdataBean = new VersionUpdataBean();
        versionUpdataBean.setParam(new VersionUpdataBean.VersionUpdataInfo(Constants.WX_APP_ID, CommUtils.getVersionName(mContext)));
        mPresenter.settingVersion(versionUpdataBean);
    }

    //???????????????????????????????????????????????????????????????????????????????????????????????????????????????@NeedsPermission?????????????????????????????????
    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MineActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * ???????????????
     */
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        ToastUtils.shortShowStr(mContext, "??????????????????????????????????????????");
    }


    /**
     * ????????????
     */
    private void loginOut(String loginType) {
        if (QQ.NAME.equals(loginType)) {
            // QQ??????
            Platform platform = ShareSDK.getPlatform(QQ.NAME);
            platform.removeAccount(true);
            if (isLogout) {
                ToastUtils.shortShowStr(mContext, "??????????????????");
            } else {
                ToastUtils.shortShowStr(mContext, "????????????");
            }
        } else if (Wechat.NAME.equals(loginType)) {
            // ????????????
            Platform platform = ShareSDK.getPlatform(Wechat.NAME);
            platform.removeAccount(true);
            if (isLogout) {
                ToastUtils.shortShowStr(mContext, "??????????????????");
            } else {
                ToastUtils.shortShowStr(mContext, "????????????");
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
     * ???????????????sp???????????????
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

    /**
     * ????????????????????????
     */
    private void showVipDialog() {
        View diaView = View.inflate(mContext, R.layout.dialog_buy_vip, null);
        ImageView img_wx_zf = diaView.findViewById(R.id.img_wx_zf);
        ImageView img_zfb_zf = diaView.findViewById(R.id.img_zfb_zf);
        ImageView iv_close = diaView.findViewById(R.id.iv_close);

        TextView tv_price = diaView.findViewById(R.id.tv_price);
        TextView tv_current_yjhy_price = diaView.findViewById(R.id.tv_current_yjhy_price);
        TextView tv_current_ndhy_price = diaView.findViewById(R.id.tv_current_ndhy_price);
        TextView tv_original_price_yjhy = diaView.findViewById(R.id.tv_original_price_yjhy);
        TextView tv_original_price_ndhy = diaView.findViewById(R.id.tv_original_price_ndhy);

        LinearLayout rl_wx = diaView.findViewById(R.id.rl_wx);
        LinearLayout rl_zfb = diaView.findViewById(R.id.rl_zfb);
        LinearLayout ll_yjhy = diaView.findViewById(R.id.ll_yjhy);
        LinearLayout ll_ndhy = diaView.findViewById(R.id.ll_ndhy);
        FrameLayout fl_zf = diaView.findViewById(R.id.fl_zf);


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(diaView);
        AlertDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // ?????????????????????????????????
        // ????????????
        p.width = (int) (d.getWidth() * 0.95); // ????????????????????????0.95
        p.gravity = Gravity.CENTER;//????????????
        //p.alpha = 0.8f;//???????????????
        dialogWindow.setAttributes(p);

        img_wx_zf.setSelected(true);
        tv_current_yjhy_price.setText(mPriceYj);
        tv_current_ndhy_price.setText(mPriceNd);
        tv_original_price_yjhy.setText(String.format("?????? ??%s", Util.changeF2Y(mOriginalPriceYj)));
        tv_original_price_ndhy.setText(String.format("?????? ??%s", Util.changeF2Y(mOriginalPriceNd)));

        ll_yjhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodCode = yjCode;
                mPrice = mPriceYj;
                hyType = "????????????";
                ll_yjhy.setSelected(true);
                ll_ndhy.setSelected(false);
                for (int i = 0; i < ll_yjhy.getChildCount(); i++) {
                    ll_yjhy.getChildAt(i).setSelected(true);
                }
                for (int i = 0; i < ll_ndhy.getChildCount(); i++) {
                    ll_ndhy.getChildAt(i).setSelected(false);
                }
                tv_price.setText(mPrice);
            }
        });
        ll_ndhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodCode = ndCode;
                mPrice = mPriceNd;
                hyType = "????????????";
                ll_yjhy.setSelected(false);
                ll_ndhy.setSelected(true);
                for (int i = 0; i < ll_yjhy.getChildCount(); i++) {
                    ll_yjhy.getChildAt(i).setSelected(false);
                }
                for (int i = 0; i < ll_ndhy.getChildCount(); i++) {
                    ll_ndhy.getChildAt(i).setSelected(true);
                }
                tv_price.setText(mPrice);
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        rl_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ????????????
                zfType = 1;
                img_zfb_zf.setSelected(false);
                img_wx_zf.setSelected(true);
            }
        });
        rl_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ???????????????
                zfType = 2;
                img_wx_zf.setSelected(false);
                img_zfb_zf.setSelected(true);
            }
        });
        fl_zf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ????????????
                String price = tv_price.getText().toString().replace("?? ", "");
                if (TextUtils.isEmpty(price)) {
                    ToastUtils.shortShowStr(mContext, "????????????,?????????");
                    return;
                }
                if (Float.parseFloat(price) > 0f) {
                    pay(price);
                } else {
                    ToastUtils.shortShowStr(mContext, "??????????????????????????????");
                }
            }
        });
        ll_yjhy.performClick();//????????????????????????

    }

    /**
     * ??????
     *
     * @param price
     */
    private void pay(String price) {
        String priceF = Util.changeY2F(Double.parseDouble(price)).split("\\.")[0];
        AddPayOrderBean addPayOrderBean = new AddPayOrderBean();
        AddPayOrderBean.AddPayOrderInfo addPayOrderInfo = new AddPayOrderBean.AddPayOrderInfo();
        addPayOrderInfo.setSubject(hyType);
        addPayOrderInfo.setBody(hyType);
        addPayOrderInfo.setChannel(Util.getChannel(mContext));
        List<AddPayOrderBean.GoodsListInfo> list = new ArrayList();
        list.add(new AddPayOrderBean.GoodsListInfo(Integer.parseInt(priceF), prodCode));
        addPayOrderInfo.setGoodsList(list);
        addPayOrderInfo.setCurrencyType("CNY");
//        addPayOrderInfo.setUserId(memberUser);
        //setRecAccount  4: ???????????? 3: ?????????
        if (zfType == 1) {
            // ????????????
            if (Util.isWeixinAvilible(mContext)) {
                addPayOrderInfo.setRecAccount(wxPay);
            } else {
                ToastUtils.shortShowStr(mContext, "???????????????????????????");
                return;
            }
        } else {
            // ???????????????
            addPayOrderInfo.setRecAccount(aliPay);
        }
        addPayOrderInfo.setAmount(Integer.parseInt(priceF));
        addPayOrderBean.setParam(addPayOrderInfo);
        mPresenter.addPayOrder(addPayOrderBean);
    }

    /**
     * ???????????????????????????
     */
    public void payV2(String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(MineActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // ??????????????????
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public boolean pageTo() {
        String isOpenMember = (String) mySharedPreferences.getValue("isOpenMember", "");
        if ("1".equals(isOpenMember)) {
            String level = (String) mySharedPreferences.getValue("level", "");
            if (TextUtils.isEmpty(level)) {
                return false;
            }
        }
        return true;
    }
}
