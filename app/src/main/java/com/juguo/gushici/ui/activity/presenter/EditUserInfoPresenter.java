package com.juguo.gushici.ui.activity.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.util.Log;

import com.juguo.gushici.base.BaseMvpPresenter;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.http.DefaultObserver;
import com.juguo.gushici.http.RetrofitUtils;
import com.juguo.gushici.http.RxSchedulers;
import com.juguo.gushici.param.EditUserInfoParams;
import com.juguo.gushici.service.ApiService;
import com.juguo.gushici.ui.activity.contract.CenterContract;
import com.juguo.gushici.ui.activity.contract.EditUserInfoContract;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

public class EditUserInfoPresenter extends BaseMvpPresenter<EditUserInfoContract.View> implements EditUserInfoContract.Presenter {

    @Inject
    public EditUserInfoPresenter() {

    }

    @Override
    public void editUserNickname(EditUserInfoParams editUserInfoParams) {

        RetrofitUtils.getInstance().create(ApiService.class)
                .requestEditUserNickName(editUserInfoParams)
                .compose(RxSchedulers.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(new DefaultObserver<BaseResponse>((Context) mView) {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mView.httpEditNickNameCallback(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.httpError(e.toString());
                    }
                });
    }

    @Override
    public void editUserHead(File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        /*MultipartBody.Part part = MultipartBody.Part.createFormData
                ("file", file.getName(), requestBody);*/
        //builder.addFormDataPart("file", file.getName());//传入服务器需要的key，和相应value值
        Log.d("Tag",file.getName()+"---"+file.getPath());
        builder.addFormDataPart("file",file.getName(),requestBody); //添加图片数据，body创建的请求体

        RetrofitUtils.getInstance().create(ApiService.class)
                .requestEditUserHead(builder.build().part(0))
                .compose(RxSchedulers.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(new DefaultObserver<BaseResponse>((Context) mView) {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mView.httpEditUserHeadCallback(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.httpError(e.toString());
                    }
                });
    }
}
