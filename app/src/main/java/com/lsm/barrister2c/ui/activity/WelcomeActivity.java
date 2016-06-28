package com.lsm.barrister2c.ui.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AppConfig;
import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.app.VersionHelper;
import com.lsm.barrister2c.data.entity.LawApp;
import com.lsm.barrister2c.push.PushUtil;
import com.lsm.barrister2c.utils.DLog;

import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * 欢迎页
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupPush();

        setContentView(R.layout.activity_welcome);

        initAppInfo();

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
        if (AppConfig.getInstance().getPushId(this) == null) {
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

    /**
     * 初始化应用信息，读取配置文件
     */
    private void initAppInfo() {
        try {

            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            //渠道信息
            Constants.MARKET = appInfo.metaData.getInt(Constants.MARKET_KEY);
            //debug
            Constants.DEBUG = appInfo.metaData.getBoolean(Constants.DEBUG_KEY);

            Log.d(TAG, "MARKET:" + Constants.MARKET + ",DEBUG:" + Constants.DEBUG);

            DisplayMetrics dm = getResources().getDisplayMetrics();

            DLog.i(TAG, "screenWidth:" + dm.widthPixels + "-screenHeigh:" + dm.heightPixels);

            Constants.screenSize = dm.widthPixels + "*" + dm.heightPixels;
            Constants.screenHeight = dm.heightPixels;
            Constants.screenWidth = dm.widthPixels;


            if (AppConfig.getInstance().getPushId(this) == null) {
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

            List<LawApp> apps = AppConfig.getInstance().getApps();
            if (apps == null || apps.isEmpty()) {
                AppConfig.getInstance().initLawApps();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
