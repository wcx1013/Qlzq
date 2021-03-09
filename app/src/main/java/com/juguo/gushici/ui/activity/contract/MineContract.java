package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.AddPayOrderBean;
import com.juguo.gushici.bean.VersionUpdataBean;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.AddPayOrderResponse;
import com.juguo.gushici.response.MemberLevelResponse;
import com.juguo.gushici.response.QueryOrderResponse;
import com.juguo.gushici.response.VersionUpdataResponse;

public interface MineContract {

    interface View extends BaseMvpCallback {
        void httpCallback(VersionUpdataResponse response);

        void httpCallback(BaseResponse response);

        void httpError(String e);

        void httpCallback(AddPayOrderResponse response);
        void httpCallback(QueryOrderResponse response);
        void httpCallback(MemberLevelResponse response);
        void httpCallback(AccountInformationResponse response);
    }

    interface Presenter {

        void settingVersion(VersionUpdataBean versionUpdataBean);

        void logOut();

        void addPayOrder(AddPayOrderBean addPayOrderBean);

        void queryOrder(String orderId);

        void getAccountInformation();

        void getMemberLevel();
    }
}
