package com.juguo.gushici.dragger.component;



import com.juguo.gushici.MyApplication;
import com.juguo.gushici.dragger.ContextLife;
import com.juguo.gushici.dragger.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Administrator
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    /**
     * 提供App的Context
     * @return
     */
    @ContextLife("Application")
    MyApplication getContext();

}
