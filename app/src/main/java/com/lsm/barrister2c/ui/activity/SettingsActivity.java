package com.lsm.barrister2c.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.app.UserHelper;
import com.lsm.barrister2c.app.VersionHelper;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.app.LogoutReq;
import com.lsm.barrister2c.ui.UIHelper;


/**
 * 设置页
 */
public class SettingsActivity extends BaseActivity {


    AQuery aq;

    boolean isLogouting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupToolbar();
        aq = new AQuery(this);
        aq.id(R.id.btn_settings_about).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goDocActivity(SettingsActivity.this,"关于我们", Constants.DOC_ABOUT);
            }
        });
        aq.id(R.id.btn_settings_feedback).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,FeedbackActivity.class);
                startActivity(intent);
            }
        });
        aq.id(R.id.btn_settings_help).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goDocActivity(SettingsActivity.this,"帮助", Constants.DOC_ABOUT);
            }
        });
        aq.id(R.id.btn_settings_update).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersionHelper.instance().check(SettingsActivity.this,true);
            }
        });
        aq.id(R.id.btn_settings_logout).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isLogouting){
                    return ;
                }

                new LogoutReq(SettingsActivity.this).execute(new Action.Callback<Boolean>() {

                    @Override
                    public void progress() {
                        isLogouting = true;
                    }

                    @Override
                    public void onError(int errorCode, String msg) {
                        isLogouting = false;
                    }

                    @Override
                    public void onCompleted(Boolean aBoolean) {
                        isLogouting = false;

                        //登出
                        UserHelper.getInstance().logout(getApplicationContext());

                        finish();

                        //跳转登录页
                        UIHelper.goLoginActivity(SettingsActivity.this);
                    }
                });

            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.settings);
    }


}
