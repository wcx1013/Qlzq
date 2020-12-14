package com.juguo.gushici.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseActivity;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.ui.activity.contract.LoginContract;
import com.juguo.gushici.ui.activity.presenter.LoginPresenter;
import com.juguo.gushici.utils.TitleBarUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * Created by Administrator on 2019/10/30.
 */

public class WebUrlActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View{

    private String title;
    private WebView urlWebView;
    private String resId;
    private TitleBarUtils titleBarUtils;


    private Context mContext;

    // 该推文是否已经收藏  1为收藏  2为未收藏
    private int isEnshrine = 2;
    private boolean isScJr;

    @Override
    protected int getLayout() {
        return R.layout.activity_web_url;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {
        mContext = this;


        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        urlWebView = (WebView) findViewById(R.id.webView);
        urlWebView.getSettings().setJavaScriptEnabled(true);
        urlWebView.setWebViewClient(new WebViewClient() {// tel://400-666-0360
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if ("tel://400-666-0360".equals(url)){
//                    String[] split = url.split("//");
//                    Intent in = new Intent(Intent.ACTION_DIAL);
//                    in.setData(Uri.parse("tel:" + split[1]));
//                    startActivity(in);
//                    return true;
//                }else {
                return false;
//                }
            }
        });
        //urlWebView.loadUrl("file:///android_asset/PrivacyGuidelines.html");
        urlWebView.loadUrl(url);
        titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setMiddleTitleText(title);
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initView() {

    }

    @Override
    public void httpCallback(LoginResponse user) {

    }

    @Override
    public void httpCallback(AccountInformationResponse response) {

    }

    @Override
    public void httpError(String e) {

    }
}
