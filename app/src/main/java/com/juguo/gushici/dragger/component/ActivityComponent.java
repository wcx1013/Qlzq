package com.juguo.gushici.dragger.component;

import android.app.Activity;


import com.juguo.gushici.dragger.ActivityScope;
import com.juguo.gushici.dragger.module.ActivityModule;
import com.juguo.gushici.ui.MainActivity;
import com.juguo.gushici.ui.activity.AddPlanActivity;
import com.juguo.gushici.ui.activity.ClassChooseActivity;
import com.juguo.gushici.ui.activity.ClassChooseListActivity;
import com.juguo.gushici.ui.activity.DetailActivity;
import com.juguo.gushici.ui.activity.EditUserInfoActivity;
import com.juguo.gushici.ui.activity.HelpFeedbackActivity;
import com.juguo.gushici.ui.activity.LearnPlanActivity;
import com.juguo.gushici.ui.activity.LearnReportActivity;
import com.juguo.gushici.ui.activity.LoginActivity;
import com.juguo.gushici.ui.activity.SearchActivity;
import com.juguo.gushici.ui.activity.SettingActivity;
import com.juguo.gushici.ui.activity.SplashActivity;
import com.juguo.gushici.ui.activity.WebUrlActivity;
import com.juguo.gushici.ui.activity.MineActivity;

import dagger.Component;

/**
 * @author Administrator
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(MainActivity mainActivity);

    void inject(MineActivity mineActivity);

    void inject(LearnReportActivity learnReportActivity);

    void inject(LearnPlanActivity learnPlanActivity);

    void inject(SearchActivity searchActivity);

    void inject(AddPlanActivity addPlanActivity);

    void inject(DetailActivity addPlanActivity);

    void inject(ClassChooseActivity classChooseActivity);

    void inject(ClassChooseListActivity classChooseListActivity);

    void inject(EditUserInfoActivity editUserInfoActivity);

    void inject(SplashActivity splashActivity);

    void inject(LoginActivity loginActivity);

    void inject(WebUrlActivity webUrlActivity);

    void inject(SettingActivity settingActivity);

    void inject(HelpFeedbackActivity helpFeedbackActivity);

}
