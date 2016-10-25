package com.lsm.barrister2c.ui.activity.creditdebt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetMyOrderListReq;
import com.lsm.barrister2c.data.io.creditdebt.DelCreditDebtInfoReq;
import com.lsm.barrister2c.data.io.creditdebt.MyUploadCreditDebtListReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.activity.BaseActivity;
import com.lsm.barrister2c.ui.adapter.CreditDebtListAdapter;
import com.lsm.barrister2c.ui.adapter.EmptyController;
import com.lsm.barrister2c.ui.adapter.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvshimin on 16/10/10.
 * 我上传的
 */
public class MyUploadCreditDebtListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_upload_credit);
        aq = new AQuery(this);

        setupToolbar();

        init();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_upload_creditdebt);
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

        mAdapter.setEditable(true);

        mRecyclerView.setAdapter(mAdapter);

        refresh();

    }

    int selectIndex;

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        selectIndex = mAdapter.getSelectIndex();

        if (item.getItemId() == 1) {
            //删除
            showDelDialog(selectIndex);
        }else if(item.getItemId() == 0){
            //修改

        }

        return super.onContextItemSelected(item);
    }

    private void showDelDialog(final int selectIndex) {

        new AlertDialog.Builder(this).setTitle(R.string.tip)
                .setMessage(getString(R.string.tip_del_debtinfo))
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doDelete(selectIndex);
            }
        }).create().show();
    }

    private void doDelete(final int selectIndex) {
        new DelCreditDebtInfoReq(this,items.get(selectIndex).getId())
                .execute(new Action.Callback<Boolean>() {
                    @Override
                    public void progress() {

                    }

                    @Override
                    public void onError(int errorCode, String msg) {
                        UIHelper.showToast(getApplicationContext(),msg);
                    }

                    @Override
                    public void onCompleted(Boolean aBoolean) {
                        UIHelper.showToast(getApplicationContext(),R.string.tip_del_success);
                        items.remove(selectIndex);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    MyUploadCreditDebtListReq mListReq;

    int page = 1;
    int total;

    List<CreditDebtInfo> items = new ArrayList<>();

    private void refresh() {

        page = 1;

        mListReq = new MyUploadCreditDebtListReq(this, page);

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

                MyUploadCreditDebtListActivity.this.total = result.total;

                System.out.println("result.total:" + result.total);

                if (result.list != null) {

                    items.clear();
                    items.addAll(result.list);
                    mAdapter.notifyDataSetChanged();

                    mEmptyController.showContent();

                } else {

                    mEmptyController.showMessage("您还没有上传记录");

                }
            }
        });

    }


    /**
     * 加载更多
     */
    public void loadMore() {

        new MyUploadCreditDebtListReq(this, ++page)
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
