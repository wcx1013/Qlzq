package com.juguo.gushici.service;



import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.FeedBackBean;
import com.juguo.gushici.bean.VersionUpdataBean;
import com.juguo.gushici.dragger.bean.User;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.response.VersionUpdataResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ApiService {


    // 版本更新
    @POST("app-v/check")
    Observable<VersionUpdataResponse> versionUpdata(@Body VersionUpdataBean versionUpdataBean);

    // 登录获取token
    @POST("user/login")
    Observable<LoginResponse> login(@Body User user);

    // 获取用户账号信息
    @GET("user/me/")
    Observable<AccountInformationResponse> accountInformation();

    // 退出登录
    @GET("user/logout")
    Observable<BaseResponse> logOut();

    // 帮助反馈
    @POST("feedback/")
    Observable<BaseResponse> feedBack(@Body FeedBackBean feedBackBean);
}
