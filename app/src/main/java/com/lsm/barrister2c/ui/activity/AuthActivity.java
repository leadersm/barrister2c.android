package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.app.WebAuthReq;
import com.lsm.barrister2c.ui.UIHelper;

/**
 * Created by lvshimin on 16/8/20.
 * 授权登陆
 */
public class AuthActivity extends BaseActivity{


    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        aq = new AQuery(this);
        aq.id(R.id.btn_auth_ok).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });

        //取消
        aq.id(R.id.btn_auth_cancel).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //关闭
        aq.id(R.id.btn_auth_close).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void doCommit() {
        new WebAuthReq(this).execute(new Action.Callback<Boolean>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getApplicationContext(),"授权失败");
            }

            @Override
            public void onCompleted(Boolean aBoolean) {
                UIHelper.showToast(getApplicationContext(),"授权成功");
                finish();
            }
        });
    }
}
