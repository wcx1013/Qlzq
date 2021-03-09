package com.juguo.gushici;

/**
 * Created by Administrator on 2017/4/11.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.juguo.gushici.bean.AppConfigBean;
import com.juguo.gushici.dragger.component.AppComponent;
import com.juguo.gushici.dragger.component.DaggerAppComponent;
import com.juguo.gushici.dragger.module.AppModule;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.CrashHandler;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.view.TTAdManagerHolder;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;


import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.Fragmentation;


public class MyApplication extends Application {
    private List<Activity> oList;//用于存放所有启动的Activity的集合
    private static MyApplication instance;
    private static MyApplication app;

    public static List<AppConfigBean.AdConfig> adConfigList;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        //CrashHandler crashHandler = CrashHandler.getInstance();
        //crashHandler.init(getApplicationContext(), this);
        oList = new ArrayList<Activity>();
//        //bugly
//        CrashReport.initCrashReport(getApplicationContext(), "0b80e9c491", false);
//        //LeakCanary
//        LeakCanary.install(this);
        // 判断sd卡是否存在
        Constants.getCachePath(this);

        MobSDK.init(this);
        // 友盟初始化
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
//        MobclickAgent.setDebugMode(true);
////         开启debug模式
//        UMConfigure.setLogEnabled(true);
//        MobclickAgent.setCheckDevice(false);//不采集手机mac地址
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);//设置场景模式为普通
//        // 友盟打印日志
//        UMConfigure.setLogEnabled(false);

        //穿山甲SDK初始化
        TTAdManagerHolder.init(this);

        MySharedPreferences mySharedPreferences = new MySharedPreferences(this, "Shared");
        int startNumber = (int) mySharedPreferences.getValue("startNumber", 0);
        if (startNumber < 3){
            startNumber++;
            mySharedPreferences.putValue("startNumber", startNumber);
        }
        if(app==null){
            Log.d("test","app==null");
        }
       /* Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .install();*/
    }

    public static AppComponent getAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            System.exit(0);
        } catch (Exception e) {
            e.toString();
        }
    }

    public static MyApplication getApp() {
        Log.d("test1","app==null");
        return app;
    }

    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
        // 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
        //判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }

}