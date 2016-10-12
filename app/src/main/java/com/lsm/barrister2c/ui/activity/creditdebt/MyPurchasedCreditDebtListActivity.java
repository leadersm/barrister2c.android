package com.lsm.barrister2c.ui.activity.creditdebt;

import android.os.Bundle;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.ui.activity.BaseActivity;

/**
 * Created by lvshimin on 16/10/10.
 * 我购买的债权债务信息
 */
public class MyPurchasedCreditDebtListActivity extends BaseActivity {

    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchased_credit);
        aq = new AQuery(this);

    }


}
