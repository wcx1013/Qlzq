package com.juguo.gushici.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.FeedBackBean;
import com.juguo.gushici.ui.activity.contract.HelpFeedbackContract;
import com.juguo.gushici.ui.activity.presenter.HelpFeedbackPresenter;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.TitleBarUtils;
import com.juguo.gushici.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpFeedbackActivity extends BaseMvpActivity<HelpFeedbackPresenter> implements HelpFeedbackContract.View {

    private Context mContext;
    @BindView(R.id.et_context)
    public EditText et_context;
    @BindView(R.id.tv_input_sum)
    public TextView tv_input_sum;
    @BindView(R.id.tv_fs_erro)
    public TextView tv_fs_erro;
    @BindView(R.id.tv_qq)
    public TextView tv_qq;
    @BindView(R.id.tv_wx)
    public TextView tv_wx;
    @BindView(R.id.et_lxfs)
    public EditText et_lxfs;

    @Override
    protected int getLayout() {
        return R.layout.activity_help_feedback;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {
        mContext = this;

        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setMiddleTitleText("帮助反馈");
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CommUtils.setEditTextInhibitInputSpaceAndTextLength(et_context, 500);

        et_context.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_input_sum.setText(String.valueOf(et_context.getText().toString().trim().length()));
            }
        });
    }

    @OnClick({R.id.tv_fs, R.id.tv_qq_fz, R.id.tv_wx_fz})
    public void btn_Login_Click(View v) {
        switch (v.getId()) {
            case R.id.tv_fs:
                // 发送
                String context = et_context.getText().toString().trim();
                if (TextUtils.isEmpty(context)){
                    ToastUtils.shortShowStr(mContext, "请输入您的问题和意见？");
                }else {
//                    String encode = "";
//                    try {
//                        encode = URLEncoder.encode(context, "utf-8");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    String contact = et_lxfs.getText().toString();
                    FeedBackBean feedBackBean = new FeedBackBean();
                    feedBackBean.setParam(new FeedBackBean.FeedBackInfo(context, contact, Constants.WX_APP_ID));
                    mPresenter.feedBack(feedBackBean);
                }
                break;
            case R.id.tv_qq_fz:
                String content = tv_qq.getText().toString().split("：")[1];
                // qq复制
                ClipboardManager cm1 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm1.setText(content);
                ToastUtils.shortShowStr(mContext, "复制成功！");
                break;
            case R.id.tv_wx_fz:
                String contentWx = tv_wx.getText().toString().split("：")[1];
                // qq复制
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(contentWx);
                ToastUtils.shortShowStr(mContext, "复制成功！");
                break;
        }
    }

    @Override
    public void httpCallback(BaseResponse response) {
        if (response.isSuccess()){
            ToastUtils.shortShowStr(mContext, "提交成功！");
            finish();
        }else {
            tv_fs_erro.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void httpError(String e) {
        ToastUtils.shortShowStr(mContext, getResources().getString(R.string.erro));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
