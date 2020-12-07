package com.juguo.gushici.ui.fragment;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseFragment;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.base.BaseMvpFragment;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.ui.activity.contract.HomeContract;
import com.juguo.gushici.ui.activity.contract.SplashContract;
import com.juguo.gushici.ui.activity.presenter.HomePresenter;
import com.juguo.gushici.ui.activity.presenter.SplashPresenter;

public class HomeFragment extends BaseMvpFragment<HomePresenter> implements HomeContract.View {


    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

    }


    @Override
    public void httpCallback(Object o) {

    }

    @Override
    public void httpError(String e) {

    }
}
