package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.app.UserHelper;
import com.lsm.barrister2c.data.entity.Account;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.app.GetMoneyReq;
import com.lsm.barrister2c.ui.UIHelper;


/**
 * 提现
 */
public class GetMoneyActivity extends BaseActivity {

    GetMoneyReq mGetMoneyReq;

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_money);
        aq = new AQuery(this);

        initActionBar();

        aq.id(R.id.btn_commit).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 提现
                doCommit();
            }
        });

        aq.id(R.id.cb_getmoney_doc).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(aq.id(R.id.cb_getmoney_doc).isChecked()){
                    aq.id(R.id.btn_commit).visible();
                }else{
                    aq.id(R.id.btn_commit).invisible();

                }
            }
        });

        aq.id(R.id.btn_getmoney_doc).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UIHelper.goDocActivity(GetMoneyActivity.this,"提现须知", Constants.DOC_GETMONEY);
            }
        });
    }



    boolean isLoading = false;

    /**
     * 保存
     */
    protected void doCommit() {
        if(isLoading)
            return;

        String bankCardBindStatus = UserHelper.getInstance().getAccount().getBankCardBindStatus();
        if(bankCardBindStatus!=null && bankCardBindStatus.equals(Account.CARD_STATUS_NOT_BOUND)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_bankcard_not_bind));
            return;
        }

        final String money = aq.id(R.id.et_get_money).getEditable().toString();

        if(TextUtils.isEmpty(money)){
            UIHelper.showToast(getApplicationContext(),"请输入提现金额...");
            return;
        }

        mGetMoneyReq = new GetMoneyReq(this, money);
        mGetMoneyReq.execute(new Action.Callback<Boolean>() {

            @Override
            public void progress() {
                isLoading = true;

                progressDialog.setMessage(getString(R.string.tip_loading));
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                isLoading = false;

                mGetMoneyReq = null;

                progressDialog.dismiss();

                UIHelper.showToast(getApplicationContext(),msg);
            }

            @Override
            public void onCompleted(Boolean t) {
                isLoading = false;

                mGetMoneyReq = null;

                progressDialog.dismiss();

                UIHelper.showToast(getApplicationContext(), getString(R.string.tip_get_money_success));

                finish();

            }
        });

    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.get_money);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mGetMoneyReq !=null && mGetMoneyReq.isLoading())
        {
            mGetMoneyReq.cancel();
            mGetMoneyReq = null;
        }
    }
}
