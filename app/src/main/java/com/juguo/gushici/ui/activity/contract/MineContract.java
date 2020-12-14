package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.VersionUpdataBean;
import com.juguo.gushici.response.VersionUpdataResponse;

public interface MineContract {

    interface View extends BaseMvpCallback {
        void httpCallback(VersionUpdataResponse response);
        void httpCallback(BaseResponse response);
        void httpError(String e);
    }

    interface Presenter {

      void settingVersion(VersionUpdataBean versionUpdataBean);

        void logOut();
    }
}
