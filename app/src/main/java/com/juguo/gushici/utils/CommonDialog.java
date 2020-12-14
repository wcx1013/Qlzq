package com.juguo.gushici.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.juguo.gushici.R;


public class CommonDialog extends Dialog implements ICommonDialog {

    public CommonDialog(@NonNull Activity activity) {
        //super(activity);
        //setOwnerActivity(activity);
        this(activity, R.style.dialogBase);
    }

    public CommonDialog(@NonNull Activity activity, int themeResId) {
        super(activity, R.style.dialogBase);
        setOwnerActivity(activity);
    }


    private View mContentView;

    @Override
    public View getContentView()
    {
        return mContentView;
    }

    @Override
    public void setContentView(int layoutId)
    {
        FrameLayout tempLayout = new FrameLayout(getContext());
        View view = LayoutInflater.from(getContext()).inflate(layoutId, tempLayout, false);
        tempLayout.removeView(view);

        setDialogView(view, view.getLayoutParams());
    }

    @Override
    public void setContentView(View view)
    {
        setDialogView(view, view.getLayoutParams());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params)
    {
        setDialogView(view, params);
    }


    @Override
    public int getDefaultPadding()
    {
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int value = (int) (screenWidth * 0.1f);
        return value;
    }

    @Override
    public CommonDialog paddingLeft(int left)
    {
        View view = getWindow().getDecorView();
        view.setPadding(left, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        return this;
    }

    @Override
    public CommonDialog paddingTop(int top)
    {
        View view = getWindow().getDecorView();
        view.setPadding(view.getPaddingLeft(), top, view.getPaddingRight(), view.getPaddingBottom());
        return this;
    }

    @Override
    public CommonDialog paddingRight(int right)
    {
        View view = getWindow().getDecorView();
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), right, view.getPaddingBottom());
        return this;
    }

    @Override
    public CommonDialog paddingBottom(int bottom)
    {
        View view = getWindow().getDecorView();
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), bottom);
        return this;
    }

    @Override
    public CommonDialog paddings(int paddings)
    {
        View view = getWindow().getDecorView();
        view.setPadding(paddings, paddings, paddings, paddings);
        return this;
    }

    @Override
    public CommonDialog setGrativity(int gravity)
    {
        getWindow().setGravity(gravity);
        return this;
    }

    @Override
    public CommonDialog setAnimations(int resId)
    {
        getWindow().setWindowAnimations(resId);
        return this;
    }

    @Override
    public void showTop()
    {
        setGrativity(Gravity.TOP);
        setAnimations(R.style.dialog_slidingTopTop);
        show();
    }

    @Override
    public void showCenter()
    {
        setGrativity(Gravity.CENTER);
        show();
    }

    @Override
    public void showBottom()
    {
        setGrativity(Gravity.BOTTOM);
        setAnimations(R.style.dialog_slidingBottomBottom);
        show();
    }

    //---------- FIDialog implements end----------

    private CommonDialog setDialogView(View view, ViewGroup.LayoutParams params)
    {
        mContentView = view;
        if (params == null)
        {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        super.setContentView(mContentView, params);

        paddings(getDefaultPadding());
        synchronizeWidth();
        return this;
    }

    /**
     * 把contentView的宽度同步到window
     */
    private void synchronizeWidth()
    {
        if (mContentView == null)
        {
            return;
        }
        ViewGroup.LayoutParams p = mContentView.getLayoutParams();
        if (p == null)
        {
            return;
        }

        WindowManager.LayoutParams wParams = getWindow().getAttributes();
        if (wParams.width != p.width)
        {
            wParams.width = p.width;
            getWindow().setAttributes(wParams);
        }
    }

    /**
     * 把contentView的高度同步到window
     */
    private void synchronizeHeight()
    {
        if (mContentView == null)
        {
            return;
        }
        ViewGroup.LayoutParams p = mContentView.getLayoutParams();
        if (p == null)
        {
            return;
        }

        WindowManager.LayoutParams wParams = getWindow().getAttributes();
        if (wParams.height != p.height)
        {
            wParams.height = p.height;
            getWindow().setAttributes(wParams);
        }
    }


    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    public void show()
    {
        try
        {
            if (getOwnerActivity() != null && !getOwnerActivity().isFinishing())
            {
                super.show();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss()
    {
        try
        {
            super.dismiss();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void setBackgroundDrawable(View view, Drawable drawable)
    {
        if (view == null)
        {
            return;
        }
        int paddingLeft = view.getPaddingLeft();
        int paddingTop = view.getPaddingTop();
        int paddingRight = view.getPaddingRight();
        int paddingBottom = view.getPaddingBottom();
        view.setBackgroundDrawable(drawable);
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public void setWindowLayoutAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        //getWindow().setAttributes(lp);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public CommonDialog setFullScreen()
    {
        paddings(0);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    @Override
    public CommonDialog setWidth(int width)
    {
        ViewGroup.LayoutParams params = mContentView.getLayoutParams();
        params.width = width;
        mContentView.setLayoutParams(params);

        synchronizeWidth();
        return this;
    }

    /**
     * 设置宽度
     *
     * @param height
     * @return
     */
    public CommonDialog setHeight(int height)
    {
        ViewGroup.LayoutParams params = mContentView.getLayoutParams();
        params.height = height;
        mContentView.setLayoutParams(params);

        synchronizeHeight();
        return this;
    }
}
