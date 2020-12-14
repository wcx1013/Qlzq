package com.juguo.gushici.ui.activity.contract;

import com.juguo.gushici.base.BaseMvpCallback;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.param.SearchParams;

public interface SearchContract {

    interface View extends BaseMvpCallback {
        void httpCallback(PoetryBean o);
        void httpError(String e);
    }

    interface Presenter {

        void requestSearch(SearchParams searchParams);
    }
}
