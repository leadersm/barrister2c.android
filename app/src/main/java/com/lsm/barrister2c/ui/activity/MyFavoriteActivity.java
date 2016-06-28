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

//    EmptyController mEmptyController;

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

//        View emptyView = findViewById(R.id.emptyLayout);
//
//        mEmptyController = new EmptyController(mSwipeRefreshLayout, emptyView, new EmptyController.Callback() {
//
//            @Override
//            public void doRefresh() {
//                onRefresh();
//            }
//        });

        mLayoutManager = new LinearLayoutManager(MyFavoriteActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FavoriteAdapter(items, new LoadMoreListener() {

            @Override
            public void onLoadMore() {
//                loadMore();
            }

            @Override
            public boolean hasMore() {

//                return mGetFavoriteListReq != null
//                        && mRefreshResult!=null
//                        && page + 1 <= mGetFavoriteListReq.getTotalPage(mRefreshResult.total, GetMyOrderListReq.pageSize);
                return false;
            }
        });


        mRecyclerView.setAdapter(mAdapter);


        refresh();
    }

//    GetMyFavoriteListReq mGetFavoriteListReq;
//    IO.GetMyOrdersResult mRefreshResult;
    int page = 1;

    private void refresh() {

        List<Favorite> favorites = UserDbService.getInstance(this).getFavoriteAction().loadAll();

        if(favorites!=null){
            items.clear();
            items.addAll(favorites);
            mAdapter.notifyDataSetChanged();
        }

//        if(mGetFavoriteListReq==null){
//            mGetFavoriteListReq = new GetMyFavoriteListReq(MyFavoriteActivity.this,page = 1, GetMyOrderListReq.TYPE_ALL);
//        }
//
//        mGetFavoriteListReq.execute(new Action.Callback<IO.GetMyFavoriteResult>() {
//
//            @Override
//            public void progress() {
////                mEmptyController.showLoading();
//                mSwipeRefreshLayout.setRefreshing(true);
//            }
//
//            @Override
//            public void onError(int errorCode, String msg) {
////                mEmptyController.showError(errorCode,msg);
//                mSwipeRefreshLayout.setRefreshing(false);
//                UIHelper.showToast(getApplicationContext(),msg);
//            }
//
//            @Override
//            public void onCompleted(IO.GetMyFavoriteResult getMyOrdersResult) {
//
//                mSwipeRefreshLayout.setRefreshing(false);
//
//                mRefreshResult = getMyOrdersResult;
//
//                if(mRefreshResult.orderItems !=null){
//
//
//
////                    mEmptyController.showContent();
//
//                }else {
//
////                    mEmptyController.showEmpty();
//
//                }
//            }
//        });

    }

    /**
     * 加载更多
     */
//    public void loadMore(){
//
//        new GetMyFavoriteListReq(MyFavoriteActivity.this, ++page, GetMyOrderListReq.TYPE_ALL)
//                .execute(new Action.Callback<IO.GetMyFavoriteResult>() {
//
//                    @Override
//                    public void progress() {
//
//                    }
//
//                    @Override
//                    public void onError(int errorCode, String msg) {
//
//                        --page;
//
//                        UIHelper.showToast(getApplicationContext(),msg);
//                    }
//
//                    @Override
//                    public void onCompleted(IO.GetMyFavoriteResult getMyOrdersResult) {
//
//                        if(getMyOrdersResult.orderItems !=null){
//
//                            items.addAll(getMyOrdersResult.orderItems);
//
//                            mAdapter.notifyDataSetChanged();
//
////                            mEmptyController.showContent();
//
//                        }
//                    }
//                });
//
//    }

    @Override
    public void onRefresh() {
        refresh();
    }

}
