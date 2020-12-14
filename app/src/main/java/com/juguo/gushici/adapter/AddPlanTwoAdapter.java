package com.juguo.gushici.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juguo.gushici.R;
import com.juguo.gushici.bean.AddPlanClassBean;
import com.juguo.gushici.bean.AddPlanTwoBean;

import java.util.List;

public class AddPlanTwoAdapter extends BaseQuickAdapter<AddPlanTwoBean, BaseViewHolder> {

    public AddPlanTwoAdapter() {
        super(R.layout.item_add_plan_two);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, AddPlanTwoBean mode) {

        TextView tvTitle = baseViewHolder.getView(R.id.tv_title);
        TextView tvContent = baseViewHolder.getView(R.id.tv_content);
        ImageView ivAlready = baseViewHolder.getView(R.id.iv_already_learn);
        ImageView ivMultipleChoose = baseViewHolder.getView(R.id.iv_choose);
        ImageView ivSingleChoose = baseViewHolder.getView(R.id.iv_single_choose);

        tvTitle.setText(mode.getPoetryBean().getName());
        tvContent.setText(mode.getPoetryBean().getStDesc());

        if (mode.isOpenMultipleChoose()) {

            ivSingleChoose.setVisibility(View.GONE);
            ivMultipleChoose.setVisibility(View.VISIBLE);
            ivMultipleChoose.setSelected(mode.isChoose());
        } else {
            ivSingleChoose.setVisibility(View.VISIBLE);
            ivMultipleChoose.setVisibility(View.GONE);
        }

        if (mode.getPoetryBean().isAlready()) {

            ivAlready.setVisibility(View.VISIBLE);
            ivAlready.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_already_learn));
            ivSingleChoose.setEnabled(false);
            mode.setChoose(true);
            ivSingleChoose.setSelected(mode.isChoose());
            //ivSingleChoose.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_add_plan_single_choose_pre));
            ivMultipleChoose.setEnabled(false);
        } else {
            ivAlready.setVisibility(View.GONE);
            ivSingleChoose.setEnabled(true);
            ivSingleChoose.setSelected(mode.isChoose());
            //mode.setChoose(false);
            //ivSingleChoose.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_add_plan_single_choose));
            ivMultipleChoose.setEnabled(true);
            ivMultipleChoose.setSelected(mode.isChoose());
        }

        baseViewHolder.addOnClickListener(R.id.iv_single_choose,R.id.iv_choose);
    }
}
