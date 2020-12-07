package com.juguo.gushici.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.bean.AppConfigBean;
import com.juguo.gushici.bean.VersionUpdataBean;
import com.juguo.gushici.response.VersionUpdataResponse;
import com.juguo.gushici.ui.MainActivity;
import com.juguo.gushici.ui.activity.contract.SplashContract;
import com.juguo.gushici.ui.activity.presenter.SplashPresenter;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.ToastUtils;
import com.juguo.gushici.utils.Util;
import com.juguo.gushici.view.TTAdManagerHolder;

import butterknife.BindView;

public class SplashActivity extends BaseMvpActivity<SplashPresenter> implements SplashContract.View {

    private TTAdNative mTTAdNative;

    //是否强制跳转到主页面
    private boolean mForceGoMain;

    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 5000;
    private String mCodeId = Constants.CSJ_CODE_ID;
    private boolean mIsExpress = false; //是否请求模板广告
    private Context mContext;

    @BindView(R.id.splash_container)
    FrameLayout mSplashContainer;
    private String adType;
    private String adImgUrl;
    private String adResUrl;

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {
        mContext = this;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        VersionUpdataBean versionUpdataBean = new VersionUpdataBean();
        versionUpdataBean.setParam(new VersionUpdataBean.VersionUpdataInfo(Constants.WX_APP_ID, CommUtils.getVersionName(mContext)));
        mPresenter.selectSplash(versionUpdataBean);
    }

    /**
     * 穿山甲
     */
    private void csjSplash() {
        //step2:创建TTAdNative对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        getExtraInfo();

        //加载开屏广告
        loadSplashAd();
    }


    private void getExtraInfo() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String codeId = intent.getStringExtra("splash_rit");
        if (!TextUtils.isEmpty(codeId)) {
            mCodeId = codeId;
        }
        mIsExpress = intent.getBooleanExtra("is_express", false);
    }

    @Override
    protected void onResume() {
        //判断是否该跳转到主页面
        if (mForceGoMain) {
            goToMainActivity();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mForceGoMain = true;
    }

    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = null;
        if (mIsExpress) {
            //个性化模板广告需要传入期望广告view的宽、高，单位dp，请传入实际需要的大小，
            //比如：广告下方拼接logo、适配刘海屏等，需要考虑实际广告大小
            //float expressViewWidth = UIUtils.getScreenWidthDp(this);
            //float expressViewHeight = UIUtils.getHeight(this);
            adSlot = new AdSlot.Builder()
                    .setCodeId(mCodeId)
                    //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
                    //view宽高等于图片的宽高
                    .setExpressViewAcceptedSize(1080, 1920)
                    .build();
        } else {
            adSlot = new AdSlot.Builder()
                    .setCodeId(mCodeId)
                    .setImageAcceptedSize(1080, 1920)
                    .build();
        }

        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                ToastUtils.shortShowStr(mContext, message);
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onTimeout() {
                ToastUtils.shortShowStr(mContext, "开屏广告加载超时");
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                if (mSplashContainer != null && !SplashActivity.this.isFinishing()) {
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    mSplashContainer.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                } else {
                    goToMainActivity();
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
//                        Log.d(TAG, "onAdClicked");
//                        showToast("开屏广告点击");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
//                        Log.d(TAG, "onAdShow");
//                        showToast("开屏广告展示");
                    }

                    @Override
                    public void onAdSkip() {
//                        Log.d(TAG, "onAdSkip");
//                        showToast("开屏广告跳过");
                        goToMainActivity();

                    }

                    @Override
                    public void onAdTimeOver() {
//                        Log.d(TAG, "onAdTimeOver");
//                        showToast("开屏广告倒计时结束");
                        goToMainActivity();
                    }
                });
                if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {
                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
//                                showToast("下载中...");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                            showToast("下载暂停...");

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                            showToast("下载失败...");

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                            showToast("下载完成...");

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {
//                            ToastUtils.shortShowStr(mContext, "安装完成...");
                        }
                    });
                }
            }
        }, AD_TIME_OUT);
    }

    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
//        mSplashContainer.removeAllViews();
        this.finish();
    }

    @Override
    public void httpCallback(VersionUpdataResponse response) {
        if (response.isSuccess()) {
            VersionUpdataResponse.VersionUpdataInfo result = response.getResult();
            if (result != null) {
                String appConfig = result.getAppConfig();
                if (!TextUtils.isEmpty(appConfig)) {
                    AppConfigBean appConfigBean = JSON.parseObject(appConfig, AppConfigBean.class);
                    String startAdFlag = appConfigBean.getStartAdFlag();
                    //NONE 无  CSJ 穿山甲  SYS 自系统
                    if ("NONE".equals(startAdFlag)) {
                        goToMainActivity();
                    } else if ("CSJ".equals(startAdFlag)) {
                        csjSplash();
                    } else if ("SYS".equals(startAdFlag)) {
                        getTheme().applyStyle(R.style.MySplashTheme, true);
                        mSplashContainer.removeAllViews();

                        View inflate = FrameLayout.inflate(mContext, R.layout.activity_splash_my, null);
                        ImageView img_logo = inflate.findViewById(R.id.img_logo);
                        mSplashContainer.addView(inflate);

                        adType = result.getAdType();// 广告类型
                        adImgUrl = result.getAdImgUrl();
                        adResUrl = result.getAdResUrl();

                        Util.displayBlendImgView(mContext, img_logo, adImgUrl, R.mipmap.ic_logo);

                        try {
                            Thread.sleep(3000);
                            goToMainActivity();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        goToMainActivity();
                    }
                } else {
                    goToMainActivity();
                }
            } else {
                goToMainActivity();
            }
        } else {
            goToMainActivity();
        }
    }

    @Override
    public void httpError(String e) {

    }
}
