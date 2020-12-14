package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.param.LearnPlanParams;

public interface LearnPlanContract {

    interface View extends BaseMvpCallback {
        void httpCallback(PoetryBean poetryBean);
        void httpError(String e);
    }

    interface Presenter {

        void getLearnPlanList(LearnPlanParams learnPlanParams);
    }
}
