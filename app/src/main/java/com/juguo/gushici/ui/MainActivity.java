package com.juguo.gushici.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.dragger.bean.User;
import com.juguo.gushici.dragger.bean.UserInfo;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.ui.activity.ClassChooseActivity;
import com.juguo.gushici.ui.activity.LearnPlanActivity;
import com.juguo.gushici.ui.activity.LearnReportActivity;
import com.juguo.gushici.ui.activity.LoginActivity;
import com.juguo.gushici.ui.activity.SearchActivity;
import com.juguo.gushici.ui.activity.WebUrlActivity;
import com.juguo.gushici.ui.activity.contract.HomeContract;
import com.juguo.gushici.ui.activity.presenter.HomePresenter;
import com.juguo.gushici.ui.activity.MineActivity;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.CommonDialog;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.ToastUtils;
import com.juguo.gushici.utils.Util;
import com.juguo.gushici.view.XCRoundImageView;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseMvpActivity<HomePresenter> implements HomeContract.View {

    private XCRoundImageView mImgUser;
    private LinearLayout mLlHomeMine;
    private ImageView mIvLearnReport;
    private LinearLayout mLlHomeLearnReport;
    private ImageView mIvLearnPlan;
    private LinearLayout mLlHomeLearnPlan;
    private ImageView mIvSearch;
    private LinearLayout mLlHomeSearch;

    private ImageView mIvHomeClass1;
    private ImageView mIvHomeClass2;
    private ImageView mIvHomeClass3;
    private ImageView mIvHomeClass4;
    private ImageView mIvHomeClass5;
    private ImageView mIvHomeClass6;

    private MySharedPreferences mMySharedPreferences;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mImgUser = findViewById(R.id.img_user);
        mLlHomeMine = findViewById(R.id.ll_home_mine);
        mIvLearnReport = findViewById(R.id.iv_learn_report);
        mLlHomeLearnReport = findViewById(R.id.ll_home_learn_report);
        mIvLearnPlan = findViewById(R.id.iv_learn_plan);
        mLlHomeLearnPlan = findViewById(R.id.ll_home_learn_plan);
        mIvSearch = findViewById(R.id.iv_search);

        mLlHomeSearch = findViewById(R.id.ll_home_search);
        mIvHomeClass1 = findViewById(R.id.iv_home_class1);
        mIvHomeClass2 = findViewById(R.id.iv_home_class2);
        mIvHomeClass3 = findViewById(R.id.iv_home_class3);
        mIvHomeClass4 = findViewById(R.id.iv_home_class4);
        mIvHomeClass5 = findViewById(R.id.iv_home_class5);
        mIvHomeClass6 = findViewById(R.id.iv_home_class6);

        mLlHomeMine.setOnClickListener(this);
        mLlHomeLearnReport.setOnClickListener(this);
        mLlHomeLearnPlan.setOnClickListener(this);
        mLlHomeSearch.setOnClickListener(this);

        mIvHomeClass1.setOnClickListener(this);
        mIvHomeClass2.setOnClickListener(this);
        mIvHomeClass3.setOnClickListener(this);
        mIvHomeClass4.setOnClickListener(this);
        mIvHomeClass5.setOnClickListener(this);
        mIvHomeClass6.setOnClickListener(this);

        // 没有登录调用接口获取token
        // 获取手机唯一码
        mMySharedPreferences = new MySharedPreferences(this, "Shared");
        String uuid = (String) mMySharedPreferences.getValue("uuid", "");
        if (TextUtils.isEmpty(uuid)) {
            mMySharedPreferences.putValue("uuid", CommUtils.getUniqueID(this));
        }
        if (CommUtils.isLogin(this)) {
            String userIcon = (String) mMySharedPreferences.getValue("userIcon", "");
            // 设置用户名和用户icon
            Util.displayCircleCropImgView(this, mImgUser, userIcon, R.mipmap.ic_user_place);
        }

        showPrivacyDialog();
        mPresenter.login(loginType());
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (!CommUtils.isLogin(this)) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        switch (v.getId()) {
            case R.id.ll_home_mine:

                intent = new Intent(this, MineActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_home_learn_report:

                intent = new Intent(this, LearnReportActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_home_learn_plan:

                intent = new Intent(this, LearnPlanActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_home_search:

                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_home_class1:
                String title = "一年级";
                int grade = 1;
                nextToClassChooseActivity(title, grade);
                break;
            case R.id.iv_home_class2:
                title = "二年级";
                grade = 2;
                nextToClassChooseActivity(title, grade);
                break;
            case R.id.iv_home_class3:
                title = "三年级";
                grade = 3;
                nextToClassChooseActivity(title, grade);
                break;
            case R.id.iv_home_class4:
                title = "四年级";
                grade = 4;
                nextToClassChooseActivity(title, grade);
                break;
            case R.id.iv_home_class5:
                title = "五年级";
                grade = 5;
                nextToClassChooseActivity(title, grade);
                break;
            case R.id.iv_home_class6:
                title = "六年级";
                grade = 6;
                nextToClassChooseActivity(title, grade);
                break;

        }
    }

    private void nextToClassChooseActivity(String title, int grade) {

        Intent intent = new Intent(this, ClassChooseActivity.class);
        intent.putExtra(ClassChooseActivity.TITLE, title);
        intent.putExtra(ClassChooseActivity.GRADE, grade);
        startActivity(intent);
    }

    @Override
    public void httpCallback(LoginResponse loginResponse) {

        if (loginResponse.isSuccess()) {
            String result = loginResponse.getResult();
            if (!TextUtils.isEmpty(result)) {
                mMySharedPreferences.putValue("token", result);

                // 已经获取到token调用其他接口
                mPresenter.getAccountInformation();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (CommUtils.isLogin(this)) {
            if (mMySharedPreferences != null) {
                String userIcon = (String) mMySharedPreferences.getValue("userIcon", "");

                // 设置用户名和用户icon
                Util.displayCircleCropImgView(this, mImgUser, userIcon, R.mipmap.ic_user_place);
            }
        }else {
            mImgUser.setImageDrawable(getResources().getDrawable(R.mipmap.ic_user_place));
        }
    }

    @Override
    public void httpCallback(AccountInformationResponse response) {
        if (response.isSuccess()) {
            AccountInformationResponse.AccountInformationInfo result = response.getResult();
            if (result != null) {
                mMySharedPreferences.putValue("MemberUser", result.getId());
                mMySharedPreferences.putValue("level", result.getLevel());
                mMySharedPreferences.putValue("dueTime", result.getDueTime());
            }
        }
    }

    @Override
    public void httpError(String e) {

    }

    /**
     * 封装登录请求参数
     *
     * @return
     */
    private User loginType() {
        User user = new User();
        UserInfo userInfo = new UserInfo();
        if (CommUtils.isLogin(this)) {
            String loginType = (String) mMySharedPreferences.getValue("loginType", "");
            String userId = (String) mMySharedPreferences.getValue("userId", "");
            if (TextUtils.isEmpty(userId)) {
                String uuid = (String) mMySharedPreferences.getValue("uuid", "");
                if (TextUtils.isEmpty(uuid)) {
                    uuid = CommUtils.getUniqueID(this);
                }
                userInfo.setType(2);
                userInfo.setUnionInfo(uuid);
                mMySharedPreferences.clear();
            } else {
                if (Wechat.NAME.equals(loginType)) {
                    userInfo.setType(3);
                    userInfo.setUnionInfo(userId);
                } else if (QQ.NAME.equals(loginType)) {
                    userInfo.setType(4);
                    userInfo.setUnionInfo(userId);
                }
            }
        } else {
            String uuid = (String) mMySharedPreferences.getValue("uuid", "");
            userInfo.setType(2);
            userInfo.setUnionInfo(uuid);
        }
        userInfo.setAppId(Constants.WX_APP_ID);
        user.setParam(userInfo);
        return user;
    }

    private void showPrivacyDialog() {
        boolean isAgree = (boolean) mMySharedPreferences.getValue("isagree", false);
        if (isAgree) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Dialog dialog;
//        builder.setTitle("标题");
        builder.setCancelable(false);//点击屏幕和返回键对话框不消失
        LinearLayout relativeLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_agree, null);
        builder.setView(relativeLayout);
        builder.setCancelable(false);
        Button bt_cancel = (Button) relativeLayout.findViewById(R.id.bt_cancle);
        Button bt_sure = (Button) relativeLayout.findViewById(R.id.bt_sure);
        TextView tv_message = (TextView) relativeLayout.findViewById(R.id.tv_message);


//        40 51 用户使用协议

//        53 64 隐私协议


        //设置文字
//        String userLicenseAgreement = "《小学古诗词App用户使用协议》";
        String userLicenseAgreement = "《" + getString(R.string.app_name) + "App用户使用协议》";
        String privacyAgreement = "《" + getString(R.string.app_name) + "App用户隐私协议》";

//        String userLicenseAgreement = "《小学古诗词App用户使用协议》";
//        String privacyAgreement = "《小学古诗词App用户隐私协议》";

        String message = "感谢您信任并使用小学古诗词的产品和服务。在您使用小学古诗词App前，请认真阅读并了解我们的";
        String messageAll = "感谢您信任并使用小学古诗词的产品和服务。在您使用小学古诗词App前，请认真阅读并了解我们的" + userLicenseAgreement + "和" + privacyAgreement;

        int start_User = message.length();
        int end_User = start_User + userLicenseAgreement.length() - 1;

        int start_Privacy = end_User + 3;
        int end_Privacy = start_Privacy + privacyAgreement.length() - 1;

        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        spannableStringBuilder.append(messageAll);
        ClickableSpan userLicenseClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent userLicenseIntent = new Intent(MainActivity.this, WebUrlActivity.class);
                userLicenseIntent.putExtra("url", "file:///android_asset/UserLicenseAgreement.html");
            }
        };
        ClickableSpan privacyClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent privacyIntent = new Intent(MainActivity.this, WebUrlActivity.class);
                privacyIntent.putExtra("url", "file:///android_asset/PrivacyGuidelines.html");
                privacyIntent.putExtra("title", "用户隐私协议");
                startActivity(privacyIntent);

            }
        };


        spannableStringBuilder.setSpan(userLicenseClickableSpan, start_User, end_User, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(privacyClickableSpan, start_Privacy, end_Privacy, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        tv_message.setMovementMethod(LinkMovementMethod.getInstance());
        tv_message.setText(spannableStringBuilder);


        /********************************************************/


        dialog = builder.show();

        bt_cancel.setOnClickListener(v -> {
            show_Toast("拜拜啦~");
            dialog.dismiss();

            finish();
        });


        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMySharedPreferences.putValue("isagree", true);
                dialog.dismiss();

            }
        });
    }

    //重写该方法之后，当弹出授权对话框时，我们点击允许授权成功时，会自动执行注解@NeedsPermission所标注的方法里面的逻辑
    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 被用户拒绝
     */
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        ToastUtils.shortShowStr(this, "权限未授予，部分功能无法使用");
    }

}