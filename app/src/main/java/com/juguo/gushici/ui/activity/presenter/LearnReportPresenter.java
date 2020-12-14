package com.juguo.gushici.ui.activity.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.juguo.gushici.base.BaseMvpPresenter;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.http.DefaultObserver;
import com.juguo.gushici.http.RetrofitUtils;
import com.juguo.gushici.http.RxSchedulers;
import com.juguo.gushici.param.RecitedListParams;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.service.ApiService;
import com.juguo.gushici.ui.activity.contract.CenterContract;
import com.juguo.gushici.ui.activity.contract.LearnReportContract;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import javax.inject.Inject;

public class LearnReportPresenter extends BaseMvpPresenter<LearnReportContract.View> implements LearnReportContract.Presenter {

    @Inject
    public LearnReportPresenter() {

    }

    @Override
    public void getRecitedList(RecitedListParams params) {
        RetrofitUtils.getInstance().create(ApiService.class)
                .getRecitedList(params)
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
}
