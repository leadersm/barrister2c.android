package com.lsm.barrister2c.ui.activity.creditdebt;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetMyOrderListReq;
import com.lsm.barrister2c.data.io.creditdebt.MyPurchasedCreditDebtListReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.activity.BaseActivity;
import com.lsm.barrister2c.ui.adapter.CreditDebtListAdapter;
import com.lsm.barrister2c.ui.adapter.EmptyController;
import com.lsm.barrister2c.ui.adapter.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvshimin on 16/10/10.
 * 我购买的债权债务信息
 */
public class MyPurchasedCreditDebtListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchased_credit);
        aq = new AQuery(this);

        setupToolbar();

        init();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_purchased_creditdebt);
    }

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    CreditDebtListAdapter mAdapter;

    EmptyController mEmptyController;

    private void init() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        View emptyView = findViewById(R.id.emptyLayout);

        mEmptyController = new EmptyController(mSwipeRefreshLayout, emptyView, new EmptyController.Callback() {

            @Override
            public void doRefresh() {
                onRefresh();
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
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

        mRecyclerView.setAdapter(mAdapter);

        refresh();

    }

    MyPurchasedCreditDebtListReq mListReq;

    int page = 1;
    int total;

    List<CreditDebtInfo> items = new ArrayList<>();

    private void refresh() {

        page = 1;

        mListReq = new MyPurchasedCreditDebtListReq(this, page);

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

                MyPurchasedCreditDebtListActivity.this.total = result.total;

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

        new MyPurchasedCreditDebtListReq(this, ++page)
                .execute(new Action.Callback<IO.CreditDebtListResult>() {

                    @Override
                    public void progress() {

                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                        --page;

                        UIHelper.showToast(getApplicationContext(), msg);
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
