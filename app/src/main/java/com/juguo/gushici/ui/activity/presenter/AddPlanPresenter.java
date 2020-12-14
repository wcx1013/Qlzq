package com.juguo.gushici.ui.activity.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.juguo.gushici.base.BaseMvpPresenter;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.ChangeStateBean;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.http.DefaultObserver;
import com.juguo.gushici.http.RetrofitUtils;
import com.juguo.gushici.http.RxSchedulers;
import com.juguo.gushici.param.AddWithRemovePlanParams;
import com.juguo.gushici.param.PoetryListParams;
import com.juguo.gushici.service.ApiService;
import com.juguo.gushici.ui.activity.contract.AddPlanContract;
import com.juguo.gushici.ui.activity.contract.HomeContract;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import javax.inject.Inject;

public class AddPlanPresenter extends BaseMvpPresenter<AddPlanContract.View> implements AddPlanContract.Presenter {

    @Inject
    public AddPlanPresenter() {

    }

    @Override
    public void getPoetryList(PoetryListParams poetryListParams) {
        RetrofitUtils.getInstance().create(ApiService.class)
                .requestPoetryList(poetryListParams)
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

    public void getPoetryList(PoetryListParams poetryListParams,boolean isFragment) {
        RetrofitUtils.getInstance().create(ApiService.class)
                .requestPoetryList(poetryListParams)
                .compose(RxSchedulers.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(new DefaultObserver<PoetryBean>((Fragment) mView) {
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
    public void addPlan(AddWithRemovePlanParams addWithRemovePlanParams) {
        RetrofitUtils.getInstance().create(ApiService.class)
                .addWithRemovePlan(addWithRemovePlanParams)
                .compose(RxSchedulers.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(new DefaultObserver<BaseResponse>((Context) mView) {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mView.httpAddPlanCallback(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.httpError(e.toString());
                    }
                });
    }
    @Override
    public void addPlan(AddWithRemovePlanParams addWithRemovePlanParams,boolean isFragment) {
        RetrofitUtils.getInstance().create(ApiService.class)
                .addWithRemovePlan(addWithRemovePlanParams)
                .compose(RxSchedulers.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(new DefaultObserver<BaseResponse>((Fragment) mView) {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mView.httpAddPlanCallback(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.httpError(e.toString());
                    }
                });
    }

    @Override
    public void changeState(ChangeStateBean changeStateBean) {

        RetrofitUtils.getInstance().create(ApiService.class)
                .requestChangeState(changeStateBean)
                .compose(RxSchedulers.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(new DefaultObserver<BaseResponse>((Context) mView) {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mView.changeStateSuccess(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.httpError(e.toString());
                    }
                });
    }


}
