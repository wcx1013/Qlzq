package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.RegisterBean;
import com.juguo.gushici.dragger.bean.User;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;

public interface RegisterContract {

    interface View extends BaseMvpCallback {
        void httpError(String e);

        void httpCallback(BaseResponse o);
        void httpCallback(LoginResponse user);
        void httpCallback(AccountInformationResponse response);
    }

    interface Presenter {

        void register(RegisterBean registerBean);

        void login(User user);

        void getAccountInformation();
    }
}
