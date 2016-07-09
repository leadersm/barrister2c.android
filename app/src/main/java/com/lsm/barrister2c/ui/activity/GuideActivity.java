package com.lsm.barrister2c.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AppConfig;
import com.lsm.barrister2c.app.VersionHelper;


/**
 * Title: GuideAcitivity.java
 * Description: 新手引导页,软件特色介绍，（首次安装）上传设备信息
 *
 * @author lsm
 * @date 2015-4-3
 */
public class GuideActivity extends FragmentActivity {

    protected static final String TAG = GuideActivity.class.getSimpleName();

    ViewPager mPager;

    GuideFragmentAdapter mAdapter;

    private final int[] resource = new int[]{R.drawable.welcome_1, R.drawable.welcome_2, R.drawable.welcome_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//		AppConfig.getInstance().setDarkTheme(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mPager = (ViewPager) findViewById(R.id.pager);

        mAdapter = new GuideFragmentAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);

    }

    public class GuideFragmentAdapter extends FragmentStatePagerAdapter {

        public GuideFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return GuideFragment.newInstance(resource[position], position == resource.length - 1);
        }

        @Override
        public int getCount() {
            return resource.length;
        }
    }

    public static class GuideFragment extends Fragment {

        int resId;
        boolean startVisiable;

        public static GuideFragment newInstance(int resId, boolean startVisiable) {
            GuideFragment fragment = new GuideFragment();

            Bundle b = new Bundle();
            b.putInt("resId", resId);
            b.putBoolean("startVisiable", startVisiable);

            fragment.setArguments(b);

            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle b = getArguments();
            this.resId = b.getInt("resId");
            this.startVisiable = b.getBoolean("startVisiable");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_guide, container, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView_guide);
            imageView.setImageResource(resId);

            if (startVisiable)
                imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    startActivity(new Intent(getActivity(), MainActivity.class));

                    getActivity().finish();

                    saveGuided();

                }
            });

            return view;
        }

        /**
         * 保存状态
         */
        public void saveGuided() {

            new Thread() {

                public void run() {

                    AppConfig.getInstance().saveVersionCode(VersionHelper.instance().getVersionCode());

                    AppConfig.getInstance().setForceUpdate(false);

                }

            }.start();

        }

    }


}
