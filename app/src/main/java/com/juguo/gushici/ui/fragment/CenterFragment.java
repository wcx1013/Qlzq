package com.juguo.gushici.ui.fragment;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpFragment;
import com.juguo.gushici.ui.activity.contract.CenterContract;
import com.juguo.gushici.ui.activity.contract.HomeContract;
import com.juguo.gushici.ui.activity.presenter.CenterPresenter;
import com.juguo.gushici.ui.activity.presenter.HomePresenter;

public class CenterFragment extends BaseMvpFragment<CenterPresenter> implements CenterContract.View {


    @Override
    protected int getLayout() {
        return R.layout.fragment_center;
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
