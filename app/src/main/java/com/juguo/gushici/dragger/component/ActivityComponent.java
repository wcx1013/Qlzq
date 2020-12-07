package com.juguo.gushici.dragger.component;

import android.app.Activity;


import com.juguo.gushici.dragger.ActivityScope;
import com.juguo.gushici.dragger.module.ActivityModule;
import com.juguo.gushici.ui.activity.HelpFeedbackActivity;
import com.juguo.gushici.ui.activity.LoginActivity;
import com.juguo.gushici.ui.activity.SettingActivity;
import com.juguo.gushici.ui.activity.SplashActivity;
import com.juguo.gushici.ui.activity.WebUrlActivity;

import dagger.Component;

/**
 * @author Administrator
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(SplashActivity splashActivity);

    void inject(LoginActivity loginActivity);

    void inject(WebUrlActivity webUrlActivity);

    void inject(SettingActivity settingActivity);

    void inject(HelpFeedbackActivity helpFeedbackActivity);

}
