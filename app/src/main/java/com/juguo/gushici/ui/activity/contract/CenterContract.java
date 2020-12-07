package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;

public interface CenterContract {

    interface View extends BaseMvpCallback {
        void httpCallback(Object o);
        void httpError(String e);
    }

    interface Presenter {


    }
}
