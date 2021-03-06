package com.juguo.gushici.dragger.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.juguo.gushici.dragger.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author Administrator
 */
@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return fragment.getActivity();
    }
}
