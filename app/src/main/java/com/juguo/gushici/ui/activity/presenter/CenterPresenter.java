package com.juguo.gushici.ui.activity.presenter;

import com.juguo.gushici.base.BaseMvpPresenter;
import com.juguo.gushici.ui.activity.contract.CenterContract;
import com.juguo.gushici.ui.activity.contract.HomeContract;

import javax.inject.Inject;

public class CenterPresenter extends BaseMvpPresenter<CenterContract.View> implements CenterContract.Presenter {

    @Inject
    public CenterPresenter() {

    }

}
