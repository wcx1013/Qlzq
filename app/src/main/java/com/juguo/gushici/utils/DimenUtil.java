package com.juguo.gushici.utils;


import com.juguo.gushici.MyApplication;

/**
 * Created by zty on 2019/8/15.
 */

public class DimenUtil {
    /**	 * dp转px
     * @param dp
     * @return
     */
    public static int dp2px(int dp)	{
        float density = MyApplication.getApp().getResources().getDisplayMetrics().density;
        return (int) (dp*density+0.5);	
    } 
    /** px转换dip */	
    public static int px2dip(int px) {	
        final float scale = MyApplication.getApp().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }	
    /** px转换sp */	
    public static int px2sp(int pxValue) {
        final float fontScale = MyApplication.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    /** sp转换px */
    public static int sp2px(int spValue) {
        final float fontScale = MyApplication.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
