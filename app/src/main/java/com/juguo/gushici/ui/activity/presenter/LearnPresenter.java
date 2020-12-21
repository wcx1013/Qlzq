package com.juguo.gushici.ui.activity.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.juguo.gushici.base.BaseMvpPresenter;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.PlanListBean;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.http.DefaultObserver;
import com.juguo.gushici.http.RetrofitUtils;
import com.juguo.gushici.http.RxSchedulers;
import com.juguo.gushici.param.AddWithRemovePlanParams;
import com.juguo.gushici.param.LearnPlanParams;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.service.ApiService;
import com.juguo.gushici.ui.activity.contract.CenterContract;
import com.juguo.gushici.ui.activity.contract.LearnPlanContract;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import javax.inject.Inject;

public class LearnPresenter extends BaseMvpPresenter<LearnPlanContract.View> implements LearnPlanContract.Presenter {

    @Inject
    public LearnPresenter() {

    }


    @Override
    public void getLearnPlanList(LearnPlanParams learnPlanParams) {
        RetrofitUtils.getInstance().create(ApiService.class)
                .requestPlanList(learnPlanParams)
                .compose(RxSchedulers.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(new DefaultObserver<PoetryBean>((Context) mView) {
                    @Override
                    public void onSuccess(PoetryBean result) {
                        mView.httpCallback(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.httpError(e.toString());
                    }
                });
    }

    @Override
    public void removePlan(AddWithRemovePlanParams addWithRemovePlanParams) {
        RetrofitUtils.getInstance().create(ApiService.class)
                .addWithRemovePlan(addWithRemovePlanParams)
                .compose(RxSchedulers.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(new DefaultObserver<BaseResponse>((Context) mView) {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mView.httpRemovePlanCallback(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.httpError(e.toString());
                    }
                });
    }
}
