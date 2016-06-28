package com.lsm.barrister2c.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.BizHelper;
import com.lsm.barrister2c.data.entity.Barrister;
import com.lsm.barrister2c.data.entity.BusinessArea;
import com.lsm.barrister2c.data.entity.BusinessType;
import com.lsm.barrister2c.data.entity.Filter;
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

public class BarristerListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String KEY_TYPE = "type";
    public static final String KEY_BIZ_TYPE = "biz.type";
    public static final String KEY_BIZ_AREA = "biz.area";

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
        mAdapter = new BarristerAdapter(items, new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public boolean hasMore() {

                return mListReq != null
                        && mRefreshResult != null
                        && page + 1 <= mListReq.getTotalPage(mRefreshResult.total, GetMyOrderListReq.pageSize);
            }
        });


        mRecyclerView.setAdapter(mAdapter);


        refresh();
    }

    GetBarristerListReq mListReq;
    IO.GetBarristerListResult mRefreshResult;
    int page = 1;

    private void refresh() {


        String bizArea = filterBizArea == null ? null : filterBizArea.getId();
        String bizType = filterBizType == null ? null : filterBizType.getId();
        String year = filterYear == null ? null : filterYear.getId();

        mListReq = new GetBarristerListReq(this, page = 1, type, bizArea, bizType, year);

        mListReq.execute(new Action.Callback<IO.GetBarristerListResult>() {

            @Override
            public void progress() {
                mSwipeRefreshLayout.setRefreshing(true);
                mEmptyController.showLoading();
            }

            @Override
            public void onError(int errorCode, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
                mEmptyController.showError(errorCode, msg);
                UIHelper.showToast(getApplicationContext(), msg);
            }


            @Override
            public void onCompleted(IO.GetBarristerListResult getMyOrdersResult) {
                mSwipeRefreshLayout.setRefreshing(false);

                mRefreshResult = getMyOrdersResult;

                if (mRefreshResult.items != null) {

                    items.clear();
                    items.addAll(mRefreshResult.items);
                    mAdapter.notifyDataSetChanged();

                    mEmptyController.showContent();

                } else {

                    mEmptyController.showEmpty();

                }
            }
        });

    }


    String type = GetBarristerListReq.TYPE_APPOINTMENT;

    /**
     * 加载更多
     */
    public void loadMore() {

        new GetMyOrderListReq(this, ++page, type)
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
                    public void onCompleted(IO.GetBarristerListResult getMyOrdersResult) {

                        if (getMyOrdersResult.items != null) {

                            items.addAll(getMyOrdersResult.items);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_barrister_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            showFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Dialog filterDialog;

    List<BusinessArea> bizAreas = new ArrayList<>();
    List<BusinessType> bizTypes = new ArrayList<>();

    List<Filter> years = new ArrayList<>();
    List<Filter> areas = new ArrayList<>();

    private void initFilters() {
        bizAreas = BizHelper.getInstance().getBizAreas();
        bizTypes = BizHelper.getInstance().getBizTypes();
        years = BizHelper.getInstance().getYears();
        areas = BizHelper.getInstance().getAreas();
    }

    List<Filter> filters = new ArrayList<>();

    ArrayAdapter<Filter> aa;

    int cFilter = 0;

    private static final int FILTER_YEAR = 0;
    private static final int FILTER_AREA = 1;
    private static final int FILTER_BIZ_AREA = 2;
    private static final int FILTER_BIZ_TYPE = 3;

    Filter filterYear, filterArea, filterBizArea, filterBizType;

    private void showFilterDialog() {

        if (filterDialog == null) {

            initFilters();

            View view = getLayoutInflater().inflate(R.layout.dialog_filter, null);

            filterDialog = new Dialog(this, R.style.CalendarDialogTheme);
            filterDialog.setContentView(view);

            WindowManager.LayoutParams lp = filterDialog.getWindow().getAttributes();
            lp.gravity = Gravity.TOP;
            lp.width = -1;

            final AQuery dialogQuery = new AQuery(view);

            aa = new ArrayAdapter<Filter>(this, R.layout.item_filter, R.id.tv_filter, filters) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(R.id.tv_filter);
                    tv.setText(getItem(position).getName());
                    return view;
                }
            };

            final GridView gridView = dialogQuery.id(R.id.listview_filter).adapter(aa).itemClicked(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Filter filter = filters.get(position);

                    if (cFilter == FILTER_AREA) {
                        filterArea = filter;
                    } else if (cFilter == FILTER_YEAR) {
                        filterYear = filter;
                    } else if (cFilter == FILTER_BIZ_AREA) {
                        filterBizArea = filter;
                    } else {
                        filterBizType = filter;
                    }

                    updateFilterStatus(dialogQuery);

                }
            }).getGridView();

            dialogQuery.id(R.id.btn_filter_year).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cFilter = FILTER_YEAR;
                    filters.clear();
                    filters.addAll(years);
                    gridView.setNumColumns(3);
                    aa.notifyDataSetChanged();
                }
            });
            dialogQuery.id(R.id.btn_filter_area).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cFilter = FILTER_AREA;
                    filters.clear();
                    filters.addAll(areas);
                    gridView.setNumColumns(4);
                    aa.notifyDataSetChanged();
                }
            });
            dialogQuery.id(R.id.btn_filter_biz_area).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cFilter = FILTER_BIZ_AREA;
                    if (bizAreas != null) {

                        filters.clear();
                        filters.addAll(bizAreas);
                        gridView.setNumColumns(3);
                        aa.notifyDataSetChanged();
                    }
                }
            });
            dialogQuery.id(R.id.btn_filter_biz_type).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cFilter = FILTER_BIZ_TYPE;
                    if (bizTypes != null) {

                        filters.clear();
                        filters.addAll(bizTypes);
                        gridView.setNumColumns(3);
                        aa.notifyDataSetChanged();
                    }
                }
            });

            dialogQuery.id(R.id.btn_filter_clear).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterArea = null;
                    filterYear = null;
                    filterBizType = null;
                    filterBizArea = null;
                    updateFilterStatus(dialogQuery);
                }
            });
            dialogQuery.id(R.id.btn_filter_ok).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterDialog.dismiss();
                    refresh();
                }
            });

            cFilter = FILTER_AREA;
            filters.clear();
            filters.addAll(areas);
            aa.notifyDataSetChanged();

            updateFilterStatus(dialogQuery);

        }

        filterDialog.show();
    }

    private void updateFilterStatus(AQuery dialogQuery) {

        String year = "年限:" + (filterYear == null ? "不限" : filterYear.getName());
        String area = "地区:" + (filterArea == null ? "不限" : filterArea.getName());
        String bizArea = "领域:" + (filterBizArea == null ? "不限" : filterBizArea.getName());
        String bizType = "业务:" + (filterBizType == null ? "不限" : filterBizType.getName());

        dialogQuery.id(R.id.tv_filter_filters).text(year + "," + area + "," + bizArea + "," + bizType);

    }

}
