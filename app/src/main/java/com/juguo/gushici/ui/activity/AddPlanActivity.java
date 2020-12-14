package com.juguo.gushici.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juguo.gushici.R;
import com.juguo.gushici.adapter.AddPlanClassAdapter;
import com.juguo.gushici.adapter.AddPlanTwoAdapter;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.ui.activity.contract.AddPlanContract;
import com.juguo.gushici.ui.activity.contract.CenterContract;
import com.juguo.gushici.ui.activity.presenter.AddPlanPresenter;
import com.juguo.gushici.ui.activity.presenter.CenterPresenter;
import com.juguo.gushici.ui.fragment.ExtraCurricularFragment;
import com.juguo.gushici.ui.fragment.TextBookFragment;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.ToastUtils;

public class AddPlanActivity extends BaseMvpActivity<AddPlanPresenter> implements AddPlanContract.View {


    private TextBookFragment mTextBookFragment;
    private ExtraCurricularFragment mExtraCurricularFragment;

    private RadioGroup mRadioGroup;
    private RadioButton mRbTextBook;
    private RadioButton mRbEC;//EC=ExtraCurricular

    private FrameLayout mFlRoot;
    private FrameLayout mFlBack;
    private TextView mTvAddPlan;
    private int mFragmentType = 1;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_plan;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mFlRoot = findViewById(R.id.fl_root);
        mFlBack = findViewById(R.id.fl_back);
        mTvAddPlan = findViewById(R.id.tv_add_plan);
        mRadioGroup = findViewById(R.id.radioGroup);
        mRbTextBook = findViewById(R.id.rb_text_book);
        mRbEC = findViewById(R.id.rb_ec);

        CommUtils.setImmerseLayout(mFlRoot, this);
        mTextBookFragment = new TextBookFragment();
        mExtraCurricularFragment = new ExtraCurricularFragment();
        loadMultipleRootFragment(R.id.fl_content, 0, mTextBookFragment, mExtraCurricularFragment);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_text_book) {

                    mFragmentType = 1;
                    chooseRb();
                } else if (checkedId == R.id.rb_ec) {

                    mFragmentType = 2;
                    chooseRb();
                }
            }
        });
        mRadioGroup.check(R.id.rb_text_book);
        showHideFragment(mTextBookFragment);
        mTvAddPlan.setOnClickListener(this);
        mFlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.tv_add_plan:
                addPlan();
                break;
        }
    }


    private void chooseRb() {
        if (mFragmentType == 1) {

            mRbTextBook.setTextColor(getResources().getColor(R.color.color_E9533A));
            mRbTextBook.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);

            mRbEC.setTextColor(getResources().getColor(R.color.color_141414));
            mRbEC.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            mRadioGroup.check(R.id.rb_text_book);
            showHideFragment(mTextBookFragment);
        } else {

            mRbEC.setTextColor(getResources().getColor(R.color.color_E9533A));
            mRbEC.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);

            mRbTextBook.setTextColor(getResources().getColor(R.color.color_141414));
            mRbTextBook.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            mRadioGroup.check(R.id.rb_ec);
            showHideFragment(mExtraCurricularFragment);
        }
    }

    private void addPlan() {

        boolean isOpen = false;
        if (mTvAddPlan.getText().toString().equals("添加计划")) {

            isOpen = true;
            mTvAddPlan.setText("取消");
        } else {

            isOpen = false;
            mTvAddPlan.setText("添加计划");
        }
        if (mFragmentType == 1) {

            mTextBookFragment.openMultipleChoose(isOpen);
        } else {

            mExtraCurricularFragment.openMultipleChoose(isOpen);
        }
    }


    @Override
    public void httpCallback(PoetryBean o) {

    }

    @Override
    public void httpAddPlanCallback(BaseResponse o) {

    }

    @Override
    public void changeStateSuccess(BaseResponse o) {

    }

    @Override
    public void httpError(String e) {

    }
}
