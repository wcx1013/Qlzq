package com.juguo.gushici.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @创建者 xlq
 * @创建时间 2016/5/23 11:49
 * @描述 ${Toast工具类}
 * @更新者 $author$
 * @更新时间
 * @更新描述 ${TODO}
 */
public class ToastUtils {
    private static Toast toast = null;
    /**
     * 短提示 by xlq
     *
     * @param context
     * @param content
     */
    public static void shortShowStr(Context context, String content)
    {
        if(context == null){
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context,null, Toast.LENGTH_SHORT);
            toast.setText(content);
        } else {
            toast.cancel();
            toast = Toast.makeText(context,null, Toast.LENGTH_SHORT);
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * 长提示 by xlq
     *
     * @param context
     * @param content
     */
    public static void longShowStr(Context context, String content)
    {
        if(context == null){
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context,null, Toast.LENGTH_LONG);
            toast.setText(content);
        } else {
            toast.cancel();
            toast = Toast.makeText(context,null, Toast.LENGTH_LONG);
            toast.setText(content);
        }
        toast.show();
    }
}
