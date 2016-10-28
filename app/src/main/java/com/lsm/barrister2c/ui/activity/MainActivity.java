package com.lsm.barrister2c.ui.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AppConfig;
import com.lsm.barrister2c.app.AppManager;
import com.lsm.barrister2c.app.BizHelper;
import com.lsm.barrister2c.app.UserHelper;
import com.lsm.barrister2c.app.VersionHelper;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.ui.fragment.AvaterCenterFragment;
import com.lsm.barrister2c.ui.fragment.CreditDebtCenter;
import com.lsm.barrister2c.ui.fragment.FaxianFragment;
import com.lsm.barrister2c.ui.fragment.HomeFragment;
import com.lsm.barrister2c.ui.fragment.LearningCenterFragment;
import com.lsm.barrister2c.ui.widget.BottomNavigationItem;
import com.lsm.barrister2c.ui.widget.BottomNavigationView;

import cn.jpush.android.api.JPushInterface;


/**
 * 主页，5个模块，HOME, 咨询，我的订单，学习中心，个人中心
 */
public class MainActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    AQuery aq;
    User user;
    private ViewPager mViewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBottomNav();

        setupViewPager();

        aq = new AQuery(this);

        user = AppConfig.getUser(this);

        BizHelper.getInstance().init(this);

        AQUtility.postDelayed(new Runnable() {
            @Override
            public void run() {
                VersionHelper.instance().check(MainActivity.this,false);
            }
        },2000);

    }

    private void setupViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.onBottomNavigationItemClick(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupBottomNav() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        if (bottomNavigationView != null){
            bottomNavigationView.isWithText(true);
            bottomNavigationView.isColoredBackground(false);
        }

        BottomNavigationItem homeItem = new BottomNavigationItem
                (getString(R.string.tab_home), getResources().getColor(R.color.colorPrimary), R.drawable.func_main_home_selector);
        BottomNavigationItem myConsultsItem = new BottomNavigationItem
                (getString(R.string.tab_faxian), getResources().getColor(R.color.colorPrimary), R.drawable.func_main_faxian_selector);

        BottomNavigationItem creditdebtItem = new BottomNavigationItem
                (getString(R.string.tab_credit_debt), getResources().getColor(R.color.colorPrimary), R.drawable.func_main_credit_selector);

        BottomNavigationItem learningCenterItem = new BottomNavigationItem
                (getString(R.string.tab_learning_center), getResources().getColor(R.color.colorPrimary), R.drawable.func_main_learning_selector);
        BottomNavigationItem avaterCenterItem = new BottomNavigationItem
                (getString(R.string.tab_avatar_center), getResources().getColor(R.color.colorPrimary),R.drawable.func_main_avatar_selector);


        bottomNavigationView.addTab(homeItem);
        bottomNavigationView.addTab(myConsultsItem);
        bottomNavigationView.addTab(creditdebtItem);
        bottomNavigationView.addTab(learningCenterItem);
        bottomNavigationView.addTab(avaterCenterItem);

        bottomNavigationView.setOnBottomNavigationItemClickListener(new BottomNavigationView.OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {

                mViewPager.setCurrentItem(index, false);

                switch (index) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                }
            }
        });
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ImageFragment (defined as a static inner class below).
            if(position==0){
                return HomeFragment.newInstance();
            }else if(position==1){
                return FaxianFragment.newInstance();
            }else if(position==2){
                return CreditDebtCenter.newInstance();
            }else if(position==3){
                return LearningCenterFragment.newInstance();
            }else{
                return AvaterCenterFragment.newInstance(user);
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

    }

    @Override
    public void onBackPressed() {

        if(AppManager.getAppManager().exit(this))
            super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();

        AppManager.setMainActivityRunning(true);

        // 启动推送服务
        JPushInterface.resumePush(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 程序退出销毁
     */
    @Override
    protected void onDestroy() {

        UserHelper.getInstance().clearListeners();

        super.onDestroy();

        AppManager.setMainActivityRunning(false);

        AppManager.getAppManager().finishAllActivity();

        //取消通知
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();

    }

}
