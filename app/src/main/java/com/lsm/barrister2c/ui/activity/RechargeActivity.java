package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.aliapi.AliPay;
import com.lsm.barrister2c.data.entity.WxPrepayInfo;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.app.GetAliPrePayInfoReq;
import com.lsm.barrister2c.data.io.app.GetWXPrePayInfoReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.wxapi.WXPay;


/**
 * 充值
 */
public class RechargeActivity extends BaseActivity {

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
                progressDialog.setMessage("正在创建订单，请稍候..");
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                progressDialog.dismiss();
                UIHelper.showToast(getApplicationContext(),"创建订单失败");
            }

            @Override
            public void onCompleted(final String payInfo) {
                progressDialog.dismiss();
                AliPay.getInstance().init(RechargeActivity.this).pay(payInfo);
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
                progressDialog.setMessage("正在创建订单，请稍候..");
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                progressDialog.dismiss();
                UIHelper.showToast(getApplicationContext(),"创建订单失败");
            }

            @Override
            public void onCompleted(WxPrepayInfo info) {
                progressDialog.dismiss();

                WXPay.getInstance().init(RechargeActivity.this).pay(info);

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
