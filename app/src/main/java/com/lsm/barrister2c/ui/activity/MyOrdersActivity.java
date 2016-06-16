package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.OrderItem;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetMyOrderListReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.adapter.EmptyController;
import com.lsm.barrister2c.ui.adapter.LoadMoreListener;
import com.lsm.barrister2c.ui.adapter.OrderListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单页
 */
public class MyOrdersActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    OrderListAdapter mAdapter;

    EmptyController mEmptyController;

    List<OrderItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        
        setupToolbar();

        init();
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_my_orders);

    }

    private void init(){

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
        android.R.color.holo_red_light, android.R.color.holo_blue_light);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);

        View emptyView = findViewById(R.id.emptyLayout);

        mEmptyController = new EmptyController(mSwipeRefreshLayout, emptyView, new EmptyController.Callback() {

            @Override
            public void doRefresh() {
                onRefresh();
            }
        });

        mLayoutManager = new LinearLayoutManager(MyOrdersActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new OrderListAdapter(items, new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public boolean hasMore() {

                return mGetOrderListReq != null
                        && mRefreshResult!=null
                        && page + 1 <= mGetOrderListReq.getTotalPage(mRefreshResult.total,GetMyOrderListReq.pageSize);
            }
        });


        mRecyclerView.setAdapter(mAdapter);


        refresh();
    }

    GetMyOrderListReq mGetOrderListReq;
    IO.GetMyOrdersResult mRefreshResult;
    int page = 1;

    private void refresh() {

        if(mGetOrderListReq==null){
            mGetOrderListReq = new GetMyOrderListReq(MyOrdersActivity.this,page = 1, GetMyOrderListReq.TYPE_ALL);
        }

        mGetOrderListReq.execute(new Action.Callback<IO.GetMyOrdersResult>() {

            @Override
            public void progress() {
                mEmptyController.showLoading();
                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onError(int errorCode, String msg) {
                mEmptyController.showError(errorCode,msg);
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.showToast(getApplicationContext(),msg);
            }

            @Override
            public void onCompleted(IO.GetMyOrdersResult getMyOrdersResult) {

                mSwipeRefreshLayout.setRefreshing(false);

                mRefreshResult = getMyOrdersResult;

                if(mRefreshResult.orderItems !=null){

                    items.clear();
                    items.addAll(mRefreshResult.orderItems);
                    mAdapter.notifyDataSetChanged();

                    mEmptyController.showContent();

                }else {

                    mEmptyController.showEmpty();

                }
            }
        });

    }

    /**
     * 加载更多
     */
    public void loadMore(){

        new GetMyOrderListReq(MyOrdersActivity.this, ++page, GetMyOrderListReq.TYPE_ALL)
                .execute(new Action.Callback<IO.GetMyOrdersResult>() {

                    @Override
                    public void progress() {

                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                        --page;

                        UIHelper.showToast(getApplicationContext(),msg);
                    }

                    @Override
                    public void onCompleted(IO.GetMyOrdersResult getMyOrdersResult) {

                        if(getMyOrdersResult.orderItems !=null){

                            items.addAll(getMyOrdersResult.orderItems);

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
