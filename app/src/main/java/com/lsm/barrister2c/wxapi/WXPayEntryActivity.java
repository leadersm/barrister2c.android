package com.lsm.barrister2c.wxapi;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.aliapi.AliPay;
import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.data.entity.WxPrepayInfo;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.app.GetAliPrePayInfoReq;
import com.lsm.barrister2c.data.io.app.GetWXPrePayInfoReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.activity.BaseActivity;
import com.lsm.barrister2c.utils.DLog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;

	AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharege);
		setupToolbar();

		aq = new AQuery(this);

		aq.id(R.id.btn_recharge_alipay).clicked(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				aq.id(R.id.cb_recharge_alipay).checked(true);
				aq.id(R.id.cb_recharge_wx).checked(false);
			}
		});
		aq.id(R.id.btn_recharge_wx).clicked(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				aq.id(R.id.cb_recharge_alipay).checked(false);
				aq.id(R.id.cb_recharge_wx).checked(true);
			}
		});
		aq.id(R.id.cb_recharge_alipay).clicked(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				aq.id(R.id.cb_recharge_alipay).checked(true);
				aq.id(R.id.cb_recharge_wx).checked(false);
			}
		});
		aq.id(R.id.cb_recharge_wx).clicked(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				aq.id(R.id.cb_recharge_alipay).checked(false);
				aq.id(R.id.cb_recharge_wx).checked(true);
			}
		});
		aq.id(R.id.btn_commit).clicked(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doCommit();
			}
		});
    	api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

//	private void setupToolbar() {
//		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setTitle(R.string.title_wx_pay_result);
//	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

    /**
     * 微信回调
     * @param resp
     */
	@Override
	public void onResp(BaseResp resp) {

		Log.d(TAG, "====>onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            System.out.println("====>resp.err:"+resp.errStr+",resp.code："+resp.errCode);
            if(resp.errCode==0){
                //成功
                DLog.d(TAG,"支付成功");
                new AlertDialog.Builder(this)
                        .setTitle(R.string.tip)
                        .setMessage("充值成功")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).create().show();

            }else if(resp.errCode==-1){
                //失败
                DLog.e(TAG,"支付失败");

            }else if(resp.errCode==-2){
                //用户取消
                DLog.d(TAG,"取消支付");
            }
		}
	}


	private void doCommit() {

		String money = aq.id(R.id.et_recharge).getEditable().toString();
		if(TextUtils.isEmpty(money)){
			UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_feedback));
			return;
		}

		//商品名称
		String goodsName = getString(R.string.goods_name_info);
		//商品信息描述
		String goodsInfo = getString(R.string.goods_name_info);

		//金额
		float moneyAct = Float.parseFloat(money);

		if(moneyAct<0.01f){
			UIHelper.showToast(getApplicationContext(),getString(R.string.tip_min_recharge_money));
			return;
		}

		if(aq.id(R.id.cb_recharge_wx).isChecked()){
			//微信支付
			doGetWxPrepayInfo(goodsName,goodsInfo,moneyAct);
		}else{
			//支付宝支付
			doGetAliPrepayInfo(goodsName,goodsInfo,moneyAct);
		}

	}

	/**
	 * 支付宝预付订单
	 * @param goodsName
	 * @param goodsInfo
	 * @param money
	 */
	private void doGetAliPrepayInfo(String goodsName, String goodsInfo, float money) {

		new GetAliPrePayInfoReq(this,goodsName,goodsInfo,money).execute(new Action.Callback<String>(){

			@Override
			public void progress() {
                aq.id(R.id.btn_commit).enabled(false);
				progressDialog.setMessage("正在创建订单，请稍候..");
				progressDialog.show();
			}

			@Override
			public void onError(int errorCode, String msg) {
                aq.id(R.id.btn_commit).enabled(true);
				progressDialog.dismiss();
				UIHelper.showToast(getApplicationContext(),"创建订单失败");
			}

			@Override
			public void onCompleted(final String payInfo) {
                aq.id(R.id.btn_commit).enabled(true);
				progressDialog.dismiss();
				AliPay.getInstance().init(WXPayEntryActivity.this).pay(payInfo);
			}
		});
	}

	/**
	 * 微信预付订单
	 * @param goodsName
	 * @param goodsInfo
	 * @param money
	 */
	private void doGetWxPrepayInfo(String goodsName, String goodsInfo, float money) {
		new GetWXPrePayInfoReq(this,goodsName,goodsInfo,money).execute(new Action.Callback<WxPrepayInfo>() {

			@Override
			public void progress() {
                aq.id(R.id.btn_commit).enabled(false);

				progressDialog.setMessage("正在创建订单，请稍候..");
				progressDialog.show();
			}

			@Override
			public void onError(int errorCode, String msg) {
                aq.id(R.id.btn_commit).enabled(true);

				progressDialog.dismiss();
				UIHelper.showToast(getApplicationContext(),"创建订单失败");
			}

			@Override
			public void onCompleted(WxPrepayInfo info) {
                aq.id(R.id.btn_commit).enabled(true);

				progressDialog.dismiss();

				WXPay.getInstance().init(WXPayEntryActivity.this).pay(info);

			}
		});

	}

	private void setupToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.recharge);
	}
}