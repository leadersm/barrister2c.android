package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.Account;
import com.lsm.barrister2c.data.entity.ConsumeDetail;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetConsumeDetailListReq;
import com.lsm.barrister2c.data.io.app.GetMyAccountReq;
import com.lsm.barrister2c.data.io.app.GetStudyListReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.adapter.IncomeListAdapter;
import com.lsm.barrister2c.ui.adapter.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的账户页
 * 1.余额
 * 2.银行卡信息
 * 3.提现记录
 * 4.入账记录
 */
public class MyAccountActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    GetMyAccountReq mGetAccountReq;
    IO.GetAccountResult result;

    GetConsumeDetailListReq mGetIncomeListReq;
    SwipeRefreshLayout mSwipeRefreshLayout;

    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        setupToolbar();
        aq = new AQuery(this);

        aq.id(R.id.btn_myaccount_bankcard).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(result==null)
//                    return;

                //TODO 银行卡
                UIHelper.goBankcardActivity(MyAccountActivity.this,bankcard);//result.account.getBankCard());
            }
        });

        aq.id(R.id.btn_myaccount_extract).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 提现
                UIHelper.goGetMoneyActivity(MyAccountActivity.this);
            }
        });

        loadAccount();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_income_detail);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new IncomeListAdapter(items, new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public boolean hasMore() {
                return mGetIncomeListReq != null && page + 1 <= mGetIncomeListReq.getTotalPage(total, GetStudyListReq.pageSize);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        loadIncomeList();

    }

    private void loadAccount() {
        mGetAccountReq = new GetMyAccountReq(this);
        mGetAccountReq.execute(new Action.Callback<IO.GetAccountResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getApplicationContext(),msg);
            }

            @Override
            public void onCompleted(IO.GetAccountResult result) {
                MyAccountActivity.this.result = result;
                bind();
            }
        });
    }

    int total;

    private void loadIncomeList() {
        mGetIncomeListReq  = new GetConsumeDetailListReq(this,page = 1);
        mGetIncomeListReq.execute(new Action.Callback<IO.GetConsumeDetailListResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {

                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.showToast(getApplicationContext(),msg);

            }

            @Override
            public void onCompleted(IO.GetConsumeDetailListResult result) {

                mSwipeRefreshLayout.setRefreshing(false);
                total = result.total;

                if(result.consumeDetails!=null){
                    items.clear();
                    items.addAll(result.consumeDetails);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    int page = 1;



    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    IncomeListAdapter mAdapter;

    List<ConsumeDetail> items = new ArrayList<>();

    Account.BankCard bankcard;
    private void bind() {
        aq.id(R.id.tv_myaccount_balance).text(result.account.getRemainingBalance());
        aq.id(R.id.tv_myaccount_total).text(result.account.getTotalConsume());

        bankcard = result.account.getBankCard();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_my_account);
    }

    boolean isLoadingMore = false;
    private void loadMore(){

        if(isLoadingMore)
            return;

        new GetConsumeDetailListReq(this,++page).execute(new Action.Callback<IO.GetConsumeDetailListResult>() {

            @Override
            public void progress() {
                isLoadingMore = true;
            }

            @Override
            public void onError(int errorCode, String msg) {
                isLoadingMore = false;

                --page;
                UIHelper.showToast(getApplicationContext(),msg);
            }

            @Override
            public void onCompleted(IO.GetConsumeDetailListResult result) {
                isLoadingMore = false;

                if(result!=null && result.consumeDetails!=null){
                    items.clear();
                    items.addAll(result.consumeDetails);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadIncomeList();
    }
}
