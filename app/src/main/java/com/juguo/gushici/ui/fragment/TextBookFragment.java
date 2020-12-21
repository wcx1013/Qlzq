package com.juguo.gushici.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juguo.gushici.R;
import com.juguo.gushici.adapter.AddPlanClassAdapter;
import com.juguo.gushici.adapter.AddPlanTwoAdapter;
import com.juguo.gushici.base.BaseMvpFragment;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.AddPlanClassBean;
import com.juguo.gushici.bean.AddPlanTwoBean;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.param.AddWithRemovePlanParams;
import com.juguo.gushici.param.PoetryListParams;
import com.juguo.gushici.ui.activity.AddPlanActivity;
import com.juguo.gushici.ui.activity.ClassChooseActivity;
import com.juguo.gushici.ui.activity.ClassChooseListActivity;
import com.juguo.gushici.ui.activity.contract.AddPlanContract;
import com.juguo.gushici.ui.activity.presenter.AddPlanPresenter;
import com.juguo.gushici.utils.ListUtils;
import com.juguo.gushici.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 课外
 */
public class TextBookFragment extends BaseMvpFragment<AddPlanPresenter> implements AddPlanContract.View {

    private RecyclerView mRvOneList;
    private AddPlanClassAdapter mOneTypeAdapter;//1级分类

    private RecyclerView mRvSecondList;
    private AddPlanTwoAdapter mSecondTypeAdapter;//2级分类

    private int mIfClass = 1;//是否是课内。1课内0课外
    private int mGrade = 1;//1-6年级,默认1

    private LinearLayout mLlBottomChoose;
    private TextView mTvAllChoose;
    private TextView mTvConfirmAddPlan;

    private boolean mIsMultipleChoose;
    private int mCurrentClickItem;

    public static TextBookFragment newInstance(int ifClass) {

        Bundle args = new Bundle();
        args.putInt(ClassChooseActivity.IFCLASS, ifClass);
        TextBookFragment fragment = new TextBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_add_plan;
    }

