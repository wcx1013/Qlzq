package com.juguo.gushici.ui.activity.contract;


import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.FeedBackBean;

public interface HelpFeedbackContract {

    interface View extends BaseMvpCallback {
        void httpCallback(BaseResponse response);
        void httpError(String e);
    }

    interface Presenter {

        void feedBack(FeedBackBean feedBackBean);
    }
}
