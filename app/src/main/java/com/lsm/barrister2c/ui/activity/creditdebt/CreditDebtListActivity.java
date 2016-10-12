package com.lsm.barrister2c.ui.activity.creditdebt;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.entity.CreditDebtUser;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetMyOrderListReq;
import com.lsm.barrister2c.data.io.creditdebt.SearchCreditDebtListReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.activity.BaseActivity;
import com.lsm.barrister2c.ui.adapter.CreditDebtListAdapter;
import com.lsm.barrister2c.ui.adapter.EmptyController;
import com.lsm.barrister2c.ui.adapter.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvshimin on 16/10/10.
 * 账款库列表,搜索,添加
 */
public class CreditDebtListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_list);
        aq = new AQuery(this);

        setupToolbar();

        init();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_credit_search);
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

        aq.id(R.id.btn_search).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });

    }

    SearchCreditDebtListReq mListReq;

    int page = 1;
    int total;

    List<CreditDebtInfo> items = new ArrayList<>();

    String userType;
    String keytype ;
    String key;

    //公司名称company、机构代码licenseNum、姓名name、身份证 idNum
    private void doSearch() {

        userType = aq.id(R.id.rb_user_credit).isChecked() ? CreditDebtUser.TYPE_CREDIT : CreditDebtUser.TYPE_DEBT;

        if (aq.id(R.id.rb_key_company).isChecked()) {
            keytype = "company";
        } else if (aq.id(R.id.rb_key_company).isChecked()) {
            keytype = "licenseNum";
        } else {
            keytype = "idNum";
        }

        page = 1;

        Editable editable = aq.id(R.id.et_search).getEditable();
        if (TextUtils.isEmpty(editable)) {
            UIHelper.showToast(getApplicationContext(), "请输入查询信息");
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        key = editable.toString();

        mListReq = new SearchCreditDebtListReq(this, ++page, key, keytype, userType);

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

                CreditDebtListActivity.this.total = result.total;

                System.out.println("result.total:" + result.total);

                if (result.list != null) {

                    items.clear();
                    items.addAll(result.list);
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

        new SearchCreditDebtListReq(this, ++page, key, keytype, userType)
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
        doSearch();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_credit_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {
            UIHelper.goAddCreditDebtActivity(this);
            return true;
        }/*else if (item.getItemId() == R.id.action_search) {
            UIHelper.goSearchCreditActivity(this);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

}
