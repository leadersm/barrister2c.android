package com.lsm.barrister2c.ui.activity.creditdebt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.entity.CreditDebtUser;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.creditdebt.UploadCreditDebtReq;
import com.lsm.barrister2c.ui.activity.BaseActivity;
import com.lsm.barrister2c.ui.fragment.uploadcredit.AddCreditDebtInfoFragment;
import com.lsm.barrister2c.ui.fragment.uploadcredit.AddCreditUserFragment;
import com.lsm.barrister2c.ui.fragment.uploadcredit.AddDebtUserFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.File;

/**
 * Created by lvshimin on 16/10/10.
 * 上传页面
 */
public class UploadCreditDebtActivity extends BaseActivity {


    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_credit);

        initActionBar();

        aq = new AQuery(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setOffscreenPageLimit(1);

        ViewGroup tab = (ViewGroup) findViewById(R.id.tab);

        tab.addView(LayoutInflater.from(this).inflate(R.layout.tab_like_a_medium_tag, tab, false));

        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

        adapter = new MyPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(adapter);

        viewPagerTab.setViewPager(mViewPager);

        adapter.notifyDataSetChanged();

        viewPagerTab.setViewPager(mViewPager);

        aq.id(R.id.tv_toolbar_title).text(R.string.title_add_credit_info);

        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                int position = mViewPager.getCurrentItem();

                if(state == ViewPager.SCROLL_STATE_IDLE){
                    updateTitle(position);
                }
            }
        });

        aq.id(R.id.btn_commit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });
    }

    private void doCommit() {

        boolean infoPrepared = addCreditDebtInfoFragment.prepared();
        boolean creditUserPrepared = addCreditUserFragment.prepared();
        boolean debtUserPrepared = addDebtUserFragment.prepared();

        if(infoPrepared && creditUserPrepared && debtUserPrepared){

            CreditDebtInfo info = addCreditDebtInfoFragment.getCreditDebtInfo();
            CreditDebtUser creditUser = addCreditUserFragment.getCreditUser();
            CreditDebtUser debtUser = addDebtUserFragment.getDebtUser();
            info.setCreditUser(creditUser);
            info.setDebtUser(debtUser);

            File proofFile = addCreditDebtInfoFragment.getProofFile();
            File judgeFile = addCreditDebtInfoFragment.getJudgeFile();

            new UploadCreditDebtReq(this,info,proofFile,judgeFile).execute(new Action.Callback<Action.CommonResult>() {
                @Override
                public void progress() {

                }

                @Override
                public void onError(int errorCode, String msg) {

                }

                @Override
                public void onCompleted(Action.CommonResult commonResult) {

                }
            });

        }

    }

    /**
     * 切换标题
     * @param position
     */
    private void updateTitle(int position) {
        String title;
        if (position == 0) {
            title = getString(R.string.title_add_credit_info);
        } else if (position == 1) {
            title = getString(R.string.title_add_credit_user);
        } else {
            title = getString(R.string.title_add_debt_user);
        }
        aq.id(R.id.tv_toolbar_title).text(title);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_upload_credit);
    }

    ViewPager mViewPager;
    MyPagerAdapter adapter;
    SmartTabLayout viewPagerTab;

    AddCreditDebtInfoFragment addCreditDebtInfoFragment = new AddCreditDebtInfoFragment();
    AddCreditUserFragment addCreditUserFragment = new AddCreditUserFragment();
    AddDebtUserFragment addDebtUserFragment = new AddDebtUserFragment();


    /**
     * Title: NewsFragment.java
     * Description:ViewPager适配器
     *
     * @author lsm
     * @date 2015-4-6
     */
    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            if (position == 0) {
                title = getString(R.string.title_add_step1);
            } else if (position == 1) {
                title = getString(R.string.title_add_step2);
            } else {
                title = getString(R.string.title_add_step3);
            }
            return title;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            if (position == 0) {

                fragment = addCreditDebtInfoFragment;

            } else if (position == 1) {

                fragment = addCreditUserFragment;

            }else{
                fragment = addDebtUserFragment;
            }

            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

    }

}
