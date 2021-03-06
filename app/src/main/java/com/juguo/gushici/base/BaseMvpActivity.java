package com.juguo.gushici.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.juguo.gushici.MyApplication;
import com.juguo.gushici.dragger.component.ActivityComponent;
import com.juguo.gushici.dragger.component.DaggerActivityComponent;
import com.juguo.gushici.dragger.module.ActivityModule;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseMvpActivity<P extends BaseMvpPresenter> extends BaseActivity implements BaseMvpCallback, View.OnClickListener {
    @Inject //drager
    @io.reactivex.annotations.Nullable
    protected P mPresenter;
    protected Unbinder unbinder;

    protected abstract int getLayout();
    protected abstract void initInject();
    protected abstract void initViewAndData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initInject();
        unbinder = ButterKnife.bind(this);
        ButterKnife.bind(this);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initViewAndData();
    }

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(MyApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        unbinder.unbind();
        super.onDestroy();
    }
}
