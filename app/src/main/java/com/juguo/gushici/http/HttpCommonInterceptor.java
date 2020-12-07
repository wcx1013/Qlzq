package com.juguo.gushici.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.juguo.gushici.MyApplication;
import com.juguo.gushici.dragger.bean.User;
import com.juguo.gushici.dragger.bean.UserInfo;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.MySharedPreferences;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/*
 * 拦截器
 *
 * 向请求头里添加公共参数
 */
public class HttpCommonInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Map<String, String> mHeaderParamsMap = new HashMap<>();
    private String newToken;
    private Request.Builder requestBuilder;

    public HttpCommonInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        // 新的请求
        requestBuilder = oldRequest.newBuilder();
        requestBuilder.method(oldRequest.method(), oldRequest.body());

        //添加公共参数,添加到header中
        if (mHeaderParamsMap.size() > 0) {
            for (Map.Entry<String, String> params : mHeaderParamsMap.entrySet()) {
                requestBuilder.header(params.getKey(), params.getValue());
            }
        }

        Request newRequest = requestBuilder.build();
        Response proceed = chain.proceed(newRequest);

        ResponseBody responseBody = proceed.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        String bodyString = buffer.clone().readString(charset);

        MySharedPreferences mySharedPreferences = new MySharedPreferences(MyApplication.getApp(), "Shared");
        //判断token过期
        if (!TextUtils.isEmpty(bodyString) && (bodyString.contains("登录信息失效") || bodyString.contains("请先登录"))) {
            // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
            String uuid = (String) mySharedPreferences.getValue("uuid", "");
            boolean isLogin = (boolean) mySharedPreferences.getValue("isLogin", false);
            String loginType = (String) mySharedPreferences.getValue("loginType", "");
            String userId = (String) mySharedPreferences.getValue("userId", "");

            User user = new User();
            UserInfo userInfo = new UserInfo();
            if (isLogin) {
                if (Wechat.NAME.equals(loginType)) {
                    userInfo.setType(3);
                    userInfo.setUnionInfo(userId);
                } else if (QQ.NAME.equals(loginType)) {
                    userInfo.setType(4);
                    userInfo.setUnionInfo(userId);
                }
            } else {
                userInfo.setType(2);
                userInfo.setUnionInfo(uuid);
            }
            userInfo.setAppId(Constants.WX_APP_ID);
            user.setParam(userInfo);

            OkHttpClient okHttpClient = new OkHttpClient();//创建OkHttpClient


            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(user));//构建请求体

            Request request = new Request.Builder().url(Constants.BASE_URL + "user/login").post(requestBody).build();//发送请求

            Call call = okHttpClient.newCall(request);//创建回调

            try {

                Response response = call.execute();

                LoginResponse loginResponse = JSON.parseObject(response.body().string(), LoginResponse.class);

                if (loginResponse != null) {
                    newToken = loginResponse.getResult();
                }

                mySharedPreferences.putValue("token", newToken);

                // 新的请求
                requestBuilder = oldRequest.newBuilder();
                requestBuilder.method(oldRequest.method(), oldRequest.body());

                //添加公共参数,添加到header中
                if (mHeaderParamsMap.size() > 0) {
                    requestBuilder.header("Authorization", newToken);
//                    for (Map.Entry<String, String> params : mHeaderParamsMap.entrySet()) {
//                        requestBuilder.header(params.getKey(), params.getValue());
//                    }
                }
//                else if (mHeaderParamsMap.size() > 0) {
//                    for (Map.Entry<String, String> params : mHeaderParamsMap.entrySet()) {
//                        if ("Authorization".equals(params.getKey())) {
//                            requestBuilder.header("Authorization", newToken);
//                        } else {
//                            requestBuilder.header(params.getKey(), params.getValue());
//                        }
//                    }
//                }

                Request newRequest1 = requestBuilder.build();
                return chain.proceed(newRequest1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            return proceed;
        }
        return null;
    }

    public static class Builder {
        HttpCommonInterceptor mHttpCommonInterceptor;

        public Builder() {
            mHttpCommonInterceptor = new HttpCommonInterceptor();
        }

        public Builder addHeaderParams(String key, String value) {
            mHttpCommonInterceptor.mHeaderParamsMap.put(key, value);
            return this;
        }

        public Builder addHeaderParams(String key, int value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, float value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, long value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, double value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public HttpCommonInterceptor build() {
            return mHttpCommonInterceptor;
        }

    }
}
