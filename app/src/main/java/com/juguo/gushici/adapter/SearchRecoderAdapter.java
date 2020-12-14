package com.juguo.gushici.adapter;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juguo.gushici.R;

/**
 * 搜索历史
 */
public class SearchRecoderAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchRecoderAdapter() {
        super(R.layout.item_search_recoder);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String mode) {

        TextView tvContent=baseViewHolder.getView(R.id.tv_content);
        tvContent.setText(mode);
    }
}
