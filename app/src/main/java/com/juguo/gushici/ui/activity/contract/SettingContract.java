package com.juguo.gushici.ui.activity.contract;


import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.base.BaseResponse;

public interface SettingContract {

    interface View extends BaseMvpCallback {
        void httpCallback(BaseResponse response);
        void httpError(String e);
    }

    interface Presenter {

        void logOut();
    }
}
