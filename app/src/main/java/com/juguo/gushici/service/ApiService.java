package com.juguo.gushici.service;



import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.bean.AddPayOrderBean;
import com.juguo.gushici.bean.ChangeStateBean;
import com.juguo.gushici.bean.FeedBackBean;
import com.juguo.gushici.bean.PlanListBean;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.bean.RegisterBean;
import com.juguo.gushici.bean.VersionUpdataBean;
import com.juguo.gushici.dragger.bean.User;
import com.juguo.gushici.param.AddWithRemovePlanParams;
import com.juguo.gushici.param.EditUserInfoParams;
import com.juguo.gushici.param.LearnPlanParams;
import com.juguo.gushici.param.PoetryListParams;
import com.juguo.gushici.param.RecitedListParams;
import com.juguo.gushici.param.SearchParams;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.AddPayOrderResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.response.MemberLevelResponse;
import com.juguo.gushici.response.QueryOrderResponse;
import com.juguo.gushici.response.VersionUpdataResponse;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @POST("user/register")
    Observable<LoginResponse> register(@Body RegisterBean registerBean);

    // 退出登录
    @GET("user/logout")
    Observable<BaseResponse> logOut();

    // 帮助反馈
    @POST("feedback/")
    Observable<BaseResponse> feedBack(@Body FeedBackBean feedBackBean);

    @POST("poem-ext/list")
    Observable<PoetryBean> requestSearch(@Body SearchParams searchParams);

    @POST("poem/recited/")
    Observable<BaseResponse> requestChangeState(@Body ChangeStateBean changeStateBean);

    @POST("poem-ext/plan-list")
    Observable<PoetryBean> requestPlanList(@Body LearnPlanParams learnPlanParams);

    /**
     * 跟搜索接口相同地址,不同请求参数
     * @param poetryListParams
     * @return
     */
    @POST("poem-ext/list")
    Observable<PoetryBean> requestPoetryList(@Body PoetryListParams poetryListParams);

    @POST("poem/plan/")
    Observable<BaseResponse> addWithRemovePlan(@Body AddWithRemovePlanParams addWithRemovePlanParams);

    @POST("poem/recited-list")
    Observable<PoetryBean> getRecitedList(@Body RecitedListParams recitedListParams);

    @POST("user/me/")
    Observable<PoetryBean> requestEditUserNickName(@Body EditUserInfoParams editUserInfoParams);

    @Multipart
    @POST("user/me/icon")
    Observable<PoetryBean> requestEditUserHead(@Part()MultipartBody.Part part);

    // 添加订单
    @POST("order/")
    Observable<AddPayOrderResponse> addPayOrder(@Body AddPayOrderBean addPayOrderBean);

    // 查询订单
    @GET("order/{id}")
    Observable<QueryOrderResponse> queryOrder(@Path("id") String id);

    // 获取会员价格列表
    @GET("member-level/list")
    Observable<MemberLevelResponse> memberLevel();
}
