package com.juguo.gushici.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.juguo.gushici.MyApplication;
import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.ui.activity.contract.CenterContract;
import com.juguo.gushici.ui.activity.presenter.CenterPresenter;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.TToast;
import com.juguo.gushici.utils.ToastUtils;
import com.juguo.gushici.view.TTAdManagerHolder;

public class AdvertActivity extends BaseMvpActivity<CenterPresenter> implements CenterContract.View {

    private String TAG = this.getClass().getSimpleName();
    private TTAdNative mTTAdNative;
    private TTFullScreenVideoAd mttFullVideoAd;
    private boolean mIsExpress = false; //是否请求模板广告
    private boolean mIsLoaded = false; //视频是否加载完成
    private boolean mHasShowDownloadActive = false;

    private static int AD_LOAD_COMPLETE = 0;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (AD_LOAD_COMPLETE == msg.what) {

                if (mttFullVideoAd != null && mIsLoaded) {
                    //step6:在获取到广告后展示
                    //该方法直接展示广告
                    //mttFullVideoAd.showFullScreenVideoAd(FullScreenVideoActivity.this);

                    //展示广告，并传入广告展示的场景
                    mttFullVideoAd.showFullScreenVideoAd(AdvertActivity.this, TTAdConstant.RitScenes.GAME_GIFT_BONUS, null);
                    mttFullVideoAd = null;
                } else {
                    TToast.show(AdvertActivity.this, "请先加载广告");
                }
            }
            return false;
        }
    });

    @Override
    protected int getLayout() {
        return R.layout.activity_advert;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(MyApplication.getApp());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(Constants.CSJ_CODE_ID1)
                .setSupportDeepLink(true)
                .setOrientation(TTAdConstant.VERTICAL)
                .build();
        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "Callback --> onError: " + code + ", " + String.valueOf(message));
                TToast.show(AdvertActivity.this, message);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                Log.e(TAG, "Callback --> onFullScreenVideoAdLoad"+"FullVideoAd loaded  广告类型：" + getAdType(ad.getFullVideoAdType()));

                //TToast.show(AdvertActivity.this, "FullVideoAd loaded  广告类型：" + getAdType(ad.getFullVideoAdType()));
                mttFullVideoAd = ad;
                mIsLoaded = false;
                mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
///storage/emulated/0/Android/data/com.juguo.gushici/cache/com.juguo.gushici-full_screen_video_cache_945815595/82bcdecaeafa7d962b167c02d97990a9
                    @Override
                    public void onAdShow() {
                        Log.d(TAG, "Callback --> FullVideoAd show");
//                        TToast.show(AdvertActivity.this, "FullVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Log.d(TAG, "Callback --> FullVideoAd bar click");
                        TToast.show(AdvertActivity.this, "FullVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Log.d(TAG, "Callback --> FullVideoAd close");
                        TToast.show(AdvertActivity.this, "FullVideoAd close");
                    }

                    @Override
                    public void onVideoComplete() {
                        Log.d(TAG, "Callback --> FullVideoAd complete");
                        TToast.show(AdvertActivity.this, "FullVideoAd complete");
                    }

                    @Override
                    public void onSkippedVideo() {
                        Log.d(TAG, "Callback --> FullVideoAd skipped");
                        TToast.show(AdvertActivity.this, "FullVideoAd skipped");
                    }
                });


                ad.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);

                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            TToast.show(AdvertActivity.this, "下载中，点击下载区域暂停");
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        TToast.show(AdvertActivity.this, "下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        TToast.show(AdvertActivity.this, "下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
                        TToast.show(AdvertActivity.this, "下载完成，点击下载区域重新下载");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Log.d("DML", "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
                        TToast.show(AdvertActivity.this, "安装完成，点击下载区域打开");
                    }
                });
            }

            @Override
            public void onFullScreenVideoCached() {
                Log.e(TAG, "Callback --> onFullScreenVideoCached");
                mIsLoaded = true;
                Message message = Message.obtain();
                message.what = AD_LOAD_COMPLETE;
                mHandler.sendMessage(message);
//                TToast.show(AdvertActivity.this, "FullVideoAd video cached");
            }
        });
    }

    @Override
    public void httpCallback(Object o) {

    }

    @Override
    public void httpError(String e) {

    }

    private String getAdType(int type) {
        switch (type) {
            case TTAdConstant.AD_TYPE_COMMON_VIDEO:
                return "普通全屏视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE_VIDEO:
                return "Playable全屏视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE:
                return "纯Playable，type=" + type;
        }

        return "未知类型+type=" + type;
    }
}
