package com.lsm.barrister2c.ui.activity.creditdebt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RadioGroup;

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
import com.lsm.barrister2c.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lvshimin on 16/10/10.
 * 账款库列表,搜索,添加
 */
public class CreditDebtListActivity extends BaseActivity {

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_list);
        aq = new AQuery(this);

        setupToolbar();

        init();
    }

    Dialog mSearchDialog;

    AQuery dialog;

    private void initSearchDialog() {

        mSearchDialog = new Dialog(this, R.style.CalendarDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.layout_search_credit, null);
        mSearchDialog.setContentView(view);
        dialog = new AQuery(view);

        WindowManager.LayoutParams lp = mSearchDialog.getWindow().getAttributes();
        lp.gravity = Gravity.TOP;
        lp.width = -1;

        dialog.id(R.id.btn_search).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });

        dialog.id(R.id.btn_advance).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHideOrShowAdvance();
            }
        });

        dialog.id(R.id.et_search_starttime).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(v.getId());
            }
        });

        dialog.id(R.id.et_search_endtime).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(v.getId());
            }
        });
    }

    Calendar cal;

    private void showDateTimePicker(final int id) {

        cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date time = cal.getTime();
                String date = DateFormatUtils.format(time, "yyyy-MM-dd");

                Log.d("onDatePick", "pickdate:" + date);
                dialog.id(id).text(date);

            }
        }, year, month, dayOfMonth).show();
    }

    /**
     * 清空搜索条件
     */
    private void clearSearchCondition() {

    }

    /**
     * 显示或隐藏高级条件
     */
    private void doHideOrShowAdvance() {
        if (dialog != null) {

            int visibility = dialog.id(R.id.layout_search_advance).getView().getVisibility();

            if (visibility == View.VISIBLE) {
                dialog.id(R.id.layout_search_advance).gone();
            }else{
                dialog.id(R.id.layout_search_advance).visible();
            }

        }

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_credit_search);
    }

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    CreditDebtListAdapter mAdapter;

    EmptyController mEmptyController;

    private void init() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        View emptyView = findViewById(R.id.emptyLayout);

        mEmptyController = new EmptyController(mRecyclerView, emptyView, new EmptyController.Callback() {

            @Override
            public void doRefresh() {
                doSearch();
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

        aq.id(R.id.btn_search_dialog).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchDialog.show();
            }
        });

        initSearchDialog();

    }

    SearchCreditDebtListReq mListReq;

    int page = 1;
    int total;

    List<CreditDebtInfo> items = new ArrayList<>();

    String userType;
    String keytype;
    String key;

    //金额范围，
    String startMoney;
    String endMoney;
    //时间范围
    String startDate;
    String endDate;
    //债状态，
    String creditDebtStatus;
    // area地区
    String area;

    //公司名称company、机构代码licenseNum、姓名name、身份证 idNum
    //高级条件：金额范围，startMoney,endMoney,时间范围startDate,endDate,creditDebtStatus 债状态，area地区
    private void doSearch() {

        mSearchDialog.dismiss();

        userType = dialog.id(R.id.rb_user_credit).isChecked() ? CreditDebtUser.TYPE_CREDIT : CreditDebtUser.TYPE_DEBT;

        if (dialog.id(R.id.rb_key_company).isChecked()) {
            keytype = "company";
        } else if (dialog.id(R.id.rb_key_license_num).isChecked()) {
            keytype = "licenseNum";
        } else {
            keytype = "idNum";
        }

        page = 1;

        Editable editable = dialog.id(R.id.et_search).getEditable();
        if (TextUtils.isEmpty(editable)) {
            UIHelper.showToast(getApplicationContext(), "请输入查询信息");
            return;
        }

        key = editable.toString();

        if (dialog != null) {

            int visibility = dialog.id(R.id.layout_search_advance).getView().getVisibility();

            if (visibility == View.VISIBLE) {

                Editable startDateEditable = dialog.id(R.id.et_search_starttime).getEditable();
                Editable endDateEditable = dialog.id(R.id.et_search_endtime).getEditable();

                Editable startMoneyEditable = dialog.id(R.id.et_search_starttime).getEditable();
                Editable endMoneyEditable = dialog.id(R.id.et_search_endtime).getEditable();
                //地区
                Editable areaEditable = dialog.id(R.id.et_search_area).getEditable();

                RadioGroup groupStatus = (RadioGroup) dialog.id(R.id.group_status).getView();

                //债状态
                int statusId = groupStatus.getCheckedRadioButtonId();

                if (statusId == R.id.rb_status_no) {
                    //不限制
                    creditDebtStatus = null;
                } else if (statusId == R.id.rb_status_not_sue) {
                    //未起诉
                    creditDebtStatus = CreditDebtInfo.CREDIT_DEBT_STATUS_NOT_SUE;
                } else if (statusId == R.id.rb_status_judging) {
                    //起诉中
                    creditDebtStatus = CreditDebtInfo.CREDIT_DEBT_STATUS_JUDGING;
                } else if (statusId == R.id.rb_status_suing) {
                    //执行中
                    creditDebtStatus = CreditDebtInfo.CREDIT_DEBT_STATUS_SUING;
                } else {
                    //已过期
                    creditDebtStatus = CreditDebtInfo.CREDIT_DEBT_STATUS_OUT_OF_DATE;
                }

                if (!TextUtils.isEmpty(startDateEditable)) {
                    startDate = startDateEditable.toString();
                }
                if (!TextUtils.isEmpty(endDateEditable)) {
                    endDate = endDateEditable.toString();
                }
                if (!TextUtils.isEmpty(startMoneyEditable)) {
                    startMoney = startMoneyEditable.toString();
                }
                if (!TextUtils.isEmpty(endMoneyEditable)) {
                    endMoney = endMoneyEditable.toString();
                }
                if (!TextUtils.isEmpty(areaEditable)) {
                    area = areaEditable.toString();
                }

                if (startDate != null && endDate != null) {
                    Date start = DateFormatUtils.parse(startDate, "yyyy-MM-dd");
                    Date end = DateFormatUtils.parse(endDate, "yyyy-MM-dd");
                    if (start.after(end)) {
                        UIHelper.showToast(getApplicationContext(), "开始时间不能晚于结束时间");
                        return;
                    }
                }

                if (startMoney != null && endMoney != null) {
                    float start = Float.parseFloat(startMoney);
                    float end = Float.parseFloat(endMoney);
                    if (start < end) {
                        UIHelper.showToast(getApplicationContext(), "金额范围填写不正确");
                        return;
                    }
                }

                //高级搜索
                mListReq = new SearchCreditDebtListReq(this, page, key, keytype, userType, startMoney, endMoney, startDate, endDate, creditDebtStatus, area);

            } else {

                startMoney = null;
                endMoney = null;
                //时间范围
                startDate = null;
                endDate = null;
                //债状态，
                creditDebtStatus = null;
                // area地区
                area = null;

                //普通搜索
                mListReq = new SearchCreditDebtListReq(this, page, key, keytype, userType);

            }
        }

        mListReq.execute(new Action.Callback<IO.CreditDebtListResult>() {

            @Override
            public void progress() {
                mEmptyController.showLoading();
            }

            @Override
            public void onError(int errorCode, String msg) {
                mEmptyController.showError(errorCode, msg);
//                UIHelper.showToast(getApplicationContext(), msg);
            }


            @Override
            public void onCompleted(IO.CreditDebtListResult result) {

                CreditDebtListActivity.this.total = result.total;

                System.out.println("result.total:" + result.total);

                if (result.list != null) {

                    items.clear();
                    items.addAll(result.list);
                    mAdapter.notifyDataSetChanged();

                    mEmptyController.showContent();

                } else {

                    mEmptyController.showMessage("没有找到您要的信息记录");

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
