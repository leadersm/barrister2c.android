package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.db.Favorite;
import com.lsm.barrister2c.data.db.UserDbService;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetMyFavoriteListReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.adapter.FavoriteAdapter;
import com.lsm.barrister2c.ui.adapter.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的收藏页
 */
public class MyFavoriteActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    FavoriteAdapter mAdapter;

    List<Favorite> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collections);
        setupToolbar();

        init();

    }
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_favorite);
    }

    private void init(){

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(MyFavoriteActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FavoriteAdapter(items, new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public boolean hasMore() {

                return mGetFavoriteListReq != null
                        && mRefreshResult!=null
                        && page + 1 <= mGetFavoriteListReq.getTotalPage(mRefreshResult.total, GetMyFavoriteListReq.pageSize);
            }
        });


        mRecyclerView.setAdapter(mAdapter);


        refresh();
    }

    GetMyFavoriteListReq mGetFavoriteListReq;
    IO.GetMyFavoriteListResult mRefreshResult;
    int page = 1;

    private void refresh() {

        List<Favorite> favorites = UserDbService.getInstance(this).getFavoriteAction().loadAll();

        if(favorites!=null){
            items.clear();
            items.addAll(favorites);
            mAdapter.notifyDataSetChanged();
        }

        if(mGetFavoriteListReq==null){
            mGetFavoriteListReq = new GetMyFavoriteListReq(MyFavoriteActivity.this,page = 1);
        }

        mGetFavoriteListReq.execute(new Action.Callback<IO.GetMyFavoriteListResult>() {

            @Override
            public void progress() {
                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onError(int errorCode, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.showToast(getApplicationContext(),msg);
            }

            @Override
            public void onCompleted(IO.GetMyFavoriteListResult result) {

                mSwipeRefreshLayout.setRefreshing(false);

                mRefreshResult = result;

                if(mRefreshResult.favoriteItemList !=null){

                    items.clear();
                    items.addAll(mRefreshResult.favoriteItemList);
                    mAdapter.notifyDataSetChanged();

                }
            }
        });

    }

    /**
     * 加载更多
     */
    public void loadMore(){

        new GetMyFavoriteListReq(this, ++page)
                .execute(new Action.Callback<IO.GetMyFavoriteListResult>() {

                    @Override
                    public void progress() {

                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                        --page;

                        UIHelper.showToast(getApplicationContext(),msg);
                    }

                    @Override
                    public void onCompleted(IO.GetMyFavoriteListResult result) {

                        if(result.favoriteItemList !=null){

                            items.addAll(result.favoriteItemList);

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
