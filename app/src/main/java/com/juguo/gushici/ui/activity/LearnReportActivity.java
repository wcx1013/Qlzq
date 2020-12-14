package com.juguo.gushici.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juguo.gushici.R;
import com.juguo.gushici.adapter.LearnReportAdapter;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.bean.LearnReportBean;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.dragger.bean.User;
import com.juguo.gushici.param.RecitedListParams;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.LoginResponse;
import com.juguo.gushici.ui.activity.contract.HomeContract;
import com.juguo.gushici.ui.activity.contract.LearnReportContract;
import com.juguo.gushici.ui.activity.presenter.HomePresenter;
import com.juguo.gushici.ui.activity.presenter.LearnReportPresenter;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.DateUtil;
import com.juguo.gushici.utils.ListUtils;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.TitleBarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 学习报告
 */
public class LearnReportActivity extends BaseMvpActivity<LearnReportPresenter> implements LearnReportContract.View {

    private TextView mTvLearnReport;
    private TextView mTvLearnReportCount;
    private TextView mTvLookAll;

    private RecyclerView mRvList;
    private LearnReportAdapter mLearnReportAdapter;

    private MySharedPreferences mMySharedPreferences;

    @Override
    protected int getLayout() {
        return R.layout.activity_learn_report;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mRvList = findViewById(R.id.rv_list);
        mTvLearnReport = findViewById(R.id.tv_learn_report_username);
        mTvLearnReportCount = findViewById(R.id.tv_learn_report_count);
        mTvLookAll = findViewById(R.id.tv_look_all);

        TitleBarUtils titleBarUtils = new TitleBarUtils(this);
        titleBarUtils.setLeftImageRes(R.mipmap.ic_arrow_left_black);
        titleBarUtils.setMiddleTitleText(getString(R.string.learn_report));
        titleBarUtils.setLeftImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initAdapter();


        mTvLookAll.setOnClickListener(this);
        requestLearnReport();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_look_all:

                Intent intent = new Intent(this, AddPlanActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initAdapter() {

        mLearnReportAdapter = new LearnReportAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvList.setLayoutManager(linearLayoutManager);
        mRvList.setAdapter(mLearnReportAdapter);

        mLearnReportAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });
    }


    @Override
    public void httpCallback(PoetryBean o) {

        dialogDismiss();
        int num = 0;

        if (!ListUtils.isEmpty(o.getList())) {

            num = o.getList().size();
            List<LearnReportBean> learnReportBeanList = new ArrayList<>();
            //使用Set去重
            Set<String> dateSet = new HashSet<>();
            List<String> dateList = new ArrayList<>();
            //利用Set去重
            for (int i = 0; i < o.getList().size(); i++) {

                dateSet.add(o.getList().get(i).getRecitedTime().split(" ")[0]);
            }
            //转换进list
            for (String date : dateSet) {
                dateList.add(date);
            }
            Collections.sort(dateList, new Comparator<String>() {
                /**
                 *
                 * @param lhs
                 * @param rhs
                 * @return
                 */
                @Override
                public int compare(String lhs, String rhs) {
                    Date date1 = DateUtil.stringToDate(lhs, "yyyy-MM-dd");
                    Date date2 = DateUtil.stringToDate(rhs, "yyyy-MM-dd");
                    // 对日期字段进行升序，如果欲降序可采用after方法
                    if (date1.before(date2)) {
                        return 1;
                    }
                    return -1;
                }
            });

            //getRecitedTime()==2020-12-10 17:49:02
            for (int i = 0; i < dateList.size(); i++) {

                String recitedTimeI = dateList.get(i);
                List<String> bookList = new ArrayList<>();

                for (int j = 0; j < o.getList().size(); j++) {

                    String recitedTimeJ = o.getList().get(j).getRecitedTime().split(" ")[0];

                    if (recitedTimeI.equals(recitedTimeJ)) {

                        bookList.add(o.getList().get(j).getName());
                    }
                }

                LearnReportBean learnReportBean = new LearnReportBean();
                learnReportBean.setBookLst(bookList);
                learnReportBean.setDate(recitedTimeI);
                learnReportBeanList.add(learnReportBean);
            }
            mLearnReportAdapter.setNewData(learnReportBeanList);
        } else {
            mLearnReportAdapter.setNewData(new ArrayList<>());
            num = 0;
        }

        String userName = "";
        if (CommUtils.isLogin(this)) {

            mMySharedPreferences = new MySharedPreferences(this, "Shared");
            userName = (String) mMySharedPreferences.getValue("userName", "");
        }

        mTvLearnReport.setText("恭喜" + userName);
        mTvLearnReportCount.setText(num + "首");
    }

    @Override
    public void httpError(String e) {

    }

    private void requestLearnReport() {
        dialogShow();
        RecitedListParams recitedListParams = new RecitedListParams();
        recitedListParams.setParam(new RecitedListParams.RecitedBean());
        mPresenter.getRecitedList(recitedListParams);
    }
}


