package com.juguo.gushici.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juguo.gushici.R;

/**
 * @创建者 author
 * @创建时间 2016/5/20 15:20
 * @描述 ${公共标题类}
 * @更新者 $author$
 * @更新时间
 * @更新描述 ${TODO}
 */
public class TitleBarUtils {


    private View titleView;
    private TextView left_textview;
    private ImageView left_imageview;
    private TextView middle_textview;
    private ImageView middle_imageview;
    private TextView right_textview;
    private ImageView right_imageview;
    private RelativeLayout title_left_layout;

    public View getTitleView(){
        return titleView;
    }

    public TitleBarUtils(Activity context) {
        titleView = context.findViewById(R.id.title_bar);
        CommUtils.setImmerseLayout(titleView,context);

        left_textview = (TextView) titleView.findViewById(R.id.title_left_textview);
        left_imageview = (ImageView) titleView.findViewById(R.id.title_left_imageview);

        middle_textview = (TextView) titleView.findViewById(R.id.title_middle_textview);
        middle_imageview = (ImageView)titleView.findViewById(R.id.title_middle_imageview);

        right_textview = (TextView) titleView.findViewById(R.id.title_right_textview);
        right_imageview = (ImageView) titleView.findViewById(R.id.title_right_imageview);

        title_left_layout = (RelativeLayout)titleView.findViewById(R.id.title_left_layout);

    }

    public TitleBarUtils(View context, Context mContext, Activity activity) {

        titleView = context.findViewById(R.id.title_bar);

        left_textview = (TextView) titleView.findViewById(R.id.title_left_textview);
        left_imageview = (ImageView) titleView.findViewById(R.id.title_left_imageview);

        middle_textview = (TextView) titleView.findViewById(R.id.title_middle_textview);
        middle_imageview = (ImageView)titleView.findViewById(R.id.title_middle_imageview);

        right_textview = (TextView) titleView.findViewById(R.id.title_right_textview);
        right_imageview = (ImageView) titleView.findViewById(R.id.title_right_imageview);

        title_left_layout = (RelativeLayout)titleView.findViewById(R.id.title_left_layout);
    }

    /**
     * title 的背景色
     */

    public TitleBarUtils seteTitleBgRes(int resid) {

        titleView.setBackgroundResource(resid);

        return this;
    }
    /**
     * title的文本
     *
     * @param text
     * @return
     */
    public TitleBarUtils setMiddleTitleText(String text) {

        middle_textview.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        middle_textview.setText(text);

        return this;
    }

    /**
     * 左边的图片按钮
     *
     * @param resId
     * @return
     */
    public TitleBarUtils setMiddleImageRes(int resId) {
        middle_imageview.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        middle_imageview.setBackgroundResource(resId);
        return this;
    }

    /**
     * 左边的图片按钮
     *
     * @param resId
     * @return
     */
    public TitleBarUtils setLeftImageRes(int resId) {

        title_left_layout.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        left_imageview.setBackgroundResource(resId);

        return this;
    }

    /**
     * 左边文字按钮
     *
     * @param text
     * @return
     */
    public TitleBarUtils setLeftText(String text) {

        left_textview.setVisibility(TextUtils.isEmpty(text) ? View.GONE: View.VISIBLE);
        left_textview.setText(text);

        return this;
    }

    /**
     * 设置左边图片点击事件
     */
    public TitleBarUtils setLeftImageListener(View.OnClickListener listener){
        title_left_layout.setOnClickListener(listener);
        return this;
    }
    /**
     * 设置左边文字点击事件
     */
    public TitleBarUtils setLeftTextListener(View.OnClickListener listener){
        left_textview.setOnClickListener(listener);
        return this;
    }



    /**
     * 右边的图片按钮
     *
     * @param resId
     * @return
     */
    public TitleBarUtils setRightImageRes(int resId) {

        right_imageview.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        right_imageview.setImageResource(resId);

        return this;
    }

    public TitleBarUtils setRightImageDrawable(Drawable drawable) {

        right_imageview.setVisibility(drawable != null ? View.VISIBLE : View.GONE);
        right_imageview.setImageDrawable(drawable);

        return this;
    }

    /**
     * 左边文字按钮
     *
     * @param text
     * @return
     */
    public TitleBarUtils setRightText(String text) {

        right_textview.setVisibility(TextUtils.isEmpty(text) ? View.GONE: View.VISIBLE);
        right_textview.setText(text);

        return this;
    }

    public TextView getRightText(){
        return right_textview;
    }


    /**
     * 设置右边图片点击事件
     */
    public TitleBarUtils setRightImageListener(View.OnClickListener listener){
        right_imageview.setOnClickListener(listener);
        return this;
    }
    /**
     * 设置右边文字点击事件
     */
    public TitleBarUtils setRightTextListener(View.OnClickListener listener){
        right_textview.setOnClickListener(listener);
        return this;
    }

    public View build(){

        return titleView;
    }

    /**
     * 设置右边文字颜色
     * @param colorId
     */
    public void setRightTextColor(int colorId){
        right_textview.setTextColor(colorId);
    }

    /**
     * 设置文字透明度
     * @param alph
     */
    public void setRightTextAlph(float alph){
        right_textview.setAlpha(alph);
    }

}
