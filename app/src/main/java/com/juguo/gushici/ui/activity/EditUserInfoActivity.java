package com.juguo.gushici.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.base.BaseResponse;
import com.juguo.gushici.param.EditUserInfoParams;
import com.juguo.gushici.ui.activity.contract.CenterContract;
import com.juguo.gushici.ui.activity.contract.EditUserInfoContract;
import com.juguo.gushici.ui.activity.presenter.CenterPresenter;
import com.juguo.gushici.ui.activity.presenter.EditUserInfoPresenter;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.GifSizeFilter;
import com.juguo.gushici.utils.GlideLoader;
import com.juguo.gushici.utils.ListUtils;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.TitleBarUtils;
import com.juguo.gushici.utils.ToastUtils;
import com.juguo.gushici.utils.Util;
import com.juguo.gushici.view.XCRoundImageView;
import com.lcw.library.imagepicker.ImagePicker;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import java.io.File;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class EditUserInfoActivity extends BaseMvpActivity<EditUserInfoPresenter> implements EditUserInfoContract.View {

    private XCRoundImageView mIvHead;
    private LinearLayout mLlHeadLayout;
    private EditText mEditUsername;
    private int REQUEST_SELECT_IMAGES_CODE = 0;
    private MySharedPreferences mySharedPreferences;
    private List<String> mHeadImgList;

    @Override
    protected int getLayout() {
        return R.layout.activity_edit_userinfo;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mLlHeadLayout = findViewById(R.id.ll_head_layout);
        mIvHead = findViewById(R.id.iv_head);
        mEditUsername = findViewById(R.id.edit_username);

        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setMiddleTitleText("????????????");
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarUtils.setRightText("??????");
        titleBarUtils.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (titleBarUtils.getRightText().getText().toString().equals("??????")) {

                    titleBarUtils.setRightText("??????");
                    mEditUsername.setEnabled(true);
                    mLlHeadLayout.setEnabled(true);
                } else {
                    titleBarUtils.setRightText("??????");
                    mEditUsername.setEnabled(false);
                    mLlHeadLayout.setEnabled(false);
                }
            }
        });
        mEditUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    requestNickName();
                }
                return false;
            }
        });
        mLlHeadLayout.setOnClickListener(this);
        mySharedPreferences = new MySharedPreferences(this, "Shared");
        if (CommUtils.isLogin(this)) {
            String userIcon = (String) mySharedPreferences.getValue("userIcon", "");
            String userName = (String) mySharedPreferences.getValue("userName", "");

            // ????????????????????????icon
            Util.displayCircleCropImgView(this, mIvHead, userIcon, R.mipmap.ic_user_place);
            mEditUsername.setText(userName);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mLlHeadLayout) {
            openPic();
        }
    }

    @Override
    public void httpEditNickNameCallback(BaseResponse o) {

        dialogDismiss();
        if (o.isSuccess()) {
            ToastUtils.shortShowStr(this, "??????????????????");
        }
    }

    @Override
    public void httpEditUserHeadCallback(BaseResponse o) {

        dialogDismiss();
        if (o.isSuccess()) {
            Util.displayCircleCropImgView(this, mIvHead, new File(mHeadImgList.get(0)), R.mipmap.ic_user_place);
            ToastUtils.shortShowStr(this, "??????????????????");
        }
    }

    @Override
    public void httpError(String e) {

    }

    private void requestNickName() {

        dialogShow();
        EditUserInfoParams params = new EditUserInfoParams();
        EditUserInfoParams.EditUserInfoBean editUserInfoBean = new EditUserInfoParams.EditUserInfoBean();
        editUserInfoBean.setNickName(mEditUsername.getText().toString());
        params.setParam(editUserInfoBean);
        mPresenter.editUserNickname(params);
    }

    private void requestEditHead(String filePath){
        dialogShow();
        EditUserInfoParams params = new EditUserInfoParams();
        EditUserInfoParams.EditUserInfoBean editUserInfoBean = new EditUserInfoParams.EditUserInfoBean();
        params.setParam(editUserInfoBean);
        mPresenter.editUserHead(new File(filePath));
    }

    private void openPic() {

        /*Matisse.from(this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.dp_120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);*/
        /*Matisse.from(this)
                .choose(MimeType.ofAll(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.juguo.gushici.fileprovider"))
                .maxSelectable(9)
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.dp_120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                 //for glide-V3
                                            .imageEngine(new GlideEngine())
                // for glide-V4
                //.imageEngine(new Glide4Engine())
                .setOnSelectedListener(new OnSelectedListener() {
                    @Override
                    public void onSelected(
                            @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("onSelected", "onSelected: pathList=" + pathList);

                    }
                })
                .originalEnable(true)
                .maxOriginalSize(10)
                .setOnCheckedListener(new OnCheckedListener() {
                    @Override
                    public void onCheck(boolean isChecked) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                    }
                })
                .forResult(REQUEST_CODE_CHOOSE);*/
        ImagePicker.getInstance()
                .setTitle("??????")//????????????
                .showCamera(true)//??????????????????????????????
                .showImage(true)//????????????????????????
                .showVideo(true)//????????????????????????
                //.filterGif(false)//??????????????????gif??????
                .setSingleType(true)//????????????????????????????????????
                .setMaxCount(1)//??????????????????????????????(?????????1?????????)
                //.setImagePaths(mImageList)//??????????????????????????????????????????????????????????????????
                .setImageLoader(new GlideLoader())//??????????????????????????????
                .start(this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE???Intent?????????requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mHeadImgList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);

            if(!ListUtils.isEmpty(mHeadImgList)){
                requestEditHead(mHeadImgList.get(0));
            }
        }
    }

    //???????????????????????????????????????????????????????????????????????????????????????????????????????????????@NeedsPermission?????????????????????????????????
    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EditUserInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * ???????????????
     */
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        ToastUtils.shortShowStr(this, "??????????????????????????????????????????");
    }

}
