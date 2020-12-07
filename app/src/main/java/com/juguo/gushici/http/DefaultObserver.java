package com.juguo.gushici.http;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.juguo.gushici.R;
import com.juguo.gushici.ui.activity.LoginActivity;
import com.juguo.gushici.ui.activity.SplashActivity;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public abstract class DefaultObserver<T> extends BaseObserver<T> {

    public AlertDialog alertDialog;
    private Context mContext;

    public DefaultObserver(Context context) {
        mContext = context;
    }

    public DefaultObserver(Fragment fragment) {
        mContext = fragment.getActivity();
    }

    //等待上传的加载框
    public void dialogShow() {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.dialog_common, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.loading_dialog_style).setView(convertView);
        alertDialog = dialog.create();
        alertDialog.show();
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (!d.isDisposed()) {
            if (!(mContext instanceof LoginActivity) && !(mContext instanceof SplashActivity)) {
                dialogShow();
            }
        }
    }

    @Override
    public void onComplete() {
        if (!(mContext instanceof LoginActivity) && !(mContext instanceof SplashActivity)) {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        super.onError(e);
        if (!(mContext instanceof LoginActivity)&&!(mContext instanceof SplashActivity)) {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        }
    }

}
