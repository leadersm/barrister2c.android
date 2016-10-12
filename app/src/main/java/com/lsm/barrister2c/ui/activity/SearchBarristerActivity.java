package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.Barrister;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetMyOrderListReq;
import com.lsm.barrister2c.data.io.app.SearchBarristerReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.adapter.BarristerAdapter;
import com.lsm.barrister2c.ui.adapter.LoadMoreListener;
import com.lsm.barrister2c.utils.DLog;

import java.util.ArrayList;
import java.util.List;

public class SearchBarristerActivity extends BaseActivity {

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        aq = new AQuery(this);

        setupToolbar();

        init();
    }

    List<Barrister> items = new ArrayList<>();

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_barrister_search);
    }

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    BarristerAdapter mAdapter;

    private void init() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

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
                return mListReq != null  && page + 1 <= mListReq.getTotalPage(total, GetMyOrderListReq.pageSize);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        aq.id(R.id.btn_search).clicked(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                doSearch();
            }
        });

    }

    SearchBarristerReq mListReq;

    int page = 1;
    int total;

    Editable keywords;

    boolean isLoading = false;

    private void doSearch() {

        if(isLoading && mListReq!=null && mListReq.isLoading()){
            mListReq.cancel();
        }

        keywords = aq.id(R.id.et_search).getEditable();

        if(TextUtils.isEmpty(keywords)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_keywords_empty));
            return;
        }

        mListReq = new SearchBarristerReq(this, keywords.toString(), page = 1);

        mListReq.execute(new Action.Callback<IO.GetBarristerListResult>() {

            @Override
            public void progress() {
                isLoading = true;
            }

            @Override
            public void onError(int errorCode, String msg) {
                isLoading = false;
                UIHelper.showToast(getApplicationContext(), msg);
            }

            @Override
            public void onCompleted(IO.GetBarristerListResult result) {
                isLoading = false;

                SearchBarristerActivity.this.total = result.total;

                DLog.d("search","result.total:"+result.total);

                if (result.items != null) {

                    items.clear();
                    items.addAll(result.items);
                    mAdapter.notifyDataSetChanged();

                }
            }
        });

    }


    /**
     * 加载更多
     */
    public void loadMore() {

        new SearchBarristerReq(this,keywords.toString(), ++page)
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
