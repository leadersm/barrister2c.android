package com.lsm.barrister2c.ui.activity.creditdebt;

import android.os.Bundle;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.ui.activity.BaseActivity;

/**
 * Created by lvshimin on 16/10/10.
 * 我上传的
 */
public class MyUploadCreditDebtListActivity extends BaseActivity {

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_upload_credit);
        aq = new AQuery(this);

    }

}
