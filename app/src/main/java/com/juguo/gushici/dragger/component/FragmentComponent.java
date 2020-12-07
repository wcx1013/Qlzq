package com.juguo.gushici.dragger.component;

import android.app.Activity;


import com.juguo.gushici.dragger.FragmentScope;
import com.juguo.gushici.dragger.module.FragmentModule;
import com.juguo.gushici.ui.fragment.CenterFragment;
import com.juguo.gushici.ui.fragment.HomeFragment;
import com.juguo.gushici.ui.fragment.MineFragment;

import dagger.Component;

/**
 * @author Administrator
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(HomeFragment homeFragment);

    void inject(CenterFragment centerFragment);

    void inject(MineFragment centerFragment);

}
