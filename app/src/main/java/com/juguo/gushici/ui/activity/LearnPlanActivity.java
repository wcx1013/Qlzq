package com.juguo.gushici.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juguo.gushici.R;
import com.juguo.gushici.adapter.LearnPlanAdapter;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.param.LearnPlanParams;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.ui.activity.contract.HomeContract;
import com.juguo.gushici.ui.activity.contract.LearnPlanContract;
import com.juguo.gushici.ui.activity.presenter.HomePresenter;
import com.juguo.gushici.ui.activity.presenter.LearnPresenter;
import com.juguo.gushici.utils.ListUtils;
import com.juguo.gushici.utils.TitleBarUtils;

import java.util.ArrayList;

/**
 * 学习计划
 */
public class LearnPlanActivity extends BaseMvpActivity<LearnPresenter> implements LearnPlanContract.View {

    private RecyclerView mRvList;
    private LearnPlanAdapter mLearnPlanAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_learn_plan;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mRvList = findViewById(R.id.rv_list);

        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setMiddleTitleText(getString(R.string.learn_plan));
        titleBarUtils.setRightText(getString(R.string.add_plan));
        titleBarUtils.setRightTextColor(getResources().getColor(R.color.color_C52409));
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarUtils.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlan();
            }
        });
        initAdapter();
        requestPlan();
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void httpCallback(PoetryBean poetryBean) {

        if (!ListUtils.isEmpty(poetryBean.getList())) {

            mLearnPlanAdapter.setNewData(poetryBean.getList());
            mLearnPlanAdapter.getEmptyView().setVisibility(View.GONE);
        } else {

            mLearnPlanAdapter.setNewData(new ArrayList<>());
            mLearnPlanAdapter.getEmptyView().setVisibility(View.VISIBLE);
        }
        dialogDismiss();
    }

    @Override
    public void httpError(String e) {

        dialogDismiss();
    }

    private void initAdapter() {

        mLearnPlanAdapter = new LearnPlanAdapter();
        View view = getLayoutInflater().inflate(R.layout.learn_plan_empty_view, null);
        mLearnPlanAdapter.setEmptyView(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvList.setLayoutManager(linearLayoutManager);
        mRvList.setAdapter(mLearnPlanAdapter);

        mLearnPlanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                clickListItem(i);
            }
        });
    }

    private void addPlan() {

        Intent intent = new Intent(this, AddPlanActivity.class);
        startActivity(intent);
    }

    private void clickListItem(int index) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.POETRY_DATA, mLearnPlanAdapter.getData().get(index));
        startActivity(intent);
    }

    private void requestPlan() {
        dialogShow();
        LearnPlanParams learnPlanParams = new LearnPlanParams();
        LearnPlanParams.LearnPlanBean learnPlanBean = new LearnPlanParams.LearnPlanBean();
        learnPlanParams.setParam(learnPlanBean);
        mPresenter.getLearnPlanList(learnPlanParams);
    }
}
