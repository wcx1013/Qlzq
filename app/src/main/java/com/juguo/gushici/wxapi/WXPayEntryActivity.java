package com.juguo.gushici.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.juguo.gushici.R;
import com.juguo.gushici.bean.WxPayMessageBean;
import com.juguo.gushici.utils.Constants;
import com.juguo.gushici.utils.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		//微信支付后续操作，失败，成功，取消
		int code = resp.errCode;
		switch (code){
			case 0://支付成功后的界面
				WxPayMessageBean wxPayMessageBean = new WxPayMessageBean();
				wxPayMessageBean.setWxPay(true);
				EventBus.getDefault().post(wxPayMessageBean);
				//返回主页面 然后在跳转至订单页面
				finish();
				break;
			case -1:
				ToastUtils.shortShowStr(this, "支付失败");
				finish();
				break;
			case -2://用户取消支付后的界面
				ToastUtils.shortShowStr(this, "您已取消支付");
				finish();
				break;
		}
	}
}