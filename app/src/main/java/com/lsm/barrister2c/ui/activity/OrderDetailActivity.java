package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.CallHistory;
import com.lsm.barrister2c.data.entity.OrderDetail;
import com.lsm.barrister2c.data.entity.OrderItem;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetOrderDetailReq;
import com.lsm.barrister2c.data.io.app.MakeCallReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.adapter.CallHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情页
 */
public class OrderDetailActivity extends BaseActivity {

    AQuery aq;

    OrderItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setupToolbar();

        item = (OrderItem) getIntent().getSerializableExtra("item");

        aq = new AQuery(this);
        aq.id(R.id.btn_call_make).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMakeCall();
            }
        });

        mAddapter = new CallHistoryAdapter(this,calls);

        ListView listView = aq.id(R.id.listview_callhistory).adapter(mAddapter).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //播放？
            }
        }).getListView();

        listView.setEmptyView(getLayoutInflater().inflate(R.layout.empty_call_history,null));

        loadOrderDetail();
    }

    CallHistoryAdapter mAddapter;
    List<CallHistory> calls = new ArrayList<>();

    /**
     * 获取订单详情
     */
    private void loadOrderDetail() {
        new GetOrderDetailReq(this,item.getId()).execute(new Action.Callback<IO.GetOrderDetailResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {

            }

            @Override
            public void onCompleted(IO.GetOrderDetailResult result) {
                bindOrderDetail(result.orderDetail);
            }
        });
    }


    private void bindOrderDetail(OrderDetail detail) {

        //预约时间
//        aq.id(R.id.tv_order_appointment_time).text(detail.getAppointment);

        //订单号
        aq.id(R.id.tv_order_num).text(detail.getOrderNo());
        //支付金额
        aq.id(R.id.tv_order_payment).text("￥"+detail.getPaymentAmount());
        //状态
        aq.id(R.id.tv_order_status).text(detail.getStatus());
        //下单时间
        aq.id(R.id.tv_order_time).text(detail.getPayTime());

        String caseType = detail.getCaseType();
        //案件类型
        aq.id(R.id.tv_order_type).text(caseType);
        //备注
        aq.id(R.id.tv_order_remark).text(detail.getRemarks());

        List<CallHistory> callHistories = detail.getCallHistories();

        if(callHistories !=null && !callHistories.isEmpty()){

            calls.clear();
            calls.addAll(callHistories);
            mAddapter.notifyDataSetChanged();

        }else{

            //show empty?? 没有通话记录
            calls.clear();
            mAddapter.notifyDataSetChanged();

        }
    }

    boolean isWaitingCall = false;
    /**
     * 打电话
     */
    private void doMakeCall() {

        if(isWaitingCall){
            progressDialog.setMessage("请等待回拨");
            progressDialog.show();
            return;
        }

        new MakeCallReq(OrderDetailActivity.this,item.getId()).execute(new Action.Callback<String>() {
            @Override
            public void progress() {
                isWaitingCall = true;
            }

            @Override
            public void onError(int errorCode, String msg) {
                isWaitingCall = false;
                UIHelper.showToast(getApplicationContext(),"拨号失败");
            }

            @Override
            public void onCompleted(String s) {

                progressDialog.setMessage("请等待回拨");
                progressDialog.show();

                AQUtility.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isWaitingCall = false;
                    }
                },60*1000);//60s等待回拨
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_order_detail);
    }

}
