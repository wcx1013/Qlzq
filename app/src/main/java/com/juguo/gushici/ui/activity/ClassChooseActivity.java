package com.juguo.gushici.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juguo.gushici.R;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.ui.activity.contract.CenterContract;
import com.juguo.gushici.ui.activity.presenter.CenterPresenter;
import com.juguo.gushici.utils.CommUtils;

/**
 * 年级选择
 */
public class ClassChooseActivity extends BaseMvpActivity<CenterPresenter> implements CenterContract.View {

    public static String TITLE = "title";
    public static String GRADE = "grade";
    public static String IFCLASS = "IFCLASS";

    private TextView mTvTitle;

    private ImageView mIvTextBook;//课内
    private ImageView mIvEc;//课外

    private FrameLayout mFlBack;
    private LinearLayout mLlRoot;
    private LinearLayout mLlSearch;

    private int mGrade = 1;//年级
    private int mIfClass = 1;//是否是课内。1课内0课外

    @Override
    protected int getLayout() {
        return R.layout.activity_class_choose;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mIvEc = findViewById(R.id.iv_class_ec);
        mIvTextBook = findViewById(R.id.iv_text_book);
        mFlBack = findViewById(R.id.fl_back);
        mLlRoot = findViewById(R.id.ll_root);
        mLlSearch = findViewById(R.id.ll_search);
        mTvTitle = findViewById(R.id.tv_title);

        String title = getIntent().getStringExtra(TITLE);
        mGrade = getIntent().getIntExtra(GRADE, 1);

        mTvTitle.setText(title);
        CommUtils.setImmerseLayout(mLlRoot, this);
        mFlBack.setOnClickListener(this);
        mIvTextBook.setOnClickListener(this);
        mIvEc.setOnClickListener(this);
        mLlSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.ll_search:

                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_text_book:

                mIfClass = 1;
                intent = new Intent(this, ClassChooseListActivity.class);
                intent.putExtra(IFCLASS, mIfClass);
                intent.putExtra(GRADE, mGrade);
                startActivity(intent);
                break;
            case R.id.iv_class_ec:

                mIfClass = 0;
                intent = new Intent(this, ClassChooseListActivity.class);
                intent.putExtra(IFCLASS, mIfClass);
                intent.putExtra(GRADE, mGrade);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void httpCallback(Object o) {

    }

    @Override
    public void httpError(String e) {

    }
}
