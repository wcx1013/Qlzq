package com.juguo.gushici.adapter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juguo.gushici.R;
import com.juguo.gushici.bean.LearnReportBean;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.utils.ListUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class LearnReportAdapter extends BaseQuickAdapter<LearnReportBean, BaseViewHolder> {

    public LearnReportAdapter() {
        super(R.layout.item_learn_report);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, LearnReportBean mode) {

        TextView tvDate = baseViewHolder.getView(R.id.tv_date);
        View viewLine = baseViewHolder.getView(R.id.view_line);
        LinearLayout lLBookName = baseViewHolder.getView(R.id.ll_book_name);

        tvDate.setText(mode.getDate());

        if (!ListUtils.isEmpty(mode.getBookLst())) {

            //if (baseViewHolder.getLayoutPosition() < mData.size() - 1) {

                int lineHeight = (int) ((mode.getBookLst().size() + 0.5) * mContext.getResources().getDimensionPixelOffset(R.dimen.dp_20));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelOffset(R.dimen.dp_1), lineHeight);
                viewLine.setLayoutParams(layoutParams);
            //}
        }
        addBookNameText(lLBookName, mode.getBookLst());
    }

    private void addBookNameText(LinearLayout layout, List<String> mode) {
        layout.removeAllViews();
        for (int i = 0; i < mode.size(); i++) {
            TextView tvBookName = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            int marginsTop = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_10);
            if (mode.size() == 1) {
                marginsTop = 0;
            }
            layoutParams.setMargins(0, marginsTop, 0, 0);
            tvBookName.setLayoutParams(layoutParams);
            tvBookName.setText(mode.get(i));
            tvBookName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            tvBookName.setTextColor(mContext.getResources().getColor(R.color.color_111111));
            layout.addView(tvBookName);
        }
    }
}
