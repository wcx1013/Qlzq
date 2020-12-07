package com.juguo.gushici.ui.activity.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.juguo.gushici.base.BaseMvpPresenter;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.FeedBackBean;
import com.juguo.gushici.http.DefaultObserver;
import com.juguo.gushici.http.RetrofitUtils;
import com.juguo.gushici.http.RxSchedulers;
import com.juguo.gushici.service.ApiService;
import com.juguo.gushici.ui.activity.contract.HelpFeedbackContract;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import javax.inject.Inject;

public class HelpFeedbackPresenter extends BaseMvpPresenter<HelpFeedbackContract.View> implements HelpFeedbackContract.Presenter {

    @Inject
    public HelpFeedbackPresenter() {

    }

    @Override
    public void feedBack(FeedBackBean feedBackBean) {
        RetrofitUtils.getInstance().create(ApiService.class)
                .feedBack(feedBackBean)
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
