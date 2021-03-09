package com.juguo.gushici.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.juguo.gushici.MyApplication;
import com.juguo.gushici.R;
import com.juguo.gushici.adapter.VerticalViewPagerAdapter;
import com.juguo.gushici.base.BaseFragment;
import com.juguo.gushici.base.BaseMvpActivity;
import com.juguo.gushici.bean.AddPayOrderBean;
import com.juguo.gushici.bean.EventBusMessage;
import com.juguo.gushici.bean.NewWXSignOrderBean;
import com.juguo.gushici.bean.PoetryBean;
import com.juguo.gushici.bean.SignOrderBean;
import com.juguo.gushici.bean.WxPayMessageBean;
import com.juguo.gushici.response.AccountInformationResponse;
import com.juguo.gushici.response.AddPayOrderResponse;
import com.juguo.gushici.response.MemberLevelResponse;
import com.juguo.gushici.response.QueryOrderResponse;
import com.juguo.gushici.ui.activity.contract.DetailContract;
import com.juguo.gushici.ui.activity.presenter.DetailPresenter;
import com.juguo.gushici.ui.fragment.DetailFragment;
import com.juguo.gushici.utils.CommUtils;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.MySharedPreferences;
import com.juguo.gushici.utils.PayResult;
import com.juguo.gushici.utils.TToast;
import com.juguo.gushici.utils.ToastUtils;
import com.juguo.gushici.utils.Util;
import com.juguo.gushici.utils.WeChatField;
import com.juguo.gushici.view.TTAdManagerHolder;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;


/**
 * 诗词详情页
 */
public class DetailActivity extends BaseMvpActivity<DetailPresenter> implements DetailContract.View {

    public static String POETRY_DATA = "poetry_data";
    public static String POETRY_INDEX = "poetry_index";

    private PoetryBean mPoetryBean;
    private int mRecited = 0;
    private MySharedPreferences mySharedPreferences;
    // 支付方式
    private int zfType = 1;
    // 会员类型
    private String hyType = "永久会员";
    private String prodCode;
    private String orderId;
    private String yjCode;
    private int querySum = 3;
    private final int SDK_PAY_FLAG = 1;
    private String mPrice;
    private String mPriceYj;
    private String mPriceNd;
    private int mOriginalPriceYj;//永久原价
    private int mOriginalPriceNd;//年度原价
    private String resId;
    private String ndCode;
    private Context mContext;

    private String TAG = this.getClass().getSimpleName();
    private TTAdNative mTTAdNative;
    private TTFullScreenVideoAd mttFullVideoAd;
    private boolean mIsExpress = false; //是否请求模板广告
    private boolean mIsLoaded = false; //视频是否加载完成
    private boolean mHasShowDownloadActive = false;
    private final int AD_LOAD_COMPLETE = 0;
    private int mIndex;

