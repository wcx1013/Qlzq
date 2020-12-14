package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.param.EditUserInfoParams;

import java.io.File;

public interface EditUserInfoContract {

    interface View extends BaseMvpCallback {
        void httpEditNickNameCallback(BaseResponse o);
        void httpEditUserHeadCallback(BaseResponse o);
        void httpError(String e);
    }

    interface Presenter {

        void editUserNickname(EditUserInfoParams editUserInfoParams);

        void editUserHead(File file);
    }
}
