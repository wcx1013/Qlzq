package com.juguo.gushici.utils;

import android.content.Context;
import android.os.Environment;

public class Constants {

    //  573c67e79ee69e07ae4087ede27d0465

    // 测试环境
    public static String BASE_URL = "http://app.91juguo.com/testJ/";
    // 正式环境
 //   public static String BASE_URL = "http://app.91juguo.com/api/";
//    public static String BASE_URL = "http://app.91juguo.com/bgtest/";

    // 微信appId
    public static final String WX_APP_ID = "wx2f677d639dd870b3";
    // 申请支付功能的商户号
    public static final String WX_MCH_ID = "1525928841";
    // 商品平台API密钥
    public static final String WX_PRIVATE_KEY = "wstl2016wstl2016wstl2016wstl2016";

    // 穿山甲
    public static final String CSJ_APP_ID = "5120395";// 应用id
    public static final String CSJ_CODE_ID = "887403978";// 代码位id

    // 本地环境
//    public static String BASE_URL = "http://172.16.0.41:8080/";


    //缓存数据保存根目录
    public static String JUGUO_CACHE_DIR;
    public static String CACHE_FILE = "/JuguoOfficeFamilyCache/";
    public static String NET_ERROR = "请连接您的网络";

    public static void getCachePath(Context mContext) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            JUGUO_CACHE_DIR = mContext.getExternalCacheDir().getAbsolutePath();
        } else {
            JUGUO_CACHE_DIR = mContext.getCacheDir().getPath();
        }
    }
}
