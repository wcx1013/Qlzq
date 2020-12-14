package com.juguo.gushici.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juguo.gushici.R;
import com.juguo.gushici.adapter.SearchAdapter;
import com.juguo.gushici.adapter.SearchRecoderAdapter;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.param.SearchParams;
import com.juguo.gushici.ui.activity.contract.SearchContract;
import com.juguo.gushici.ui.activity.presenter.SearchPresenter;
import com.juguo.gushici.utils.ListUtils;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.TitleBarUtils;
import com.juguo.gushici.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索
 */
public class SearchActivity extends BaseMvpActivity<SearchPresenter> implements SearchContract.View {

    public static String SEARCH_KEY = "search_key";

    private RecyclerView mRvRecoderGrid;
    private SearchRecoderAdapter mSearchRecoderAdapter;

    private RecyclerView mRvList;
    private SearchAdapter mSearchAdapter;

    private EditText mEditSearch;
    private TextView mTvSearch;
    private TextView mTvRecoderTitle;

    private MySharedPreferences mySharedPreferences;
    private String mSearchKey;

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mRvRecoderGrid = findViewById(R.id.rv_recoder_grid);
        mRvList = findViewById(R.id.rv_search_list);
        mEditSearch = findViewById(R.id.edit_search);
        mTvSearch = findViewById(R.id.tv_search);
        mTvRecoderTitle = findViewById(R.id.tv_recoder_title);

        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setMiddleTitleText(getString(R.string.search));
        titleBarUtils.setRightText(getString(R.string.clear));
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarUtils.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEditSearch.setText("");
            }
        });
        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    mSearchKey = mEditSearch.getText().toString();
                    requestSearch();
                }
                return false;
            }
        });

        mTvSearch.setOnClickListener(this);
        initAdapter();
    }

    private void initAdapter() {

        mySharedPreferences = new MySharedPreferences(this, MySharedPreferences.SEARCH_RECODER);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRvRecoderGrid.setLayoutManager(gridLayoutManager);
        mSearchRecoderAdapter = new SearchRecoderAdapter();

        getLocalSearchRecord();
        mRvRecoderGrid.setAdapter(mSearchRecoderAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvList.setLayoutManager(linearLayoutManager);
        mSearchAdapter = new SearchAdapter();
        mSearchAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.view_search_empty_layout, null));
        mSearchAdapter.getEmptyView().setVisibility(View.GONE);
        mRvList.setAdapter(mSearchAdapter);

        mSearchRecoderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                mSearchKey = mSearchRecoderAdapter.getData().get(i);
                requestSearch();
            }
        });
        mSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.POETRY_DATA, mSearchAdapter.getData().get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_search:
                mSearchKey = mEditSearch.getText().toString();
                requestSearch();
                break;
        }
    }

    @Override
    public void httpCallback(PoetryBean poetryBean) {

        dialogDismiss();
        if (!ListUtils.isEmpty(poetryBean.getList())) {

            List<PoetryBean.PoetryInfo> poetryInfoList = poetryBean.getList();
            mSearchAdapter.setNewData(poetryInfoList);
            mSearchAdapter.getEmptyView().setVisibility(View.GONE);
        } else {
            mSearchAdapter.setNewData(new ArrayList<>());
            mSearchAdapter.getEmptyView().setVisibility(View.VISIBLE);
        }

        //移除相同搜索记录
        for (int i = mSearchRecoderAdapter.getData().size() - 1; i >= 0; i--) {

            String currentSearch = mSearchKey;
            String localSearch = mSearchRecoderAdapter.getData().get(i).toString();
            if (currentSearch.equals(localSearch)) {
                mSearchRecoderAdapter.remove(i);
            }
        }

        List<String> newList = new ArrayList<>();
        newList.add(mSearchKey);
        newList.addAll(mSearchRecoderAdapter.getData());

        String searchJson = "";
        for (int i = 0; i < newList.size(); i++) {
            if (TextUtils.isEmpty(searchJson)) {
                searchJson = newList.get(i);
            } else {
                searchJson = searchJson + "," + newList.get(i);
            }

        }
        Log.d("tag", "searchJson==" + searchJson);
        mySharedPreferences.putValue(SEARCH_KEY, searchJson);
        mRvRecoderGrid.setVisibility(View.VISIBLE);
        mSearchRecoderAdapter.setNewData(newList);
    }

    @Override
    public void httpError(String e) {

    }

    /**
     * 搜索结果
     */
    private void requestSearch() {
        if (TextUtils.isEmpty(mSearchKey)) {
            ToastUtils.shortShowStr(this, "请输入搜索名称/作者");
            return;
        }
        SearchParams searchParams = new SearchParams();
        SearchParams.SearchInfo searchInfo = new SearchParams.SearchInfo();
        searchInfo.setSearchKey(mSearchKey);
        searchParams.setParam(searchInfo);
        dialogShow();
        mPresenter.requestSearch(searchParams);
    }

    /**
     * 获取本地存储的搜索历史
     */
    private void getLocalSearchRecord() {

        String searchRecord = (String) mySharedPreferences.getValue(SEARCH_KEY, "");
        Log.d("tag", "searchJson=get=" + searchRecord);
        if (!TextUtils.isEmpty(searchRecord)) {

            List<String> recordList = new ArrayList<>();
            searchRecord = searchRecord.replace("\"", "");
            /*searchRecord = searchRecord.replace("[", "");
            searchRecord = searchRecord.replace("]", "");*/
            Log.d("tag", "searchJson=get==" + searchRecord);

            String[] local = searchRecord.split(",");
            for (int i = 0; i < local.length; i++) {
                recordList.add(local[i]);
            }

            if (!ListUtils.isEmpty(recordList)) {

                mTvRecoderTitle.setVisibility(View.VISIBLE);
                mRvRecoderGrid.setVisibility(View.VISIBLE);
                mSearchRecoderAdapter.setNewData(recordList);
            } else {
                mTvRecoderTitle.setVisibility(View.GONE);
                mRvRecoderGrid.setVisibility(View.GONE);
            }
        } else {
            mTvRecoderTitle.setVisibility(View.GONE);
            mRvRecoderGrid.setVisibility(View.GONE);
        }
    }
}