    @Override
    protected void initInject() {

        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mRvOneList = mRootView.findViewById(R.id.rv_class_list);
        mRvSecondList = mRootView.findViewById(R.id.rv_two_list);
        mLlBottomChoose = mRootView.findViewById(R.id.ll_bottom_choose);
        mTvAllChoose = mRootView.findViewById(R.id.tv_all_choose);
        mTvConfirmAddPlan = mRootView.findViewById(R.id.tv_confirm_plan);
        initAdapter();
        mIfClass = getArguments().getInt(ClassChooseActivity.IFCLASS);
        mGrade = 1;
        requestList();

        mTvAllChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mIsMultipleChoose) {
                    setAllChoose();
                }
            }
        });
        mTvConfirmAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestAddPlan();
            }
        });
    }

    private void initAdapter() {

        mOneTypeAdapter = new AddPlanClassAdapter();
        mSecondTypeAdapter = new AddPlanTwoAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvOneList.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(RecyclerView.VERTICAL);
        mRvSecondList.setLayoutManager(linearLayoutManager1);

        List<AddPlanClassBean> oneList = new ArrayList<>();
        oneList.add(new AddPlanClassBean("一年级", true));
        oneList.add(new AddPlanClassBean("二年级", false));
        oneList.add(new AddPlanClassBean("三年级", false));
        oneList.add(new AddPlanClassBean("四年级", false));
        oneList.add(new AddPlanClassBean("五年级", false));
        oneList.add(new AddPlanClassBean("六年级", false));

        mOneTypeAdapter.setNewData(oneList);
        mRvOneList.setAdapter(mOneTypeAdapter);
        mRvSecondList.setAdapter(mSecondTypeAdapter);

        mOneTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                for (int j = 0; j < baseQuickAdapter.getData().size(); j++) {
                    mOneTypeAdapter.getData().get(j).setChoose(false);
                }
                mOneTypeAdapter.getData().get(i).setChoose(true);
                mOneTypeAdapter.notifyDataSetChanged();

                mGrade = i + 1;
                requestList();
                mTvAllChoose.setSelected(false);
            }
        });
        /*mSecondTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                for (int j = 0; j < mSecondTypeAdapter.getData().size(); j++) {

                    if (j == i) {
                        mSecondTypeAdapter.getData().get(j).setChoose(true);
                    } else {
                        mSecondTypeAdapter.getData().get(j).setChoose(false);
                    }
                    mSecondTypeAdapter.getData().get(j).setChoose(false);
                }
                mSecondTypeAdapter.notifyDataSetChanged();
            }
        });*/
        mSecondTypeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                mCurrentClickItem = i;
                switch (view.getId()) {
                    case R.id.iv_single_choose:

                        if (mSecondTypeAdapter.getData().get(i).isSingleChoose()) {
                            ToastUtils.shortShowStr(getActivity(), "已经添加过了");
                        } else {
                            requestAddPlan(mSecondTypeAdapter.getData().get(i).getPoetryBean().getId());
                        }
                        break;
                    //多选
                    case R.id.iv_choose:

                        if (!mSecondTypeAdapter.getData().get(i).isSingleChoose()) {
                            boolean currentState = mSecondTypeAdapter.getData().get(i).isChoose();
                            mSecondTypeAdapter.getData().get(i).setChoose(!currentState);
                            mSecondTypeAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.shortShowStr(getActivity(), "已经添加过了");
                        }
                        break;

                }
            }
        });
    }


    @Override
    public void httpCallback(PoetryBean poetryBean) {

        if (!ListUtils.isEmpty(poetryBean.getList())) {

            List<AddPlanTwoBean> twoBeanList = new ArrayList<>();
            for (int i = 0; i < poetryBean.getList().size(); i++) {
                AddPlanTwoBean addPlanTwoBean = new AddPlanTwoBean();
                addPlanTwoBean.setPoetryBean(poetryBean.getList().get(i));
                twoBeanList.add(addPlanTwoBean);
            }
            mSecondTypeAdapter.setNewData(twoBeanList);
            // mSecondTypeAdapter.getEmptyView().setVisibility(View.GONE);
        } else {

            mSecondTypeAdapter.setNewData(new ArrayList<>());
            //mSecondTypeAdapter.getEmptyView().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void httpAddPlanCallback(BaseResponse o) {

        if (o.isSuccess()) {

            String[] chooseIdArray = null;
            if (mIsMultipleChoose) {

                chooseIdArray = getChooseId().split(",");
            } else {
                chooseIdArray = mSingChooseId.split(",");
            }
            for (int i = 0; i < chooseIdArray.length; i++) {

                for (int j = 0; j < mSecondTypeAdapter.getData().size(); j++) {

                    if (chooseIdArray[i].equals(mSecondTypeAdapter.getData().get(j).getPoetryBean().getId())) {

                        mSecondTypeAdapter.getData().get(j).setSingleChoose(true);
                    }
                }
            }
            mSecondTypeAdapter.notifyDataSetChanged();
            ToastUtils.shortShowStr(getActivity(), "添加计划成功");
        }
    }

    @Override
    public void changeStateSuccess(BaseResponse o) {

    }

    @Override
    public void httpError(String e) {

    }

    private void requestList() {
        PoetryListParams poetryListParams = new PoetryListParams();
        PoetryListParams.PoetryListBean poetryListBean = new PoetryListParams.PoetryListBean();
        poetryListBean.setIfClass(mIfClass);
        poetryListBean.setGrade(mGrade);
        poetryListParams.setParam(poetryListBean);
        mPresenter.getPoetryList(poetryListParams, true);
    }

    /**
     * @param isOpen==true==多选,isOpen==false==单选
     */
    public void openMultipleChoose(boolean isOpen) {

        mIsMultipleChoose = isOpen;
        if (isOpen) {
            mLlBottomChoose.setVisibility(View.VISIBLE);
        } else {
            mLlBottomChoose.setVisibility(View.GONE);
            mTvAllChoose.setSelected(false);
        }
        for (int i = 0; i < mSecondTypeAdapter.getData().size(); i++) {

            mSecondTypeAdapter.getData().get(i).setOpenMultipleChoose(isOpen);
            if (!isOpen) {
                mSecondTypeAdapter.getData().get(i).setChoose(false);
            }
        }
        mSecondTypeAdapter.notifyDataSetChanged();
    }

    private void setAllChoose() {

        if (!mIsMultipleChoose) {
            return;
        }
        for (int i = 0; i < mSecondTypeAdapter.getData().size(); i++) {

            boolean singleChoose = mSecondTypeAdapter.getData().get(i).isSingleChoose();
            if (!singleChoose) {
                mSecondTypeAdapter.getData().get(i).setChoose(!mTvAllChoose.isSelected());
            }
        }
        mTvAllChoose.setSelected(!mTvAllChoose.isSelected());
        mSecondTypeAdapter.notifyDataSetChanged();
    }

    public String getChooseId() {

        String ids = "";
        for (int i = 0; i < mSecondTypeAdapter.getData().size(); i++) {

            if (mSecondTypeAdapter.getData().get(i).isChoose()) {
                if (TextUtils.isEmpty(ids)) {

                    ids = mSecondTypeAdapter.getData().get(i).getPoetryBean().getId();
                } else {

                    ids = ids + "," + mSecondTypeAdapter.getData().get(i).getPoetryBean().getId();
                }
            }
        }
        return ids;
    }

    /**
     * 多选
     */
    private void requestAddPlan() {
        if (TextUtils.isEmpty(getChooseId())) {

            ToastUtils.shortShowStr(getActivity(), "请选中一项诗词");
            return;
        }

        AddWithRemovePlanParams addWithRemovePlanParams = new AddWithRemovePlanParams();
        AddWithRemovePlanParams.PlanBean planBean = new AddWithRemovePlanParams.PlanBean();
        planBean.setAddPoemIds(getChooseId());
        addWithRemovePlanParams.setParam(planBean);
        mPresenter.addPlan(addWithRemovePlanParams, true);
    }

    private String mSingChooseId;

    /**
     * 单选
     *
     * @param id
     */
    private void requestAddPlan(String id) {

        mSingChooseId = id;
        AddWithRemovePlanParams addWithRemovePlanParams = new AddWithRemovePlanParams();
        AddWithRemovePlanParams.PlanBean planBean = new AddWithRemovePlanParams.PlanBean();
        planBean.setAddPoemIds(id);
        addWithRemovePlanParams.setParam(planBean);
        mPresenter.addPlan(addWithRemovePlanParams, true);
    }
}
