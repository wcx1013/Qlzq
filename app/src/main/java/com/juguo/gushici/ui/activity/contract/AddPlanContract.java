package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.ChangeStateBean;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.param.AddWithRemovePlanParams;
import com.juguo.gushici.param.PoetryListParams;

public interface AddPlanContract {

    interface View extends BaseMvpCallback {
        void httpCallback(PoetryBean o);
        void httpAddPlanCallback(BaseResponse o);
        void changeStateSuccess(BaseResponse o);
        void httpError(String e);
    }

    interface Presenter {

        void getPoetryList(PoetryListParams poetryListParams);

        void addPlan(AddWithRemovePlanParams addWithRemovePlanParams);
        void addPlan(AddWithRemovePlanParams addWithRemovePlanParams,boolean isFragment);

        void changeState(ChangeStateBean changeStateBean);
    }
}
