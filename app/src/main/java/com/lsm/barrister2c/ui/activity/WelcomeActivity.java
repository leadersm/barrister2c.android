package com.lsm.barrister2c.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AppConfig;
import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.app.VersionHelper;
import com.lsm.barrister2c.module.push.PushUtil;
import com.lsm.barrister2c.utils.DLog;

import cn.jpush.android.api.JPushInterface;


/**
 * 欢迎页
 */
public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setupPush();

        handler.sendEmptyMessageDelayed(MSG_WHAT_GO, DELAYED);
    }

    private static final int MSG_WHAT_GO = 0;
    //跳转延时
    private static final int DELAYED = 3000;


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            //检查配置文件中的版本信息与当前版本是否一致
            int savedVersion = AppConfig.getInstance().getSavedVersionCode(WelcomeActivity.this);
            int appVersion = VersionHelper.instance().getVersionCode();

            DLog.i(TAG, "cVersion:" + appVersion + ",preVersion:" + savedVersion);

            if (savedVersion < appVersion) {

                //首次安装或版本升级 进入新手引导页
                Intent i = new Intent(WelcomeActivity.this, GuideActivity.class);
                startActivity(i);

            } else {

                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);

            }

//            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//            startActivity(intent);

            finish();


        }
    };

    private void setupPush() {

        if (TextUtils.isEmpty(Constants.PUSH_ID)) {
            String pushId = JPushInterface.getRegistrationID(this);
            DLog.d(TAG, "pushId:" + pushId);
        }

        String pushTag = AppConfig.getInstance().getPushTag(this);

        //设置别名，防止内外网推送混乱
        if (Constants.DEBUG) {

            DLog.d(TAG, "推送设置：内网接收");

            if (TextUtils.isEmpty(pushTag) || !pushTag.equals(Constants.TAG_LAN)) {
                PushUtil.getInstance().setTag(Constants.TAG_LAN);
            }

        } else {

            DLog.d(TAG, "推送设置：外网接收");
            if (TextUtils.isEmpty(pushTag) || !pushTag.equals(Constants.TAG_WAN)) {
                PushUtil.getInstance().setTag(Constants.TAG_WAN);
            }

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
