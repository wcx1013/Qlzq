package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.bean.AddPayOrderBean;
import com.juguo.gushici.bean.ChangeStateBean;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.AddPayOrderResponse;
import com.juguo.gushici.response.MemberLevelResponse;
import com.juguo.gushici.response.QueryOrderResponse;

public interface DetailContract {

    interface View extends BaseMvpCallback {
        void changeStateSuccess(Object o);
        void httpError(String e);

        void httpCallback(AddPayOrderResponse response);
        void httpCallback(QueryOrderResponse response);
        void httpCallback(MemberLevelResponse response);
        void httpCallback(AccountInformationResponse response);
    }

    interface Presenter {

        void changeState(ChangeStateBean changeStateBean);

        void addPayOrder(AddPayOrderBean addPayOrderBean);

        void queryOrder(String orderId);

        void getAccountInformation();

        void getMemberLevel();
    }
}
