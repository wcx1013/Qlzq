package com.juguo.gushici.ui.activity.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.juguo.gushici.base.BaseMvpPresenter;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.http.DefaultObserver;
import com.juguo.gushici.http.RetrofitUtils;
import com.juguo.gushici.http.RxSchedulers;
import com.juguo.gushici.param.SearchParams;
import com.juguo.gushici.service.ApiService;
import com.juguo.gushici.ui.activity.contract.SearchContract;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import javax.inject.Inject;

public class SearchPresenter extends BaseMvpPresenter<SearchContract.View> implements SearchContract.Presenter {

    @Inject
    public SearchPresenter() {

    }

    @Override
    public void requestSearch(SearchParams searchParams) {
        RetrofitUtils.getInstance().create(ApiService.class)
                .requestSearch(searchParams)
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
