package com.juguo.gushici.base;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.juguo.gushici.MyApplication;
import com.juguo.gushici.R;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.KeyBoradHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Administrator on 2018/1/29.
 */

public class BaseActivity extends SupportActivity implements LifecycleOwner, ViewTreeObserver.OnGlobalLayoutListener {
    private MyApplication application;
    private BaseActivity oContext;
    public AlertDialog alertDialog;
    private ImmersionBar mImmersionBar;//状态栏沉浸


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (application == null) {
            // 得到Application对象
            application = MyApplication.getApp();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法
        EventBus.getDefault().register(this);

        statusBarConfig().init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object myEvent) {

    }

    /**
     * 初始化沉浸式状态栏
     */
    protected ImmersionBar statusBarConfig() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(statusBarDarkFont())    //默认状态栏字体颜色为黑色
                .keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
        //必须设置View树布局变化监听，否则软键盘无法顶上去，还有模式必须是SOFT_INPUT_ADJUST_PAN
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        return mImmersionBar;
    }

    /**
     * 获取状态栏字体颜色
     */
    public boolean statusBarDarkFont() {
        //返回false表示白色字体
        return true;
    }

    // 添加Activity方法
    public void addActivity() {
        application.addActivity_(oContext);// 调用BaseApplication的添加Activity方法
    }

    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity_(oContext);// 调用BaseApplication的销毁单个Activity方法
    }

    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity_();// 调用BaseApplication的销毁所有Activity方法
    }

    /* 把Toast定义成一个方法  可以重复使用，使用时只需要传入需要提示的内容即可*/
    public void show_Toast(String text) {
        Toast.makeText(oContext, text, Toast.LENGTH_SHORT).show();
    }

    public boolean isEmpty(String str) {
        if (!TextUtils.isEmpty(str) && !str.equals("null")) {
            return false;
        } else {
            return true;
        }
    }

    //等待上传的加载框
    public void dialogShow() {
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.dialog_common, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(oContext, R.style.loading_dialog_style).setView(convertView);
        alertDialog = dialog.create();
        alertDialog.show();
    }

    public void dialogDismiss() {
        if (alertDialog != null) alertDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity();
        EventBus.getDefault().unregister(this);
        if (mImmersionBar != null) mImmersionBar.destroy();
    }

    @Override
    public void onGlobalLayout() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 防止重复快速点击跳转多个界面
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (CommUtils.checkDoubleClick()) {
                return true;
            }

            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (KeyBoradHelper.isShouldHideKeyboard(v, ev)) { //判断用户点击的是否是输入框以外的区域
                KeyBoradHelper.hideKeyboard(oContext, v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}