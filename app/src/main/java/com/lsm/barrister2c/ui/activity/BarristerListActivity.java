package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.Barrister;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetBarristerListReq;
import com.lsm.barrister2c.data.io.app.GetMyOrderListReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.adapter.BarristerAdapter;
import com.lsm.barrister2c.ui.adapter.EmptyController;
import com.lsm.barrister2c.ui.adapter.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class BarristerListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final String KEY_TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barrister_list);
        setupToolbar();

        init();
    }

    List<Barrister> items = new ArrayList<>();

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_barrister_list);
    }

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    BarristerAdapter mAdapter;

    EmptyController mEmptyController;

    private void init(){

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
        mAdapter = new BarristerAdapter(items, new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public boolean hasMore() {

                return mGetOrderListReq != null
                        && mRefreshResult!=null
                        && page + 1 <= mGetOrderListReq.getTotalPage(mRefreshResult.total, GetMyOrderListReq.pageSize);
            }
        });


        mRecyclerView.setAdapter(mAdapter);


        refresh();
    }

    GetBarristerListReq mGetOrderListReq;
    IO.GetBarristerListResult mRefreshResult;
    int page = 1;

    private void refresh() {

        if(mGetOrderListReq==null){
            mGetOrderListReq = new GetBarristerListReq(this,page = 1, type);
        }

        mGetOrderListReq.execute(new Action.Callback<IO.GetBarristerListResult>() {

            @Override
            public void progress() {
                mSwipeRefreshLayout.setRefreshing(true);
                mEmptyController.showLoading();
            }

            @Override
            public void onError(int errorCode, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
                mEmptyController.showError(errorCode,msg);
                UIHelper.showToast(getApplicationContext(),msg);
            }


            @Override
            public void onCompleted(IO.GetBarristerListResult getMyOrdersResult) {
                mSwipeRefreshLayout.setRefreshing(false);

                mRefreshResult = getMyOrdersResult;

                if(mRefreshResult.barristerList !=null){

                    items.clear();
                    items.addAll(mRefreshResult.barristerList);
                    mAdapter.notifyDataSetChanged();

                    mEmptyController.showContent();

                }else {

                    mEmptyController.showEmpty();

                }
            }
        });

    }


    String type = GetBarristerListReq.TYPE_APPOINTMENT;

    /**
     * 加载更多
     */
    public void loadMore(){

        new GetMyOrderListReq(this, ++page, type)
                .execute(new Action.Callback<IO.GetBarristerListResult>() {

                    @Override
                    public void progress() {

                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                        --page;

                        UIHelper.showToast(getApplicationContext(),msg);
                    }

                    @Override
                    public void onCompleted(IO.GetBarristerListResult getMyOrdersResult) {

                        if(getMyOrdersResult.barristerList !=null){

                            items.addAll(getMyOrdersResult.barristerList);

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
