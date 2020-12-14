package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.bean.ChangeStateBean;

public interface DetailContract {

    interface View extends BaseMvpCallback {
        void changeStateSuccess(Object o);
        void httpError(String e);
    }

    interface Presenter {

        void changeState(ChangeStateBean changeStateBean);
    }
}
