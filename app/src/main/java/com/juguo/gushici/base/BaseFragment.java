package com.juguo.gushici.base;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import me.yokeyword.fragmentation.SupportFragment;


public class BaseFragment<A extends BaseActivity> extends SupportFragment implements LifecycleOwner {

    public A mActivity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected boolean isNeedToAddBackStack() {
        return true;
    }

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (A) requireActivity();
    }

    /**
     * 获取绑定的Activity，防止出现 getActivity() 为空
     */
    public A getBindingActivity() {
        return mActivity;
    }
}
