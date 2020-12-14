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
        titleBarUtils.setMiddleTitleText("编辑资料");
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarUtils.setRightText("编辑");
        titleBarUtils.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (titleBarUtils.getRightText().getText().toString().equals("编辑")) {

                    titleBarUtils.setRightText("取消");
                    mEditUsername.setEnabled(true);
                    mLlHeadLayout.setEnabled(true);
                } else {
                    titleBarUtils.setRightText("编辑");
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

            // 设置用户名和用户icon
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
            ToastUtils.shortShowStr(this, "修改昵称成功");
        }
    }

    @Override
    public void httpEditUserHeadCallback(BaseResponse o) {

        dialogDismiss();
        if (o.isSuccess()) {
            Util.displayCircleCropImgView(this, mIvHead, new File(mHeadImgList.get(0)), R.mipmap.ic_user_place);
            ToastUtils.shortShowStr(this, "修改头像成功");
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
                .setTitle("相册")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                //.filterGif(false)//设置是否过滤gif图片
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                //.setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
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

    //重写该方法之后，当弹出授权对话框时，我们点击允许授权成功时，会自动执行注解@NeedsPermission所标注的方法里面的逻辑
    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EditUserInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 被用户拒绝
     */
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        ToastUtils.shortShowStr(this, "权限未授予，部分功能无法使用");
    }

}
