package com.juguo.gushici.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juguo.gushici.R;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.utils.Util;
import com.juguo.gushici.view.XCRoundImageView;

/**
 * 搜索列表
 */
public class SearchAdapter extends BaseQuickAdapter<PoetryBean.PoetryInfo, BaseViewHolder> {

    public SearchAdapter() {
        super(R.layout.item_search);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, PoetryBean.PoetryInfo mode) {

        View dividerLine = baseViewHolder.getView(R.id.view_line);
        //末尾分割线隐藏
        if (baseViewHolder.getLayoutPosition() == getData().size() - 1) {
            dividerLine.setVisibility(View.INVISIBLE);
        } else {
            dividerLine.setVisibility(View.VISIBLE);
        }

        TextView tvTitle=baseViewHolder.getView(R.id.tv_title);
        TextView tvContent=baseViewHolder.getView(R.id.tv_content);
        XCRoundImageView ivCover=baseViewHolder.getView(R.id.iv_cover);

        tvTitle.setText(mode.getName());
        tvContent.setText(mode.getStDesc());
        Util.displayCircleCropImgView(mContext, ivCover, mode.getAuthorImgUrl(), R.mipmap.ic_user_place);
    }
}
