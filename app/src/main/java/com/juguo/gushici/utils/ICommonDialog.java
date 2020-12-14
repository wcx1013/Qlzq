package com.juguo.gushici.utils;

import android.view.View;
import android.view.ViewGroup;

public interface ICommonDialog {

    View getContentView();

    /**
     * 设置内容布局id
     *
     * @param layoutId
     */
    void setContentView(int layoutId);

    /**
     * 设置内容
     *
     * @param view
     */
    void setContentView(View view);

    /**
     * 设置内容
     *
     * @param view
     * @param params
     */
    void setContentView(View view, ViewGroup.LayoutParams params);

    /**
     * 返回默认的padding
     *
     * @return
     */
    int getDefaultPadding();

    /**
     * 设置左边间距
     *
     * @param padding
     * @return
     */
    CommonDialog paddingLeft(int padding);

    /**
     * 设置顶部间距
     *
     * @param padding
     * @return
     */
    CommonDialog paddingTop(int padding);

    /**
     * 设置右边间距
     *
     * @param padding
     * @return
     */
    CommonDialog paddingRight(int padding);

    /**
     * 设置底部间距
     *
     * @param padding
     * @return
     */
    CommonDialog paddingBottom(int padding);

    /**
     * 设置上下左右间距
     *
     * @param paddings
     * @return
     */
    CommonDialog paddings(int paddings);


    /**
     * 设置窗口显示的位置
     *
     * @param gravity
     * @return
     */
    CommonDialog setGrativity(int gravity);


    /**
     * 显示顶部
     */
    void showTop();

    /**
     * 显示中央
     */
    void showCenter();

    /**
     * 显示底部
     */
    void showBottom();

    /**
     * 设置全屏
     *
     * @return
     */
    CommonDialog setFullScreen();
    /**
     * 设置宽度
     *
     * @param width
     * @return
     */
    CommonDialog setWidth(int width);

    /**
     * 设置高度
     *
     * @param height
     * @return
     */
    CommonDialog setHeight(int height);

    /**
     * 设置窗口动画style
     *
     * @param resId
     * @return
     */
    CommonDialog setAnimations(int resId);
}
