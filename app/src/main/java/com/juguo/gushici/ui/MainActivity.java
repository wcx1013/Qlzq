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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juguo.gushici.MyApplication;
import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.bean.AppConfigBean;
import com.juguo.gushici.bean.VersionUpdataBean;
import com.juguo.gushici.dragger.bean.User;
import com.juguo.gushici.dragger.bean.UserInfo;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.response.VersionUpdataResponse;
import com.juguo.gushici.ui.activity.ClassChooseActivity;
import com.juguo.gushici.ui.activity.ClassChooseListActivity;
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

import java.util.ArrayList;
import java.util.List;

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

    private RadioGroup mRadioGroup1;
    private RadioGroup mRadioGroup2;
    private RadioGroup mRadioGroup3;
    private RadioGroup mRadioGroup4;
    private RadioGroup mRadioGroup5;
    private RadioGroup mRadioGroup6;


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

        mRadioGroup1 = findViewById(R.id.radioGroup1);
        mRadioGroup2 = findViewById(R.id.radioGroup2);
        mRadioGroup3 = findViewById(R.id.radioGroup3);
        mRadioGroup4 = findViewById(R.id.radioGroup4);
        mRadioGroup5 = findViewById(R.id.radioGroup5);
        mRadioGroup6 = findViewById(R.id.radioGroup6);

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
        getGetAppVersion();

        mRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_class1_kenei) {

                    mIfClass = 1;
                    nextToClassChooseActivity();
                } else if (checkedId == R.id.rb_class1_kewai) {

                    mIfClass = 0;
                    nextToClassChooseActivity();
                }
            }
        });
        mRadioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_class2_kenei) {

                    mIfClass = 1;
                    nextToClassChooseActivity();
                } else if (checkedId == R.id.rb_class2_kewai) {

                    mIfClass = 0;
                    nextToClassChooseActivity();
                }
            }
        });
        mRadioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_class3_kenei) {

                    mIfClass = 1;
                    nextToClassChooseActivity();
                } else if (checkedId == R.id.rb_class3_kewai) {

                    mIfClass = 0;
                    nextToClassChooseActivity();
                }
            }
        });
        mRadioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_class4_kenei) {

                    mIfClass = 1;
                    nextToClassChooseActivity();
                } else if (checkedId == R.id.rb_class4_kewai) {

                    mIfClass = 0;
                    nextToClassChooseActivity();
                }
            }
        });
        mRadioGroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_class5_kenei) {

                    mIfClass = 1;
                    nextToClassChooseActivity();
                } else if (checkedId == R.id.rb_class5_kewai) {

                    mIfClass = 0;
                    nextToClassChooseActivity();
                }
            }
        });
        mRadioGroup6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_class6_kenei) {

                    mIfClass = 1;
                    nextToClassChooseActivity();
                } else if (checkedId == R.id.rb_class6_kewai) {

                    mIfClass = 0;
                    nextToClassChooseActivity();
                }
            }
        });
        mRadioGroupList.add(mRadioGroup1);
        mRadioGroupList.add(mRadioGroup2);
        mRadioGroupList.add(mRadioGroup3);
        mRadioGroupList.add(mRadioGroup4);
        mRadioGroupList.add(mRadioGroup5);
        mRadioGroupList.add(mRadioGroup6);
    }

    /**
     * 获取app的版本信息
     */
    private void getGetAppVersion() {
        if (!CommUtils.isNetworkAvailable(this)) {
            ToastUtils.shortShowStr(this, Constants.NET_ERROR);
            return;
        }
        VersionUpdataBean versionUpdataBean = new VersionUpdataBean();
        versionUpdataBean.setParam(new VersionUpdataBean.VersionUpdataInfo(Constants.WX_APP_ID, CommUtils.getVersionName(this)));
        mPresenter.settingVersion(versionUpdataBean);
    }

    private String mClassTitle = "一年级";
    private int mClassGrade = 1;
    private int mIfClass = 1;

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
                mClassTitle = "一年级";
                mClassGrade = 1;
                showRadioGroup(mClassGrade - 1);
                break;
            case R.id.iv_home_class2:
                mClassTitle = "二年级";
                mClassGrade = 2;
                showRadioGroup(mClassGrade - 1);
                break;
            case R.id.iv_home_class3:
                mClassTitle = "三年级";
                mClassGrade = 3;
                showRadioGroup(mClassGrade - 1);
                break;
            case R.id.iv_home_class4:
                mClassTitle = "四年级";
                mClassGrade = 4;
                showRadioGroup(mClassGrade - 1);
                break;
            case R.id.iv_home_class5:
                mClassTitle = "五年级";
                mClassGrade = 5;
                showRadioGroup(mClassGrade - 1);
                break;
            case R.id.iv_home_class6:
                mClassTitle = "六年级";
                mClassGrade = 6;
                showRadioGroup(mClassGrade - 1);
                break;

        }
    }

    List<View> mRadioGroupList = new ArrayList<>();

    private void showRadioGroup(int index) {

        for (int i = 0; i < mRadioGroupList.size(); i++) {

            if (i == index) {
                mRadioGroupList.get(i).setVisibility(View.VISIBLE);
            } else {
                mRadioGroupList.get(i).setVisibility(View.GONE);
            }
        }
    }

    private void nextToClassChooseActivity() {

        Intent intent = new Intent(this, ClassChooseListActivity.class);
        intent.putExtra(ClassChooseActivity.TITLE, mClassTitle);
        intent.putExtra(ClassChooseActivity.GRADE, mClassGrade);
        intent.putExtra(ClassChooseActivity.IFCLASS, mIfClass);
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
        } else {
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
                    mMySharedPreferences.putValue("isOpenMember", ifPay);
                    MyApplication.adConfigList = appConfigBean.getAdConfig();
                }
                List<VersionUpdataResponse.RecAccountList> recAccountList = result.getRecAccountList();
                if (recAccountList != null && recAccountList.size() > 0) {
                    for (int i = 0; i < recAccountList.size(); i++) {
                        String payerType = recAccountList.get(i).getPayerType();
                        if ("ALI".equalsIgnoreCase(payerType)) {
                            mMySharedPreferences.putValue("ali", recAccountList.get(i).getId());
                        } else if ("WX".equalsIgnoreCase(payerType)) {
                            mMySharedPreferences.putValue("wx", recAccountList.get(i).getId());
                        }
                    }
                }
            }
        }
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
                if ("0".equals(loginType)) {
                    String password = (String) mMySharedPreferences.getValue("password", "");
                    String account = (String) mMySharedPreferences.getValue("account", "");
                    userInfo.setType(0);
                    userInfo.setPassword(password);
                    userInfo.setAccount(account);
                } else if (Wechat.NAME.equals(loginType)) {
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
//        String userLicenseAgreement = "《古诗词小学必考大全App用户使用协议》";
        String userLicenseAgreement = "《" + getString(R.string.app_name) + "App用户使用协议》";
        String privacyAgreement = "《" + getString(R.string.app_name) + "App用户隐私协议》";

//        String userLicenseAgreement = "《古诗词小学必考大全App用户使用协议》";
//        String privacyAgreement = "《古诗词小学必考大全App用户隐私协议》";

        String message = "感谢您信任并使用古诗词小学必考大全的产品和服务。在您使用古诗词小学必考大全App前，请认真阅读并了解我们的";
        String messageAll = "感谢您信任并使用古诗词小学必考大全的产品和服务。在您使用古诗词小学必考大全App前，请认真阅读并了解我们的" + userLicenseAgreement + "和" + privacyAgreement;

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
                userLicenseIntent.putExtra("title", "用户许可协议");
                startActivity(userLicenseIntent);
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