package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.bean.VersionUpdataBean;
import com.juguo.gushici.dragger.bean.User;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.response.VersionUpdataResponse;

public interface HomeContract {

    interface View extends BaseMvpCallback {
        void httpCallback(LoginResponse loginResponse);

        void httpCallback(AccountInformationResponse response);

        void httpError(String e);

        void httpCallback(VersionUpdataResponse response);
    }

    interface Presenter {

        void login(User user);

        void getAccountInformation();

        void settingVersion(VersionUpdataBean versionUpdataBean);
    }
}
