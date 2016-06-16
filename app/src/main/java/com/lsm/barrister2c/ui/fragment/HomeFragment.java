package com.lsm.barrister2c.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AppConfig;
import com.lsm.barrister2c.data.entity.Ad;
import com.lsm.barrister2c.data.entity.BusinessArea;
import com.lsm.barrister2c.data.entity.BusinessType;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetLunboAdsReq;
import com.lsm.barrister2c.data.io.app.GetMyAccountReq;
import com.lsm.barrister2c.data.io.app.GetUserHomeReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.activity.WebViewActivity;
import com.lsm.barrister2c.ui.adapter.BusinessTypeAdapter;
import com.lsm.barrister2c.ui.adapter.CaseTypeAdapter;
import com.lsm.barrister2c.ui.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    AQuery aq;
    ViewPager mViewPager;
    AdsPagerAdapter mAdsAdapter;
    CirclePageIndicator indicator;

    GetUserHomeReq mGetHomeReq;
    GetLunboAdsReq mGetAdsReq;
    GetMyAccountReq mGetAccountReq;

    RecyclerView mCaseTypesListView, mBusinessTypeListView;
    GridLayoutManager mBusinessTypeListLayoutManager, mCaseTypeListLayoutManager;

    CaseTypeAdapter mCaseTypeListAdapter;
    BusinessTypeAdapter mBusinessTypeListAdapter;

    View view;

    private void init(View view) {
        this.view = view;

        aq = new AQuery(view);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_home_images);
        mAdsAdapter = new AdsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdsAdapter);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);

        //实例化
        mCaseTypesListView = (RecyclerView) view.findViewById(R.id.recyclerview_home_case_type);
        mBusinessTypeListView = (RecyclerView) view.findViewById(R.id.recyclerview_home_business_type);

        //布局管理
        mBusinessTypeListLayoutManager = new GridLayoutManager(getActivity(), 5);
        mCaseTypeListLayoutManager = new GridLayoutManager(getActivity(), 2);

        mCaseTypeListAdapter = new CaseTypeAdapter(caseTypeList);
        mCaseTypesListView.setLayoutManager(mBusinessTypeListLayoutManager);
        mCaseTypesListView.setItemAnimator(new DefaultItemAnimator());
        mCaseTypesListView.setAdapter(mCaseTypeListAdapter);

        mBusinessTypeListAdapter = new BusinessTypeAdapter(businessTypeList);
        mBusinessTypeListView.setLayoutManager(mCaseTypeListLayoutManager);
        mBusinessTypeListView.setItemAnimator(new DefaultItemAnimator());
        mBusinessTypeListView.setAdapter(mBusinessTypeListAdapter);

        refresh();
    }

    List<BusinessArea> caseTypeList = new ArrayList<>();
    List<BusinessType> businessTypeList = new ArrayList<>();

    public void refresh() {
        if (mGetHomeReq == null) {
            mGetHomeReq = new GetUserHomeReq(getContext());
        }

        mGetHomeReq.execute(new Action.Callback<IO.HomeResult>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getContext(), msg);
            }

            @Override
            public void onCompleted(IO.HomeResult homeResult) {

                bindHomeData(homeResult);
            }
        });

        if (mGetAdsReq == null) {
            mGetAdsReq = new GetLunboAdsReq(getActivity());
        }

        mGetAdsReq.execute(new Action.Callback<IO.GetLunboAdsResult>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getContext(), msg);
            }

            @Override
            public void onCompleted(IO.GetLunboAdsResult getLunboAdsResult) {
                if (getLunboAdsResult != null && getLunboAdsResult.ads != null) {

                    ads.clear();
                    ads.addAll(getLunboAdsResult.ads);
                    mAdsAdapter.notifyDataSetChanged();

                }
            }
        });


        if(mGetAccountReq == null){
            mGetAccountReq = new GetMyAccountReq(getContext());
        }

        User user = AppConfig.getUser(getContext());

        if(user!=null)
        mGetAccountReq.execute(new Action.Callback<IO.GetAccountResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getContext(),"获取信息失败："+msg);
            }

            @Override
            public void onCompleted(IO.GetAccountResult result) {

                if(result!=null){

                    String remainingBalance = result.account.getRemainingBalance();
                    String totalConsume = result.account.getTotalConsume();

                    aq.id(R.id.tv_home_yue).text(remainingBalance + "元");

                    aq.id(R.id.tv_home_consume).text(totalConsume + "元");

                }
            }
        });


    }


    private void bindHomeData(IO.HomeResult homeResult) {

        List<BusinessArea> caseTypeList = homeResult.caseTypeList;
        List<BusinessType> businessTypeList = homeResult.businessTypeList;

        if (caseTypeList != null) {
            this.caseTypeList.clear();
            this.caseTypeList.addAll(caseTypeList);
            mCaseTypeListAdapter.notifyDataSetChanged();
        }

        if (businessTypeList != null) {
            this.businessTypeList.clear();
            this.businessTypeList.addAll(businessTypeList);
            mBusinessTypeListAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    List<Ad> ads = new ArrayList<>();

    public class AdsPagerAdapter extends FragmentPagerAdapter {

        public AdsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return AdFragment.newInstance(ads.get(position));
        }

        @Override
        public int getCount() {
            return ads.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

    }

    public static class AdFragment extends Fragment {

        private static final String AD = "item.ad";

        public AdFragment() {
        }

        Ad ad;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle args = getArguments();
            if (args != null && args.getSerializable(AD) != null) {
                ad = (Ad) args.getSerializable(AD);
            }
        }

        public static AdFragment newInstance(Ad ad) {
            AdFragment fragment = new AdFragment();
            Bundle args = new Bundle();
            args.putSerializable(AD, ad);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            SimpleDraweeView draweeView = (SimpleDraweeView) inflater.inflate(R.layout.item_image, container, false);

            draweeView.setImageURI(Uri.parse(ad.getImage()));

//            Picasso.with(getActivity()).load(image.getThumb()).into(draweeView);

            draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(WebViewActivity.KEY_URL, ad.getUrl());
                    intent.putExtra(WebViewActivity.KEY_TITLE, "");
                    startActivity(intent);
                }
            });
            return draweeView;
        }
    }
}
