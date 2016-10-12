package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
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
import com.lsm.barrister2c.utils.DLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 专家咨询列表
 */
public class ExpertListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expert_list);

        aq = new AQuery(this);

        setupToolbar();

        init();
    }

    List<Barrister> items = new ArrayList<>();

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_expert_list);
    }

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EmptyController mEmptyController;
    BarristerAdapter mAdapter;

    private void init() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

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
                return mListReq != null && page + 1 <= mListReq.getTotalPage(total, GetMyOrderListReq.pageSize);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        refresh();
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    GetBarristerListReq mListReq;

    int page = 1;
    int total;

    boolean isLoading = false;

    private void refresh() {

        if (isLoading && mListReq != null && mListReq.isLoading()) {
            mListReq.cancel();
        }

        mListReq = new GetBarristerListReq(this, true, page = 1);

        mListReq.execute(new Action.Callback<IO.GetBarristerListResult>() {

            @Override
            public void progress() {

                isLoading = true;
                mSwipeRefreshLayout.setRefreshing(true);
                mEmptyController.showLoading();

            }

            @Override
            public void onError(int errorCode, String msg) {

                isLoading = false;
                UIHelper.showToast(getApplicationContext(), msg);
                mSwipeRefreshLayout.setRefreshing(false);
                mEmptyController.showError(errorCode, msg);

            }

            @Override
            public void onCompleted(final IO.GetBarristerListResult result) {

                if (result != null && !TextUtils.isEmpty(result.docUrl)) {

                    final String title = getString(R.string.title_expert_doc);

                    aq.id(R.id.btn_expert_consume_doc).text(Html.fromHtml("<u>" + title + "</u>")).clicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UIHelper.goDocActivity(ExpertListActivity.this, title, result.docUrl);
                        }
                    }).visible();
                }

                isLoading = false;

                mSwipeRefreshLayout.setRefreshing(false);

                ExpertListActivity.this.total = result.total;

                DLog.d("search", "result.total:" + result.total);

                if (result.items != null) {

                    items.clear();
                    items.addAll(result.items);
                    mAdapter.notifyDataSetChanged();

                    mEmptyController.showContent();

                } else {

                    mEmptyController.showEmpty();

                }
            }
        });

    }


    /**
     * 加载更多
     */
    public void loadMore() {

        new GetBarristerListReq(this, true, ++page)
                .execute(new Action.Callback<IO.GetBarristerListResult>() {

                    @Override
                    public void progress() {

                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                        --page;

                        UIHelper.showToast(getApplicationContext(), msg);
                    }

                    @Override
                    public void onCompleted(IO.GetBarristerListResult result) {

                        if (result.items != null) {

                            items.addAll(result.items);

                            mAdapter.notifyDataSetChanged();

                        }
                    }
                });

    }


}
