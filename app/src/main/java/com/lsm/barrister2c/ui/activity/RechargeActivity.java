package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.aliapi.AliPay;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.wxapi.WXPay;


/**
 * 充值
 */
public class RechargeActivity extends BaseActivity {

    AQuery aq;

//    GetWXPrePayInfoReq mRechareReq;

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
        String goodsName = "大律师充值";
        //商品信息描述
        String goodsInfo = "大律师充值";
        //金额
        float moneyAct = Float.parseFloat(money);

        if(aq.id(R.id.cb_recharge_wx).isChecked()){
            //微信支付
            WXPay.getInstance().init(RechargeActivity.this).pay(goodsName,goodsInfo,moneyAct);
        }else{
            //支付宝支付
            AliPay.getInstance().init(RechargeActivity.this).pay(goodsName,goodsInfo,moneyAct);
        }

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.recharge);
    }
}
