package com.juguo.gushici.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juguo.gushici.R;
import com.juguo.gushici.adapter.ClassChooseListAdapter;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.AddPlanTwoBean;
import com.juguo.gushici.bean.ChangeStateBean;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.param.AddWithRemovePlanParams;
import com.juguo.gushici.param.PoetryListParams;
import com.juguo.gushici.ui.activity.contract.AddPlanContract;
import com.juguo.gushici.ui.activity.presenter.AddPlanPresenter;
import com.juguo.gushici.utils.ListUtils;
import com.juguo.gushici.utils.TitleBarUtils;
import com.juguo.gushici.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ClassChooseListActivity extends BaseMvpActivity<AddPlanPresenter> implements AddPlanContract.View {

    private RecyclerView mRvList;
    private ClassChooseListAdapter mClassChooseListAdapter;

    private int mGrade = 1;
    private int mIfClass = 1;//是否是课内。1课内0课外
    private boolean mIsResume;

    private PoetryBean poetryBean;

    @Override
    protected int getLayout() {
        return R.layout.activity_class_choose_list;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mRvList = findViewById(R.id.rv_list);
        mIfClass = getIntent().getIntExtra(ClassChooseActivity.IFCLASS, 1);
        mGrade = getIntent().getIntExtra(ClassChooseActivity.GRADE, 1);

        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (mIfClass == 1) {
            titleBarUtils.setMiddleTitleText("课内古诗");
        } else {
            titleBarUtils.setMiddleTitleText("课外古诗");
        }
        titleBarUtils.setRightImageDrawable(getResources().getDrawable(R.mipmap.ic_home_search));
        titleBarUtils.setRightImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClassChooseListActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        initAdapter();
        requestList();
    }

    private void initAdapter() {

        mClassChooseListAdapter = new ClassChooseListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvList.setLayoutManager(linearLayoutManager);

        View emptyView = getLayoutInflater().inflate(R.layout.view_empty_text, null);
        mClassChooseListAdapter.setEmptyView(emptyView);
        mRvList.setAdapter(mClassChooseListAdapter);

        mClassChooseListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                Intent intent = new Intent(ClassChooseListActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.POETRY_DATA,poetryBean);
                intent.putExtra(DetailActivity.POETRY_INDEX, i);
                startActivity(intent);
            }
        });
        mClassChooseListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                switch (view.getId()) {
                    case R.id.iv_cover_layout:

                        changeState(i);
                        index = i;
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsResume) {
            requestList();
        } else {
            mIsResume = true;
        }
    }

    @Override
    public void httpCallback(PoetryBean poetryBean) {

        if (!ListUtils.isEmpty(poetryBean.getList())) {

            this.poetryBean=poetryBean;
            List<AddPlanTwoBean> twoBeanList = new ArrayList<>();
            for (int i = 0; i < poetryBean.getList().size(); i++) {
                AddPlanTwoBean addPlanTwoBean = new AddPlanTwoBean();
                addPlanTwoBean.setPoetryBean(poetryBean.getList().get(i));
                twoBeanList.add(addPlanTwoBean);
            }
            mClassChooseListAdapter.setNewData(twoBeanList);
            // mSecondTypeAdapter.getEmptyView().setVisibility(View.GONE);
        } else {

            mClassChooseListAdapter.setNewData(new ArrayList<>());
            //mSecondTypeAdapter.getEmptyView().setVisibility(View.VISIBLE);
        }
        dialogDismiss();
    }

    @Override
    public void httpAddPlanCallback(BaseResponse o) {

        if (o.isSuccess()) {

            //mClassChooseListAdapter.getData().get(index).setChoose(false);
            //mClassChooseListAdapter.notifyDataSetChanged();
            //requestList();
            //mClassChooseListAdapter.remove(index);
            mClassChooseListAdapter.notifyDataSetChanged();
        } else {
        }
        dialogDismiss();
    }

    @Override
    public void changeStateSuccess(BaseResponse o) {

        dialogDismiss();
        if (o.isSuccess()) {
            ToastUtils.shortShowStr(this, "修改背诵状态成功");
            //requestAddPlan(mClassChooseListAdapter.getData().get(index).getPoetryBean().getId());
        } else {
            ToastUtils.shortShowStr(this, "修改背诵状态失败");
        }
    }

    @Override
    public void httpError(String e) {

        dialogDismiss();
    }


    private void requestList() {
        dialogShow();
        PoetryListParams poetryListParams = new PoetryListParams();
        PoetryListParams.PoetryListBean poetryListBean = new PoetryListParams.PoetryListBean();
        poetryListBean.setIfClass(mIfClass);
        poetryListBean.setGrade(mGrade);
        poetryListParams.setParam(poetryListBean);
        mPresenter.getPoetryList(poetryListParams);
    }

    private String id;
    private int index;

    /**
     * 单选移除
     *
     * @param id
     */
    private void requestAddPlan(String id) {

        AddWithRemovePlanParams addWithRemovePlanParams = new AddWithRemovePlanParams();
        AddWithRemovePlanParams.PlanBean planBean = new AddWithRemovePlanParams.PlanBean();
        planBean.setRmPoemIds(id);
        addWithRemovePlanParams.setParam(planBean);
        mPresenter.addPlan(addWithRemovePlanParams);
    }

    private void changeState(int itemIndex) {

        int mRecited = mClassChooseListAdapter.getData().get(itemIndex).getPoetryBean().getRecited();
        String poemId = mClassChooseListAdapter.getData().get(itemIndex).getPoetryBean().getId();
        dialogShow();
        //背诵状态。0未背诵1已背2取消已背
        if (mRecited == 0 || mRecited == 2) {

            mRecited = 1;
        } else {
            mRecited = 2;
        }
        ChangeStateBean changeStateBean = new ChangeStateBean();
        ChangeStateBean.ChangeStateInfo changeStateInfo = new ChangeStateBean.ChangeStateInfo();
        changeStateInfo.setPoemId(poemId);
        changeStateInfo.setRecited(mRecited);
        changeStateBean.setParam(changeStateInfo);

        mPresenter.changeState(changeStateBean);
    }
}
