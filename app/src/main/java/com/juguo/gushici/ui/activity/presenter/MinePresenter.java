package com.juguo.gushici.ui.activity.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.juguo.gushici.base.BaseMvpPresenter;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.VersionUpdataBean;
import com.juguo.gushici.http.DefaultObserver;
import com.juguo.gushici.http.RetrofitUtils;
import com.juguo.gushici.http.RxSchedulers;
import com.juguo.gushici.response.VersionUpdataResponse;
import com.juguo.gushici.service.ApiService;
import com.juguo.gushici.ui.activity.contract.HomeContract;
import com.juguo.gushici.ui.activity.contract.MineContract;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import javax.inject.Inject;

public class MinePresenter extends BaseMvpPresenter<MineContract.View> implements MineContract.Presenter {

    @Inject
    public MinePresenter() {

    }

    @Override
    public void settingVersion(VersionUpdataBean versionUpdataBean) {
        RetrofitUtils.getInstance().create(ApiService.class)
                .versionUpdata(versionUpdataBean)
                .compose(RxSchedulers.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(new DefaultObserver<VersionUpdataResponse>((Context) mView) {
                    @Override
                    public void onSuccess(VersionUpdataResponse result) {
                        mView.httpCallback(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.httpError(e.toString());
                    }
                });
    }

    @Override
    public void logOut() {
        RetrofitUtils.getInstance().create(ApiService.class)
                .logOut()
                .compose(RxSchedulers.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(new DefaultObserver<BaseResponse>((Context) mView) {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mView.httpCallback(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.httpError(e.toString());
                    }
                });
    }
}