    private FrameLayout mFlRoot;
    private FrameLayout mFlPreviousFragment;
    private FrameLayout mFlNextFragment;
    private VerticalViewPager mViewPager;
    private String aliPay;
    private String wxPay;
    private String isOpenMember;
    private boolean isShowAd;


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    switch (payResult.getResultStatus()) {
                        case "9000":
                            MobclickAgent.onEvent(mContext, "payment_success", "付款成功");
                            mPresenter.queryOrder(orderId);
                            break;
                        case "6004":
                        case "8000":
                            ToastUtils.shortShowStr(mContext, "正在处理中");
                            break;
                        case "4000":
                            MobclickAgent.onEvent(mContext, "payment_fali", "付款失败");
                            ToastUtils.shortShowStr(mContext, "订单支付失败");
                            break;
                        case "5000":
                            ToastUtils.shortShowStr(mContext, "重复请求");
                            break;
                        case "6001":
                            ToastUtils.shortShowStr(mContext, "已取消支付");
                            break;
                        case "6002":
                            ToastUtils.shortShowStr(mContext, "网络连接出错");
                            break;
                        default:
                            MobclickAgent.onEvent(mContext, "payment_fali", "付款失败");
                            ToastUtils.shortShowStr(mContext, "支付失败");
                            break;
                    }
                }
                break;
                case AD_LOAD_COMPLETE:

                    if (mttFullVideoAd != null && mIsLoaded) {
                        //step6:在获取到广告后展示
                        //该方法直接展示广告
                        //mttFullVideoAd.showFullScreenVideoAd(FullScreenVideoActivity.this);

                        //展示广告，并传入广告展示的场景
                        mttFullVideoAd.showFullScreenVideoAd(DetailActivity.this, TTAdConstant.RitScenes.GAME_GIFT_BONUS, null);
                        mttFullVideoAd = null;
                    } else {
                        TToast.show(DetailActivity.this, "请先加载广告");
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initInject() {

        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndData() {

        mContext = this;
        mFlRoot = findViewById(R.id.fl_root);
        mFlPreviousFragment = findViewById(R.id.fl_previous_fragment);
        mFlNextFragment = findViewById(R.id.fl_next_fragment);
        mViewPager = findViewById(R.id.vertical_viewpager);

        //CommUtils.setImmerseLayout(mFlRoot, this);
        mPoetryBean = (PoetryBean) getIntent().getSerializableExtra(POETRY_DATA);
        mIndex = getIntent().getIntExtra(POETRY_INDEX, 0);

        mySharedPreferences = new MySharedPreferences(this, "Shared");
        isOpenMember = (String) mySharedPreferences.getValue("isOpenMember", "");
        if (isOpenMember.equals("1")) {
            mPresenter.getMemberLevel();
            mFlRoot.setVisibility(View.GONE);
            //isOpenMember==1,开启支付
            // 获取用户会员信息
            // 非会员展示详情页全屏广告
            if (MyApplication.adConfigList != null) {
                for (int i = 0; i < MyApplication.adConfigList.size(); i++) {

                    if ("4".equals(MyApplication.adConfigList.get(i).location)) {
                        if (MyApplication.adConfigList.get(i).isShow == 1) {
                            isShowAd = true;
                            break;
                        } else {
                            isShowAd = false;
                            break;
                        }
                    }
                }
            }
        } else {
            mFlRoot.setVisibility(View.VISIBLE);
        }


        mPresenter.getAccountInformation();

        mFlPreviousFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mViewPager.getCurrentItem() == 0) {
                    ToastUtils.shortShowStr(DetailActivity.this, "已经到顶了");
                } else {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                }
            }
        });
        mFlNextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPoetryBean != null) {
                    if (mViewPager.getCurrentItem() == mPoetryBean.getList().size() - 1) {
                        ToastUtils.shortShowStr(DetailActivity.this, "已经到底了");
                    } else {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                }
            }
        });
        aliPay = (String) mySharedPreferences.getValue("ali", "");
        wxPay = (String) mySharedPreferences.getValue("wx", "");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void initViewPager() {

        List<BaseFragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < mPoetryBean.getList().size(); i++) {

            fragmentList.add(DetailFragment.newInstance(mPoetryBean.getList().get(i), i));
        }
        VerticalViewPagerAdapter pagerAdapter = new VerticalViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(mIndex);
    }

    @Override
    public void httpCallback(AddPayOrderResponse response) {
        if (response.isSuccess()) {
            AddPayOrderResponse.AddPayOrderInfo result = response.getResult();
            if (result != null) {
                String payerType = result.getPayerType();
                orderId = result.getOrderId();
                if ("ALI".equals(payerType)) {
                    payV2(result.getSignOrder());
                } else if ("WX".equals(payerType)) {
                    String signOrder = result.getSignOrder();
                    SignOrderBean signOrderBean = JSON.parseObject(signOrder, SignOrderBean.class);
                    wxPay(signOrderBean.getReturn_code(), signOrderBean.getReturn_msg(), signOrderBean.getPrepay_id(), signOrderBean.getNonce_str());

                }
            }
        } else {
            ToastUtils.shortShowStr(mContext, response.getMsg());
        }
    }


    @Override
    public void httpCallback(QueryOrderResponse response) {
        if (response.isSuccess()) {
            QueryOrderResponse.QueryOrderInfo result = response.getResult();
            if (result != null) {
                String orderStatus = result.getOrderStatus();
                if ("2".equals(orderStatus) || "3".equals(orderStatus)) {
                    querySum = 3;
                    // 交易成功
                    ToastUtils.shortShowStr(mContext, "支付成功");
                    mySharedPreferences.putValue("isOpenMember", "1");
                    mPresenter.getAccountInformation();
                } else {
                    // 重新查询
                    if (querySum > 0) {
                        querySum--;
                        mPresenter.queryOrder(orderId);
                    } else {
                        querySum = 3;
                        ToastUtils.shortShowStr(mContext, "支付成功");
                        ToastUtils.shortShowStr(mContext, "订单查询失败，请先联系客服。");
                    }
                }
            }
        }
    }

    @Override
    public void httpCallback(MemberLevelResponse response) {
        if (response.isSuccess()) {
            List<MemberLevelResponse.MemberLevelInfo> list = response.getList();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    MemberLevelResponse.MemberLevelInfo memberLevelInfo = list.get(i);
                    if (memberLevelInfo != null) {
                        String code = memberLevelInfo.getCode();
                        int originalPrice = memberLevelInfo.getOriginalPrice();
                        int price = memberLevelInfo.getPrice();
                        if ("9".equals(code)) {
                            yjCode = memberLevelInfo.getGoodId();
                            //prodCode = yjCode;
                            mPriceYj = String.format("¥ %s", Util.changeF2Y(price));
                            mOriginalPriceYj = originalPrice;
//                            if(!pageTo()){
//                                showVipDialog();
//                            }
                            //tv_yjhy_yj.setText(String.format("原价 ¥%s", Util.changeF2Y(originalPrice)));
                            //tv_yjhy_price.setText(String.format("¥ %s", Util.changeF2Y(price)));
                            //tv_price.setText(String.format("¥ %s", Util.changeF2Y(price)));
                        } else if ("4".equals(code)) {
                            ndCode = memberLevelInfo.getGoodId();
                            mPriceNd = String.format("¥ %s", Util.changeF2Y(price));
                            mOriginalPriceNd = originalPrice;
                            //v_ndhy_yj.setText(String.format("原价 ¥%s", Util.changeF2Y(originalPrice)));
                            //tv_ndhy_price.setText(String.format("¥ %s", Util.changeF2Y(price)));
                        } else if ("2".equals(code)) {
                            //ydCode = memberLevelInfo.getGoodId();
                            //tv_ydhy_yj.setText(String.format("原价 ¥%s", Util.changeF2Y(originalPrice)));
                            //tv_ydhy_price.setText(String.format("¥ %s", Util.changeF2Y(price)));
                        }
                    }
                }
            }
        } else {
            ToastUtils.shortShowStr(mContext, response.getMsg());
        }
    }

    @Override
    public void httpCallback(AccountInformationResponse response) {
        if (response.isSuccess()) {
            AccountInformationResponse.AccountInformationInfo result = response.getResult();
            if (result != null) {
                mySharedPreferences.putValue("MemberUser", result.getId());
                mySharedPreferences.putValue("level", result.getLevel());
                mySharedPreferences.putValue("dueTime", result.getDueTime());
                String dueTime = result.getDueTime();
                long count = Util.timeCompare(dueTime);
                String level = result.getLevel();
                if ("1".equals(isOpenMember)) {
                    if (!TextUtils.isEmpty(level)) {
                        // 已开通会员
                        EventBusMessage eventBusMessage = new EventBusMessage();
                        eventBusMessage.setLevel(level);
                        eventBusMessage.setDueTime(result.getDueTime());
                        EventBus.getDefault().post(eventBusMessage);
                        if (isShowAd) {
                            if ("9".equals(level)) {
                                mFlRoot.setVisibility(View.VISIBLE);
                                initViewPager();
                            } else if ("4".equals(level)) {
                                if (count <= 0) {
                                    loadAdvert();
                                } else {
                                    mFlRoot.setVisibility(View.VISIBLE);
                                    initViewPager();
                                }
                            }
                        } else {
                            mFlRoot.setVisibility(View.VISIBLE);
                            initViewPager();
                        }
                    } else {
                        //详情页广告状态开启，则显示，反之隐藏直接观看内容
                        if (isShowAd) {
                            loadAdvert();
                        } else {
                            mFlRoot.setVisibility(View.VISIBLE);
                            initViewPager();
                        }
                    }
                } else {
                    mFlRoot.setVisibility(View.VISIBLE);
                    initViewPager();
                }
                //showGmcgDialog();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WxPayMessageBean event) {
        if (event.isWxPay()) {
            String requestGet = Util.requestGet(orderId, true);
            QueryOrderResponse queryOrderResponse = JSON.parseObject(requestGet, QueryOrderResponse.class);
            if (queryOrderResponse != null && queryOrderResponse.isSuccess()) {
                QueryOrderResponse.QueryOrderInfo queryOrderInfo = queryOrderResponse.getResult();
                if (queryOrderInfo != null) {
                    String orderStatus = queryOrderInfo.getOrderStatus();
                    if ("2".equals(orderStatus) || "3".equals(orderStatus)) {
                        querySum = 3;
                        // 交易成功
                        ToastUtils.shortShowStr(mContext, "支付成功");
                        // 获取用户信息
                        String requestStr = Util.requestGet("", false);
                        AccountInformationResponse accountInformationResponse = JSON.parseObject(requestStr, AccountInformationResponse.class);
                        if (accountInformationResponse != null && accountInformationResponse.isSuccess()) {
                            AccountInformationResponse.AccountInformationInfo informationInfo = accountInformationResponse.getResult();
                            mySharedPreferences.putValue("MemberUser", informationInfo.getId());
                            mySharedPreferences.putValue("level", informationInfo.getLevel());
                            mySharedPreferences.putValue("dueTime", informationInfo.getDueTime());
                            String level = informationInfo.getLevel();
                            if (!TextUtils.isEmpty(level)) {
                                // 已开通会员
                                EventBusMessage eventBusMessage = new EventBusMessage();
                                eventBusMessage.setLevel(level);
                                eventBusMessage.setDueTime(informationInfo.getDueTime());
                                EventBus.getDefault().post(eventBusMessage);
                                //mPresenter.getTemplate(resId);
                                //loadData(TYPE_ORIGINAL);
                            }
                            //showGmcgDialog();
                        }
                        mPresenter.getAccountInformation();


//                        if (spxq) {
//                            setResult(10);
//                        }
//                        mPresenter.getAccountInformation();
                    } else {
                        // 重新查询
                        if (querySum > 2) {
                            querySum--;
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String req = Util.requestGet(orderId, true);
                            QueryOrderResponse queryOrderResponse1 = JSON.parseObject(req, QueryOrderResponse.class);
                            if (queryOrderResponse1 != null && queryOrderResponse1.isSuccess()) {
                                QueryOrderResponse.QueryOrderInfo queryOrderInfo1 = queryOrderResponse1.getResult();
                                if (queryOrderInfo1 != null) {
                                    String orderStatus1 = queryOrderInfo1.getOrderStatus();
                                    if ("2".equals(orderStatus1) || "3".equals(orderStatus1)) {
                                        querySum = 3;
                                        // 交易成功
                                        ToastUtils.shortShowStr(mContext, "支付成功");
                                        // 获取用户信息
                                        String requestStr = Util.requestGet("", false);
                                        AccountInformationResponse accountInformationResponse = JSON.parseObject(requestStr, AccountInformationResponse.class);
                                        if (accountInformationResponse != null && accountInformationResponse.isSuccess()) {
                                            AccountInformationResponse.AccountInformationInfo informationInfo = accountInformationResponse.getResult();
                                            mySharedPreferences.putValue("MemberUser", informationInfo.getId());
                                            mySharedPreferences.putValue("level", informationInfo.getLevel());
                                            mySharedPreferences.putValue("dueTime", informationInfo.getDueTime());
                                            String level = informationInfo.getLevel();
                                            if (!TextUtils.isEmpty(level)) {
                                                // 已开通会员
                                                EventBusMessage eventBusMessage = new EventBusMessage();
                                                eventBusMessage.setLevel(level);
                                                eventBusMessage.setDueTime(informationInfo.getDueTime());
                                                EventBus.getDefault().post(eventBusMessage);
                                            }
                                            //showGmcgDialog();
                                        }
//                                        if (spxq) {
//                                            setResult(10);
//                                        }
//                                        mPresenter.getAccountInformation();
                                    } else {
                                        querySum = 3;
                                        ToastUtils.longShowStr(mContext, "订单查询失败，请联系客服。");
                                    }
                                }
                            }
                        }
                    }
                }
            }
//            mPresenter.queryOrder(orderId);
        }
    }

    /**
     * 是否购买提示弹窗
     */
    private void showVipDialog() {
        View diaView = View.inflate(mContext, R.layout.dialog_buy_vip, null);
        ImageView img_wx_zf = diaView.findViewById(R.id.img_wx_zf);
        ImageView img_zfb_zf = diaView.findViewById(R.id.img_zfb_zf);
        ImageView iv_close = diaView.findViewById(R.id.iv_close);

        TextView tv_price = diaView.findViewById(R.id.tv_price);
        TextView tv_current_yjhy_price = diaView.findViewById(R.id.tv_current_yjhy_price);
        TextView tv_current_ndhy_price = diaView.findViewById(R.id.tv_current_ndhy_price);
        TextView tv_original_price_yjhy = diaView.findViewById(R.id.tv_original_price_yjhy);
        TextView tv_original_price_ndhy = diaView.findViewById(R.id.tv_original_price_ndhy);

        LinearLayout rl_wx = diaView.findViewById(R.id.rl_wx);
        LinearLayout rl_zfb = diaView.findViewById(R.id.rl_zfb);
        LinearLayout ll_yjhy = diaView.findViewById(R.id.ll_yjhy);
        LinearLayout ll_ndhy = diaView.findViewById(R.id.ll_ndhy);
        FrameLayout fl_zf = diaView.findViewById(R.id.fl_zf);

        if (mVideoPlayComplete) {
        } else {
            ToastUtils.shortShowStr(mContext, "看完视频才可浏览诗词内容1次");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(diaView);
        AlertDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置宽度
        p.width = (int) (d.getWidth() * 0.95); // 宽度设置为屏幕的0.95
        p.gravity = Gravity.CENTER;//设置位置
        //p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);

        img_wx_zf.setSelected(true);
        tv_current_yjhy_price.setText(mPriceYj);
        tv_current_ndhy_price.setText(mPriceNd);
        tv_original_price_yjhy.setText(String.format("原价 ¥%s", Util.changeF2Y(mOriginalPriceYj)));
        tv_original_price_ndhy.setText(String.format("原价 ¥%s", Util.changeF2Y(mOriginalPriceNd)));

        ll_yjhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodCode = yjCode;
                mPrice = mPriceYj;
                hyType = "永久会员";
                ll_yjhy.setSelected(true);
                ll_ndhy.setSelected(false);
                for (int i = 0; i < ll_yjhy.getChildCount(); i++) {
                    ll_yjhy.getChildAt(i).setSelected(true);
                }
                for (int i = 0; i < ll_ndhy.getChildCount(); i++) {
                    ll_ndhy.getChildAt(i).setSelected(false);
                }
                tv_price.setText(mPrice);
            }
        });
        ll_ndhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodCode = ndCode;
                mPrice = mPriceNd;
                hyType = "年度会员";
                ll_yjhy.setSelected(false);
                ll_ndhy.setSelected(true);
                for (int i = 0; i < ll_yjhy.getChildCount(); i++) {
                    ll_yjhy.getChildAt(i).setSelected(false);
                }
                for (int i = 0; i < ll_ndhy.getChildCount(); i++) {
                    ll_ndhy.getChildAt(i).setSelected(true);
                }
                tv_price.setText(mPrice);
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoPlayComplete) {
                    dialog.dismiss();
                    mFlRoot.setVisibility(View.VISIBLE);
                    initViewPager();
                } else {
                    dialog.dismiss();
                    finish();
                }
            }
        });
        rl_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 微信支付
                zfType = 1;
                img_zfb_zf.setSelected(false);
                img_wx_zf.setSelected(true);
            }
        });
        rl_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 支付宝支付
                zfType = 2;
                img_wx_zf.setSelected(false);
                img_zfb_zf.setSelected(true);
            }
        });
        fl_zf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确认支付
                dialog.dismiss();
                String price = tv_price.getText().toString().replace("¥ ", "");
                if (TextUtils.isEmpty(price)) {
                    ToastUtils.shortShowStr(mContext, "数据异常,请重试");
                    return;
                }
                if (Float.parseFloat(price) > 0f) {
                    pay(price);
                } else {
                    ToastUtils.shortShowStr(mContext, "请选择需要购买的课程");
                }
            }
        });
        ll_yjhy.performClick();//默认选中永久会员

    }

    /**
     * 支付
     *
     * @param price
     */
    private void pay(String price) {
        String priceF = Util.changeY2F(Double.parseDouble(price)).split("\\.")[0];
        AddPayOrderBean addPayOrderBean = new AddPayOrderBean();
        AddPayOrderBean.AddPayOrderInfo addPayOrderInfo = new AddPayOrderBean.AddPayOrderInfo();
        addPayOrderInfo.setSubject(hyType);
        addPayOrderInfo.setBody(hyType);
        addPayOrderInfo.setChannel(Util.getChannel(mContext));
        List<AddPayOrderBean.GoodsListInfo> list = new ArrayList();
        list.add(new AddPayOrderBean.GoodsListInfo(Integer.parseInt(priceF), prodCode));
        addPayOrderInfo.setGoodsList(list);
        addPayOrderInfo.setCurrencyType("CNY");
//        addPayOrderInfo.setUserId(memberUser);
        //setRecAccount  4: 微信支付 3: 支付宝
        if (zfType == 1) {
            // 微信支付
            if (Util.isWeixinAvilible(mContext)) {
                addPayOrderInfo.setRecAccount(wxPay);
            } else {
                ToastUtils.shortShowStr(mContext, "请先安装微信客户端");
                return;
            }
        } else {
            // 支付宝支付
            addPayOrderInfo.setRecAccount(aliPay);
        }
        addPayOrderInfo.setAmount(Integer.parseInt(priceF));
        addPayOrderBean.setParam(addPayOrderInfo);
        mPresenter.addPayOrder(addPayOrderBean);
    }

    /**
     * 微信支付
     */
    private void wxPay(String return_code, String return_msg, String prepay_id, String nonce_str) {
        //1、 先向微信注册APPID
        IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, null);
        wxapi.registerApp(Constants.WX_APP_ID);

        if ("SUCCESS".equalsIgnoreCase(return_code) && "OK".equalsIgnoreCase(return_msg)) {
            PayReq req = new PayReq();
            req.appId = Constants.WX_APP_ID;//APPID
            req.partnerId = Constants.WX_MCH_ID;//商户号
            req.prepayId = prepay_id;
            req.nonceStr = nonce_str;
            String time = System.currentTimeMillis() / 1000 + "";
            req.timeStamp = time;//时间戳，这次是截取long类型时间的前10位
            req.packageValue = "Sign=WXPay";//参数是固定的，写死的
            SortedMap<String, String> sortedMap = new TreeMap<String, String>();//一定要注意键名，去掉下划线，字母全是小写
            sortedMap.put("appid", Constants.WX_APP_ID);
            sortedMap.put("partnerid", Constants.WX_MCH_ID);
            sortedMap.put("prepayid", prepay_id);
            sortedMap.put("noncestr", nonce_str);
            sortedMap.put("timestamp", time);
            sortedMap.put("package", "Sign=WXPay");
            req.sign = WeChatField.getSign(sortedMap);//重新存除了sign字段之外，再次签名，要不然唤起微信支付会返回-1，特别坑爹的是，键名一定要去掉下划线，不然返回-1
            //进行支付
            wxapi.sendReq(req);
        } else {
            ToastUtils.shortShowStr(mContext, "生成订单失败,请稍后重试!");
        }
    }

    /**
     * 支付宝支付业务示例
     */
    public void payV2(String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(DetailActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public boolean pageTo() {
        String isOpenMember = (String) mySharedPreferences.getValue("isOpenMember", "");
        if ("1".equals(isOpenMember)) {
            String level = (String) mySharedPreferences.getValue("level", "");
            if (TextUtils.isEmpty(level)) {
                return false;
            }
        }
        return true;
    }

    private boolean mVideoPlayComplete;

    private void loadAdvert() {
//step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(MyApplication.getApp());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(Constants.CSJ_CODE_ID1)
                .setSupportDeepLink(true)
                .setOrientation(TTAdConstant.VERTICAL)
                .build();
        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "Callback --> onError: " + code + ", " + String.valueOf(message));
                TToast.show(mContext, message);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                Log.e(TAG, "Callback --> onFullScreenVideoAdLoad" + "FullVideoAd loaded  广告类型：" + getAdType(ad.getFullVideoAdType()));

                //TToast.show(AdvertActivity.this, "FullVideoAd loaded  广告类型：" + getAdType(ad.getFullVideoAdType()));
                mttFullVideoAd = ad;
                mIsLoaded = false;
                mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
                    ///storage/emulated/0/Android/data/com.juguo.gushici/cache/com.juguo.gushici-full_screen_video_cache_945815595/82bcdecaeafa7d962b167c02d97990a9
                    @Override
                    public void onAdShow() {
                        Log.d(TAG, "Callback --> FullVideoAd show");
//                        TToast.show(AdvertActivity.this, "FullVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Log.d(TAG, "Callback --> FullVideoAd bar click");
                        //TToast.show(mContext, "FullVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Log.d(TAG, "Callback --> FullVideoAd close");
                        //TToast.show(mContext, "FullVideoAd close");
                        // loadData(TYPE_ORIGINAL);
                        // mFlRoot.setVisibility(View.VISIBLE);
                        showVipDialog();
                    }

                    @Override
                    public void onVideoComplete() {
                        Log.d(TAG, "Callback --> FullVideoAd complete");
                        mVideoPlayComplete = true;
                        //TToast.show(mContext, "FullVideoAd complete");
                    }

                    @Override
                    public void onSkippedVideo() {
                        Log.d(TAG, "Callback --> FullVideoAd skipped");
                        //TToast.show(mContext, "FullVideoAd skipped");
                        mVideoPlayComplete = false;
                    }
                });


                ad.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);

                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            TToast.show(mContext, "下载中，点击下载区域暂停");
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        TToast.show(mContext, "下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        TToast.show(mContext, "下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
                        TToast.show(mContext, "下载完成，点击下载区域重新下载");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Log.d("DML", "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
                        TToast.show(mContext, "安装完成，点击下载区域打开");
                    }
                });
            }

            @Override
            public void onFullScreenVideoCached() {
                Log.e(TAG, "Callback --> onFullScreenVideoCached");
                mIsLoaded = true;
                Message message = Message.obtain();
                message.what = AD_LOAD_COMPLETE;
                mHandler.sendMessage(message);
//                TToast.show(AdvertActivity.this, "FullVideoAd video cached");
            }
        });
    }

    private String getAdType(int type) {
        switch (type) {
            case TTAdConstant.AD_TYPE_COMMON_VIDEO:
                return "普通全屏视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE_VIDEO:
                return "Playable全屏视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE:
                return "纯Playable，type=" + type;
        }

        return "未知类型+type=" + type;
    }

    @Override
    public void changeStateSuccess(Object o) {

    }

    @Override
    public void httpError(String e) {
        dialogDismiss();
    }
}
