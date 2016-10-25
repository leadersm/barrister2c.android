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
import com.lsm.barrister2c.app.BizHelper;
import com.lsm.barrister2c.app.UserHelper;
import com.lsm.barrister2c.data.entity.Account;
import com.lsm.barrister2c.data.entity.Ad;
import com.lsm.barrister2c.data.entity.BusinessArea;
import com.lsm.barrister2c.data.entity.BusinessType;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetBarristerListReq;
import com.lsm.barrister2c.data.io.app.GetMyAccountReq;
import com.lsm.barrister2c.data.io.app.GetUserHomeReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.activity.WebViewActivity;
import com.lsm.barrister2c.ui.adapter.BizAreaAdapter;
import com.lsm.barrister2c.ui.adapter.BizTypeAdapter;
import com.lsm.barrister2c.ui.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements UserHelper.UserActionListener,UserHelper.OnAccountUpdateListener{


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
//    GetLunboAdsReq mGetAdsReq;
//    GetMyAccountReq mGetAccountReq;

    RecyclerView mCaseTypesListView, mBusinessTypeListView;
    GridLayoutManager mBusinessTypeListLayoutManager, mCaseTypeListLayoutManager;

    BizAreaAdapter mCaseTypeListAdapter;
    BizTypeAdapter mBusinessTypeListAdapter;

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
        mCaseTypeListLayoutManager = new GridLayoutManager(getActivity(), 3);

        mCaseTypeListAdapter = new BizAreaAdapter(mBizAreas);
        mCaseTypesListView.setNestedScrollingEnabled(false);
        mCaseTypesListView.setLayoutManager(mBusinessTypeListLayoutManager);
        mCaseTypesListView.setItemAnimator(new DefaultItemAnimator());
        mCaseTypesListView.setAdapter(mCaseTypeListAdapter);

        mBusinessTypeListAdapter = new BizTypeAdapter(mBizTypes);
        mBusinessTypeListView.setNestedScrollingEnabled(false);
        mBusinessTypeListView.setLayoutManager(mCaseTypeListLayoutManager);
        mBusinessTypeListView.setItemAnimator(new DefaultItemAnimator());
        mBusinessTypeListView.setAdapter(mBusinessTypeListAdapter);


        aq.id(R.id.btn_faxian_appointment).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goBarristerListAcitivity(v.getContext(), GetBarristerListReq.TYPE_APPOINTMENT, null, null);
            }
        });
        aq.id(R.id.btn_faxian_im).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goBarristerListAcitivity(v.getContext(), GetBarristerListReq.TYPE_IM, null, null);
            }
        });

        aq.id(R.id.btn_faxian_expert).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goExpertListAcitivity(v.getContext());
            }
        });

        aq.id(R.id.btn_upload_case).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goUploadCaseActivity(getActivity());
            }
        });

        refresh();

        loadMyAccount();
    }

    List<BusinessArea> mBizAreas = new ArrayList<>();
    List<BusinessType> mBizTypes = new ArrayList<>();

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
               if(isAdded()){
                   UIHelper.showToast(getContext(), msg);
               }
            }

            @Override
            public void onCompleted(IO.HomeResult homeResult) {

                if(isAdded()){
                    bindHomeData(homeResult);
                }
            }
        });
    }

    private void loadMyAccount(){

        User user = AppConfig.getUser(getContext());

        if(user==null)
            return;

        new GetMyAccountReq(getActivity()).execute(new Action.Callback<IO.GetAccountResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
            }

            @Override
            public void onCompleted(IO.GetAccountResult result) {
                UserHelper.getInstance().setAccount(result.account);
                UserHelper.getInstance().updateAccount();
            }
        });

    }


    private void bindHomeData(IO.HomeResult homeResult) {

        List<BusinessArea> bizAreas = homeResult.bizAreas;
        List<BusinessType> bizTypes = homeResult.bizTypes;

        BizHelper.getInstance().setBizAreas(bizAreas);
        BizHelper.getInstance().setBizTypes(bizTypes);

        List<Ad> ads = homeResult.list;

        if (bizAreas != null) {
            this.mBizAreas.clear();
            this.mBizAreas.addAll(bizAreas);
            mCaseTypeListAdapter.notifyDataSetChanged();
        }

        if (bizTypes != null) {
            this.mBizTypes.clear();
            this.mBizTypes.addAll(bizTypes);
            mBusinessTypeListAdapter.notifyDataSetChanged();
        }

        if(ads!=null){
            this.ads.clear();
            this.ads.addAll(ads);
            mAdsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        UserHelper.getInstance().addOnUserActionListener(this);
        UserHelper.getInstance().addOnAccountUpdateListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UserHelper.getInstance().removeListener(this);
        UserHelper.getInstance().removeAccountListener(this);
    }

    List<Ad> ads = new ArrayList<>();

    @Override
    public void onSSOLoginCallback(User user) {
    }

    @Override
    public void onLoginCallback(User user) {
        //请求账户信息，余额，累计消费
        loadMyAccount();
    }

    @Override
    public void onLogoutCallback() {
        //余额，累计消费 显示 0.0
        onUpdateUserInfo();
    }

    @Override
    public void onUpdateUserInfo() {

    }

    @Override
    public void onUpdateAccount(Account account) {
//        float remainingBalance = 0f;
//        float totalConsume = 0f;
//
//        if(account != null){
//            remainingBalance = account.getRemainingBalance();
//            totalConsume = account.getTotalConsume();
//        }
//        aq.id(R.id.tv_home_yue).text(String.format(Locale.CHINA,"%.2f元", remainingBalance));
//        aq.id(R.id.tv_home_consume).text(String.format(Locale.CHINA,"%.2f元", totalConsume));
    }

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
