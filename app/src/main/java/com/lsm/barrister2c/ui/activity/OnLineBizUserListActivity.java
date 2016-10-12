package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.BusinessType;
import com.lsm.barrister2c.data.entity.OnlineBizUser;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetOnlineBizUserListReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.adapter.LoadMoreListener;
import com.lsm.barrister2c.ui.adapter.OnLineBizUserListAdapter;
import com.lsm.barrister2c.utils.DLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线业务人员列表
 */
public class OnLineBizUserListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    AQuery aq;

    BusinessType item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biz_userlist);
        item = (BusinessType) getIntent().getSerializableExtra("item");

        aq = new AQuery(this);

        setupToolbar();

        init();
    }

    List<OnlineBizUser> items = new ArrayList<>();

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_online_list);
    }

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    OnLineBizUserListAdapter mAdapter;

    private void init() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new OnLineBizUserListAdapter(items, new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public boolean hasMore() {
                return mListReq != null  && page + 1 <= mListReq.getTotalPage(total, GetOnlineBizUserListReq.pageSize);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        aq.id(R.id.btn_biz_doc).text(Html.fromHtml("<u>"+item.getDocTitle()+"</u>")).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goDocActivity(OnLineBizUserListActivity.this,item.getDocTitle(),item.getDocUrl());
            }
        });

        refresh();
    }

    GetOnlineBizUserListReq mListReq;

    int page = 1;
    int total;

    boolean isLoading = false;

    private void refresh() {

        if(isLoading && mListReq!=null && mListReq.isLoading()){
            mListReq.cancel();
        }

        mListReq = new GetOnlineBizUserListReq(this, item.getId());

        mListReq.execute(new Action.Callback<IO.GetOnlineBizUserListResult>() {

            @Override
            public void progress() {
                isLoading = true;
                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onError(int errorCode, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                UIHelper.showToast(getApplicationContext(), msg);
            }

            @Override
            public void onCompleted(IO.GetOnlineBizUserListResult result) {
                isLoading = false;

                mSwipeRefreshLayout.setRefreshing(false);

                OnLineBizUserListActivity.this.total = result.total;

                DLog.d("search","result.total:"+result.total);

                if (result.list != null) {

                    items.clear();
                    items.addAll(result.list);
                    mAdapter.notifyDataSetChanged();

                }
            }
        });

    }


    /**
     * 加载更多
     */
    public void loadMore() {

        new GetOnlineBizUserListReq(this, item.getId())
                .execute(new Action.Callback<IO.GetOnlineBizUserListResult>() {

                    @Override
                    public void progress() {

                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                        --page;

                        UIHelper.showToast(getApplicationContext(), msg);
                    }

                    @Override
                    public void onCompleted(IO.GetOnlineBizUserListResult result) {

                        if (result.list != null) {

                            items.addAll(result.list);

                            mAdapter.notifyDataSetChanged();

                        }
                    }
                });

    }


    @Override
    public void onRefresh() {
        refresh();
    }
}
