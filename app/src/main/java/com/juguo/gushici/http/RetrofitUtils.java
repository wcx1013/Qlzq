package com.juguo.gushici.http;

import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.juguo.gushici.http.fastjson.FastJsonConverterFactory;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.Util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


public class RetrofitUtils {
    private static Retrofit mRetrofit;
    private static Retrofit getRetrofit() {
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(60, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(60, TimeUnit.SECONDS);//写操作超时时间
        builder.readTimeout(60, TimeUnit.SECONDS);//读操作超时时间

        // 添加公共参数拦截器
        if (!TextUtils.isEmpty(Util.authorization())) {
            HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
                    .addHeaderParams("Authorization", Util.authorization())
                    .build();
            builder.addInterceptor(commonInterceptor);
        }else {
            HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
                    .build();
            builder.addInterceptor(commonInterceptor);
        }

//        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
//        }

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(FastJsonConverterFactory.create())//重点是这句话
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return mRetrofit;
    }

    public static synchronized Retrofit getInstance() {
        //在基础URL不变情况下可以使用单例模式
//        if (mRetrofit == null) {
            try {
                mRetrofit = getRetrofit();
            } catch (Exception e) {
                e.toString();
            }
//        }
        return mRetrofit;
    }

    public static synchronized Retrofit changeUrl() {
        try {
            mRetrofit = getRetrofit();
        } catch (Exception e) {
            //当基础url格式错误时会报错
            return null;
        }
        return mRetrofit;
    }
}
