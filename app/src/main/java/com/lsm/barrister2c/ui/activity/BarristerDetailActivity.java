package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.lsm.barrister2c.R;


/**
 * 律师信息页
 */
public class BarristerDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barrister_detail);

        setupToolbar();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_barrister_detail);

    }
}
