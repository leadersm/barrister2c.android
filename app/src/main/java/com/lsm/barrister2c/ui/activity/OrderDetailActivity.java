package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.rongcloud.ECHelper;

/**
 * 订单详情页
 */
public class OrderDetailActivity extends BaseActivity {

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setupToolbar();

        aq = new AQuery(this);
        aq.id(R.id.btn_call_make).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ECHelper.getInstance().makeVoiceCall("8011846500000003");
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_order_detail);
    }
}
