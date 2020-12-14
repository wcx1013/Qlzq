package com.juguo.gushici.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juguo.gushici.R;
import com.juguo.gushici.bean.AddPlanClassBean;

import java.util.List;

/**
 * 添加计划,左侧1级分类
 */
public class AddPlanClassAdapter extends BaseQuickAdapter<AddPlanClassBean, BaseViewHolder> {

    public AddPlanClassAdapter() {
        super(R.layout.item_add_plan_class);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, AddPlanClassBean mode) {

        TextView tvClassTitle = baseViewHolder.getView(R.id.tv_class_title);
        tvClassTitle.setText(mode.getTitle());

        tvClassTitle.setSelected(mode.isChoose());
        if(mode.isChoose()){

            tvClassTitle.setTextColor(mContext.getResources().getColor(R.color.white));
            tvClassTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tvClassTitle.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_e9533a));
        }else {

            tvClassTitle.setTextColor(mContext.getResources().getColor(R.color.color_575757));
            tvClassTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tvClassTitle.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_white));
        }

        /*tvClassTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (AddPlanClassBean addOne :mData) {
                    addOne.setChoose(false);
                }
                mode.setChoose(true);
            }
        });*/
    }
}
