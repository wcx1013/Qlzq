package com.juguo.gushici.adapter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juguo.gushici.R;
import com.juguo.gushici.bean.AddPlanTwoBean;

import java.util.List;

public class ClassChooseListAdapter extends BaseQuickAdapter<AddPlanTwoBean, BaseViewHolder> {

    public ClassChooseListAdapter() {
        super(R.layout.item_class_choose);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, AddPlanTwoBean addPlanTwoBean) {

        TextView tvTitle = baseViewHolder.getView(R.id.tv_title);
        TextView tvAuthor = baseViewHolder.getView(R.id.tv_author);
        TextView tvNum = baseViewHolder.getView(R.id.tv_user_num);
        ImageView ivAlreadyLearn = baseViewHolder.getView(R.id.iv_already_learn);
        ImageView ivCoverLayout = baseViewHolder.getView(R.id.iv_cover_layout);

        tvTitle.setText(addPlanTwoBean.getPoetryBean().getName());
        tvAuthor.setText(addPlanTwoBean.getPoetryBean().getAuthorName());
        tvNum.setText(addPlanTwoBean.getPoetryBean().getRecitedNum()+"");
        if (addPlanTwoBean.getPoetryBean().isAlready()) {

            ivAlreadyLearn.setVisibility(View.VISIBLE);
            baseViewHolder.addOnClickListener(R.id.iv_cover_layout);
        } else {

            ivAlreadyLearn.setVisibility(View.GONE);
        }

    }
}
