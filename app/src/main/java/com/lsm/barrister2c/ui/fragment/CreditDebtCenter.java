package com.lsm.barrister2c.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AppConfig;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetMyOrderListReq;
import com.lsm.barrister2c.data.io.creditdebt.SearchCreditDebtListReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.adapter.CreditDebtListAdapter;
import com.lsm.barrister2c.ui.adapter.EmptyController;
import com.lsm.barrister2c.ui.adapter.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvshimin on 2016/10/26.
 * 债立方
 */
public class CreditDebtCenter extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    public static CreditDebtCenter newInstance() {
        CreditDebtCenter fragment = new CreditDebtCenter();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit_debt_center,container,false);
        return view;
    }


    AQuery aq;

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    CreditDebtListAdapter mAdapter;

    EmptyController mEmptyController;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aq = new AQuery(view);

        aq.id(R.id.btn_faxian_creditdebt).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = AppConfig.getUser(getContext());

                if (user != null) {
                    UIHelper.goCreditListAcitivity(v.getContext());
                } else {
                    UIHelper.goLoginActivity(getActivity());
                }

            }
        });

        aq.id(R.id.btn_add_creditdebt_info).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goAddCreditDebtActivity(getContext());
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        View emptyView = view.findViewById(R.id.emptyLayout);

        mEmptyController = new EmptyController(mSwipeRefreshLayout, emptyView, new EmptyController.Callback() {

            @Override
            public void doRefresh() {
                onRefresh();
            }
        });

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CreditDebtListAdapter(items, new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public boolean hasMore() {
                return mListReq != null && page + 1 <= mListReq.getTotalPage(total, GetMyOrderListReq.pageSize);
            }
        });

        mAdapter.setEditable(false);
        mAdapter.setCenterList(true);

        mRecyclerView.setAdapter(mAdapter);

        refresh();

    }

    SearchCreditDebtListReq mListReq;

    int page = 1;
    int total;

    List<CreditDebtInfo> items = new ArrayList<>();

    private void refresh() {

        page = 1;

        mListReq = new SearchCreditDebtListReq(getActivity(), page);

        mListReq.execute(new Action.Callback<IO.CreditDebtListResult>() {

            @Override
            public void progress() {
                mSwipeRefreshLayout.setRefreshing(true);
                mEmptyController.showLoading();
            }

            @Override
            public void onError(int errorCode, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
                mEmptyController.showError(errorCode, msg);
//                UIHelper.showToast(getApplicationContext(), msg);
            }


            @Override
            public void onCompleted(IO.CreditDebtListResult result) {
                mSwipeRefreshLayout.setRefreshing(false);

                CreditDebtCenter.this.total = result.total;

                System.out.println("result.total:" + result.total);

                if (result.list != null) {

                    items.clear();
                    items.addAll(result.list);
                    mAdapter.notifyDataSetChanged();

                    mEmptyController.showContent();

                } else {

                    mEmptyController.showMessage("没有购买记录");

                }
            }
        });

    }


    /**
     * 加载更多
     */
    public void loadMore() {

        new SearchCreditDebtListReq(getActivity(), ++page)
                .execute(new Action.Callback<IO.CreditDebtListResult>() {

                    @Override
                    public void progress() {

                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                        --page;

                        UIHelper.showToast(getContext(), msg);
                    }

                    @Override
                    public void onCompleted(IO.CreditDebtListResult result) {

                        if (result.list != null) {

                            items.addAll(result.list);

                            mAdapter.notifyDataSetChanged();

                            mEmptyController.showContent();

                        }
                    }
                });

    }


    @Override
    public void onRefresh() {
        refresh();
    }

}
