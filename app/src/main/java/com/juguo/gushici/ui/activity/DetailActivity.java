package com.juguo.gushici.ui.activity;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.bean.ChangeStateBean;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.ui.activity.contract.DetailContract;
import com.juguo.gushici.ui.activity.presenter.DetailPresenter;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.ToastUtils;
import com.juguo.gushici.utils.Util;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 诗词详情页
 */
public class DetailActivity extends BaseMvpActivity<DetailPresenter> implements DetailContract.View {

    public static String POETRY_DATA = "poetry_data";

    public int TYPE_ORIGINAL = 0;
    public int TYPE_TRANSLATION = 1;
    public int TYPE_APPRECIATION = 2;

    private FrameLayout mFlBack;
    private TextView mTvTitle;
    private ImageView mIvAlready;//背诵状态,已背/未背
    private ImageView mIvShare;
    private WebView mWebView;
    private TextView mTvOriginalText;//原文
    private TextView mTvTranslationText;//译文
    private TextView mTvAppreciation;
    private ImageView mIvBg;

    private PoetryBean.PoetryInfo mPoetryInfo;
    private int mRecited = 0;
    private MySharedPreferences mySharedPreferences;

    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mFlBack = findViewById(R.id.fl_back);
        mTvTitle = findViewById(R.id.tv_title);
        mIvAlready = findViewById(R.id.iv_already_learn);
        mIvShare = findViewById(R.id.iv_share);
        mWebView = findViewById(R.id.webView);
        mTvOriginalText = findViewById(R.id.tv_original_text);
        mTvTranslationText = findViewById(R.id.tv_translation_text);
        mTvAppreciation = findViewById(R.id.tv_appreciation);
        mIvBg = findViewById(R.id.iv_bg);

        FrameLayout flRoot = findViewById(R.id.fl_root);
        CommUtils.setImmerseLayout(flRoot, this);
        mPoetryInfo = (PoetryBean.PoetryInfo) getIntent().getSerializableExtra(POETRY_DATA);

        mFlBack.setOnClickListener(this);
        mIvAlready.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mTvOriginalText.setOnClickListener(this);
        mTvTranslationText.setOnClickListener(this);
        mTvAppreciation.setOnClickListener(this);
        loadData(TYPE_ORIGINAL);

        if (mPoetryInfo.isAlready()) {

            mIvAlready.setImageDrawable(getResources().getDrawable(R.mipmap.ic_already_learn));
            mRecited = 1;
        } else {
            mIvAlready.setImageDrawable(getResources().getDrawable(R.mipmap.ic_no_already_learn));
            mRecited = mPoetryInfo.getRecited();
        }
        mySharedPreferences = new MySharedPreferences(this, "Shared");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.iv_share:

                shareSelectDialog();
                break;
            case R.id.iv_already_learn:
                changeState();
                break;
            case R.id.tv_original_text:

                loadData(TYPE_ORIGINAL);
                break;
            case R.id.tv_translation_text:

                loadData(TYPE_TRANSLATION);
                break;
            case R.id.tv_appreciation:

                loadData(TYPE_APPRECIATION);
                break;
        }
    }

    @Override
    public void changeStateSuccess(Object o) {

        dialogDismiss();
        //背诵状态。0未背诵1已背2取消已背
        if (mRecited == 0 || mRecited == 2) {

            mIvAlready.setImageDrawable(getResources().getDrawable(R.mipmap.ic_no_already_learn));
            //mRecited = 1;
        } else {

            mIvAlready.setImageDrawable(getResources().getDrawable(R.mipmap.ic_already_learn));
            //mRecited = 2;
        }
    }

    @Override
    public void httpError(String e) {
        dialogDismiss();
    }


    private void loadData(int type) {

        String webViewContent = mPoetryInfo.getContent();
        if (TYPE_ORIGINAL == type) {

            webViewContent = mPoetryInfo.getContent();
            mTvOriginalText.setSelected(true);
            mTvTranslationText.setSelected(false);
            mTvAppreciation.setSelected(false);
            mIvBg.setBackground(getResources().getDrawable(R.mipmap.bg_detail_original_text));
            mTvTitle.setText("原文");

        } else if (TYPE_TRANSLATION == type) {

            webViewContent = mPoetryInfo.getNote();
            mTvOriginalText.setSelected(false);
            mTvTranslationText.setSelected(true);
            mTvAppreciation.setSelected(false);
            mIvBg.setBackground(getResources().getDrawable(R.mipmap.bg_detail_translation_text));
            mTvTitle.setText("译文");

        } else if (TYPE_APPRECIATION == type) {

            webViewContent = mPoetryInfo.getAppreciation();
            mTvOriginalText.setSelected(false);
            mTvTranslationText.setSelected(false);
            mTvAppreciation.setSelected(true);
            mIvBg.setBackground(getResources().getDrawable(R.mipmap.bg_detail_appreciation_text));
            mTvTitle.setText("赏析");

        }

        mWebView.loadDataWithBaseURL(null, webViewContent, "text/html", "utf-8", null);
    }

    private void changeState() {

        dialogShow();
        //背诵状态。0未背诵1已背2取消已背
        if (mRecited == 0 || mRecited == 2) {

            mRecited = 1;
        } else {
            mRecited = 2;
        }
        ChangeStateBean changeStateBean = new ChangeStateBean();
        ChangeStateBean.ChangeStateInfo changeStateInfo = new ChangeStateBean.ChangeStateInfo();
        changeStateInfo.setPoemId(mPoetryInfo.getId());
        changeStateInfo.setRecited(mRecited);
        changeStateBean.setParam(changeStateInfo);

        mPresenter.changeState(changeStateBean);
    }

    /**
     * 点击分享底部弹窗
     */
    private void shareSelectDialog() {
        final AlertDialog dialog;
        View diaView = View.inflate(this, R.layout.spfx_popupwindow, null);
        LinearLayout ll_qq = diaView.findViewById(R.id.ll_qq);
        LinearLayout ll_wx = diaView.findViewById(R.id.ll_wx);
        LinearLayout ll_pyq = diaView.findViewById(R.id.ll_pyq);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(diaView);
        dialog = builder.create();
        dialog.show();

        Display display = getWindowManager().getDefaultDisplay();
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
                if (Util.isQQClientAvailable(DetailActivity.this)) {
                    share(QQ.NAME);
                } else {
                    ToastUtils.shortShowStr(DetailActivity.this, "请先安装QQ应用");
                }
                dialog.dismiss();
            }
        });

        // 微信分享
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isWeixinAvilible(DetailActivity.this)) {
                    share(Wechat.NAME);
                } else {
                    ToastUtils.shortShowStr(DetailActivity.this, "请先安装微信应用");
                }
                dialog.dismiss();
            }
        });

        // 微信朋友圈分享
        ll_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isWeixinAvilible(DetailActivity.this)) {
                    share(WechatMoments.NAME);
                } else {
                    ToastUtils.shortShowStr(DetailActivity.this, "请先安装微信应用");
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
        String assetsResource = Util.getAssetsResource(DetailActivity.this, "share_icon.png", R.mipmap.ic_launcher);
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
                ToastUtils.shortShowStr(DetailActivity.this, "分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.shortShowStr(DetailActivity.this, "分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.shortShowStr(DetailActivity.this, "取消分享");
            }
        });
        platform.share(shareParams);
    }
}
